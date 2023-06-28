package cn.ihoway.util;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * 模板配置
* @author howay
*/
public class EssayConfigReader {

   private static Properties prop = null;

   static{
       getResource();
   }
   /**
    * 读取指定配置
    */
   public static String getConfig(String tempCode){
       return (String) prop.get(tempCode);
   }

   public synchronized static void getResource() {
       if (prop == null) {
           try(InputStream in = new ClassPathResource(
                   "essay.properties")
                   .getInputStream();) {

               prop = new Properties();
               prop.load(new InputStreamReader(in, StandardCharsets.UTF_8));   // 解决中文读取乱码问题
           } catch (Exception e) {
               e.printStackTrace();
           }

       }
   }

}