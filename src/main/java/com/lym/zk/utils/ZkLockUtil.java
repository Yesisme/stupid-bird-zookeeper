package com.lym.zk.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.redisson.api.RedissonClient;

import java.util.Collections;
import java.util.List;
/*
*排它锁实现
*/
@Slf4j
public class ZkLockUtil {

    public static final String LOCK_ROOT_PATH="/Locks";

    public static final String LOCK_NODE_NAME="Lock_";

    private static String lockPath = "";

    private static ZooKeeper zooKeeper;

    public static void setZooKeeper(ZooKeeper zooKeeper) {
        ZkLockUtil.zooKeeper = zooKeeper;
    }

    //获取锁
    public void acquireLock()throws Exception{

        createLock();

        attemptLock();

    }

    //创建锁节点
    public void createLock() throws Exception{
        //判断Locks是否存在,不存在创建
        Stat stat = zooKeeper.exists(LOCK_ROOT_PATH, false);
        //不存在则创建
        if(stat==null){
            zooKeeper.create(LOCK_ROOT_PATH, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        //创建一个临时有序节点
        lockPath = zooKeeper.create(LOCK_ROOT_PATH+"/"+LOCK_NODE_NAME,new byte[0], ZooDefs.Ids.READ_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);

        log.info("\t{}",lockPath+"创建成功");

    }

      Watcher watcher = new Watcher() {
        @Override
        public void process(WatchedEvent event) {
            if(event.getType()==Event.EventType.NodeDeleted){
                synchronized (this){
                    notify();
                }
            }
        }
    };

    //尝试获取锁
    public  void attemptLock()throws Exception{
        //获取子节点并排序
        List<String> childrensNode = zooKeeper.getChildren(LOCK_ROOT_PATH, false);
        Collections.sort(childrensNode);

        //判断当前节点的位置
        int index = childrensNode.indexOf(lockPath.substring(LOCK_ROOT_PATH.length()+1));
        if(index==0){
            log.info("\t{}","获取锁成功!");
        }else {
            //上一个节点的路径
            String path = childrensNode.get(index - 1);
            Stat stat = zooKeeper.exists(LOCK_ROOT_PATH + "/" + path, watcher);
            if(stat == null){
                attemptLock();
            }
            synchronized (watcher){
                watcher.wait();
            }
            attemptLock();
        }
    }


    //释放锁
    public  void releaselock() throws Exception{
        //删除临时有序节点
        zooKeeper.delete(lockPath,-1);
        log.info("锁已经释放{}",lockPath);
    }


}
