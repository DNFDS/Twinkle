package com.example.twinkle.serverenity;

import java.util.Date;

public class ServerNewsComment {
    private String commentid;

    private String newsid;

    private String text;

    private Date commenttime;

    private String commenterid;

    private String parentid;

    private String parenttext;

    private String parenter;

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid == null ? null : commentid.trim();
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid == null ? null : newsid.trim();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public Date getCommenttime() {
        return commenttime;
    }

    public void setCommenttime(Date commenttime) {
        this.commenttime = commenttime;
    }

    public String getCommenterid() {
        return commenterid;
    }

    public void setCommenterid(String commenterid) {
        this.commenterid = commenterid == null ? null : commenterid.trim();
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid == null ? null : parentid.trim();
    }

    public String getParenttext() {
        return parenttext;
    }

    public void setParenttext(String parenttext) {
        this.parenttext = parenttext == null ? null : parenttext.trim();
    }

    public String getParenter() {
        return parenter;
    }

    public void setParenter(String parenter) {
        this.parenter = parenter == null ? null : parenter.trim();
    }
}