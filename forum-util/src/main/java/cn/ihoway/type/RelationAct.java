package cn.ihoway.type;

/**
 *
 */
public enum RelationAct {
    FOLLOW(0), //关注
    UNFRIEND(1),//拉黑
    CHAT(2); //私信

    private int type;
    RelationAct(int type){
        this.type = type;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static RelationAct toEnum(int type){
        for(RelationAct e : RelationAct.values()) {
            if(e.getType() == type){
                return e;
            }
        }
        return null;
    }
}
