package com.javayh.constants;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.javayh.entity.Order;
/**
 * @author Dylan Yang
 * @Description: StaticNumber
 * @Title: StaticNumber
 * @ProjectName javayh-common
 * @date 2019/7/17 15:15
 */
public class FastJsonConvertUtil {

    /**
     * @Description 将字符串转换成实体
     * @author Dylan
     * @date 2019/9/9
     * @param message
     * @param obj
     * @return com.javayh.entity.Order
     */
    public static Order convertJSONToObject(String message, Object obj){
        Order order = JSON.parseObject(message, new TypeReference<Order>() {});
        return order;
    }

    /**
     * @Description 将object转换成string
     * @author Dylan
     * @date 2019/9/9
     * @param obj
     * @return java.lang.String
     */
    public static String convertObjectToJSON(Object obj){
        String text = JSON.toJSONString(obj);
        return text;
    }
}
