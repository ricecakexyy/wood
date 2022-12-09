package com.xlyj.wood.pet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.xlyj.wood.KeepCountdownView.KeepCountdownView;
import com.xlyj.wood.R;


public class TestActivity extends AppCompatActivity {
    KeepCountdownView keep1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        if (getIntent() != null)
        {

            keep1=findViewById(R.id.keep1);
            keep1.setVisibility(View.VISIBLE);
            //倒计时监听
            keep1.setCountdownListener(new KeepCountdownView.CountdownListener() {
                @Override
                public void onStart() {
                }

                @Override
                public void onEnd() {

                }
            });
            keep1.startCountDown();//开始倒计时
        }

    }
}