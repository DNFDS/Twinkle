package com.example.twinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.SongList;

import java.util.List;

public class AddSongAdapter extends ArrayAdapter<SongList> {
    private List<SongList> songListList;
    private int resourceId;

    public AddSongAdapter(Context context, int textViewResourceId, List<SongList> objects){
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
            viewHolder.songlist_name = view.findViewById (R.id.songlist_name);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.songlist_name.setText(songlist.getSongListName());
        return view;
    }
    class ViewHolder {
        TextView songlist_name;
    }

}
