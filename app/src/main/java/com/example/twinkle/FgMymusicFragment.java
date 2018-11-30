package com.example.twinkle;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.example.twinkle.SonglistAdapter.InnerItemOnclickListener;
import android.widget.AdapterView.OnItemClickListener;
import com.example.twinkle.ScrollDisabledListView;
import java.util.ArrayList;
import java.util.List;

public class FgMymusicFragment extends Fragment implements InnerItemOnclickListener, OnItemClickListener{
    private List<SongList> created_songListList = new ArrayList<SongList>();
    private List<SongList> collected_songListList = new ArrayList<SongList>();
    private ImageView created_songlist_drop;
    private ImageView collected_songlist_drop;
    private ImageView created_songlist_line;
    private ImageView collected_songlist_line;
    private ImageView singlist_operate;
    private ListView created_songlist;
    private ListView collected_songlist;
    private TextView popup_out;
    private View contentView;
    private PopupWindow popupWindow;
    private SonglistAdapter created_songlistAdapter;
    private SonglistAdapter collected_songlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fg_my_music, container, false);
        created_songlist_line=view.findViewById(R.id.created_songlist_line);
        collected_songlist_line=view.findViewById(R.id.collected_songlist_line);
        //初始化歌单列表
        initSongLists();
        created_songlist = view.findViewById(R.id.created_songlist_list_view);
        created_songlistAdapter = new SonglistAdapter(this.getActivity(), R.layout.songlist_item, created_songListList);
        created_songlistAdapter.setIsdrop(true);
        created_songlistAdapter.setOnInnerItemOnClickListener(this);
        created_songlist.setAdapter(created_songlistAdapter);
        created_songlist.setOnItemClickListener(this);

        collected_songlist = view.findViewById(R.id.collected_songlist_list_view);
        collected_songlistAdapter = new SonglistAdapter(this.getActivity(), R.layout.songlist_item, collected_songListList);
        collected_songlistAdapter.setIsdrop(false);
        collected_songlistAdapter.setOnInnerItemOnClickListener(this);
        collected_songlist.setAdapter(collected_songlistAdapter);
        collected_songlist.setOnItemClickListener(this);

        return view;
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
        //初始化底部弹窗
        initPopwindow();
    }

    void initSongLists() {
        SongList songList1 = new SongList();
        songList1.setSongListName("我喜欢的音乐");
        songList1.setSongListImageId(R.drawable.cover);
        songList1.setSongs_count("18首");
        created_songListList.add(songList1);
        SongList songList2 = new SongList();
        songList2.setSongListName("我喜欢的音乐");
        songList2.setSongListImageId(R.drawable.cover);
        songList2.setSongs_count("18首");
        created_songListList.add(songList2);
        SongList songList3 = new SongList();
        songList3.setSongListName("我喜欢的音乐");
        songList3.setSongListImageId(R.drawable.cover);
        songList3.setSongs_count("18首");
        created_songListList.add(songList3);
        SongList songList4 = new SongList();
        songList4.setSongListName("我喜欢的音乐");
        songList4.setSongListImageId(R.drawable.cover);
        songList4.setSongs_count("18首");
        created_songListList.add(songList4);

        songList1.setSongListName("我喜欢的音乐");
        songList1.setSongListImageId(R.drawable.cover);
        songList1.setSongs_count("18首");
        collected_songListList.add(songList1);
        songList2.setSongListName("我喜欢的音乐");
        songList2.setSongListImageId(R.drawable.cover);
        songList2.setSongs_count("18首");
        collected_songListList.add(songList2);
        songList3.setSongListName("我喜欢的音乐");
        songList3.setSongListImageId(R.drawable.cover);
        songList3.setSongs_count("18首");
        collected_songListList.add(songList3);
        songList4.setSongListName("我喜欢的音乐");
        songList4.setSongListImageId(R.drawable.cover);
        songList4.setSongs_count("18首");
        collected_songListList.add(songList4);
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.songlist_operate:
                showPopWindow();
                setBackgroundAlpha(0.7f);
                break;
            default:
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    //底部弹窗
    private void initPopwindow() {
        contentView = LayoutInflater.from(this.getActivity()).inflate(R.layout.popup_layout, null, false);
        popupWindow = new PopupWindow(contentView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.animTranslate);
        popup_out=contentView.findViewById(R.id.popup_out);
        popup_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    private void showPopWindow() {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
    }
}
