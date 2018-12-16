package com.example.twinkle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SongListShowActivity extends AppCompatActivity {

    private TextView songList_title = null;
    private RecyclerView songList_RecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_show);

        StaticSongList.Init();
        String songList_toShow = getIntent().getStringExtra("SongListToShow");

        songList_title = (TextView)findViewById(R.id.song_list_title);
        songList_title.setText(songList_toShow);

        songList_RecyclerView = (RecyclerView) findViewById(R.id.song_list_show_recyclerView);
        songList_RecyclerView.setLayoutManager(new LinearLayoutManager(SongListShowActivity.this));
        songList_RecyclerView.setAdapter(new SingleSongAdapter(songList_toShow,SongListShowActivity.this));
    }
}
