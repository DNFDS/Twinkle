package com.example.twinkle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.Image;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "PlayerActivity";

    private Song playingSong;

    private ImageView image_return;
    private TextView text_title;
    private ImageView image_share;

    private ImageView image_cover;

    private SeekBar sb_progress;
    private ImageView image_star;
    private ImageView image_pre;
    private ImageView image_play;
    private ImageView image_next;
    private ImageView image_list;

    private Intent serviceIntent;
    private MyConnection connection;
    private MusicService.MyBinder musicControl;

    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
    private static final int UPDATE_PROGRESS = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_PROGRESS: {
                    updatePlayText();
                    updateProgress();
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Log.d(TAG, "onCreate");

        image_return = (ImageView) findViewById(R.id.return_btn);
        text_title = (TextView) findViewById(R.id.title_Text);
        image_share = (ImageView) findViewById(R.id.share_btn);

        image_cover = (ImageView) findViewById(R.id.cover_Image);

        sb_progress = (SeekBar) findViewById(R.id.progress_sb);
        image_star = (ImageView) findViewById(R.id.star_btn);
        image_pre = (ImageView) findViewById(R.id.pre_btn);
        image_play = (ImageView) findViewById(R.id.play_btn);
        image_next = (ImageView) findViewById(R.id.next_btn);
        image_list = (ImageView) findViewById(R.id.playingList_btn);

        image_return.setOnClickListener(this);
        image_share.setOnClickListener(this);
        image_star.setOnClickListener(this);
        image_pre.setOnClickListener(this);
        image_play.setOnClickListener(this);
        image_next.setOnClickListener(this);
        image_list.setOnClickListener(this);

        //播放进度调正
        sb_progress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    musicControl.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        connection = new MyConnection();

        serviceIntent = new Intent(this, MusicService.class);
        //给Service传播放歌曲名
        serviceIntent.putExtra("MusicTitle", "Madison Beer,(G)I-DLE,Jaira Burns - POP／STARS.mp3");

        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);

        InitView();
    }

    public static Boolean isServiceRunning(Context context, String ServiceName) {
        if (ServiceName.equals(""))
            return false;
        ActivityManager mManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningServiceInfos
                = (ArrayList<ActivityManager.RunningServiceInfo>) mManager.getRunningServices(Integer.MAX_VALUE);

        for (int i = 0; i < runningServiceInfos.size(); i++) {
            if (runningServiceInfos.get(i).getClass().getName().toString().equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    private void InitView() {
        //根据播放歌曲playingSong 的信息初始化Title等组件
//        image_cover.setImageResource(playingSong.getSongImage());
        image_cover.setImageResource(R.drawable.cover_pop_star);
//        text_title.setText(playingSong.getSongName());
        text_title.setText("POP/STAR");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (musicControl != null) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (musicControl != null) {
            handler.sendEmptyMessage(UPDATE_PROGRESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onClick(View v) {
        Log.d("PlayerActivity", "onClick");
        switch (v.getId()) {
            case R.id.return_btn:
                finish();
                break;
            case R.id.share_btn:
                break;
            case R.id.star_btn:
                break;
            case R.id.pre_btn:
                stopService(serviceIntent);
                serviceIntent.putExtra("MusicTitle", "Joan Jett & the Blackhearts - I Hate Myself For Loving You.mp3Joan Jett & the Blackhearts - I Hate Myself For Loving You.mp3");
                startService(serviceIntent);
                bindService(serviceIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.play_btn:
                musicControl.play();
                break;
            case R.id.next_btn:
                stopService(serviceIntent);
                serviceIntent.putExtra("MusicTitle", "尤长靖,那吾克热LIL-EM - 飘向北方 (Live).mp3");
                startService(serviceIntent);
                bindService(serviceIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.playingList_btn:
                break;
            default:
                break;
        }
    }

    private class MyConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MyBinder) service;
            updatePlayText();
            //设置进度条的最大值
            sb_progress.setMax(musicControl.getDuration());
            //设置进度条的进度
            sb_progress.setProgress(musicControl.getCurrentPosition());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    }

    private void updateProgress() {
        int currentPosition = musicControl.getCurrentPosition();
        sb_progress.setProgress(currentPosition);

        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 10000);
    }

    public void updatePlayText() {
        if (musicControl.isPlaying()) {
            image_play.setImageResource(R.drawable.pause);
        } else {
            image_play.setImageResource(R.drawable.play);
        }
        handler.sendEmptyMessage(UPDATE_PROGRESS);
    }

    public void play(View view) {
        musicControl.play();
        updatePlayText();
        updateProgress();
    }
}
