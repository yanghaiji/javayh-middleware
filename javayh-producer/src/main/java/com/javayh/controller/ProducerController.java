package com.javayh.controller;

import com.javayh.entity.Order;
import com.javayh.id.Uid;
import com.javayh.service.RabbitProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName javayh-rabbitmq → com.javayh.controller → TestController
 * @Description
 * @Author Dylan
 * @Date 2019/9/5 16:55
 * @Version
 */
@Slf4j
@RestController
public class ProducerController {
    @Autowired
    private RabbitProducerService rabbitProducerService;

    /**
     * @Description 测试消息发送
     * @UserModule: exam-web-paper
     * @author Dylan
     * @date 2019/9/5
     * @param
     * @return void
     */
    @GetMapping("send")
    public void send(){
        Order order= Order.builder().id(Uid.getUidInt()).messageId(Uid.getUid()).name(Uid.getUid()).build();
        rabbitProducerService.topicSend(order);
    }
}
