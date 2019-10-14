package com.javayh.redisson;

import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * @author Dylan Yang
 * @Description: 分布式redis锁
 * @ProjectName javayh-middleware
 * @date 2019-10-14 21:42
 */
public interface DistributedRedisLocker {

    /**
     * 获取锁 拿不到，一直等待
     * @param lockKey
     * @return
     */
    RLock lock(String lockKey);

    /**
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    RLock lock(String lockKey, int timeout);

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    RLock lock(String lockKey, TimeUnit unit, int timeout);

    /**
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    /**
     * 根据key释放锁
     * @param lockKey
     */
    void unlock(String lockKey);

    /**
     * 释放锁
     * @param lockKey
     */
    void unlock(RLock lock);
}
