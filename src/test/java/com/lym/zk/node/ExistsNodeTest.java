package com.lym.zk.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class ExistsNodeTest {

    @Autowired
    private ZooKeeper zooKeeper;

    @Test
    public void testExistsNode1() throws Exception {
        Stat stat = zooKeeper.exists("/exists", false);
        log.info("exists\t{}",stat.getVersion());
    }

    @Test
    public void testExistsNode2() throws Exception {
        zooKeeper.exists("/exists", false, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                log.info("rc\t{}",rc);
                log.info("path\t{}",path);
                log.info("ctx\t{}",ctx);
                log.info("stat\t{}",stat);
            }
        },"i am context");
        Thread.sleep(5000);
        log.info("结束\t{}","-----------------------------");
    }
}
