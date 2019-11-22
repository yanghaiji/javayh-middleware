package com.javayh;


import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName javayh-rabbitmq → com.javayh → TestMain
 * @Description
 * @Author Dylan
 * @Date 2019/9/16 15:51
 * @Version
 */
@Slf4j
@MapperScan(value = "com.javayh.conf.*")
//@ComponentScan(basePackages = {"com.javayh.*"})
@SpringBootApplication
public class DruidApp {
    public static void main(String[] args) {
        SpringApplication.run(DruidApp.class,args);
        log.info("DruidApp 启动成功");
    }
}
