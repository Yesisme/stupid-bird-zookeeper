package com.lym.zk.job;

import com.lym.zk.mapper.OrderItemMapper;
import com.lym.zk.utils.DistributedRedisLock;
import com.lym.zk.utils.ZkLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


@EnableScheduling
@Component
@Slf4j
public class OrderItemJob {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private CuratorFramework client;

    private static final String LOCK_PAHT="/lock1";

    private int lockId = 133;

    ZkLockUtil zkLockUtil = new ZkLockUtil();


    @Autowired
    private RedissonClient redissonClient;

    /*public void doJob() {
        if (count < 10) {
            try {
                zkLockUtil.acquireLock();
                int quantityById = orderItemMapper.findQuantityById();
                log.info("商品剩余\t{}",quantityById);
                quantityById=quantityById-1;
                orderItemMapper.updateById(quantityById);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    count++;
                    zkLockUtil.releaselock();
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }else {
            log.info("执行完毕", "-------------------------------");
        }
    }*/


    //curator互斥锁
    /*public void doJob() {
        InterProcessLock lock = null;
        if (count < 10) {
            try {
                lock = new InterProcessMutex(client,LOCK_PAHT);
                lock.acquire();
                int quantityById = orderItemMapper.findQuantityById();
                log.info("商品剩余\t{}",quantityById);
                quantityById=quantityById-1;
                orderItemMapper.updateById(quantityById);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    count++;
                    lock.release();
                }catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }else {
            log.info("执行完毕", "-------------------------------");
        }
    }*/

    //读锁可以同时读
    /*public void doJob() {
        InterProcessReadWriteLock lock = null;
        InterProcessMutex interProcessMutex=null;
        if (count < 10) {
            try {
                lock = new InterProcessReadWriteLock(client,LOCK_PAHT);
                interProcessMutex = lock.readLock();
                interProcessMutex.acquire();
                int quantityById = orderItemMapper.findQuantityById();
                quantityById=quantityById-1;
                log.info("商品剩余\t{}",quantityById);
                orderItemMapper.updateById(quantityById);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    count++;
                    interProcessMutex.release();
                }catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }else {
            log.info("执行完毕", "-------------------------------");
        }
    }*/


    //写锁只能一个人写
    /*public void doJob() {
        InterProcessReadWriteLock lock = null;
        InterProcessMutex interProcessMutex=null;
        if (count < 10) {
            try {
                lock = new InterProcessReadWriteLock(client,LOCK_PAHT);
                interProcessMutex = lock.writeLock();
                interProcessMutex.acquire();
                int quantityById = orderItemMapper.findQuantityById();
                quantityById=quantityById-1;
                log.info("商品剩余\t{}",quantityById);
                orderItemMapper.updateById(quantityById);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            } finally {
                try {
                    count++;
                    interProcessMutex.release();
                }catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }else {
            log.info("执行完毕", "-------------------------------");
        }
    }*/


    //redisLock
    public void doJob() {
           boolean getLock = false;
            try {
                if(getLock=DistributedRedisLock.tryLock(String.valueOf(lockId), 0, 5, TimeUnit.SECONDS)){
                    int quantityById = orderItemMapper.findQuantityById(lockId);
                    if(quantityById>0){
                        quantityById=quantityById-1;
                        log.info("商品剩余\t{}",quantityById);
                        orderItemMapper.updateById(quantityById,lockId);
                    }else{
                        log.info("商品剩余\t{}","商品被卖光了");
                    }
                }else {
                    log.info("未获取到分布式锁{}\t",Thread.currentThread().getName());
                }
            } catch (Exception e) {
                log.error("分布式锁获取异常{}\t", e);
            } finally {
                if(!getLock){
                    return;
                }
                DistributedRedisLock.unLock(String.valueOf(lockId));
            }
    }

    /*public void doJob() {
            RLock lock = redissonClient.getLock(LOCK_PAHT);
            boolean getLock = false;
            try {
                if(getLock=lock.tryLock(0, 50, TimeUnit.SECONDS)){
                    log.info("线程【{}】加锁成功:{}", Thread.currentThread().getName(), LOCK_PAHT);
                    int quantityById = orderItemMapper.findQuantityById(lockId);
                    if(quantityById>0){
                        quantityById=quantityById-1;
                        log.info("商品剩余\t{}",quantityById);
                        orderItemMapper.updateById(quantityById,lockId);
                    }else{
                        log.info("商品剩余\t{}","商品被卖光了");
                    }
                }else {
                    log.info("未获取到分布式锁{}\t",Thread.currentThread().getName());
                }
            } catch (Exception e) {
                log.error("分布式锁获取异常{}\t", e);
            } finally {
                if(!getLock){
                    return;
                }
                lock.unlock();
                log.info("线程【{}】释放锁成功:{}", Thread.currentThread().getName(), LOCK_PAHT);
            }
        }*/
}
