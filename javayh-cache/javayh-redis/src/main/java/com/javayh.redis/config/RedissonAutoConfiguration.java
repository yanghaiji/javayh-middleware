package com.javayh.redis.config;

import com.javayh.redis.redisson.impl.RedissonDistributedLocker;
import com.javayh.redis.redisson.util.RedissonLockUtil;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName javayh-middleware → com.javayh.redis.config → RedissonAutoConfiguration
 * @Description redis 自动配置
 * @Author Dylan
 * @Date 2019/10/17 17:31
 * @Version
 */
@Configuration
public class RedissonAutoConfiguration {
    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     * @return
     */
    @Bean
    public RedissonDistributedLocker redissonLocker(RedissonClient redissonClient) {
        RedissonDistributedLocker locker = new RedissonDistributedLocker(redissonClient);
        //设置LockUtil的锁处理对象
        RedissonLockUtil.setLocker(locker);
        return locker;
    }
}
