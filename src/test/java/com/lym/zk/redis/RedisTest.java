package com.lym.zk.redis;

import com.lym.zk.utils.DistributedRedisLock;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class RedisTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    @Test
    public void testString() {
        stringRedisTemplate.opsForValue().set("name", "李四");
        log.info("name:\t{}", stringRedisTemplate.opsForValue().get("name"));
    }

    @Test
    public void testRedisLock(){

    }

}
