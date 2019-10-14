package com.javayh.config;

import com.javayh.redisson.impl.RedissonDistributedLocker;
import com.javayh.redisson.util.RedissonLockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

/**
 * @author Dylan Yang
 * @Description: redis配置
 * @ProjectName javayh-middleware
 * @date 2019-10-14 22:05
 */
@Configuration
@PropertySource(value = "classpath:application.yml")
public class RedissonManagerConfig {
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;

    /**
     * RedissonClient,单机模式
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + host + ":" + port);
//        singleServerConfig.setTimeout(timeout);
//        singleServerConfig.setDatabase(database);
        if (password != null && !"".equals(password)) { //有密码
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    @Bean
    public RedissonDistributedLocker redissonLocker(RedissonClient redissonClient) {
        RedissonDistributedLocker locker = new RedissonDistributedLocker(redissonClient);
        //设置LockUtil的锁处理对象
        RedissonLockUtil.setLocker(locker);
        return locker;
    }

//    @Profile("pro")
//    @Bean(name = "redissonClient")
//    public RedissonClient redissonClientCluster() throws IOException {
//        String[] nodes = urls.split(",");
//        // redisson版本是3.5，集群的ip前面要加上“redis://”，不然会报错，3.2版本可不加
//        for (int i = 0; i < nodes.length; i++) {
//            nodes[i] = "redis://" + nodes[i];
//        }
//        RedissonClient redisson = null;
//        Config config = new Config();
//        config.useClusterServers() // 这是用的集群server
//                .setScanInterval(2000) // 设置集群状态扫描时间
//                .addNodeAddress(nodes).setPassword(password);
//        redisson = Redisson.create(config);
//        // 可通过打印redisson.getConfig().toJSON().toString()来检测是否配置成功
//        return redisson;
//    }
}
