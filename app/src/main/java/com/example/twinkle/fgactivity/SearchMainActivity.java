package com.example.twinkle.fgactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.activity.PlayerActivity;
import com.example.twinkle.adapter.MyFragmentAdapter;
import com.example.twinkle.fragment.FgserchsongFragment;
import com.example.twinkle.fragment.FgserchsonglistFragment;
import com.example.twinkle.fragment.FgserchuserFragment;

import java.util.ArrayList;


public class SearchMainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener{

    private View view_status;
    private TextView search_song_title;
    private TextView search_songlist_title;
    private TextView search_user_title;
    private ImageView search_song_line;
    private ImageView search_songlist_line;
    private ImageView search_user_line;
    private EditText search_editbox;
    private ViewPager vp_content;
    private String searchKey;
    private Toolbar toolbars;
    private Bundle bundle;
    private String currentUserID;
    private String connecturl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_main);
        currentUserID=getIntent().getStringExtra("currentUserID");
        connecturl=getIntent().getStringExtra("connecturl");
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(Color.parseColor("#008df2"));
        }
        initView();
        bundle = new Bundle();
    }

    void initView(){
        view_status=(View)findViewById(R.id.view_status);
        search_song_title=findViewById(R.id.search_song_title);
        search_songlist_title=findViewById(R.id.search_songlist_title);
        search_user_title=findViewById(R.id.search_user_title);
        vp_content=(ViewPager)findViewById(R.id.vp_content);
        toolbars=(Toolbar)findViewById(R.id.toolbars);
        search_song_line=findViewById(R.id.search_song_line);
        search_songlist_line=findViewById(R.id.search_songlist_line);
        search_user_line=findViewById(R.id.search_user_line);
        search_song_line.setVisibility(View.INVISIBLE);
        search_songlist_line.setVisibility(View.INVISIBLE);
        search_user_line.setVisibility(View.INVISIBLE);
        search_song_title.setOnClickListener(this);
        search_songlist_title.setOnClickListener(this);
        search_user_title.setOnClickListener(this);
        ImageView search_back=findViewById(R.id.back);
        search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
        search_editbox=findViewById(R.id.search_editbox);
        search_editbox.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        search_editbox.setSingleLine(false);
        search_editbox.setHorizontallyScrolling(false);
        search_editbox.setFocusable(true);
        search_editbox.setFocusableInTouchMode(true);
        search_editbox.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        search_editbox.setSelection(search_editbox.getText().length());
        TextView search_btn=findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!search_editbox.getText().toString().equals("")){
                    hideKeyboard();
                    if(!search_editbox.getText().toString().equals(searchKey)){
                        searchKey=search_editbox.getText().toString();
                        bundle.putString("searchkey", search_editbox.getText().toString());
                        bundle.putString("currentUserID", currentUserID);
                        bundle.putString("connecturl", connecturl);
                        initContentFragment();
                    }
                }
            }
        });
        TextView bottm_player_btn=findViewById(R.id.bottm_player_btn);
        bottm_player_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchMainActivity.this,PlayerActivity.class);
                intent.putExtra("currentUserID",currentUserID);
                intent.putExtra("connecturl",connecturl);
                intent.putExtra("option","");
                startActivity(intent);
                overridePendingTransition(R.anim.mid_bottom_slide_in, R.anim.fade_out);
            }
        });
    }

    private void initContentFragment(){
        ArrayList<Fragment> mFragmentList=new ArrayList<>();
        mFragmentList.add(new FgserchsongFragment());
        mFragmentList.add(new FgserchsonglistFragment());
        mFragmentList.add(new FgserchuserFragment());
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
        search_song_title.setSelected(false);
        search_songlist_title.setSelected(false);
        search_user_title.setSelected(false);
        switch (i){
            case 0:
                search_song_title.setSelected(true);
                search_song_line.setVisibility(View.VISIBLE);
                search_songlist_line.setVisibility(View.INVISIBLE);
                search_user_line.setVisibility(View.INVISIBLE);
                break;
            case 1:
                search_songlist_title.setSelected(true);
                search_song_line.setVisibility(View.INVISIBLE);
                search_songlist_line.setVisibility(View.VISIBLE);
                search_user_line.setVisibility(View.INVISIBLE);
                break;
            case 2:
                search_user_title.setSelected(true);
                search_song_line.setVisibility(View.INVISIBLE);
                search_songlist_line.setVisibility(View.INVISIBLE);
                search_user_line.setVisibility(View.VISIBLE);
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
            case R.id.search_song_title:
                if(vp_content.getCurrentItem()!=0){
                    setCurrentItem(0);
            }
            break;
            case R.id.search_songlist_title:
                if(vp_content.getCurrentItem()!=1){
                    setCurrentItem(1);
                }
                break;
            case R.id.search_user_title:
                if(vp_content.getCurrentItem()!=2){
                    setCurrentItem(2);
                }
                break;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
    //关闭软键盘
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}