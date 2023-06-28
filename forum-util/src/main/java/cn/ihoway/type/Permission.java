package cn.ihoway.type;

/**
 * 权限类型
 */
public enum Permission {
    PRIVATE(0), //私有
    ONLY_FRIEND(1), //仅好友
    ONLY_FENS(2), //仅关注
    PUBLIC(3); //公开
    private int value;
    Permission(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
