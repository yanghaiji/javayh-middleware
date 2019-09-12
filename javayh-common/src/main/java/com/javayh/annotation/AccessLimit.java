package com.javayh.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName javayh-rabbitmq → com.javayh.annotation → AccessLimit
 * @Description 实现接口幂等性
 * @Author Dylan
 * @Date 2019/9/12 17:14
 * @Version
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AccessLimit {

    int maxCount();// 最大访问次数

    int seconds();// 固定时间, 单位: s
}
