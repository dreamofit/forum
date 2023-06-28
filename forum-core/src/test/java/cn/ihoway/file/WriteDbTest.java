package cn.ihoway.file;

import cn.ihoway.util.EssayConfigReader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WriteDbTest {

    @Test
    void writeEssay() {
        String name = EssayConfigReader.getConfig("path");
        System.out.println(name);
    }
}