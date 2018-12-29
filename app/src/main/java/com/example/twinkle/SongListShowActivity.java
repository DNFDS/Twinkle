package com.example.twinkle;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class SongListShowActivity extends AppCompatActivity {


    public class MusicInfo{
        public int ID;
        public String name;
        public String Artist;
    }


    private TextView songList_title = null;
    private RecyclerView songList_RecyclerView = null;
    private ArrayList<MusicInfo> mMusicList = null;


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
//        songList_RecyclerView.setAdapter(new SingleSongAdapter(SongListToShow, SongListShowActivity.this));


        Cursor mCursor = getApplicationContext().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,null,null,MediaStore.Audio.AudioColumns.IS_MUSIC);

        if(mCursor!=null){
            int i=0;
            while(mCursor.moveToNext()){
                MusicInfo singleSong = new MusicInfo();

                singleSong.ID = ++i;
                singleSong.name = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                singleSong.Artist = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));

                mMusicList.add(singleSong);
            }
            mCursor.close();
        }




//        for (int i = 0; i < mCursor.getCount(); ++i) {
//            MusicInfo singleSong = new MusicInfo();
//            mCursor.moveToNext();
//
//            singleSong.ID = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
//            singleSong.name = mCursor.getString(1);
//            singleSong.Artist = mCursor.getString(2);
//
//            mMusicList.add(singleSong);
//        }



        Log.d("check123","OnScan");
        Scan();
        songList_RecyclerView.setAdapter(new SingleSongAdapter(mMusicList, SongListShowActivity.this));

    }


    private void Scan() {

    }


}
