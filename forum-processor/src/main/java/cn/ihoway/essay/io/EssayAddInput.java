package cn.ihoway.essay.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class EssayAddInput extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria{
        public String title;//标题
        public String content; //内容
        public String text; //富文本内容
        public Integer author;//作者
        public String imgs;//图片地址，用分号隔开
        public Integer type;//0:资讯类  1：论坛类  2：动态类
        public String url;//文章地址
        public Integer readPermissions;
        public Integer commentPermission;//评论权限：0：私有  1：仅好友   2：仅关注人  3：公开
        public String label;//文章标签，用空格隔开，最多自定义五个标签，每个标签长度不超过8个字
        public String topic;//话题分类，最多定义5个话题，每个话题不超过15个字,用空格分开
    }
}
