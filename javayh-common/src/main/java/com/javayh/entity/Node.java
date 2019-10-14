package com.javayh.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Map;

/**
 * @ClassName javayh-rabbitmq → com.javayh.entity → Node
 * @Description
 * @Author Dylan
 * @Date 2019/9/27 17:07
 * @Version
 */
@Data
public class Node implements Serializable {
    //域名
    private String domain;
    //ip
    private String ip;
    //数据
    private Map<String, Object> data;

    public Node(String domain, String ip) {
        this.domain = domain;
        this.ip = ip;
    }

    /*
     * @Description 新增
     * @author Dylan
     * @date 2019/9/27
     * @param key
     * @param value
     * @return void
     */
    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    /**
     * @Description 删除
     * @author Dylan
     * @date 2019/9/27
     * @param key
     * @return void
     */
    public void remove(String key){
        data.remove(key);
    }

    /**
     * @Description 获取
     * @author Dylan
     * @date 2019/9/27
     * @param key
     * @return T
     */
    public <T> T get(String key) {
        return (T) data.get(key);
    }
}
