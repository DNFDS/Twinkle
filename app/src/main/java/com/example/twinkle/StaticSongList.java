package com.example.twinkle;

import java.util.ArrayList;

public class StaticSongList {
    static public ArrayList<Song> musicList_Local = new ArrayList<Song>();
    static public ArrayList<Song> musicList_Stars = new ArrayList<Song>();
    static public ArrayList<Song> musicList_Downloads = new ArrayList<Song>();
    static public ArrayList<Song> musicList_History = new ArrayList<Song>();

    public static void Init(){
        musicList_Local.add(new Song(1,"POP/STARS","Madison Beer"));
        musicList_Local.add(new Song(2,"Until You","Shayne Ward"));
        musicList_Local.add(new Song(3,"Cornfield Chase","Hans Zimmer"));
        musicList_Local.add(new Song(4,"Waiting For Love","Avicii"));
        musicList_Local.add(new Song(5,"Don't say lazy","樱高轻音部"));
        musicList_Local.add(new Song(6,"My Heart Will Go On","Celine Dion"));

        musicList_Stars.add(new Song(1,"POP/STARS"));
        musicList_Stars.add(new Song(1,"Cornfield Chase"));

        musicList_History.add(new Song(1,"POP/STARS"));
    }

    public static ArrayList<Song> getSongListByName(String name){
        switch (name){
            case "Local" : return musicList_Local;
            case "Stars" : return musicList_Stars;
            case "Downloads" : return musicList_Downloads;
            case "History" : return musicList_History;
        }
        return null;
    }

}
//Question:每个歌单拥有的应该是歌曲指针还是歌曲实体？—— 存储占用问题