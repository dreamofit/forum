package cn.ihoway.relation.io;

import cn.ihoway.annotation.InputCheck;
import cn.ihoway.annotation.InsideCheck;
import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

@InputCheck(check = true)
public class ChatContentInput extends CommonInput {
    public InChomm inChomm = new InChomm();

    @InputCheck(check = true)
    public static class InChomm extends CommonSeria {

        @InsideCheck(mustInput = true)
        public Integer targetId; //目标对象id

    }
}
