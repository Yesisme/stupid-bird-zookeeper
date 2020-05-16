package com.lym.zk.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CountDownLatch;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ZkConnectionWatcherTest {

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
                log.info("eventType=" + event.getType());
            }
        });
        countDownLatch.await();
    }

    @After
    public void watchEvent1After() throws Exception {
        zooKeeper.close();
    }

    @Test
    public void watcherExists1() throws KeeperException, InterruptedException {
        //arg1: 节点路径
        //arg2：自定义使用连接对象中的watcher
        //一次性的 一次只能捕获一次
        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("监听路径\t{}", event.getPath());
                log.info("监听事件类型\t{}", event.getType());
            }
        });
        Thread.sleep(50000);
    }

    @Test
    public void wathcerExists2() {
        //watcher监听多次

        Watcher watcher = null;
        try {
            watcher = new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    log.info("监听路径\t{}", event.getPath());
                    log.info("监听事件类型\t{}", event.getType());
                    try {
                        zooKeeper.exists("/watcher1", this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            zooKeeper.exists("/watcher1", watcher);
            Thread.sleep(50000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void watcherExists3() throws KeeperException, InterruptedException {

        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("第一个监听器{}","--------------");
                log.info("监听路径\t{}", event.getPath());
                log.info("监听事件类型\t{}", event.getType());
            }
        });

        zooKeeper.exists("/watcher1", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                log.info("第二个监听器{}","---------------");
                log.info("监听路径\t{}", event.getPath());
                log.info("监听事件类型\t{}", event.getType());
            }
        });
        Thread.sleep(50000);
    }
}
