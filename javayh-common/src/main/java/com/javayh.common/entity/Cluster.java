package com.javayh.common.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName javayh-rabbitmq → com.javayh.entity → Cluster
 * @Description 集群
 * @Author Dylan
 * @Date 2019/9/27 17:10
 * @Version
 */
@Data
public abstract class Cluster {

    public List<Node> nodes;

    public Cluster() {
        this.nodes = new ArrayList<>();
    }

    public abstract void addNode(Node node);

    public abstract void removeNode(Node node);

    public abstract Node get(String key);
}