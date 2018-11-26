package com.example.twinkle;

public class Singer {	//歌手实体类
    private String SingerID;
    private String SingerName;
    private String SingerImage;
    private String SingerSex;
    private String Region;
    private String Introduction;
    public String getSingerID() {
        return SingerID;
    }
    public void setSingerID(String singerID) {
        SingerID = singerID;
    }
    public String getSingerName() {
        return SingerName;
    }
    public void setSingerName(String singerName) {
        SingerName = singerName;
    }
    public String getSingerImage() {
        return SingerImage;
    }
    public void setSingerImage(String singerImage) {
        SingerImage = singerImage;
    }
    public String getSingerSex() {
        return SingerSex;
    }
    public void setSingerSex(String singerSex) {
        SingerSex = singerSex;
    }
    public String getRegion() {
        return Region;
    }
    public void setRegion(String region) {
        Region = region;
    }
    public String getIntroduction() {
        return Introduction;
    }
    public void setIntroduction(String introduction) {
        Introduction = introduction;
    }
}
