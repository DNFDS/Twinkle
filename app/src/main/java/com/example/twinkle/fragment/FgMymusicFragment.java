package com.example.twinkle.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.twinkle.activity.ManageSonglistActivity;
import com.example.twinkle.R;
import com.example.twinkle.serverenity.ServerSongList;
import com.example.twinkle.serverenity.ServerUser;
import com.example.twinkle.activity.ShareSonglistActivity;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.activity.SongListDetailActivity;
import com.example.twinkle.adapter.SonglistAdapter;
import com.example.twinkle.adapter.SonglistAdapter.InnerItemOnclickListener;
import com.example.twinkle.enity.User;
import com.example.twinkle.activity.EditSonglistActivity;

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


public class FgMymusicFragment extends Fragment implements InnerItemOnclickListener, OnItemClickListener{
    private List<SongList> created_songListList = new ArrayList<SongList>();
    private List<SongList> collected_songListList = new ArrayList<SongList>();
    private String currentUserID;
    private String connecturl;
    private int currentPosition;
    private ImageView created_songlist_drop;
    private ImageView collected_songlist_drop;
    private ImageView created_songlist_line;
    private ImageView collected_songlist_line;
    private ListView created_songlist;
    private ListView collected_songlist;
    private View mymusicView;
    private View popupView;
    private View newsonglistView;
    private PopupWindow popupWindow;
    private PopupWindow newsonglistWindow;
    private SonglistAdapter created_songlistAdapter;
    private SonglistAdapter collected_songlistAdapter;
    private EditText newsonglistEditText;
    private Bundle bundle;
    private Handler handler=null;
    private Runnable show;
    private Runnable collectshow;
    private User currentUser=new User();
    int icon[] = new int[]{R.drawable.csonglist1, R.drawable.csonglist2, R.drawable.csonglist3,R.drawable.csonglist4};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mymusicView = inflater.inflate(R.layout.fg_my_music, container, false);
        handler=new Handler();
        show=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                created_songlistAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnChildren(created_songlist);
                collected_songlistAdapter.notifyDataSetInvalidated();
                setListViewHeightBasedOnChildren(collected_songlist);
            }
        };
        collectshow=new  Runnable(){
            @Override
            public void run() {
                //更新界面
                collected_songlistAdapter.notifyDataSetInvalidated();
                setListViewHeightBasedOnChildren(collected_songlist);
            }
        };
        bundle = this.getArguments();
        currentUserID = bundle.getString("currentUserID");
        connecturl = bundle.getString("connecturl");
        created_songlist_line=mymusicView.findViewById(R.id.created_songlist_line);
        collected_songlist_line=mymusicView.findViewById(R.id.collected_songlist_line);
        //初始化歌单列表
        getJsonData();
        created_songlist = mymusicView.findViewById(R.id.created_songlist_list_view);
        created_songlistAdapter = new SonglistAdapter(this.getActivity(), R.layout.songlist_item, created_songListList);
        created_songlistAdapter.setIsdrop(true);
        created_songlistAdapter.setOnInnerItemOnClickListener(this);
        created_songlist.setAdapter(created_songlistAdapter);
        created_songlist.setOnItemClickListener(this);
        collected_songlist = mymusicView.findViewById(R.id.collected_songlist_list_view);
        collected_songlistAdapter = new SonglistAdapter(this.getActivity(), R.layout.songlist_item, collected_songListList);
        collected_songlistAdapter.setIsdrop(false);
        collected_songlistAdapter.setOnInnerItemOnClickListener(this);
        collected_songlist.setAdapter(collected_songlistAdapter);
        collected_songlist.setOnItemClickListener(this);
        return mymusicView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化歌单下拉按钮
        created_songlist_drop =getActivity().findViewById(R.id.created_songlist_drop);
        created_songlist_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (created_songlistAdapter.getIsdrop()) {
                    created_songlist_drop.setImageResource(R.drawable.drop);
                    created_songlistAdapter.setIsdrop(false);
                    created_songlistAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(created_songlist);
                    created_songlist_line.setVisibility(View.VISIBLE);
                } else {
                    created_songlist_drop.setImageResource(R.drawable.shrink);
                    created_songlistAdapter.setIsdrop(true);
                    created_songlistAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(created_songlist);
                    created_songlist_line.setVisibility(View.INVISIBLE);
                }
            }
        });
        collected_songlist_drop =getActivity().findViewById(R.id.collected_songlist_drop);
        collected_songlist_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collected_songlistAdapter.getIsdrop()) {
                    collected_songlist_drop.setImageResource(R.drawable.drop);
                    collected_songlistAdapter.setIsdrop(false);
                    collected_songlistAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(collected_songlist);
                    collected_songlist_line.setVisibility(View.VISIBLE);
                } else {
                    collected_songlist_drop.setImageResource(R.drawable.shrink);
                    collected_songlistAdapter.setIsdrop(true);
                    getcollectData();
                }
            }
        });
        //初始化弹窗
        initPopwindow();
    }

    private void getJsonData(){
        String url =connecturl+"/getAllMySongList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("id",currentUserID);
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
                JSONObject userJson =(JSONObject)params.get("user");
                ServerUser user = JSON.toJavaObject(userJson,ServerUser.class);
                currentUser.setUserImage(R.drawable.user_image);
                currentUser.setUserName(user.getUsername());
                currentUser.setUserID(user.getUserid());
                List<ServerSongList> servercreatesonglistList = new ArrayList<>();
                List<Integer> servercreatesonglistcountlist = new ArrayList<>();
                List<ServerSongList> servercollectsonglistList = new ArrayList<>();
                List<Integer> servercollectsonglistcountlist = new ArrayList<>();
                servercreatesonglistList= JSON.parseArray(JSON.parseObject(responseStr).getString("Csonglist"), ServerSongList.class);
                servercreatesonglistcountlist= JSON.parseArray(JSON.parseObject(responseStr).getString("Cnum"), Integer.class);
                servercollectsonglistList= JSON.parseArray(JSON.parseObject(responseStr).getString("Ssonglist"), ServerSongList.class);
                servercollectsonglistcountlist= JSON.parseArray(JSON.parseObject(responseStr).getString("Snum"), Integer.class);
                created_songListList.clear();
                collected_songListList.clear();
                for(int i=0;i<servercreatesonglistList.size();i++){
                    ServerSongList serversongList=servercreatesonglistList.get(i);
                    SongList createsongList = new SongList();
                    createsongList.setSongListID(serversongList.getSonglistid());
                    createsongList.setSongListName(serversongList.getSonglistname());
                    createsongList.setSongListImageId(R.drawable.default_cover);
                    String songlistcount_str=String.valueOf(servercreatesonglistcountlist.get(i))+"首";
                    createsongList.setSongs_count(songlistcount_str);
                    createsongList.setUserID(serversongList.getUserid());
                    createsongList.setUserName(currentUser.getUserName());
                    if(createsongList.getSongListID().equals("3000518"))
                        createsongList.setSongListImageId(icon[0]);
                    if(createsongList.getSongListID().equals("3000531"))
                        createsongList.setSongListImageId(icon[1]);
                    if(createsongList.getSongListID().equals("3000530"))
                        createsongList.setSongListImageId(icon[2]);
                    if(createsongList.getSongListID().equals("3000533"))
                        createsongList.setSongListImageId(icon[3]);
                    created_songListList.add(createsongList);
                }
                for(int i=0;i<servercollectsonglistList.size();i++){
                    ServerSongList serversongList=servercollectsonglistList.get(i);
                    SongList collectsongList = new SongList();
                    collectsongList.setSongListID(serversongList.getSonglistid());
                    collectsongList.setSongListName(serversongList.getSonglistname());
                    collectsongList.setSongListImageId(R.drawable.default_cover);
                    if(collectsongList.getSongListID().equals("3000518"))
                        collectsongList.setSongListImageId(icon[0]);
                    if(collectsongList.getSongListID().equals("3000531"))
                        collectsongList.setSongListImageId(icon[1]);
                    if(collectsongList.getSongListID().equals("3000530"))
                        collectsongList.setSongListImageId(icon[2]);
                    if(collectsongList.getSongListID().equals("3000533"))
                        collectsongList.setSongListImageId(icon[3]);
                    String songlistcount_str=String.valueOf(servercollectsonglistcountlist.get(i))+"首";
                    collectsongList.setSongs_count(songlistcount_str);
                    collectsongList.setUserID(serversongList.getUserid());
                    collected_songListList.add(collectsongList);
                }
                Looper.prepare();
                handler.post(show);
                Looper.loop();
            }
        });
    }
    private void getcollectData(){
        String url =connecturl+"/getAllMySongList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("id",currentUserID);
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
                JSONObject userJson =(JSONObject)params.get("user");
                ServerUser user = JSON.toJavaObject(userJson,ServerUser.class);
                currentUser.setUserImage(R.drawable.user_image);
                currentUser.setUserName(user.getUsername());
                currentUser.setUserID(user.getUserid());
                List<ServerSongList> servercollectsonglistList = new ArrayList<>();
                List<Integer> servercollectsonglistcountlist = new ArrayList<>();
                servercollectsonglistList= JSON.parseArray(JSON.parseObject(responseStr).getString("Ssonglist"), ServerSongList.class);
                servercollectsonglistcountlist= JSON.parseArray(JSON.parseObject(responseStr).getString("Snum"), Integer.class);
                collected_songListList.clear();
                for(int i=0;i<servercollectsonglistList.size();i++){
                    ServerSongList serversongList=servercollectsonglistList.get(i);
                    SongList collectsongList = new SongList();
                    collectsongList.setSongListID(serversongList.getSonglistid());
                    collectsongList.setSongListName(serversongList.getSonglistname());
                    collectsongList.setSongListImageId(R.drawable.default_cover);
                    if(collectsongList.getSongListID().equals("3000518"))
                        collectsongList.setSongListImageId(icon[0]);
                    if(collectsongList.getSongListID().equals("3000531"))
                        collectsongList.setSongListImageId(icon[1]);
                    if(collectsongList.getSongListID().equals("3000530"))
                        collectsongList.setSongListImageId(icon[2]);
                    if(collectsongList.getSongListID().equals("3000533"))
                        collectsongList.setSongListImageId(icon[3]);
                    String songlistcount_str=String.valueOf(servercollectsonglistcountlist.get(i))+"首";
                    collectsongList.setSongs_count(songlistcount_str);
                    collectsongList.setUserID(serversongList.getUserid());
                    collected_songListList.add(collectsongList);
                }
                Looper.prepare();
                handler.post(collectshow);
                Looper.loop();
            }
        });
    }
    private void newsonglist(final String name){
        String url =connecturl+"/createSongList";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("id",currentUserID);
        paramsMap.put("name",name);
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
                String id=(String)params.get("msg");
                SongList newsongList = new SongList();
                newsongList.setSongListID(id);

                newsongList.setSongListImageId(R.drawable.default_cover);newsongList.setSongListName(name);
                String songlistcount_str="0首";
                newsongList.setSongs_count(songlistcount_str);
                newsongList.setUserID(currentUserID);
                created_songListList.add(newsongList);
            }
        });
    }

    private void changesonglistname(String id,final String name){
        String url =connecturl+"/changeSongListName";
        HashMap<String,String> paramsMap=new HashMap<>();
        paramsMap.put("songlistid",id);
        paramsMap.put("name",name);
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
                Log.d("liangyue",responseStr);
            }
        });
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.songlist_operate:
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
        Intent intent = new Intent(getActivity(),SongListDetailActivity.class);
        intent.putExtra("songlist",created_songListList.get(position));
        intent.putExtra("connecturl",connecturl);
        intent.putExtra("currentUserID",currentUserID);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
    }

    //初始化底部弹窗
    private void initPopwindow() {
        //歌单操作弹窗
        popupView = LayoutInflater.from(this.getActivity()).inflate(R.layout.popup_layout, null, false);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.animTranslate);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out=popupView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView pop_download=popupView.findViewById(R.id.pop_download_btn_area);
        pop_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        //跳转到分享歌单活动
        TextView pop_share=popupView.findViewById(R.id.pop_share_btn_area);
        pop_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(),ShareSonglistActivity.class);
                intent.putExtra("songlist",created_songListList.get(currentPosition));
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("currentUserID",currentUserID);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
        //跳转到编辑歌单信息活动
        TextView pop_edit=popupView.findViewById(R.id.pop_edit_btn_area);
        pop_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(),EditSonglistActivity.class);
                intent.putExtra("songlist",created_songListList.get(currentPosition));
                intent.putExtra("Position",currentPosition+"");
                FgMymusicFragment.this.startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
        //新建歌单弹窗
        newsonglistView= LayoutInflater.from(this.getActivity()).inflate(R.layout.new_songlist_layout, null, false);
        newsonglistWindow = new PopupWindow(newsonglistView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        newsonglistWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        newsonglistWindow.setFocusable(true);
        newsonglistWindow.setTouchable(true);
        newsonglistWindow.setOutsideTouchable(true);
        newsonglistWindow.setAnimationStyle(R.style.newsonglistTranslate);
        newsonglistWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                boolean handler =new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        setBackgroundAlpha(1.0f);
                    }
                 },300); // 延时0.3秒
            }
        });
        newsonglistEditText =newsonglistView.findViewById(R.id.new_songlist_eidtbox);
        newsonglistEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        newsonglistEditText .setImeOptions(EditorInfo.IME_ACTION_DONE);
        newsonglistEditText.setFocusable(true);
        newsonglistEditText.setFocusableInTouchMode(true);
        final InputMethodManager inputManager = (InputMethodManager)newsonglistEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        ImageView new_songlist_btn=mymusicView.findViewById(R.id.add_created_songlist_btn);
        new_songlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewSonglistWindow();
                setBackgroundAlpha(0.7f);
                newsonglistEditText.requestFocus();
                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                inputManager.showSoftInput(newsonglistEditText, 0);
            }
        });
        TextView new_songlist_cancel=newsonglistView.findViewById(R.id.cancel_box);
        new_songlist_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsonglistWindow.dismiss();
            }
        });
        TextView new_songlist_confirm=newsonglistView.findViewById(R.id.confirm_box);
        new_songlist_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!newsonglistEditText.getText().equals("")){
                    newsonglist(newsonglistEditText.getText().toString());
                    newsonglistWindow.dismiss();
                    getJsonData();
                }
            }
        });
        //跳转到管理歌单活动
        ImageView manage_songlist_btn=mymusicView.findViewById(R.id.manage_created_songlist_btn);
        manage_songlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ManageSonglistActivity.class);
                intent.putExtra("songListList",(Serializable) (created_songListList));
                intent.putExtra("connecturl",connecturl);
                FgMymusicFragment.this.startActivityForResult(intent, 2);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
    }

    //显示底部弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        TextView songlist_name =popupView.findViewById(R.id.pop_songlist_title);
        String pop_songlist_title="歌单："+created_songListList.get(currentPosition).getSongListName();
        songlist_name.setText(pop_songlist_title);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    //显示新建歌单弹窗
    private void showNewSonglistWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        newsonglistEditText.setText("");
        newsonglistWindow.showAtLocation(rootview, Gravity.TOP, 0, 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String option=data.getStringExtra("Option");
        String position_str=data.getStringExtra("Position");
        switch (option) {
            case "delete":
                getJsonData();
                boolean handler =new Handler().postDelayed(new Runnable() {
                    @Override public void run() {
                        created_songlistAdapter.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(created_songlist);
                    }
                },1000); // 延时0.3秒
                break;
            case "edit":
                SongList backsonglist=(SongList)data.getSerializableExtra("songlist");
                created_songListList.get(Integer.parseInt(position_str)).setSongListName(backsonglist.getSongListName());
                TextView temp_songlistname=created_songlist.getChildAt(Integer.parseInt(position_str)).findViewById(R.id.songlist_name);
                temp_songlistname.setText(backsonglist.getSongListName());
                changesonglistname(backsonglist.getSongListID(),backsonglist.getSongListName());
                break;
            default:

        }
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
