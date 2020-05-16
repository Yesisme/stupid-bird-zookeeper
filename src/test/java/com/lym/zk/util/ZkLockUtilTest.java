package com.lym.zk.util;

import com.lym.zk.mapper.OrderItemMapper;
import com.lym.zk.utils.ZkLockUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class ZkLockUtilTest {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    public void testZkLock() {

    }

}
