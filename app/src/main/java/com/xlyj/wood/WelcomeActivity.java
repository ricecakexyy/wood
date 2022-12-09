package com.xlyj.wood;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xlyj.wood.home.HomeActivity;
import com.xlyj.wood.utils.DensityUtil;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/28
 * fun: 欢迎界面
 */
public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.view_page)
    ViewPager viewPage;
    @BindView(R.id.btn_skip)
    Button btnSkip;


    private List<ImageView> imageviewlist;
    private int previousEnablePosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initStatusBar();
        ButterKnife.bind(this);
        initView();
    }


    /**
     * 设置状态栏
     */
    private void initStatusBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
    }

    /**
     * 初始化
     */
    private void initView() {
        ImageView iv;
        View view;
        LinearLayout.LayoutParams params;
        imageviewlist = new ArrayList<ImageView>();
        int[] imageresid = {R.drawable.welcome, R.drawable.welcome, R.drawable.welcome,
                R.drawable.welcome, R.drawable.welcome};
        for (int id : imageresid) {
            iv = new ImageView(this);
            iv.setBackgroundResource(id);
            imageviewlist.add(iv);

            // 每循环一次，添加一个点到Linearlayout中
            view = new View(this);
            view.setBackgroundResource(R.drawable.welcome_point_bg);
            int width = DensityUtil.dp2px(this, 8);
            params = new LinearLayout.LayoutParams(width, width);
            params.leftMargin = width;
            view.setEnabled(false);
            view.setLayoutParams(params);
            llPointGroup.addView(view);// 向线性布局中添加点
        }
        viewPage.setAdapter(new WelcomeAdapter());
        viewPage.addOnPageChangeListener(new WelcomeOnPageChangeListener());
        viewPage.setCurrentItem(0); // 设置当前的默认索引
        llPointGroup.getChildAt(0).setEnabled(true);

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                WelcomeActivity.this.startActivity(intent);
                finish();
            }
        });
    }

    /**
     * ViewPage的适配器
     */
    class WelcomeAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageviewlist.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(imageviewlist.get(position % imageviewlist.size()));
            return imageviewlist.get(position % imageviewlist.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageviewlist.get(position
                    % imageviewlist.size()));
        }
    }

    public class WelcomeOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // 取余后的索引
            int newPosition = position;
            // 把上一个点设置为未选中
            llPointGroup.getChildAt(previousEnablePosition).setEnabled(false);
            // 根据索引设置被选中的点
            llPointGroup.getChildAt(newPosition).setEnabled(true);
            previousEnablePosition = newPosition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }
}
