package com.xlyj.slqj.SLQJ.Show3d;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.xlyj.slqj.R;

public class ViewPagerAdater extends PagerAdapter {
    private Context context;
    private int[] imgs;
    String filename;
    private String []names;
    private String []contents={   "0",   "0","  年年有鱼是“年年有余”的谐音可谓中国传统吉祥祈福最具代表的语言之一， 若用图画表示则可看作是传统吉祥符号。图中要有莲花或莲藕， 还要有鱼，即“莲连有鱼”。代表生活富足，每年都有多余的财富及食粮！",
            "  麒麟，是仁慈和祥的象征，瑞兽，是祥瑞之兽、吉祥神兽，主太平、长寿。在民间生活中常常会体现出它特有的珍贵和灵异。 常比喻为杰出之人，品德高尚、地位崇高，用麒麟的高洁祥瑞赞颂拥有者的高贵品质。只在太平盛世出现，集祥瑞寓意于一身，神性通灵显贵。"};

    public ViewPagerAdater(Context context,int[] imgs,String []names) {
        this.context = context;
        this.imgs=imgs;
        this.names=names;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(imgs[position]);
        //Glide.with(context).load(imgs[position]).into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                intent=new Intent(context, Show3dActivity.class);
                filename="3d"+position+".STL";
                intent.putExtra("filename",filename);
                intent.putExtra("title",names[position]);
                intent.putExtra("content",contents[position]);
                context.startActivity(intent);
            }
        });
        container.addView(imageView);
        return imageView;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }
}
