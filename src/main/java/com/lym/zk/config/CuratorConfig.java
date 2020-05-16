package com.lym.zk.config;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CuratorConfig {


    @Bean
    public CuratorFramework curatorFrameworkFactory(){
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //连接的ip地址的端口
                .connectString("192.168.96.128:2181,192.168.96.129:2181,192.168.96.130:2181")
                //会话超时时间
                .sessionTimeoutMs(5000)
                //重连机制：三秒后重连一次，只连一次
                .retryPolicy(retryPolicy())
                //命名空间
                .namespace("curator")
                .build();

        client.start();
        return client;
    }

    @Bean
    public RetryPolicy retryPolicy(){
        //三秒后重连一次，只连一次
        //RetryPolicy retryPolicy = new RetryOneTime(3000);

        //每三秒连接一次,重连三次
        RetryPolicy retryPolicy = new RetryNTimes(3,3000);

        //每3秒重连一次，总等待时间超过10秒停止重连
        //RetryPolicy retryPolicy = new RetryUntilElapsed(10000,3000);
        return retryPolicy;
    }

}
