package com.example.twinkle.enity;

import java.io.Serializable;

public class Song implements Serializable {  //大势实体类

    private String SongID;
    private String SongPath;
    private String SongName;
    private String WriterName;
    private String ProducerName;
    private int SongImageId;
    private String Length;
    private int Playtimes;
    private String AlbumID;
    private String SingerID;
    private String SongSchool;
    private String SongAge;
    private String SingerName;
    private String AlbumName;
    private Boolean SongType;//true为云端，false为本地
    public Song (){
    }
    public Song (String ID, String name){
        this.SongID = ID;
        this.SongName = name;
    }

    public Song (String ID, String name, String singerName){
        this.SongID = ID;
        this.SongName = name;
        this.SingerName = singerName;
    }
    public Boolean getSongType() {
        return SongType;
    }
    public void setSongSongType(Boolean songType) {
        SongType = songType;
    }
    public String getSingerID() {
        return SingerID;
    }
    public void setSingerID(String singerID) {
        SingerID = singerID;
    }
    public int getSongImageId() {
        return SongImageId;
    }
    public void setSongImageId(int songImageId) {
        SongImageId = songImageId;
    }
    public String getAlbumName() {
        return AlbumName;
    }
    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }
    public String getSingerName() {
        return SingerName;
    }
    public void setSingerName(String singerName) {
        SingerName = singerName;
    }
    public String getSongID() {
        return SongID;
    }
    public void setSongID(String songID) {
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
    public String getWriterName() {
        return WriterName;
    }
    public void setWriterName(String writerName) {
        WriterName = writerName;
    }
    public String getProducerName() {
        return ProducerName;
    }
    public void setProducerName(String producerName) {
        ProducerName = producerName;
    }
    public String getLength() {
        return Length;
    }
    public void setLength(String length) {
        Length = length;
    }
    public int getPlaytimes() {
        return Playtimes;
    }
    public void setPlaytimes(int playtimes) {
        Playtimes = playtimes;
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
