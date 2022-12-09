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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.Publish;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.find.activity.PublishDetailsActivity;
import com.xlyj.wood.find.activity.VideoDetailsActivity;
import com.xlyj.wood.find.adapter.PublishAdapter;
import com.xlyj.wood.find.adapter.VideoAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun: 动态
 */
public class PublishFragment extends BaseFragment {
    private View view;
    RecyclerView recyclerView;
    PublishAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;

    private List<Publish> publishes;


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
        View view = inflater.inflate(R.layout.f_fragment_publish, null, false);
        this.view = view;
        initData();
        return view;
    }

    BmobQuery<Publish> query;

    /**
     * 获取数据
     */
    private void initData() {
        query = new BmobQuery<>();
        query.include("user");
        query.order("-date");
        boolean isCache = query.hasCachedResult(Publish.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(new FindListener<Publish>() {
            @Override
            public void done(List<Publish> object, BmobException e) {
                if (e == null) {
                    publishes = object;
                    handler.sendEmptyMessage(GET_VIDEO_SUCCESS);
                } else {
                    Log.e("PublishFragment", e.toString() + "错误码"+e.getErrorCode());
                    handler.sendEmptyMessage(GET_VIDEO_FAIL);
                }
            }
        });
    }

    private void initView(boolean isSuccess) {

        adapter = new PublishAdapter(getContext(),publishes);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query.clearCachedResult(Publish.class);
                initData();
                swipeRefreshLayout.setRefreshing(false);
                //TODO
            }
        });
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //点击事件
        OnMyItemClick();
    }

    private void OnMyItemClick() {
        adapter.setOnItemClickListener(new PublishAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), PublishDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("publish", publishes.get(position % 5));
                intent.putExtras(bundle);
                intent.putExtra("position", position);
                getActivity().startActivity(intent);
            }
        });
    }

}
