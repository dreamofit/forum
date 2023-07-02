package cn.ihoway.comment;

import cn.ihoway.annotation.Processor;
import cn.ihoway.api.user.UserAsm;
import cn.ihoway.comment.io.CommentsByBelongIdInput;
import cn.ihoway.comment.io.CommentsByBelongIdOutput;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.entity.Comment;
import cn.ihoway.impl.CommentServiceImpl;
import cn.ihoway.service.CommentService;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.HowayConfigReader;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.MDC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** todo 需要显示点赞数量和点踩数量
 * 查询指定文章下面评论
 */
@Processor(name = "CommentsByEssayProcessor")
public class CommentsByBelongIdProcessor extends CommonProcessor<CommentsByBelongIdInput, CommentsByBelongIdOutput> {
    private final HowayLog logger = new HowayLog(CommentsByBelongIdProcessor.class);
    private final CommentService commentService = new CommentServiceImpl();

    @Override
    protected StatusCode dataCheck(CommentsByBelongIdInput input){
        if(input.inChomm.essayId == null && input.inChomm.commentId == null){
            logger.info("CommentsByEssayProcessor dataCheck error:必输字段缺少");
            return StatusCode.FIELDMISSING;
        }
        if(input.inChomm.layerIndex == null){
            input.inChomm.layerIndex = 0;
        }
        if(input.inChomm.floorSize == null){
            input.inChomm.floorSize = Integer.parseInt(HowayConfigReader.getConfig("howay.properties","comment.floorSize"));
        }
        if(input.inChomm.layerSize == null){
            input.inChomm.layerSize = Integer.parseInt(HowayConfigReader.getConfig("howay.properties","comment.layerSize"));
        }
        if(input.inChomm.floorIndex == null){
            input.inChomm.floorIndex = 0;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult beforeProcess(CommentsByBelongIdInput input, CommentsByBelongIdOutput output){
        if(StringUtils.isBlank(input.token)){
            //降低查询评论的数量
            if(input.inChomm.commentId != null && input.inChomm.essayId == null){
                return HowayResult.createFailResult(StatusCode.PERMISSIONDENIED,"请登录后再进行操作",output);
            }
            limitPermission(input);
            return HowayResult.createSuccessResult(output);
        }
        HashMap<String, Object> res = getIsTokenRule(input, AuthorityLevel.COMMONMEMBER);
        if((Integer)res.get("code") != 200){
            if(input.inChomm.commentId != null && input.inChomm.essayId == null){
                return HowayResult.createFailResult(StatusCode.PERMISSIONDENIED,"请登录后再进行操作",output);
            }
            limitPermission(input);
            return HowayResult.createSuccessResult(output);
        }
        return HowayResult.createSuccessResult(output);
    }

    @Override
    protected HowayResult process(CommentsByBelongIdInput input, CommentsByBelongIdOutput output) {
        if(input.inChomm.essayId != null){
            return SearchByEssay(input, output);
        }else{
            return SearchByComment(input,output);
        }
    }

    //查询文章下面一级评论
    private HowayResult SearchByEssay(CommentsByBelongIdInput input, CommentsByBelongIdOutput output) {
        HashMap<Integer, Object> userCache = new HashMap<>(); //用戶緩存
        UserAsm userAsm = (UserAsm) HowayContainer.getContext().getBean("UserAsm");
        List<Comment> floorList = commentService.findCommentsByBelongId(input.inChomm.essayId,1, input.inChomm.floorIndex, input.inChomm.floorSize);
        List<Comment> newFloorList = new ArrayList<>();
        for(Comment floor:floorList){
            List<Comment> layerList = commentService.findCommentsByBelongId(floor.getId(),2, 0, 2);
            List<Comment> newLayerList = new ArrayList<>();
            for (Comment layer:layerList){
                setUserInfo(userCache, userAsm, layer);
                newLayerList.add(layer);
            }
            int total = commentService.findCommentNumByBelongId(floor.getId(),2);
            floor.setLayerTotalNum(total);
            floor.setLayerList(newLayerList);
            setUserInfo(userCache,userAsm,floor);
            newFloorList.add(floor);
        }
        commentService.free();
        output.commentList = newFloorList;
        return HowayResult.createSuccessResult(output);
    }

    //查询一级评论下面二级评论
    private HowayResult SearchByComment(CommentsByBelongIdInput input, CommentsByBelongIdOutput output) {
        HashMap<Integer, Object> userCache = new HashMap<>();
        UserAsm userAsm = (UserAsm) HowayContainer.getContext().getBean("UserAsm");
        List<Comment> layerList = commentService.findCommentsByBelongId(input.inChomm.commentId,2, input.inChomm.layerIndex, input.inChomm.layerSize);
        List<Comment> newLayerList = new ArrayList<>();
        for (Comment layer:layerList){
            setUserInfo(userCache, userAsm, layer);
            newLayerList.add(layer);
        }
        commentService.free();
        output.layerList = newLayerList;
        return HowayResult.createSuccessResult(output);
    }


    private void setUserInfo(HashMap<Integer, Object> userCache, UserAsm userAsm, Comment comment) {
        Integer id = comment.getPublisher();
        HashMap<String, Object> publisher;
        if(userCache.containsKey(id)){
            publisher = (HashMap<String, Object>) userCache.get(id);
        }else{
            String eventNo = getEventNo();
            publisher = userAsm.getUserById(comment.getPublisher(), eventNo, (String) MDC.get("traceId"));
            if(publisher != null) {
                userCache.put(comment.getPublisher(), publisher);
            }
        }
        if(publisher != null){
            comment.setPublisherInfo(publisher);
        }
    }

    /**
     * 未登录或无权限用户可以查看少数数据
     * @param input input
     */
    private void limitPermission(CommentsByBelongIdInput input){
        if (input.inChomm.floorIndex > 0){
            input.inChomm.floorIndex = 0;
        }
        if (input.inChomm.floorSize > Integer.parseInt(HowayConfigReader.getConfig("howay.properties","comment.limit.floorSize"))){
            input.inChomm.floorSize = Integer.parseInt(HowayConfigReader.getConfig("howay.properties","comment.limit.floorSize"));
        }
        if (input.inChomm.layerIndex > 0){
            input.inChomm.layerIndex = 0;
        }
        if(input.inChomm.layerSize > Integer.parseInt(HowayConfigReader.getConfig("howay.properties","comment.limit.layerSize"))){
            input.inChomm.layerSize = Integer.parseInt(HowayConfigReader.getConfig("howay.properties","comment.limit.layerSize"));
        }
    }
}
