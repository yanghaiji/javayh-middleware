package com.javayh.exception;

/**
 * @ClassName javayh-rabbitmq → com.javayh.exception → ServiceException
 * @Description 业务逻辑异常
 * @Author Dylan
 * @Date 2019/9/12 17:38
 * @Version
 */
public class ServiceException extends RuntimeException{

    private String code;
    private String msg;

    public ServiceException() {
    }

    public ServiceException(String msg) {
        this.msg = msg;
    }

    public ServiceException(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
