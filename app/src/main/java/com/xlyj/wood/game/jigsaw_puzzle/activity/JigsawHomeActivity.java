package com.xlyj.wood.game.jigsaw_puzzle.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JigsawHomeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_start_new_game)
    Button btnStartNewGame;
    @BindView(R.id.btn_continue_game)
    Button btnContinueGame;
    @BindView(R.id.btn_select)
    Button btnSelect;
    @BindView(R.id.btn_setting)
    Button btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_jigsaw_home);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        ButterKnife.bind(this);
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STCAIYUN.TTF");
        tvTitle.setTypeface(font);
        btnStartNewGame.setOnClickListener(new OnClick());
    }

    class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.btn_start_new_game:
                    intent.setClass(JigsawHomeActivity.this, JigsawPuzzleActivity.class);
                    break;

            }
            startActivity(intent);
        }
    }

}
