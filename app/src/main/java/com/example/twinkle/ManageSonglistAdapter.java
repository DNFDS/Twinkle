package com.example.twinkle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ManageSonglistAdapter extends ArrayAdapter<SongList> implements View.OnClickListener {
    private InnerItemOnclickListener mListener;
    private int resourceId;

    public ManageSonglistAdapter(Context context, int textViewResourceId, List<SongList> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        SongList songlist =getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.songlist_checkbox=view.findViewById (R.id.songlist_checkBox);
            if(songlist.getSonglistIsChecked()){
                viewHolder.songlist_checkbox.setChecked(true);
            }
            viewHolder.songlist_cover = view.findViewById (R.id.songlist_cover);
            viewHolder.songlist_check_area=view.findViewById (R.id.songlist_check_area);
            viewHolder.songlist_up =  view.findViewById (R.id.songlist_up);
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
        viewHolder.songlist_up.setOnClickListener(this);
        viewHolder.songlist_checkbox.setOnClickListener(this);
        viewHolder.songlist_up.setTag(position);
        viewHolder.songlist_check_area.setTag(position);
        viewHolder.songlist_checkbox.setTag(position);
        return view;
    }
    class ViewHolder {
        CheckBox songlist_checkbox;
        ImageView songlist_cover;
        ImageView songlist_up;
        TextView songlist_name;
        TextView songlist_check_area;
        TextView songs_count;
    }
    interface InnerItemOnclickListener {
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
