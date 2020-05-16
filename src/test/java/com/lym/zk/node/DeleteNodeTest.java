package com.lym.zk.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class DeleteNodeTest {


    @Autowired
    private ZooKeeper zooKeeper;

    @Test
    public void testDeleteNode1() throws Exception {
        //不考虑版本信息删除
        //同步删除
        zooKeeper.delete("/delete/node1",-1);
    }

    @Test
    public void testDeleteNode2() throws Exception {
        zooKeeper.delete("/delete/node1", -1, new AsyncCallback.VoidCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx) {
                log.info("rc\t{}",rc);
                log.info("path\t{}",path);
                log.info("ctx\t{}",ctx);
            }
        },"i am context");
        Thread.sleep(5000);
        log.info("结束{}","----------------------");

    }
}
