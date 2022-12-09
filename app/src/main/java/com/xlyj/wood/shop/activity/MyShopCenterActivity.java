package com.xlyj.wood.shop.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

public class MyShopCenterActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_my_shop_center);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

    }
}
