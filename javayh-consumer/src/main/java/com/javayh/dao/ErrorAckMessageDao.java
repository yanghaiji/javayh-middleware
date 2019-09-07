package com.javayh.dao;

import com.javayh.entity.ErrorAckMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErrorAckMessageDao {

    void insertAll(ErrorAckMessage errorAckMessage);
}
