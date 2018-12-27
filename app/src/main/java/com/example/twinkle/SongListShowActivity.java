package com.example.twinkle;

import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongListShowActivity extends AppCompatActivity {
    private TextView songList_title = null;
    private RecyclerView songList_RecyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_show);

        songList_title = (TextView) findViewById(R.id.song_list_title);
        songList_RecyclerView = (RecyclerView) findViewById(R.id.song_list_show_recyclerView);

        StaticSongList.Init();
        String SongListToShow = getIntent().getStringExtra("SongListToShow");

        songList_title.setText(SongListToShow);

        songList_RecyclerView.setLayoutManager(new LinearLayoutManager(SongListShowActivity.this));
        songList_RecyclerView.setAdapter(new SingleSongAdapter(SongListToShow, SongListShowActivity.this));
    }

}
