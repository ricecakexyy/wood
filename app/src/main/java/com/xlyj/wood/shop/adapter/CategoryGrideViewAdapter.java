package com.xlyj.wood.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlyj.wood.R;
import com.xlyj.wood.ui.MyGridView;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/27
 * fun:
 */
public class CategoryGrideViewAdapter extends BaseAdapter {
    private Context mContext;
    int position;

//    String[] categories = {"展示架","工艺品","桌椅","门窗","屏风","壁挂","雕刻工具"};

    int[] picSid;
    String[] name;

    public CategoryGrideViewAdapter(Context mContext, int position) {
        this.mContext = mContext;
        if(position == 0) {
            //展示架
            picSid = new int[]{
                    R.drawable.shop_home_category1, R.drawable.shop_category1_p2,R.drawable.shop_category1_p3,
                    R.drawable.shop_category1_p4
//                    R.drawable.shop_home_category4,R.drawable.shop_home_category5,R.drawable.shop_home_category6,
            };
            name = new String[]{
                    "桌面小架","博古架","月洞门","茶架"
            };
        }else if(position == 1) {
            //工艺品
            picSid = new int[]{
                    R.drawable.shop_home_category2, R.drawable.shop_category2_p2, R.drawable.shop_category2_p3,
                    R.drawable.shop_category2_p4,R.drawable.shop_category2_p5,R.drawable.shop_category2_p6,
            };
            name = new String[]{
                    "五福临门", "花鸟摆件", "动物摆件", "人物摆件","字摆件","根雕"
            };
        }
        //TODO else if(position == 2){
            //桌椅
//
//        }else if(position ==3){
//            //门窗
//
//        }else if(position == 4){
//            //屏风
//
//        }else if(position == 5){
//            //壁挂
//
//        }else if(position == 6){
//            //雕刻工具
//        }

        else{
            picSid = new int[] {R.drawable.shop_home_category1,R.drawable.shop_home_category2,R.drawable.shop_home_category3,
                    R.drawable.shop_home_category4,R.drawable.shop_home_category5,R.drawable.shop_home_category6,
                    R.drawable.shop_home_category7};
            name = new String[]{"展示架","工艺品","桌椅","门窗","屏风","壁挂","雕刻工具"};
        }
    }

    @Override
    public int getCount() {
        return picSid.length;
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
            convertView = View.inflate(mContext, R.layout.s_category_gv_item,null);
            viewHolder.tvName = convertView.findViewById(R.id.tv_name);
            viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvName.setText(name[position]);
        viewHolder.ivPicture.setImageResource(picSid[position]);
        return convertView;
    }
    static class ViewHolder {
        private TextView tvName;
        private ImageView ivPicture;
    }
}
