package cn.ihoway.essay;

import cn.ihoway.annotation.Processor;
import cn.ihoway.api.user.UserAsm;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.container.HowayContainer;
import cn.ihoway.entity.Essay;
import cn.ihoway.essay.io.EssayDetailInput;
import cn.ihoway.essay.io.EssayDetailOutput;
import cn.ihoway.impl.CommentServiceImpl;
import cn.ihoway.impl.EssayServiceImpl;
import cn.ihoway.service.CommentService;
import cn.ihoway.service.EssayService;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;

import java.util.HashMap;

/**
 * todo 需要判断读取权限
 * 指定文章具体细节处理器
 */
@Processor(name = "EssaySearchProcessor")
public class EssayDetailProcessor extends CommonProcessor<EssayDetailInput, EssayDetailOutput> {

    private final HowayLog logger = new HowayLog(EssayDetailProcessor.class);
    private final EssayService essayService = new EssayServiceImpl();
    private final CommentService commentService = new CommentServiceImpl();

    @Override
    protected StatusCode dataCheck(EssayDetailInput input){
        if(input.inChomm.essayId == null){
            logger.info("EssayDetailProcessor dataCheck error:必输字段缺少");
            return StatusCode.FIELDMISSING;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(EssayDetailInput input, EssayDetailOutput output) {
        Essay essay = essayService.findById(input.inChomm.essayId);
        if(essay != null){
            //todo 查询文章评论总数
            int total = commentService.findCommentNumByBelongId(input.inChomm.essayId,1);
            essay.setCommentTotalNum(total);
            UserAsm userAsm = (UserAsm)HowayContainer.getContext().getBean("UserAsm");
            String eventNo = getEventNo();
            logger.info("EssaySearchProcessor eventNo = " + eventNo);
            HashMap<String, Object> user = userAsm.getUserById(essay.getAuthor(), eventNo, input.traceId);
            if(user != null){
                essay.setAuthorInfo(user);
            }
        }
        essayService.free();
        commentService.free();
        output.essay = essay;
        return HowayResult.createSuccessResult(output);
    }

}
