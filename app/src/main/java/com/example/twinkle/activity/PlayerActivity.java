package com.example.twinkle.activity;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.Service.MusicService;
import com.example.twinkle.adapter.PlayListAdapter;
import com.example.twinkle.adapter.PlayListAdapter.InnerItemOnclickListener;
import com.example.twinkle.enity.Song;
import com.example.twinkle.singleton.PlayingSongList;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener, OnItemClickListener, MediaPlayer.OnCompletionListener, InnerItemOnclickListener {
    private static String TAG = "PlayerActivity";

    private ImageView image_return;
    private TextView text_title;
    private ImageView image_share;
    private TextView songname;

    private ImageView image_cover;
    private ObjectAnimator objectAnimator;

    private SeekBar sb_progress;
    private ImageView image_star;
    private ImageView image_pre;
    private ImageView image_play;
    private ImageView image_next;
    private ImageView image_list;

    private MyConnection connection;
    private MusicService.MyBinder musicControl;

    private Intent serviceIntent = null;
    private SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss");
    private static final int UPDATE_PROGRESS = 0;

    private Song playingSong = null;
    private PlayingSongList playingSongList = PlayingSongList.getInstance();
    private PopupWindow playlistPopupWindow;
    private View playlistPopupView;
    private ListView playlistListView;
    private PlayListAdapter PlayListAdapter;
    private List<Song> playlist;
    private boolean mode;
    private ImageView mode_image;
    private ImageView clear_btn;
    private TextView play_mode_title;
    private String connecturl;
    private String currentUserID;
    private boolean islike;
    private Handler mhandler = null;
    private Runnable likeshow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //更换顶部颜色
        //保证界面风格统一
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mhandler = new Handler();
        likeshow = new Runnable() {
            @Override
            public void run() {
                //更新界面
                if (islike)
                    image_star.setImageResource(R.drawable.star_yes);
                else
                    image_star.setImageResource(R.drawable.star_no);
            }
        };
        setContentView(R.layout.activity_player);
        connecturl = getIntent().getStringExtra("connecturl");
        currentUserID = getIntent().getStringExtra("currentUserID");
        Log.d(TAG, "onCreate");
        image_return = (ImageView) findViewById(R.id.return_btn);
        text_title = (TextView) findViewById(R.id.title_Text);
        image_share = (ImageView) findViewById(R.id.share_btn);

        image_cover = (ImageView) findViewById(R.id.cover_Image);
        objectAnimator = ObjectAnimator.ofFloat(image_cover, "rotation", 0f, 360f);
        objectAnimator.setDuration(100000);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(-1);
        objectAnimator.start();

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
                if (fromUser) {
                    if (progress > musicControl.getDuration() - 1) {
                        progress = musicControl.getDuration() - 50;
                    }
                    musicControl.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        //启动并绑定Service
        connection = new MyConnection();
        serviceIntent = new Intent(this, MusicService.class);

        startService(serviceIntent);
        bindService(serviceIntent, connection, BIND_AUTO_CREATE);
        playlist = playingSongList.getList();
        initPopwindow();
        Log.d(TAG, "BIND");
    }

    //改变或者获取当前播放循环模式
    public void changeModel() {
        musicControl.changeModel();
    }

    public Boolean getModel() {
        return musicControl.getModel();
    }


    //更新当前界面的View 展示
    private void UpdateView() {
        text_title.setText(playingSongList.getNow().getSongName());
        image_cover.setImageResource(playingSongList.getNow().getSongImageId());
        islikesong();
        Log.d(TAG, "viewOnUpdate");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
        switch (v.getId()) {
            case R.id.return_btn:
                finish();
                break;
            case R.id.share_btn:
                break;
            case R.id.star_btn:
                likesong();
                if (islike) {
                    islike = false;
                    image_star.setImageResource(R.drawable.star_no);
                } else {
                    islike = true;
                    image_star.setImageResource(R.drawable.star_yes);
                }
                //更新界面
                break;
            case R.id.pre_btn:
                playPre();
                break;
            case R.id.play_btn:
                play();
                break;
            case R.id.next_btn:
                playNext();
                break;
            case R.id.playingList_btn:
                showPopWindow();
                setBackgroundAlpha(0.7f);
                break;
            case R.id.mode_image:
                changeModel();
                mode = getModel();
                if (!mode) {
                    mode_image.setImageResource(R.drawable.list_mode);
                    play_mode_title.setText("列表循环");
                } else {
                    mode_image.setImageResource(R.drawable.single_mode);
                    play_mode_title.setText("单曲循环");
                }
                break;
            case R.id.play_mode_title:
                changeModel();
                mode = getModel();
                if (!mode) {
                    mode_image.setImageResource(R.drawable.list_mode);
                    play_mode_title.setText("列表循环");
                } else {
                    mode_image.setImageResource(R.drawable.single_mode);
                    play_mode_title.setText("单曲循环");
                }
                break;
            case R.id.clear_image:
                musicControl.stop();
                playingSongList.removeALL();
                playlistPopupWindow.dismiss();
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        playNext();
    }

    //自定义Connection
    private class MyConnection implements ServiceConnection, MediaPlayer.OnCompletionListener {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            musicControl = (MusicService.MyBinder) service;
            musicControl.getPlayer().setOnCompletionListener(this);

            Log.d(TAG, "onInitPlayer");
            if (getIntent().getStringExtra("option").equals("next")) {
                musicControl.newPlay();
            } else {
                //防止切出去再返回后重新播放
                if (!playingSongList.ifEqual()) {
                    playingSongList.setindex();
                    musicControl.newPlay();
                }
            }
            Log.d(TAG, "onConnected");
            updatePlayText();

            //设置进度条的最大值
            sb_progress.setMax(musicControl.getDuration());
            //设置进度条的进度
            sb_progress.setProgress(musicControl.getCurrentPosition());

            UpdateView();
            handler.sendEmptyMessage(UPDATE_PROGRESS);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }

        @Override
        public void onCompletion(MediaPlayer mp) {
            playNext();
        }
    }

    //控制播放器播放或暂停
    private void play() {
        musicControl.OnClick_Play(objectAnimator);
        updatePlayText();
        updateProgress();
    }

    //控制播放器停止
    private void stop() {
        musicControl.stop();
        objectAnimator.pause();
    }

    //播放上一首
    private void playPre() {
        //从playingList中获取上一首歌的信息
        playingSongList.moveToPre();
        musicControl.newPlay();
        //设置进度条的最大值
        sb_progress.setMax(musicControl.getDuration());
        //设置进度条的进度
        sb_progress.setProgress(musicControl.getCurrentPosition());
        musicControl.seekTo(0);
        UpdateView();
    }

    //播放下一首
    private void playNext() {
        //从playingList中获取下一首歌的信息
        if (!getModel())
            playingSongList.moveToNext();
        musicControl.newPlay();
        //设置进度条的最大值
        sb_progress.setMax(musicControl.getDuration());
        //设置进度条的进度
        sb_progress.setProgress(musicControl.getCurrentPosition());
        musicControl.seekTo(0);
        UpdateView();
    }


    //收藏当前正在播放的歌曲
    private void likesong() {
        String url = connecturl + "/favoriteSong";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", currentUserID);
        paramsMap.put("songid", playlist.get(playingSongList.getIndexNow()).getSongID());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue", var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String, Object> params = com.alibaba.fastjson.JSONObject.parseObject(responseStr, new TypeReference<Map<String, Object>>() {
                });
            }
        });
    }

    //判断当前正在播放的歌曲是否已被收藏
    private void islikesong() {
        String url = connecturl + "/isFavorite";
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("userid", currentUserID);
        paramsMap.put("songid", playlist.get(playingSongList.getIndexNow()).getSongID());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue", var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String, Object> params = com.alibaba.fastjson.JSONObject.parseObject(responseStr, new TypeReference<Map<String, Object>>() {
                });
                islike = (Boolean) params.get("succ");
                Looper.prepare();
                mhandler.post(likeshow);
                Looper.loop();
            }
        });
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.delete_btn:
                musicControl.remove(position);
                playlist = playingSongList.getList();
                PlayListAdapter.notifyDataSetInvalidated();
                musicControl.newPlay();
                UpdateView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (playingSongList.getIndexNow() != position) {
            playingSongList.moveToIndex(position);
            musicControl.newPlay();
            playlistPopupWindow.dismiss();
            updatePlayText();

            //设置进度条的最大值
            sb_progress.setMax(musicControl.getDuration());
            //设置进度条的进度
            sb_progress.setProgress(musicControl.getCurrentPosition());

            UpdateView();
        }
    }

    //播放列表操作弹窗
    private void initPopwindow() {
        playlistPopupView = LayoutInflater.from(this).inflate(R.layout.playlist_popup_layout, null, false);
        playlistListView = playlistPopupView.findViewById(R.id.playlist_list_view);
        PlayListAdapter = new PlayListAdapter(this, R.layout.playlist_item, playlist);
        PlayListAdapter.setOnInnerItemOnClickListener(this);
        playlistListView.setOnItemClickListener(this);
        playlistListView.setAdapter(PlayListAdapter);
        playlistPopupWindow = new PopupWindow(playlistPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        playlistPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        playlistPopupWindow.setFocusable(true);
        playlistPopupWindow.setTouchable(true);
        playlistPopupWindow.setOutsideTouchable(true);
        playlistPopupWindow.setAnimationStyle(R.style.animTranslate);
        playlistPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView addsongpopup_out = playlistPopupView.findViewById(R.id.popup_out);
        addsongpopup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playlistPopupWindow.dismiss();
            }
        });
        mode_image = playlistPopupView.findViewById(R.id.mode_image);
        mode_image.setOnClickListener(this);
        play_mode_title = playlistPopupView.findViewById(R.id.play_mode_title);
        clear_btn = playlistPopupView.findViewById(R.id.clear_image);
        clear_btn.setOnClickListener(this);
        play_mode_title.setOnClickListener(this);
    }

    //显示添加歌曲弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this).inflate(R.layout.search_main, null);
        mode = getModel();
        if (!mode) {
            mode_image.setImageResource(R.drawable.list_mode);
            play_mode_title.setText("列表循环");
        } else {
            mode_image.setImageResource(R.drawable.single_mode);
            play_mode_title.setText("单曲循环");
        }
        //songname=playlistListView.getChildAt(playingSongList.getIndexNow()).findViewById(R.id.songlist_name);
        //String song_str = "<font color='#008df2'>"+playlist.get(playingSongList.getIndexNow()).getSongName()+"-"+playlist.get(playingSongList.getIndexNow()).getSingerName()+"</font>";
        //songname.setText(song_str);
        playlistPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

    //判断Service是否在运行，实际没有用到
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

    //实时更新进度条
    private void updateProgress() {
        if (playingSongList.isEmpty())
            return;
        int currentPosition = musicControl.getCurrentPosition();
        sb_progress.setProgress(currentPosition);

        if (musicControl.getCurrentPosition() == musicControl.getDuration()) playNext();

        handler.sendEmptyMessageDelayed(UPDATE_PROGRESS, 500000);
    }

    //根据播放状态调整播放/暂停按钮的显示
    private void updatePlayText() {
        if (playingSongList.isEmpty())
            return;
        if (musicControl.isPlaying()) {
            image_play.setImageResource(R.drawable.pause);
        } else {
            image_play.setImageResource(R.drawable.player_play);
        }
        handler.sendEmptyMessage(UPDATE_PROGRESS);
    }

    //异步检测
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
}
