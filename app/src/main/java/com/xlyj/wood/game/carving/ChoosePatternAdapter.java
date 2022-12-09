package com.xlyj.wood.game.carving;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xlyj.wood.R;
import com.xlyj.wood.shop.adapter.CategoryGrideViewAdapter;

/**
 * @author cute xyy biu~biu~
 * created on 2020/9/19
 * fun:雕刻游戏中选择图片的gridview的adapter
 */
public class ChoosePatternAdapter extends BaseAdapter {
    Context myContext;
    int[] picSid;

    public ChoosePatternAdapter(Context myContext, int[] picSid){
        this.myContext = myContext;
        this.picSid = picSid;
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
            convertView = View.inflate(myContext, R.layout.game_carving_pattern_item,null);
            viewHolder.ivPicture = convertView.findViewById(R.id.iv_picture);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivPicture.setImageResource(picSid[position]);
        if (onItemClick != null) {
            viewHolder.ivPicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick.onItemClick(position);
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        private ImageView ivPicture;
    }
}
