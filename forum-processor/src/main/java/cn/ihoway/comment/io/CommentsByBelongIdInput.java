package cn.ihoway.comment.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class CommentsByBelongIdInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public Integer essayId; //文章id不为空，优先查文章下面一级评论
        public Integer commentId; //查询一级评论下面的二级评论
        public Integer floorIndex; //一级评论分页查询起始值
        public Integer floorSize; //一级评论分页查询数量
        public Integer layerIndex; //二级评论分页查询起始值
        public Integer layerSize; //二级评论分页查询数量
    }
}
