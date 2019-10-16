package com.javayh.common.id;

import java.util.UUID;

/**
 * @ClassName javayh-rabbitmq → com.javayh.id → Uid
 * @Description
 * @Author Dylan
 * @Date 2019/9/5 14:40
 * @Version
 */
public class Uid {

    /**
     * @Description 获取全局uid
     * @author Dylan
     * @date 2019/9/9
     * @param
     * @return java.lang.String
     */
    public static String getUid(){
        return UUID.randomUUID().toString();
    }

    /**
     * @Description 获取全局uid hash值
     * @UserModule: exam-web-paper
     * @author Dylan
     * @date 2019/9/9
     * @param
     * @return int
     */
    public static int getUidInt(){
        return getUid().hashCode();
    }
}
