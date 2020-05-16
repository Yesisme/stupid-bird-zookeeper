package com.lym.zk.curator.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.junit.After;
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
    private CuratorFramework client;

    @After
    public void close(){
        client.close();
        log.info("curator关闭{}\t","-----------------");
    }


    @Test
    public void testDeleteNode1() throws Exception {

        client.delete().forPath("/delete/node1");
    }

    @Test
    public void testeDeletNode2() throws Exception {
        client.delete().deletingChildrenIfNeeded().forPath("/delete/node2/node22");
    }

    @Test
    public void testDeleteNode3() throws Exception {
        client.delete().inBackground(new BackgroundCallback() {
            @Override
            public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                log.info("路径\t{}",event.getPath());
                log.info("事件\t{}",event.getType());

            }
        }).forPath("/delete/node3");
    }

}
