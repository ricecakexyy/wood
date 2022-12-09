package com.xlyj.wood.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Activity;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/4
 * fun:
 */
public class ActivityMyAdapter extends RecyclerView.Adapter<ActivityMyAdapter.myViewHolder>  {
    private Context myContext;
    List<Activity> activities;

    public ActivityMyAdapter(Context myContext){
        this.myContext = myContext;
    }

    public ActivityMyAdapter(Context myContext, List<Activity> activities){
        this.myContext = myContext;
        this.activities = activities;
    }

    private OnItemClick onItemClick;   //定义点击事件接口

    //定义设置点击事件监听的方法
    public void setOnItemClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    //定义一个点击事件的接口
    public interface OnItemClick {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(myContext).inflate(R.layout.f_activity_my_item, parent, false);
        return new myViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Activity activity = activities.get(Math.min(position % 2,activities.size()));
        holder.tvName.setText(activity.getName());
        holder.tvDetails.setText(activity.getDetails());
        Glide.with(myContext)
                .load(activity.getCover())
                .into(holder.ivCover);

        if (onItemClick != null) {
            holder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivCover;
        private TextView tvName;
        private TextView tvDetails;
        private TextView tvDate;
        private TextView tvNumber;
        private LinearLayout llItem;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvDetails = itemView.findViewById(R.id.tv_details);
            tvNumber = itemView.findViewById(R.id.tv_number);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
