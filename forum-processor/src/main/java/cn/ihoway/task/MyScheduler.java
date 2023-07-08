package cn.ihoway.task;

import cn.ihoway.util.ConfigException;
import cn.ihoway.util.JobXmlParser;
import cn.ihoway.util.XmlParser;
import org.dom4j.Element;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * todo 定时任务：对于举报数量大于阈值时触发文章隐藏机制并将文章提交给审核进行处理
 * todo 定时任务：对每个用户建立文章、收藏喜好表，将类似的用户习惯进行文章个性化推荐
 * 全部定时任务集合
 */
public class MyScheduler {
    public static void execute() throws ConfigException {
        try {
            SchedulerConfig scheduler = new SchedulerConfig();
            scheduler.init();
            JobXmlParser.init();
            XmlParser parser = JobXmlParser.parser;
            List<Element> elements = parser.getElements();
            for (Element element : elements){
                String impl = String.valueOf(parser.getValueFromElement(element,"impl"));
                String cron = String.valueOf(parser.getValueFromElement(element,"cron"));
                Class<?> clz = Class.forName(impl);
                scheduler.addJob(clz,cron);
            }
            scheduler.run();
        } catch (SchedulerException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
