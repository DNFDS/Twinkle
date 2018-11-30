package com.example.twinkle;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder> {
    Context mContext;
    private ArrayList<Song> songList=new ArrayList<Song>();

    public SongListAdapter(Context context,String songlistName){
        this.mContext = context;
        songList.add(new Song(1,"POP/STARS"));
        songList.add(new Song(2,"Until You"));
        songList.add(new Song(3,"Cornfield Chase"));
        songList.add(new Song(4,"Waiting For Love"));
        songList.add(new Song(5,"Don't say lazy"));
        songList.add(new Song(6,"My Heart Will Go On"));

        switch (songlistName){
            case "Local":songList = StaticSongList.musicList_Local;
            break;
            case "Stars":songList = StaticSongList.musicList_Stars;
            break;
            case "Downloads":songList = StaticSongList.musicList_Downloads;
            break;
            case "History":songList = StaticSongList.musicList_History;
            break;
        }
    }

    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int layoutId) {
        return new SongListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singlesong,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder viewHolder, final int position) {
        viewHolder.textView.setText(songList.get(position).getSongName());
        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给播放器提供接口
                Toast.makeText(mContext, "SingleSongClicked :"+songList.get(position).getSongName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext,PlayerActivity.class);
                intent.putExtra("songName",songList.get(position).getSongName());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class SongListViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public SongListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.item_SingleSong);
        }
    }
}
