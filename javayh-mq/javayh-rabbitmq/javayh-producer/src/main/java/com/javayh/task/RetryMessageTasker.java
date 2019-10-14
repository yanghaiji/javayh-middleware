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

    /**
     * @Description 启动定时任务
     * @author Dylan
     * @date 2019/9/9
     * @param
     * @return void
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 15000)
    public void reSend(){
        System.out.println("-----------定时任务开始-----------");
        //拉取数据库全部投递中的数据
        List<BrokerMessageLog> list = brokerMessageLogMapper.query4StatusAndTimeoutMessage();
        list.forEach(messageLog -> {
            if(messageLog.getTryCount() >= 3){
                //尝试次数超限后，更新为失败
                brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLog.getMessageId(), FAILURE_DELIVERY, new Date());
                log.info("尝试次数"+messageLog.getTryCount());
            } else {
                // 尝试次数更新
                brokerMessageLogMapper.update4ReSend(messageLog.getMessageId(),  new Date());
                Order reSendOrder = FastJsonConvertUtil.convertJSONToObject(messageLog.getMessage(), Order.class);
                try {
                    //重新发送消息
                    rabbitProducerService.topicSend(reSendOrder);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("异常信息" +e);
                }
            }
        });
    }
}

