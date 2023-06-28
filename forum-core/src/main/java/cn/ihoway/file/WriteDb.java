package cn.ihoway.file;

import cn.ihoway.util.EssayConfigReader;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 直接操作文件
 */
public class WriteDb {

    /**
     * 写文章
     * @param text
     * @param userName
     * @param name
     */
    public static void writeEssay(String text,String userName,String name){
        text = "<meta content=\"text/html\"; charset=\"utf-8\">" + text;
        String dirStr = EssayConfigReader.getConfig("path") + userName;
        FileOutputStream fos = null;
        try {
            File dir = new File(dirStr);
            if(!dir.exists()){
                dir.mkdirs();
            }
            fos = new FileOutputStream(dirStr+name);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(text.getBytes(StandardCharsets.UTF_8));
            bos.flush();
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
