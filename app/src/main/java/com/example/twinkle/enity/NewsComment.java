package com.example.twinkle.enity;

public class NewsComment {	//评论实体类

    private String UserName;
    private String ID;
    private String UserID;
    private int UserCover;
    private int CommentAgreeCount;
    private String CommentTime;
    private String CommentText;
    private boolean IsAgree;
    private boolean IsReply;
    private String ReplyUserName;
    private String ReplyText;
    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }
    public String getID() {
        return ID;
    }
    public void setID(String iD) {
        ID = iD;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public int getUserCover() {
        return UserCover;
    }
    public void setUserCover(int userCover) {
        UserCover = userCover;
    }
    public int getCommentAgreeCount() {
        return CommentAgreeCount;
    }
    public void setCommentAgreeCount(int commentAgreeCount) {
        CommentAgreeCount = commentAgreeCount;
    }
    public String getCommentTime() {
        return CommentTime;
    }
    public void setCommentTime(String commentTime) {
        CommentTime = commentTime;
    }
    public String getCommentText() {
        return CommentText;
    }
    public void setCommentText(String commentText) {
        CommentText = commentText;
    }
    public boolean getIsAgree() {
        return IsAgree;
    }
    public void setIsAgree(boolean isAgree) {
        IsAgree = isAgree;
    }
    public boolean getIsReply() {
        return IsReply;
    }
    public void setIsReply(boolean isReply) {
        IsReply = isReply;
    }
    public String getReplyUserName() {
        return ReplyUserName;
    }
    public void setReplyUserName(String replyUserName) {
        ReplyUserName = replyUserName;
    }
    public String getReplyText() {
        return ReplyText;
    }
    public void setReplyText(String replyText) {
        ReplyText = replyText;
    }
}

