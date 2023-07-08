package cn.ihoway;

import cn.ihoway.util.ConfigException;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.task.MyScheduler;
import cn.ihoway.util.AccessXmlParser;
import cn.ihoway.util.HowayLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//todo 启动各种需要启动的缓存
@SpringBootApplication
@EnableCaching
public class ServiceInit {

    public static void main( String[] args ) throws ConfigException {
        HowayLog logger = new HowayLog(ServiceInit.class);
        //消费者配置读取
        HowayContainer container = new HowayContainer();
        container.start();
        //accessXml解析器启动
        AccessXmlParser.init();
        //定时任务启动
        MyScheduler.execute();
        SpringApplication.run(ServiceInit.class, args);
        logger.info("*** forum服务已经启动 ***");
    }
}