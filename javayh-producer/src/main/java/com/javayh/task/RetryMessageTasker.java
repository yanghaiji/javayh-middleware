package com.javayh.task;

import com.javayh.constants.FastJsonConvertUtil;
import com.javayh.dao.BrokerMessageLogMapper;
import com.javayh.entity.BrokerMessageLog;
import com.javayh.entity.Order;
import com.javayh.service.RabbitProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

import static com.javayh.constants.StaticNumber.FAILURE_DELIVERY;

@Slf4j
@Component
public class RetryMessageTasker {


    @Autowired
    private RabbitProducerService rabbitProducerService;

    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    @Scheduled(initialDelay = 5000, fixedDelay = 15000)

    public void reSend(){
        System.out.println("-----------定时任务开始-----------");
        //pull status = 0 and timeout message
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        list.forEach(messageLog -> {
            if(messageLog.getTryCount() >= 3){
                //update fail message
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), FAILURE_DELIVERY, new Date());
                log.info("尝试次数"+messageLog.getTryCount());
            } else {
                // resend
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
                Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
                try {
                    rabbitProducerService.topicSend(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("-----------异常处理-----------");
                }
            }
        });
    }
}

