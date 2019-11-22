package com.javayh;

import com.javayh.redis.redisson.util.RedissonLockUtil;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName javayh-middleware → com.javayh → TestRedis
 * @Description
 * @Author Dylan
 * @Date 2019/11/11 13:48
 * @Version
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
    static String string = "javayh";

    static Thread thread = new Thread(() -> {
        boolean b = RedissonLockUtil.tryAcquire(string, 6, 10);
        if(b){
            System.out.println("thread 获取成功,可以去做你想要的的业务了"+Thread.class.getName());
        }else {
            System.out.println("thread 获取资源失败，请稍等"+Thread.class.getName());
        }


    });

    static Thread thread2 = new Thread(() -> {
        boolean b = RedissonLockUtil.tryAcquire(string, 6, 10);
        if(b){
            System.out.println("thread2 获取成功,可以去做你想要的的业务了"+Thread.class.getName());
        }else {
            System.out.println(" thread2 获取资源失败，请稍等"+Thread.class.getName());
        }
    });

    public static void main(String[] args) {
        boolean da = RedissonLockUtil.trySetRate(string, 6, 1);
        if(da){
            thread.start();
            thread2.start();
        }
    }
}
