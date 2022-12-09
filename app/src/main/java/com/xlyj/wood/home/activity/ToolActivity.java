package com.xlyj.wood.home.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intrusoft.squint.DiagonalView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToolActivity extends BaseActivity {
    @BindView(R.id.view_page)
    ViewPager viewPage;
    int[] imageresid = {R.drawable.tool_p1, R.drawable.tool_p2, R.drawable.tool_p3,
            R.drawable.tool_p4, R.drawable.tool_p5};
    String[] themes = {"凿","刀具","磨刀石","斧头","硬木槌"};

    String[] des={
            "用于凿孔","是木雕的主要工具"," ",
            "按刀口形状主要分为六种","平凿  圆凿  翘头凿","雕刀  蝴蝶凿  三角凿",
            "用于磨制雕刀","使刀凿锋利","削木如泥",
            "分解大块木料","用于砍劈大型木材"," ",
            "锤击雕刻刀具的工具","用质地坚硬的木料做成"," "
    };


    private List<View> viewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_tool);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        initViewPager();
    }

    private void initView() {
        TextView tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("器具");
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvTitle.setTypeface(font);
    }

    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < imageresid.length; i++) {
            View view = View.inflate(this, R.layout.h_culture_theme, null);
            TextView tvTitle  = view.findViewById(R.id.tv_theme);
            Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
            tvTitle.setTypeface(font);
            tvTitle.setText(themes[i]);
//            tvTitle.setLetterSpacing(0.6f);

            TextView tvD1  = view.findViewById(R.id.tv_d1);
            tvD1.setTypeface(font);
            tvD1.setText(des[i*3]);
            tvD1.setLetterSpacing(0.6f);

            TextView tvD2  = view.findViewById(R.id.tv_d2);
            tvD2.setTypeface(font);
            tvD2.setText(des[i*3+1]);
            tvD2.setLetterSpacing(0.6f);

            TextView tvD3  = view.findViewById(R.id.tv_d3);
            tvD3.setTypeface(font);
            tvD3.setText(des[i*3+2]);
            tvD3.setLetterSpacing(0.6f);


            DiagonalView iv = view.findViewById(R.id.iv_picture);
            iv.setImageResource(imageresid[i]);
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
