package cn.ihoway.relation;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.Relation;
import cn.ihoway.redis.RelationRedis;
import cn.ihoway.relation.io.ChatContentInput;
import cn.ihoway.relation.io.ChatContentOutput;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * 获取用户和目标对象的全部聊天记录
 */
@Processor(name = "ChatContentProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class ChatContentProcessor extends CommonProcessor<ChatContentInput, ChatContentOutput> {

    private final HowayLog logger = new HowayLog(ChatContentProcessor.class);
    private RelationRedis relationRedis = new RelationRedis();

    @Override
    protected HowayResult process(ChatContentInput input, ChatContentOutput output) {
        HashMap<String, Object> user = getUserByToken(input.token);
        Integer firstId = (Integer) user.get("id");
        HashMap<String, Object> target = getUserById(input.inChomm.targetId,input);
        if(target == null){
            logger.info("目标对象不存在！" + input.inChomm.targetId);
            return HowayResult.createFailResult(StatusCode.ILLEGALPARAMETER,"目标对象不存在!",output);
        }
        //给目标对象的聊天记录
        List<Relation> firstList = relationRedis.getConditionMatch(firstId,2,input.inChomm.targetId,null);
        //获取目标对象的聊天记录
        List<Relation> secondList = relationRedis.getConditionMatch(input.inChomm.targetId,2,firstId,null);
        firstList.addAll(secondList);
        //排序
        Collections.sort(firstList);
        List<HashMap<String,String>> contents = new ArrayList<>();
        for(Relation relation : firstList){
            HashMap<String,String> content = new HashMap<>();
            content.put("content",relation.getContent());
            if(relation.getFirstId().equals(firstId)){
                content.put("sender",user.get("name").toString());
            }else {
                content.put("sender",target.get("name").toString());
            }
            content.put("time",relation.getUpdateTime());
            contents.add(content);
        }
        output.contents = contents;
        return HowayResult.createSuccessResult(output);
    }


}
