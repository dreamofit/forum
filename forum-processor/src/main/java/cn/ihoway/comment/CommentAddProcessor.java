package cn.ihoway.comment;

import cn.ihoway.annotation.Processor;
import cn.ihoway.comment.io.CommentAddInput;
import cn.ihoway.comment.io.CommentAddOutput;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.Comment;
import cn.ihoway.impl.CommentServiceImpl;
import cn.ihoway.service.CommentService;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;

@Processor(name = "CommentAddProcessor",certification = true,limitAuthority = AuthorityLevel.COMMONMEMBER)
public class CommentAddProcessor extends CommonProcessor<CommentAddInput, CommentAddOutput> {
    private final HowayLog logger = new HowayLog(CommentAddProcessor.class);
    private final CommentService commentService = new CommentServiceImpl();

    @Override
    protected StatusCode dataCheck(CommentAddInput input){
        if(input.inChomm.type == null || input.inChomm.belongId == null || StringUtils.isBlank(input.inChomm.content)){
            logger.info("CommentAddProcessor dataCheck error:缺少必输字段!");
            return StatusCode.FIELDMISSING;
        }else if(input.inChomm.type < 1 || input.inChomm.type > 2){
            logger.info("CommentAddProcessor dataCheck error:type is illegal");
            return StatusCode.ILLEGALPARAMETER;
        }else if(input.inChomm.type == 2 && input.inChomm.repliedId == null){
            logger.info("CommentAddProcessor dataCheck error:repliedId is null when type is 2!");
            return StatusCode.ILLEGALPARAMETER;
        }

        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult process(CommentAddInput input, CommentAddOutput output) {
        logger.info("CommentAddProcessor process begin");
        Comment comment = packToComment(input);
        HashMap<String, Object> user = getUserByToken(input.token);
        comment.setPublisher((Integer) user.get("id"));
        comment.setCreateTime(getCurrentTime(new Date(System.currentTimeMillis())));
        if(input.inChomm.type == 1){
            int rs = commentService.addFloorComment(comment);
            if(rs > 0){
                return HowayResult.createSuccessResult(output);
            }else if(rs < 0){
                return HowayResult.createFailResult(StatusCode.ILLEGALOPERATION,"该文章不存在，无法进行评论",output);
            }
        }else if(input.inChomm.type == 2){
            int rs = commentService.addLayerComment(comment);
            if(rs > 0){
                return HowayResult.createSuccessResult(output);
            }else if(rs < 0){
                return HowayResult.createFailResult(StatusCode.ILLEGALOPERATION,"该评论不存在，无法进行评论",output);
            }
        }
        return HowayResult.createFailResult(StatusCode.INSERTERROR,output);
    }

    private Comment packToComment(CommentAddInput input){
        Convert convert = new Convert();
        return (Comment) convert.inputToBean(Comment.class.getName(),CommentAddInput.class.getName()+"$InChomm",input.inChomm);
    }
}
