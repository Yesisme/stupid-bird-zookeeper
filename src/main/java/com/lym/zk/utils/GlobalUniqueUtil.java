package com.lym.zk.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
@Slf4j
public class GlobalUniqueUtil {

    public static final String defaultPath = "/uniqueId";

    public static String getUniqueId(){

        String path = "";
        ZooKeeper zookeeper = (ZooKeeper) SpringUtil.getBean("zookeeper");

        try {
            path = zookeeper.create(defaultPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        } catch (KeeperException e) {
            log.error(e.getMessage(),e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
        }
        return path.substring(9);
    }
}
