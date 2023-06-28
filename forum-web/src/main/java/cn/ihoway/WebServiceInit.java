package cn.ihoway;

import cn.ihoway.container.HowayContainer;
import cn.ihoway.task.MyScheduler;
import cn.ihoway.util.AccessXmlParser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//todo 启动各种需要启动的缓存
@SpringBootApplication
@EnableCaching
public class WebServiceInit {

    public static void main( String[] args ) {
        MyScheduler.execute();
        HowayContainer container = new HowayContainer();
        container.start();
        AccessXmlParser.init();
        SpringApplication.run(WebServiceInit.class, args);
    }
}