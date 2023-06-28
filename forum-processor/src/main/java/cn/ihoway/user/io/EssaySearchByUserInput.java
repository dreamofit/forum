package cn.ihoway.user.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class EssaySearchByUserInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public Integer type;
        public Integer index;
        public Integer size;
    }
}
