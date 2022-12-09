package com.xlyj.wood.home.adapter;

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
import com.xlyj.wood.domain.Pictures;
import com.xlyj.wood.find.adapter.ArticleAdapter;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/13
 * fun: 其他作品
 */
public class OtherWorksAdapter extends RecyclerView.Adapter<OtherWorksAdapter.ViewHolder> {

    Context myContext;
    List<Pictures> works;

    public OtherWorksAdapter(Context myContext, List<Pictures> works){
        this.myContext = myContext;
        this.works = works;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myContext).inflate(R.layout.h_recycler_other_work, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(myContext)
                .load(works.get(position).getUrl())
                .into(holder.ivPicture);
        holder.tvName.setText(works.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return works.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPicture;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}
