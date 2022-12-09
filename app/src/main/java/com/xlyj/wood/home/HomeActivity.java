package com.xlyj.wood.home;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.slqj.SLQJActivity;
import com.xlyj.wood.home.activity.CultureActivity;
import com.xlyj.wood.home.activity.MoreActivity;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/29
 * fun: 欢迎界面
 */
public class HomeActivity extends BaseActivity {


    @BindView(R.id.btn_culture)
    Button btnCulture;
    @BindView(R.id.btn_vr_ar)
    Button btnVrAr;
    @BindView(R.id.btn_more)
    Button btnMore;
    @BindView(R.id.tv_app)
    TextView tvApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_home);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        setOnClickListener();
    }

    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/home_tv.TTF");
        btnCulture.setTypeface(font);
        btnMore.setTypeface(font);
        btnVrAr.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "Fonts/app.TTF");
        tvApp.setTypeface(font);
    }

    /**
     * 点击监听事件
     */
    private void setOnClickListener() {
        HomeOnClickListener homeOnCickListener = new HomeOnClickListener();
        btnCulture.setOnClickListener(homeOnCickListener);
        btnVrAr.setOnClickListener(homeOnCickListener);
        btnMore.setOnClickListener(homeOnCickListener);
    }


    public class HomeOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.btn_culture:
                    //科普
                    intent.setClass(HomeActivity.this, CultureActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_vr_ar:
                    //身临其境
                    intent.setClass(HomeActivity.this, SLQJActivity.class);
                    startActivity(intent);
                    break;
                case R.id.btn_more:
                    //更多
                    intent.setClass(HomeActivity.this, MoreActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }


    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(HomeActivity.this, "再点一次，返回桌面", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }
    }
}
