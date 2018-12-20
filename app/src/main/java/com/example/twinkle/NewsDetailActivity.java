package com.example.twinkle;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.twinkle.NewsCommentAdapter.InnerItemOnclickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsDetailActivity extends AppCompatActivity implements InnerItemOnclickListener, OnItemClickListener{
    private List<String> agreeList=new ArrayList<String>();
    private List<String> shareList=new ArrayList<String>();
    private String[] str_arr = new String[]{};
    private List<NewsComment> commentList=new ArrayList<NewsComment>();
    private int replyposition=-1;
    private View popupView;
    private View commentpopupView;
    private News news;
    private String IsShared;
    private String IsComment;
    private String position_str;
    private ImageView content_agree_img;
    private ImageView content_share_img;
    private TextView content_agree_text;
    private TextView content_share_text;
    private ListView comment_listview;
    private EditText editcomment;
    private NewsCommentAdapter commentAdapter;
    private ImageView content_agree;
    private PopupWindow popupWindow;
    private PopupWindow commentpopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_detail_layout);
        news =(News)getIntent().getSerializableExtra("News");
        position_str=getIntent().getStringExtra("Position");
        IsShared=getIntent().getStringExtra("IsShared");
        IsComment=getIntent().getStringExtra("IsComment");
        final View main_view = findViewById(R.id.main_div);
        content_agree =  findViewById (R.id.content_agree);
        content_agree_img= findViewById (R.id.content_agree_img);
        content_share_img= findViewById (R.id.content_share_img);
        content_agree_text = findViewById (R.id.content_agree_text);
        content_share_text =findViewById (R.id.content_share_text);
        ImageView back = findViewById (R.id.back);
        ImageView news_detail_opreate = findViewById (R.id.news_detail_opreate);
        ImageView user_cover = findViewById (R.id.user_cover);
        ImageView content_cover =  findViewById (R.id.content_cover);
        ImageView content_comment =  findViewById (R.id.content_comment);
        ImageView content_share = findViewById (R.id.content_share);
        ImageView content_bg = findViewById (R.id.content_bg);
        TextView user_name = findViewById (R.id.user_name);
        TextView news_content_title = findViewById (R.id.news_content_title);
        TextView news_date_title = findViewById (R.id.news_date_title);
        TextView news_content = findViewById (R.id.news_content);
        TextView content_name = findViewById (R.id.content_name);
        TextView content_creator = findViewById (R.id.content_creator);
        TextView content_skim_count = findViewById (R.id.content_skim_count);
        TextView comment_send_btn=findViewById(R.id.comment_send);
        //初始化数据
        String news_content_title_string ="分享"+news.getNewsType()+":";
        String content_name_string =news.getNewsType()+":"+news.getContentName();
        String content_creator_string ="歌手:"+news.getContentCreator();
        String content_skim_count_string ="浏览"+String.valueOf(news.getSkimCount())+"次";
        user_cover.setImageResource(news.getNewsCreatorCover());
        content_cover.setImageResource(news.getContentCover());
        if(news.getIsAgree())
            content_agree.setImageResource(R.drawable.agree_active);
        else
            content_agree.setImageResource(R.drawable.agree);
        user_name.setText(news.getNewsCreatorName());
        if(news.getIsShared())
            news_content_title.setText("转发:");
        else
            news_content_title.setText(news_content_title_string);
        news_date_title.setText(news.getNewsDate());
        news_content.setText(news.getNewsContent());
        content_name.setText(content_name_string);
        content_creator.setText(content_creator_string);
        content_skim_count.setText(content_skim_count_string);
        //初始化弹窗
        initPopwindow();
        //初始化点赞和转发文本
        initAgree();
        initShare();
        showAgree();
        showShare();
        //初始化评论
        initComent();
        comment_listview=findViewById(R.id.comment_list_view);
        commentAdapter = new NewsCommentAdapter(this, R.layout.comment_item, commentList);
        commentAdapter.setOnInnerItemOnClickListener(this);
        comment_listview.setAdapter(commentAdapter);
        comment_listview.setOnItemClickListener(this);
        //初始化评论输入框
        editcomment=findViewById(R.id.edit_comment);
        editcomment.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editcomment.setSingleLine(false);
        editcomment.setHorizontallyScrolling(false);
        editcomment.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_NONE);
        editcomment.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if((replyposition==-1)&&editcomment.getText().toString().equals("")){
                        InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        editcomment.requestFocus();
                        inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                        inputManager.showSoftInput(editcomment, 0);
                        editcomment.setText(str_arr[0]);
                        editcomment.setSelection(editcomment.getText().length());
                    }
                } else {
                    // 此处为失去焦点时的处理内容
                }
            }
        });
        editcomment.addTextChangedListener(new TextWatcher() {
            String tmp;
            int cursor;
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                int line = editcomment.getLineCount();
                if (line > 3) {
                    if (before > 0 && start == 0) {
                        if (s.toString().equals(tmp)) {
                            // setText触发递归TextWatcher
                            cursor--;
                        } else {
                            // 手动移动光标为0
                            cursor = count - 1;
                        }
                    } else {
                        cursor = start + count - 1;
                    }
                }
            }
            @Override public void afterTextChanged(Editable s) {
                // 限制可输入行数
                int line = editcomment.getLineCount();
                if (line > 3){
                    String str = s.toString();
                    tmp = str.substring(0, cursor) + str.substring(cursor + 1);
                    editcomment.setText(tmp);
                    editcomment.setSelection(cursor);
                }
            }
        });
        //评论发送按钮点击事件
        comment_send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_view.setFocusable(true);
                main_view.setFocusableInTouchMode(true);
                main_view.requestFocus();
                str_arr[replyposition+1]=editcomment.getText().toString();
                addNewComment(replyposition);
                str_arr[replyposition+1]="";
                replyposition=-1;
                InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editcomment.getWindowToken(), 0);
                editcomment.setHint("说点什么吧...");
                editcomment.setText("");
            }
        });
        //是否立刻弹出键盘
        if(IsComment.equals("true")){
            boolean handler =new Handler().postDelayed(new Runnable() {
                @Override public void run() {
                    editcomment.requestFocus();
                }
            },300); // 延时0.3秒
        }
        //返回按钮点击事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_activity("back");
            }
        });
        //动态管理按钮点击事件
        news_detail_opreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopWindow();
                setBackgroundAlpha(0.7f);
            }
        });
        //分享内容点击事件
        content_cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsDetailActivity.this,"已转发到动态",Toast.LENGTH_SHORT).show();
            }
        });
        content_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NewsDetailActivity.this,"已转发到动态",Toast.LENGTH_SHORT).show();
            }
        });
        //动态点赞按钮点击事件
        content_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_agree_pressed();
            }
        });
        //动态评论按钮点击事件
        content_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editcomment.requestFocus();
            }
        });
        //动态转发按钮点击事件
        content_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewsDetailActivity.this,ShareNewsActivity.class);
                intent.putExtra("SharedNews",(Serializable) (news));
                startActivity(intent);
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });
        //主布局点击事件
        main_view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                main_view.setFocusable(true);
                main_view.setFocusableInTouchMode(true);
                main_view.requestFocus();
                str_arr[replyposition+1]=editcomment.getText().toString();
                Log.d("liangyue",str_arr[0]);
                replyposition=-1;
                InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(editcomment.getWindowToken(), 0);
                editcomment.setHint("说点什么吧...");
                editcomment.setText("");
                return false;
            }
        });
    }

    private void initAgree(){
        String agree_user_name1="flowrain";
        agreeList.add(agree_user_name1);
        String agree_user_name2="Laser、VNE";
        agreeList.add(agree_user_name2);
        String agree_user_name3="梁月";
        agreeList.add(agree_user_name3);
        String agree_user_name4="苏格拉";
        agreeList.add(agree_user_name4);
    }

    private void initShare(){
        String share_user_name1="flowrain";
        shareList.add(share_user_name1);
        String share_user_name2="Laser、VNE";
        shareList.add(share_user_name2);
        String share_user_name3="梁月";
        shareList.add(share_user_name3);
        String share_user_name4="苏格拉";
        shareList.add(share_user_name4);
    }

    private void showAgree(){
        if(agreeList.size()==0){
            content_agree_img.setVisibility(View.GONE);
            content_agree_text.setVisibility(View.GONE);
        }
        else {
            content_agree_img.setVisibility(View.VISIBLE);
            content_agree_text.setVisibility(View.VISIBLE);
            String agree_str = "<font color='#000000'>觉得很赞</font>";
            String content_agree_str=agreeList.get(0);
            if(agreeList.size()>1){
                for(int i=1;i<agreeList.size();i++){
                    content_agree_str+="、";
                    content_agree_str+=agreeList.get(i);
                }
            }
            content_agree_str+=agree_str;
            content_agree_text.setText(Html.fromHtml(content_agree_str));
        }
    }

    private void showShare(){
        if(shareList.size()==0){
            content_share_img.setVisibility(View.GONE);
            content_share_text.setVisibility(View.GONE);
        }
        else {
            content_share_img.setVisibility(View.VISIBLE);
            content_share_text.setVisibility(View.VISIBLE);
            String share_str = "<font color='#000000'>转发</font>";
            String content_share_str=shareList.get(0);
            if(shareList.size()>1){
                for(int i=1;i<shareList.size();i++){
                    content_share_str+="、";
                    content_share_str+=shareList.get(i);
                }
            }
            content_share_str+=share_str;
            content_share_text.setText(Html.fromHtml(content_share_str));
        }
    }

    private void addNewComment(int position){
        NewsComment newComment=new NewsComment();
        newComment.setCommentAgreeCount(0);
        newComment.setCommentText(str_arr[replyposition+1]);
        newComment.setCommentTime("8月9日19:57");
        newComment.setUserCover(R.drawable.user_image);
        newComment.setUserName("梁月");
        newComment.setIsAgree(false);
        if(position==-1)
            newComment.setIsReply(false);
        else{
            newComment.setIsReply(true);
            newComment.setReplyUserName(commentList.get(position).getUserName());
            newComment.setReplyText(commentList.get(position).getCommentText());
        }
        commentList.add(newComment);
        str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
        str_arr[str_arr.length-1] = "";
        commentAdapter.notifyDataSetChanged();
    }

    private void initComent(){
        NewsComment newsComment1=new NewsComment();
        newsComment1.setCommentAgreeCount(0);
        newsComment1.setCommentText("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        newsComment1.setCommentTime("8月9日19:57");
        newsComment1.setUserCover(R.drawable.user_image);
        newsComment1.setUserName("flowrain");
        newsComment1.setIsAgree(false);
        newsComment1.setIsReply(false);
        commentList.add(newsComment1);
        NewsComment newsComment2=new NewsComment();
        newsComment2.setCommentAgreeCount(10);
        newsComment2.setCommentText("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        newsComment2.setCommentTime("8月9日19:57");
        newsComment2.setUserCover(R.drawable.user_image);
        newsComment2.setUserName("梁月");
        newsComment2.setIsAgree(true);
        newsComment2.setIsReply(false);
        commentList.add(newsComment2);
        NewsComment newsComment3=new NewsComment();
        newsComment3.setCommentAgreeCount(0);
        newsComment3.setCommentText("嘤嘤嘤");
        newsComment3.setCommentTime("8月9日19:57");
        newsComment3.setUserCover(R.drawable.user_image);
        newsComment3.setUserName("flowrain");
        newsComment3.setIsAgree(false);
        newsComment3.setIsReply(true);
        newsComment3.setReplyUserName("梁月");
        newsComment3.setReplyText("假日无缘回家，只能在学校窝着，宝宝心里苦,嘤嘤嘤");
        commentList.add(newsComment3);
        for(int i=0;i<=commentList.size();i++){
            str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
            str_arr[str_arr.length-1] = "";
        }
    }

    private void initPopwindow() {
        popupView = LayoutInflater.from(this).inflate(R.layout.manage_news_detail_layout, null, false);
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
        TextView popup_out_left=popupView.findViewById(R.id.popup_out_left);
        popup_out_left.setOnClickListener(new View.OnClickListener() {
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
        //评论管理弹窗
        commentpopupView = LayoutInflater.from(this).inflate(R.layout.manage_news_comment_layout, null, false);
        commentpopupWindow = new PopupWindow(commentpopupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        commentpopupWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        commentpopupWindow.setFocusable(true);
        commentpopupWindow.setTouchable(true);
        commentpopupWindow.setOutsideTouchable(true);
        commentpopupWindow.setAnimationStyle(R.style.mangagenewsTranslate);
        commentpopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
        TextView commentpopup_out_above=commentpopupView.findViewById(R.id.popup_out_above);
        commentpopup_out_above.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentpopupWindow.dismiss();
            }
        });
        TextView commentpopup_out_below=commentpopupView.findViewById(R.id.popup_out_below);
        commentpopup_out_below.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentpopupWindow.dismiss();
            }
        });
    }
    //显示评论管理弹窗
    private void showcommentPopWindow(final int position) {
        View rootview = LayoutInflater.from(this).inflate(R.layout.news_detail_layout, null);
        TextView delete_title = commentpopupView.findViewById(R.id.delete_title);
        TextView add_same_title = commentpopupView.findViewById(R.id.add_same_title);
        final TextView copy_btn = commentpopupView.findViewById(R.id.copy_btn);
        TextView delete_btn = commentpopupView.findViewById(R.id.delete_btn);
        TextView add_same_btn = commentpopupView.findViewById(R.id.add_same_btn);
        ImageView others_comment_line = commentpopupView.findViewById(R.id.others_comment_line);
        ImageView my_comment_line = commentpopupView.findViewById(R.id.my_comment_line);
        copy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(commentList.get(position).getCommentText());
                Toast.makeText(NewsDetailActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
                commentpopupWindow.dismiss();
            }
        });
        if(commentList.get(position).getUserName().equals("梁月")){
            final int current=position;
            add_same_title.setVisibility(View.GONE);
            add_same_btn.setVisibility(View.GONE);
            others_comment_line.setVisibility(View.GONE);
            delete_title.setVisibility(View.VISIBLE);
            delete_btn.setVisibility(View.VISIBLE);
            delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentList.remove(current);
                    for(int i=current+1;i<str_arr.length-1;i++){
                        str_arr[i]=str_arr[i+1];
                    }
                    str_arr = Arrays.copyOf(str_arr, str_arr.length-1);
                    commentAdapter.notifyDataSetChanged();
                    commentpopupWindow.dismiss();
                }
            });
            my_comment_line.setVisibility(View.VISIBLE);
        }else if(IsShared.equals(false)){
            delete_title.setVisibility(View.GONE);
            delete_btn.setVisibility(View.GONE);
            add_same_title.setVisibility(View.VISIBLE);
            add_same_btn.setVisibility(View.VISIBLE);
            my_comment_line.setVisibility(View.GONE);
            others_comment_line.setVisibility(View.VISIBLE);
            add_same_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsComment newsComment=new NewsComment();
                    newsComment=commentList.get(position);
                    commentList.add(newsComment);
                    str_arr = Arrays.copyOf(str_arr, str_arr.length+1);
                    str_arr[str_arr.length-1] = "";
                    commentAdapter.notifyDataSetChanged();
                    commentpopupWindow.dismiss();
                }
            });
        }
        commentpopupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }
    //显示动态管理弹窗
    private void showPopWindow() {
        View rootview = LayoutInflater.from(this).inflate(R.layout.news_detail_layout, null);
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
        if(news.getNewsType().equals("单曲")) {
            next_play_title.setVisibility(View.VISIBLE);
            next_play_btn.setVisibility(View.VISIBLE);
            next_play_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NewsDetailActivity.this,"已加入播放列表",Toast.LENGTH_SHORT).show();
                    popupWindow.dismiss();
                }
            });
        }
        else
            next_play_title.setVisibility(View.GONE);
        if(news.getNewsCreatorName().equals("梁月")){
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
                    popupWindow.dismiss();
                    finish_activity("delete");
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
            others_news_line.setVisibility(View.VISIBLE);
            ignore_news_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    finish_activity("ignore");
                }
            });
            unfollow_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    finish_activity("unfollow");
                }
            });
        }
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onBackPressed() {
        finish_activity("back");
    }

    private void finish_activity(String option){
        Intent intent = new Intent();
        intent.putExtra("Option", option);
        intent.putExtra("Position", position_str);
        intent.putExtra("AgreeCount", String.valueOf(news.getAgreeCount()));
        setResult(RESULT_OK, intent);
        finish();
        overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }

    private void content_agree_pressed(){
        int temp =news.getAgreeCount();
        if(news.getIsAgree()) {
            news.setIsAgree(false);
            content_agree.setImageResource(R.drawable.agree);
            temp--;
            news.setAgreeCount(temp);
            for(int i=0;i<agreeList.size();i++){
                if(agreeList.get(i).equals("梁月"))
                    agreeList.remove(i);
            }
            showAgree();
        }
        else {
            news.setIsAgree(true);
            content_agree.setImageResource(R.drawable.agree_active);
            Animation mAnimation = AnimationUtils.loadAnimation(this,R.anim. agree_btn_anim);
            content_agree.setAnimation(mAnimation);
            mAnimation.start();
            temp++;
            news.setAgreeCount(temp);
            agreeList.add("梁月");
            showAgree();
        }
    }

    private void comment_agree_pressed(int position){
        ImageView temp_comment_agree= comment_listview.getChildAt(position).findViewById(R.id.comment_agree);
        TextView temp_comment_agree_count= comment_listview.getChildAt(position).findViewById(R.id.comment_agree_count);
        int temp =commentList.get(position).getCommentAgreeCount();
        if(commentList.get(position).getIsAgree()) {
            commentList.get(position).setIsAgree(false);
            temp_comment_agree.setImageResource(R.drawable.agree);
            temp--;
            commentList.get(position).setCommentAgreeCount(temp);
            temp_comment_agree_count.setText(String.valueOf(temp));
            if(temp==0)
                temp_comment_agree_count.setVisibility(View.INVISIBLE);
            else
                temp_comment_agree_count.setVisibility(View.VISIBLE);
        }
        else {
            commentList.get(position).setIsAgree(true);
            temp_comment_agree.setImageResource(R.drawable.agree_active);
            Animation mAnimation = AnimationUtils.loadAnimation(this,R.anim. agree_btn_anim);
            temp_comment_agree.setAnimation(mAnimation);
            mAnimation.start();
            temp++;
            commentList.get(position).setCommentAgreeCount(temp);
            temp_comment_agree_count.setText(String.valueOf(temp));
            temp_comment_agree_count.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void itemClick(View v) {
        int position;
        position = (Integer) v.getTag();
        switch (v.getId()) {
            case R.id.comment_agree:
                comment_agree_pressed(position);
                break;
            case R.id.comment_text:
                showcommentPopWindow(position);
                setBackgroundAlpha(0.7f);
                break;
            case R.id.comment_reply:
                replyposition=position;
                InputMethodManager inputManager = (InputMethodManager)editcomment.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                editcomment.requestFocus();
                inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
                inputManager.showSoftInput(editcomment, 0);
                String hint_str="回复"+commentList.get(position).getUserName()+":";
                editcomment.setHint(hint_str);
                editcomment.setText(str_arr[replyposition+1]);
                editcomment.setSelection(editcomment.getText().length());
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }

    //调整屏幕亮度
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }
}
