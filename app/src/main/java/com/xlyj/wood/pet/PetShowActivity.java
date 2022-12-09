package com.xlyj.wood.pet;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.pet.Task.Task;
import com.xlyj.wood.pet.Task.TaskAdapter;
import com.xlyj.wood.utils.StatusBarUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;


public class PetShowActivity extends BaseActivity {

    GifImageView gifImageView_pet,gifImageView_huafei,gifImageView_music;
    GifDrawable gifDrawable_pet,gifDrawable_huafei,gifDrawable_music;
    int loop = 3;
    boolean isRunning;
    int is_Running = 1, not_Running = 0,is_chat=2;

    Random rand = new Random();
    String chatcontent[] = {"你知道吗，浙江木雕有东阳木雕、乐清黄杨木雕……",
            "木雕之所以珍贵，不仅仅在于其如雪似银的胎釉，而在于它精美的划花、刻花和印花的纹饰。",
            "浙江东阳著名的木雕大师有黄晓明、陆光正....感兴趣的话可以搜搜看他们的作品哦！",
        "东阳木雕，是以平面浮雕为主的雕刻艺术。其多层次浮雕、散点透视构图、保留平面的装饰，形成了自己鲜明的特色。" +
                "又因色泽清淡，保留原木天然纹理色泽，格调高雅，又称“白木雕”,被誉为“国之瑰宝”。",
    "冯文土在艺术上，打破了木雕传统室内装饰的宫殿式的陈旧程式，创作了富有自然情趣的园林式的新风格，很受好评。" +
            "对于东阳木雕的历史、现状和发展等，他也作了系统的理论研究，编写专著《东阳木雕技艺》和不少论文."};
    int musicId[]={R.raw.music4};
    int music_isplaying=1;
    TextView tv_chatcontent;
    LinearLayout ll_chat;
    Button btn_task;
    List<Task> Tlist=new ArrayList<>();
    MediaPlayer mediaPlayer=new MediaPlayer();

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if(mediaPlayer!=null) {
//            mediaPlayer.pause();
//            mediaPlayer.release();
//        }
//        if(gifDrawable_music!=null)
//            gifImageView_music.setVisibility(View.GONE);
    }


    @Override
    protected void onPause() {
        super.onPause();
//        if(mediaPlayer!=null) {
//            mediaPlayer.pause();
//            mediaPlayer.release();
//        }
//        if(gifDrawable_music!=null)
//            gifImageView_music.setVisibility(View.GONE);
        gifImageView_pet.setImageResource(R.mipmap.pet_0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_show);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        initT();
        tv_chatcontent = findViewById(R.id.tv_chatcontent);
        ll_chat = findViewById(R.id.ll_chat);

        //获取到Bottom Sheet对象
        View bottomSheet = findViewById(R.id.design_bottom_sheet);
        final BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        //默认设置为隐藏
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        btn_task = (Button) findViewById(R.id.pet_btn_task);
        btn_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomSheet(behavior);
            }
        });

        RecyclerView recyclerView=findViewById(R.id.rl_pet_task);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
       TaskAdapter adapter = new TaskAdapter(Tlist,this);
        recyclerView.setAdapter(adapter);
        mediaPlayer = MediaPlayer.create(this, R.raw.music4);



    }
    private void initT(){
        //String title, String content, int imageId
        Task e1=new Task("逛一逛","浏览科普界面10s即可获得10个金币",R.mipmap.task_icon1);
        Tlist.add(e1);
        Task e2=new Task("玩一玩","参与拼图游戏并连续闯过5关即可获得20个金币",R.mipmap.task_icon2);
        Tlist.add(e2);
        Task e3=new Task("买一买","商城下单任意商品即可获得20个金币",R.mipmap.task_icon3);
        Tlist.add(e3);
        Task e4=new Task("赏一赏","前往身临其境模块并浏览10s即可获得10个金币",R.mipmap.task_icon1);
        Tlist.add(e4);
        Task e5=new Task("发一发","发布一个动态即可获得5个金币",R.mipmap.task_icon2);
        Tlist.add(e5);
        Task e6=new Task("看一看","浏览任意视频即可获得5个金币",R.mipmap.task_icon3);
        Tlist.add(e6);
    }
    private void showBottomSheet(BottomSheetBehavior behavior) {
        if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            //mShowBottomSheet.setText(R.string.hide_bottom_sheet);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
           // mShowBottomSheet.setText(R.string.show_bottom_sheet);
        }
    }
    public void Feed(View v) {

        gifImageView_huafei = (GifImageView) findViewById(R.id.gifImageView_huafei);
        gifImageView_huafei.setVisibility(View.VISIBLE);
        gifImageView_huafei.setImageResource(R.mipmap.huafei);


        gifDrawable_huafei = (GifDrawable) gifImageView_huafei.getDrawable();
        gifDrawable_huafei.start(); //开始播放
        gifDrawable_huafei.setLoopCount(loop); //设置播放的次数，播放完了就自动停止
        //gifImageView_huafei.setVisibility(View.GONE);
        isRunning = gifDrawable_huafei.isRunning();
        if (isRunning) {
            handler.sendEmptyMessageDelayed(is_Running, 100);
        } else {
            handler.sendEmptyMessageDelayed(not_Running, 100);

        }

    }

    public void Chat(View v) {
        int n;
        n = rand.nextInt(5);
        tv_chatcontent.setVisibility(View.VISIBLE);
        tv_chatcontent.setText(chatcontent[n]);
        ll_chat.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(is_chat, 500);


    }
    public void Sing(View v) throws IOException {
        //音乐符出现
        gifImageView_music = (GifImageView) findViewById(R.id.gifImageView_music);
        gifImageView_music.setVisibility(View.VISIBLE);
        gifImageView_music.setImageResource(R.mipmap.music);
        gifDrawable_music = (GifDrawable) gifImageView_music.getDrawable();
        gifDrawable_music.start(); //开始播放
        //宠物gif开启
        gifImageView_pet = (GifImageView) findViewById(R.id.gifImageView_pet);
        gifImageView_pet.setImageResource(R.mipmap.pet_sing);
        gifDrawable_pet = (GifDrawable) gifImageView_pet.getDrawable();
        gifDrawable_pet.start(); //开始播放


        if(music_isplaying==1){
            mediaPlayer.stop();
            music_isplaying=0;
            gifImageView_music.setVisibility(View.GONE);
            gifImageView_pet.setImageResource(R.mipmap.pet_0);
        }
        else {
//            int n;
//            n = rand.nextInt(3);
            mediaPlayer= mediaPlayer = MediaPlayer.create(this, musicId[0]);
            int music_time=mediaPlayer.getDuration();
            mediaPlayer.start();
            gifImageView_music.setVisibility(View.VISIBLE);
            music_isplaying=1;


        }



    }
    public void GoToShop(View v){
        startActivity(new Intent(this, PetsShopActivity.class));
    }
    public void ShowTask(View v){

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case 1:
                    isRunning = gifDrawable_huafei.isRunning();
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