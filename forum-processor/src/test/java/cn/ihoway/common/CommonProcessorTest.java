package cn.ihoway.common;

import cn.ihoway.common.io.CommonInput;
import cn.ihoway.common.io.CommonOutput;
import cn.ihoway.util.HowayResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonProcessorTest {

    private CommonProcessor processor = new CommonProcessor() {
        @Override
        protected HowayResult process(CommonInput input, CommonOutput output) {
            return null;
        }
    };

    @Test
    void doExecute() {
        CommonInput input = new CommonInput();
        CommonOutput output = new CommonOutput();
        HowayResult res = processor.doExecute(input,output);
        Assertions.assertFalse(res.isOk());
    }
}