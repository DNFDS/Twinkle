package com.example.twinkle;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twinkle.NewsAdapter.InnerItemOnclickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FgFriendsFragment extends Fragment implements InnerItemOnclickListener,OnItemClickListener {

    private List<News> NewsList = new ArrayList<News>();
    private List<User> FriendsList = new ArrayList<User>();
    private View myfriendView;
    private ListView news_list_view;
    private NewsAdapter newsAdapter;
    private View popupView;
    private PopupWindow popupWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        myfriendView = inflater.inflate(R.layout.fg_friends, container, false);
        initFriendsList();
        RecyclerView friend_recyclerView = (RecyclerView) myfriendView.findViewById(R.id.follow_list_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this.getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        friend_recyclerView.setLayoutManager(layoutManager);
        friendsAdapter adapter = new friendsAdapter(FriendsList);
        friend_recyclerView.setAdapter(adapter);
        initNewsList();
        news_list_view = myfriendView.findViewById(R.id.news_list_view);
        newsAdapter = new NewsAdapter(this.getActivity(), R.layout.shared_news_item, NewsList);
        newsAdapter.setOnInnerItemOnClickListener(this);
        news_list_view.setAdapter(newsAdapter);
        news_list_view.setOnItemClickListener(this);
        return myfriendView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化弹窗
        initPopwindow();
    }

    private void initFriendsList() {
        User friend1=new User();
        friend1.setUserImage(R.drawable.user_image);
        friend1.setUserName("苏格拉");
        FriendsList.add(friend1);
        User friend2=new User();
        friend2.setUserImage(R.drawable.user_image);
        friend2.setUserName("flowrain");
        FriendsList.add(friend2);
        User friend3=new User();
        friend3.setUserImage(R.drawable.user_image);
        friend3.setUserName("Laser、VNE");
        FriendsList.add(friend3);
        User friend4=new User();
        friend4.setUserImage(R.drawable.user_image);
        friend4.setUserName("苏格拉");
        FriendsList.add(friend4);
        User friend5=new User();
        friend5.setUserImage(R.drawable.user_image);
        friend5.setUserName("flowrain");
        FriendsList.add(friend5);
        User friend6=new User();
        friend6.setUserImage(R.drawable.user_image);
        friend6.setUserName("Laser、VNE");
        FriendsList.add(friend6);
        User friend7=new User();
        friend7.setUserImage(R.drawable.user_image);
        friend7.setUserName("苏格拉");
        FriendsList.add(friend7);
        User friend8=new User();
        friend8.setUserImage(R.drawable.user_image);
        friend8.setUserName("flowrain");
        FriendsList.add(friend8);
        User friend9=new User();
        friend9.setUserImage(R.drawable.user_image);
        friend9.setUserName("Laser、VNE");
        FriendsList.add(friend9);
    }

    private void initNewsList() {
        News news1 = new News();
        news1.setNewsId(5646);
        news1.setNewsType("单曲");
        news1.setCommentCount(70);
        news1.setForwardCount(80);
        news1.setAgreeCount(90);
        news1.setSkimCount(147);
        news1.setNewsCreatorCover(R.drawable.user_image);
        news1.setContentCover(R.drawable.cover);
        news1.setNewsContent("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        news1.setNewsCreatorName("梁月");
        news1.setNewsDate("2016年10月1日");
        news1.setContentName("青春的问答");
        news1.setContentCreator("许巍洲");
        news1.setIsAgree(false);
        news1.setIsShared(false);
        NewsList.add(news1);

        News news2 = new News();
        news2.setNewsId(1234);
        news2.setNewsType("单曲");
        news2.setCommentCount(70);
        news2.setForwardCount(80);
        news2.setAgreeCount(90);
        news2.setSkimCount(147);
        news2.setNewsCreatorCover(R.drawable.user_image);
        news2.setContentCover(R.drawable.cover);
        news2.setNewsContent("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤嘤嘤嘤嘤嘤嘤");
        news2.setNewsCreatorName("苏格拉");
        news2.setNewsDate("2016年10月1日");
        news2.setContentName("青春的问答");
        news2.setContentCreator("许巍洲");
        news2.setIsAgree(true);
        news2.setIsShared(true);
        news2.setOriginalNewsId(5646);
        news2.setOriginalCreator("梁月");
        news2.setOriginalComment("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        NewsList.add(news2);

        News news3 = new News();
        news3.setNewsId(4321);
        news3.setNewsType("歌单");
        news3.setCommentCount(70);
        news3.setForwardCount(80);
        news3.setAgreeCount(90);
        news3.setSkimCount(147);
        news3.setNewsCreatorCover(R.drawable.user_image);
        news3.setContentCover(R.drawable.cover);
        news3.setNewsContent("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤嘤嘤嘤嘤嘤嘤");
        news3.setNewsCreatorName("苏格拉");
        news3.setNewsDate("2016年10月1日");
        news3.setContentName("青春的问答");
        news3.setContentCreator("许巍洲");
        news3.setIsAgree(true);
        news3.setIsShared(false);
        NewsList.add(news3);
    }

    private void initPopwindow() {
        popupView = LayoutInflater.from(this.getActivity()).inflate(R.layout.manage_news_layout, null, false);
        popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.mangagenewsTranslate);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView popup_out_above=popupView.findViewById(R.id.popup_out_above);
        popup_out_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        TextView popup_out_below=popupView.findViewById(R.id.popup_out_below);
        popup_out_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    //显示底部弹窗
    private void showPopWindow(final int position) {
        View rootview = LayoutInflater.from(this.getActivity()).inflate(R.layout.activity_main, null);
        TextView next_play_title = popupView.findViewById(R.id.next_play_title);
        TextView delete_title = popupView.findViewById(R.id.delete_title);
        TextView ignore_news_title = popupView.findViewById(R.id.ignore_news_title);
        TextView unfollow_title = popupView.findViewById(R.id.unfollow_title);
        TextView next_play_btn = popupView.findViewById(R.id.next_play_btn);
        TextView delete_btn = popupView.findViewById(R.id.delete_btn);
        TextView ignore_news_btn = popupView.findViewById(R.id.ignore_news_btn);
        TextView unfollow_btn = popupView.findViewById(R.id.unfollow_btn);
        ImageView others_news_line = popupView.findViewById(R.id.others_news_line);
        ImageView my_news_line = popupView.findViewById(R.id.my_news_line);
        if(NewsList.get(position).getNewsType()=="单曲") {
            next_play_title.setVisibility(View.VISIBLE);
            next_play_btn.setVisibility(View.VISIBLE);
            next_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"已加入播放列表",Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
        }
        else{
            next_play_title.setVisibility(View.GONE);
            next_play_btn.setVisibility(View.GONE);
        }
        if(NewsList.get(position).getNewsCreatorName()=="梁月"){
            final int current=position;
            ignore_news_title.setVisibility(View.GONE);
            ignore_news_btn.setVisibility(View.GONE);
            unfollow_title.setVisibility(View.GONE);
            unfollow_btn.setVisibility(View.GONE);
            others_news_line.setVisibility(View.GONE);
            delete_title.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsList.remove(current);
                    newsAdapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                }
            });
            my_news_line.setVisibility(View.VISIBLE);
        }else{
            delete_title.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
            ignore_news_title.setVisibility(View.VISIBLE);
            ignore_news_btn.setVisibility(View.VISIBLE);
            unfollow_title.setVisibility(View.VISIBLE);
            unfollow_btn.setVisibility(View.VISIBLE);
            my_news_line.setVisibility(View.GONE);
            others_news_line.setVisibility(View.VISIBLE);
            ignore_news_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            unfollow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.news_operate:
                showPopWindow(position);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.content_agree:
                content_agree_pressed(position);
                break;
            case R.id.content_comment:
                Intent comment_intent = new Intent(getContext(), NewsDetailActivity.class);
                comment_intent.putExtra("News", NewsList.get(position));
                comment_intent.putExtra("IsShared", "false");
                comment_intent.putExtra("IsComment", "true");
                comment_intent.putExtra("NewsId", NewsList.get(position).getNewsId()+"");
                comment_intent.putExtra("Position", position+"");
                this.startActivityForResult(comment_intent, 1);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                break;
            case R.id.content_share:
                content_share_pressed(position);
                break;
            case R.id.content_cover:
                Toast.makeText(this.getActivity(),"已转发到动态",Toast.LENGTH_SHORT).show();
                break;
            case R.id.content_bg:
                Toast.makeText(this.getActivity(),"已转发到动态",Toast.LENGTH_SHORT).show();
                break;
            case R.id.shared_news_content:
                int current=-1;
                for(int i=0;i<NewsList.size();i++){
                    if(NewsList.get(i).getNewsId()==NewsList.get(position).getOriginalNewsId())
                        current=i;
                }
                Intent intent = new Intent(getContext(), NewsDetailActivity.class);
                intent.putExtra("News", NewsList.get(current));
                intent.putExtra("IsShared", "true");
                intent.putExtra("IsComment", "false");
                intent.putExtra("NewsId", NewsList.get(current).getNewsId()+"");
                intent.putExtra("Position", current+"");
                this.startActivityForResult(intent, 1);
                getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                break;
            default:
                break;
        }
    }

    private void content_agree_pressed(int position){
        ImageView temp_agree_btn = news_list_view.getChildAt(position).findViewById(R.id.content_agree);
        TextView temp_agree_count = news_list_view.getChildAt(position).findViewById(R.id.content_agree_count);
        int temp =NewsList.get(position).getAgreeCount();
        if(NewsList.get(position).getIsAgree()) {
            NewsList.get(position).setIsAgree(false);
            temp_agree_btn.setImageResource(R.drawable.agree);
            temp--;
            NewsList.get(position).setAgreeCount(temp);
            temp_agree_count.setText(String.valueOf(temp));
        }
        else {
            NewsList.get(position).setIsAgree(true);
            temp_agree_btn.setImageResource(R.drawable.agree_active);
            Animation mAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim. agree_btn_anim);
            temp_agree_btn.setAnimation(mAnimation);
            mAnimation.start();
            temp++;
            NewsList.get(position).setAgreeCount(temp);
            temp_agree_count.setText(String.valueOf(temp));
        }
    }

    private void content_share_pressed(int position){
        Intent intent = new Intent(getActivity(),ShareNewsActivity.class);
        intent.putExtra("SharedNews",(Serializable) (NewsList.get(position)));
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), NewsDetailActivity.class);
        intent.putExtra("News", NewsList.get(position));
        intent.putExtra("IsShared", "false");
        intent.putExtra("IsComment", "false");
        intent.putExtra("NewsId", NewsList.get(position).getNewsId()+"");
        intent.putExtra("Position", position+"");
        this.startActivityForResult(intent, 1);
        getActivity().overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getActivity().getWindow().getAttributes();
        this.getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getActivity().getWindow().setAttributes(lp);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String option=data.getStringExtra("Option");
        String position_str=data.getStringExtra("Position");
        String agree_count_str=data.getStringExtra("AgreeCount");
        int position=Integer.parseInt(position_str);
        int agree_count=Integer.parseInt(agree_count_str);
        switch (option) {
            case "delete":
                NewsList.remove(position);
                newsAdapter.notifyDataSetChanged();
                break;
            case "ignore":
                for(int i=0;i<NewsList.size();i++){
                    if(NewsList.get(i).getNewsCreatorName().equals(NewsList.get(position).getNewsCreatorName()))
                        NewsList.remove(i);
                }
                newsAdapter.notifyDataSetChanged();
                break;
            case "unfollow":
                for(int i=0;i<NewsList.size();i++){
                    if(NewsList.get(i).getNewsCreatorName().equals(NewsList.get(position).getNewsCreatorName()))
                        NewsList.remove(i);
                }
                newsAdapter.notifyDataSetChanged();
                break;
            default:
                if(position!=-1&&NewsList.get(position).getAgreeCount()!=agree_count){
                    content_agree_pressed(position);
                }
        }
    }

}
