package com.lym.zk.curator.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class SetNodeTest {

    @Autowired
    private CuratorFramework client;

    @After
    public void close(){
        client.close();
        log.info("curator关闭{}\t","-----------------");
    }

    @Test
    public void setDateTest1() throws Exception {
        client.setData().forPath("/node4","nd4".getBytes());
    }

    @Test
    public void setDateTest2() throws Exception {
        //根据版本号更新
        client.setData().
                withVersion(-1).forPath("/node1","nd11".getBytes());
    }

    @Test
    public void setDateTest3() throws Exception {

        client.setData().withVersion(-1).inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                log.info("更新路径\t{}",event.getPath());
                log.info("更新的事件\t{}",event.getType());
            }
        }).forPath("/node1","nd1111".getBytes());
        Thread.sleep(5000);
    }
}
