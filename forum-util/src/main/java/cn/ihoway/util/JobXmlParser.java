package cn.ihoway.util;

public class JobXmlParser {
    public static XmlParser parser;

    public static void init() throws ConfigException {
        parser = new XmlParser();
        parser.setPathName("/META-INF/job.xml");
        parser.init();
    }
}
