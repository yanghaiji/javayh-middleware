package com.javayh.conf.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName javayh-middleware → com.javayh.mq → RabbitSender
 * @Description Rabbitmq 发送
 * @Author Dylan
 * @Date 2019/10/15 13:32
 * @Version
 */
@Slf4j
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * @Description 实例化 rabbitTemplate，进行统一处理
     * @author Dylan
     * @date 2019/9/12
     * @param
     * @return org.springframework.amqp.rabbit.core.RabbitTemplate
     */
    public <T> void sendExchange(String exchangeName, String routingKey, T t,CorrelationData correlationData) {
        this.rabbitTemplate.convertAndSend(exchangeName, routingKey, t, correlationData);
    }
}
