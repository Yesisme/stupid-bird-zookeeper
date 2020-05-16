package com.lym.zk.curator.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class getNodeTest {

    @Autowired
    private CuratorFramework client;

    @After
    public void close(){
        client.close();
        log.info("curator关闭{}\t","-----------------");
    }

    @Test
    public void testGetData1() throws Exception {
        byte[] bytes = client.getData().forPath("/get/node1");
        log.info("data\t{}",new String(bytes));
    }

    @Test
    public void testGetData2() throws Exception {
        Stat stat = new Stat();
        client.getData().storingStatIn(stat).forPath("/get/node1");
        log.info("stat\t{}",stat.getVersion());
        log.info("stat\t{}",stat);
    }

    @Test
    public void testGetData3() throws Exception {
        client.getData().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                log.info("路径\t{}",event.getPath());
                log.info("事件\t{}",event.getType());
            }
        }).forPath("/get/node1");
        Thread.sleep(5000);
    }
}
