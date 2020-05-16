package com.lym.zk.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class PathChildrenCacheWatcher /*implements InitializingBean */{


    @Autowired
    private CuratorFramework client;


    @PostConstruct
    public void pathChildCache() throws Exception {
        // 监视子节点的变化
        // arg1:连接对象
        // arg2:监视的节点路径
        // arg3:事件中是否可以获取节点的数据
        PathChildrenCache childCache = new PathChildrenCache(client,"/watcher1",true);
        childCache.start();
        childCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                log.info("pathChildCache监听事件启动{}\t","----------------------");
                log.info("事件类型\t{}",event.getType());
                if(event.getType()== PathChildrenCacheEvent.Type.CONNECTION_RECONNECTED)return;
                log.info("当前节点路径\t{}",event.getData().getPath());
                log.info("当前节点数据\t{}",new String(event.getData().getData()));

            }
        });

    }

   /* @Override
    public void afterPropertiesSet() throws Exception {
        pathChildCache();
    }*/
}
