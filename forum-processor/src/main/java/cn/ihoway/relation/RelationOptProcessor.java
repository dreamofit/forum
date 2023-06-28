package cn.ihoway.relation;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.Relation;
import cn.ihoway.redis.RelationRedis;
import cn.ihoway.relation.io.RelationOptInput;
import cn.ihoway.relation.io.RelationOptOutput;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.RelationAct;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * 关注/拉黑/私信
 * 私信两分钟内允许撤回
 */
@Processor(name = "RelationOptProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class RelationOptProcessor extends CommonProcessor<RelationOptInput, RelationOptOutput> {

    private final HowayLog logger = new HowayLog(RelationOptProcessor.class);
    private RelationRedis relationRedis = new RelationRedis();

    @Override
    protected StatusCode dataCheck(RelationOptInput input){
        //私信时，私信内容不能为空
        if(input.inChomm.action == RelationAct.CHAT && StringUtils.isBlank(input.inChomm.content)){
            logger.info("私信内容为空！");
            return StatusCode.FIELDMISSING;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(RelationOptInput input, RelationOptOutput output) {
        if(input.inChomm.action == RelationAct.FOLLOW && StringUtils.isBlank(input.inChomm.group)){
            input.inChomm.group = "默认分组";
        }
        Relation relation = new Relation();
        HashMap<String, Object> user = getUserByToken(input.token);
        relation.setFirstId((Integer) user.get("id"));
        relation.setAction(input.inChomm.action.getType());
        relation.setGroup(input.inChomm.group);
        relation.setContent(input.inChomm.content);
        HashMap<String, Object> target = getUserById(input.inChomm.targetId,input);
        if(target == null){
            logger.info("目标对象不存在！" + input.inChomm.targetId);
            return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,"目标对象不存在!",output);
        }
        if(input.inChomm.targetId.equals(relation.getFirstId())){
            logger.info("目标对象不能为自己！");
            return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,"目标对象不能为自己!",output);
        }
        //todo 私信时判断对象自己是否达到了对方的允许私信权限，是否被对方拉黑，否则不允许私信
        relation.setSecondId(input.inChomm.targetId);
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        relation.setUpdateTime(dateFormat.format(new Date()));

        if(input.inChomm.type == 1){ //新增
            if(!relationRedis.add(relation)){
                return HowayResult.createFailResult(StatusCode.INSERTERROR,output);
            }
        }else {
            if(!relationRedis.delete(relation)){
                return HowayResult.createFailResult(StatusCode.DELETEERROR,output);
            }
        }
        return HowayResult.createSuccessResult(output);
    }
}
