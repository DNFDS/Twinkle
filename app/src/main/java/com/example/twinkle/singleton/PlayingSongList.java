package com.example.twinkle.singleton;

import com.example.twinkle.enity.Song;

import java.util.ArrayList;
import java.util.List;

//懒汉模式实现的单例播放列表
//只能通过getInstance得到唯一实例
public class PlayingSongList {
    private static PlayingSongList list = new PlayingSongList();

    private ArrayList<Song> playingSongs = new ArrayList<Song>();

    private String playingSongListId;
    private String tempSongListId;

    private static int pointerHead;
    private int pointerIndex;
    private int tempindex;

    private PlayingSongList() {
        pointerHead = 0;
        pointerIndex = pointerHead;
        playingSongListId = "";
    }
    public Boolean isEmpty() {
        if (playingSongs.size()==0)
            return true;
        else
            return false;
    }
    //判断正在播放的音乐是否与选择播放的相同
    public Boolean ifEqual(){
        return (pointerIndex == tempindex)&&(playingSongListId==tempSongListId);
    }
    //修改Index所指位置
    public void setindex(){
        pointerIndex = tempindex;
        playingSongListId=tempSongListId;
    }
    //获取唯一实例
    public static PlayingSongList getInstance() {
        return list;
    }

    public ArrayList<Song> getList(){
        return playingSongs;
    }

    //初始化播放列表
    public void InitPlayingSongList(List<Song>listToPlay, String songListToPlay, int Index) {
        if (playingSongListId.equals(songListToPlay)&&isSame(listToPlay)) {

            tempindex = Index;
            return;
        }

        tempSongListId = songListToPlay;
        reset();

        for(int i = 0;i < listToPlay.size(); i++){
            playingSongs.add(listToPlay.get(i));
        }

        tempindex = Index;
    }
    //在末尾添加新的音乐
    public void addNew(Song newSong) {
        playingSongs.add(newSong);
    }
    //在下一首的位置添加新的音乐
    public void addNext(Song newSong){
        playingSongs.add(pointerIndex+1,newSong);
    }

    private Boolean isSame(List<Song>listToPlay){
        if(playingSongs.size()!=listToPlay.size())
            return false;
        for(int i =0; i<playingSongs.size()&&i<listToPlay.size();i++){
            if (playingSongs.get(i)!=listToPlay.get(i))
                return false;
        }
        return true;

    }

    public void reset(){
        playingSongs.clear();
        pointerIndex = pointerHead;

    }

    //自然播放结束或者切歌时
    //都要进行调整
    //以确保随时都可以定位到正在播放的歌曲
    public void moveToPre() {
        if (pointerIndex == 0)
            pointerIndex = this.getSize() - 1;
        else
            pointerIndex = pointerIndex - 1;
    }
    public void moveToNext() {
        if (pointerIndex == this.getSize() - 1)
            pointerIndex = pointerHead;
        else
            pointerIndex = pointerIndex + 1;
    }

    //得到当前正在播放的音乐实例
    public Song getNow() {
        return playingSongs.get(pointerIndex);
    }
    //得到当前上一首音乐实例
    public Song getPre() {
        moveToPre();
        return getNow();
    }
    //得到当前下一首的音乐实例
    public Song getNext() {
        moveToNext();
        return getNow();
    }
    public int getIndexNow(){
        return pointerIndex;
    }
    public void moveToIndex(int target){
        pointerIndex=target;
    }
    public void remove(int index){
        playingSongs.remove(index);
    }
    public  void removeALL(){
        playingSongs.clear();;
    }
    public int getSize() {
        return playingSongs.size();
    }

    //根据name寻找歌曲对应的Index
    //如果没有就返回-1
    public int getIndexByName(String songName) {
        for (int i = pointerHead; i < getSize() - 1; i++) {
            if (playingSongs.get(i).getSongName().equals(songName))
                return i;
        }
        return -1;
    }
}
