package cn.ihoway.type;

public enum EssayType {
    INFORMATION(0),
    BLOG(1),
    DIARY(2);

    private int type;
    EssayType(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
