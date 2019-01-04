package com.example.twinkle.enity;

public class BuyRecord {	//购买记录实体类
    private String SongID;
    private String BuyTime;
    public String getSongID() {
        return SongID;
    }
    public void setSongID(String songID) {
        SongID = songID;
    }
    public String getBuyTime() {
        return BuyTime;
    }
    public void setBuyTime(String buyTime) {
        BuyTime = buyTime;
    }
}
