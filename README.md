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
            --javayh-redis                raedis 示例
       
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


  