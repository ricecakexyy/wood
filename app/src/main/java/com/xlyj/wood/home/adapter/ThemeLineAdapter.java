package com.xlyj.wood.home.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.ThemeCategoryBean;

import java.util.List;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/19
 * fun: 主题大类 adapter
 */
public class ThemeLineAdapter extends BaseAdapter {

    /**
     * 上下文环境
     **/
    private Context mContext;

    /**
     * 实体类列表 建议最好用实体类，当然也可以用Map之类的来动态添加或生成， 我自己在这里建了一个实体类，配套后面的holder来用
     **/
    private List<ThemeCategoryBean> infoList;

    /**
     * 布局渲染器
     **/
    private LayoutInflater inflater;

    public ThemeLineAdapter() {
        super();
    }

    /**
     * 构造函数
     *
     * @param mContext 上下文环境
     * @param infoList 实体类列表
     */
    public ThemeLineAdapter(Context mContext, List<ThemeCategoryBean> infoList) {
        super();
        this.mContext = mContext;
        this.infoList = infoList;
    }


    @Override
    public int getCount() {
        return infoList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 如果convertView为空，则加载界面布局文件
        inflater = LayoutInflater.from(parent.getContext());
        // 如果为空，就重新加载布局文件
        if (null == convertView) {
            /*
             * 由于我这里有两种不同的布局，而且是交叉呈现，所以做如下处理，将position对2求余，
             * 如果为0就加载左边布局，为1就加载右边布局
             */
            if (position % 2 == 0) {
                // 加载左边布局
                convertView = inflater.inflate(R.layout.h_theme_left_list_item, null);
                // 创建一个VIewHolder用以缓存
                holder = new ViewHolder();
                holder.mInfoCategory = convertView
                        .findViewById(R.id.left_tv_time);
                Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Fonts/STKAITI.TTF");
                holder.mInfoCategory.setTypeface(font);
                holder.mInfoIcon = convertView
                        .findViewById(R.id.left_item_img);
                // 这里多了一个变量
                holder.isLeftOrRight = true;

                // 设置holder标签
                convertView.setTag(holder);
            } else {
                // 加载右边布局
                convertView = inflater.inflate(R.layout.h_theme_right_list_item, null);
                // 创建一个VIewHolder用以缓存
                holder = new ViewHolder();
                holder.mInfoCategory = convertView
                        .findViewById(R.id.right_tv_time);
                Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Fonts/STKAITI.TTF");
                holder.mInfoCategory.setTypeface(font);
                holder.mInfoIcon = convertView
                        .findViewById(R.id.right_item_img);
                // 这里多了一个变量
                holder.isLeftOrRight = false;

                // 设置holder标签
                convertView.setTag(holder);
            }

        } else {// 如果不为空，就直接从缓存中去取
            holder = (ViewHolder) convertView.getTag();//本来直接这样取是没有问题的，但是会发现有重叠的bug

            // 所以接下来的处理是为了解决会出现的bug，如果有同学不明白的话，可以进下面的注释掉，然后运行一遍，就会发现问题了
            // 如何来解释这个呢？如果一屏幕有三个item，那么holder就将缓存三个item，到第四个的时候，
            // 就会将holder中原来缓存的第一个去覆盖第四个，这样就会出现，本来应该是右边出现的布局跑到了左边，还跟下面出现的布局重合的现象
            // 建议注释掉之后看效果，这样有点讲不明白
            if (0 == position % 2 && !holder.isLeftOrRight) {
                // 加载左边布局
                convertView = inflater.inflate(R.layout.h_theme_left_list_item, null);
                // 创建一个VIewHolder用以缓存
                holder = new ViewHolder();
                holder.mInfoCategory = (TextView) convertView
                        .findViewById(R.id.left_tv_time);
                Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Fonts/STKAITI.TTF");
                holder.mInfoCategory.setTypeface(font);
                holder.mInfoIcon = convertView
                        .findViewById(R.id.left_item_img);
                // 这里多了一个变量
                holder.isLeftOrRight = true;

                // 设置holder标签
                convertView.setTag(holder);
            } else if (1 == position % 2 && holder.isLeftOrRight) {
                // 加载右边布局
                convertView = inflater.inflate(R.layout.h_theme_right_list_item, null);
                // 创建一个VIewHolder用以缓存
                holder = new ViewHolder();
                holder.mInfoCategory = convertView
                        .findViewById(R.id.right_tv_time);
                Typeface font = Typeface.createFromAsset(mContext.getAssets(), "Fonts/STKAITI.TTF");
                holder.mInfoCategory.setTypeface(font);
                holder.mInfoIcon = convertView
                        .findViewById(R.id.right_item_img);
                // 这里多了一个变量
                holder.isLeftOrRight = false;

                // 设置holder标签
                convertView.setTag(holder);
            } else {

            }
        }
        ThemeCategoryBean infoBean;
        infoBean = infoList.get(position);
        holder.mInfoCategory.setText(infoBean.getCategory());
        holder.mInfoIcon.setImageResource(infoBean.getPic_src());

        return convertView;
    }

    /**
     * 按照流行的写法，用holder来做缓存
     **/
    public class ViewHolder {
        /**
         * 分类
         **/
        TextView mInfoCategory;

        /**
         * 图片
         **/
        RoundedImageView mInfoIcon;
        /**
         * 左布局还是由布局，为true表示加载左布局,为false表示加载右边布局
         **/
        boolean isLeftOrRight;

        public ViewHolder() {
            super();
        }
    }

}
