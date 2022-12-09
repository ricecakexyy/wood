package com.xlyj.wood.shop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.R;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/27
 * fun:
 */
public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.MyViewHolder> {
    Context mContext;

    public CategoryRecyclerViewAdapter(Context context) {
        this.mContext = context;
    }

    String[] categories = {
            "展示架","工艺品","桌椅","门窗","屏风","壁挂","雕刻工具"
    };

    private OnItemClick onItemClick;   //定义点击事件接口

    public void setOnItemClickListener(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.s_category_rv_item, null);
        return new MyViewHolder(view);
    }

   MyViewHolder preHolder = null;
    int selectedPostion = 0;

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if(position == selectedPostion){
            if(preHolder!=null) {
                preHolder.llItem.setBackgroundColor(Color.TRANSPARENT);
            }
            holder.llItem.setBackgroundColor(Color.WHITE);
            preHolder = holder;
        }
        holder.tvCategory.setText(categories[position]);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preHolder!=null) {
                    preHolder.llItem.setBackgroundColor(Color.TRANSPARENT);
                }
                holder.llItem.setBackgroundColor(Color.WHITE);
                preHolder = holder;
                if(onItemClick != null){
                    onItemClick.onItemClick(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return categories.length;
    }

    public void setSelectedPostion(int selectedPostion) {
        this.selectedPostion = selectedPostion;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tvCategory;
        LinearLayout llItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_category);
            llItem = itemView.findViewById(R.id.ll_item);
        }
    }
}
