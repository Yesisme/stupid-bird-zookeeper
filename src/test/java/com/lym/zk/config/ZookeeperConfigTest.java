package com.lym.zk.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ZookeeperConfigTest {

    @Autowired
    private ZooKeeper zooKeeper;

    @Test
    public void testZookeeper1(){
        log.info("zookeeper信息{}",zooKeeper);
    }

    @Test
    public void testZookeeper2() throws Exception{
        //测试该用户是否有权限访问
        zooKeeper.addAuthInfo("digest1","lym1:123456".getBytes());
        byte[] data = zooKeeper.getData("/get/node1", false, null);
        log.info("data\t{}",data);
    }
}
