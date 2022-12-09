package com.xlyj.wood.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

public class PetsShopActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pets_shop);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
    }
}