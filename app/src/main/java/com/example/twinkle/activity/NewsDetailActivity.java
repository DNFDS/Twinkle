package com.example.twinkle.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
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
import com.example.twinkle.adapter.NewsCommentAdapter;
import com.example.twinkle.adapter.NewsCommentAdapter.InnerItemOnclickListener;
import com.example.twinkle.enity.News;
import com.example.twinkle.enity.NewsComment;
import com.example.twinkle.enity.Song;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.serverenity.ServerNewsComment;
import com.example.twinkle.serverenity.ServerSong;
import com.example.twinkle.serverenity.ServerSongList;
import com.example.twinkle.singleton.PlayingSongList;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
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

public class NewsDetailActivity extends AppCompatActivity implements InnerItemOnclickListener, OnItemClickListener{
    private List<String> agreeList=new ArrayList<String>();
    private List<String> shareList=new ArrayList<String>();
    private String[] str_arr = new String[]{};
    private List<NewsComment> commentList=new ArrayList<NewsComment>();
    private int replyposition=-1;
    private View popupView;
    private View commentpopupView;
    private News news;
    private String IsComment;
    private int CommentCount;
    private String position_str;
    private ImageView content_agree_img;
    private ImageView content_share_img;
    private TextView content_agree_text;
    private TextView content_share_text;
    private ListView comment_listview;
    private EditText editcomment;
    private NewsCommentAdapter commentAdapter;
    private ImageView content_agree;
    private PopupWindow popupWindow;
    private PopupWindow commentpopupWindow;
    private String currentUserID;
    private String connecturl;
    List<Boolean> isLikeList = new ArrayList<Boolean>();
    private Handler handler=null;
    private Runnable show;
    private Runnable play;
    private Runnable todetail;
    private Runnable deleteshow;
    SongList songList=new SongList();
    private PlayingSongList playingSongList = PlayingSongList.getInstance();
    private Song playsong=new Song();
    private int icon[] = new int[]{R.drawable.song1, R.drawable.song2, R.drawable.song3,R.drawable.song4,
            R.drawable.song5,R.drawable.song6,R.drawable.song7,R.drawable.song8,R.drawable.song9,R.drawable.song10,
            R.drawable.song11,R.drawable.song12,R.drawable.song13,R.drawable.song14,R.drawable.song15,
            R.drawable.song16,R.drawable.song17,R.drawable.song18,R.drawable.song19,R.drawable.song20,R.drawable.song21,R.drawable.song22};
    int songlisticon[] = new int[]{R.drawable.csonglist1, R.drawable.csonglist2, R.drawable.csonglist3,R.drawable.csonglist4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);
        currentUserID=getIntent().getStringExtra("currentUserID");
        connecturl=getIntent().getStringExtra("connecturl");
        news =(News)getIntent().getSerializableExtra("News");
        position_str=getIntent().getStringExtra("Position");
        IsComment=getIntent().getStringExtra("IsComment");
        CommentCount=Integer.valueOf(getIntent().getStringExtra("CommentCount"));
        final View main_view = findViewById(R.id.main_div);
        content_agree =  findViewById (R.id.content_agree);
        content_agree_img= findViewById (R.id.content_agree_img);
        content_share_img= findViewById (R.id.content_share_img);
        content_agree_text = findViewById (R.id.content_agree_text);
        content_share_text =findViewById (R.id.content_share_text);
        ImageView back = findViewById (R.id.back);
        ImageView news_detail_opreate = findViewById (R.id.news_detail_opreate);
        ImageView user_cover = findViewById (R.id.user_cover);
        ImageView content_cover =  findViewById (R.id.content_cover);
        ImageView content_comment =  findViewById (R.id.content_comment);
        ImageView content_share = findViewById (R.id.content_share);
        ImageView content_bg = findViewById (R.id.content_bg);
        TextView user_name = findViewById (R.id.user_name);
        TextView news_content_title = findViewById (R.id.news_content_title);
        TextView news_date_title = findViewById (R.id.news_date_title);
        TextView news_content = findViewById (R.id.news_content);
        TextView content_name = findViewById (R.id.content_name);
        TextView content_creator = findViewById (R.id.content_creator);
        TextView content_skim_count = findViewById (R.id.content_skim_count);
        TextView comment_send_btn=findViewById(R.id.comment_send);
        //初始化数据
        String news_content_title_string ="分享"+news.getNewsType()+":";
        String content_name_string =news.getNewsType()+":"+news.getContentName();
        String content_creator_string ="歌手:"+news.getContentCreator();
        if(news.getNewsType().equals("歌单"));
            content_creator_string="创建者："+news.getContentCreator();
        String content_skim_count_string ="浏览"+String.valueOf(news.getSkimCount())+"次";
        user_cover.setImageResource(news.getNewsCreatorCover());
        content_cover.setImageResource(news.getContentCover());
        if(news.getIsAgree())
            content_agree.setImageResource(R.drawable.agree_active);
        else
            content_agree.setImageResource(R.drawable.agree);
        user_name.setText(news.getNewsCreatorName());
        if(news.getIsShared())
            news_content_title.setText("转发:");
        else
            news_content_title.setText(news_content_title_string);
        news_date_title.setText(news.getNewsDate());
        news_content.setText(news.getNewsContent());
        content_name.setText(content_name_string);
        content_creator.setText(content_creator_string);
        content_skim_count.setText(content_skim_count_string);
        //初始化弹窗
        initPopwindow();
        //初始化点赞和转发文本
        getJsonData();
        str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
        str_arr[str_arr.length-1] = "";
        comment_listview=findViewById(R.id.comment_list_view);
        commentAdapter = new NewsCommentAdapter(this, R.layout.comment_item, commentList);
        commentAdapter.setOnInnerItemOnClickListener(this);
        comment_listview.setAdapter(commentAdapter);
        setListViewHeightBasedOnChildren(comment_listview);
        comment_listview.setOnItemClickListener(this);
        //初始化评论输入框
        editcomment=findViewById(R.id.edit_comment);
        editcomment.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editcomment.setSingleLine(false);
        editcomment.setHorizontallyScrolling(false);
        editcomment.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        editcomment.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if((replyposition==-1)&&editcomment.getText().toString().equals("")){
                        InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        editcomment.requestFocus();
                        inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                        inputManager.showSoftInput(editcomment, 0);
                        editcomment.setText(str_arr[0]);
                        editcomment.setSelection(editcomment.getText().length());
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });
        editcomment.addTextChangedListener(new TextWatcher() {
            String tmp;
            int cursor;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int line = editcomment.getLineCount();
                if (line > 3) {
                    if (before > 0 && start == 0) {
                        if (s.toString().equals(tmp)) {
                            // setText触发递归TextWatcher
                            cursor--;
                        } else {
                            // 手动移动光标为0
                            cursor = count - 1;
                        }
                    } else {
                        cursor = start + count - 1;
                    }
                }
            }
            @Override public void afterTextChanged(Editable s) {
                // 限制可输入行数
                int line = editcomment.getLineCount();
                if (line > 3){
                    String str = s.toString();
                    tmp = str.substring(0, cursor) + str.substring(cursor + 1);
                    editcomment.setText(tmp);
                    editcomment.setSelection(cursor);
                }
            }
        });
        //评论发送按钮点击事件
        comment_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_view.setFocusable(true);
                main_view.setFocusableInTouchMode(true);
                main_view.requestFocus();
                str_arr[replyposition+1]=editcomment.getText().toString();
                if(replyposition==-1)
                    createnewscomment(str_arr[replyposition+1]);
                else
                    replynewscomment(str_arr[replyposition+1],replyposition);
                str_arr[replyposition+1]="";
                replyposition=-1;
                InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editcomment.getWindowToken(), 0);
                editcomment.setHint("说点什么吧...");
                editcomment.setText("");
            }
        });
        //是否立刻弹出键盘
        if(IsComment.equals("true")){
            boolean handler =new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    editcomment.requestFocus();
                }
            },300); // 延时0.3秒
        }
        //返回按钮点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity("back");
            }
        });
        //动态管理按钮点击事件
        news_detail_opreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
                setBackgroundAlpha(0.7f);
            }
        });
        //动态内容点击事件
        content_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news.getNewsType().equals("单曲"))
                    getsong(news.getNewsContentId());
                else{
                    songList.setSongListID(news.getNewsContentId());
                    songList.setUserName(news.getContentCreator());
                    songList.setSongListName(news.getContentName());
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
            }
        });
        content_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news.getNewsType().equals("单曲"))
                    getsong(news.getNewsContentId());
                else{
                    songList.setSongListID(news.getNewsContentId());
                    songList.setUserName(news.getContentCreator());
                    songList.setSongListName(news.getContentName());
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
            }
        });
        //动态点赞按钮点击事件
        content_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_agree_pressed();
            }
        });
        //动态评论按钮点击事件
        content_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editcomment.requestFocus();
            }
        });
        //动态转发按钮点击事件
        content_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this,ShareNewsActivity.class);
                intent.putExtra("SharedNews",(Serializable) (news));
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("connecturl",connecturl);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
        //主布局点击事件
        main_view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                main_view.setFocusable(true);
                main_view.setFocusableInTouchMode(true);
                main_view.requestFocus();
                str_arr[replyposition+1]=editcomment.getText().toString();
                Log.d("liangyue",str_arr[0]);
                replyposition=-1;
                InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editcomment.getWindowToken(), 0);
                editcomment.setHint("说点什么吧...");
                editcomment.setText("");
                return false;
            }
        });
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                commentAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(comment_listview);
                showAgree();
                showShare();
            }
        };
        deleteshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                commentAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(comment_listview);
                commentpopupWindow.dismiss();
            }
        };
        todetail=new  Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent(NewsDetailActivity.this,SongListDetailActivity.class);
                intent.putExtra("songlist",songList);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("currentUserID",currentUserID);
                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
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
                Intent intent = new Intent(NewsDetailActivity.this,PlayerActivity.class);
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("option","next");
                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        };
    }
    private void getJsonData(){
        String url =connecturl+"/showNewsDetail";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("uid",currentUserID);
        paramsMap.put("nid",news.getNewsId());
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
                List<ServerNewsComment> servernewscommentList = new ArrayList<ServerNewsComment>();
                servernewscommentList= JSON.parseArray(JSON.parseObject(responseStr).getString("comments"), ServerNewsComment.class);
                List<String> likenameList = new ArrayList<String>();
                likenameList= JSON.parseArray(JSON.parseObject(responseStr).getString("like"), String.class);
                List<Integer> likenumList = new ArrayList<Integer>();
                likenumList= JSON.parseArray(JSON.parseObject(responseStr).getString("likeNum"), Integer.class);
                List<String> forwardnameList = new ArrayList<String>();
                forwardnameList= JSON.parseArray(JSON.parseObject(responseStr).getString("forward"), String.class);
                List<String> usernameList = new ArrayList<String>();
                usernameList= JSON.parseArray(JSON.parseObject(responseStr).getString("commentUser"), String.class);
                List<String> parenternameList = new ArrayList<String>();
                parenternameList= JSON.parseArray(JSON.parseObject(responseStr).getString("parenter"), String.class);
                commentList.clear();
                isLikeList.clear();
                isLikeList= JSON.parseArray(JSON.parseObject(responseStr).getString("isLike"), Boolean.class);
                int count=0;
                for(int i=0;i<likenameList.size();i++){
                    String agree_user_name=likenameList.get(i);
                    agreeList.add(agree_user_name);
                }
                for(int i=0;i<forwardnameList.size();i++){
                    String share_user_name=forwardnameList.get(i);
                    shareList.add(share_user_name);
                }
                for(int i=0;i<servernewscommentList.size();i++){
                    ServerNewsComment serverNewsComment=servernewscommentList.get(i);
                    NewsComment newsComment=new NewsComment();
                    newsComment.setUserID(serverNewsComment.getCommenterid());
                    newsComment.setCommentAgreeCount(likenumList.get(i));
                    newsComment.setIsAgree(isLikeList.get(i));
                    newsComment.setID(serverNewsComment.getCommentid());
                    newsComment.setCommentText(serverNewsComment.getText());
                    newsComment.setCommentTime("8月9日19:57");
                    if(newsComment.getUserID().equals(currentUserID))
                        newsComment.setUserCover(R.drawable.user_image);
                    else
                        newsComment.setUserCover(R.drawable.lemon);
                    newsComment.setUserName(usernameList.get(i));
                    newsComment.setIsReply(false);
                    newsComment.setReplyText(serverNewsComment.getParenttext());
                    newsComment.setReplyUserName(serverNewsComment.getParenter());
                    if(serverNewsComment.getParenttext()!=null){
                        newsComment.setReplyText(serverNewsComment.getParenttext());
                        newsComment.setReplyUserName(parenternameList.get(count));
                        newsComment.setIsReply(true);
                    }
                    commentList.add(newsComment);
                    str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
                    str_arr[str_arr.length-1] = "";
                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }
    private void changecommentagree(String commentsid){
        String url =connecturl+"/likeComment";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("uid",currentUserID);
        paramsMap.put("cid",commentsid);
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
                    playsong.setSongImageId(icon[index]);
                Looper.prepare();
                handler.post(play);
                Looper.loop();
            }
        });
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
    private void deletecomment(final int position){
        String url =connecturl+"/deleteComment";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("cid",commentList.get(position).getID());
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
                commentList.remove(position);
                for(int i=position+1;i<str_arr.length-1;i++){
                    str_arr[i]=str_arr[i+1];
                }
                str_arr = Arrays.copyOf(str_arr, str_arr.length-1);
                CommentCount--;
                Looper.prepare();
                handler.post(deleteshow);
                Looper.loop();
            }
        });
    }
    private void createnewscomment(final String text){
        String url =connecturl+"/createNewsComment";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("newsid",news.getNewsId());
        paramsMap.put("text",text);
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
                addNewComment(-1,(String)params.get("cid"),(String)params.get("username"),text);
            }
        });
    }
    private void replynewscomment(final String text,final int position){
        String url =connecturl+"/answerComment";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("userid",currentUserID);
        paramsMap.put("commentid",commentList.get(position).getID());
        paramsMap.put("text",text);
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
                addNewComment(position,(String)params.get("cid"),(String)params.get("username"),text);
            }
        });
    }
    private void showAgree(){
        if(agreeList.size()==0){
            content_agree_img.setVisibility(View.GONE);
            content_agree_text.setVisibility(View.GONE);
        }
        else {
            content_agree_img.setVisibility(View.VISIBLE);
            content_agree_text.setVisibility(View.VISIBLE);
            String agree_str = "<font color='#000000'>觉得很赞</font>";
            String content_agree_str=agreeList.get(0);
            if(agreeList.size()>1){
                for(int i=1;i<agreeList.size();i++){
                    content_agree_str+="、";
                    content_agree_str+=agreeList.get(i);
                }
            }
            content_agree_str+=agree_str;
            content_agree_text.setText(Html.fromHtml(content_agree_str));
        }
    }

    private void showShare(){
        if(shareList.size()==0){
            content_share_img.setVisibility(View.GONE);
            content_share_text.setVisibility(View.GONE);
        }
        else {
            content_share_img.setVisibility(View.VISIBLE);
            content_share_text.setVisibility(View.VISIBLE);
            String share_str = "<font color='#000000'>转发</font>";
            String content_share_str=shareList.get(0);
            if(shareList.size()>1){
                for(int i=1;i<shareList.size();i++){
                    content_share_str+="、";
                    content_share_str+=shareList.get(i);
                }
            }
            content_share_str+=share_str;
            content_share_text.setText(Html.fromHtml(content_share_str));
        }
    }

    private void addNewComment(int position,String id,String username,String text){
        NewsComment newComment=new NewsComment();
        newComment.setID(id);
        newComment.setCommentAgreeCount(0);
        newComment.setCommentText(text);
        newComment.setUserID(currentUserID);
        newComment.setCommentTime("8月9日19:57");
        if(newComment.getUserID().equals(currentUserID))
            newComment.setUserCover(R.drawable.user_image);
        else
            newComment.setUserCover(R.drawable.lemon);
        newComment.setUserName(username);
        newComment.setIsAgree(false);
        if(position==-1)
            newComment.setIsReply(false);
        else{
            newComment.setIsReply(true);
            newComment.setReplyUserName(commentList.get(position).getUserName());
            newComment.setReplyText(commentList.get(position).getCommentText());
        }
        commentList.add(newComment);
        str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
        str_arr[str_arr.length-1] = "";
        CommentCount++;
        Looper.prepare();
        handler.post(show);
        Looper.loop();
    }

    private void initComent(){
        NewsComment newsComment1=new NewsComment();
        newsComment1.setCommentAgreeCount(0);
        newsComment1.setCommentText("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        newsComment1.setCommentTime("8月9日19:57");
        newsComment1.setUserCover(R.drawable.user_image);
        newsComment1.setUserName("flowrain");
        newsComment1.setIsAgree(false);
        newsComment1.setIsReply(false);
        commentList.add(newsComment1);
        NewsComment newsComment2=new NewsComment();
        newsComment2.setCommentAgreeCount(10);
        newsComment2.setCommentText("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        newsComment2.setCommentTime("8月9日19:57");
        newsComment2.setUserCover(R.drawable.user_image);
        newsComment2.setUserName("梁月");
        newsComment2.setIsAgree(true);
        newsComment2.setIsReply(false);
        commentList.add(newsComment2);
        NewsComment newsComment3=new NewsComment();
        newsComment3.setCommentAgreeCount(0);
        newsComment3.setCommentText("嘤嘤嘤");
        newsComment3.setCommentTime("8月9日19:57");
        newsComment3.setUserCover(R.drawable.user_image);
        newsComment3.setUserName("flowrain");
        newsComment3.setIsAgree(false);
        newsComment3.setIsReply(true);
        newsComment3.setReplyUserName("梁月");
        newsComment3.setReplyText("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        commentList.add(newsComment3);
        for(int i=0;i<=commentList.size();i++){
            str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
            str_arr[str_arr.length-1] = "";
        }
    }

    private void initPopwindow() {
        popupView = LayoutInflater.from(this).inflate(R.layout.manage_news_detail_layout, null, false);
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
        TextView popup_out_left=popupView.findViewById(R.id.popup_out_left);
        popup_out_left.setOnClickListener(new View.OnClickListener() {
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
        //评论管理弹窗
        commentpopupView = LayoutInflater.from(this).inflate(R.layout.manage_news_comment_layout, null, false);
        commentpopupWindow = new PopupWindow(commentpopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        commentpopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        commentpopupWindow.setFocusable(true);
        commentpopupWindow.setTouchable(true);
        commentpopupWindow.setOutsideTouchable(true);
        commentpopupWindow.setAnimationStyle(R.style.mangagenewsTranslate);
        commentpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView commentpopup_out_above=commentpopupView.findViewById(R.id.popup_out_above);
        commentpopup_out_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentpopupWindow.dismiss();
            }
        });
        TextView commentpopup_out_below=commentpopupView.findViewById(R.id.popup_out_below);
        commentpopup_out_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentpopupWindow.dismiss();
            }
        });
    }
    //显示评论管理弹窗
    private void showcommentPopWindow(final int position) {
        View rootview = LayoutInflater.from(this).inflate(R.layout.news_detail_layout, null);
        TextView delete_title = commentpopupView.findViewById(R.id.delete_title);
        TextView add_same_title = commentpopupView.findViewById(R.id.add_same_title);
        final TextView copy_btn = commentpopupView.findViewById(R.id.copy_btn);
        TextView delete_btn = commentpopupView.findViewById(R.id.delete_btn);
        TextView add_same_btn = commentpopupView.findViewById(R.id.add_same_btn);
        ImageView others_comment_line = commentpopupView.findViewById(R.id.others_comment_line);
        ImageView my_comment_line = commentpopupView.findViewById(R.id.my_comment_line);
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(commentList.get(position).getCommentText());
                Toast.makeText(NewsDetailActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
                commentpopupWindow.dismiss();
            }
        });
        if(commentList.get(position).getUserID().equals(currentUserID)){
            final int current=position;
            add_same_title.setVisibility(View.GONE);
            add_same_btn.setVisibility(View.GONE);
            others_comment_line.setVisibility(View.GONE);
            delete_title.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletecomment(current);
                }
            });
            my_comment_line.setVisibility(View.VISIBLE);
        }else {
            delete_title.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
            add_same_title.setVisibility(View.VISIBLE);
            add_same_btn.setVisibility(View.VISIBLE);
            my_comment_line.setVisibility(View.GONE);
            others_comment_line.setVisibility(View.VISIBLE);
            add_same_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createnewscomment(commentList.get(position).getCommentText());
                    commentpopupWindow.dismiss();
                }
            });
        }
        commentpopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    //显示动态管理弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this).inflate(R.layout.news_detail_layout, null);
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
        if(news.getNewsType().equals("单曲")) {
            next_play_title.setVisibility(View.VISIBLE);
            next_play_btn.setVisibility(View.VISIBLE);
            next_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NewsDetailActivity.this,"已加入播放列表",Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
        }
        else
            next_play_title.setVisibility(View.GONE);
        if(news.getNewsCreatorID().equals(currentUserID)){
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
                    popupWindow.dismiss();
                    finish_activity("delete");
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
            others_news_line.setVisibility(View.VISIBLE);
            ignore_news_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    finish_activity("ignore");
                }
            });
            unfollow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    finish_activity("unfollow");
                }
            });
        }
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onBackPressed() {
        finish_activity("back");
    }

    private void finish_activity(String option){
        Intent intent = new Intent();
        intent.putExtra("Option", option);
        intent.putExtra("Position", position_str);
        intent.putExtra("Position", position_str);
        intent.putExtra("AgreeCount", String.valueOf(news.getAgreeCount()));
        intent.putExtra("CommentCount", String.valueOf(CommentCount));
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }

    private void content_agree_pressed(){
        int temp =news.getAgreeCount();
        if(news.getIsAgree()) {
            news.setIsAgree(false);
            content_agree.setImageResource(R.drawable.agree);
            temp--;
            news.setAgreeCount(temp);
            for(int i=0;i<agreeList.size();i++){
                if(agreeList.get(i).equals("梁月"))
                    agreeList.remove(i);
            }
            showAgree();
        }
        else {
            news.setIsAgree(true);
            content_agree.setImageResource(R.drawable.agree_active);
            Animation mAnimation = AnimationUtils.loadAnimation(this,R.anim. agree_btn_anim);
            content_agree.setAnimation(mAnimation);
            mAnimation.start();
            temp++;
            news.setAgreeCount(temp);
            agreeList.add("梁月");
            showAgree();
        }
    }

    private void comment_agree_pressed(int position){
        ImageView temp_comment_agree= comment_listview.getChildAt(position).findViewById(R.id.comment_agree);
        TextView temp_comment_agree_count= comment_listview.getChildAt(position).findViewById(R.id.comment_agree_count);
        int temp =commentList.get(position).getCommentAgreeCount();
        if(commentList.get(position).getIsAgree()) {
            commentList.get(position).setIsAgree(false);
            temp_comment_agree.setImageResource(R.drawable.agree);
            temp--;
            commentList.get(position).setCommentAgreeCount(temp);
            temp_comment_agree_count.setText(String.valueOf(temp));
            if(temp==0)
                temp_comment_agree_count.setVisibility(View.INVISIBLE);
            else
                temp_comment_agree_count.setVisibility(View.VISIBLE);
        }
        else {
            commentList.get(position).setIsAgree(true);
            temp_comment_agree.setImageResource(R.drawable.agree_active);
            Animation mAnimation = AnimationUtils.loadAnimation(this,R.anim. agree_btn_anim);
            temp_comment_agree.setAnimation(mAnimation);
            mAnimation.start();
            temp++;
            commentList.get(position).setCommentAgreeCount(temp);
            temp_comment_agree_count.setText(String.valueOf(temp));
            temp_comment_agree_count.setVisibility(View.VISIBLE);
        }
        changecommentagree(commentList.get(position).getID());
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.comment_agree:
                comment_agree_pressed(position);
                break;
            case R.id.comment_text:
                showcommentPopWindow(position);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.comment_reply:
                replyposition=position;
                InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                editcomment.requestFocus();
                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                inputManager.showSoftInput(editcomment, 0);
                String hint_str="回复"+commentList.get(position).getUserName()+":";
                editcomment.setHint(hint_str);
                editcomment.setText(str_arr[replyposition+1]);
                editcomment.setSelection(editcomment.getText().length());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
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
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1))+300;
        listView.setLayoutParams(params);
    }
}
