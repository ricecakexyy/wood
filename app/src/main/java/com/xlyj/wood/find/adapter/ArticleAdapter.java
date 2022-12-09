package com.xlyj.wood.find.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Article;
import com.xlyj.wood.find.activity.ArticleDetailsActivity;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/1
 * fun:
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {

    private Context myContext;
    private List<Article> articles;

    public ArticleAdapter(Context context, List<Article> articles){
        this.myContext = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(myContext).inflate(R.layout.f_recycler_article_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.tvDes.setText(article.getDes());
        holder.tvTitle.setText(article.getTitle());
        Glide.with(myContext)
                .load(article.getCover())
                .into(holder.ivCover);
        holder.tvLikeCnt.setText(article.getLike_cnt().toString());
        holder.tvCommentCnt.setText(article.getComment_cnt().toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(myContext, ArticleDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("article", articles.get(position));
                intent.putExtras(bundle);
                myContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        private View itemView;
        private TextView tvTitle;
        private TextView tvDes, tvLikeCnt, tvCommentCnt;
        private ImageView ivCover;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDes = itemView.findViewById(R.id.tv_des);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvLikeCnt = itemView.findViewById(R.id.tv_like_cnt);
            tvCommentCnt = itemView.findViewById(R.id.tv_comment_cnt);
        }
    }

}
