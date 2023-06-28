package cn.ihoway.operate.io;

import cn.ihoway.annotation.InputCheck;
import cn.ihoway.annotation.InsideCheck;
import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;
import cn.ihoway.type.OperateAct;

@InputCheck(check = true)
public class OperateActionInput  extends CommonInput {

    public InChomm inChomm = new InChomm();

    @InputCheck(check = true)
    public static class InChomm extends CommonSeria {

        @InsideCheck(mustInput = true,maxLen = 1,ranges = "0,1")
        public Integer type;//类型，0：文章  1：评论

        @InsideCheck(mustInput = true)
        public Integer optId;

        @InsideCheck(mustInput = true)
        public OperateAct action;

        @InsideCheck(maxLen = 1,ranges = "0,1")
        public Integer status;//0：未行动   1：已行动

        @InsideCheck(minValue = 1,maxValue = 10)
        public Integer score;//评分 1-10

        @InsideCheck(maxLen = 500)
        public String reprinted; //转载内容

        @InsideCheck(maxLen = 100)
        private String complaints; //举报内容，举报时必输

    }
}
