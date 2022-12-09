package com.xlyj.wood.find.fragment;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xlyj.wood.R;
import com.xlyj.wood.domain.Article;
import com.xlyj.wood.domain.Video;
import com.xlyj.wood.find.adapter.ArticleAdapter;
import com.xlyj.wood.find.adapter.HotTopicAdapter;
import com.xlyj.wood.ui.ProgressDialog;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.wasabeef.richeditor.RichEditor;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/18
 * fun: 文章界面
 */
public class ArticleFragment extends BaseFragment {
    private View view;
    RecyclerView rvHotTopic;
    HotTopicAdapter hotTopicAdapter;
    RecyclerView rvArticle;
    ArticleAdapter articleAdapter;
//    RichEditor editor;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
//            if(articles.size()>0)
//                editor.setHtml(articles.get(0).getHtml());
            initView();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.f_fragment_article, null, false);
        this.view = view;
//        editor = view.findViewById(R.id.editor);
        initData();
        return view;
    }

    private void initView() {
        rvHotTopic = view.findViewById(R.id.rv_hot_topic);
        hotTopicAdapter = new HotTopicAdapter(getContext());
        rvHotTopic.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        rvHotTopic.setAdapter(hotTopicAdapter);

        rvArticle = view.findViewById(R.id.rv_article);
        articleAdapter = new ArticleAdapter(getContext(), articles);
        rvArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvArticle.setAdapter(articleAdapter);


        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query.clearCachedResult(Article.class);
                initData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    BmobQuery<Article> query;
    List<Article> articles;

    /**
     * 获取数据
     */
    private void initData() {
        query = new BmobQuery<>();
        query.include("author");
        boolean isCache = query.hasCachedResult(Article.class);
        if(isCache){
            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        }else{
            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));//此表示缓存一天
        query.findObjects(new FindListener<Article>() {

            @Override
            public void done(List<Article> object, BmobException e) {
                if (e == null) {
                    articles = object;
                    handler.sendEmptyMessage(0);
                } else {
                    Log.e("ActivityFragment", e.toString() + "错误码"+e.getErrorCode());
                }
            }

        });
    }
}
