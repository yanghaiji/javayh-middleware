package com.javayh.dao;

import com.javayh.entity.ErrorAckMessage;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
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
