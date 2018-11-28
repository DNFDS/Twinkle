package com.example.twinkle;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListViewHolder> {
    Context context;
    private ArrayList<Song> musiclist = new ArrayList<Song>();

    public SongListAdapter(Context context){
//        musiclist.add(new MusicInfoItem(1,"POP/STARS","Madison Beer"));
//        musiclist.add(new MusicInfoItem(2,"Cornfield Chase","Hans Zimmer"));
//        musiclist.add(new MusicInfoItem(3,"Casablanca","Bertie Higgins"));

        this.context = context;
    }

    @Override
    public SongListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int layoutId) {
        return new SongListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_singlesong,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongListViewHolder viewHolder, int position) {
        viewHolder.textView.setText(musiclist.get(position).getSongName());

        viewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //给播放器提供接口
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class SongListViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public SongListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.item_SingleSong);
        }


    }

}
