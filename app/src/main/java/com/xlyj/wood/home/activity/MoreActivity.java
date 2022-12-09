package com.xlyj.wood.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.daily_recommend.DailyRecommendActivity;
import com.xlyj.wood.find.FindActivity;
import com.xlyj.wood.game.main_page.AllGameActivity;
import com.xlyj.wood.me.MeActivity;
import com.xlyj.wood.shop.ShopActivity;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/31
 * fun: 点击更多展现界面
 */
public class MoreActivity extends BaseActivity {

    @BindView(R.id.ll_knowledge)
    LinearLayout llKnowledge;
    @BindView(R.id.ll_game)
    LinearLayout llGame;
    @BindView(R.id.ll_find)
    LinearLayout llFind;
    @BindView(R.id.ll_market)
    LinearLayout llMarket;
    @BindView(R.id.ll_me)
    LinearLayout llMe;
    @BindView(R.id.ll_setting)
    LinearLayout llSetting;
    @BindView(R.id.ll_h_more)
    LinearLayout llHMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_more);
        ButterKnife.bind(this);
        initStatusBar();
        showView();
        setOnListener();
    }

    private void setOnListener() {
        llKnowledge.setOnClickListener(new MoreClickListener());
        llGame.setOnClickListener(new MoreClickListener());
        llFind.setOnClickListener(new MoreClickListener());
        llMarket.setOnClickListener(new MoreClickListener());
        llMe.setOnClickListener(new MoreClickListener());
    }


    private class MoreClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.ll_knowledge:
                    //每日推荐
                    intent.setClass(MoreActivity.this, DailyRecommendActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_game:
                    //娱乐
                    intent.setClass(MoreActivity.this, AllGameActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_find:
                    //发现
                    intent.setClass(MoreActivity.this, FindActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_market:
                    //集市
                    intent.setClass(MoreActivity.this, ShopActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_me:
                    //我的
                    intent.setClass(MoreActivity.this, MeActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * 展现视图
     */
    private void showView() {
        final LinearLayout rootView = findViewById(R.id.ll_h_more);
        for (int i = 0; i < rootView.getChildCount(); i++) {
            final View view = rootView.getChildAt(i);
            view.setVisibility(View.INVISIBLE);
            //延迟显示每个子试图(主要动画就体现在这里)
            Observable.timer(i * 200, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            view.setVisibility(View.VISIBLE);
                            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
                            alphaAnimation.setDuration(1500);
                            view.startAnimation(alphaAnimation);
                        }
                    });
        }
    }

    private void initStatusBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        getWindow().setNavigationBarColor(getResources().getColor(R.color.h_more_bg));
    }
}
