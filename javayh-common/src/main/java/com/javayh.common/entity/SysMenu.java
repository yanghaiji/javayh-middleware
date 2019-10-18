package com.javayh.common.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author Dylan Yang
 * @Description: SysMenu
 * @Title: SysMenu
 * @ProjectName javayh-oauth2
 * @date 2019/5/19 18:29
 */
@Data
@Builder
@Document
public class SysMenu implements Serializable {
    @Id
    private String id;
    private String code;
    private String pcode;

}

