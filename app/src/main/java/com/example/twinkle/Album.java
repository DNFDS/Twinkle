package com.example.twinkle;

public class Album {	//购买记录实体类
    private String AlbumID;
    private String AlbumName;
    private String SingerID;
    private String AlbumImage;
    private String AlbumAge;
    private String AdminID;
    public String getAlbumID() {
        return AlbumID;
    }
    public void setAlbumID(String albumID) {
        AlbumID = albumID;
    }
    public String getAlbumName() {
        return AlbumName;
    }
    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }
    public String getSingerID() {
        return SingerID;
    }
    public void setSingerID(String singerID) {
        SingerID = singerID;
    }
    public String getAlbumImage() {
        return AlbumImage;
    }
    public void setAlbumImage(String albumImage) {
        AlbumImage = albumImage;
    }
    public String getAlbumAge() {
        return AlbumAge;
    }
    public void setAlbumAge(String albumAge) {
        AlbumAge = albumAge;
    }
    public String getAdminID() {
        return AdminID;
    }
    public void setAdminID(String adminID) {
        AdminID = adminID;
    }
}
