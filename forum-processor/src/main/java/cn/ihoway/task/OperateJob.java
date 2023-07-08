package cn.ihoway.task;

import cn.ihoway.redis.OperateRedis;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务，将operateRedis中的数据持久化到数据库中
 */
public class OperateJob implements Job {

    private final OperateRedis operateRedis = new OperateRedis();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        operateRedis.putAllToDb();
    }
}
