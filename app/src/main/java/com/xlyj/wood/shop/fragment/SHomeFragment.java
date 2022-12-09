package com.xlyj.wood.shop.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.Commodity;
import com.xlyj.wood.shop.adapter.SHomeAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/25
 * fun:
 */
public class SHomeFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    SHomeAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private final int GET_COMMODITY = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_COMMODITY:
                    initView();
                    break;
            }
        }
    };
    private List<Commodity> commodities;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.s_fragment_home, container, false);
        getDate();
        return view;
    }

    /**
     * 初始化view
     */
    private void initView() {
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query.clearCachedResult(Commodity.class);
                getDate();
                swipeRefreshLayout.setRefreshing(false);
                //TODO
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new SHomeAdapter(getContext(),commodities);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
    }


    private BmobQuery<Commodity> query;

    /**
     * 获取商品信息
     * @return
     */
    private List<Commodity> getDate() {
        query = new BmobQuery<>();
        query.include("author");
        boolean isCache = query.hasCachedResult(Commodity.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(new FindListener<Commodity>() {

            @Override
            public void done(List<Commodity> object, BmobException e) {
                if (e == null) {
                    commodities = object;
                    handler.sendEmptyMessage(GET_COMMODITY);
                } else {
                    Log.e("SHomeAdapter——获取商品信息", e.toString() + "错误码"+e.getErrorCode());
                }
            }

        });
        return null;
    }
}
