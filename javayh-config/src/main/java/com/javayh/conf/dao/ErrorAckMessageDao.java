package com.javayh.conf.dao;

import com.javayh.common.entity.ErrorAckMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface ErrorAckMessageDao {

    /**
     * @Description 插入错误ack日志
     * @author Dylan
     * @date 2019/9/9
     * @param errorAckMessage
     * @return void
     */
    void insertAll(ErrorAckMessage errorAckMessage);
}
