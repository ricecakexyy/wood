package com.xlyj.wood.shop.adapter;

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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Commodity;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/4
 * fun:
 */
public class CommodityAdapter extends RecyclerView.Adapter<CommodityAdapter.ViewHolder> {

    private Context myContext;
    List<Commodity> myCommodities;

    public CommodityAdapter(Context context){
        myContext = context;
    }

    public CommodityAdapter(Context context, List<Commodity> myCommodities){
        myContext = context;
        this.myCommodities = myCommodities;
    }

//    int[] imageId = {R.drawable.goods1_01,R.drawable.s_commodity4,
//                    R.drawable.s_commodity5,R.drawable.s_commodity6,R.drawable.s_commodity7,R.drawable.s_commodity8,
//                    R.drawable.s_commodity9};

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(myContext).inflate(R.layout.s_commodity_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Commodity commodity = myCommodities.get(position);
        Glide.with(myContext)
                .load(commodity.getPictures()[0])
                .override(Target.SIZE_ORIGINAL)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPicture);
        holder.tvName.setText(commodity.getName());
        holder.tvPrice.setText("￥"+Double.toString(commodity.getPrice()));
        //holder.ivPicture.setImageResource(imageId[position % imageId.length]);
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
        return Math.min(myCommodities.size(),20);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPicture;
        private CardView cvItem;
        private TextView tvName;
        private TextView tvPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPicture = itemView.findViewById(R.id.iv_picture);
            cvItem = itemView.findViewById(R.id.cv_item);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}

