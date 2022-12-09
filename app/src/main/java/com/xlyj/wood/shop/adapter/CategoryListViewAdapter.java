package com.xlyj.wood.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.xlyj.wood.R;
import com.xlyj.wood.ui.MyGridView;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/27
 * fun:
 */
public class CategoryListViewAdapter extends BaseAdapter {

    private Context mContext;
    int[] imageId = {R.drawable.shop_home_category1,R.drawable.shop_home_category2,R.drawable.shop_home_category3,
            R.drawable.shop_home_category4,R.drawable.shop_home_category5,R.drawable.shop_home_category6,
            R.drawable.shop_home_category7};
    String[] categories = {"展示架","工艺品","桌椅","门窗","屏风","壁挂","雕刻工具"};

    public CategoryListViewAdapter(Context mContext) {
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
            convertView = View.inflate(mContext, R.layout.s_category_lv_item,null);
            viewHolder.tvCategory = convertView.findViewById(R.id.tv_category);
            viewHolder.gridView = convertView.findViewById(R.id.grid_view);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvCategory.setText(categories[position]);
        setGridView(viewHolder, position);
        return convertView;
    }

    /**
     * 设置grid view
     * @param viewHolder
     */
    private void setGridView(ViewHolder viewHolder, int position) {
        CategoryGrideViewAdapter adapter = new CategoryGrideViewAdapter(mContext, position);
        viewHolder.gridView.setAdapter(adapter);

    }

    static class ViewHolder {
        private TextView tvCategory;
        private MyGridView gridView;
    }
}
