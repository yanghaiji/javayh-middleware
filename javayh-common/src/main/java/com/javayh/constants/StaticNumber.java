package com.javayh.constants;

/**
 * @author Dylan Yang
 * @Description: StaticNumber
 * @Title: StaticNumber
 * @ProjectName javayh-common
 * @date 2019/7/17 15:15
 */
public interface StaticNumber {

    /*Direct 方式*/
    //保存用户-交换机名称
    String SAVE_USER_EXCHANGE_NAME = "direct.exchange.name";
    //保存用户-队列名称
    String SAVE_USER_QUEUE_NAME = "save.queue.name";
    //保存用户-队列路由键
    String SAVE_USER_QUEUE_ROUTE_KEY = "save.queue.route.key";

    /*Fanout 方式*/
    /*队列名*/
    String JAVAYOHO_QUEUE = "javayoho";
    String DYLAN_QUEUE = "dylan";
    String YHJ_QUEUE = "yanghaiji";
    /*交换机名*/
    String JAVAYH_EXCHANGE = "javayh.exchange";

    /*主题模式*/
    String JAVAYOHO_TOPIC = "topic.javayh";
    String YHJ_TOPIC = "topic.yhj";
    String TOPIC = "topic.#";
    String TOPIC_EXCHANGE = "javayh.topic";
    String TOPIC_KEY = "javayh.topic.key";

    /*  生产者  */
    /*资源投递中*/
    String IN_DELIVERY = "0";
    /*投递成功*/
    String SUCCESSFUL_DELIVERY = "1";
    /*投递失败*/
    String FAILURE_DELIVERY = "2";

    /*   消费者   */
    /*消费成功*/
    String CONSUMER_SUCCESSFUL = "1";
    /*消费失败（为尝试）*/
    String CONSUMER_FAILURE = "2";
    /*消费失败（达到最大尝试次数）*/
    String CONSUMER_TRY_FAILURE = "3";

    String YMDHMS = "yyyy-MM-dd HH:mm:ss";
}
