package com.example.twinkle;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class NewsCommentAdapter extends ArrayAdapter<NewsComment> implements View.OnClickListener {
    private List<NewsComment> newsCommentList;
    private InnerItemOnclickListener mListener;
    private int resourceId;

    public NewsCommentAdapter(Context context, int textViewResourceId, List<NewsComment> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        newsCommentList=objects;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        NewsComment newsComment =getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.comment_user_cover = view.findViewById (R.id.comment_user_cover);
            viewHolder.comment_agree =  view.findViewById (R.id.comment_agree);
            viewHolder.comment_reply =  view.findViewById (R.id.comment_reply);
            viewHolder.comment_user_name = view.findViewById (R.id.comment_user_name);
            viewHolder.comment_text = view.findViewById (R.id.comment_text);
            viewHolder.comment_data_title = view.findViewById (R.id.comment_data_title);
            viewHolder.comment_agree_count = view.findViewById (R.id.comment_agree_count);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.comment_user_cover.setImageResource(newsComment.getUserCover());
        viewHolder.comment_user_name.setText(newsComment.getUserName());
        String comment_text_str="<font color='#000000'>"+newsComment.getCommentText()+"</font>";
        if(newsComment.getIsReply())
            comment_text_str+="<font color='#008df2'>@"+newsComment.getReplyUserName()+"：</font>" +newsComment.getReplyText();
        viewHolder.comment_text.setText(Html.fromHtml(comment_text_str));
        viewHolder.comment_data_title.setText(newsComment.getCommentTime());
        viewHolder.comment_agree_count.setText(String.valueOf(newsComment.getCommentAgreeCount()));
        if(newsComment.getCommentAgreeCount()==0)
            viewHolder.comment_agree_count.setVisibility(View.INVISIBLE);
        else
            viewHolder.comment_agree_count.setVisibility(View.VISIBLE);
        if(newsComment.getIsAgree())
            viewHolder.comment_agree.setImageResource(R.drawable.agree_active);
        else
            viewHolder.comment_agree.setImageResource(R.drawable.agree);
        viewHolder.comment_agree.setOnClickListener(this);
        viewHolder.comment_agree.setTag(position);
        viewHolder.comment_user_cover.setOnClickListener(this);
        viewHolder.comment_user_cover.setTag(position);
        viewHolder.comment_text.setOnClickListener(this);
        viewHolder.comment_text.setTag(position);
        viewHolder.comment_user_name.setOnClickListener(this);
        viewHolder.comment_user_name.setTag(position);
        viewHolder.comment_reply.setOnClickListener(this);
        viewHolder.comment_reply.setTag(position);
        return view;
    }
    class ViewHolder {
        ImageView comment_user_cover;
        ImageView comment_agree;
        ImageView comment_reply;
        TextView comment_user_name;
        TextView comment_text;
        TextView comment_data_title;
        TextView comment_agree_count;
    }
    interface InnerItemOnclickListener {
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
