package com.javayh.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.javayh.constants.StaticNumber.JAVAYOHO_TOPIC;
import static com.javayh.constants.StaticNumber.TOPIC;
import static com.javayh.constants.StaticNumber.TOPIC_EXCHANGE;
import static com.javayh.constants.StaticNumber.YHJ_TOPIC;

/**
 * @ClassName javayh-rabbitmq → com.javayh.config → TopicRabbitConfig
 * @Description topic配置
 * @Author Dylan
 * @Date 2019/9/5 14:12
 * @Version
 */
@Configuration
public class TopicRabbitConfig {

    /**
     * @Description 创建队列
     * @author Dylan
     * @date 2019/9/5
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean("queueMessage")
    public Queue queueMessage() {//默认开启
        return new Queue(JAVAYOHO_TOPIC,true);//rounting-key 匹配规则, 是否开启持久化
    }

    /**
     * @Description 创建队列
     * @author Dylan
     * @date 2019/9/5
     * @param
     * @return org.springframework.amqp.core.Queue
     */
    @Bean("queueMessages")
    public Queue queueMessages() {
        return new Queue(YHJ_TOPIC,true);
    }

    /**
     * @Description 创建交换机
     * @UserModule:
     * @author Dylan
     * @date 2019/9/5
     * @param
     * @return org.springframework.amqp.core.Exchange
     */
    @Bean
    Exchange exchange() {
        return ExchangeBuilder.topicExchange(TOPIC_EXCHANGE).durable(true).build();
    }

    /**
     * @Description 绑定交换机
     * @author Dylan
     * @date 2019/9/5
     * @param queueMessage
     * @param exchange
     * @return org.springframework.amqp.core.Binding
     */
    @Bean
    Binding bindingExchangeMessage(Queue queueMessage, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessage).to(exchange).with(JAVAYOHO_TOPIC);
    }

    /**
     * @Description 绑定交换机
     * @author Dylan
     * @date 2019/9/5
     * @param queueMessages
     * @param exchange
     * @return org.springframework.amqp.core.Binding
     */
    @Bean
    Binding bindingExchangeMessages(Queue queueMessages, TopicExchange exchange) {
        return BindingBuilder.bind(queueMessages).to(exchange).with(TOPIC);
    }
}

