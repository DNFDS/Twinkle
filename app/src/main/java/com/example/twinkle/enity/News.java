package com.example.twinkle.enity;

import java.io.Serializable;

public class News implements Serializable{  //歌单实体类
    private String NewsId;
    private String NewsType;
    private String NewsContentId;
    private String NewsContent;
    private String NewsCreatorID;
    private String NewsCreatorName;
    private String NewsDate;
    private int NewsCreatorCover;
    private int ContentCover;
    private String ContentName;
    private String ContentCreator;
    private int SkimCount;
    private int AgreeCount;
    private int CommentCount;
    private int ForwardCount;
    private boolean IsAgree;
    private boolean IsShared;
    private String OriginalNewsId;//若不存在则为空
    private String OriginalCreator;//若不存在则为空
    private String OriginalComment;//若不存在则为空

    public String getNewsCreatorID() {
        return NewsCreatorID;
    }
    public void setNewsCreatorID(String newsCreatorID) {
        NewsCreatorID = newsCreatorID;
    }
    public String getNewsId() {
        return NewsId;
    }
    public void setNewsId(String newsId) {
        NewsId = newsId;
    }
    public String getOriginalNewsId() {
        return OriginalNewsId;
    }
    public void setOriginalNewsId(String originalNewsId) {
        OriginalNewsId = originalNewsId;
    }
    public boolean getIsShared() {
        return IsShared;
    }
    public void setIsShared(boolean isShared) {
        IsShared = isShared;
    }
    public boolean getIsAgree() {
        return IsAgree;
    }
    public void setIsAgree(boolean isAgree) {
        IsAgree = isAgree;
    }
    public String getNewsType() {
        return NewsType;
    }
    public void setNewsType(String newsType) {
        NewsType = newsType;
    }
    public int getCommentCount() {
        return CommentCount;
    }
    public void setCommentCount(int commentCount) {
        CommentCount = commentCount;
    }
    public int getForwardCount() {
        return ForwardCount;
    }
    public void setForwardCount(int forwardCount) {
        ForwardCount = forwardCount;
    }
    public int getAgreeCount() {
        return AgreeCount;
    }
    public void setAgreeCount(int agreeCount) {
        AgreeCount = agreeCount;
    }
    public int getSkimCount() {
        return SkimCount;
    }
    public void setSkimCount(int skimCount) {
        SkimCount = skimCount;
    }
    public int getNewsCreatorCover() {
        return NewsCreatorCover;
    }
    public void setNewsCreatorCover(int newsCreatorCover) {
        NewsCreatorCover = newsCreatorCover;
    }
    public String getNewsContentId() {
        return NewsContentId;
    }
    public void setNewsContentId(String newsContentId) {
        NewsContentId = newsContentId;
    }
    public String getNewsContent() {
        return NewsContent;
    }
    public void setNewsContent(String newsContent) {
        NewsContent = newsContent;
    }
    public String getNewsCreatorName() {
        return NewsCreatorName;
    }
    public void setNewsCreatorName(String newsCreatorName) {
        NewsCreatorName = newsCreatorName;
    }
    public String getNewsDate() {
        return NewsDate;
    }
    public void setNewsDate(String newsDate) {
        NewsDate = newsDate;
    }
    public int getContentCover() {
        return ContentCover;
    }
    public void setContentCover(int contentCover) {
        ContentCover = contentCover;
    }
    public String getContentName() {
        return ContentName;
    }
    public void setContentName(String contentName) {
        ContentName = contentName;
    }
    public String getContentCreator() {
        return ContentCreator;
    }
    public void setContentCreator(String contentCreator) { ContentCreator = contentCreator; }
    public String getOriginalCreator(){return  OriginalCreator;}
    public void setOriginalCreator(String originalCreator) { OriginalCreator = originalCreator; }
    public String getOriginalComment(){return  OriginalComment;}
    public void setOriginalComment(String originalComment) { OriginalComment = originalComment; }
}
