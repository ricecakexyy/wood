package com.xlyj.wood.home.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.DensityUtil;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/29
 * fun: 科普
 */
public class CultureActivity extends BaseActivity {

    @BindView(R.id.view_page)
    ViewPager viewPage;

    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;

    int[] imageresid = {R.drawable.h_culture_history, R.drawable.h_culture_theme, R.drawable.h_culture_tools,
            R.drawable.h_culture_material, R.drawable.h_culture_craftsman};
    String[] title = {"历史","题材","器具","用材","匠人"};
    private List<View> viewList;
    private int previousEnablePosition = 0;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_culture);
        ButterKnife.bind(this);
        initStatusBar();
        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        float itemHight = point.y / 3f;
        RelativeLayout.LayoutParams param;
        param = new RelativeLayout.LayoutParams(point.x, (int) itemHight);
        param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        llPointGroup.setLayoutParams(param);

        viewList = new ArrayList<>();
        for (int i = 0; i<5; i++) {
            View view = View.inflate(this,R.layout.h_culture,null);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText(title[i]);
            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/hanyiweibeifan.TTF");
            tvTitle.setTypeface(font);
            ImageView iv = view.findViewById(R.id.iv_picture);
            iv.setOnClickListener(new CultureOnClickListener());
            iv.setBackgroundResource(imageresid[i]);
            viewList.add(view);

            //加点
            view = new View(this);
            view.setBackgroundResource(R.drawable.h_point_bg);
            int width = DensityUtil.dp2px(this, 12);
            LinearLayout.LayoutParams params;
            params = new LinearLayout.LayoutParams(width, width);
            width = DensityUtil.dp2px(this, 20);
            params.leftMargin = width;
            view.setEnabled(false);
            view.setLayoutParams(params);
            llPointGroup.addView(view);
        }

        viewPage.setAdapter(new CultureAdapter());
        viewPage.addOnPageChangeListener(new CultureOnPageChangeListener());
        viewPage.setCurrentItem(0); // 设置当前的默认索引
        setPointView(llPointGroup.getChildAt(0),true);
        llPointGroup.getChildAt(0).setEnabled(true);
    }

    /**
     * ViewPage的适配器
     */
    class CultureAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(viewList.get(position % viewList.size()));
            return viewList.get(position % viewList.size());
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position % viewList.size()));
        }
    }

    public class CultureOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // 取余后的索引
            int newPosition = position;
            llPointGroup.getChildAt(previousEnablePosition).setEnabled(false);
            llPointGroup.getChildAt(newPosition).setEnabled(true);

            View view = llPointGroup.getChildAt(previousEnablePosition);
            setPointView(view,false);
            previousEnablePosition = newPosition;
            currentPosition = newPosition;

            view = llPointGroup.getChildAt(newPosition);
            setPointView(view,true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    /**
     * 设置点的样式
     * @param view
     * @param isSelected
     */
    void setPointView(View view,boolean isSelected){
        int width;
        if(!isSelected)
           width = DensityUtil.dp2px(CultureActivity.this, 12);
        else
            width= DensityUtil.dp2px(CultureActivity.this, 23);
        LinearLayout.LayoutParams params;
        params = new LinearLayout.LayoutParams(width, width);
        width = DensityUtil.dp2px(CultureActivity.this, 20);
        params.leftMargin = width;
        view.setLayoutParams(params);
    }


    /**
     * 设置状态栏
     */
    private void initStatusBar() {
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
    }

    private class CultureOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (currentPosition){
                case 0:
                    //历史
                    showHistory();
                    break;
                case 1:
                    //题材
                    showTheme();
                    break;
                case 2:
                    //器具
                    showTool();
                    break;
                case 3:
                    //用材
                    showMaterial();
                    break;
                case 4:
                    //匠人
                    showCraftsman();
                    break;
            }
        }
    }

    /**
     * 匠人
     */
    private void showCraftsman() {
        Intent intent = new Intent(this, CraftsmanActivity.class);
        startActivity(intent);
    }

    /**
     * 用材
     */
    private void showMaterial() {
        Intent intent = new Intent(this, MaterialActivity.class);
        startActivity(intent);
    }

    /**
     * 器具
     */
    private void showTool() {
        Intent intent = new Intent(this, ToolActivity.class);
        startActivity(intent);
    }

    /**
     * 题材
     */
    private void showTheme() {
        Intent intent = new Intent(this, ThemeActivity.class);
        startActivity(intent);
    }

    /**
     * 历史
     */
    private void showHistory() {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }
}
