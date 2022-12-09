package com.xlyj.wood.find.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/22
 * 活动申请者adapter
 */
public class ActivitySignUpAdapter extends RecyclerView.Adapter<ActivitySignUpAdapter.ViewHolder>  {

    List<User> users;

    public ActivitySignUpAdapter(List<User> users){
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.f_activity_sign_up_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = users.get(position);
        Glide.with(holder.view)
                .load(user.getAvatar())
                .into(holder.ivHead);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;
        private RoundedImageView ivHead;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            ivHead = itemView.findViewById(R.id.iv_head);
        }
    }
}
