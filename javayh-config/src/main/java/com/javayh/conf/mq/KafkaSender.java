package com.javayh.conf.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

/**
 * @ClassName javayh-middleware → com.javayh.conf.mq → KafkaSender
 * @Description kafk统一发送
 * @Author Dylan
 * @Date 2019/10/17 15:44
 * @Version
 */
@Slf4j
@Component
public class KafkaSender {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    /**
     * @Description 发送到topic
     * @author Dylan
     * @date 2019/10/17
     * @param topic
     * @param msg
     * @return void
     */
    public void sendTopic(String topic,String msg){
        ListenableFuture send = kafkaTemplate.send(topic,msg);
        send.addCallback(new ListenableFutureCallback(){
            @Override
            public void onSuccess(Object result) {
                log.info("send success");
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("send failure");
            }
        });
        log.info("send success");
    }
}
