package cn.ihoway.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

/**
 * operate  用户对文章或者评论进行收藏、点赞等操作
 * 唯一索引：Idx_Four (user_id,type,opt_id,action)
 * @author howay
 */
@Data
public class Operate implements Serializable {

    public Operate(){
        this.status = 1;
        SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.updateTime = dateFormat.format(new Date());
    }

    private Integer id;

    private Integer userId;

    /**
     * 类型，0：文章  1：评论
     */
    private Integer type;

    /**
     * 文章/评论id
     */
    private Integer optId;

    /**
     * 0：阅读 （针对文章）
     * 1：收藏（针对文章，允许取消）
     * 2：点赞（允许取消）
     * 3：点踩（点赞和点踩互斥，允许取消）
     * 4：举报 新增字段：举报内容
     * 5：转载（针对文章，允许取消） 新增字段：转载内容
     * 6：评分（针对文章）
     */
    private Integer action;

    /**
     * 0：未行动   1：已行动  默认1
     */
    private Integer status;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 评分
     */
    private Integer score;

    /**
     * 转载内容
     */
    private String reprinted;

    /**
     * 举报内容
     */
    private String complaints;

    /**
     * 备用字段
     */
    private String backup;

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Operate operate) {
            return userId.equals(operate.getUserId()) && type.equals(operate.getType())
                    && optId.equals(operate.getOptId()) && action.equals(operate.getAction())
                    && status.equals(operate.getStatus());
        }
        return false;
    }

}