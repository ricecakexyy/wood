package com.xlyj.wood.home.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.PictureDetailsActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.domain.Pictures;
import com.xlyj.wood.domain.Publish;
import com.xlyj.wood.home.adapter.OtherWorksAdapter;
import com.xlyj.wood.ui.BrokenLineView;
import com.xlyj.wood.utils.DensityUtil;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import uk.co.senab.photoview.PhotoView;

public class WorksDetailsActivity extends BaseActivity {

    private static final int GET_WORKS_SUCCESS = 1;
    String theme;
    @BindView(R.id.line_view)
    BrokenLineView view;
    @BindView(R.id.iv_work)
    ImageView ivWork;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.iv_speak)
    ImageView ivSpeak;
    @BindView(R.id.tv_name_big)
    TextView tvNameBig;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.rv_other_works)
    RecyclerView rvOtherWorks;
    OtherWorksAdapter adapter;

    private TextToSpeech mSpeech;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GET_WORKS_SUCCESS:
                    initRecycler();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_activity_works_details);
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        ButterKnife.bind(this);
        getDate();
        initView();
        getDateFromNet();
    }

    /**
     * 其他作品
     */
    private void initRecycler() {
        adapter = new OtherWorksAdapter(this,works);
        rvOtherWorks.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvOtherWorks.setAdapter(adapter);
    }

    List<Pictures> works;
    void getDateFromNet(){
        BmobQuery<Pictures> query;
        query = new BmobQuery<>();
        query.addWhereEqualTo("pic_type","作品");
        query.findObjects(new FindListener<Pictures>() {
            @Override
            public void done(List<Pictures> object, BmobException e) {
                if (e == null) {
                    works = object;
                    handler.sendEmptyMessage(GET_WORKS_SUCCESS);
                } else {
                    Log.e("PublishFragment", e.toString() + "错误码"+e.getErrorCode());
                }
            }
        });
    }

    private void initView() {
        //设置最大值最小值
        view.setMaxMinPrice("2.0", "6.0");
        //设置把view自身的宽高传进去
        view.setWideHigh(DensityUtil.dp2px(this, 250), DensityUtil.dp2px(this, 500));
        //七个点的
        view.setSevenPrice("5.6", "5.0", "4.5", "3.8", "3.5", "4.1", "3.0");
        //开始动画
        view.start();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                view.onClick(v);
            }
        });

        tvNameBig.setText("忆江南");
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvNameBig.setTypeface(font);
        mSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mSpeech.setLanguage(Locale.ENGLISH);
                    if (result == TextToSpeech.LANG_MISSING_DATA
                            || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Log.e("lanageTag", "not use");
                    } else {
//                        mSpeech.speak(tvDetails.getText().toString(), TextToSpeech.QUEUE_FLUSH,
//                                null);
                    }
                }
            }
        });
        ivSpeak.setOnClickListener(new OnClick());

        ivWork.setOnClickListener(new OnClick());
    }

    ArrayList<String> urls;

    class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_work:
                    urls = new ArrayList<>();
                    urls.add("http://img.mp.itc.cn/upload/20160904/ef61a2a18473481b8ce1d8cd10ad754b_th.png");
                    urls.add("http://img.mp.itc.cn/upload/20160904/cf453c515dbe44c98fff23e36ed358f2_th.png");
                    urls.add("http://img.mp.itc.cn/upload/20160904/dbcd460ed3384723b59907e03ab06621_th.png");
                    Intent intent = new Intent(WorksDetailsActivity.this, PictureDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("urls",urls);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.iv_speak:
                    if(mSpeech.isSpeaking()){
                        mSpeech.stop();
                    }else {
                        mSpeech.setPitch(0.8f);
                        mSpeech.setLanguage(Locale.CHINA);
                        mSpeech.speak(tvDetails.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                    break;
            }
        }
    }

    /**
     * 得到数据
     */
    private void getDate() {
        theme = getIntent().getStringExtra("theme");
    }
}
