package com.javayh.entity;

import lombok.Data;

import java.util.Date;
@Data
public class BrokerMessageLog {
    private String messageId;

    private String message;

    private Integer tryCount;

    private String status;

    private Date nextRetry;

    private Date createTime;

    private Date updateTime;

}

