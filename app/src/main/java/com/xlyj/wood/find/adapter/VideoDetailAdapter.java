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
import com.bumptech.glide.request.target.Target;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.utils.ConstantUtil;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/7
 * fun:
 */
public class VideoDetailAdapter  extends RecyclerView.Adapter<VideoDetailAdapter.myViewHolder> {

    private Context myContent;
    List<Video> videos;

    public VideoDetailAdapter(Context myContent, List<Video> videos) {
        this.myContent = myContent;
        this.videos = videos;
    }

    private VideoAdapter.OnItemClick onItemClick;   //定义点击事件接口

    //定义设置点击事件监听的方法
    public void setOnItemClickListener(VideoAdapter.OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    //定义一个点击事件的接口
    public interface OnItemClick {
        void onItemClick(int position);
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myContent).inflate(R.layout.f_video_deatil_recommend_item, parent, false);
        return new myViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Video video = videos.get(position%videos.size());
        Glide.with(myContent)
                .load(ConstantUtil.vedio_net+video.getCover_url())
                .override(Target.SIZE_ORIGINAL)
                .into(holder.ivCover);
        holder.tvAuthor.setText(video.getAuthor().getNickname());
        holder.tvTitle.setText(video.getTitle());
        int num = video.getLike_cnt() + video.getComment_cnt();
        holder.tv_watch_cnt.setText(""+num);
        holder.tv_comment_cnt.setText(video.getComment_cnt().toString());
    }


    @Override
    public int getItemCount() {
        return 10;
    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvAuthor,tv_watch_cnt,tv_comment_cnt;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tv_watch_cnt = itemView.findViewById(R.id.tv_watch_cnt);
            tv_comment_cnt = itemView.findViewById(R.id.tv_comment_cnt);
        }
    }
}
