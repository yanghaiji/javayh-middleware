package com.javayh.redis.redisson.impl;

import com.javayh.redis.redisson.DistributedRedisLocker;
import org.redisson.api.RLock;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Dylan Yang
 * @Description: redis锁的实现
 * @ProjectName javayh-middleware
 * @date 2019-10-14 21:50
 */
@Component
public class RedissonDistributedLocker implements DistributedRedisLocker {

    private RedissonClient redissonClient;

    public RedissonDistributedLocker(RedissonClient redissonClient) {
        super();
        this.redissonClient = redissonClient;
    }
    /**
     * 加锁
     * @param lockKey
     * @return
     */
    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 获取锁，带有失效时间
     * @param lockKey
     * @param leaseTime
     * @return
     */
    @Override
    public RLock lock(String lockKey, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(leaseTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    @Override
    public RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }


    /**
     * 释放锁
     * @param lock
     */
    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 限流器
     * @param key       限流key
     * @param perNum    生成个数
     * @param time      所需要时间
     * @return
     */
    @Override
    public boolean trySetRate(String key,long perNum, long time) {
        boolean b = redissonClient.getRateLimiter(key).trySetRate(RateType.OVERALL, perNum, time, RateIntervalUnit.SECONDS);
        return b;
    }

    /**
     * 限流器
     * @param key       限流key
     * @param permits   等待时间
     * @param timeout   超时时间
     */
    public boolean tryAcquire(String key,long permits, long timeout) {
        boolean b = redissonClient.getRateLimiter(key).tryAcquire(permits, timeout, TimeUnit.SECONDS);
        return b;
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
