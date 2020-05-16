package com.lym.zk.utils;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;


@Slf4j
public class DistributedRedisLock {
    private static RedissonClient redissonClient;
    private static final String LOCK_PREFIX = "redisLock_";

    public static void setRedissonClient(RedissonClient client) {
        if (DistributedRedisLock.redissonClient == null) {
            DistributedRedisLock.redissonClient = client;
        }
    }

    /**
     * 加锁，未设置锁过期时间，则默认锁1分钟
     *
     * @param lockName
     * @param expireTime
     * @param unit
     * @author: lixiongxing
     * @date: 2019年5月24日 10:50
     */
    public static void lock(String lockName, long expireTime, TimeUnit unit) {
        //声明key对象
        String key = LOCK_PREFIX + lockName;
        //获取锁对象
        RLock rLock = redissonClient.getLock(key);
        //加锁，并且设置锁过期时间，防止死锁的产生,默认锁1分钟
        if (expireTime > 0 && unit != null) {
            rLock.lock(expireTime, unit);
        } else {
            rLock.lock(1, TimeUnit.MINUTES);
        }
        log.info("线程【{}】加锁成功:{}", Thread.currentThread().getName(), lockName);
        //加锁成功
    }

    /**
     * 获取分布式锁，第一个参数，一定时间内未获取到锁，则不再等待直接返回boolean。交给上层处理
     *
     * @param lockName
     * @param tryTime
     * @param expireTime
     * @param unit
     * @author: lixiongxing
     * @date: 2019年5月24日 10:56
     */
    public static boolean tryLock(String lockName, long tryTime, long expireTime, TimeUnit unit) throws InterruptedException {
        //声明key对象
        String key = LOCK_PREFIX + lockName;
        //获取锁对象
        RLock rLock = redissonClient.getLock(key);
        //加锁，并且设置锁过期时间，防止死锁的产生,默认锁1分钟
        if (expireTime > 0 && unit != null) {
            log.info("线程【{}】尝试加锁:{}", Thread.currentThread().getName(), lockName);
            return rLock.tryLock(tryTime, expireTime, unit);
        } else {
            log.info("线程【{}】尝试加锁:{}", Thread.currentThread().getName(), lockName);
            return rLock.tryLock(1, 1, TimeUnit.MINUTES);
        }
    }

    /**
     * 释放锁
     *
     * @param lockName
     * @author: lixiongxing
     * @date: 2019年5月24日 10:57
     */
    public static void unLock(String lockName) {
        //声明key对象
        String key = LOCK_PREFIX + lockName;
        log.info("线程【{}】释放锁:{}", Thread.currentThread().getName(), lockName);
        //获取锁对象
        RLock rLock = redissonClient.getLock(key);
        if (rLock != null) {
            rLock.unlock();
        }
    }
}
