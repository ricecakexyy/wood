package com.xlyj.wood;

import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoView;

public class PictureDetailsActivity extends BaseActivity {
    ArrayList<String> urls;
    @BindView(R.id.photo_view)
    PhotoView photoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_details);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        getDate();
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        Glide.with(this)
                .load(urls.get(0))
                .into(photoView);
    }

    /**
     * 从intent中获得数据
     */
    private void getDate() {
        Bundle bundle = getIntent().getExtras();
        urls = bundle.getStringArrayList("urls");
    }
}
