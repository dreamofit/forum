package cn.ihoway;

import cn.ihoway.util.HowayContainer;
import cn.ihoway.task.MyScheduler;
import cn.ihoway.util.AccessXmlParser;
import cn.ihoway.util.HowayLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.support.ClassPathXmlApplicationContext;

//todo 启动各种需要启动的缓存
@SpringBootApplication
@EnableCaching
public class ServiceInit {

    public static void main( String[] args ) {
        HowayLog logger = new HowayLog(ServiceInit.class);
        //定时任务启动
        MyScheduler.execute();
        //生产者provider配置读取
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("dubbo/provider.xml");
        context.start();
        //消费者配置读取
        HowayContainer container = new HowayContainer();
        container.start();
        //accessXml解析器启动
        AccessXmlParser.init();
        SpringApplication.run(ServiceInit.class, args);
        logger.info("*** forum服务已经启动 ***");
    }
}