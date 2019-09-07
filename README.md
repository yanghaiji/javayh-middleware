# Springboot2.X 整合RabbitMq 实现消息持久化

## 一、RabbitMQ 简介
官方推出的六种模式
### 1.1 "Hello World!" 
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190720145646828.png)
简单模式 一对一生产消费
### 1.2 Work Queues
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190720145947947.png)
一个生产者对应多个消费队列
默认情况下，RabbitMQ将按顺序将每条消息发送给下一个消费者。平均而言，每个消费者将获得相同数量的消息
### 1.3 Publish/Subscribe
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019072015044025.png)
订阅发布：多个队列订阅一个交换机，每个队列都会接收到自己订阅的交换机
### 1.4 Routing
![在这里插入图片描述](https://img-blog.csdnimg.cn/2019072015100250.png)
路由模式：对消息进行过滤，把控消费队列获取消息的信息量
### 1.5 Topics
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190720151402627.png)

## 二、SQL
-- ----------------------------
-- Table structure for broker_message_log
-- ----------------------------
    DROP TABLE IF EXISTS `broker_message_log`;
    CREATE TABLE `broker_message_log` (
      `message_id` varchar(255) NOT NULL COMMENT '消息唯一ID',
      `message` varchar(4000) NOT NULL COMMENT '消息内容',
      `try_count` int(4) DEFAULT '0' COMMENT '重试次数',
      `status` varchar(10) DEFAULT '' COMMENT '消息投递状态 0投递中，1投递成功，2投递失败',
      `next_retry` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP COMMENT '下一次重试时间',
      `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
      `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
      PRIMARY KEY (`message_id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
    DROP TABLE IF EXISTS `t_order`;
    CREATE TABLE `t_order` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `name` varchar(255) DEFAULT NULL,
      `message_id` varchar(255) DEFAULT NULL,
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=2018091102 DEFAULT CHARSET=utf8;
   
    CREATE TABLE `error_ack_message` (
    `id`  varchar(64) NOT NULL ,
    `error_method`  varchar(512) NULL COMMENT '错误方法' ,
    `error_message`  varchar(255) NULL COMMENT '错误信息' ,
    `status`  varchar(2) NOT NULL COMMENT '状态' ,
    `message`  varchar(512) NOT NULL COMMENT '消息实体' ,
    `create_time`  date NOT NULL DEFAULT 创建时间 ,
    `modify_time`  date NULL DEFAULT 修改时间 ,
    `remarks`  varchar(256) NOT NULL COMMENT '备注' ,
    PRIMARY KEY (`id`)
    )
    ;


## 三、配置介绍
    rabbitmq:
      addresses: ip+port
      username: name
      password: pwd
      #虚拟主机(一个RabbitMQ服务可以配置多个虚拟主机，
      #每一个虚拟机主机之间是相互隔离，相互独立的，
      #授权用户到指定的virtual-host就可以发送消息到指定队列)
      virtual-host: dev
      publisher-returns: true #返回确认
      publisher-confirms: true # 成功确认
      ## 消费端配置
      listener:
        simple:
          concurrency: 1 # 最小消费者数量
          acknowledge-mode: manual #手动消费，默认是自动消费
          max-concurrency: 10 # 最大消费者数量
          prefetch: 1
          retry:
            enabled: true     #是否支持重试 默认支持

### 3.1 Auto
    1. 如果消息成功被消费（成功的意思是在消费的过程中没有抛出异常），则自动确认
    2. 当抛出 AmqpRejectAndDontRequeueException 异常的时候，则消息会被拒绝，且 requeue = false（不重新入队列）
    3. 当抛出 ImmediateAcknowledgeAmqpException 异常，则消费者会被确认
    4. 其他的异常，则消息会被拒绝，且 requeue = true，此时会发生死循环，可以通过 setDefaultRequeueRejected（默认是true）去设置抛弃消息
    5. 如设置成manual手动确认，一定要对消息做出应答，否则rabbit认为当前队列没有消费完成，将不再继续向该队列发送消息。
    6. channel.basicAck(long,boolean); 确认收到消息，消息将被队列移除，false只确认当前consumer一个消息收到，true确认所有consumer获得的消息。
    7. channel.basicNack(long,boolean,boolean); 确认否定消息，第一个boolean表示一个consumer还是所有，第二个boolean表示requeue是否重新回到队列，true重新入队。
    8. channel.basicReject(long,boolean); 拒绝消息，requeue=false 表示不再重新入队，如果配置了死信队列则进入死信队列。
    9. 当消息回滚到消息队列时，这条消息不会回到队列尾部，而是仍是在队列头部，这时消费者会又接收到这条消息，如果想消息进入队尾，须确认消息后再次发送消息。