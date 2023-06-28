package cn.ihoway.essay.io;

import cn.ihoway.annotation.InputCheck;
import cn.ihoway.entity.Essay;
import cn.ihoway.util.Convert;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class EssayAddInputTest {

    @Test
    void testCovert(){
        EssayAddInput input = new EssayAddInput();
        input.inChomm.type = 0;
        input.inChomm.author = 1;
        input.inChomm.commentPermission = 0;
        input.inChomm.imgs = "/imgs/001.png;";
        input.inChomm.label = "java;html";
        input.inChomm.topic = "java";
        input.inChomm.readPermissions = 0;
        input.inChomm.title = "学习java";
        input.inChomm.url = "/essay/howay/001";
        Convert convert = new Convert();
        Essay essay = (Essay) convert.inputToBean(Essay.class.getName(),EssayAddInput.class.getName()+"$InChomm",input.inChomm);
        System.out.println(JSON.toJSONString(essay));
    }

    @Test
    void testReg(){
        String str = "{\"condition\":\"\",\"data\":{\"userList\":[{\"id\":4,\"name\":\"李四\\n\",\"password\":\"2f4ce48a2f919d6df49edef749142e87\",\"qq\":\"123\",\"role\":99,\"sign\":\"hhhh2233\",\"tel\":\"15215675679\"}]},\"ok\":true,\"statusCode\":\"SUCCESS\"}";
        str = str.replaceAll("\"data\":.*,","");
        System.out.println(str);
    }

    @Test
    void testReflect() throws ClassNotFoundException, IllegalAccessException {
        EssayAddInput input = new EssayAddInput();
        input.ip = "12.3";
//        System.out.println(input.getClass());
//        Field[] fileds = input.getClass().getFields();
//        for (Field field : fileds){
//            System.out.println(field.getName());
//        }
//        Class[] calz = input.getClass().getDeclaredClasses();
//        for (Class cz:calz){
//            System.out.println(cz.getName());
//            InputCheck a = (InputCheck) cz.getAnnotation(InputCheck.class);
//            System.out.println(a.check());
//        }

        Class clz = input.getClass();
        //InputCheck inputCheck = (InputCheck) clz.getDeclaredAnnotation(InputCheck.class);


        Field[] fileds2 = clz.getFields();
        for (Field field : fileds2){
            System.out.println(field.get(input));
        }
        System.out.println(getFastField("ip",input));
    }

    public  static <T> Object getFastField(String fieldName, T t) {
        Class<?> aClass = t.getClass();
        do {
            try {
                Field field = aClass.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object o = field.get(t);
                return o;
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                aClass = aClass.getSuperclass();
            }
        } while (aClass == Object.class);
        return null;
    }

}