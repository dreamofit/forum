package cn.ihoway.comment.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class CommentAddInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public Integer type; //1：一级评论  2：二级评论
        public Integer belongId; //一级评论属于哪个文章id，二级评论属于哪个一级评论
        public Integer repliedId; //被回复的是哪个评论，一级评论可为空
        public String content; //评论内容
    }
}
