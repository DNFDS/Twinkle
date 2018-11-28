package com.example.twinkle;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class SongListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songlist);

        RecyclerView songListView = (RecyclerView)findViewById(R.id.view_SongList);
        songListView.setLayoutManager(new LinearLayoutManager(SongListActivity.this));
        songListView.setAdapter(new SongListAdapter(SongListActivity.this));
    }
}
