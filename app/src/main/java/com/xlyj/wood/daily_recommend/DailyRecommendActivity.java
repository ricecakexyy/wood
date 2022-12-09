package com.xlyj.wood.daily_recommend;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DailyRecommendActivity extends BaseActivity {

    @BindView(R.id.view_page)
    ViewPager viewPage;

    @BindView(R.id.iv_share)
    ImageView ivShare;
    int[] imageresid = {R.drawable.daily_recommend_picture1};
    String[] des = {"半是神形半鬼形", "一棚傀儡木雕成"};

    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_activity_daily_recommend);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        initViewPager();
        ivShare.setOnClickListener(new OnClick());
    }

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_share:
                    Intent intent = new Intent(DailyRecommendActivity.this, CalendarActivity.class);
                    startActivity(intent);
            }
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            if(i == 0) {
                View view = View.inflate(this, R.layout.daily_recommend_vp_item, null);
                Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");

                TextView tvD1 = view.findViewById(R.id.tv_d1);
                tvD1.setTypeface(font);
                tvD1.setText(des[(i % imageresid.length) * 2]);
                tvD1.setLetterSpacing(0.6f);

                TextView tvD2 = view.findViewById(R.id.tv_d2);
                tvD2.setTypeface(font);
                tvD2.setText(des[(i % imageresid.length) * 2 + 1]);
                tvD2.setLetterSpacing(0.6f);

                RoundedImageView iv = view.findViewById(R.id.iv_picture);
                iv.setImageResource(imageresid[i % imageresid.length]);
                viewList.add(view);
            }else if(i == 1){
                View view = View.inflate(this,R.layout.daily_recommend_layout2,null);

                viewList.add(view);
            }
        }
        viewPage.setAdapter(new DailyAdapter());
        viewPage.setCurrentItem(viewList.size()); // 设置当前的默认索引
    }

    /**
     * ViewPage的适配器
     */
    class DailyAdapter extends PagerAdapter {

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
}
