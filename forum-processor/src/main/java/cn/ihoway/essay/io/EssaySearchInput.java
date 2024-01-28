package cn.ihoway.essay.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

//不能使用InputCheck注解进行检查，因为token字段非必输
public class EssaySearchInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public Integer type;
        public Integer index; //查询起始位置
        public Integer size; //查询大小
    }
}
