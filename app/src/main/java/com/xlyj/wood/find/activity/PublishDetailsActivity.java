package com.xlyj.wood.find.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Publish;
import com.xlyj.wood.find.fragment.BaseFragment;
import com.xlyj.wood.utils.StatusBarUtil;

public class PublishDetailsActivity extends BaseActivity {

    Publish publish;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_publish_details);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        publish = (Publish) getIntent().getSerializableExtra("publish");
    }
}
