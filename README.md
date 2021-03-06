# Springboot2.X 整合 中间件案例

    说明：
    本代码禁用于学习总结,希望大家可以容忍代码存在的bug

## 一、项目结构
    --javayh-middleware                 顶级 
        --javayh-config                 带有dao操作的common
        --javayh-common                 common
        --javayh-mq                     mq组件
            --javayh-rabbitmq           rabbitmq示例
                --javayh-rabbitmq-consumer          消费者
                --javayh-rabbitmq-producer          生产者
            --javayh-kafka              kafka示例            
                --javayh-kafka-consumer             消费者
                --javayh-kafka-producer             生产者
        --javayh-cahe                    常用缓存
            --javayh-redis               raedis  示例
            --javayh-mongo               mongodb 示例
        --javayh-xxl-job
            --javayh-xxl-job-admin       job-web端
            --javayh-xxl-job-core        核心依赖
            --javayh-xxl-job-executor-samples       定时任务
            --javayh-xxl-job-executor-sample        定时任务示例
        --javayh-monitor                监控平台
            --javayh-druid              Druid监控    
       
## 二、Rabbitmq示例
### 1.目录
- RabbitMQ 简介
- AMQP模式
- MQ应用场景
- RabbitMQ 消息传递流程
- RabbitMQ可靠性投递，防止重复消费设计
- 配置介绍
### 2.代码示例
- 消息发送

        public void topicSend(Order order){
            Date orderTime = new Date();
            orderMapper.insert(order);
            BrokerMessageLog brokerMessageLog = new BrokerMessageLog();
            brokerMessageLog.setMessageId(order.getMessageId());
            brokerMessageLog.setMessage(FastJsonConvertUtil.convertObjectToJSON(order));
            // 设置消息状态为0 表示发送中
            brokerMessageLog.setStatus(IN_DELIVERY);
            // 设置消息未确认超时时间窗口为 一分钟
            brokerMessageLog.setNextRetry(DateUtils.addMinutes(orderTime,1*60000));
            brokerMessageLog.setCreateTime(new Date());
            brokerMessageLog.setUpdateTime(new Date());
            brokerMessageLogMapper.insertSelective(brokerMessageLog);
            //构建回调返回的数据
            CorrelationData correlationData = new CorrelationData();
            correlationData.setId(order.getMessageId());
            rabbitSender.sendExchange(TOPIC_EXCHANGE,JAVAYOHO_TOPIC,order,correlationData);
        }
        
- 消息消费

        @RabbitListener(queues = JAVAYOHO_TOPIC)
        @RabbitHandler
        public void receiveMessage(@Payload Order order, @Headers Map<String,Object> headers,
            Channel channel) throws IOException {   
            Long deliveryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
             boolean multiple = false;
             /*应答模式*/
             String ackAction = AckAction.ACK_SUCCESSFUL;
             log.info("---------收到消息，开始消费---------");
             log.info("订单ID："+order.getId());
             channel.basicAck(deliveryTag,multiple);
             log.info("cheng");
             try{
             //消费者操作
             log.info("---------收到消息，开始消费---------");
             log.info("订单ID："+order.getId());
             String messageId = order.getMessageId();
                }catch (Exception e){
                    String message = e.getMessage();
                    log.info(message);
                    if(message == null){//直接入库
                        ackAction = AckAction.ACK_REJECT;
                    }
                    if(message != null){//达到最大次数入库
                        ackAction = AckAction.ACK_RETRY;
                        }
                    }
                }finally {
                    if (ackAction == AckAction.ACK_SUCCESSFUL) {
                        //ACK,确认一条消息已经被消费
                        channel.basicAck(deliveryTag,multiple);
                        log.info("cheng");
                    } else if (ackAction == AckAction.ACK_RETRY) {//重新加入队列
                        channel.basicNack(deliveryTag, false, true);
                    } else {//放弃入队，避免消息丢失，入库处理，后期可手动维护
                        try {
                            channel.basicNack(deliveryTag, false, false);
                            Thread.sleep(2000L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } 
### 3.详情：
 - [RabbitMQ详细介绍](javayh-mq/javayh-rabbitmq/README.md)
 
## 三、Redis示例

### 1.目录
- Redis数据结构
- 持久化方式
- 配置文件

### 2.代码示例
     根据key获取value
     redisUtil.get("javayh");
     
     将数据保存,并设置过期时间
     redisUtil.set("javayh","javayh",10);
     
     根据key获取锁,最大等待10s,10s后自动解锁
     RedissonLockUtil.tryLock("javayh", TimeUnit.SECONDS,10,10);

### 3.详情：
 - [Redis学习示例](javayh-cache/javayh-redis/README.md)
 ## 关注 Java有货领取更多资料
![在这里插入图片描述](https://img-blog.csdnimg.cn/20191112104505246.jpg)
**如遇到问题可以联系小编。微信：372787553，互相学习**

**技术博客：[https://blog.csdn.net/weixin_38937840](https://blog.csdn.net/weixin_38937840)**

**SpringCloud学习代码： [https://github.com/Dylan-haiji/javayh-cloud](https://github.com/Dylan-haiji/javayh-cloud)**

**Redis、Mongo、Rabbitmq、Kafka学习代码： [https://github.com/Dylan-haiji/javayh-middleware](https://github.com/Dylan-haiji/javayh-middleware)**

**AlibabaCloud学习代码：[https://github.com/Dylan-haiji/javayh-cloud-nacos](https://github.com/Dylan-haiji/javayh-cloud-nacos)**
**SpringBoot+SpringSecurity实现自定义登录学习代码：[https://github.com/Dylan-haiji/javayh-distribution](https://github.com/Dylan-haiji/javayh-distribution)**

  
