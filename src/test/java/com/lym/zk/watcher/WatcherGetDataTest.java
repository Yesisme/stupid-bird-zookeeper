package com.lym.zk.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
@Slf4j
public class WatcherGetDataTest {

    String IP = "192.168.96.129:2181";
    ZooKeeper zooKeeper = null;


    @Before
    public void watchEvent1Before() throws Exception {
        //破获节点发生的变化
        //使用连接对象watcher
        CountDownLatch countDownLatch = new CountDownLatch(1);
        // 连接zookeeper客户端         
        zooKeeper = new ZooKeeper(IP, 6000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("连接对象的参数{}", "----------");
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    countDownLatch.countDown();
                }
                log.info("path={}", event.getPath());
                log.info("eventType={}" + event.getType());
            }
        });
        countDownLatch.await();
    }

    @After
    public void watchEvent1After() throws Exception {
        zooKeeper.close();
    }

    @Test
    public void testGetData1() throws Exception {
        //使用连接中watcher
        zooKeeper.getData("/watcher2",true,null);
        Thread.sleep(50000);
    }

    @Test
    public void testGetData2() throws Exception {
        zooKeeper.getData("/watcher2", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("自定义watcher{}","----------------");
                log.info("path={}", event.getPath());
                log.info("eventType={}" + event.getType());
            }
        },null);
        Thread.sleep(50000);
    }
}
