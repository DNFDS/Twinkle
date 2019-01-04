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

public class SearchSonglistAdapter extends ArrayAdapter<SongList> implements View.OnClickListener {
    private List<SongList> songListList;
    public InnerItemOnclickListener mListener;
    private int resourceId;

    public SearchSonglistAdapter(Context context, int textViewResourceId, List<SongList> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        songListList=objects;
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
            viewHolder.songlist_name = view.findViewById (R.id.songlist_name);
            viewHolder.songlist_count_creator = view.findViewById (R.id.songlist_count_creator);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.songlist_cover.setImageResource(songlist.getSongListImageId());
        viewHolder.songlist_name.setText(songlist.getSongListName());
        String songlist_count_creator_str=songlist.getSongs_count()+"首 by"+songlist.getUserName();
        viewHolder.songlist_count_creator.setText(songlist_count_creator_str);
        return view;
    }
    class ViewHolder {
        ImageView songlist_cover;
        TextView songlist_name;
        TextView songlist_count_creator;
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
