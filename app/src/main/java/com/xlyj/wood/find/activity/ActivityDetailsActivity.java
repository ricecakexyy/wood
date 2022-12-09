package com.xlyj.wood.find.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Activity;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.find.adapter.ActivitySignUpAdapter;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class ActivityDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_time_start)
    TextView tvTimeStart;
    @BindView(R.id.tv_time_end)
    TextView tvTimeEnd;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.tv_host)
    TextView tvHost;
    @BindView(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_way)
    TextView tvWay;
    @BindView(R.id.tv_tel)
    TextView tvTel;
    @BindView(R.id.rc_application)
    RecyclerView rcApplication;
    @BindView(R.id.ll_no_application)
    LinearLayout llNoApplication;
    @BindView(R.id.rc_msg)
    RecyclerView rcMsg;
    @BindView(R.id.ll_no_msg)
    LinearLayout llNoMsg;
    @BindView(R.id.btn_sign_up)
    Button btnSignUp;
    @BindView(R.id.tv_des)
    TextView tvDes;
    private final int IS_SIGNED = 1;
    private final int NO_SIGNED = 2;
    private final int GET_APPLICATIONS = 3;
    private final int SIGN_UP_SUCCESS = 4;
    private final int SIGN_UP_FAIL = 5;
    private final int NO_SIGN_SUCCESS = 6;
    private final int NO_SIGN_FAIL = 7;
    private Activity activity;
    private boolean isSign = false;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_APPLICATIONS:
                    initSignUp();
                    break;
                case IS_SIGNED:
                    isSign = true;
                    setIsSign(true);
                    break;
                case NO_SIGNED:
                    isSign = false;
                    setIsSign(false);
                    break;
                case SIGN_UP_SUCCESS:
                    isSign = true;
                    users.add(BmobUser.getCurrentUser(User.class));
                    setIsSign(true);
                    initSignUp();
                    Toast.makeText(ActivityDetailsActivity.this, "报名成功", Toast.LENGTH_SHORT).show();
                    break;
                case SIGN_UP_FAIL:
                    isSign = false;
                    setIsSign(false);
                    initSignUp();
                    Toast.makeText(ActivityDetailsActivity.this, "报名失败", Toast.LENGTH_SHORT).show();
                    break;
                case NO_SIGN_SUCCESS:
                    for (int i = 0; i < users.size(); i++) {
                        if (users.get(i).getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                            users.remove(i);
                        }
                    }
                    isSign = false;
                    setIsSign(false);
                    initSignUp();
                    Toast.makeText(ActivityDetailsActivity.this, "取消报名成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 初始化所有申请者
     */
    private void initSignUp() {
        if (users.size() > 0 && !users.isEmpty()) {
            Log.i("initSignUp: ", "" + users.size());
            llNoApplication.setVisibility(View.GONE);
            rcApplication.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
            rcApplication.setLayoutManager(layoutManager);
            ActivitySignUpAdapter adapter = new ActivitySignUpAdapter(users);
            rcApplication.setAdapter(adapter);
        } else {
            Log.i("initSignUp: ", "" + users.size());
            llNoApplication.setVisibility(View.VISIBLE);
            rcApplication.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_activity_activity_details);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        initDate();
        initView();
    }

    /**
     * 初始化布局
     */
    private void initView() {

        collapsingToolbarLayout.setTitle("活动详情");
        collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.CENTER);//设置收缩后标题的位置
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后标题的颜色
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);//设置收缩后标题的颜色

        Glide.with(this)
                .load(activity.getCover())
                .override(Target.SIZE_ORIGINAL)
                .into(ivPicture);
        tvTimeStart.setText(activity.getDateStart().getDate());
        tvTimeEnd.setText(activity.getDateEnd().getDate());
        tvHost.setText(activity.getUser().getNickname());
        tvDes.setText(activity.getDetails());
        getApplications();

        btnSignUp.setOnClickListener(new ActivityDetailsClick());
    }

    List<User> users;

    /**
     * 获得所有申请者
     */
    private void getApplications() {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereRelatedTo("applications", new BmobPointer(activity));
        query.findObjects(new FindListener<User>() {

            @Override
            public void done(List<User> object, BmobException e) {
                if (e == null) {
                    users = object;
                    handler.sendEmptyMessage(GET_APPLICATIONS);
                    User me = BmobUser.getCurrentUser(User.class);
                    for (int i = 0; i < users.size(); i++) {
                        User user = users.get(i);
                        if (user.getObjectId().equals(me.getObjectId())) {
                            Log.i("bmob", "已经报名");
                            handler.sendEmptyMessage(IS_SIGNED);
                            isSign = true;
                            break;
                        } else if (i == users.size() - 1) {
                            handler.sendEmptyMessage(NO_SIGNED);
                            isSign = false;
                            Log.i("bmob", "尚未报名");
                        }
                    }
                    Log.i("bmob", "查询个数：" + object.size());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage());
                }
            }

        });
    }

    /**
     * 是否已经报名有不同的图标
     *
     * @param isSign
     */
    private void setIsSign(boolean isSign) {
        if (isSign == true) {
            btnSignUp.setText("取消报名");
            btnSignUp.setBackgroundResource(R.drawable.btn_signed_shape);
        } else {
            btnSignUp.setText("我要报名");
            btnSignUp.setBackgroundResource(R.drawable.btn_sign_up_shape);
        }
    }

    /**
     * 得到数据
     */
    private void initDate() {
        activity = (Activity) getIntent().getSerializableExtra("activity");
    }

    private class ActivityDetailsClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_sign_up:
                    DoSignUp();
                    break;
            }
        }
    }

    /**
     * 点击报名按钮
     */
    private void DoSignUp() {
        User user = BmobUser.getCurrentUser(User.class);
        if (!isSign) {
            BmobRelation relation = new BmobRelation();
            relation.add(user);
            activity.setApplications(relation);
            activity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("ActivityDetailsActivity", "activity中添加了报名者");
                        BmobRelation r2 = new BmobRelation();
                        r2.add(activity);
                        user.setActivities(r2);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    handler.sendEmptyMessage(SIGN_UP_SUCCESS);
                                    Log.i("ActivityDetailsActivity", "user中添加了activity");

                                } else {
                                    handler.sendEmptyMessage(SIGN_UP_FAIL);
                                    Log.i("ActivityDetailsActivity", "失败：" + e.getMessage() + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        handler.sendEmptyMessage(SIGN_UP_FAIL);
                        Log.i("ActivityDetailsActivity", "失败：" + e.getMessage() + e.getErrorCode());
                    }
                }
            });

        } else {
            BmobRelation relation = new BmobRelation();
            relation.remove(user);
            activity.setApplications(relation);
            activity.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.i("bmob", "activity中删除了报名者");
                        BmobRelation r2 = new BmobRelation();
                        r2.remove(activity);
                        user.setActivities(r2);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    handler.sendEmptyMessage(NO_SIGN_SUCCESS);
                                    Log.i("ActivityDetailsActivity", "user中删除了activity");

                                } else {
                                    handler.sendEmptyMessage(NO_SIGN_FAIL);
                                    Log.i("ActivityDetailsActivity", "失败：" + e.getMessage() + e.getErrorCode());
                                }
                            }
                        });
                    } else {
                        handler.sendEmptyMessage(NO_SIGN_FAIL);
                        Log.i("bmob", "失败：" + e.getMessage());
                    }
                }
            });

        }
    }

}
