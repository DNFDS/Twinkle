package com.example.twinkle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.twinkle.SonglistAdapter.InnerItemOnclickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class FgMymusicFragment extends Fragment implements InnerItemOnclickListener, OnItemClickListener{
    private List<SongList> created_songListList = new ArrayList<SongList>();
    private List<SongList> collected_songListList = new ArrayList<SongList>();
    private String currentSonglistName;

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

    private ImageView local_songlist_imageView;
    private ImageView collected_songlist_imageview;
    private ImageView download_songlist_imageview;
    private ImageView recent_songlist_imageview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mymusicView = inflater.inflate(R.layout.fg_my_music, container, false);
        created_songlist_line=mymusicView.findViewById(R.id.created_songlist_line);
        collected_songlist_line=mymusicView.findViewById(R.id.collected_songlist_line);
        //初始化歌单列表
        initSongLists();
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
                if (created_songlistAdapter.getIsdrop()==true) {
                    created_songlist_drop.setImageResource(R.drawable.drop);
                    created_songlistAdapter.setIsdrop(false);
                    created_songlistAdapter.notifyDataSetChanged();
                    created_songlist_line.setVisibility(View.VISIBLE);
                } else {
                    created_songlist_drop.setImageResource(R.drawable.shrink);
                    created_songlistAdapter.setIsdrop(true);
                    created_songlistAdapter.notifyDataSetChanged();
                    created_songlist_line.setVisibility(View.INVISIBLE);
                }
            }
        });
        collected_songlist_drop =getActivity().findViewById(R.id.collected_songlist_drop);
        collected_songlist_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (collected_songlistAdapter.getIsdrop()==true) {
                    collected_songlist_drop.setImageResource(R.drawable.drop);
                    collected_songlistAdapter.setIsdrop(false);
                    collected_songlistAdapter.notifyDataSetChanged();
                    collected_songlist_line.setVisibility(View.VISIBLE);
                } else {
                    collected_songlist_drop.setImageResource(R.drawable.shrink);
                    collected_songlistAdapter.setIsdrop(true);
                    collected_songlistAdapter.notifyDataSetChanged();
                    collected_songlist_line.setVisibility(View.INVISIBLE);
                }
            }
        });
        //本地/收藏/下载/播放 列表图片
        local_songlist_imageView = (ImageView)mymusicView.findViewById(R.id.local_song_imageView);
        local_songlist_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SongListShowActivity.class);
                intent.putExtra("SongListToShow","Local");
                startActivity(intent);
            }
        });

        collected_songlist_imageview = (ImageView)mymusicView.findViewById(R.id.collect_song_imageView);
        collected_songlist_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SongListShowActivity.class);
                intent.putExtra("SongListToShow","Stars");
                startActivity(intent);
            }
        });

        download_songlist_imageview = (ImageView)mymusicView.findViewById(R.id.download_song_imageView);
        download_songlist_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SongListShowActivity.class);
                intent.putExtra("SongListToShow","Downloads");
                startActivity(intent);
            }
        });

        recent_songlist_imageview = (ImageView)mymusicView.findViewById(R.id.recent_song_imageView);
        recent_songlist_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),SongListShowActivity.class);
                intent.putExtra("SongListToShow","Recent");
                startActivity(intent);
            }
        });

        //初始化弹窗
        initPopwindow();
    }

    void initSongLists() {
        SongList songList1 = new SongList();
        songList1.setSongListName("我喜欢的音乐");
        songList1.setSongListImageId(R.drawable.cover);
        songList1.setSongs_count("18首");
        created_songListList.add(songList1);
        SongList songList2 = new SongList();
        songList2.setSongListName("temp");
        songList2.setSongListImageId(R.drawable.cover);
        songList2.setSongs_count("18首");
        created_songListList.add(songList2);
        SongList songList3 = new SongList();
        songList3.setSongListName("code");
        songList3.setSongListImageId(R.drawable.cover);
        songList3.setSongs_count("18首");
        created_songListList.add(songList3);
        SongList songList4 = new SongList();
        songList4.setSongListName("电音");
        songList4.setSongListImageId(R.drawable.cover);
        songList4.setSongs_count("18首");
        created_songListList.add(songList4);

        SongList songList5 = new SongList();
        songList5.setSongListName("我喜欢的音乐");
        songList5.setSongListImageId(R.drawable.cover);
        songList5.setSongs_count("18首");
        collected_songListList.add(songList5);
        SongList songList6 = new SongList();
        songList6.setSongListName("思考");
        songList6.setSongListImageId(R.drawable.cover);
        songList6.setSongs_count("18首");
        collected_songListList.add(songList6);
        SongList songList7 = new SongList();
        songList7.setSongListName("回忆");
        songList7.setSongListImageId(R.drawable.cover);
        songList7.setSongs_count("18首");
        collected_songListList.add(songList7);
        SongList songList8 = new SongList();
        songList8.setSongListName("青春");
        songList8.setSongListImageId(R.drawable.cover);
        songList8.setSongs_count("18首");
        collected_songListList.add(songList8);
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.songlist_operate:
                currentSonglistName=created_songListList.get(position).getSongListName();
                showPopWindow();
                setBackgroundAlpha(0.7f);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        currentSonglistName = (String) ((TextView)view.findViewById(R.id.songlist_name)).getText();
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
                intent.putExtra("songlist_name",currentSonglistName);
                String songlist_creator="";
                for(int i = 0;i < created_songListList.size(); i ++){
                    if(created_songListList.get(i).getSongListName()==currentSonglistName){
                       // songlist_creator=created_songListList.get(i).getUserID();
                        songlist_creator="梁月";
                    }
                }
                intent.putExtra("songlist_creator",songlist_creator);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
        //跳转到编辑歌单信息活动
        TextView pop_edit=popupView.findViewById(R.id.pop_edit_btn_area);
        pop_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(getActivity(),EditSonglistActivity.class);
                intent.putExtra("songlist_name",currentSonglistName);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
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
        ImageView new_songlist_btn=mymusicView.findViewById(R.id.add_created_songlist_btn);
        new_songlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewSonglistWindow();
                setBackgroundAlpha(0.7f);
                EditText newsonglistEditText =newsonglistView.findViewById(R.id.new_songlist_eidtbox);
                newsonglistEditText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                newsonglistEditText .setImeOptions(EditorInfo.IME_ACTION_DONE);
                newsonglistEditText.setFocusable(true);
                newsonglistEditText.setFocusableInTouchMode(true);;
                InputMethodManager inputManager = (InputMethodManager)newsonglistEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
        //跳转到管理歌单活动
        ImageView manage_songlist_btn=mymusicView.findViewById(R.id.manage_created_songlist_btn);
        manage_songlist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ManageSonglistActivity.class);
                intent.putExtra("songListList",(Serializable) (created_songListList));//key就是自己定义一个String的字符串就行了
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.push_fade_out, R.anim.push_fade_in);
            }
        });
    }

    //显示底部弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        TextView songlist_name =popupView.findViewById(R.id.pop_songlist_title);
        String pop_songlist_title="歌单："+currentSonglistName;
        songlist_name.setText(pop_songlist_title);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    //显示新建歌单弹窗
    private void showNewSonglistWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        newsonglistWindow.showAtLocation(rootview, Gravity.TOP, 0, 100);
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
    }
}
