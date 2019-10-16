package com.javayh.common.entity;

import lombok.Data;

import static java.util.Objects.hash;

/**
 * @ClassName javayh-rabbitmq → com.javayh.entity → NormalHashCluster
 * @Description 取模
 * @Author Dylan
 * @Date 2019/9/27 17:11
 * @Version
 */
@Data
public class NormalHashCluster extends Cluster {

    public NormalHashCluster() {
        super();
    }

    @Override
    public void addNode(Node node) {
        this.nodes.add(node);
    }

    @Override
    public void removeNode(Node node) {
        this.nodes.removeIf(o -> o.getIp().equals(node.getIp()) ||
                o.getDomain().equals(node.getDomain()));
    }

    @Override
    public Node get(String key) {
        long hash = hash(key);
        long index =  hash % nodes.size();
        return nodes.get((int)index);
    }
}