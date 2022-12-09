package com.xlyj.wood.shop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.R;
import com.xlyj.wood.shop.adapter.CategoryListViewAdapter;
import com.xlyj.wood.shop.adapter.CategoryRecyclerViewAdapter;

/**
 * @author cute xyy biu~biu~
 * created on 2020/8/25
 * fun:
 */
public class SCategoryFragment extends Fragment {
    View view;
    RecyclerView recyclerView;
    CategoryRecyclerViewAdapter recyclerViewAdapter;
    ListView listView;
    CategoryListViewAdapter listViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.s_fragment_category, container, false);
        initView();
        return view;
    }


    /**
     * 初始化view
     */
    private void initView() {
        //左侧recyclerview
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewAdapter = new CategoryRecyclerViewAdapter(getContext());
        recyclerView.setAdapter(recyclerViewAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.scrollToPosition(0);

        //右侧listview
        listView = view.findViewById(R.id.list_view);
        listViewAdapter = new CategoryListViewAdapter(getContext());
        listView.setAdapter(listViewAdapter);
        adapterClick();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int scrollState;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                this.scrollState = scrollState;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if (isClick && firstVisibleItem != selectedPosition) {
                    return;
                } else if (isClick && firstVisibleItem == selectedPosition) {
                    isClick = false;
                    return;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    return;
                }
                recyclerViewAdapter.setSelectedPostion(firstVisibleItem);
                recyclerViewAdapter.notifyDataSetChanged();
                if ((totalItemCount > 0)
                        && (view.getLastVisiblePosition()
                        == totalItemCount - 1)) {
                    //最后一个数据
                    if (view.getBottom()
                            == view.getChildAt(
                            view.getChildCount() - 1)
                            .getBottom()) {
                        recyclerViewAdapter.setSelectedPostion(totalItemCount - 1);
                    }
                }
            }
        });
    }

    boolean isClick = false;
    int selectedPosition = 0;

    /**
     * 列表的点击事件
     */
    private void adapterClick() {
        recyclerViewAdapter.setOnItemClickListener(new CategoryRecyclerViewAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                isClick = true;
                selectedPosition = position;
                listView.smoothScrollToPosition(position);
            }
        });
    }
}
