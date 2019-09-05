package com.javayh.id;

import java.util.UUID;

/**
 * @ClassName javayh-rabbitmq → com.javayh.id → Uid
 * @Description
 * @Author Dylan
 * @Date 2019/9/5 14:40
 * @Version
 */
public class Uid {
    public static String getUid(){
        return UUID.randomUUID().toString();
    }

    public static int getUidInt(){
        return UUID.randomUUID().toString().hashCode();
    }
}
