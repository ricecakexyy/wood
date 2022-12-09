package com.xlyj.wood.find.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.Activity;
import com.xlyj.wood.domain.Publish;
import com.xlyj.wood.find.activity.ActivityDetailsActivity;
import com.xlyj.wood.find.adapter.ActivityMyAdapter;
import com.xlyj.wood.find.adapter.ActivityNowAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/4
 * fun:
 */
public class ActivityFragment extends BaseFragment {
    RecyclerView recyclerViewANow;
    RecyclerView recyclerViewAMy;
    private View view;

    SwipeRefreshLayout refreshLayout;

    ActivityNowAdapter activityNowAdapter;
    ActivityMyAdapter activityMyAdapter;

    private List<Activity> activities;

    private final int GET_VIDEO_SUCCESS = 1;
    private final int GET_VIDEO_FAIL = 2;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_VIDEO_SUCCESS:
                    //成功获取数据
                    initView(true);
                    break;
                case GET_VIDEO_FAIL:
                    //获取数据失败
                    initView(false);
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_fragment_activity, null, false);
        this.view = view;
        initData();
        return view;
    }

    BmobQuery<Activity> query;

    /**
     * 获取数据
     */
    private void initData() {
        query = new BmobQuery<>();
        query.include("user");
        boolean isCache = query.hasCachedResult(Activity.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(new FindListener<Activity>() {
            @Override
            public void done(List<Activity> object, BmobException e) {
                if (e == null) {
                    activities = object;
                    handler.sendEmptyMessage(GET_VIDEO_SUCCESS);
                } else {
                    Log.e("ActivityFragment", e.toString() + "错误码"+e.getErrorCode());
                    handler.sendEmptyMessage(GET_VIDEO_FAIL);
                }
            }
        });
    }

    private void initView(boolean isGet) {
        recyclerViewANow = view.findViewById(R.id.recycler_view_a_now);
        activityNowAdapter = new ActivityNowAdapter(getContext(),activities);
        recyclerViewANow.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewANow.setAdapter(activityNowAdapter);

        recyclerViewAMy = view.findViewById(R.id.recycler_view_a_my);
        activityMyAdapter = new ActivityMyAdapter(getContext(),activities);
        recyclerViewAMy.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewAMy.setAdapter(activityMyAdapter);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query.clearCachedResult(Activity.class);
                initData();
                refreshLayout.setRefreshing(false);
                //TODO
            }
        });
        //点击事件
        OnMyItemClick();
    }

    private void OnMyItemClick() {
        activityMyAdapter.setOnItemClickListener(new ActivityMyAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity", activities.get(position % 2));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });

        activityNowAdapter.setOnItemClickListener(new ActivityNowAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("activity", activities.get(position % 4));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }
}
