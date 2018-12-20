package com.example.twinkle;

public class User {  //用户实体类

    private String UserID;
    private String UserName;
    private String UserPassword;
    private String Password_old;
    private int UserImage;
    private String UserSex;
    private boolean Sex;
    private char IsVIP;
    private double UserBalance;
    private char IsBanned;

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

    public String getPassword_old() {
        return Password_old;
    }

    public void setPassword_old(String password_old) {
        Password_old = password_old;
    }

    public int getUserImage() {
        return UserImage;
    }

    public void setUserImage(int userImage) {
        UserImage = userImage;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String userSex) {
        UserSex = userSex;
    }

    public boolean isSex() {
        return Sex;
    }

    public void setSex(boolean sex) {
        Sex = sex;
    }

    public char getIsVIP() {
        return IsVIP;
    }

    public void setIsVIP(char isVIP) {
        IsVIP = isVIP;
    }

    public double getUserBalance() {
        return UserBalance;
    }

    public void setUserBalance(double userBalance) {
        UserBalance = userBalance;
    }

    public char getIsBanned() {
        return IsBanned;
    }

    public void setIsBanned(char isBanned) {
        IsBanned = isBanned;
    }
}
