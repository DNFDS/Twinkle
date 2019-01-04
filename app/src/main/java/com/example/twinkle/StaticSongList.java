package com.example.twinkle;

import android.os.Environment;

import com.example.twinkle.enity.Song;

import java.util.ArrayList;

public class StaticSongList {
    static public ArrayList<Song> musicList_Local = new ArrayList<Song>();
    static public ArrayList<Song> musicList_Stars = new ArrayList<Song>();
    static public ArrayList<Song> musicList_Downloads = new ArrayList<Song>();
    static public ArrayList<Song> musicList_History = new ArrayList<Song>();

    public static void Init(){

        if (musicList_Local.size()!=0) return;

        musicList_Local.add(new Song(1+"","POP/STARS","Madison Beer"));
        musicList_Local.add(new Song(2+"","I Hate Myself For Loving You.mp3","Joan Jett & the Blackhearts - "));
        musicList_Local.add(new Song(3+"","不记年","妖言君"));
        musicList_Local.add(new Song(4+"","昨日青空","尤长靖"));
        musicList_Local.add(new Song(5+"","飘向北方 (Live)","尤长靖,那吾克热LIL-EM"));
        musicList_Local.add(new Song(6+"","通天大道宽又阔.mp3","崔京浩,三叶草演唱组"));
        String basePath = Environment.getExternalStorageDirectory()+ "/Music/";
        musicList_Local.get(0).setSongPath("/sdcard/Music/Madison Beer,(G)I-DLE,Jaira Burns - POP／STARS.mp3");
        musicList_Local.get(1).setSongPath("/sdcard/Music/Joan Jett & the Blackhearts - I Hate Myself For Loving You.mp3");
        musicList_Local.get(2).setSongPath(basePath + "妖言君 - 不记年.mp3");
        musicList_Local.get(3).setSongPath(basePath + "尤长靖 - 昨日青空.mp3");
        musicList_Local.get(4).setSongPath(basePath + "尤长靖,那吾克热LIL-EM - 飘向北方 (Live).mp3");
        musicList_Local.get(5).setSongPath(basePath + "崔京浩,三叶草演唱组 - 通天大道宽又阔.mp3");
        musicList_Local.get(0).setSongImageId(R.drawable.default_cover);
        musicList_Local.get(1).setSongImageId(R.drawable.default_cover);
        musicList_Local.get(2).setSongImageId(R.drawable.default_cover);
        musicList_Local.get(3).setSongImageId(R.drawable.default_cover);
        musicList_Local.get(4).setSongImageId(R.drawable.default_cover);
        musicList_Local.get(5).setSongImageId(R.drawable.default_cover);
        musicList_Local.get(0).setSongSongType(false);
        musicList_Local.get(1).setSongSongType(false);
        musicList_Local.get(2).setSongSongType(false);
        musicList_Local.get(3).setSongSongType(false);
        musicList_Local.get(4).setSongSongType(false);
        musicList_Local.get(5).setSongSongType(false);
        musicList_Stars.add(new Song(1+"","POP/STARS"));
        musicList_Stars.add(new Song(1+"","Cornfield Chase"));

        musicList_History.add(new Song(1+"","POP/STARS"));
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