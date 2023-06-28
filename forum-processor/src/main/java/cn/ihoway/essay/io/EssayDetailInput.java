package cn.ihoway.essay.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class EssayDetailInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public Integer essayId;
    }
}
