package com.xlyj.wood;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome2Activity extends BaseActivity {


    private final static int TIME_MESSAGE = 1;
    private static int TIME = 3;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_MESSAGE:
                    TIME--;
                    if (TIME > 0) {
                        tvTime.setText(TIME + "s");
                        handler.sendEmptyMessageDelayed(TIME_MESSAGE, 1000);      // send message
                    } else {
                        doSkip();
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_skip)
    RelativeLayout btnSkip;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;


    private void doSkip() {
        Intent intent = new Intent(this, LoginActivity.class);
        this.startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        initView();
        handler.sendEmptyMessageDelayed(TIME_MESSAGE, 1000);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeCallbacksAndMessages(null);
                doSkip();
            }
        });
    }

    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvSkip.setTypeface(font);
        tv1.setTypeface(font);
        tv2.setTypeface(font);
        tv3.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "Fonts/times.ttf");
        tvTime.setTypeface(font);
    }

}
