package com.xlyj.wood.game.carving;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarvingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.gv_choose_pattern)
    GridView gvChoosePattern;
    ChoosePatternAdapter adapter;

    int[] picSid = {
            R.drawable.game_carving_pattern1, R.drawable.game_carving_pattern2, R.drawable.game_carving_pattern3,
            R.drawable.game_carving_pattern4, R.drawable.game_carving_pattern5, R.drawable.game_carving_pattern6,
            R.drawable.game_carving_pattern7, R.drawable.game_carving_pattern8, R.drawable.game_carving_pattern9,
            R.drawable.game_carving_pattern10, R.drawable.game_carving_pattern11, R.drawable.game_carving_pattern12,
            R.drawable.game_carving_pattern13, R.drawable.game_carving_pattern14, R.drawable.game_carving_pattern15,
            R.drawable.game_carving_pattern16, R.drawable.game_carving_pattern17, R.drawable.game_carving_pattern18,
            R.drawable.game_carving_pattern19, R.drawable.game_carving_pattern20
    };
    @BindView(R.id.iv_pattern)
    ImageView ivPattern;
    @BindView(R.id.btn_start_carving)
    Button btnStartCarving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_carving);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        initGridView();
    }

    /**
     * 初始化图案选择的gridview
     */
    private void initGridView() {
        adapter = new ChoosePatternAdapter(this, picSid);
        gvChoosePattern.setAdapter(adapter);
        ChoosePatternClick();
    }

    int picChoosed;
    /**
     * 选择雕刻的图片
     */
    private void ChoosePatternClick() {
        adapter.setOnItemClickListener(new ChoosePatternAdapter.OnItemClick() {
            @Override
            public void onItemClick(int position) {
                ivPattern.setImageResource(picSid[position]);
                ivPattern.setVisibility(View.VISIBLE);
                picChoosed = picSid[position];
            }
        });
    }

    /**
     * 初始化布局
     */
    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STCAIYUN.TTF");
        tvTitle.setTypeface(font);

        /**
         * 开始雕刻
         */
        btnStartCarving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ivPattern.getVisibility()==View.GONE){
                    Toast.makeText(CarvingActivity.this,"请选择要雕刻的图案",Toast.LENGTH_SHORT).show();
                }else{
                    Intent intent = new Intent(CarvingActivity.this, CarvingDetailActivity.class);
                    intent.putExtra("pattern",picChoosed);
                    startActivity(intent);
                }
            }
        });
    }
}
