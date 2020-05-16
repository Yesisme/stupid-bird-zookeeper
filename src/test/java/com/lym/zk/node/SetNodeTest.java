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
public class SetNodeTest {

    @Autowired
    private ZooKeeper zooKeeper;

    @Test
    public void setTest1() throws Exception {
        //同步更新
        //-1表示版本号不参与更新
        //stat返回值可以获取信息
        Stat stat = zooKeeper.setData("/set/node1", "node1".getBytes(), -1);
        log.info("stat\t{}",stat);
    }

    @Test
    public void setTest2() throws Exception {
        //更新指定的版本号
        zooKeeper.setData("/set/node1", "s1n1".getBytes(), 1, new AsyncCallback.StatCallback() {
            @Override
            public void processResult(int rc, String path, Object ctx, Stat stat) {
                //0表示成功
                log.info("rc\t{}",rc);
                // 修改节点的路径
                log.info("path\t{}",path);
                //
                log.info("ctx\t{}",ctx);
                //stat创建事件信息
                log.info("stat\t{}",stat.getCtime());
            }
        },"i am context");
        Thread.sleep(5000);
        log.info("\t{}","结束");
    }
}
