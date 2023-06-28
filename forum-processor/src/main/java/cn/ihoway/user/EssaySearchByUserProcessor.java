package cn.ihoway.user;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.impl.EssayServiceImpl;
import cn.ihoway.service.EssayService;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.user.io.EssaySearchByUserInput;
import cn.ihoway.user.io.EssaySearchByUserOutput;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;

import java.util.HashMap;

/**
 * 查询某用户发表的全部文章
 */
@Processor(name = "EssaySearchByUserProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class EssaySearchByUserProcessor extends CommonProcessor<EssaySearchByUserInput, EssaySearchByUserOutput> {

    private final HowayLog logger = new HowayLog(EssaySearchByUserProcessor.class);
    private final EssayService essayService = new EssayServiceImpl();

    @Override
    protected StatusCode dataCheck(EssaySearchByUserInput input){
        if(input.inChomm.type == null){
            logger.info("EssaySearchProcessor dataCheck error:必输字段缺少");
            return StatusCode.FIELDMISSING;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult beforeProcess(EssaySearchByUserInput input, EssaySearchByUserOutput output) {
        if(input.inChomm.size == null){
            input.inChomm.size = 10;
        }
        if(input.inChomm.index == null){
            input.inChomm.index = 0;
        }
        return HowayResult.createSuccessResult(output);
    }

    @Override
    protected HowayResult process(EssaySearchByUserInput input, EssaySearchByUserOutput output) {
        HashMap<String, Object> user = getUserByToken(input.token);
        Integer userId = (Integer) user.get("id");
        output.essayList = essayService.findByUser(input.inChomm.type,userId,input.inChomm.index,input.inChomm.size);
        return HowayResult.createSuccessResult(output);
    }
}
