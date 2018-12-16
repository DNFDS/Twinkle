package com.example.twinkle;

public class Song {  //大势实体类

    private int SongID;
    private String SongPath;
    private String SongName;
    private String SingerName;
    private String SongImage;
    private String Length;
    private int PlayTimes;
    private String AlbumID;
    private String SongSchool;
    private String SongAge;

    public Song (int ID, String name){
        this.SongID = ID;
        this.SongName = name;
    }

    public Song (int ID, String name, String singerName){
        this.SongID = ID;
        this.SongName = name;
        this.SingerName = singerName;
    }

    public int getSongID() {
        return SongID;
    }
    public void setSongID(int songID) {
        SongID = songID;
    }
    public String getSongPath() {
        return SongPath;
    }
    public void setSongPath(String songPath) {
        SongPath = songPath;
    }
    public String getSongName() {
        return SongName;
    }
    public void setSongName(String songName) {
        SongName = songName;
    }
    public String getSingerName() {
        return SingerName;
    }
    public void setSingerName(String singerName) {
        SingerName = singerName;
    }
    public String getSongImage() {
        return SongImage;
    }
    public void setSongImage(String songImage) {
        SongImage = songImage;
    }
    public String getLength() {
        return Length;
    }
    public void setLength(String length) {
        Length = length;
    }
    public int getPlayTimes() {
        return PlayTimes;
    }
    public void setPlayTimes(int playTimes) {
        PlayTimes = playTimes;
    }
    public String getAlbumID() {
        return AlbumID;
    }
    public void setAlbumID(String albumID) {
        AlbumID = albumID;
    }
    public String getSongSchool() {
        return SongSchool;
    }
    public void setSongSchool(String songSchool) {
        SongSchool = songSchool;
    }
    public String getSongAge() {
        return SongAge;
    }
    public void setSongAge(String songAge) {
        SongAge = songAge;
    }
}
