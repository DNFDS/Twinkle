package com.example.twinkle;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class FgMymusicFragment extends Fragment implements View.OnClickListener {
    protected View mView;
    private ImageView localSongList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mView = inflater.inflate(R.layout.fg_my_music,container,false);

        localSongList = (ImageView)mView.findViewById(R.id.imageView);
        localSongList.setOnClickListener(this);

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState){
        super.onViewCreated(view,savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imageView:{
                Intent intent = new Intent(getActivity(),SongListActivity.class);
                intent.putExtra("songListName","Local");
                startActivity(intent);
            }break;
        }
    }
}
