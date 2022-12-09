package com.xlyj.slqj.SLQJ.Museum;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.VideoView;

import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarUtil;

public class VrHallActivity extends BaseActivity {
//    VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_hall);
        StatusBarUtil.setRootViewFitsSystemWindows(this,false);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        WebView webView = findViewById(R.id.web_view);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://720yun.com/t/299ju7mytO5?scene_id=24667145");
//        videoView = findViewById(R.id.vv_vr);
//        videoView.setVideoURI(Uri.parse("android.resource://" + this.getPackageName() + "/" +R.raw.vr_video));
//        videoView.start();
        //MediaController mediaController = new MediaController(this);
        //videoView.setMediaController(mediaController);
       // mediaController.setMediaPlayer(videoView);
    }
}