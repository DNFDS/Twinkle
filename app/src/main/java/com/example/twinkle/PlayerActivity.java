package com.example.twinkle;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "PlayerActivity";

    private ImageView image_cover;
    private TextView text_title;
    private Button btn_play;
    private SeekBar sb_progress;

    private MyConnection connection;
    private MusicService.MyBinder musicControl;

    private static final int UPDATE_PROGRESS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Log.d(TAG,"onCreate");

        image_cover = (ImageView)findViewById(R.id.cover_Image);
        text_title = (TextView)findViewById(R.id.title_Text);
        btn_play = (Button)findViewById(R.id.play_btn);
        sb_progress = (SeekBar)findViewById(R.id.progress_sb);

        Intent intent = new Intent(this,MusicService.class);
        intent.putExtra("MusicTitle","Madison Beer,(G)I-DLE,Jaira Burns - POP／STARS.mp3");

        connection = new MyConnection();

        startService(intent);
        bindService(intent,connection,BIND_AUTO_CREATE);

        btn_play.setOnClickListener(this);

        //播放进度调正
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                    musicControl.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        InitView();
    }

    private void InitView(){
        image_cover.setImageResource(R.drawable.cover_pop_star);
        text_title.setText("POP/STAR");
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATE_PROGRESS:
                    updateProgress();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_btn:
            {
                Log.d("PlayerActivity","onClick");
                musicControl.play();
                break;
            }
        }
    }

    private class MyConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MyBinder) service;
            updatePlayText();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicControl!=null){
            handler.sendEmptyMessage(UPDATE_PROGRESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    private void updateProgress(){
        int currentPosition = musicControl.getCurrentPosition();
        sb_progress.setProgress(currentPosition);

        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS,500);
    }

    public void updatePlayText(){
        if(musicControl.isPlaying()){
            btn_play.setText("Pause");
            handler.sendEmptyMessage(UPDATE_PROGRESS);
        }else{
            btn_play.setText("Play");
        }
    }
}
