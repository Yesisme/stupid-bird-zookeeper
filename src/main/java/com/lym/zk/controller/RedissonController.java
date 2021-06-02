package com.lym.zk.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RCountDownLatch;
import org.redisson.api.RKeys;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Iterator;

/**
 * @author yiming.le
 * @version 1.0.0
 * @ClassName RedissonController.java
 * @Description
 * @createTime 2021-06-02 23:33
 */
@RestController
@RequestMapping("redisson")
@Slf4j
@RequiredArgsConstructor
public class RedissonController {


    private final RedissonClient redissonClient;
    private final RedisTemplate redisTemplate;
    private static final String COUNT_DOWN_LATCH = "countdownlatch";
    private static int COUNT = 5;

    @GetMapping("/countdownlatch/{id}")
    public String testCountdownlatch(@PathVariable String id){
        log.info("闭锁请求参数:[{}]",id);
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(COUNT_DOWN_LATCH);
        countDownLatch.countDown();
        return "还要执行"+ --COUNT +"次数";
    }

    @GetMapping("/countdownlatch/start")
    public String awitCountdownlatch() throws InterruptedException {
        RCountDownLatch countDownLatch = redissonClient.getCountDownLatch(COUNT_DOWN_LATCH);
        countDownLatch.trySetCount(COUNT); //执行完了5次,countdownlatch则会结束
        countDownLatch.await();
        return "countdownlatch end";
    }

    @GetMapping("/redis/get")
    public String redisGet(){
        RKeys keys = redissonClient.getKeys();
        Iterator<String> iterator = keys.getKeys().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
        String value = (String) redisTemplate.opsForValue().get("redis");
        log.info("redis value:{}",value);
        return String.valueOf(value);
    }

    @GetMapping("/redission/get")
    public String redissionGet(){
        String value = (String)redisTemplate.opsForValue().get("redission");
        log.info("countdownlatch value:{}",value);
        return value;
    }
}
