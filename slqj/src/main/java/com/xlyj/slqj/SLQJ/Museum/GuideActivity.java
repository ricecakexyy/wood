package com.xlyj.slqj.SLQJ.Museum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarUtil;

public class GuideActivity extends BaseActivity {
    ImageView iv_map;
    int flag=1;//默认1f
    Button btn1,btn2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

        iv_map=findViewById(R.id.iv_guide_map);
        btn1=findViewById(R.id.btn_1f);
        btn2=findViewById(R.id.btn_2f);
    }
    public void ChangeTo1F(View v){
        if(flag!=1){
            iv_map.setImageResource(R.mipmap.guidemap1f);
            flag=1;
            btn1.setBackgroundResource(R.mipmap.gude_btn_is);
            btn1.setTextColor(Color.parseColor("#ffffff"));
            btn2.setBackgroundResource(R.mipmap.gude_btn_not);
            btn2.setTextColor(Color.parseColor("#000000"));
        }
    }
    public void ChangeTo2F(View v){
        if(flag!=2){
            iv_map.setImageResource(R.mipmap.guidemap2f);
            flag=2;
            btn2.setBackgroundResource(R.mipmap.gude_btn_is);
            btn2.setTextColor(Color.parseColor("#000000"));
            btn1.setBackgroundResource(R.mipmap.gude_btn_not);
            btn1.setTextColor(Color.parseColor("#ffffff"));
        }
    }
}