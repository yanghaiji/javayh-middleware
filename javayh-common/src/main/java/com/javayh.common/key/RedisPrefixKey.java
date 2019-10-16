package com.javayh.common.key;

import static com.javayh.common.constants.StaticNumber.PREFIXKEY;

/**
 * @ClassName javayh-middleware → com.javayh.key → RedisPrefixKey
 * @Description redis 前缀
 * @Author Dylan
 * @Date 2019/10/15 17:49
 * @Version
 */
public class RedisPrefixKey {

    /**
     * @Description 设置前缀
     * @author Dylan
     * @date 2019/10/15
     * @param key
     * @return java.lang.String
     */
    public static String prefixKey(String key){
        return PREFIXKEY + key;
    }
}
