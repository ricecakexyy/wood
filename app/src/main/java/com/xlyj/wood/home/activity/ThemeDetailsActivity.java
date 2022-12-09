package com.xlyj.wood.home.activity;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrusoft.squint.DiagonalView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.ui.BrokenLineView;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ThemeDetailsActivity extends BaseActivity {

    int[] imageresid;
    String[] themes;
    String[] des;
    String title;

    @BindView(R.id.view_page)
    ViewPager viewPage;
    private List<View> viewList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_theme_details);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        ButterKnife.bind(this);
        getDate();
        initView();
        initViewPager();

    }

    /**
     * 得到初始化数据
     */
    private void getDate() {
        Bundle bundle = this.getIntent().getExtras();
        imageresid = bundle.getIntArray("imageresid");
        themes = bundle.getStringArray("themes");
        des = bundle.getStringArray("des");
        title = bundle.getString("title");
    }

    /**
     * 初始化布局
     */
    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvTitle.setTypeface(font);
        tvTitle.setText(title);

    }

    /**
     * //     * 初始化viewpage
     * //
     */
    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < imageresid.length; i++) {
            View view = View.inflate(this, R.layout.h_culture_theme, null);
            TextView tvTitle = view.findViewById(R.id.tv_theme);
            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
            tvTitle.setTypeface(font);
            tvTitle.setText(themes[i]);
//            tvTitle.setLetterSpacing(0.6f);

            TextView tvD1 = view.findViewById(R.id.tv_d1);
            tvD1.setTypeface(font);
            tvD1.setText(des[i * 3]);
            tvD1.setLetterSpacing(0.6f);

            TextView tvD2 = view.findViewById(R.id.tv_d2);
            tvD2.setTypeface(font);
            tvD2.setText(des[i * 3 + 1]);
            tvD2.setLetterSpacing(0.6f);

            TextView tvD3 = view.findViewById(R.id.tv_d3);
            tvD3.setTypeface(font);
            tvD3.setText(des[i * 3 + 2]);
            tvD3.setLetterSpacing(0.6f);


            DiagonalView iv = view.findViewById(R.id.iv_picture);
            iv.setImageResource(imageresid[i]);
            int finalI = i;
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ThemeDetailsActivity.this, WorksDetailsActivity.class);
                    intent.putExtra("theme",themes[finalI]);
                    startActivity(intent);
                }
            });
            viewList.add(view);
        }
        viewPage.setAdapter(new ThemeAdapter());
        viewPage.setCurrentItem(0); // 设置当前的默认索引
    }

    /**
     * ViewPage的适配器
     */
    class ThemeAdapter extends PagerAdapter {

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
