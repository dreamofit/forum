package cn.ihoway.entity;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * settings个人设置
 * @author howay
 */
@Data
public class Settings implements Serializable {
    private Integer id;

    private Integer userId;

    /**
     * 空间访问权限
     */
    private Integer readPermission;

    /**
     * 私信权限
     */
    private Integer chatPermission;

    /**
     * 空间动态评论权限
     */
    private Integer commentPermission;

    /**
     * 查看范围
        0：全部
        1：最近三天
        2：最近一个月
        3：最近半年
     */
    private Integer readRange;

    /**
     * 空间样式
     */
    private String styles;

    /**
     * 消息通知
        0：关闭，1：我关注人的消息，2：全部消息
     */
    private Integer notify;

    /**
     *  0:正常模式
        1：深色模式
     */
    private Integer background;

    /**
     * 0:中文
     */
    private Integer language;

    /**
     * 个性化推荐
        0：开启 1：关闭
     */
    private Integer recommend;

    /**
     * 备用字段
     */
    private String backup;

    @Serial
    private static final long serialVersionUID = 1L;
}