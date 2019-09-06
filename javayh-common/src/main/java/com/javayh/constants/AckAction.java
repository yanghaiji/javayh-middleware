package com.javayh.constants;

/**
 * @ClassName javayh-rabbitmq → com.javayh.constants → AckAction
 * @Description ack应答拒绝模式
 * @Author Dylan
 * @Date 2019/9/6 16:52
 * @Version
 */
public interface AckAction {
    /*消费成功*/
    String ACK_SUCCESSFUL = "success";
    /*重新入队*/
    String ACK_RETRY = "retry";
    /*拒绝入队*/
    String ACK_REJECT = "reject";
}
