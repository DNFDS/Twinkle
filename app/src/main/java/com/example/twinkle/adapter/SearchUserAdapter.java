package com.example.twinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.twinkle.R;
import com.example.twinkle.enity.User;

import java.util.List;

public class SearchUserAdapter extends ArrayAdapter<User> implements View.OnClickListener {
    private List<User> userList;
    public InnerItemOnclickListener mListener;
    private int resourceId;

    public SearchUserAdapter(Context context, int textViewResourceId, List<User> objects){
        super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
        userList=objects;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        User user =getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.user_cover = view.findViewById (R.id.user_cover);
            viewHolder.follow_operate =  view.findViewById (R.id.follow_operate);
            viewHolder.user_name = view.findViewById (R.id.user_name);
            viewHolder.follow_count = view.findViewById (R.id.follow_count);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.user_cover.setImageResource(user.getUserImage());
        if(user.getIsFollow())
            viewHolder.follow_operate.setImageResource(R.drawable.unfollow);
        else
            viewHolder.follow_operate.setImageResource(R.drawable.follow);
        viewHolder.user_name.setText(user.getUserName());
        String songlist_count_creator_str="粉丝："+user.getUserFollowCount();
        viewHolder.follow_count.setText(songlist_count_creator_str);
        viewHolder.follow_operate.setOnClickListener(this);
        viewHolder.follow_operate.setTag(position);
        return view;
    }
    class ViewHolder {
        ImageView user_cover;
        ImageView follow_operate;
        TextView user_name;
        TextView follow_count;
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
