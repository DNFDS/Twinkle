package com.example.twinkle;

import android.content.Context;
import android.nfc.Tag;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class SingleSongAdapter extends RecyclerView.Adapter<SingleSongAdapter.SingleSongViewHolder> {
    //目前只是以静态类模拟歌单存储功能
    //与数据库连接后需要改动
    @NonNull
    private Context mContext;
//    private ArrayList<Song> showingSongs = null;
    private List<SongListShowActivity.MusicInfo> showingSongs = null;

//    public SingleSongAdapter(String SongListToShow, Context context) {
//        this.mContext = context;
//
//        if(SongListToShow.equals("Local"))
//            showingSongs = StaticSongList.getSongListByName("Local");
//
//        Log.d("check123",getItemCount()+"");
//    }

    public SingleSongAdapter(List<SongListShowActivity.MusicInfo> songList, Context context) {
        this.mContext = context;
        showingSongs = songList;
        Log.d("check123",getItemCount()+"");
    }

    @Override
    public SingleSongAdapter.SingleSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int layoutId) {
        return new SingleSongViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_song, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SingleSongViewHolder viewHolder, int position) {

//        viewHolder.numShow.setText(showingSongs.get(position).getSongID() + "");
//        viewHolder.nameShow.setText(showingSongs.get(position).getSongName());
//        viewHolder.messageShow.setText(showingSongs.get(position).getSingerName());

        viewHolder.numShow.setText(showingSongs.get(position).ID + "");
        viewHolder.nameShow.setText(showingSongs.get(position).name);
        viewHolder.messageShow.setText(showingSongs.get(position).Artist);
    }

    @Override
    public int getItemCount() {
        return showingSongs.size();
    }

    class SingleSongViewHolder extends RecyclerView.ViewHolder{
        private TextView numShow = null;
        private TextView nameShow = null;
        private TextView messageShow = null;


        public SingleSongViewHolder(@NonNull View itemView) {
            super(itemView);

            numShow = (TextView)itemView.findViewById(R.id.song_num_textView);
            nameShow = (TextView)itemView.findViewById(R.id.song_name_textView);
            messageShow = (TextView)itemView.findViewById(R.id.song_message_textView);
        }
    }
}
