package com.javayh.kafka.seriver;

import cn.hutool.json.JSONUtil;
import com.javayh.common.entity.Order;
import com.javayh.conf.mq.KafkaSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.javayh.common.constants.StaticNumber.JAVAYOHO;

/**
 * @ClassName javayh-middleware → com.javayh.kafka.seriver → KafkaService
 * @Description
 * @Author Dylan
 * @Date 2019/10/17 15:50
 * @Version
 */
@Slf4j
@Service
public class KafkaService {

    @Autowired
    private KafkaSender kafkaSender;

    /**
     * 发送消息
     * @param order
     * @return
     */
    public String send(Order order){
        kafkaSender.sendTopic(JAVAYOHO, JSONUtil.toJsonStr(order));
        return "kafka";
    }
}
