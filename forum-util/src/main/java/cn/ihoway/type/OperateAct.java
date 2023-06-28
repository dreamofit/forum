package cn.ihoway.type;
/**
 * 0： 阅读 （用户已读文章或者评论）
 * 1：收藏
 * 2：点赞
 * 3：点踩（点赞和点踩互斥）
 * 4：举报
 * 5：转载（针对文章）
 * 6：评分
 */
public enum OperateAct {
    READ(0),
    FAVORITE(1),
    SUPPORT(2),
    OPPOSE(3),
    COMPLAIN(4),
    SHARE(5),
    SCORE(6);
    private int type;
    OperateAct(int type){
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static OperateAct toEnum(int type){
        for(OperateAct e : OperateAct.values()) {
            if(e.getType() == type){
                return e;
            }
        }
        return null;
    }
}
