package com.xlyj.wood;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lzy.ninegrid.NineGridView;

import cn.bmob.v3.Bmob;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun:
 */
public  class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        NineGridView.setImageLoader(new GlideImageLoader());
        Bmob.initialize(this, "6be28a17fc91bed9de9e4d7e83cf071b");
    }

    /** Glide 加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.ic_default_image)//
                    .error(R.drawable.ic_default_image)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
