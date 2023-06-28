package cn.ihoway.util;

public class HowayStringUtils {

    //对象转为Integer
    public static Integer toInteger(Object obj){
        if(obj == null){
            return null;
        }
        try{
            return Integer.parseInt(obj.toString());
        }catch (Exception e){
            return null;
        }
    }
}
