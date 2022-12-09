package com.xlyj.wood;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.xlyj.wood.domain.User;
import com.xlyj.wood.home.HomeActivity;
import com.xlyj.wood.home.activity.HistoryActivity;
import com.xlyj.wood.myDomain.myUser;
import com.xlyj.wood.ui.ProgressDialog;
import com.xlyj.wood.ui.SoftKeyBoardListener;
import com.xlyj.wood.utils.HttpUtil;
import com.xlyj.wood.utils.StatusBarUtil;
import com.xlyj.wood.utils.ToastUtil;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.tv_forget)
    TextView tvForget;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.card_et)
    CardView cardEt;

    boolean isLogin = true; //判断是登录还是注册

    private final int LOGIN_SUCCESS = 1;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case LOGIN_SUCCESS:
                    GoMainActivity();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        StatusBarUtil.setStatusBarDarkTheme(this, false);
        initView();
    }

    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvForget.setTypeface(font);
        tvRegister.setTypeface(font);
        etPassword.setTypeface(font);
        etUsername.setTypeface(font);
        font = Typeface.createFromAsset(getAssets(), "Fonts/STXINGKA.TTF");
        btnLogin.setTypeface(font);

        btnLogin.setOnClickListener(new LoginOnClick());
        tvRegister.setOnClickListener(new LoginOnClick());
        tvForget.setOnClickListener(new LoginOnClick());
    }

    public class LoginOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_login:
//                    login();
                    LoginClick();
                    break;
                case R.id.tv_register:
                    changeText();
                    break;
                case R.id.tv_forget:
                    doForget();
                    break;
            }
        }
    }

    private void changeText() {
        if(isLogin){
            isLogin = false;
            btnLogin.setText("注 册");
            tvRegister.setText("登录账号");
        }else{
            isLogin = true;
            btnLogin.setText("登 录");
            tvRegister.setText("注册账号");
        }
    }

    private void doForget() {

    }

    /**
     * 登录/注册按钮点击
     */
    private void LoginClick() {
        boolean flag = true;
        if (etUsername.getText().toString().equals("")) {
            //1.设置自定义文字
//            etUsername.setError("用户名为空");
            //2.设置自定义文字和图标
//            Drawable d = getResources().getDrawable(R.drawable.f_more_icon);
//            d.setBounds(0, 0, 100, 100); //必须设置大小，否则不显示
//            etUsername.setError("有错误提示", d);
            //3.设置文字及颜色
            etUsername.setError(Html.fromHtml("<font color=#ffffff>用户名为空</font>"));
            flag = false;
        }
        if (etPassword.getText().toString().equals("")) {
            etPassword.setError(Html.fromHtml("<font color=#ffffff>密码为空</font>"));
            flag = false;
        }
        if(!flag)
            return;

        if(isLogin) {
//            login();
            doLogin();
        }else{
            doRegister();
        }

    }

    /**
     * Bmob的登录账号
     */
    private void doLogin() {

        ProgressDialog progressBar = new ProgressDialog(this, "登陆中...");
        progressBar.show();
        BmobUser.loginByAccount(etUsername.getText().toString(), etPassword.getText().toString(), new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    progressBar.dismiss();
                    Toast.makeText(LoginActivity.this,"登录成功，正在跳转...",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(LOGIN_SUCCESS,1000);
                } else {
                    progressBar.dismiss();
                    Toast.makeText(LoginActivity.this,"登录失败，请检查账号密码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 注册账号
     */
    private void doRegister() {
        ProgressDialog progressBar = new ProgressDialog(this, "注册中...");
        progressBar.show();
        final User user = new User();
        user.setUsername(etUsername.getText().toString());
        user.setPassword(etPassword.getText().toString());
        user.setNickname(etUsername.getText().toString());
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    progressBar.dismiss();
                    Toast.makeText(LoginActivity.this,"注册成功，正在跳转...",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(LOGIN_SUCCESS,1000);
                } else {
                    progressBar.dismiss();
                    Toast.makeText(LoginActivity.this,"注册失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 进入主界面
     */
    private void GoMainActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

    //退出时的时间
    private long mExitTime;

    //对返回键进行监听
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再点一次，返回桌面", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(homeIntent);
        }
    }

    /**
     * 自己的后台的登录
     */
    void login(){
        ProgressDialog progressBar = new ProgressDialog(this, "登陆中...");
        progressBar.show();
        RequestBody formBody = new FormBody.Builder()
                .add("username", String.valueOf(etUsername.getText()))
                .add("pwd", String.valueOf(etPassword.getText()))
                .build();
        final Request request = new Request.Builder()
                .url("http://120.25.218.227:8080/Rumu/AppLoginServlet")
                .post(formBody)
                .build();
        HttpUtil.sendHttpRequest(request,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("LoginActivity","----Fail"+e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("LoginActivity","----Success");
                String responDate = response.body().string();
                try {
                    praseJSONWithGson(responDate);
                    progressBar.dismiss();
                    ToastUtil.showToast(LoginActivity.this, "登录成功，正在跳转...");
//                    Toast.makeText(LoginActivity.this,"登录成功，正在跳转...",Toast.LENGTH_SHORT).show();
                    handler.sendEmptyMessageDelayed(LOGIN_SUCCESS,1000);
                } catch (Exception e) {
                    e.printStackTrace();
                    progressBar.dismiss();
                    ToastUtil.showToast(LoginActivity.this, "登录失败，请检查账号密码");
//                    Toast.makeText(LoginActivity.this,"登录失败，请检查账号密码",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void praseJSONWithGson(String jsonData) throws Exception{
        if (jsonData == null) {
            Log.e("LoginActivity", "null");
            throw new Exception();
        }
        try {
            Log.e("LoginActivity", jsonData);
            JSONObject jsonObject = new JSONObject(jsonData);
            String uid = jsonObject.optString("uid");
            String username = jsonObject.optString("username");
            String pwd = jsonObject.optString("pwd");
            String name = jsonObject.optString("name");
            String email = jsonObject.optString("email");
            String phone = jsonObject.optString("phone");
            String sex = jsonObject.optString("sex");
            String age = jsonObject.optString("age");
            myUser user = new myUser();
            user.setUid(uid);
            user.setUsername(username);
            user.setPwd(pwd);
            user.setName(name);
            user.setEmail(email);
            user.setPhone(phone);
            user.setSex(sex);
            user.setAge(age);

        }catch (Exception e) {
            Log.e("LoginActivity", "登录失败"+e.getMessage());
            throw e;
        }
    }
}
