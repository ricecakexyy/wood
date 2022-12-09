package com.xlyj.wood.game.jigsaw_puzzle.activity;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.game.jigsaw_puzzle.GameView;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JigsawPuzzleActivity extends BaseActivity {
//
    @BindView(R.id.gameview)
    public GameView gameview;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_jigsaw_puzzle);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        ButterKnife.bind(this);
        initView();
        initGame();

    }

    /**
     * 初始化游戏盘
     */
    private void initGame() {
        //显示时间
        gameview.setTimeEnabled(true);
        tvLevel.setText("第1关  ");
        gameview.setOnGamemListener(new GameView.GamePintuListener() {
            @Override
            public void nextLevel(final int nextLevel) {

                new AlertDialog.Builder(JigsawPuzzleActivity.this).setTitle("拼图完成").setMessage("进入下一关").setPositiveButton("下一关", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameview.nextLevel();
                        tvLevel.setText("第" + nextLevel+"关  ");
                    }
                }).show();
            }

            @Override
            public void timechanged(int time) {
                //设置时间
                tvTime.setText("倒计时：" + time);
            }

            @Override
            public void gameOver() {
                new AlertDialog.Builder(JigsawPuzzleActivity.this).setTitle("游戏结束").setMessage("挑战失败，已经超时！").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gameview.restartGame();
                    }
                }).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).show();
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STCAIYUN.TTF");
        tvTitle.setTypeface(font);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameview.pauseGame();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameview.resumeGame();
    }
}
