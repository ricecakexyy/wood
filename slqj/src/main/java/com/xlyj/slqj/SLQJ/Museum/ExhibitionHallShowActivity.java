package com.xlyj.slqj.SLQJ.Museum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.Exhibition;
import com.xlyj.slqj.SLQJ.Museum.Exhibition.ExhitionAdapter;
import com.xlyj.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExhibitionHallShowActivity extends BaseActivity implements View.OnClickListener, TextToSpeech.OnInitListener{
    List<Exhibition> Elist=new ArrayList<>();
    // TTS对象
    private TextToSpeech mTextToSpeech;
    private LinearLayout speechBtn;
    TextView speechTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition_hall_show);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }


        initE();//初始化展品数据
        //展品列表
        RecyclerView recyclerView1=findViewById(R.id.rv_ehs_e);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(layoutManager1);
        ExhitionAdapter adapter1 = new ExhitionAdapter(Elist,this);
        recyclerView1.setAdapter(adapter1);


        speechBtn =  findViewById(R.id.ll_voiceplay);
        speechBtn.setOnClickListener(this);
        speechTxt = findViewById(R.id.tv_eh_content);
        mTextToSpeech = new TextToSpeech(this, this);
        initTextToSpeech();
    }
    private void initE(){
        Exhibition e1=new Exhibition("九龙壁",R.mipmap.slqj_ex8);
        Elist.add(e1);
        Exhibition e2=new Exhibition("曹操赠袍",R.mipmap.slqj_ex13);
        Elist.add(e2);
        Exhibition e3=new Exhibition("三英战吕布",R.mipmap.slqj_ex14);
        Elist.add(e3);
        Exhibition e4=new Exhibition("五代木雕罗汉像",R.mipmap.slqj_ex18);
        Elist.add(e4);
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
}