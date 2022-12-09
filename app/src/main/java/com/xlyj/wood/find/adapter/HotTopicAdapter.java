package com.xlyj.wood.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xlyj.wood.R;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/1
 * fun:
 */
public class HotTopicAdapter extends RecyclerView.Adapter<HotTopicAdapter.MyViewHolder> {

    String[] picUrl = {
            "http://www.zgmdysw.com/KUpload/image/20150914/20150914191427_7059.jpg",
            "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3363050320,1450577953&fm=26&gp=0.jpg"
    };
    String[] topics = {
            "#跟着木雕去旅游#","#24节气#"
    };

    private Context myContext;

    public HotTopicAdapter(Context context){
        this.myContext = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myContext).inflate(R.layout.f_recycler_hot_topic_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvTopic.setText(topics[position]);
        Glide.with(myContext)
                .load(picUrl[position])
                .into(holder.ivPicture);
    }

    @Override
    public int getItemCount() {
        return topics.length;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPicture;
        TextView tvTopic;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            tvTopic = itemView.findViewById(R.id.tv_topic);
        }
    }
}
