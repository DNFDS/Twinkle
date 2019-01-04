package com.example.twinkle.enity;

import java.io.Serializable;

public class User implements Serializable {  //用户实体类

    private String UserID;
    private String UserName;
    private String UserPassword;
    private int UserImage;
    private int UserFollowCount;
    private boolean IsFollow;
    private String IsVIP;
    public boolean getIsFollow() {
        return IsFollow;
    }

    public void setIsFollow(boolean isFollow) {
        IsFollow = isFollow;
    }
    public int getUserFollowCount() {
        return UserFollowCount;
    }

    public void setUserFollowCount(int userFollowCount) {
        UserFollowCount = userFollowCount;
    }
    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID=userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public int getUserImage() {
        return UserImage;
    }

    public void setUserImage(int userImage) {
        UserImage = userImage;
    }

    public String getIsVIP() {
        return IsVIP;
    }

    public void setIsVIP(String isVIP) {
        IsVIP = isVIP;
    }

}
