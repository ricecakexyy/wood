package com.xlyj.wood.me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.LoginActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.utils.StatusBarUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

public class MeActivity extends BaseActivity {

    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.iv_head)
    RoundedImageView ivHead;

    User currentUser;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.rl_no_login)
    RelativeLayout rlNoLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private static final String UPDATE_USER_INFO = "android.intent.action.USER_INFO_UPDATE_SUCCESS";
    LocalBroadcastManager broadcastManager;
    IntentFilter intentFilter;
    BroadcastReceiver bordcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATE_USER_INFO)) {
                initView();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.m_activity_me);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        registerBoardCast();
        initView();
        setClick();
    }

    private void registerBoardCast() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(UPDATE_USER_INFO);
        broadcastManager.registerReceiver(bordcastReceiver, intentFilter);
    }

    private void initView() {
        if (isLogined()) {
            rlNoLogin.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);

            currentUser = BmobUser.getCurrentUser(User.class);

            if (currentUser.getAvatar()!= null ) {
                Log.e("MeActivity", "initView: "+currentUser.getAvatar());
                Glide.with(this)
                        .load(currentUser.getAvatar())
                        .error(R.drawable.m_head_default)
                        .into(ivHead);
            }
            tvNickname.setText(currentUser.getNickname());
        } else {
            rlNoLogin.setVisibility(View.VISIBLE);
            llLogin.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否已经登录
     *
     * @return
     */
    private boolean isLogined() {
        return BmobUser.isLogin();
    }

    private void setClick() {
        tvDetails.setOnClickListener(new MeOnClick());
        btnLogin.setOnClickListener(new MeOnClick());
    }

    public class MeOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_details:
                    GoEditUserInfo();
                    break;
                case R.id.btn_login:
                    GoLogin();
            }
        }
    }

    private void GoLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void GoEditUserInfo() {
        Intent intent = new Intent(this, EditUserInfoActivity.class);
        startActivity(intent);
    }
}
