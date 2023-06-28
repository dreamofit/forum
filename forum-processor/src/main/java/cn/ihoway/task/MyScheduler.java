package cn.ihoway.task;

import org.quartz.SchedulerException;

/**
 * todo 定时任务：对于举报数量大于阈值时触发文章隐藏机制并将文章提交给审核进行处理
 * todo 定时任务：对每个用户建立文章、收藏喜好表，将类似的用户习惯进行文章个性化推荐
 * 全部定时任务集合
 */
public class MyScheduler {
    public static void execute(){
        try {
            SchedulerConfig scheduler = new SchedulerConfig();
            scheduler.init();
            //todo 定时时长写入定时任务的配置文件中
            scheduler.addJob(OperateJob.class,"0 0/2 * * * ?");
            scheduler.addJob(RelationJob.class,"0 0/2 * * * ?");
            scheduler.run();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
