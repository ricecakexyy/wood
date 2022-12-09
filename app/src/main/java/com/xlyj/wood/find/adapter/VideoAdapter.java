package com.xlyj.wood.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.utils.ConstantUtil;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun: 精选视频的adapter
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.myViewHolder> {

    private Context myContent;
    List<Video> videos;

    public VideoAdapter(Context myContent) {
        this.myContent = myContent;
    }

    public VideoAdapter(Context myContent, List<Video> videos) {
        this.myContent = myContent;
        this.videos = videos;
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
        View itemView = LayoutInflater.from(myContent).inflate(R.layout.f_recycler_video_item, parent, false);
        return new myViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Video video = videos.get(position % videos.size());


        Glide.with(myContent)
                .load(ConstantUtil.vedio_net+video.getCover_url())
                .into(holder.ivCover);


        holder.tvVideoTitle.setText(video.getTitle());
        holder.tvVideoDesc.setText(video.getBrief());
        holder.tvCommentCnt.setText(video.getComment_cnt().toString());
        holder.tvLikeCnt.setText(video.getLike_cnt().toString());
        if (onItemClick != null) {
            holder.cvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        CardView cvItem;
        ImageView ivCover; //封面
        TextView tvVideoTitle; //视频标题
        TextView tvVideoDesc; //视频简介
        TextView tvCommentCnt;//评论数
        TextView tvLikeCnt;//点赞数

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            cvItem = itemView.findViewById(R.id.cv_item);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvVideoTitle = itemView.findViewById(R.id.tv_video_title);
            tvVideoDesc = itemView.findViewById(R.id.tv_video_desc);
            tvCommentCnt = itemView.findViewById(R.id.tv_comment_cnt);
            tvLikeCnt = itemView.findViewById(R.id.tv_like_cnt);
        }
    }
}
