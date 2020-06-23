package com.lym.zk.redis;

import com.lym.zk.utils.DistributedRedisLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.TIMEOUT;
import org.redisson.misc.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {

    @Autowired
    private RedisTemplate redisTemplate;



    @Test
    public void testString() {
        redisTemplate.opsForValue().set("name", "李四");
        log.info("name:\t{}", redisTemplate.opsForValue().get("name"));
    }

    @Test
    public void testRedisLock(){
        Map<String,Object> map = new HashMap<>();
        map.put("userName","lyz");
        redisTemplate.opsForHash().put("hash1","m1",map);
        log.info("=================={}\t",redisTemplate.opsForHash().get("hash1","m1"));
        log.info("=================={}\t",redisTemplate.opsForHash().get("hash2","name"));

        //stringRedisTemplate.opsForHash().getOperations().expire("hash1",20, TimeUnit.SECONDS);
    }

}
