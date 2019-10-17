package com.javayh.kafka.controller;

import com.javayh.common.entity.Order;
import com.javayh.common.id.Uid;
import com.javayh.kafka.seriver.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName javayh-middleware → com.javayh.kafka.controller → KafkaController
 * @Description
 * @Author Dylan
 * @Date 2019/10/17 15:55
 * @Version
 */
@RestController
@RequestMapping(value = "/kafka/")
public class KafkaController {
    @Autowired
    private KafkaService kafkaService;

    @GetMapping(value = "send")
    public String send(){
        Order order = Order.builder().id(Uid.getUidInt()).messageId("test").build();
        return kafkaService.send(order);
    }
}
