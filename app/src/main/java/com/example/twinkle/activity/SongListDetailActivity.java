package com.example.twinkle.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.adapter.AddSongAdapter;
import com.example.twinkle.adapter.SongListDetailAdapter;
import com.example.twinkle.adapter.SongListDetailAdapter.InnerItemOnclickListener;
import com.example.twinkle.enity.Song;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.serverenity.ServerSinger;
import com.example.twinkle.serverenity.ServerSong;
import com.example.twinkle.serverenity.ServerSongList;
import com.example.twinkle.singleton.PlayingSongList;

import java.io.IOException;
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

public class SongListDetailActivity extends AppCompatActivity implements InnerItemOnclickListener, OnItemClickListener{
    private List<Song> songList=new ArrayList<Song>();
    private List<SongList> songListList = new ArrayList<SongList>();
    private View songpopupView;
    private SongList songlist;
    private PopupWindow addSongPopupWindow;
    private View addSongPopupView;
    private ImageView songlist_cover;
    private ImageView share_btn;
    private ImageView like_btn;
    private TextView songlist_name;
    private TextView user_name;
    private PopupWindow songpopupWindow;
    private ListView songListView;
    private SongListDetailAdapter adapter;
    private String currentUserID;
    private String connecturl;
    private Handler handler=null;
    private Runnable show;
    private int currentPosition;
    private AddSongAdapter addSongAdapter;
    private Runnable addsongshow;
    private ListView addsongListview;
    private String msg;
    private Runnable deletesongshow;
    private Runnable addsonglistshow;
    private Runnable islikeshow;
    private boolean islike;
    private PlayingSongList playingSongList=PlayingSongList.getInstance();
    private int icon[] = new int[]{R.drawable.song1, R.drawable.song2, R.drawable.song3,R.drawable.song4,
            R.drawable.song5,R.drawable.song6,R.drawable.song7,R.drawable.song8,R.drawable.song9,R.drawable.song10,
            R.drawable.song11,R.drawable.song12,R.drawable.song13,R.drawable.song14,R.drawable.song15,
            R.drawable.song16,R.drawable.song17,R.drawable.song18,R.drawable.song19,R.drawable.song20,R.drawable.song21,R.drawable.song22};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.songlist_detail);
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                adapter.notifyDataSetChanged();
                addSongAdapter.notifyDataSetChanged();
            }
        };
        addsongshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                addSongPopupWindow.dismiss();
                Toast.makeText(SongListDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        };
        addsonglistshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                Toast.makeText(SongListDetailActivity.this,msg,Toast.LENGTH_SHORT).show();
            }
        };
        deletesongshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                getJsonData();
                songpopupWindow.dismiss();
            }
        };
        islikeshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                if(islike){
                    like_btn.setImageResource(R.drawable.star_yes);
                }
                else {
                    like_btn.setImageResource(R.drawable.star_no);
                }
            }
        };
        currentUserID=getIntent().getStringExtra("currentUserID");
        connecturl=getIntent().getStringExtra("connecturl");
        songlist=(SongList) getIntent().getSerializableExtra("songlist");
        songlist_cover=findViewById(R.id.songlist_cover);
        share_btn=findViewById(R.id.share_btn);
        like_btn=findViewById(R.id.like_btn);
        songlist_name=findViewById(R.id.songlist_name);
        user_name=findViewById(R.id.user_name);
        //初始化数据
        songlist_cover.setImageResource(songlist.getSongListImageId());
        String user_name_str="by "+songlist.getUserName();
        user_name.setText(user_name_str);
        songlist_name.setText(songlist.getSongListName());
        getJsonData();
        iscolloct();
        //初始化弹窗
        initPopwindow();
        songListView=findViewById(R.id.songlist_detail_list_view);
        adapter = new SongListDetailAdapter(this, R.layout.detail_song_item, songList);
        adapter.setOnInnerItemOnClickListener(this);
        songListView.setAdapter(adapter);
        songListView.setOnItemClickListener(this);
        //返回按钮点击事件
        ImageView back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.mid_bottom_slide_out);
            }
        });
        //跳转到分享歌单活动
        share_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SongListDetailActivity.this,ShareSonglistActivity.class);
                intent.putExtra("songlist",songlist);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("currentUserID",currentUserID);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
        //收藏歌单
        like_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colloctsonglist();
                if(islike){
                    islike=false;
                    like_btn.setImageResource(R.drawable.star_no);
                }
                else {
                    islike=true;
                    like_btn.setImageResource(R.drawable.star_yes);
                }
            }
        });
    }
    private void getJsonData(){
        String url =connecturl+"/showSongList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("songlistid",songlist.getSongListID());
        paramsMap.put("uid",currentUserID);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new   Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue",var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                List<ServerSinger> serversingerList = new ArrayList<>();
                List<ServerSong> serversonglist = new ArrayList<>();
                List<ServerSongList> serversonglistlist = new ArrayList<>();
                List<String> albumnamelist = new ArrayList<>();
                serversingerList= JSON.parseArray(JSON.parseObject(responseStr).getString("singers"), ServerSinger.class);
                serversonglist= JSON.parseArray(JSON.parseObject(responseStr).getString("songs"), ServerSong.class);
                albumnamelist= JSON.parseArray(JSON.parseObject(responseStr).getString("albums"), String.class);
                serversonglistlist= JSON.parseArray(JSON.parseObject(responseStr).getString("songLists"), ServerSongList.class);
                songList.clear();
                songListList.clear();
                for(int i=0;i<serversonglist.size();i++){
                    ServerSong serversong=serversonglist.get(i);
                    Song song = new Song();
                    song.setSongImageId(R.drawable.cover);
                    song.setSongID(serversong.getSongid());
                    song.setAlbumID(serversong.getAlbumid());
                    song.setSongName(serversong.getSongname());
                    song.setSingerName(serversingerList.get(i).getSingername());
                    song.setSingerID(serversingerList.get(i).getSingerid());
                    song.setAlbumName(albumnamelist.get(i));
                    song.setSongSongType(true);
                    String Path = Environment.getExternalStorageDirectory()+ "/Music/"+song.getSongID()+".mp3";
                    song.setSongPath(Path);
                    int index=Integer.valueOf(song.getSongID())-4000000;
                    if(index>=0&&index<22)
                        song.setSongImageId(icon[index]);
                    songList.add(song);
                }
                for (int i=0;i<serversonglistlist.size();i++){
                    ServerSongList serverSongList=serversonglistlist.get(i);
                    SongList songList=new SongList();
                    songList.setSongListID(serverSongList.getSonglistid());
                    songList.setSongListName(serverSongList.getSonglistname());
                    songListList.add(songList);
                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }
    private void addsong(String songlistid){
        String url =connecturl+"/keepSong";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("songid",songList.get(currentPosition).getSongID());
        paramsMap.put("songlistid",songlistid);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new   Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue",var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                msg=(String)params.get("msg");
                Looper.prepare();
                handler.post(addsonglistshow);
                Looper.loop();
            }
        });
    }
    private void deletesong(){
        String url =connecturl+"/deleteSongInList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("songid",songList.get(currentPosition).getSongID());
        paramsMap.put("songlistid",songlist.getSongListID());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new   Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue",var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                Log.d("liangyue",(String)params.get("msg"));
                Looper.prepare();
                handler.post(deletesongshow);
                Looper.loop();
            }
        });
    }
    private void colloctsonglist(){
        String url =connecturl+"/keepSongList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("songlistid",songlist.getSongListID());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new   Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue",var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                Log.d("liangyue",(String)params.get("msg"));
            }
        });
    }
    private void iscolloct(){
        String url =connecturl+"/isKeeped";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("songlistid",songlist.getSongListID());
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //追加表单信息
            builder.add(key, paramsMap.get(key));
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody formBody=builder.build();
        Request request=new   Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call var1, IOException var) {
                Log.d("liangyue",var.toString());
            }

            @Override
            public void onResponse(Call var1, Response response) throws IOException {
                String responseStr = response.body().string();
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                islike=(Boolean)params.get("succ");
                Looper.prepare();
                handler.post(islikeshow);
                Looper.loop();
            }
        });
    }
   private void initPopwindow() {
        songpopupView= LayoutInflater.from(this).inflate(R.layout.songlist_detail_popup_layout, null, false);
        songpopupWindow = new PopupWindow(songpopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        songpopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        songpopupWindow.setFocusable(true);
        songpopupWindow.setTouchable(true);
        songpopupWindow.setOutsideTouchable(true);
        songpopupWindow.setAnimationStyle(R.style.animTranslate);
        songpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out=songpopupView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songpopupWindow.dismiss();
            }
        });
       //下一首播放
       TextView pop_next_play=songpopupView.findViewById(R.id.next_play_btn_area);
       pop_next_play.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
       //跳转到分享歌单活动
       TextView pop_share=songpopupView.findViewById(R.id.pop_share_btn_area);
       pop_share.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               songpopupWindow.dismiss();
               Intent intent = new Intent(SongListDetailActivity.this,ShareSongActivity.class);
               intent.putExtra("song",songList.get(currentPosition));
               intent.putExtra("currentUserID",currentUserID);
               intent.putExtra("connecturl",connecturl);
               startActivity(intent);
               overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
           }
       });
       //显示添加歌曲弹窗
       TextView pop_add=songpopupView.findViewById(R.id.pop_colloct_btn_area);
       pop_add.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               songpopupWindow.dismiss();
               setBackgroundAlpha(0.7f);
               showaddSongPopWindow();
           }
       });
       //删除歌曲
       if(songlist.getUserID().equals(currentUserID)){
           TextView pop_delete=songpopupView.findViewById(R.id.pop_delete_btn_area);
           TextView pop_delete_title=songpopupView.findViewById(R.id.pop_delete_title);
           ImageView pop_delete_image=songpopupView.findViewById(R.id.pop_delete_image);
           ImageView pop_delete_line=songpopupView.findViewById(R.id.pop_delete_title_line);
           pop_delete.setVisibility(View.VISIBLE);
           pop_delete_title.setVisibility(View.VISIBLE);
           pop_delete_image.setVisibility(View.VISIBLE);
           pop_delete_line.setVisibility(View.VISIBLE);
           pop_delete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   deletesong();
               }
           });
       }
       //添加歌曲操作弹窗
       addSongPopupView = LayoutInflater.from(this).inflate(R.layout.add_song_popup_layout, null, false);
       addsongListview=addSongPopupView.findViewById(R.id.add_song_list_view);
       addSongAdapter = new AddSongAdapter(this, R.layout.add_song_item, songListList);
       addsongListview.setAdapter(addSongAdapter);
       addsongListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
           public  void onItemClick(AdapterView<?>parent,View view,int position,long id){
               addsong(songListList.get(position).getSongListID());
           }
       });
       addSongPopupWindow = new PopupWindow(addSongPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
       addSongPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
       addSongPopupWindow.setFocusable(true);
       addSongPopupWindow.setTouchable(true);
       addSongPopupWindow.setOutsideTouchable(true);
       addSongPopupWindow.setAnimationStyle(R.style.animTranslate);
       addSongPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
           @Override public void onDismiss() {
               setBackgroundAlpha(1.0f);
           }
       });
       TextView addsongpopup_out=addSongPopupView.findViewById(R.id.popup_out);
       addsongpopup_out.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               addSongPopupWindow.dismiss();
           }
       });
    }
    //显示底部弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this).inflate(R.layout.songlist_detail, null);
        TextView song_name =songpopupView.findViewById(R.id.pop_song_title);
        String pop_song_title="单曲："+songList.get(currentPosition).getSongName();
        song_name.setText(pop_song_title);
        songpopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    //显示添加歌曲弹窗
    private void showaddSongPopWindow() {
        View rootview = LayoutInflater.from(this).inflate(R.layout.songlist_detail, null);
        addSongPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.song_operate:
                currentPosition=position;
                showPopWindow();
                setBackgroundAlpha(0.7f);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentPosition=position;
        playingSongList.InitPlayingSongList(songList,songlist.getSongListID(),position);
        Intent intent = new Intent(SongListDetailActivity.this,PlayerActivity.class);
        intent.putExtra("currentUserID",currentUserID);
        intent.putExtra("connecturl",connecturl);
        intent.putExtra("option","");
        startActivity(intent);
        overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.fade_in, R.anim.mid_bottom_slide_out);
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }

}
