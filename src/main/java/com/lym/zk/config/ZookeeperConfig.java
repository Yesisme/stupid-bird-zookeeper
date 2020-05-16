package com.lym.zk.config;

import com.lym.zk.utils.ZkLockUtil;
import com.lym.zk.watcher.ZkConnectionWatcher;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Configuration
@Slf4j
public class ZookeeperConfig {


    /*@Bean
    public ZooKeeper zooKeeper(){

        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper("192.168.96.129:2181", 5000, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if(event.getState()==Event.KeeperState.SyncConnected){
                        log.info("zookeeper连接成功\t{}","-------------------");
                        countDownLatch.countDown();
                    }
                }
            });
            // 主线程阻塞等待连接对象的创建成功
            countDownLatch.await();
        } catch (IOException e) {
            log.error("zookeeper连接异常\t{}",e.getMessage());
        } catch (InterruptedException e) {
            log.error("countDownLatch异常\t{}",e.getMessage());
        }
        return zooKeeper;
    }*/

    @Bean(name="zookeeper")
    public ZooKeeper zooKeeper(){
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ZooKeeper zooKeeper = null;
        try {
            zooKeeper = new ZooKeeper("192.168.96.129:2181", 5000,new ZkConnectionWatcher(countDownLatch));
            log.info("当前会话ID\t{}",zooKeeper.getSessionId());
            countDownLatch.await();
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
        ZkLockUtil.setZooKeeper(zooKeeper);
        return zooKeeper;
    }

}
