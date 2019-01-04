package com.example.twinkle.fgactivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.StaticSongList;
import com.example.twinkle.activity.PlayerActivity;
import com.example.twinkle.adapter.MyFragmentAdapter;
import com.example.twinkle.enity.SongList;
import com.example.twinkle.enity.User;
import com.example.twinkle.fragment.FgFindmusicFragment;
import com.example.twinkle.fragment.FgFriendsFragment;
import com.example.twinkle.fragment.FgMymusicFragment;
import com.example.twinkle.singleton.PlayingSongList;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private View view_status;
    private ImageView iv_title_find_music;
    private ImageView iv_title_my_music;
    private ImageView iv_title_friends;
    private ViewPager vp_content;
    private Toolbar toolbars;
    private ImageView search_title_image;
    private Bundle bundle;
    private List<SongList> songListList = new ArrayList<SongList>();
    private String connecturl;
    private User currentuser=new User();
    PlayingSongList playingSongList = PlayingSongList.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#008df2"));
        }
        setContentView(R.layout.activity_main);
        currentuser=(User) getIntent().getSerializableExtra("loginUser");
        connecturl=getIntent().getStringExtra("connecturl");
        bundle = new Bundle();
        bundle.putString("currentUserID", currentuser.getUserID());
        bundle.putString("connecturl", connecturl);
        initView();
        initContentFragment();
        StaticSongList.Init();
    }

    void initView(){
        view_status=(View)findViewById(R.id.view_status);
        iv_title_find_music=(ImageView)findViewById(R.id.iv_title_find_music);
        iv_title_my_music=(ImageView)findViewById(R.id.iv_title_my_music);
        iv_title_friends=(ImageView)findViewById(R.id.iv_title_friends);
        search_title_image=findViewById(R.id.search_title_image);
        TextView bottm_player_btn=findViewById(R.id.bottm_player_btn);
        bottm_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,PlayerActivity.class);
                intent.putExtra("currentUserID",currentuser.getUserID());
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("option","");
                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        });
        vp_content=(ViewPager)findViewById(R.id.vp_content);
        toolbars=(Toolbar)findViewById(R.id.toolbars);
        search_title_image.setOnClickListener(this);
        iv_title_find_music.setOnClickListener(this);
        iv_title_my_music.setOnClickListener(this);
        iv_title_friends.setOnClickListener(this);
        search_title_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchMainActivity.class);
                intent.putExtra("currentUserID",currentuser.getUserID());
                intent.putExtra("connecturl",connecturl);
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
    }

    private void initContentFragment(){
        ArrayList<Fragment> mFragmentList=new ArrayList<>();
        mFragmentList.add(new FgFindmusicFragment());
        mFragmentList.add(new FgMymusicFragment());
        mFragmentList.add(new FgFriendsFragment());
        mFragmentList.get(0).setArguments(bundle);
        mFragmentList.get(1).setArguments(bundle);
        mFragmentList.get(2).setArguments(bundle);
        MyFragmentAdapter adapter =new MyFragmentAdapter(getSupportFragmentManager(),mFragmentList);
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
            case R.id.search_title_image:
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                intent.putExtra("currentUser",currentuser);
                startActivity(intent);
                overridePendingTransition(R.anim.left_slide_out, R.anim.right_slide_in);
        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}