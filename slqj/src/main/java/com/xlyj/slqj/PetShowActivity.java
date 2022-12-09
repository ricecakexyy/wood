package com.xlyj.slqj;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarUtil;

import java.io.IOException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class PetShowActivity extends BaseActivity {

    GifImageView gifImageView_huafei;
    GifDrawable gifDrawable;
    int loop = 3;
    boolean isRunning;
    int is_Running = 1, not_Running = 0,is_chat=2;

    Random rand = new Random();
    String chatcontent[] = {"你知道吗，浙江木雕有东阳木雕、乐清黄杨木雕……", "选材需要注意。。。。。", "浙江东阳著名的木雕大师有黄晓明、陆光正....感兴趣的话可以搜搜看哦"};
    TextView tv_chatcontent;
    LinearLayout ll_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_show);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        tv_chatcontent = findViewById(R.id.tv_chatcontent);
        ll_chat = findViewById(R.id.ll_chat);
        Button btnSin= findViewById(R.id.btn_sing);

    }

    public void sing(View v){

    }

    public void Feed(View v) {

        gifImageView_huafei = (GifImageView) findViewById(R.id.gifImageView_huafei);
        gifImageView_huafei.setVisibility(View.VISIBLE);
        gifImageView_huafei.setImageResource(R.mipmap.huafei);


        gifDrawable = (GifDrawable) gifImageView_huafei.getDrawable();
        gifDrawable.start(); //开始播放
        gifDrawable.setLoopCount(loop); //设置播放的次数，播放完了就自动停止
        //gifImageView_huafei.setVisibility(View.GONE);
        isRunning = gifDrawable.isRunning();
        if (isRunning) {
            handler.sendEmptyMessageDelayed(is_Running, 100);
        } else {
            handler.sendEmptyMessageDelayed(not_Running, 100);

        }

    }

    public void Chat(View v) {
        int n;
        n = rand.nextInt(3);
        tv_chatcontent.setVisibility(View.VISIBLE);
        tv_chatcontent.setText(chatcontent[n]);
        ll_chat.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(is_chat, 500);


    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    isRunning = gifDrawable.isRunning();
                    if (isRunning) {
                        handler.sendEmptyMessageDelayed(is_Running, 500);
                    } else {
                        handler.sendEmptyMessageDelayed(not_Running, 500);

                    }
                    break;
                case 0:
                    gifImageView_huafei.setVisibility(View.GONE);
                    break;
                case 2:
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            ll_chat.setVisibility(View.GONE);
                        }
                    }, 1500);
                default:
                    break;
            }
        }
    };
}