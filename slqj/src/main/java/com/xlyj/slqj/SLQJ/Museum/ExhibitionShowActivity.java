package com.xlyj.slqj.SLQJ.Museum;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.Exhibition;

import com.xlyj.utils.StatusBarUtil;
import com.youth.banner.Banner;
import com.youth.banner.adapter.BannerAdapter;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.RotateYTransformer;
//import com.youth.banner.BannerConfig;
//import com.youth.banner.Transformer;
//import com.youth.banner.listener.OnBannerListener;
//import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExhibitionShowActivity extends BaseActivity implements View.OnClickListener, TextToSpeech.OnInitListener {
    List<Exhibition> Elist=new ArrayList<>();
    private Banner mBanner;
    // TTS对象
    private TextToSpeech mTextToSpeech;
    private LinearLayout speechBtn;
    TextView speechTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_show);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        initBanner();



        speechBtn =  findViewById(R.id.ll_ex_voiceplay);
        speechBtn.setOnClickListener(this);
        speechTxt = findViewById(R.id.tv_eh_content);
        mTextToSpeech = new TextToSpeech(this, this);
        initTextToSpeech();
    }

    private void initTextToSpeech() {
        // 参数Context,TextToSpeech.OnInitListener
        mTextToSpeech = new TextToSpeech(this, this);
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mTextToSpeech.setPitch(1.0f);
        // 设置语速
        mTextToSpeech.setSpeechRate(0.5f);
    }

    /**
     * 用来初始化TextToSpeech引擎
     * status:SUCCESS或ERROR这2个值
     * setLanguage设置语言，帮助文档里面写了有22种
     * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
     * TextToSpeech.LANG_NOT_SUPPORTED:不支持
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTextToSpeech.setLanguage(Locale.CHINA);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                //Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
            }
        }
    }
/**
 * text 需要转成语音的文字
 * queueMode 队列方式：
 * QUEUE_ADD：播放完之前的语音任务后才播报本次内容
 * QUEUE_FLUSH：丢弃之前的播报任务，立即播报本次内容
 * params 设置TTS参数，可以是null。
 * KEY_PARAM_STREAM：音频通道，可以是：STREAM_MUSIC、STREAM_NOTIFICATION、STREAM_RING等
 * KEY_PARAM_VOLUME：音量大小，0-1f
 * utteranceId：当前朗读文本的id
 */

    @Override
    public void onClick(View v) {
        if (mTextToSpeech != null && !mTextToSpeech.isSpeaking()) {
            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
            mTextToSpeech.setPitch(1.0f);
            //设定语速 ，默认1.0正常语速
            mTextToSpeech.setSpeechRate(1.5f);
            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
            mTextToSpeech.speak(speechTxt.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        mTextToSpeech.stop(); // 不管是否正在朗读TTS都被打断
        mTextToSpeech.shutdown(); // 关闭，释放资源
    }
    private void initBanner(){
        mBanner = findViewById(R.id.bn_ex);
        //图片资源
        int[] imageResourceID = new int[]{R.mipmap.jxjn1, R.mipmap.jxjn2, R.mipmap.jxjn3,R.mipmap.jxjn4};
        List<Integer> imgeList = new ArrayList<>();
        //轮播标题
        String[] mtitle = new String[]{"1/4", "2/4", "3/4","4/4"};
        List<String> titleList = new ArrayList<>();

        for (int i = 0; i < imageResourceID.length; i++) {
            imgeList.add(imageResourceID[i]);//把图片资源循环放入list里面
            titleList.add(mtitle[i]);//把标题循环设置进列表里面
            //设置图片加载器，通过Glide加载图片
//            mBanner.setImageLoader(new ImageLoader() {
//                @Override
//                public void displayImage(Context context, Object path, ImageView imageView) {
//                    Glide.with(ExhibitionShowActivity.this).load(path).into(imageView);
//                }
//            });
//            //设置轮播的动画效果,里面有很多种特效,可以到GitHub上查看文档。
//            mBanner.setBannerAnimation(Transformer.Default);
//            mBanner.setImages(imgeList);//设置图片资源
//            mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);//设置banner显示样式（带标题的样式）
//            mBanner.setBannerTitles(titleList); //设置标题集合（当banner样式有显示title时）
//            //设置指示器位置（即图片下面的那个小圆点）
//            mBanner.setIndicatorGravity(BannerConfig.CENTER);
//            mBanner.setDelayTime(2000);//设置轮播时间3秒切换下一图
//            mBanner.setOnBannerListener(this);//设置监听
//            mBanner.start();//开始进行banner渲染

        }

        ImageAdapter adapter = new ImageAdapter(imgeList);
        mBanner.setAdapter(adapter)//设置适配器
                .setCurrentItem(0, false)
                    .addBannerLifecycleObserver(this)//添加生命周期观察者
//                .addPageTransformer(new RotateYTransformer())//添加切换效果
                .setBannerGalleryMZ(mBanner.getWidth(),0)
                .setBannerGalleryEffect(mBanner.getWidth(), 0, 0)
                .setIndicator(new CircleIndicator(this));//设置指示器
        mBanner.start();
    }

    /**
     * 自定义布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
     */
    public class ImageAdapter extends BannerAdapter<Integer, ImageAdapter.BannerViewHolder> {

        public ImageAdapter(List<Integer> mDatas) {
            //设置数据，也可以调用banner提供的方法,或者自己在adapter中实现
            super(mDatas);
        }

        //创建ViewHolder，可以用viewType这个字段来区分不同的ViewHolder
        @Override
        public BannerViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            ImageView imageView = new ImageView(parent.getContext());
            //注意，必须设置为match_parent，这个是viewpager2强制要求的
            imageView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return new BannerViewHolder(imageView);
        }

        @Override
        public void onBindView(BannerViewHolder holder, Integer data, int position, int size) {
            holder.imageView.setImageResource(data);
        }

        class BannerViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public BannerViewHolder(@NonNull ImageView view) {
                super(view);
                this.imageView = view;
            }
        }
    }

//    @Override
//    public void OnBannerClick(int position) {
//
//    }
}