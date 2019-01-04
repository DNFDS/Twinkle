package com.example.twinkle.enity;

import java.io.Serializable;

public class SongList implements Serializable{  //歌单实体类

    private String SongListID;
    private String SongListName;
    private int SongListImageId;
    private String IsPrivate;
    private String UserID;
    private String UserName;
    private String Songs_count;
    private boolean SonglistIsChecked;

    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
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
    public boolean getSonglistIsChecked() {
        return SonglistIsChecked;
    }
    public void setSonglistIsChecked(boolean songlistIsChecked) {
        SonglistIsChecked = songlistIsChecked;
    }
}
