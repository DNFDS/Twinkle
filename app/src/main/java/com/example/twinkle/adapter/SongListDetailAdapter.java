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

public class SongListDetailAdapter extends ArrayAdapter<Song> implements View.OnClickListener {
    private List<Song> songList;
    public InnerItemOnclickListener mListener;
    private int resourceId;

    public SongListDetailAdapter(Context context, int textViewResourceId, List<Song> objects){
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
            viewHolder.search_song_operate =  view.findViewById (R.id.song_operate);
            viewHolder.count =  view.findViewById (R.id.count);
            viewHolder.song_name = view.findViewById (R.id.song_name);
            viewHolder.singer_album = view.findViewById (R.id.singer_album);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.count.setText(String.valueOf(position+1));
        viewHolder.song_name.setText(song.getSongName());
        String singer_album_str=song.getSingerName()+"-"+song.getAlbumName();
        viewHolder.singer_album.setText(singer_album_str);
        viewHolder.search_song_operate.setOnClickListener(this);
        viewHolder.search_song_operate.setTag(position);
        return view;
    }
    class ViewHolder {
        ImageView search_song_operate;
        TextView song_name;
        TextView singer_album;
        TextView count;
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
