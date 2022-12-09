package com.xlyj.wood.home.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xlyj.wood.BaseActivity;
import com.xlyj.wood.R;
import com.xlyj.wood.utils.StatusBarUtil;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CraftsmanDetailsActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    RoundedImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_life)
    TextView tvLife;
    @BindView(R.id.tv_location)
    TextView tvLocation;
    @BindView(R.id.tv_designation)
    TextView tvDesignation;
    @BindView(R.id.tv_a1)
    TextView tvA1;
    @BindView(R.id.tv_a2)
    TextView tvA2;
    @BindView(R.id.tv_a3)
    TextView tvA3;
    @BindView(R.id.tv_a4)
    TextView tvA4;
    @BindView(R.id.ll_achieve)
    LinearLayout llAchieve;
    @BindView(R.id.tv_value)
    TextView tvValue;
    @BindView(R.id.ll_value)
    LinearLayout llValue;

    @BindView(R.id.tv_experience)
    TextView tvExperience;
    @BindView(R.id.tv_works)
    TextView tvWorks;
    @BindView(R.id.iv_speak)
    ImageView ivSpeak;

    private TextToSpeech mSpeech;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h_culture_craftsman);
        ButterKnife.bind(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        getDate();
        initView();
    }

    String name, life, location, value, designation,experience;
    int imageresid;
    String[] achieves;

    private void getDate() {
        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        imageresid = bundle.getInt("imageresid");
        life = bundle.getString("life");
        location = bundle.getString("location");
        designation = bundle.getString("designation");
        experience = bundle.getString("experience");

//        achieves = bundle.getStringArray("achieves");
//        value = bundle.getString("value");

    }

    private void initView() {
        Typeface font = Typeface.createFromAsset(getAssets(), "Fonts/STKAITI.TTF");
        tvName.setText(name);

        tvLife.setText(life);

        tvLocation.setText(location);

        tvDesignation.setText(designation);
        tvExperience.setText(experience);
        tvExperience.setTypeface(font);

        tvWorks.setTypeface(font);
        tvWorks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CraftsmanDetailsActivity.this, CraftsmanWorksActivity.class);
                startActivity(intent);
            }
        });
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
        ivSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSpeech.isSpeaking()){
                    mSpeech.stop();
                }else {
                    mSpeech.setPitch(0.8f);
                    mSpeech.speak(tvExperience.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
//        tvA1.setTypeface(font);
//        tvA1.setText(achieves[0]);
//
//        tvA2.setTypeface(font);
//        tvA2.setText(achieves[1]);
//
//        tvA3.setTypeface(font);
//        tvA3.setText(achieves[2]);
//
//        if (achieves[3].equals("")) {
//            tvA4.setVisibility(View.GONE);
//        } else {
//            tvA4.setTypeface(font);
//            tvA4.setText(achieves[3]);
//        }

        tvValue.setText(value);
        tvValue.setTypeface(font);

        ivHead.setImageResource(imageresid);

        ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CraftsmanDetailsActivity.this, CraftsmanWorksActivity.class);
                startActivity(intent);
            }
        });

    }
}
