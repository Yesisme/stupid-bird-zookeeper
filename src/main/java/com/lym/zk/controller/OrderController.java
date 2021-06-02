package com.lym.zk.controller;

import com.lym.zk.service.impl.OrderItemServiceImpl;
import com.lym.zk.utils.ZkLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("order")
@Slf4j
public class OrderController {


    @Autowired
    private OrderItemServiceImpl orderItemService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisTemplate redisTemplate;

    ZkLockUtil zkLockUtil = new ZkLockUtil();

    @PostMapping
    @RequestMapping("/restock")
    public void order(@RequestParam("id") int id){
        String uniqueId = getUUID();
        boolean getLock =false;
        RLock lock = redissonClient.getLock(String.valueOf(id));
        try {
            if(getLock=lock.tryLock(0,60, TimeUnit.SECONDS)){
                if(!Thread.currentThread().isInterrupted()){
                    log.info(uniqueId+"用户获取到锁{}","####################");
                    int count = orderItemService.queryStockById(id);
                    if(count>0){
                        log.info(uniqueId+"用户获取到第{}手机",+count);
                        count=count-1;
                        orderItemService.updateStockById(count,id);
                    }else {
                        log.info(uniqueId+"不好意思手机被抢光了{}","===============");
                    }
                }
            }else{
                log.info(uniqueId+"用户未获取到锁{}","---------------");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            if(!getLock){
                return;
            }
            if(!Thread.currentThread().isInterrupted()){
                log.info(uniqueId+"用户释放了锁{}","####################");
                lock.unlock();
            }
        }
    }

    @PostMapping
    @RequestMapping("/stock")
    public void zkOrder(@RequestParam("id") int id){
        try {
            zkLockUtil.acquireLock();
            String uniqueId = getUUID();
            int count = orderItemService.queryStockById(id);
            if(count>0){
                log.info(uniqueId+"用户获取到第{}电脑",+count);
                count=count-1;
                orderItemService.updateStockById(count,id);
            }else {
                log.info(uniqueId+"不好意思电脑被抢光了{}","===============");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                zkLockUtil.releaselock();
            } catch (Exception e) {
                log.info(e.getMessage(),e);
            }
        }
    }


    public static String getUUID(){
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId.substring(uniqueId.lastIndexOf("-")+1, uniqueId.length());
    }
}
