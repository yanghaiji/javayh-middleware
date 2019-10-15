# Springboot2.X 整合 中间件案例

    说明：
    本代码禁用于学习总结,希望大家可以容忍代码存在的bug

## 一、项目结构
    --javayh-middleware                 顶级 
        --javayh-config                 带有dao操作的common
        --javayh-common                 common
        --javayh-mq                     mq组件
            --javayh-rabbitmq           rabbitmq示例
                --javayh-consumer       消费者
                --javayh-producer       生产者
        --javayh-cahe                   常用缓存
            javayh-redis                raedis 示例
       
## 二、Rabbitmq讲解
##### 1.RabbitMQ 简介
##### 2.AMQP模式
##### 3.MQ应用场景
##### 4.RabbitMQ 消息传递流程
##### 5.RabbitMQ可靠性投递，防止重复消费设计
##### 6.配置介绍

详情： 
- [javayh-rabbitmq/README.md](javayh-mq/javayh-rabbitmq/README.md)
  