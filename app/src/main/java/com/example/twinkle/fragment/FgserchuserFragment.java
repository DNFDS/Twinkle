package com.example.twinkle.fragment;

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
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.R;
import com.example.twinkle.adapter.SearchUserAdapter;
import com.example.twinkle.adapter.SearchUserAdapter.InnerItemOnclickListener;
import com.example.twinkle.serverenity.ServerUser;
import com.example.twinkle.enity.User;

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

public class FgserchuserFragment extends Fragment implements InnerItemOnclickListener, OnItemClickListener{
    private View searchuserView;
    private String connecturl;
    private String currentUserID;
    private String searchKey;
    private List<User> searchuserList = new ArrayList<User>();
    private ListView searchuserListview;
    private Bundle bundle;
    private Handler handler=null;
    private Runnable show;
    private SearchUserAdapter searchUserAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        searchuserView = inflater.inflate(R.layout.fg_search_user, container, false);
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                searchUserAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(searchuserListview);
            }
        };
        bundle = this.getArguments();
        searchKey = bundle.getString("searchkey");
        currentUserID=bundle.getString("currentUserID");
        connecturl = bundle.getString("connecturl");
        searchuserList.clear();
        getJsonData();
        searchuserListview=searchuserView.findViewById(R.id.search_user_list_view);
        searchUserAdapter = new SearchUserAdapter(this.getActivity(), R.layout.search_user_item, searchuserList);
        searchUserAdapter.setOnInnerItemOnClickListener(this);
        searchuserListview.setAdapter(searchUserAdapter);
        searchuserListview.setOnItemClickListener(this);
        return searchuserView;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    private void getJsonData(){
        String url =connecturl+"/searchUser";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("name",searchKey);
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
                List<ServerUser> serveruserList = new ArrayList<>();
                List<Integer> serverfollowcountlist = new ArrayList<>();
                List<Boolean> isfollowlist = new ArrayList<>();
                serveruserList= JSON.parseArray(JSON.parseObject(responseStr).getString("users"), ServerUser.class);
                serverfollowcountlist= JSON.parseArray(JSON.parseObject(responseStr).getString("fansnum"), Integer.class);
                isfollowlist= JSON.parseArray(JSON.parseObject(responseStr).getString("isfollowed"), Boolean.class);
                searchuserList.clear();
                for(int i=0;i<serveruserList.size();i++){
                    ServerUser serverUser=serveruserList.get(i);
                    User user = new User();
                    user.setUserID(serverUser.getUserid());
                    user.setUserName(serverUser.getUsername());
                    user.setIsFollow(isfollowlist.get(i));
                    user.setUserFollowCount(serverfollowcountlist.get(i));
                    user.setUserImage(R.drawable.user_default);
                    searchuserList.add(user);
                }
                Looper.prepare();
                handler.post(show);
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
                Log.d("liangyue",response.body().toString());
            }
        });
    }
    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.follow_operate:
                changefollow(searchuserList.get(position).getUserID());
                ImageView temp_follow_operate=searchuserListview.getChildAt(position).findViewById(R.id.follow_operate);
                if(searchuserList.get(position).getIsFollow()){
                    searchuserList.get(position).setIsFollow(false);
                    temp_follow_operate.setImageResource(R.drawable.follow);
                }else{
                    searchuserList.get(position).setIsFollow(true);
                    temp_follow_operate.setImageResource(R.drawable.unfollow);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
