package com.example.twinkle;

public class Comment {	//评论实体类

    private String UserID;
    private String SongID;
    private String CommentTime;
    private String CommentText;

    public String getUserID() {
        return UserID;
    }
    public void setUserID(String userID) {
        UserID = userID;
    }
    public String getSongID() {
        return SongID;
    }
    public void setSongID(String songID) {
        SongID = songID;
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
}

