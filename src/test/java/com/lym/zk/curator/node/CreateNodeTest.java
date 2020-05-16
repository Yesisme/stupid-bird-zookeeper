package com.lym.zk.curator.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class CreateNodeTest {

    @Autowired
    private CuratorFramework client;

    @After
    public void close(){
        client.close();
        log.info("curator关闭{}\t","-----------------");
    }

    @Test
    public void testCreateNode1() throws Exception {
        client.create().
                withMode(CreateMode.PERSISTENT).
                withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE).
                forPath("/node1","n1".getBytes());

    }

    @Test
    public void testCreateNode2() throws Exception {
        List<ACL> list = new ArrayList<>();
        Id id = new Id("ip","192.168.96.129");

        list.add(new ACL(ZooDefs.Perms.ALL,id));

        client.create().withMode(CreateMode.PERSISTENT)
                .withACL(list)
                .forPath("/node2","n2".getBytes());

    }

    @Test
    public void testCrrateNode3() throws Exception {
        client.create().
                creatingParentsIfNeeded()
                .withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                .forPath("/node3/node33","n3".getBytes());
    }

    @Test
    public void createNode4() throws Exception {
        client.create().
                withMode(CreateMode.PERSISTENT)
                .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                //异步回调接口
                .inBackground(new BackgroundCallback() {
                    @Override
                    public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
                        //节点的路径
                        log.info("节点的路径是{}",event.getPath());
                        //时间类型
                        log.info("时间类型{}",event.getType());
                    }
                })
                .forPath("/node4","n4".getBytes());
                Thread.sleep(5000);
    }

}
