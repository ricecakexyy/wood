package com.xlyj.wood.shop;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.shop.adapter.CommodityAdapter;
import com.xlyj.wood.shop.fragment.SCartFragment;
import com.xlyj.wood.shop.fragment.SCategoryFragment;
import com.xlyj.wood.shop.fragment.SHomeFragment;
import com.xlyj.wood.shop.fragment.SMeFragment;
import com.xlyj.wood.utils.StatusBarUtil;
import com.youth.banner.adapter.BannerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
//    @BindView(R.id.iv_to_top)
//    FloatingActionButton ivToTop;
//
//
//    @BindView(R.id.recycler_view_commodity)
//    RecyclerView recyclerViewCommodity;

    CommodityAdapter adapter;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_category)
    RadioButton rbCategory;
    @BindView(R.id.rb_cart)
    RadioButton rbCart;
    @BindView(R.id.rb_me)
    RadioButton rbMe;
    @BindView(R.id.rg_bottom_tab)
    RadioGroup rgBottomTab;

    private FragmentManager fragmentManager;
    private Fragment sHomeFragment, sCategoryFragment, sCartFragment, sMeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_activity_shop);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.WHITE);
        }
        initFragment();
//        initRecycler();
//        onClick();
    }

    /**
     * 添加各个fragment
     */
    private void initFragment() {
        // 创建Fragment管理对象
        fragmentManager = getSupportFragmentManager();
        rgBottomTab.setOnCheckedChangeListener(this);
        // 默认显示第一项
        setTabSelection(0);
    }

    /**
     * 根据传入的index参数来设置选中的tab页。 每个tab页对应的下标。0表示首页，1表示分类，2代表购物车，4表示我的
     */
    private void setTabSelection(int index) {
        // 开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // 先隐藏所有Fragment
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:// 首页
                if (sHomeFragment == null) {
                    // 如果Fragment为空，则创建一个并添加到界面上
                    sHomeFragment = new SHomeFragment();
                    fragmentTransaction.add(R.id.content, sHomeFragment);
                } else {
                    // 如果Fragment不为空，则直接将它显示出来
                    fragmentTransaction.show(sHomeFragment);
                }
                rbHome.setChecked(true);
                break;
            case 1://分类
                if (sCategoryFragment == null) {
                    sCategoryFragment = new SCategoryFragment();
                    fragmentTransaction.add(R.id.content, sCategoryFragment);
                } else {
                    fragmentTransaction.show(sCategoryFragment);
                }
                rbCategory.setChecked(true);
                break;
            case 2://购物车
                if (sCartFragment == null) {
                    sCartFragment = new SCartFragment();
                    fragmentTransaction.add(R.id.content, sCartFragment);
                } else {
                    fragmentTransaction.show(sCartFragment);
                }
                rbCart.setChecked(true);
                break;
            case 3:// 我的
                if (sMeFragment == null) {
                    sMeFragment = new SMeFragment();
                    fragmentTransaction.add(R.id.content, sMeFragment);
                } else {
                    fragmentTransaction.show(sMeFragment);
                }
                rbMe.setChecked(true);
                break;
        }
        // 提交
        fragmentTransaction.commitAllowingStateLoss();
    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     */
    private void hideFragments(FragmentTransaction transaction) {
        if (sHomeFragment != null) {
            transaction.hide(sHomeFragment);
        }
        if (sCategoryFragment != null) {
            transaction.hide(sCategoryFragment);
        }
        if(sCartFragment != null) {
            transaction.hide(sCartFragment);
        }
        if (sMeFragment != null) {
            transaction.hide(sMeFragment);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_home:// 首页
                setTabSelection(0);
                break;
            case R.id.rb_category:// 分类
                setTabSelection(1);
                break;
            case R.id.rb_cart:// 购物车
                setTabSelection(2);
                break;
            case R.id.rb_me:// 我的
                setTabSelection(3);
                break;
        }
    }





    /**
     * 点击
     */
    private void onClick() {
//        ivMe.setOnClickListener(new ShopClick());
    }


    private void initRecycler() {
        adapter = new CommodityAdapter(this);
//        recyclerViewCommodity.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//        recyclerViewCommodity.setAdapter(adapter);


    }


    private class ShopClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//
            }
        }
    }
}
