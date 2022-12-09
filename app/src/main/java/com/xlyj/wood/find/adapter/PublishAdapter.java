package com.xlyj.wood.find.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Publish;
import com.xlyj.wood.utils.GetTime;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun:
 */
public class PublishAdapter extends RecyclerView.Adapter<PublishAdapter.myViewHolder> {

    private Context myContent;
    List<Publish> publishes;

    public PublishAdapter(Context myContent, List<Publish> publishes){
        this.myContent = myContent;
        this.publishes = publishes;
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
    public PublishAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myContent).inflate(R.layout.f_recycler_publish_item, parent, false);
        return new myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder holder, int position) {
        Publish publish =  publishes.get(position%publishes.size());
        holder.tvContent.setText(publish.getContent());
        holder.tvPublishTime.setText(GetTime.friendly_time(publish.getDate().getDate()));

        holder.tvName.setText(publish.getUser().getNickname());
        //图片
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        if(publish.getPictures()!=null && publish.getPictures().length>0) {
            String[] pictures = publish.getPictures();
            for (String picture : pictures){
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(picture);
                info.setBigImageUrl(picture);
                imageInfo.add(info);
            }
        }

        holder.tvLikeCnt.setText(publish.getLike_cnt().toString());
        holder.tvCommentCnt.setText(publish.getComment_cnt().toString());
        holder.tvForwardCnt.setText(publish.getForward_cnt().toString());

        holder.nineGridView.setAdapter(new NineGridViewClickAdapter(myContent, imageInfo));
        Glide.with(myContent)
                .load(publish.getUser().getAvatar())
                .into(holder.ivHead);

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
        return 10;
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        NineGridView nineGridView;
        TextView tvPublishTime, tvLikeCnt, tvCommentCnt, tvForwardCnt;
        TextView tvName;
        RoundedImageView ivHead;
        LinearLayout llItem;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            nineGridView = itemView.findViewById(R.id.f_publish_nine_grid);
            tvPublishTime = itemView.findViewById(R.id.tv_publish_time);
            tvName = itemView.findViewById(R.id.tv_name);
            ivHead = itemView.findViewById(R.id.iv_head);
            llItem = itemView.findViewById(R.id.ll_item);
            tvLikeCnt = itemView.findViewById(R.id.tv_like_cnt);
            tvCommentCnt = itemView.findViewById(R.id.tv_comment_cnt);
            tvForwardCnt = itemView.findViewById(R.id.tv_forward_cnt);
        }
    }
}
