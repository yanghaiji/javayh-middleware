package com.javayh.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Order implements Serializable {
    private int id;
    private String name;
    private String messageId;

}
