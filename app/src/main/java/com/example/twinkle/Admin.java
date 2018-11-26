package com.example.twinkle;

public class Admin {	//管理员实体类

    private String AdminID;
    private String AdminName;
    private String AdminPassword;
    private String AdminImage;

    public String getAdminID() {
        return AdminID;
    }
    public void setAdminID(String adminID) {
        AdminID = adminID;
    }
    public String getAdminName() {
        return AdminName;
    }
    public void setAdminName(String adminName) {
        AdminName = adminName;
    }
    public String getAdminPassword() {
        return AdminPassword;
    }
    public void setAdminPassword(String adminPassword) {
        AdminPassword = adminPassword;
    }
    public String getAdminImage() {
        return AdminImage;
    }
    public void setAdminImage(String adminImage) {
        AdminImage = adminImage;
    }
}