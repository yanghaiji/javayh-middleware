package com.javayh.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
public class ErrorAckMessage implements Serializable {
    private String id;
    private String errorMethod;
    private String errorMessage;
    private String status;
    private String message;
    private Date createTime;
    private Date modifyTime;
    private String remarks;
}
