package com.xlyj.wood.shop.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.ShopCart;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.shop.adapter.CartAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/25
 * fun:
 */
public class SCartFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    CartAdapter adapter;
    CheckBox cbAllCheck;
    TextView tvTotalPrice;

    private static final int GETSHOPCART = 2;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GETSHOPCART:
                    //得到购物车数据后才能初始化
                    initView();
                    break;
            }
        }
    };



    private static final String ADD_TO_CART = "android.intent.action.ADD_TO_CART_SUCCESS";
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver bordcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ADD_TO_CART)) {
                getDate();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.s_fragment_cart, container, false);
        registerBoardCast();
        getDate();
        return view;
    }

    /**
     * 注册广播
     */
    private void registerBoardCast() {
        broadcastManager = LocalBroadcastManager.getInstance(getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction(ADD_TO_CART);
        broadcastManager.registerReceiver(bordcastReceiver, intentFilter);
    }


    List<ShopCart> myShopCart;
    private User user;

    /**
     * 获取数据信息
     */
    private void getDate() {
        BmobQuery<ShopCart> query = new BmobQuery<ShopCart>();
        user = BmobUser.getCurrentUser(User.class);
        query.addWhereRelatedTo("shopCart", new BmobPointer(user));
        query.include("commodity");
        query.findObjects(new FindListener<ShopCart>() {

            @Override
            public void done(List<ShopCart> object, BmobException e) {
                if(e==null){
                    myShopCart = object;
                    handler.sendEmptyMessage(GETSHOPCART);
                    Log.i("查询购物车","查询个数："+object.size());
                }else{
                    Log.i("查询购物车","失败："+e.getMessage());
                }
            }

        });
    }


    /**
     * 初始化view
     */
    private void initView() {
        recyclerView = view.findViewById(R.id.recycler_view);
        cbAllCheck = view.findViewById(R.id.cb_all_check);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        adapter = new CartAdapter(getContext(),myShopCart,cbAllCheck,tvTotalPrice);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

    }

}
