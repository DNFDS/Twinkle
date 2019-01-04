package com.example.twinkle.serverenity;

import java.util.Date;

public class ServerSongList {
    private String songlistid;

    private String songlistname;

    private String songlistimage;

    private String isprivate;

    private String userid;

    private Date createtime;

    public String getSonglistid() {
        return songlistid;
    }

    public void setSonglistid(String songlistid) {
        this.songlistid = songlistid == null ? null : songlistid.trim();
    }

    public String getSonglistname() {
        return songlistname;
    }

    public void setSonglistname(String songlistname) {
        this.songlistname = songlistname == null ? null : songlistname.trim();
    }

    public String getSonglistimage() {
        return songlistimage;
    }

    public void setSonglistimage(String songlistimage) {
        this.songlistimage = songlistimage == null ? null : songlistimage.trim();
    }

    public String getIsprivate() {
        return isprivate;
    }

    public void setIsprivate(String isprivate) {
        this.isprivate = isprivate == null ? null : isprivate.trim();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}