package com.example.twinkle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.twinkle.R;
import com.example.twinkle.activity.RecommendSongActivity;
import com.example.twinkle.activity.SongListDetailActivity;
import com.example.twinkle.enity.Song;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.serverenity.ServerSong;
import com.example.twinkle.serverenity.ServerSongList;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FgFindmusicFragment extends Fragment {
    private View findmusicView;
    private String connecturl;
    private String currentUserID;
    private List<SongList> recommend_songListList = new ArrayList<SongList>();
    private List<Song> recommend_song = new ArrayList<Song>();
    private TextView recommend_songlist_text1;
    private TextView recommend_songlist_text2;
    private TextView recommend_songlist_text3;
    private TextView recommend_songlist_text4;
    private TextView recommend_songlist_text5;
    private TextView recommend_songlist_text6;
    private TextView recommend_song_text1;
    private TextView recommend_song_text2;
    private TextView recommend_song_text3;
    private Bundle bundle;
    private Handler handler=null;
    private Runnable show;
    private int icon[] = new int[]{R.drawable.songlist1, R.drawable.songlist2, R.drawable.songlist3,R.drawable.songlist4,R.drawable.songlist5,R.drawable.songlist6};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        findmusicView = inflater.inflate(R.layout.fg_find_music, container, false);
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                recommend_songlist_text1.setText(recommend_songListList.get(0).getSongListName());
                recommend_songlist_text2.setText(recommend_songListList.get(1).getSongListName());
                recommend_songlist_text3.setText(recommend_songListList.get(2).getSongListName());
                recommend_songlist_text4.setText(recommend_songListList.get(3).getSongListName());
                recommend_songlist_text5.setText(recommend_songListList.get(4).getSongListName());
                recommend_songlist_text6.setText(recommend_songListList.get(5).getSongListName());
                recommend_song_text1.setText(recommend_song.get(0).getSongName());
                recommend_song_text2.setText(recommend_song.get(1).getSongName());
                recommend_song_text3.setText(recommend_song.get(2).getSongName());

            }
        };
        bundle = this.getArguments();
        currentUserID = bundle.getString("currentUserID");
        connecturl = bundle.getString("connecturl");
        getJsonData();
        ImageView recommend_songlist_image1=findmusicView.findViewById(R.id.recommend_songlist_image1);
        ImageView recommend_songlist_image2=findmusicView.findViewById(R.id.recommend_songlist_image2);
        ImageView recommend_songlist_image3=findmusicView.findViewById(R.id.recommend_songlist_image3);
        ImageView recommend_songlist_image4=findmusicView.findViewById(R.id.recommend_songlist_image4);
        ImageView recommend_songlist_image5=findmusicView.findViewById(R.id.recommend_songlist_image5);
        ImageView recommend_songlist_image6=findmusicView.findViewById(R.id.recommend_songlist_image6);
        recommend_songlist_text1=findmusicView.findViewById(R.id.recommend_songlist_text1);
        recommend_songlist_text2=findmusicView.findViewById(R.id.recommend_songlist_text2);
        recommend_songlist_text3=findmusicView.findViewById(R.id.recommend_songlist_text3);
        recommend_songlist_text4=findmusicView.findViewById(R.id.recommend_songlist_text4);
        recommend_songlist_text5=findmusicView.findViewById(R.id.recommend_songlist_text5);
        recommend_songlist_text6=findmusicView.findViewById(R.id.recommend_songlist_text6);
        ImageView recommend_song_image1=findmusicView.findViewById(R.id.recommend_song_image1);
        ImageView recommend_song_image2=findmusicView.findViewById(R.id.recommend_song_image2);
        ImageView recommend_song_image3=findmusicView.findViewById(R.id.recommend_song_image3);
        recommend_song_text1=findmusicView.findViewById(R.id.recommend_song_text1);
        recommend_song_text2=findmusicView.findViewById(R.id.recommend_song_text2);
        recommend_song_text3=findmusicView.findViewById(R.id.recommend_song_text3);
        ImageView recommend_song_btn=findmusicView.findViewById(R.id.IM1);
        recommend_song_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecommendSongActivity.class);
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("connecturl",connecturl);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        });
        recommend_songlist_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSonglistdetail(0);
            }
        });
        recommend_songlist_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSonglistdetail(1);
            }
        });
        recommend_songlist_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSonglistdetail(2);
            }
        });
        recommend_songlist_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSonglistdetail(3);
            }
        });
        recommend_songlist_image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSonglistdetail(4);
            }
        });
        recommend_songlist_image6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSonglistdetail(5);
            }
        });
        return findmusicView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    private void getJsonData(){
        String url =connecturl+"/showRecommend";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
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
                List<ServerSongList> serversonglistList = new ArrayList<>();
                List<ServerSong> serversonglist = new ArrayList<>();
                serversonglistList= JSON.parseArray(JSON.parseObject(responseStr).getString("songlists"), ServerSongList.class);
                serversonglist= JSON.parseArray(JSON.parseObject(responseStr).getString("songs"), ServerSong.class);
                for(int i=0;i<serversonglistList.size();i++){
                    ServerSongList serversongList=serversonglistList.get(i);
                    SongList songList = new SongList();
                    songList.setSongListID(serversongList.getSonglistid());
                    songList.setSongListName(serversongList.getSonglistname());
                    songList.setSongListImageId(icon[i]);
                    songList.setUserID(serversongList.getUserid());
                    songList.setUserName("Twinkle");
                    recommend_songListList.add(songList);
                }
                for(int i=0;i<serversonglist.size();i++){
                    ServerSong serversong=serversonglist.get(i);
                    Song song = new Song();
                    song.setSongID(serversong.getSongid());
                    song.setAlbumID(serversong.getAlbumid());
                    song.setSongName(serversong.getSongname());
                    recommend_song.add(song);
                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }

    private void showSonglistdetail(int position){
        Intent intent = new Intent(getActivity(),SongListDetailActivity.class);
        intent.putExtra("songlist",recommend_songListList.get(position));
        intent.putExtra("currentUserID",currentUserID);
        intent.putExtra("connecturl",connecturl);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }
}
