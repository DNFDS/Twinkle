package com.example.twinkle.Service;

import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.example.twinkle.singleton.PlayingSongList;

import java.io.IOException;

public class MusicService extends Service {
    private String musicPath = Environment.getExternalStorageDirectory() + "/Music/";
    private String musicTitle = null;
    private MediaPlayer player = null;

    private static String TAG = "PlayerActivity";

    private PlayingSongList playingSongList = PlayingSongList.getInstance();

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (player == null) {
            Log.d(TAG, "PlayerOnCreate");
            player = new MediaPlayer();
        }
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        player.pause();
        Log.d("PlayerActivity", "ServiceOnDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        return new MyBinder();
    }

    //自定义Binder
    public class MyBinder extends Binder {
        public MediaPlayer getPlayer(){
            return player;
        }

        public boolean isPlaying() {
            return player.isPlaying();
        }
        public void changeModel(){
            player.setLooping(!player.isLooping());
        }
        public Boolean getModel(){
            return player.isLooping();
        }
        public void OnClick_Play(ObjectAnimator objectAnimator) {
            Log.d(TAG, "onPlay");
            if (player == null) {
                newPlay();
            } else {
                if (player.isPlaying()) {
                    player.pause();
                    objectAnimator.pause();
                } else {
                    player.start();
                    objectAnimator.resume();
                }
            }
        }
        //从播放列表中移除当前歌曲
        public void remove(){
                playingSongList.remove(playingSongList.getIndexNow());
        }
        //从播放列表中移除指定歌曲
        public void  remove(int index){
            playingSongList.remove(index);
        }
        //停止播放
        public void stop(){
            if(player.isPlaying()){
                player.stop();
                player.seekTo(0);
            }
        }
        //获取当前歌曲的最大进度限制
        public int getDuration() {
            return player.getDuration();
        }
        //获取当前的播放进度
        public int getCurrentPosition() {
            return player.getCurrentPosition();
        }
        //调整当前的播放进度
        public void seekTo(int mesc) {
            player.seekTo(mesc);
        }
        //播放当前播放列表指定的当前歌曲
        public void newPlay() {
            //播放列表为空
            if(player == null) {
                Log.d(TAG,"playerNull");
                return;
            }

            if (playingSongList.getSize()==0) {
                Log.d(TAG, "playingList Empty");
                return;
            }
            //重置MediaPlayer
            player.reset();
            String path = playingSongList.getNow().getSongPath();
            try {
                player.setDataSource(path);
                player.prepare();
                Log.d(TAG,"OnPrepare");
            } catch (IOException e) {
                e.printStackTrace();
            }
            player.start();
            player.seekTo(0);
        }
    }
}
