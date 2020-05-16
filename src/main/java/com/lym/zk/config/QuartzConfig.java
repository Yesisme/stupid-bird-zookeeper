package com.lym.zk.config;

import com.lym.zk.job.OrderItemJob;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean(name = "orderItemBean")
    public MethodInvokingJobDetailFactoryBean orderItemBean(OrderItemJob orderItemJob) {
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        jobDetail.setConcurrent(false); // 是否并发
        jobDetail.setName("general-orderItemJob"); // 任务的名字
        jobDetail.setGroup("general"); // 任务的分组
        jobDetail.setTargetObject(orderItemJob); // 被执行的对象
        jobDetail.setTargetMethod("doJob"); // 被执行的方法
        return jobDetail;
    }

    @Bean(name = "orderItemTrigger")
    public CronTriggerFactoryBean pushMsgTrigger(@Qualifier("orderItemBean") MethodInvokingJobDetailFactoryBean orderItemBean) {
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setJobDetail(orderItemBean.getObject());
        tigger.setCronExpression("0/1 35 00 * * ? ");
        tigger.setName("general-orderItemTrigger");
        return tigger;
    }

    @Bean(name = "schedulerFactory")
    public SchedulerFactoryBean schedulerFactory(@Qualifier("orderItemTrigger") Trigger orderItemTrigger) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setStartupDelay(15);
        factory.setTriggers(orderItemTrigger);
        return factory;
    }
}
