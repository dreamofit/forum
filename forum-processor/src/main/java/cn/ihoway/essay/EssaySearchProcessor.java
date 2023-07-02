package cn.ihoway.essay;

import cn.ihoway.annotation.Processor;
import cn.ihoway.api.user.UserAsm;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.entity.Essay;
import cn.ihoway.essay.io.EssaySearchInput;
import cn.ihoway.essay.io.EssaySearchOutput;
import cn.ihoway.impl.EssayServiceImpl;
import cn.ihoway.service.EssayService;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.log4j.MDC;

import java.util.HashMap;
import java.util.List;

/**
 * 文章查询处理器
 * todo 加入排序类型，按时间排序、按热度排序、按评分/质量排序
 * todo 热度解释：由 评论数*1.3+点赞数*1.1+收藏数*1.5+转载数*1.2+阅读数 进行合计
 * todo 评分：对文章进行评分（1-10），取平均分 (对文章有效)
 * todo 质量：点赞数*1.2-点踩数 （对一级评论有效）
 * todo 查询的过程需要将部分信息返回出去，比如评分、评论数、点赞数、收藏数、转载数、阅读数
 */
@Processor(name = "EssaySearchProcessor")
public class EssaySearchProcessor extends CommonProcessor<EssaySearchInput, EssaySearchOutput> {

    private final HowayLog logger = new HowayLog(EssaySearchProcessor.class);
    private final EssayService essayService = new EssayServiceImpl();

    @Override
    protected StatusCode dataCheck(EssaySearchInput input){
        if(input.inChomm.type == null){
            logger.info("EssaySearchProcessor dataCheck error:必输字段缺少");
            return StatusCode.FIELDMISSING;
        }
        if(input.inChomm.index == null){
            input.inChomm.index = 0;
        }
        if(input.inChomm.size == null || input.inChomm.size < 0){
            input.inChomm.size = 10;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(EssaySearchInput input, EssaySearchOutput output) {
        List<Essay> essayList = essayService.findAll(input.inChomm.type,input.inChomm.index,input.inChomm.size);
        HashMap<Integer, Object> userCache = new HashMap<>();
        UserAsm userAsm = (UserAsm) HowayContainer.getContext().getBean("UserAsm");
        for (Essay essay:essayList){
            setUserInfo(userCache,userAsm,essay);
            String content = essay.getContent().replaceAll("<[^>]+>", "");
            essay.setContent(content.replaceAll("&nbsp;",""));
            output.essayList.add(essay);
        }
        essayService.free();
        return HowayResult.createSuccessResult(output);
    }

    private void setUserInfo(HashMap<Integer, Object> userCache, UserAsm userAsm, Essay essay) {
        Integer id = essay.getAuthor();
        HashMap<String, Object> author;
        if(userCache.containsKey(id)){
            author = (HashMap<String, Object>) userCache.get(id);
        }else{
            String eventNo = getEventNo();
            author = userAsm.getUserById(id, eventNo, (String) MDC.get("traceId"));
            if(author != null) {
                userCache.put(id, author);
            }
        }
        if(author != null){
            essay.setAuthorInfo(author);
        }
    }

}
