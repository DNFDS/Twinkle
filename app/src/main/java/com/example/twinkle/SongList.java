package com.example.twinkle;

public class SongList {  //歌单实体类

    private String SongListID;
    private String SongListName;
    private String SongListImage;
    private String IsPrivate;
    private String UserID;

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
    public String getSongListImage() {
        return SongListImage;
    }
    public void setSongListImage(String songListImage) {
        SongListImage = songListImage;
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
    public void setUserID(String userID) {
        UserID = userID;
    }
}