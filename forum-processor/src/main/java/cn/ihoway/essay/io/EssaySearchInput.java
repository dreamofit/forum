package cn.ihoway.essay.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class EssaySearchInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public Integer type;
        public Integer index; //查询起始位置
        public Integer size; //查询大小
    }
}
