package cn.ihoway.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

import lombok.Data;

/**
 * essay 文章
 * @author howay
 */
@Data
public class Essay implements Serializable {
    private Integer id;

    /**
     * 文章标题
     */
    private String title;

    private String content;

    /**
     * 作者id
     */
    private Integer author;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 图片地址，用分号隔开
     */
    private String imgs;

    /**
     * todo 0：管理员/平台发布的资讯、新闻等，阅读权限公开，评论权限允许关闭
     * todo 1：用户发布的文章，默认公开，阅读权限和评论权限都可以降级，
     *        阅读权限与评论权限分别降级，但是文章依然公开显示，只是详细内容被限制
     * todo 2：用户发布的动态，默认仅好友，阅读权限和评论权限可以同步降级和升级
     * 0:资讯类  1：论坛类  2：动态类
     */
    private Integer type;

    /**
     * 文章地址
     */
    private String url;

    /**
     * 阅读权限：0：私有  1：仅好友   2：仅关注人  3：公开
     */
    private Integer readPermissions;

    /**
     * 评论权限：0：私有  1：仅好友   2：仅关注人  3：公开
     */
    private Integer commentPermission;

    /**
     * 状态  0：正常  1：审核中  2：违规隐藏
     */
    private Integer status;

    /**
     * todo 标签需要新建数据库类，由平台进行管理
     * 文章标签，用##隔开，最多自定义五个标签，如#java##python#
     */
    private String label;

    /**
     * todo 话题由用户进行创建
     * 话题分类，最多定义5个话题,用##分开
     */
    private String topic;

    /**
     * 备用字段
     */
    private String backup;

    //无需写入数据库的
    private HashMap<String, Object> authorInfo; //作者信息，查询作者id获取
    private Integer commentTotalNum; //一级评论

    @Serial
    private static final long serialVersionUID = 1L;
}