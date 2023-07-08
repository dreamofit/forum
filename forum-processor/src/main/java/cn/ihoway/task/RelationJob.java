package cn.ihoway.task;

import cn.ihoway.redis.RelationRedis;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务，将relationRedis中的数据持久化到数据库中
 */
public class RelationJob implements Job {
    private final RelationRedis relationRedis = new RelationRedis();
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        relationRedis.putAllToDb();
    }
}
