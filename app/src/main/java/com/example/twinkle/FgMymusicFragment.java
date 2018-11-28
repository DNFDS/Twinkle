package com.example.twinkle;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FgMymusicFragment extends Fragment {
    private ImageView localSongList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        return inflater.inflate(R.layout.fg_my_music,container,false);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);

        //test for SongList
        localSongList = (ImageView) view.findViewById(R.id.imageView);
        localSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log.d("check_3", "onClick: ");
                Intent intent = new Intent(getActivity(),SongListActivity.class);
                intent.putExtra("SongListName","localList");
                startActivity(intent);
            }
        });

    }
}
