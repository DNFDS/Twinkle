package com.example.twinkle;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class MusicService extends Service {
    private String musicPath = Environment.getExternalStorageDirectory() +"/Music/";
    private String musicTitle = null;
    private MediaPlayer player = null;

    private static String TAG = "MusicService";

    public MusicService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(player==null) {
            musicTitle = intent.getStringExtra("MusicTitle");
            musicPath += musicTitle;

            Log.d(TAG, "onCreate");
            player = new MediaPlayer();
            try {
                player.setDataSource(musicPath);

                player.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }


    public class MyBinder extends Binder {
        public boolean isPlaying(){
            return player.isPlaying();
        }

        public void play(){
            Log.d(TAG,"onPlay");
            if(player.isPlaying()) {
                player.pause();
            }else{
                player.start();
            }
        }

        public int getDuration(){
            return player.getDuration();
        }

        public int getCurrentPosition(){
            return player.getCurrentPosition();
        }

        public void seekTo(int mesc){
            player.seekTo(mesc);
        }
    }
}
