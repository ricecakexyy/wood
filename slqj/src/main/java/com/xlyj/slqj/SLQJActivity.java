package com.xlyj.slqj;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.SLQJ.AI.MainActivity;
import com.xlyj.slqj.SLQJ.Museum.MainMuseumActivity;
import com.xlyj.slqj.SLQJ.Show3d.Main3dActivity;
import com.xlyj.utils.StatusBarUtil;

public class SLQJActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slqj);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }

    }
    public void GotoVRMuseum(View v){
        startActivity(new Intent(SLQJActivity.this, MainMuseumActivity.class));
    }
    public void Goto3Dshow(View v){
        startActivity(new Intent(SLQJActivity.this, Main3dActivity.class));
    }
    public void GotoAIshow(View v){
        startActivity(new Intent(SLQJActivity.this, MainActivity.class));
    }
}