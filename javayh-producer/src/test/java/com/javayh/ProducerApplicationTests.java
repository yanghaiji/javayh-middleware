package com.javayh;

import com.javayh.entity.Order;
import com.javayh.id.Uid;
import com.javayh.service.RabbitProducerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProducerApplicationTests {

    @Autowired
    private RabbitProducerService rabbitProducerService;

    @Test
    public void contextLoads() {
        Order order= Order.builder().id(Uid.getUidInt()).messageId(Uid.getUid()).name(Uid.getUid()).build();
        rabbitProducerService.topicSend(order);
    }

}
