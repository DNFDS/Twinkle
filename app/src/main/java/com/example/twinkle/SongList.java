package com.example.twinkle;

public class SongList {  //歌单实体类

    private String SongListID;
    private String SongListName;
    private int SongListImageId;
    private String IsPrivate;
    private String UserID;
    private String Songs_count;

    public String getSongListID() {
        return SongListID;
    }
    public void setSongListID(String songListID) {
        SongListID = songListID;
    }
    public String getSongListName() {
        return SongListName;
    }
    public void setSongListName(String songListName) {
        SongListName = songListName;
    }
    public int getSongListImageId() {
        return SongListImageId;
    }
    public void setSongListImageId(int songListImageId) {
        SongListImageId = songListImageId;
    }
    public String getIsPrivate() {
        return IsPrivate;
    }
    public void setIsPrivate(String isPrivate) {
        IsPrivate = isPrivate;
    }
    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) { UserID = userID; }
    public String getSongs_count(){return  Songs_count;}
    public void setSongs_count(String songs_count) { Songs_count = songs_count; }
}
