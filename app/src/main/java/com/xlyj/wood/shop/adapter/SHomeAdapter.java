package com.xlyj.wood.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.Activity;
import com.xlyj.wood.domain.Commodity;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.find.activity.ActivityDetailsActivity;
import com.xlyj.wood.shop.activity.CommodityDetailsActivity;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.RotateYTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/25
 * fun:
 */
public class SHomeAdapter extends RecyclerView.Adapter {

    private final int BANNER = 0;
    private final int CATEGORY = 1;
    private final int RECOMMEND = 2;
    int cnt = 3; //总共多少块
    int currentType;

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Commodity> commodities;

    public SHomeAdapter(Context context,List<Commodity> commodities) {
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        this.commodities = commodities;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == BANNER) {
            return new BannerViewHolder(mContext, mLayoutInflater.inflate(R.layout.s_home_banner, null));
        }else if (viewType == CATEGORY) {
            return new CategoryViewHolder(mContext, mLayoutInflater.inflate(R.layout.s_home_category, null));
        }else if (viewType == RECOMMEND) {
            return new RecommendViewHolder(mContext, mLayoutInflater.inflate(R.layout.s_home_recommend, null));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == BANNER) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.setDate();
        }else if(getItemViewType(position) == CATEGORY){
            CategoryViewHolder categoryViewHolder = (CategoryViewHolder) holder;
            categoryViewHolder.setDate();
        }else if(getItemViewType(position) == RECOMMEND){
            RecommendViewHolder recommendViewHolder = (RecommendViewHolder) holder;
            recommendViewHolder.initRecycler(commodities);
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    /**
     * 得到type
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case BANNER:
                currentType = BANNER;
                break;
            case CATEGORY:
                currentType = CATEGORY;
                break;
            case RECOMMEND:
                currentType = RECOMMEND;
                break;
        }
        return currentType;
    }

    /**
     * Banner的viewholder
     */
    class BannerViewHolder extends RecyclerView.ViewHolder {

        private Context mContext;
        private Banner banner;

        public BannerViewHolder(Context mContext, @NonNull View itemView) {
            super(itemView);
            this.mContext = mContext;
            banner = itemView.findViewById(R.id.banner);
        }

        public void setDate() {
            List<Integer> images = new ArrayList<>();
            images.add(R.drawable.banner1);
            images.add(R.drawable.banner2);
            images.add(R.drawable.banner3);
            ImageAdapter adapter = new ImageAdapter(images);
            banner.setAdapter(adapter)//设置适配器
                    .setCurrentItem(0, false)
//                    .addBannerLifecycleObserver(this)//添加生命周期观察者
                    .addPageTransformer(new RotateYTransformer())//添加切换效果
//                .setBannerGalleryMZ(banner.getWidth(),0)
                    .setBannerGalleryEffect(banner.getWidth(), 0, 0)
                    .setIndicator(new CircleIndicator(mContext));//设置指示器
            banner.start();
        }

        /**
         * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
         */
        public class ImageAdapter extends BannerAdapter<Integer, ImageAdapter.InnerBanner> {

            public ImageAdapter(List<Integer> mDatas) {
                //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
                super(mDatas);
            }

            //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
            @Override
            public InnerBanner onCreateHolder(ViewGroup parent, int viewType) {
                ImageView imageView = new ImageView(parent.getContext());
                //注意，必须设置为match_parent，这个是viewpager2强制要求的
                imageView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                return new InnerBanner(imageView);
            }

            @Override
            public void onBindView(InnerBanner holder, Integer data, final int position, int size) {
                //holder.imageView.setImageResource(data.imageRes);
                holder.imageView.setImageResource(data);
//            Glide.with(holder.imageView.getContext()).load((String)data.imageUrl).into(holder.imageView);
                holder.itemView.setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //点击事件
                                Toast.makeText(v.getContext(), "点击了" + position, Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }

            class InnerBanner extends RecyclerView.ViewHolder {
                ImageView imageView;

                public InnerBanner(@NonNull ImageView view) {
                    super(view);
                    this.imageView = view;
                }
            }
        }

    }


    /**
     * category的viewholder
     */
    private class CategoryViewHolder extends RecyclerView.ViewHolder {
        private GridView gridView;
        CategoryAdapter adapter;
        public CategoryViewHolder(Context mcontext, @NonNull View itemView) {
            super(itemView);
            gridView = itemView.findViewById(R.id.grid_view);
        }

        public void setDate() {
            adapter = new CategoryAdapter(mContext);
            gridView.setAdapter(adapter);

            //点击
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(mContext,""+position,Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * recommend的viewholder
     */
    private class RecommendViewHolder extends RecyclerView.ViewHolder{
        private Context mContext;
        CommodityAdapter adapter;
        RecyclerView recyclerViewCommodity;
        public RecommendViewHolder(Context context,@NonNull View itemView) {
            super(itemView);
            this.mContext = context;
            recyclerViewCommodity = itemView.findViewById(R.id.recycler_view);
        }

        public void initRecycler(List<Commodity> commodities) {
            adapter = new CommodityAdapter(mContext,commodities);
            recyclerViewCommodity.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerViewCommodity.setAdapter(adapter);

            //点击跳转至详情界面
            adapter.setOnItemClickListener(new CommodityAdapter.OnItemClick() {
                @Override
                public void onItemClick(int position) {
                    Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("commodity", commodities.get(position));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }


    }
}
