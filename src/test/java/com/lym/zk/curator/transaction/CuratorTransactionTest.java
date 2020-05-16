package com.lym.zk.curator.transaction;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CuratorTransactionTest {

    @Autowired
    private CuratorFramework client;

    @After
    public void close(){
        client.close();
        log.info("curator关闭{}\t","-----------------");
    }

    @Test
    public void testTransaction() throws Exception {
        client.inTransaction().
                create().
                forPath("/watcher1/node6").
                and()
                .setData().
                forPath("/watcher1/node7","n7".getBytes())
                .and().commit();
    }
}
