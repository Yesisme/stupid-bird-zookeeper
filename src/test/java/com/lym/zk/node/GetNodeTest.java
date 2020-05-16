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

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class GetNodeTest {

    @Autowired
    private ZooKeeper zooKeeper;

    @Test
    public void testGetNode1() throws Exception {

        Stat stat = new Stat();
        byte[] data = zooKeeper.getData("/get/node1", false, stat);
        //获取的数据信息
        log.info("data\t{}",new String(data));

        log.info("查看版本信息\t{}",stat.getVersion());

    }

    @Test
    public void testGetNode2() throws Exception {
        zooKeeper.getData("/get/node1", false, new AsyncCallback.DataCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
                log.info("rc\t{}",rc);
                log.info("path\t{}",path);
                log.info("ctx\t{}",ctx);
                log.info("data\t{}",new String(data));
                log.info("stat\t{}",stat.getVersion());
            }
        },"i am context");
        Thread.sleep(1000);
        log.info("结束{}","------------------------");
    }

    @Test
    public void testGetChildren1() throws Exception{
        List<String> children = zooKeeper.getChildren("/get", false);
        children.forEach(s-> System.out.println(s));
    }

    @Test
    public void testGetChildrenNode2() throws Exception {
        zooKeeper.getChildren("/get", false, new AsyncCallback.ChildrenCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, List<String> children) {
                log.info("rc\t{}",rc);
                log.info("path\t{}",path);
                log.info("ctx\t{}",ctx);
                children.forEach(s-> System.out.println(s));
            }
        },"i am context");

        Thread.sleep(10000);
        log.info("结束\t{}","--------------------");
    }

}
