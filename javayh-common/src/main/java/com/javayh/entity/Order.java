package com.javayh.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Order implements Serializable {
    private static final long serialVersionUID = 2174447427263683506L;
    private int id;
    private String name;
    private String messageId;

}
