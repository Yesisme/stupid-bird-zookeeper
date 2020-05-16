package com.lym.zk.node;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
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
    private ZooKeeper zooKeeper;

    @Test
    public void testCreateNode1() throws Exception{
        //arg1:节点名称
        //arg2：节点数据
        //arg3：节点权限
        //arg4，节点类型 ：临时还是持久
        String zkNode = zooKeeper.create("/hadoop/node4", "n4".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        log.info("zkNode\t{}",zkNode);
    }

    @Test
    public void testCreatNode2() throws Exception {
        List<ACL> list = new ArrayList<>();

        Id id = new Id("world","anyone");

        list.add(new ACL(ZooDefs.Perms.READ,id));
        list.add(new ACL(ZooDefs.Perms.WRITE,id));

        zooKeeper.create("/hadoop/node5","n5".getBytes(),list,CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateNode3() throws Exception {
        List<ACL> aclList = new ArrayList<>();
        Id id = new Id("ip","192.168.96.129");

        aclList.add(new ACL(ZooDefs.Perms.ALL,id));

        zooKeeper.create("/hadoop/node6","n6".getBytes(),aclList,CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateNode4() throws Exception {
        //代码方式添加用户
        zooKeeper.addAuthInfo("digest","lym:123456".getBytes());
        zooKeeper.create("/hadoop/node7","n7".getBytes(),ZooDefs.Ids.CREATOR_ALL_ACL,CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateNode5()throws Exception{

        zooKeeper.addAuthInfo("digest","lym:123456".getBytes());

        List<ACL> aclList = new ArrayList<>();
        Id id = new Id("auth","lym");
        aclList.add(new ACL(ZooDefs.Perms.READ,id));
        zooKeeper.create("/hadoop/node8","n8".getBytes(),aclList,CreateMode.PERSISTENT);
    }

    @Test
    public void testCreateNode6() throws Exception{
        List<ACL> list = new ArrayList<>();
        Id id  = new Id("digest","itcast:673OfZhUE8JEFMcu0l64qI8e5ek=");
        list.add(new ACL(ZooDefs.Perms.ALL,id));
        zooKeeper.create("/hadoop/node9","n9".getBytes(),list,CreateMode.PERSISTENT);
    }


    @Test
    public void testCreateNode7() throws Exception{
        //持久化顺序节点
        zooKeeper.create("/hadoop/node10","n10".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT_SEQUENTIAL);
    }

    @Test
    public void testCreateNode8() throws Exception{
        //临时节点 test结束会话消失就没了
        String node = zooKeeper.create("/hadoop/node11", "n11".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        log.info("node\t{}",node);
    }

    @Test
    public void testCreateNode9() throws Exception{
        //临时有序节点
        String node = zooKeeper.create("/hadoop/node12","n12".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
        log.info("node\t{}",node);
    }

    @Test
    public void testCreateNode10() throws Exception{
        //异步方法创建
        zooKeeper.create("/hadoop/node13", "n13".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT, new AsyncCallback.StringCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, String name) {
                //0表示节点创建成功
                log.info("rc\t{}",rc);
                //path 表示节点路径
                log.info("path\t{}",path);

                log.info("path\t",name);

                //ctx 上下文参数
                log.info("name\t{}",name);

            }
        },"i am context");
        Thread.sleep(10000);
        log.info("结束{}","----------------");
    }

    @After
    public void close() throws Exception {
        zooKeeper.close();
    }
}
