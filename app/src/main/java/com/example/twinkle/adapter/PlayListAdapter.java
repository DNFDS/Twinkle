package com.example.twinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.Song;

import java.util.List;

public class PlayListAdapter extends ArrayAdapter<Song> implements View.OnClickListener{
    private List<Song> songList;
    public InnerItemOnclickListener mListener;
    private int resourceId;

    public PlayListAdapter(Context context, int textViewResourceId, List<Song> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        songList=objects;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Song song =getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.songlist_name = view.findViewById (R.id.songlist_name);
            viewHolder.delete_btn = view.findViewById (R.id.delete_btn);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        String str=song.getSongName()+"-"+song.getSingerName();
        viewHolder.songlist_name.setText(str);
        viewHolder.delete_btn.setOnClickListener(this);
        viewHolder.delete_btn.setTag(position);
        viewHolder.songlist_name.setTag(position);
        return view;
    }
    class ViewHolder {
        TextView songlist_name;
        ImageView delete_btn;
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
