package com.xlyj.wood.home.activity;

import android.os.Bundle;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/31
 * fun: 历史的详情
 */
public class HistoryActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_history);
        initStatusBar();

    }

    /**
     * 设置状态栏
     */
    private void initStatusBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
    }
}
