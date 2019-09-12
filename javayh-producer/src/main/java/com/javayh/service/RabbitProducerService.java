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

import static com.javayh.constants.StaticNumber.IN_DELIVERY;
import static com.javayh.constants.StaticNumber.JAVAYOHO_TOPIC;
import static com.javayh.constants.StaticNumber.SUCCESSFUL_DELIVERY;
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
public class RabbitProducerService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

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
        brokerMessageLog.setStatus(IN_DELIVERY);
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

}
