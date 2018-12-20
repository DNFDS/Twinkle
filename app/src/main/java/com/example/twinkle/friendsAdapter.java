package com.example.twinkle;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class friendsAdapter extends RecyclerView.Adapter<friendsAdapter.ViewHolder> {
    private List<User> friendList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View friendView;
        ImageView friend_cover;
        TextView friend_name;

        public ViewHolder(View view) {
            super(view);
            friendView = view;
            friend_cover = (ImageView) view.findViewById(R.id.friend_cover);
            friend_name = (TextView) view.findViewById(R.id.friend_name);
        }
    }

    public friendsAdapter(List<User> friendlist) {
        friendList = friendlist;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.friendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                User current_user = friendList.get(position);
                Toast.makeText(v.getContext(), "you clicked view " + current_user.getUserName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User current_user = friendList.get(position);
        holder.friend_cover.setImageResource(current_user.getUserImage());
        holder.friend_name.setText(current_user.getUserName());
    }

    @Override
    public int getItemCount() {
        return friendList.size();
    }
}
