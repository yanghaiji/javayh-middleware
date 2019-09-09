package com.javayh.receive;

import com.javayh.constants.AckAction;
import com.javayh.constants.FastJsonConvertUtil;
import com.javayh.dao.ErrorAckMessageDao;
import com.javayh.entity.ErrorAckMessage;
import com.javayh.entity.Order;
import com.javayh.redis.RedisUtil;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import static com.javayh.constants.StaticNumber.CONSUMER_FAILURE;
import static com.javayh.constants.StaticNumber.CONSUMER_TRY_FAILURE;
import static com.javayh.constants.StaticNumber.JAVAYOHO_TOPIC;
import static com.javayh.constants.StaticNumber.YMDHMS;

/**
 * @author Dylan Yang
 * @Description: TopicService
 * @Title: TopicService
 * @ProjectName javayh-cloud
 * @date 2019/7/20 17:12
 */
@Slf4j
@Component
@RabbitListener(queues = JAVAYOHO_TOPIC)
public class TopicService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ErrorAckMessageDao errorAckMessageDao;

    /*类名*/
    String  className = this.getClass().getName();

    @RabbitHandler
    public void receiveMessage(@Payload Order order, @Headers Map<String,Object> headers, Channel channel) throws IOException {
        /**
         * Delivery Tag 用来标识信道中投递的消息。RabbitMQ 推送消息给 Consumer 时，会附带一个 Delivery Tag，
         * 以便 Consumer 可以在消息确认时告诉 RabbitMQ 到底是哪条消息被确认了。
         * RabbitMQ 保证在每个信道中，每条消息的 Delivery Tag 从 1 开始递增。
         */
        Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        /**
         *  multiple 取值为 false 时，表示通知 RabbitMQ 当前消息被确认
         *  如果为 true，则额外将比第一个参数指定的 delivery tag 小的消息一并确认
         */
        boolean multiple = false;
        /*应答模式*/
        String ackAction = AckAction.ACK_SUCCESSFUL;
        try{
            //消费者操作
            log.info("---------收到消息，开始消费---------");
            log.info("订单ID："+order.getId());
            String messageId = order.getMessageId();
            Integer.valueOf(messageId);
        }catch (Exception e){
            //这里需要根据也无需求，看错误方式是否可以重新入队
            //需要考虑全面，否则会造成MQ阻塞，一直循环调用
            String message = e.getMessage();
            log.info(message);
            //添加尝试次数显示，达到最大限制次数，消费依旧失败，
            //不在进行尝试，存入库中，后期手动维护
            if(message == null){//直接入库
                ackAction = AckAction.ACK_REJECT;
                ErrorAckMessage errorAckMessage =
                        ErrorAckMessage.builder().
                                        id(order.getMessageId()).
                                        errorMethod(className+"."+Thread.currentThread().getStackTrace()[1].getMethodName()).
                                        errorMessage(message).
                                        createTime(DateFormatUtils.format(new Date(),YMDHMS)).
                                        status(CONSUMER_FAILURE).
                                        message(FastJsonConvertUtil.convertObjectToJSON(order)).
                                        remarks("消费失败，为入队，请手动处理").
                                        build();
                errorAckMessageDao.insertAll(errorAckMessage);
            }
            if(message != null){//达到最大次数入库
                ackAction = AckAction.ACK_RETRY;
                long incr = redisUtil.incr(order.getMessageId(), 1);
                //重复尝试入队三次，在此消费失败将放弃入队
                if(incr>3){
                    ackAction = AckAction.ACK_REJECT;
                    ErrorAckMessage errorAckMessage =
                            ErrorAckMessage.builder().
                                    id(order.getMessageId()).
                                    errorMethod(className+"."+Thread.currentThread().getStackTrace()[1].getMethodName()).
                                    errorMessage(message).
                                    createTime(DateFormatUtils.format(new Date(),YMDHMS)).
                                    status(CONSUMER_TRY_FAILURE).
                                    message(FastJsonConvertUtil.convertObjectToJSON(order)).
                                    remarks("消费失败，入队尝试次数达到最大次数，请手动处理").
                                    build();
                    errorAckMessageDao.insertAll(errorAckMessage);
                }
            }
        }finally {
            //消费失败被拒绝 应答模式
            //如果设置为true ，则会添加在队列的末端
            //channel.basicNack 消费失败重新入队 多条
            //channel.basicReject 消费失败重新入队 只能操作单条
            //channel.basicReject(deliveryTag,true);
            // 通过finally块来保证Ack/Nack会且只会执行一次
            if (ackAction == AckAction.ACK_SUCCESSFUL) {
                //ACK,确认一条消息已经被消费
                channel.basicAck(deliveryTag,multiple);
            } else if (ackAction == AckAction.ACK_RETRY) {//重新加入队列
                channel.basicNack(deliveryTag, false, true);
            } else {//放弃入队，避免消息丢失，入库处理，后期可手动维护
                try {
                    channel.basicNack(deliveryTag, false, false);
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        /*
         * “channel.basicQos(10)” 这个方法来设置当前channel的prefetch count。
         * 也可以通过配置文件设置: spring.rabbitmq.listener.simple.prefetch=10
         * RabbitMQ官方给出的建议是prefetch count一般设置在100 - 300之间。
         * 也就是一个消费者服务最多接收到100 - 300个message来处理，允许处于unack状态。
         * 这个状态下可以兼顾吞吐量也很高，同时也不容易造成内存溢出的问题。
         */
//        channel.basicQos(150);
    }
}

