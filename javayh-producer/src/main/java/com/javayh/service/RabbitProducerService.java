package com.javayh.service;

import com.javayh.constants.FastJsonConvertUtil;
import com.javayh.dao.BrokerMessageLogMapper;
import com.javayh.dao.OrderMapper;
import com.javayh.entity.BrokerMessageLog;
import com.javayh.entity.Order;
import com.javayh.id.Uid;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import java.util.Date;

import static com.javayh.constants.StaticNumber.JAVAYOHO_TOPIC;
import static com.javayh.constants.StaticNumber.TOPIC_EXCHANGE;

/**
 * @ClassName javayh-rabbitmq → com.javayh.service → RabbitProducerService
 * @Description
 * @Author Dylan
 * @Date 2019/9/5 14:32
 * @Version
 */
@Slf4j
@Service
public class RabbitProducerService implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);            //指定 ConfirmCallback
        rabbitTemplate.setReturnCallback(this);             //指定 ReturnCallback

    }

    /**
     * @Description 发送消息
     * @UserModule: exam-web-paper
     * @author Dylan
     * @date 2019/9/5
     * @param order
     * @return void
     */
    public void topicSend(Order order){
        Date orderTime = new Date();
        orderMapper.insert(order);
        BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
        brokerMessageLog.setMessageId(order.getMessageId());
        brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
        // 设置消息状态为0 表示发送中
        brokerMessageLog.setStatus("0");
        // 设置消息未确认超时时间窗口为 一分钟
        brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime,1*60000));
        brokerMessageLog.setCreateTime(new Date());
        brokerMessageLog.setUpdateTime(new Date());
        brokerMessageLogMapper.insertSelective(brokerMessageLog);
        //构建回调返回的数据
        CorrelationData correlationData = new CorrelationData();
        correlationData.setId(order.getMessageId());
       rabbitTemplate.convertAndSend(TOPIC_EXCHANGE,JAVAYOHO_TOPIC,order,correlationData);
    }

    /**
     * @Description  只确认消息是否正确到达 Exchange 中
     * @author Dylan
     * @date 2019/9/5
     * @param correlationData
     * @param ack
     * @param cause
     * @return void
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        String messageId = correlationData.getId();
        if (ack) {
            log.info("消息发送成功" + correlationData);
            //如果confirm返回成功 则进行更新
            brokerMessageLogMapper.changeBrokerMessageLogStatus(messageId, "1", new Date());
        } else {
            log.info("消息发送失败:" + cause);
        }
    }

    /**
     * @Description  消息没有正确到达队列时触发回调，如果正确到达队列不执行
     * @author Dylan
     * @date 2019/9/5
     * @param message
     * @param replyCode
     * @param replyText
     * @param exchange
     * @param routingKey
     * @return void
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText,
                                String exchange, String routingKey) {
        // 反序列化对象输出
        log.info("消息主体: " + message);
        log.info("应答码: " + replyCode);
        log.info("描述：" + replyText);
        log.info("消息使用的交换器 exchange : " + exchange);
        log.info("消息使用的路由键 routing : " + routingKey);
    }

}
