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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.adapter.SearchSonglistAdapter;
import com.example.twinkle.adapter.SearchSonglistAdapter.InnerItemOnclickListener;
import com.example.twinkle.serverenity.ServerSongList;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.activity.SongListDetailActivity;

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

public class FgserchsonglistFragment extends Fragment implements InnerItemOnclickListener, OnItemClickListener{
    private View searchsonglistView;
    private String connecturl;
    private String currentUserID;
    private String searchKey;
    private List<SongList> searchsonglistList = new ArrayList<SongList>();
    private ListView searchsonglistListview;
    private int currentPosition;
    private Bundle bundle;
    private Handler handler=null;
    private Runnable show;
    private SearchSonglistAdapter searchSonglistAdapter;
    private int icon[] = new int[]{R.drawable.csonglist1, R.drawable.csonglist2, R.drawable.csonglist3,R.drawable.csonglist4};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        searchsonglistView = inflater.inflate(R.layout.fg_search_songlist, container, false);
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                searchSonglistAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(searchsonglistListview);
            }
        };
        bundle = this.getArguments();
        searchKey = bundle.getString("searchkey");
        connecturl = bundle.getString("connecturl");
        currentUserID=bundle.getString("currentUserID");
        searchsonglistList.clear();
        getJsonData();
        searchsonglistListview=searchsonglistView.findViewById(R.id.search_songlist_list_view);
        searchSonglistAdapter = new SearchSonglistAdapter(this.getActivity(), R.layout.search_songlist_item, searchsonglistList);
        searchSonglistAdapter.setOnInnerItemOnClickListener(this);
        searchsonglistListview.setAdapter(searchSonglistAdapter);
        searchsonglistListview.setOnItemClickListener(this);
        return searchsonglistView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    private void getJsonData(){
        String url =connecturl+"/searchSongList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("name",searchKey);
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
                List<ServerSongList> serversonglistList = new ArrayList<>();
                List<Integer> serversonglistcountlist = new ArrayList<>();
                List<String> usernamelist = new ArrayList<>();
                serversonglistList= JSON.parseArray(JSON.parseObject(responseStr).getString("songlist"), ServerSongList.class);
                serversonglistcountlist= JSON.parseArray(JSON.parseObject(responseStr).getString("songnum"), Integer.class);
                usernamelist= JSON.parseArray(JSON.parseObject(responseStr).getString("users"), String.class);
                searchsonglistList.clear();
                for(int i=0;i<serversonglistList.size();i++){
                    ServerSongList serversongList=serversonglistList.get(i);
                    SongList songList = new SongList();
                    songList.setSongListID(serversongList.getSonglistid());
                    songList.setSongListName(serversongList.getSonglistname());
                    songList.setSongListImageId(R.drawable.default_cover);
                    if(songList.getSongListID().equals("3000518"))
                        songList.setSongListImageId(icon[0]);
                    if(songList.getSongListID().equals("3000531"))
                        songList.setSongListImageId(icon[1]);
                    if(songList.getSongListID().equals("3000530"))
                        songList.setSongListImageId(icon[2]);
                    if(songList.getSongListID().equals("3000533"))
                        songList.setSongListImageId(icon[3]);
                    String songlistcount_str=String.valueOf(serversonglistcountlist.get(i));
                    songList.setSongs_count(songlistcount_str);
                    songList.setUserID(serversongList.getUserid());
                    songList.setUserName(usernamelist.get(i));
                    searchsonglistList.add(songList);
                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.songlist_operate:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),SongListDetailActivity.class);
        intent.putExtra("songlist",searchsonglistList.get(position));
        intent.putExtra("connecturl",connecturl);
        intent.putExtra("currentUserID",currentUserID);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
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
