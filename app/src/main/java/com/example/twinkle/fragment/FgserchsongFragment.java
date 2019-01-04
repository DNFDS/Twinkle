package com.example.twinkle.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.activity.PlayerActivity;
import com.example.twinkle.activity.ShareSongActivity;
import com.example.twinkle.adapter.AddSongAdapter;
import com.example.twinkle.adapter.SearchSongAdapter;
import com.example.twinkle.adapter.SearchSongAdapter.InnerItemOnclickListener;
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

public class FgserchsongFragment extends Fragment implements InnerItemOnclickListener, OnItemClickListener{
    private View searchsongView;
    private String connecturl;
    private String currentUserID;
    private String searchKey;
    private List<Song> searchsongList = new ArrayList<Song>();
    private List<SongList> songListList = new ArrayList<SongList>();
    private ListView searchsongListview;
    private ListView addsongListview;
    private int currentPosition;
    private Bundle bundle;
    private Handler handler=null;
    private SearchSongAdapter searchSongAdapter;
    private AddSongAdapter addSongAdapter;
    private PopupWindow searchPopupWindow;
    private View searchPopupView;
    private PopupWindow addSongPopupWindow;
    private View addSongPopupView;
    private Runnable show;
    private Runnable addsongshow;
    private String msg;
    private PlayingSongList playingSongList = PlayingSongList.getInstance();
    private int icon[] = new int[]{R.drawable.song1, R.drawable.song2, R.drawable.song3,R.drawable.song4,
            R.drawable.song5,R.drawable.song6,R.drawable.song7,R.drawable.song8,R.drawable.song9,R.drawable.song10,
            R.drawable.song11,R.drawable.song12,R.drawable.song13,R.drawable.song14,R.drawable.song15,
            R.drawable.song16,R.drawable.song17,R.drawable.song18,R.drawable.song19,R.drawable.song20,R.drawable.song21,R.drawable.song22};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        searchsongView = inflater.inflate(R.layout.fg_search_song, container, false);
        initPopwindow();
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                searchSongAdapter.notifyDataSetChanged();
                addSongAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(searchsongListview);
            }
        };
        addsongshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                addSongPopupWindow.dismiss();
                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
            }
        };
        bundle = this.getArguments();

        searchKey = bundle.getString("searchkey");
        currentUserID=bundle.getString("currentUserID");
        connecturl = bundle.getString("connecturl");
        searchsongList.clear();
        getJsonData();
        searchsongListview=searchsongView.findViewById(R.id.search_song_list_view);
        searchSongAdapter = new SearchSongAdapter(this.getActivity(), R.layout.search_song_item, searchsongList);
        searchSongAdapter.setOnInnerItemOnClickListener(this);
        searchsongListview.setAdapter(searchSongAdapter);
        searchsongListview.setOnItemClickListener(this);

        return searchsongView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    private void getJsonData(){
        String url =connecturl+"/searchSong";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("name",searchKey);
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
                searchsongList.clear();
                songListList.clear();
                for(int i=0;i<serversonglist.size();i++){
                    ServerSong serversong=serversonglist.get(i);
                    Song song = new Song();
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
                    searchsongList.add(song);
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
        paramsMap.put("songid",searchsongList.get(currentPosition).getSongID());
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
                handler.post(addsongshow);
                Looper.loop();
            }
        });
    }
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.search_song_operate:
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
        if( playingSongList.getSize()==0){
            List<Song> tempsongList = new ArrayList<Song>();
            tempsongList.add(searchsongList.get(currentPosition));
            playingSongList.InitPlayingSongList(tempsongList,"search",0);
        }
        if(!playingSongList.getNow().getSongID().equals(searchsongList.get(currentPosition).getSongID())){
            playingSongList.addNext(searchsongList.get(currentPosition));
            playingSongList.moveToNext();
        }
        Intent intent = new Intent(getActivity(),PlayerActivity.class);
        intent.putExtra("currentUserID",currentUserID);
        intent.putExtra("connecturl",connecturl);
        intent.putExtra("option","next");
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }
    //初始化底部弹窗
    private void initPopwindow() {
        //搜索歌曲操作弹窗
        searchPopupView = LayoutInflater.from(this.getActivity()).inflate(R.layout.search_song_popup_layout, null, false);
        searchPopupWindow = new PopupWindow(searchPopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        searchPopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        searchPopupWindow.setFocusable(true);
        searchPopupWindow.setTouchable(true);
        searchPopupWindow.setOutsideTouchable(true);
        searchPopupWindow.setAnimationStyle(R.style.animTranslate);
        searchPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out=searchPopupView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopupWindow.dismiss();
            }
        });
        //下一首播放
        TextView pop_next_play=searchPopupView.findViewById(R.id.next_play_btn_area);
        pop_next_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playingSongList.addNext(searchsongList.get(currentPosition));
                searchPopupWindow.dismiss();
            }
        });
        //跳转到分享歌单活动
        TextView pop_share=searchPopupView.findViewById(R.id.pop_share_btn_area);
        pop_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopupWindow.dismiss();
                Intent intent = new Intent(getActivity(),ShareSongActivity.class);
                intent.putExtra("song",searchsongList.get(currentPosition));
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("option","");
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
        //显示添加歌曲弹窗
        TextView pop_add=searchPopupView.findViewById(R.id.pop_colloct_btn_area);
        pop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchPopupWindow.dismiss();
                setBackgroundAlpha(0.7f);
                showaddSongPopWindow();
            }
        });
        //添加歌曲操作弹窗
        addSongPopupView = LayoutInflater.from(this.getActivity()).inflate(R.layout.add_song_popup_layout, null, false);
        addsongListview=addSongPopupView.findViewById(R.id.add_song_list_view);
        addSongAdapter = new AddSongAdapter(this.getActivity(), R.layout.add_song_item, songListList);
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
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.search_main, null);
        TextView pop_song_title =searchPopupView.findViewById(R.id.pop_song_title);
        String pop_song_str="单曲："+searchsongList.get(currentPosition).getSongName();
        pop_song_title.setText(pop_song_str);
        searchPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    //显示添加歌曲弹窗
    private void showaddSongPopWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.search_main, null);
        addSongPopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
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
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
