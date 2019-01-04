package com.example.twinkle.serverenity;

import java.util.Date;

public class ServerNews {
    private String id;

    private String type;

    private String createrid;

    private Date createtime;

    private String text;

    private String contentid;

    private String forwardedid;

    private String forwardedtext;

    private String forwarder;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getCreaterid() {
        return createrid;
    }

    public void setCreaterid(String createrid) {
        this.createrid = createrid == null ? null : createrid.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text == null ? null : text.trim();
    }

    public String getContentid() {
        return contentid;
    }

    public void setContentid(String contentid) {
        this.contentid = contentid == null ? null : contentid.trim();
    }

    public String getForwardedid() {
        return forwardedid;
    }

    public void setForwardedid(String forwardedid) {
        this.forwardedid = forwardedid == null ? null : forwardedid.trim();
    }

    public String getForwardedtext() {
        return forwardedtext;
    }

    public void setForwardedtext(String forwardedtext) {
        this.forwardedtext = forwardedtext == null ? null : forwardedtext.trim();
    }

    public String getForwarder() {
        return forwarder;
    }

    public void setForwarder(String forwarder) {
        this.forwarder = forwarder == null ? null : forwarder.trim();
    }
}