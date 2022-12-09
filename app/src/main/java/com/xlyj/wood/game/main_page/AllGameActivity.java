package com.xlyj.wood.game.main_page;

import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllGameActivity extends BaseActivity {

//    @BindView(R.id.tv_jigsaw_title)
//    TextView tvJigsawTitle;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    GameAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity_all_game);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
    }

    private void initView() {
        adapter = new GameAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

//    /**
//     * 初始化布局
//     */
//    private void initView() {
//        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STCAIYUN.TTF");
//        tvJigsawTitle.setTypeface(font);
//        tvJigsawTitle.setOnClickListener(new OnClick());
//    }
//
//    class OnClick implements View.OnClickListener{
//
//        @Override
//        public void onClick(View v) {
//            Intent intent = new Intent();
//            switch (v.getId()){
//                case R.id.tv_jigsaw_title:
//                    intent.setClass(AllGameActivity.this, JigsawHomeActivity.class);
//                    break;
//            }
//            startActivity(intent);
//        }
//    }
}
