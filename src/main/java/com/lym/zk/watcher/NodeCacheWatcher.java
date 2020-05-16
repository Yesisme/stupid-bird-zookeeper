package com.lym.zk.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NodeCacheWatcher implements InitializingBean {

    @Autowired
    private CuratorFramework client;

    public void watcher() throws Exception {
        NodeCache nodeCache = new NodeCache(client,"/watcher1");
        nodeCache.start();

        nodeCache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                log.info("curator开始监听节点{}","----------------");
                log.info("当前节点路径\t{}",nodeCache.getCurrentData().getPath());
                log.info("当前节点数据\t{}",new String(nodeCache.getCurrentData().getData()));
            }
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        //watcher();
    }
}
