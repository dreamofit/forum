package cn.ihoway.essay;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.essay.io.EssayAllSearchInput;
import cn.ihoway.essay.io.EssayAllSearchOutput;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;

@Processor(name = "EssayAllSearchProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class EssayAllSearchProcessor  extends CommonProcessor<EssayAllSearchInput, EssayAllSearchOutput> {
    private final HowayLog logger = new HowayLog(EssayAllSearchProcessor.class);

    @Override
    protected StatusCode dataCheck(EssayAllSearchInput input){
        return StatusCode.SUCCESS;
    }
    
    @Override
    protected HowayResult process(EssayAllSearchInput input, EssayAllSearchOutput output) {
        return null;
    }
}