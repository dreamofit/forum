package cn.ihoway.task;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 定时任务配置类
 */
public class SchedulerConfig {
    private final SchedulerFactory schedulerFactory = new StdSchedulerFactory();
    private Scheduler scheduler;

    public void init() throws SchedulerException {
        scheduler = schedulerFactory.getScheduler();
    }

    public void addJob(Class clz,String cron) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(clz)
                .build();
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .startNow()//立即生效
                //"0/2 * * * * ? *"
                .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                .build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public void run() throws SchedulerException {
        scheduler.start();
    }

}