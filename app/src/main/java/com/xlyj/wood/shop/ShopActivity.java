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
     * ????????????fragment
     */
    private void initFragment() {
        // ??????Fragment????????????
        fragmentManager = getSupportFragmentManager();
        rgBottomTab.setOnCheckedChangeListener(this);
        // ?????????????????????
        setTabSelection(0);
    }

    /**
     * ???????????????index????????????????????????tab?????? ??????tab?????????????????????0???????????????1???????????????2??????????????????4????????????
     */
    private void setTabSelection(int index) {
        // ????????????
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        // ???????????????Fragment
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:// ??????
                if (sHomeFragment == null) {
                    // ??????Fragment?????????????????????????????????????????????
                    sHomeFragment = new SHomeFragment();
                    fragmentTransaction.add(R.id.content, sHomeFragment);
                } else {
                    // ??????Fragment???????????????????????????????????????
                    fragmentTransaction.show(sHomeFragment);
                }
                rbHome.setChecked(true);
                break;
            case 1://??????
                if (sCategoryFragment == null) {
                    sCategoryFragment = new SCategoryFragment();
                    fragmentTransaction.add(R.id.content, sCategoryFragment);
                } else {
                    fragmentTransaction.show(sCategoryFragment);
                }
                rbCategory.setChecked(true);
                break;
            case 2://?????????
                if (sCartFragment == null) {
                    sCartFragment = new SCartFragment();
                    fragmentTransaction.add(R.id.content, sCartFragment);
                } else {
                    fragmentTransaction.show(sCartFragment);
                }
                rbCart.setChecked(true);
                break;
            case 3:// ??????
                if (sMeFragment == null) {
                    sMeFragment = new SMeFragment();
                    fragmentTransaction.add(R.id.content, sMeFragment);
                } else {
                    fragmentTransaction.show(sMeFragment);
                }
                rbMe.setChecked(true);
                break;
        }
        // ??????
        fragmentTransaction.commitAllowingStateLoss();
    }
    /**
     * ????????????Fragment????????????????????????
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
            case R.id.rb_home:// ??????
                setTabSelection(0);
                break;
            case R.id.rb_category:// ??????
                setTabSelection(1);
                break;
            case R.id.rb_cart:// ?????????
                setTabSelection(2);
                break;
            case R.id.rb_me:// ??????
                setTabSelection(3);
                break;
        }
    }





    /**
     * ??????
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
