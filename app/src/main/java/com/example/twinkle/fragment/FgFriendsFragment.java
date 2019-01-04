package com.example.twinkle.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.activity.NewsDetailActivity;
import com.example.twinkle.activity.PlayerActivity;
import com.example.twinkle.activity.ShareNewsActivity;
import com.example.twinkle.activity.SongListDetailActivity;
import com.example.twinkle.adapter.NewsAdapter;
import com.example.twinkle.adapter.NewsAdapter.InnerItemOnclickListener;
import com.example.twinkle.adapter.friendsAdapter;
import com.example.twinkle.enity.News;
import com.example.twinkle.enity.Song;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.enity.User;
import com.example.twinkle.serverenity.ServerNews;
import com.example.twinkle.serverenity.ServerSong;
import com.example.twinkle.serverenity.ServerSongList;
import com.example.twinkle.serverenity.ServerUser;
import com.example.twinkle.singleton.PlayingSongList;

import java.io.IOException;
import java.io.Serializable;
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

public class FgFriendsFragment extends Fragment implements InnerItemOnclickListener,OnItemClickListener {

    private List<News> NewsList = new ArrayList<News>();
    private List<User> FriendsList = new ArrayList<User>();
    private String connecturl;
    private View myfriendView;
    private String currentUserID;
    private ListView news_list_view;
    private NewsAdapter newsAdapter;
    private View popupView;
    private PopupWindow popupWindow;
    private friendsAdapter adapter;
    private Bundle bundle;
    private Handler handler=null;
    private Runnable show;
    private Runnable play;
    private Runnable todetail;
    private Runnable deleteshow;
    private Runnable unfollowshow;
    private Song playsong=new Song();
    private SongList songList=new SongList();
    private PlayingSongList playingSongList = PlayingSongList.getInstance();
    private int icon[] = new int[]{R.drawable.user1, R.drawable.user2, R.drawable.user3,R.drawable.user4,R.drawable.user5,R.drawable.user6};
    private int song_icon[] = new int[]{R.drawable.song1, R.drawable.song2, R.drawable.song3,R.drawable.song4,
            R.drawable.song5,R.drawable.song6,R.drawable.song7,R.drawable.song8,R.drawable.song9,R.drawable.song10,
            R.drawable.song11,R.drawable.song12,R.drawable.song13,R.drawable.song14,R.drawable.song15,
            R.drawable.song16,R.drawable.song17,R.drawable.song18,R.drawable.song19,R.drawable.song20,R.drawable.song21,R.drawable.song22};
    int songlisticon[] = new int[]{R.drawable.csonglist1, R.drawable.csonglist2, R.drawable.csonglist3,R.drawable.csonglist4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        myfriendView = inflater.inflate(R.layout.fg_friends, container, false);
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                adapter.notifyDataSetChanged();
                newsAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(news_list_view);
            }
        };
        unfollowshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                getfriendlist();
                getnewslist();
            }
        };
        deleteshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                newsAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(news_list_view);
                popupWindow.dismiss();
            }
        };
        todetail=new  Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(),SongListDetailActivity.class);
                intent.putExtra("songlist",songList);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("currentUserID",currentUserID);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        };
        play=new  Runnable(){
            @Override
            public void run() {
                if( playingSongList.getSize()==0){
                    List<Song> tempsongList = new ArrayList<Song>();
                    tempsongList.add(playsong);
                    playingSongList.InitPlayingSongList(tempsongList,"newsdetail",0);
                }
                if(!playingSongList.getNow().getSongID().equals(playsong.getSongID())){
                    playingSongList.addNext(playsong);
                    playingSongList.moveToNext();
                }
                Intent intent = new Intent(getActivity(),PlayerActivity.class);
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("option","next");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        };
        bundle = this.getArguments();
        currentUserID = bundle.getString("currentUserID");
        connecturl = bundle.getString("connecturl");
        getfriendlist();
        getnewslist();
        RecyclerView friend_recyclerView = (RecyclerView) myfriendView.findViewById(R.id.follow_list_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        friend_recyclerView.setLayoutManager(layoutManager);
        adapter = new friendsAdapter(FriendsList);
        friend_recyclerView.setAdapter(adapter);
        news_list_view = myfriendView.findViewById(R.id.news_list_view);
        newsAdapter = new NewsAdapter(this.getActivity(), R.layout.shared_news_item, NewsList);
        newsAdapter.setOnInnerItemOnClickListener(this);
        news_list_view.setAdapter(newsAdapter);
        setListViewHeightBasedOnChildren(news_list_view);
        news_list_view.setOnItemClickListener(this);
        ImageView friends_more=myfriendView.findViewById(R.id.friends_more);
        friends_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getfriendlist();
                getnewslist();
            }
        });
        return myfriendView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化弹窗
        initPopwindow();
    }
    private void getfriendlist(){
        String url =connecturl+"/showFollowedUser";
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
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                List<ServerUser> serverfriendtList = new ArrayList<>();
                serverfriendtList= JSON.parseArray(JSON.parseObject(responseStr).getString("FollowedUser"), ServerUser.class);
                FriendsList.clear();
                int count=0;
                for(int i=0;i<serverfriendtList.size();i++){
                    ServerUser serverfriend=serverfriendtList.get(i);
                    User friend = new User();
                    friend.setUserID(serverfriend.getUserid());
                    if(count>=6)
                        friend.setUserImage(R.drawable.user_default);
                    else if(friend.getUserID().equals("100001"))
                        friend.setUserImage(R.drawable.lemon);
                    else {
                        friend.setUserImage(icon[count]);
                        count++;
                    }
                    friend.setUserName(serverfriend.getUsername());
                    FriendsList.add(friend);
                }
            }
        });
    }
    private void getnewslist(){
        String url =connecturl+"/showNews";
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
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                List<ServerNews> servernewsList = new ArrayList<ServerNews>();
                servernewsList= JSON.parseArray(JSON.parseObject(responseStr).getString("news"), ServerNews.class);
                List<String> nameList = new ArrayList<String>();
                nameList= JSON.parseArray(JSON.parseObject(responseStr).getString("name"), String.class);
                List<String> forwardnameList = new ArrayList<String>();
                forwardnameList= JSON.parseArray(JSON.parseObject(responseStr).getString("forward"), String.class);
                List<String> usernameList = new ArrayList<String>();
                usernameList= JSON.parseArray(JSON.parseObject(responseStr).getString("muser"), String.class);
                List<String> creatorList = new ArrayList<String>();
                creatorList= JSON.parseArray(JSON.parseObject(responseStr).getString("creator"), String.class);
                List<Boolean> isLikeList = new ArrayList<Boolean>();
                isLikeList= JSON.parseArray(JSON.parseObject(responseStr).getString("isLike"), Boolean.class);
                List<Integer> likeNumList = new ArrayList<Integer>();
                likeNumList= JSON.parseArray(JSON.parseObject(responseStr).getString("likeNum"), Integer.class);
                List<Integer> forwardNumList = new ArrayList<Integer>();
                forwardNumList= JSON.parseArray(JSON.parseObject(responseStr).getString("forwardNum"), Integer.class);
                List<Integer> commentNumList = new ArrayList<Integer>();
                commentNumList= JSON.parseArray(JSON.parseObject(responseStr).getString("commentNum"), Integer.class);
                int count=0;
                NewsList.clear();
                for(int i=0;i<servernewsList.size();i++){
                    ServerNews servernews=servernewsList.get(i);
                    News news = new News();
                    news.setNewsId(servernews.getId());
                    news.setNewsCreatorID(servernews.getCreaterid());
                    news.setNewsContentId(servernews.getContentid());
                    news.setNewsType(servernews.getType());
                    news.setCommentCount(commentNumList.get(i));
                    news.setForwardCount(forwardNumList.get(i));
                    news.setAgreeCount(likeNumList.get(i));
                    news.setSkimCount(147);
                    if(news.getNewsCreatorID().equals(currentUserID))
                         news.setNewsCreatorCover(R.drawable.user_image);
                    else
                        news.setNewsCreatorCover(R.drawable.lemon);
                    news.setContentCover(R.drawable.default_cover);
                    int index=Integer.valueOf(news.getNewsContentId())-4000000;
                    if(index>=0&&index<22)
                        news.setContentCover(song_icon[index]);
                    if(news.getNewsContentId().equals("3000518"))
                        news.setContentCover(songlisticon[0]);
                    if(news.getNewsContentId().equals("3000531"))
                        news.setContentCover(songlisticon[1]);
                    if(news.getNewsContentId().equals("3000530"))
                        news.setContentCover(songlisticon[2]);
                    if(news.getNewsContentId().equals("3000533"))
                        news.setContentCover(songlisticon[3]);
                    news.setNewsContent(servernews.getText());
                    news.setNewsCreatorName(usernameList.get(i));
                    news.setNewsDate("2018年12月31日");
                    news.setContentName(nameList.get(i));
                    news.setContentCreator(creatorList.get(i));
                    news.setIsAgree(isLikeList.get(i));
                    news.setIsShared(false);
                    if(servernews.getForwarder()!=null){
                        news.setIsShared(true);
                        news.setOriginalCreator(forwardnameList.get(count));
                        news.setOriginalNewsId(servernews.getForwardedid());
                        count++;
                        news.setOriginalComment(servernews.getForwardedtext());
                    }
                    NewsList.add(news);
                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }
    private void changeagree(String newsid){
        String url =connecturl+"/likeNews";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("newsid",newsid);
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
                Log.d("liangyue",response.toString());
            }
        });
    }
    private void deletenews(final int position){
        String url =connecturl+"/deleteNews";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("nid",NewsList.get(position).getNewsId());
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
                NewsList.remove(position);
                Looper.prepare();
                handler.post(deleteshow);
                Looper.loop();
            }
        });
    }
    private void getsong(final String songid){
        String url =connecturl+"/showSong";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("songid",songid);
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
                ServerSong serversong = JSON.toJavaObject((JSONObject)params.get("song"),ServerSong.class);
                playsong.setSongID(serversong.getSongid());
                playsong.setAlbumID(serversong.getAlbumid());
                playsong.setSongName(serversong.getSongname());
                playsong.setSingerName((String)params.get("singer"));
                playsong.setAlbumName((String)params.get("album"));
                playsong.setSongSongType(true);
                String Path = Environment.getExternalStorageDirectory()+ "/Music/"+playsong.getSongID()+".mp3";
                playsong.setSongPath(Path);
                int index=Integer.valueOf(playsong.getSongID())-4000000;
                if(index>=0&&index<22)
                    playsong.setSongImageId(song_icon[index]);
                Looper.prepare();
                handler.post(play);
                Looper.loop();
            }
        });
    }
    private void changefollow(String id){
        String url =connecturl+"/followUser";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("otherid",id);
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
                Looper.prepare();
                handler.post(unfollowshow);
                Looper.loop();
            }
        });
    }
    private void initPopwindow() {
        popupView = LayoutInflater.from(this.getActivity()).inflate(R.layout.manage_news_layout, null, false);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mangagenewsTranslate);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out_above=popupView.findViewById(R.id.popup_out_above);
        popup_out_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView popup_out_below=popupView.findViewById(R.id.popup_out_below);
        popup_out_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //显示底部弹窗
    private void showPopWindow(final int position) {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        TextView next_play_title = popupView.findViewById(R.id.next_play_title);
        TextView delete_title = popupView.findViewById(R.id.delete_title);
        TextView ignore_news_title = popupView.findViewById(R.id.ignore_news_title);
        TextView unfollow_title = popupView.findViewById(R.id.unfollow_title);
        TextView next_play_btn = popupView.findViewById(R.id.next_play_btn);
        TextView delete_btn = popupView.findViewById(R.id.delete_btn);
        TextView ignore_news_btn = popupView.findViewById(R.id.ignore_news_btn);
        TextView unfollow_btn = popupView.findViewById(R.id.unfollow_btn);
        ImageView others_news_line = popupView.findViewById(R.id.others_news_line);
        ImageView my_news_line = popupView.findViewById(R.id.my_news_line);
        if(NewsList.get(position).getNewsType()=="单曲") {
            next_play_title.setVisibility(View.VISIBLE);
            next_play_btn.setVisibility(View.VISIBLE);
            next_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"已加入播放列表",Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
        }
        else{
            next_play_title.setVisibility(View.GONE);
            next_play_btn.setVisibility(View.GONE);
        }
        if(NewsList.get(position).getNewsCreatorID().equals(currentUserID)){
            final int current=position;
            ignore_news_title.setVisibility(View.GONE);
            ignore_news_btn.setVisibility(View.GONE);
            unfollow_title.setVisibility(View.GONE);
            unfollow_btn.setVisibility(View.GONE);
            others_news_line.setVisibility(View.GONE);
            delete_title.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletenews(current);
                }
            });
            my_news_line.setVisibility(View.VISIBLE);
        }else{
            delete_title.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
            ignore_news_title.setVisibility(View.VISIBLE);
            ignore_news_btn.setVisibility(View.VISIBLE);
            unfollow_title.setVisibility(View.VISIBLE);
            unfollow_btn.setVisibility(View.VISIBLE);
            my_news_line.setVisibility(View.GONE);
            others_news_line.setVisibility(View.VISIBLE);
            ignore_news_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            unfollow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changefollow(NewsList.get(position).getNewsCreatorID());
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    private void getsonglist(final String songid){
        String url =connecturl+"/songListDetail";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("songlistid",songid);
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
                Map<String,Object> params= com.alibaba.fastjson.JSONObject.parseObject(responseStr,new TypeReference<Map<String, Object>>(){});
                ServerSongList serversonglsit = JSON.toJavaObject((JSONObject)params.get("songlist"),ServerSongList.class);
                songList.setUserID(serversonglsit.getUserid());
                Looper.prepare();
                handler.post(todetail);
                Looper.loop();
            }
        });
    }
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.news_operate:
                showPopWindow(position);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.content_agree:
                content_agree_pressed(position);
                break;
            case R.id.content_comment:
                Intent comment_intent = new Intent(getContext(), NewsDetailActivity.class);
                comment_intent.putExtra("News", NewsList.get(position));
                comment_intent.putExtra("currentUserID", currentUserID);
                comment_intent.putExtra("connecturl", connecturl);
                comment_intent.putExtra("IsShared", "false");
                comment_intent.putExtra("IsComment", "true");
                comment_intent.putExtra("NewsId", NewsList.get(position).getNewsId()+"");
                comment_intent.putExtra("CommentCount", NewsList.get(position).getCommentCount()+"");
                comment_intent.putExtra("Position", position+"");
                this.startActivityForResult(comment_intent, 1);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                break;
            case R.id.content_share:
                content_share_pressed(position);
                break;
            case R.id.content_cover:
                if(NewsList.get(position).getNewsType().equals("单曲"))
                    getsong(NewsList.get(position).getNewsContentId());
                else{
                    songList.setSongListID(NewsList.get(position).getNewsContentId());
                    songList.setUserName(NewsList.get(position).getContentCreator());
                    songList.setSongListName(NewsList.get(position).getContentName());
                    songList.setSongListImageId(R.drawable.default_cover);
                    if(songList.getSongListID().equals("3000518"))
                        songList.setSongListImageId(songlisticon[0]);
                    if(songList.getSongListID().equals("3000531"))
                        songList.setSongListImageId(songlisticon[1]);
                    if(songList.getSongListID().equals("3000530"))
                        songList.setSongListImageId(songlisticon[2]);
                    if(songList.getSongListID().equals("3000533"))
                        songList.setSongListImageId(songlisticon[3]);
                    getsonglist(songList.getSongListID());
                }
                break;
            case R.id.content_bg:
                if(NewsList.get(position).getNewsType().equals("单曲"))
                    getsong(NewsList.get(position).getNewsContentId());
                else{
                    songList.setSongListID(NewsList.get(position).getNewsContentId());
                    songList.setUserName(NewsList.get(position).getContentCreator());
                    songList.setSongListName(NewsList.get(position).getContentName());
                    songList.setSongListImageId(R.drawable.default_cover);
                    if(songList.getSongListID().equals("3000518"))
                        songList.setSongListImageId(songlisticon[0]);
                    if(songList.getSongListID().equals("3000531"))
                        songList.setSongListImageId(songlisticon[1]);
                    if(songList.getSongListID().equals("3000530"))
                        songList.setSongListImageId(songlisticon[2]);
                    if(songList.getSongListID().equals("3000533"))
                        songList.setSongListImageId(songlisticon[3]);
                    getsonglist(songList.getSongListID());
                }
                break;
            case R.id.shared_news_content:
                int current=-1;
                for(int i=0;i<NewsList.size();i++){
                    if(NewsList.get(i).getNewsId().equals(NewsList.get(position).getOriginalNewsId()))
                        current=i;
                }
                if(current==-1){
                    Toast.makeText(this.getActivity(),"动态已删除",Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                    intent.putExtra("currentUserID", currentUserID);
                    intent.putExtra("connecturl", connecturl);
                    intent.putExtra("News", NewsList.get(current));
                    intent.putExtra("IsComment", "false");
                    intent.putExtra("NewsId", NewsList.get(current).getNewsId()+"");
                    intent.putExtra("CommentCount", NewsList.get(current).getCommentCount()+"");
                    intent.putExtra("Position", current+"");
                    this.startActivityForResult(intent, 1);
                    getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                }
                break;
            default:
                break;
        }
    }

    private void content_agree_pressed(int position){
        ImageView temp_agree_btn = news_list_view.getChildAt(position).findViewById(R.id.content_agree);
        TextView temp_agree_count = news_list_view.getChildAt(position).findViewById(R.id.content_agree_count);
        int temp =NewsList.get(position).getAgreeCount();
        if(NewsList.get(position).getIsAgree()) {
            NewsList.get(position).setIsAgree(false);
            temp_agree_btn.setImageResource(R.drawable.agree);
            temp--;
            NewsList.get(position).setAgreeCount(temp);
            temp_agree_count.setText(String.valueOf(temp));
        }
        else {
            NewsList.get(position).setIsAgree(true);
            temp_agree_btn.setImageResource(R.drawable.agree_active);
            Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim. agree_btn_anim);
            temp_agree_btn.setAnimation(mAnimation);
            mAnimation.start();
            temp++;
            NewsList.get(position).setAgreeCount(temp);
            temp_agree_count.setText(String.valueOf(temp));
        }
        changeagree(NewsList.get(position).getNewsId());
    }

    private void content_share_pressed(int position){
        Intent intent = new Intent(getActivity(),ShareNewsActivity.class);
        intent.putExtra("SharedNews",(Serializable) (NewsList.get(position)));
        intent.putExtra("currentUserID",currentUserID);
        intent.putExtra("connecturl",connecturl);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("currentUserID", currentUserID);
        intent.putExtra("connecturl", connecturl);
        intent.putExtra("News", NewsList.get(position));
        intent.putExtra("IsComment", "false");
        intent.putExtra("CommentCount", NewsList.get(position).getCommentCount()+"");
        intent.putExtra("NewsId", NewsList.get(position).getNewsId()+"");
        intent.putExtra("Position", position+"");
        this.startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String option=data.getStringExtra("Option");
        String position_str=data.getStringExtra("Position");
        String agree_count_str=data.getStringExtra("AgreeCount");
        String comment_count_str=data.getStringExtra("CommentCount");
        int position=Integer.parseInt(position_str);
        int agree_count=Integer.parseInt(agree_count_str);
        int comment_count=Integer.parseInt(comment_count_str);
        switch (option) {
            case "delete":
                deletenews(position);
                break;
            case "ignore":
                for(int i=0;i<NewsList.size();i++){
                    if(NewsList.get(i).getNewsCreatorName().equals(NewsList.get(position).getNewsCreatorName()))
                        NewsList.remove(i);
                }
                newsAdapter.notifyDataSetChanged();
                break;
            case "unfollow":
                changefollow(NewsList.get(position).getNewsCreatorID());
                break;
            default:
                if(position!=-1&&NewsList.get(position).getAgreeCount()!=agree_count){
                    content_agree_pressed(position);
                }
                if(position!=-1&&NewsList.get(position).getCommentCount()!=comment_count){
                    TextView temp_comment_count = news_list_view.getChildAt(position).findViewById(R.id.content_comment_count);
                    temp_comment_count.setText(comment_count_str);
                }
        }
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+100;
        listView.setLayoutParams(params);
    }
}
