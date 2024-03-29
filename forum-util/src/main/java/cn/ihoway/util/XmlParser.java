package cn.ihoway.util;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;

public class XmlParser {

    private static final HowayLog logger = new HowayLog(XmlParser.class);
    private String pathName = ""; //xml文件路径

    private List<Element> elements; //获取全部元素标签
    private String pk = "id"; //元素主键名称,默认为id

    /**
     * 初始化
     */
    public void init() throws ConfigException {
        try {
            InputStream in = new ClassPathResource(pathName).getInputStream();
            //创建SAXReader对象
            SAXReader reader = new SAXReader();
            //读取文件 转换成Document
            Document document = reader.read(in);
            //获取根节点
            Element root = document.getRootElement();
            elements = root.elements();
            checkPk();
        } catch (DocumentException | IOException e) {
            logger.error(e.getStackTrace());
        } catch (ConfigException e) {
            throw new ConfigException(e.getMessage());
        }
    }

    //检查主键，必须唯一
    private void checkPk() throws ConfigException {
        HashSet<String> set = new HashSet<>();
        for (Element element:elements){
            Attribute attribute =  element.attribute(pk);
            String value = attribute.getValue();
            if(set.contains(value)){
                throw new ConfigException(pathName + " [" + pk + " duplicate] : " + value + "!");
            }
            set.add(value);
        }
    }

    /**
     * 获取元素的主键的值获取对应的元素
     * @param v value
     * @return element
     */
    public Element getElement(String v){
        for (Element element:elements){
            Attribute attribute =  element.attribute(pk);
            if(attribute == null){
                return null;
            }
            if(v.equals(attribute.getValue())){
                return element;
            }
        }
        return null;
    }

    /**
     * 根据某个属性获取指定元素的值
     * @param element element
     * @param k 属性key
     * @return value
     */
    public Object getValueFromElement(Element element,String k){
        List<Attribute> attributes = element.attributes();
        for (Attribute attribute : attributes){
            if(k.equals(attribute.getName())){
                return attribute.getValue();
            }
        }
        return null;
    }

    public void setPathName(String pathName) {
        this.pathName = pathName;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public List<Element> getElements() {
        return elements;
    }
}
