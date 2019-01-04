package com.example.twinkle.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.News;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> implements View.OnClickListener {
    private List<News> NewsList;
    public InnerItemOnclickListener mListener;
    private int resourceId;

    public NewsAdapter(Context context, int textViewResourceId, List<News> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        NewsList=objects;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        News news =getItem(position);
        View view;
        ViewHolder viewHolder;
            if(!news.getIsShared()){
                if (convertView == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.shared_news_content = view.findViewById (R.id.shared_news_content);
                    viewHolder.user_cover = view.findViewById (R.id.user_cover);
                    viewHolder.content_cover =  view.findViewById (R.id.content_cover);
                    viewHolder.content_bg =  view.findViewById(R.id.content_bg);
                    viewHolder.news_operate =  view.findViewById (R.id.news_operate);
                    viewHolder.content_agree =  view.findViewById (R.id.content_agree);
                    viewHolder.content_comment =  view.findViewById (R.id.content_comment);
                    viewHolder.content_share =  view.findViewById (R.id.content_share);
                    viewHolder.user_name = view.findViewById (R.id.user_name);
                    viewHolder.news_content_title = view.findViewById (R.id.news_content_title);
                    viewHolder.news_date_title = view.findViewById (R.id.news_date_title);
                    viewHolder.news_content = view.findViewById (R.id.news_content);
                    viewHolder.content_name = view.findViewById (R.id.content_name);
                    viewHolder.content_creator = view.findViewById (R.id.content_creator);
                    viewHolder.content_skim_count = view.findViewById (R.id.content_skim_count);
                    viewHolder.content_agree_count = view.findViewById (R.id.content_agree_count);
                    viewHolder.content_comment_count = view.findViewById (R.id.content_comment_count);
                    viewHolder.content_share_count = view.findViewById (R.id.content_share_count);
                    view.setTag(viewHolder); // 将ViewHolder存储在View中
                } else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
                }
                String news_content_title_string ="分享"+news.getNewsType()+":";
                String content_name_string =news.getNewsType()+":"+news.getContentName();
                String content_creator_string ="歌手:"+news.getContentCreator();
                if(news.getNewsType().equals("歌单"));
                     content_creator_string="创建者："+news.getContentCreator();
                String content_skim_count_string ="浏览"+String.valueOf(news.getSkimCount())+"次";
                viewHolder.user_cover.setImageResource(news.getNewsCreatorCover());
                viewHolder.content_cover.setImageResource(news.getContentCover());
                viewHolder.news_content_title.setText(news_content_title_string);
                if(news.getIsAgree())
                    viewHolder.content_agree.setImageResource(R.drawable.agree_active);
                else
                    viewHolder.content_agree.setImageResource(R.drawable.agree);
                viewHolder.user_name.setText(news.getNewsCreatorName());
                viewHolder.news_content_title.setText(news_content_title_string);
                viewHolder.news_date_title.setText(news.getNewsDate());
                viewHolder.news_content.setText(news.getNewsContent());
                viewHolder.content_name.setText(content_name_string);
                viewHolder.content_creator.setText(content_creator_string);
                viewHolder.content_skim_count.setText(content_skim_count_string);
                viewHolder.content_agree_count.setText(String.valueOf(news.getAgreeCount()));
                viewHolder.content_comment_count.setText(String.valueOf(news.getCommentCount()));
                viewHolder.content_share_count.setText(String.valueOf(news.getForwardCount()));
                viewHolder.news_operate.setOnClickListener(this);
                viewHolder.news_operate.setTag(position);
                viewHolder.content_agree.setOnClickListener(this);
                viewHolder.content_agree.setTag(position);
                viewHolder.content_comment.setOnClickListener(this);
                viewHolder.content_comment.setTag(position);
                viewHolder.content_share.setOnClickListener(this);
                viewHolder.content_share.setTag(position);
                viewHolder.content_cover.setOnClickListener(this);
                viewHolder.content_cover.setTag(position);
                viewHolder.content_bg.setOnClickListener(this);
                viewHolder.content_bg.setTag(position);
                viewHolder.shared_news_content.setOnClickListener(this);
                viewHolder.shared_news_content.setTag(position);
                return view;
            }
            else {
                if (convertView == null) {
                    view = LayoutInflater.from(getContext()).inflate(R.layout.shared_news_item, parent, false);
                    viewHolder = new ViewHolder();
                    viewHolder.shared_news_content = view.findViewById (R.id.shared_news_content);
                    viewHolder.user_cover = view.findViewById(R.id.user_cover);
                    viewHolder.content_cover = view.findViewById(R.id.content_cover);
                    viewHolder.content_bg =  view.findViewById(R.id.content_bg);
                    viewHolder.news_operate = view.findViewById(R.id.news_operate);
                    viewHolder.content_agree = view.findViewById(R.id.content_agree);
                    viewHolder.content_comment = view.findViewById(R.id.content_comment);
                    viewHolder.content_share = view.findViewById(R.id.content_share);
                    viewHolder.user_name = view.findViewById(R.id.user_name);
                    viewHolder.news_content_title = view.findViewById(R.id.news_content_title);
                    viewHolder.news_date_title = view.findViewById(R.id.news_date_title);
                    viewHolder.news_content = view.findViewById(R.id.news_content);
                    viewHolder.content_name = view.findViewById(R.id.content_name);
                    viewHolder.content_creator = view.findViewById(R.id.content_creator);
                    viewHolder.content_skim_count = view.findViewById(R.id.content_skim_count);
                    viewHolder.content_agree_count = view.findViewById(R.id.content_agree_count);
                    viewHolder.content_comment_count = view.findViewById(R.id.content_comment_count);
                    viewHolder.content_share_count = view.findViewById(R.id.content_share_count);
                    view.setTag(viewHolder); // 将ViewHolder存储在View中
                } else {
                    view = convertView;
                    viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
                }
                String news_content_title_string = "分享" + news.getNewsType() + ":";
                String content_name_string = news.getNewsType() + ":" + news.getContentName();
                String content_creator_string = "歌手:" + news.getContentCreator();
                String content_skim_count_string = "浏览" + String.valueOf(news.getSkimCount()) + "次";
                viewHolder.user_cover.setImageResource(news.getNewsCreatorCover());
                viewHolder.content_cover.setImageResource(news.getContentCover());
                viewHolder.news_content_title.setText("转发:");
                String shared_news_content_str = "<font color='#008df2'>"+news.getOriginalCreator()+"</font>"+news_content_title_string+news.getOriginalComment();
                viewHolder.shared_news_content.setText(Html.fromHtml(shared_news_content_str));
                if (news.getIsAgree())
                    viewHolder.content_agree.setImageResource(R.drawable.agree_active);
                else
                    viewHolder.content_agree.setImageResource(R.drawable.agree);
                viewHolder.user_name.setText(news.getNewsCreatorName());
                viewHolder.news_date_title.setText(news.getNewsDate());
                viewHolder.news_content.setText(news.getNewsContent());
                viewHolder.content_name.setText(content_name_string);
                viewHolder.content_creator.setText(content_creator_string);
                viewHolder.content_skim_count.setText(content_skim_count_string);
                viewHolder.content_agree_count.setText(String.valueOf(news.getAgreeCount()));
                viewHolder.content_comment_count.setText(String.valueOf(news.getCommentCount()));
                viewHolder.content_share_count.setText(String.valueOf(news.getForwardCount()));
                viewHolder.news_operate.setOnClickListener(this);
                viewHolder.news_operate.setTag(position);
                viewHolder.content_agree.setOnClickListener(this);
                viewHolder.content_agree.setTag(position);
                viewHolder.content_comment.setOnClickListener(this);
                viewHolder.content_comment.setTag(position);
                viewHolder.content_share.setOnClickListener(this);
                viewHolder.content_share.setTag(position);
                viewHolder.content_cover.setOnClickListener(this);
                viewHolder.content_cover.setTag(position);
                viewHolder.content_bg.setOnClickListener(this);
                viewHolder.content_bg.setTag(position);
                viewHolder.shared_news_content.setOnClickListener(this);
                viewHolder.shared_news_content.setTag(position);
                return view;
            }
    }
    class ViewHolder {
        ImageView user_cover;
        ImageView content_cover;
        ImageView news_operate;
        ImageView content_agree;
        ImageView content_comment;
        ImageView content_share;
        TextView user_name;
        TextView news_content_title;
        TextView news_date_title;
        TextView news_content;
        TextView content_name;
        TextView content_creator;
        TextView content_skim_count;
        TextView content_agree_count;
        TextView content_comment_count;
        TextView content_share_count;
        TextView shared_news_content;
        ImageView content_bg;
    }
    public interface InnerItemOnclickListener {
        void itemClick(View v);
    }

    public void setOnInnerItemOnClickListener(InnerItemOnclickListener listener){
        this.mListener=listener;
    }

    @Override
    public void onClick(View v) {
        mListener.itemClick(v);
    }
}
