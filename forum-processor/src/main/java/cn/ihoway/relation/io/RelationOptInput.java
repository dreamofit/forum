package cn.ihoway.relation.io;

import cn.ihoway.annotation.InputCheck;
import cn.ihoway.annotation.InsideCheck;
import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.type.RelationAct;

@InputCheck(check = true)
public class RelationOptInput  extends CommonInput {

    public InChomm inChomm = new InChomm();

    @InputCheck(check = true)
    public static class InChomm extends CommonSeria {

        @InsideCheck(mustInput = true)
        public Integer targetId; //目标对象id

        @InsideCheck(mustInput = true)
        public RelationAct action; //操作类型

        @InsideCheck(mustInput = true)
        public Integer type; //新增：1 删除已有记录：2

        @InsideCheck(maxLen = 1000)
        public String content; //私信内容

        public String group; //被关注者所在分组名称
    }
}
