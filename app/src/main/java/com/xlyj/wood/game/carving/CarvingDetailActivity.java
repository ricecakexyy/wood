package com.xlyj.wood.game.carving;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarvingDetailActivity extends BaseActivity {

    @BindView(R.id.iv_carving_board)
    MyCarvingBoard ivCarvingBoard;
    @BindView(R.id.iv_my_view)
    MyView ivMyView;
    @BindView(R.id.iv_board)
    ImageView ivBoard;
    @BindView(R.id.btn_back)
    Button btnBack;
    @BindView(R.id.btn_complete)
    Button btnComplete;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_carving_detail);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        onClick();
    }

    /**
     * 点击事件
     */
    private void onClick() {
        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivMyView.setVisibility(View.GONE);
                ivPicture.setVisibility(View.GONE);
                ivCarvingBoard.Clip(ivMyView.getPath());
            }
        });
    }


    /**
     * 初始化布局
     */
    private void initView() {
        Intent intent = getIntent();
        int pic = intent.getIntExtra("pattern", 0);
    }
}
