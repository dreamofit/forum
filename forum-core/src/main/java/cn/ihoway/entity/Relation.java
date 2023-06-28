package cn.ihoway.entity;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;
import lombok.SneakyThrows;

/**
 * relation
 * @author howay
 */
@Data
public class Relation implements Serializable,Comparable<Relation> {
    private Integer id;

    /**
     * 主动用户id
     */
    private Integer firstId;

    /**
     * 被动用户id
     */
    private Integer secondId;

    /**
     * 行为类型：
        1：关注/取消关注
        2：拉黑/解除拉黑 （被拉黑者不能再向拉黑者文章、评论等进行各种操作，不能私信对方）
        3：私信/私信撤回
     */
    private Integer action;

    /**
     * 私信内容
     */
    private String content;

    /**
     * 关注者分组名称，action为关注时该字段为默认分组
     */
    private String group;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 私信序号
     */
    private Integer num;

    /**
     * 备用字段
     */
    private String backup;

    private static final long serialVersionUID = 1L;

    @SneakyThrows
    @Override
    public int compareTo(Relation o) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = sdf.parse(this.updateTime);
        Date date2 = sdf.parse(o.updateTime);
        return (int) ((date1.getTime() - date2.getTime()) / 1000);
    }
}