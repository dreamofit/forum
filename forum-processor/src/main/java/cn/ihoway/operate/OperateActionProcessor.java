package cn.ihoway.operate;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.Operate;
import cn.ihoway.operate.io.OperateActionInput;
import cn.ihoway.operate.io.OperateActionOutput;
import cn.ihoway.redis.OperateRedis;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.OperateAct;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;

import java.util.HashMap;

/**
 * todo 获取用户全部文章，获取文章的点赞数、热度、评分...
 * todo 获取用户行为（点赞、评论、收藏...）
 * todo 我的消息：获取用户的文章/评论的被动行为（被点赞/评论/收藏）
 * 用户行为
 */
@Processor(name = "OperateActionProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class OperateActionProcessor extends CommonProcessor<OperateActionInput, OperateActionOutput> {

    private final HowayLog logger = new HowayLog(OperateActionProcessor.class);
    private OperateRedis operateRedis = new OperateRedis();

    @Override
    protected StatusCode dataCheck(OperateActionInput input){
        if(input.inChomm.type == null || input.inChomm.optId == null || input.inChomm.action == null){
            return StatusCode.FIELDMISSING;
        }
        if(input.inChomm.action == OperateAct.SCORE){
            if(input.inChomm.score == null){
                logger.info("评分字段为空");
                return StatusCode.FIELDMISSING;
            }
            if(input.inChomm.score <= 0){
                return StatusCode.ILLEGALPARAMETER;
            }
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(OperateActionInput input, OperateActionOutput output) {
        Operate operate = new Operate();
        operate.setAction(input.inChomm.action.getType());
        if(input.inChomm.status != null){
            operate.setStatus(input.inChomm.status);
        }
        if(input.inChomm.score != null){
            operate.setScore(input.inChomm.score);
        }
        operate.setOptId(input.inChomm.optId);
        operate.setType(input.inChomm.type);
        HashMap<String, Object> user = getUserByToken(input.token);
        if(user == null){
            return HowayResult.createFailResult(StatusCode.USEREMPTY,output);
        }
        Integer id = (Integer) user.get("id");
        if(id == null){
            return HowayResult.createFailResult(StatusCode.USERSTATUSERROR,output);
        }
        operate.setUserId(id);
        operateRedis.add(operate);
        return HowayResult.createSuccessResult(output);
    }
}
