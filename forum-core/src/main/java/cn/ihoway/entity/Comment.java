package cn.ihoway.entity;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import lombok.Data;

/**
 * comment 评论
 * @author howay
 */
@Data
public class Comment implements Serializable {
    private Integer id;

    /**
     * 1：一级评论  2：二级评论
     */
    private Integer type;

    /**
     * 一级评论属于哪个文章id，二级评论属于哪个一级评论
     */
    private Integer belongId;

    /**
     * 被回复的是哪个评论，一级评论可为空
     */
    private Integer repliedId;

    /**
     * 一级评论为所在楼，二级评论为所在层
     */
    private Integer floor;

    /**
     * 发表者id
     */
    private Integer publisher;

    /**
     * 内容
     */
    private String content;

    /**
     * 备用
     */
    private String backup;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 状态  0：正常  1：审核中  2：违规隐藏/被删除
     */
    private Integer status;

    private HashMap<String, Object> publisherInfo; //作者信息，查询作者id获取
    private List<Comment> layerList;
    private Integer layerTotalNum;

    @Serial
    private static final long serialVersionUID = 1L;
}