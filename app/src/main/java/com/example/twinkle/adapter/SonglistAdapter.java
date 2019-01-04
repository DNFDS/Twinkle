package com.example.twinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.SongList;

import java.util.List;

public class SonglistAdapter extends ArrayAdapter<SongList> implements View.OnClickListener {
    private List<SongList> songListList;
    public InnerItemOnclickListener mListener;
    private int resourceId;
    private boolean Isdrop;

    public SonglistAdapter(Context context, int textViewResourceId,List<SongList> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        songListList=objects;
    }

    public void setIsdrop(boolean isdrop ){
        Isdrop=isdrop;
    }
    public boolean getIsdrop(){
        return Isdrop;
    }
    @Override
    public int getCount() {
        // 重点区域
        if (Isdrop) {
            return songListList.size();
        } else {
            return 0;
        }
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        SongList songlist =getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.songlist_cover = view.findViewById (R.id.songlist_cover);
            viewHolder.songlist_operate =  view.findViewById (R.id.songlist_operate);
            viewHolder.songlist_name = view.findViewById (R.id.songlist_name);
            viewHolder.songs_count = view.findViewById (R.id.songs_count);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.songlist_cover.setImageResource(songlist.getSongListImageId());
        viewHolder.songlist_name.setText(songlist.getSongListName());
        viewHolder.songs_count.setText(songlist.getSongs_count());
        viewHolder.songlist_operate.setOnClickListener(this);
        viewHolder.songlist_operate.setTag(position);
        return view;
    }
    class ViewHolder {
        ImageView songlist_cover;
        ImageView songlist_operate;
        TextView songlist_name;
        TextView songs_count;
    }
    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
