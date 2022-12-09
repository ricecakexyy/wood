package com.xlyj.slqj.SLQJ.Show3d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import com.study.xuan.gifshow.widget.stlview.callback.OnReadCallBack;
import com.study.xuan.gifshow.widget.stlview.widget.STLView;
import com.study.xuan.gifshow.widget.stlview.widget.STLViewBuilder;
import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarHeightView;
import com.xlyj.utils.StatusBarUtil;


public class Show3dActivity extends BaseActivity {
    private STLView mStl;
    private Context mContext;
    private ProgressDialog mBar;
    private Bundle bundle = new Bundle();
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            float cur = bundle.getFloat("cur");
            float total = bundle.getFloat("total");
            float progress = cur / total;
            Log.i("Progress", progress + "");
            mBar.setProgress((int) (progress * 100.0f));
        }
    };

    String filename;
    private String title;
    private String content;
    TextView tv_title,tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_show3d);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.hide();
        }
        //获取文件名加载某个模型,获取标题和内容加载简介
        filename=getIntent().getStringExtra("filename");
        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");

        //获取并设置底部简介
        tv_title=findViewById(R.id.tv_modeltitle);
        tv_title.setText(title);
        tv_content=findViewById(R.id.tv_modelcontent);
        tv_content.setText(content);

        //模型加载
        mContext = this;
        mStl = (STLView) findViewById(R.id.stl);
        mBar = prepareProgressDialog(mContext);
        mStl.setOnReadCallBack(new OnReadCallBack() {
            @Override
            public void onStart() {
                //Toast.makeText(mContext, "开始解析木雕3d模型", Toast.LENGTH_LONG).show();
                mBar.show();
            }

            @Override
            public void onReading(int cur, int total) {
                bundle.putFloat("cur", cur);
                bundle.putFloat("total", total);
                Message msg = new Message();
                msg.setData(bundle);
                handler.sendMessage(msg);
            }

            @Override
            public void onFinish() {
                mBar.dismiss();
            }
        });
        STLViewBuilder.init(mStl).Assets(this, filename).build();
        mStl.setTouch(true);
        mStl.setScale(true);
        mStl.setRotate(true);
        //mStl.setSensor(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        //super.onSaveInstanceState(outState, outPersistentState);
    }

    private ProgressDialog prepareProgressDialog(Context context) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(R.string.stl_load_progress_title);
        progressDialog.setMax(100);
        progressDialog.setMessage(context.getString(R.string.stl_load_progress_message));
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        return progressDialog;
    }



}