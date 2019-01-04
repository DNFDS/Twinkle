package com.example.twinkle.serverenity;

import java.math.BigDecimal;

public class ServerSong {
    private String songid;

    private String songpath;

    private String songname;

    private String songimage;

    private String length;

    private Integer playtimes;

    private String albumid;

    private String songschool;

    private String songage;

    private String company;

    private String language;

    private BigDecimal savenum;

    public String getSongid() {
        return songid;
    }

    public void setSongid(String songid) {
        this.songid = songid == null ? null : songid.trim();
    }

    public String getSongpath() {
        return songpath;
    }

    public void setSongpath(String songpath) {
        this.songpath = songpath == null ? null : songpath.trim();
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname == null ? null : songname.trim();
    }

    public String getSongimage() {
        return songimage;
    }

    public void setSongimage(String songimage) {
        this.songimage = songimage == null ? null : songimage.trim();
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length == null ? null : length.trim();
    }

    public Integer getPlaytimes() {
        return playtimes;
    }

    public void setPlaytimes(Integer playtimes) {
        this.playtimes = playtimes;
    }

    public String getAlbumid() {
        return albumid;
    }

    public void setAlbumid(String albumid) {
        this.albumid = albumid == null ? null : albumid.trim();
    }

    public String getSongschool() {
        return songschool;
    }

    public void setSongschool(String songschool) {
        this.songschool = songschool == null ? null : songschool.trim();
    }

    public String getSongage() {
        return songage;
    }

    public void setSongage(String songage) {
        this.songage = songage == null ? null : songage.trim();
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language == null ? null : language.trim();
    }

    public BigDecimal getSavenum() {
        return savenum;
    }

    public void setSavenum(BigDecimal savenum) {
        this.savenum = savenum;
    }
}