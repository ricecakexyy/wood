package com.xlyj.wood.find;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.find.activity.RichEditActivity;
import com.xlyj.wood.find.adapter.VideoAdapter;
import com.xlyj.wood.find.fragment.ActivityFragment;
import com.xlyj.wood.find.fragment.ArticleFragment;
import com.xlyj.wood.find.fragment.BaseFragment;
import com.xlyj.wood.find.fragment.PublishFragment;
import com.xlyj.wood.find.fragment.VideoFragment;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author cute xyy biu~biu~
 * created on 2020/7/31
 * fun: 发现主界面
 */

public class FindActivity extends BaseActivity {

    @BindView(R.id.find_tab_layout)
    TabLayout findTabLayout;
    @BindView(R.id.find_viewpager)
    ViewPager findViewpager;
    @BindView(R.id.iv_more)
    ImageView ivMore;

    private String[] titles = new String[]{"精选视频","动态","活动","文章"};

    private ArrayList<BaseFragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_find);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        init();
    }

    /**
     * 初始化指示器和viewpager
     */
    private void init() {
        fragments.add(new VideoFragment());
        fragments.add(new PublishFragment());
        fragments.add(new ActivityFragment());
        fragments.add(new ArticleFragment());
        findTabLayout.setupWithViewPager(findViewpager);
        findTabLayout.setTabIndicatorFullWidth(false);
        findViewpager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragments));

        for(int i=0;i<titles.length;i++){
            findTabLayout.getTabAt(i).setText(titles[i]);
        }

        ivMore.setOnClickListener(new Click());
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BaseFragment> list;
        public ViewPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> list) {
            super(fm);
            this.list = list;
        }
        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }
        @Override
        public int getCount() {
            return list.size();
        }
    }

    public class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_more:
                    Intent intent = new Intent(FindActivity.this, RichEditActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

}
