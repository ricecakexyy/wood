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
import android.widget.Adapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.find.activity.VideoDetailsActivity;
import com.xlyj.wood.find.adapter.VideoAdapter;
import com.xlyj.wood.ui.LoadMoreRecyclerView;
import com.xlyj.wood.ui.ProgressDialog;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.util.V;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/1
 * fun: 精选视频
 */
public class VideoFragment extends BaseFragment {

    private View view;
    LoadMoreRecyclerView recyclerView;
    VideoAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressDialog progressDialog;

    private List<Video> videos;


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
                    progressDialog.dismiss();
                    initView(true);
                    break;
                case GET_VIDEO_FAIL:
                    //获取数据失败
                    progressDialog.dismiss();
                    initView(false);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_fragment_video, null, false);
        this.view = view;
        progressDialog = new ProgressDialog(getContext(),"请求数据中");
        progressDialog.show();
        initData();
        return view;
    }


    BmobQuery<Video> query;

    /**
     * 获取数据
     */
    private void initData() {
        query = new BmobQuery<>();
        query.include("author");
        boolean isCache = query.hasCachedResult(Video.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(new FindListener<Video>() {

            @Override
            public void done(List<Video> object, BmobException e) {
                if (e == null) {
                    videos = object;
                    handler.sendEmptyMessage(GET_VIDEO_SUCCESS);
                } else {
                    Log.e("ActivityFragment", e.toString() + "错误码"+e.getErrorCode());
                    handler.sendEmptyMessage(GET_VIDEO_FAIL);
                }
            }

        });
    }

    /**
     * 初始化VIew
     */
    private void initView(boolean isSuccess) {
        LinearLayout llNoDate = view.findViewById(R.id.ll_no_date);
        if(!isSuccess){
            llNoDate.setVisibility(View.VISIBLE);
            return;
        }
        llNoDate.setVisibility(View.GONE);
        adapter = new VideoAdapter(getContext(),videos);
        swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query.clearCachedResult(Video.class);
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
        adapter.setOnItemClickListener(new VideoAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(getActivity(), VideoDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", videos.get(position % videos.size()));
                bundle.putSerializable("allVideo", (Serializable) videos);
                intent.putExtras(bundle);
                intent.putExtra("position", position);
                getActivity().startActivity(intent);
            }
        });
    }

}
