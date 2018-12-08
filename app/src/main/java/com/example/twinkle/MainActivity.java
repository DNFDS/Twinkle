package com.example.twinkle;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private View view_status;
    private ImageView iv_title_find_music;
    private ImageView iv_title_my_music;
    private ImageView iv_title_friends;
    private ViewPager vp_content;
    private Toolbar toolbars;
    private List<SongList> songListList = new ArrayList<SongList>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#3b95d4"));
        }
        initView();
        initContentFragment();
    }

    void initView(){
        view_status=(View)findViewById(R.id.view_status);
        iv_title_find_music=(ImageView)findViewById(R.id.iv_title_find_music);
        iv_title_my_music=(ImageView)findViewById(R.id.iv_title_my_music);
        iv_title_friends=(ImageView)findViewById(R.id.iv_title_friends);
        vp_content=(ViewPager)findViewById(R.id.vp_content);
        toolbars=(Toolbar)findViewById(R.id.toolbars);

        iv_title_find_music.setOnClickListener(this);
        iv_title_my_music.setOnClickListener(this);
        iv_title_friends.setOnClickListener(this);

    }

    private void initContentFragment(){
        ArrayList<Fragment> mFragmentList=new ArrayList<>();
        mFragmentList.add(new FgFindmusicFragment());
        mFragmentList.add(new FgMymusicFragment());
        mFragmentList.add(new FgFriendsFragment());
        com.example.twinkle.MyFragmentAdapter adapter =new com.example.twinkle.MyFragmentAdapter(getSupportFragmentManager(),mFragmentList);
        vp_content.setAdapter(adapter);
        vp_content.setOffscreenPageLimit(2);
        vp_content.addOnPageChangeListener(this);
        setSupportActionBar(toolbars);
        ActionBar actionBar =getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayShowTitleEnabled(false);
        }
        setCurrentItem(0);
    }

    private void setCurrentItem(int i){
        vp_content.setCurrentItem(i);
        iv_title_find_music.setSelected(false);
        iv_title_my_music.setSelected(false);
        iv_title_friends.setSelected(false);
        switch (i){
            case 0:
                iv_title_find_music.setSelected(true);
                break;
            case 1:
                iv_title_my_music.setSelected(true);
                break;
            case 2:
                iv_title_friends.setSelected(true);
                break;
        }
    }

    @Override
    public void onPageScrolled(int position,float postionOffset,int positionOffsetPixels){
    }

    @Override
    public void onPageSelected(int position){
        setCurrentItem(position);
    }

    @Override
    public  void onPageScrollStateChanged(int state){

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_title_find_music:
                if(vp_content.getCurrentItem()!=0){
                    setCurrentItem(0);
            }
            break;
            case R.id.iv_title_my_music:
                if(vp_content.getCurrentItem()!=1){
                    setCurrentItem(1);
                }
                break;
            case R.id.iv_title_friends:
                if(vp_content.getCurrentItem()!=2){
                    setCurrentItem(2);
                }
                break;
        }
    }
}