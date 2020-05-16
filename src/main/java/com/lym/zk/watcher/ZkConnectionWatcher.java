package com.lym.zk.watcher;

import com.lym.zk.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ZkConnectionWatcher implements Watcher {


    private CountDownLatch countDownLatch;

    public ZkConnectionWatcher(CountDownLatch countDownLatch){
        this.countDownLatch = countDownLatch;
    }
    @Override
    public void process(WatchedEvent event) {

        try {
            //事件类型
            if(event.getType() == Event.EventType.None){
                if(event.getState() == Event.KeeperState.SyncConnected){
                    log.info("连接zookeeper成功\t{}","-----------------");
                    countDownLatch.countDown();
                }else if(event.getState() == Event.KeeperState.Disconnected){
                    log.info("zookeeper断开连接\t{}","---------------------");
                    //通过消息队列发送消息进行通知
                }else if(event.getState() == Event.KeeperState.Expired){
                    log.info("zookeeper会话超时\t{}","---------------------");
                }else if(event.getState()==Event.KeeperState.AuthFailed){
                    log.info("zookeeper认证失败\t{}","----------------------");
                }
            }
            ZooKeeper zookeeper = (ZooKeeper) SpringUtil.getBean("zookeeper");
            zookeeper.exists("/watcher1",true);
            zookeeper.getData("/watcher2",this,null);
            List<String> children = zookeeper.getChildren("/watcher3", true);
            children.stream().forEach(System.out::println);
            log.info("监听路径\t{}",event.getPath());
            log.info("监听事件类型\t{}",event.getType());

        } catch (Exception e) {
           log.error(e.getMessage(),e);
        }
    }
}
