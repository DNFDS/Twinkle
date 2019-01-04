package com.example.twinkle.serverenity;

public class ServerSinger {
    private String singerid;

    private String singername;

    private String singerimage;

    private String singersex;

    private String region;

    private String introduction;

    public String getSingerid() {
        return singerid;
    }

    public void setSingerid(String singerid) {
        this.singerid = singerid == null ? null : singerid.trim();
    }

    public String getSingername() {
        return singername;
    }

    public void setSingername(String singername) {
        this.singername = singername == null ? null : singername.trim();
    }

    public String getSingerimage() {
        return singerimage;
    }

    public void setSingerimage(String singerimage) {
        this.singerimage = singerimage == null ? null : singerimage.trim();
    }

    public String getSingersex() {
        return singersex;
    }

    public void setSingersex(String singersex) {
        this.singersex = singersex == null ? null : singersex.trim();
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region == null ? null : region.trim();
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction == null ? null : introduction.trim();
    }
}