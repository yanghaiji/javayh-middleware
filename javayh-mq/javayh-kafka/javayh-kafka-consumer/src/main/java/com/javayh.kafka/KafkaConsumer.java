package com.javayh.kafka;

import com.alibaba.fastjson.JSON;
import com.javayh.common.entity.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @author Dylan Yang
 * @Description: KafkaConsumer
 * @Title: KafkaConsumer
 * @ProjectName javayh-cloud
 * @date 2019/7/17 15:53
 */
@Slf4j
@Component
public class KafkaConsumer {

    @Autowired
    private KafkaListenerEndpointRegistry registry;

    /**
     *  消费者
     * @param record 消息
     */
    @Transactional
    @KafkaListener(topics = "#{kafkaTopicName}", groupId = "#{topicGroupId}" )
    public void processMessage(ConsumerRecord<String, String> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            log.info("开始消费");
            log.info("record {}",record);
            log.info("message {}", message);
        }
    }


}

