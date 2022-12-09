package com.xlyj.wood.home.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

public class CraftsmanWorksActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_craftsman_works);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);


    }
}
