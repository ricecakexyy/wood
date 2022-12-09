package com.xlyj.wood.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlyj.wood.R;
import com.youth.banner.adapter.IViewHolder;

import cn.bmob.v3.util.V;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/3
 * fun:
 */
public class CategoryAdapter extends BaseAdapter {

    private Context mContext;
    int[] imageId = {R.drawable.shop_home_category1,R.drawable.shop_home_category2,R.drawable.shop_home_category3,
            R.drawable.shop_home_category4,R.drawable.shop_home_category5,R.drawable.shop_home_category6,
        R.drawable.shop_home_category7};
    String[] categories = {"展示架","工艺品","桌椅","门窗","屏风","壁挂","雕刻工具"};

    public CategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();
        if(convertView == null){
//            convertView = LayoutInflater.from(mContext).inflate(R.layout.s_home_category_item,null);
            convertView = View.inflate(mContext, R.layout.s_home_category_item,null);
            viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
            viewHolder.tvCategory = convertView.findViewById(R.id.tv_category);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivPicture.setImageResource(imageId[position]);
        viewHolder.tvCategory.setText(categories[position]);
        return convertView;
    }


    static class ViewHolder {
        private ImageView ivPicture;
        private TextView tvCategory;
    }
}
