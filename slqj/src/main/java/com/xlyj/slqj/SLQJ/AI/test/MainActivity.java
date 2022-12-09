package com.xlyj.slqj.SLQJ.AI.test;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.classify.ClassifyInterface;
import com.baidu.ai.edge.core.detect.DetectionResultModel;
import com.baidu.ai.edge.core.infer.InferConfig;
import com.baidu.ai.edge.core.infer.InferManager;
import com.baidu.ai.edge.core.util.Util;
import com.xlyj.slqj.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    static final String LOGTAG = "armtest_tag";

    private String imagePath = "test.jpg";


    private boolean isRunning = false;

    private ClassifyInterface mClassifyDLManager;

    EditText threadView;
    Spinner socSelector;
    Spinner modelTypeSelector;
    EditText threadCountView;

    TextView boardView;

    Handler uiHandler;

    Button startBtn;

    private static final String[] SOC_LIST = {"通用", "华为NPU", "高通DSP"};
    private static final String[] MODEL_TYPE = {"分类", "检测"};

    private int socType = 0;
    private int modelType = 0;

    final static int runCount = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_time_test);
        initPermission();

        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int time  = msg.arg1;
                String txt;
                switch (time) {
                    case -10086:
                        txt = "运行中";
                        break;
                    case -1:
                        txt = "运行错误";
                        break;
                    default:
                        txt = String.valueOf(time);


                }
                boardView.setText(txt);
                return false;
            }
        });

        threadView = findViewById(R.id.test_paddle_threadNumber);
        threadView.setText(String.valueOf(Util.getInferCores()));

        ArrayAdapter<String> adapter;
        socSelector = findViewById(R.id.test_soc_type);
        socSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                socType = position;
                if (socType != 0) {
                    threadCountView.setVisibility(View.GONE);
                } else {
                    threadCountView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, SOC_LIST);
        socSelector.setAdapter(adapter);

        boardView = findViewById(R.id.test_boardView);

        modelTypeSelector = findViewById(R.id.test_model_type);
        modelTypeSelector.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelType = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item, MODEL_TYPE);
        modelTypeSelector.setAdapter(adapter);

        modelTypeSelector = findViewById(R.id.test_model_type);

        startBtn = findViewById(R.id.test_start);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msgInit =  new Message();
                        msgInit.arg1 = -10086;
                        long result = -1;
                        if (isRunning) {
                            return;
                        }
                        uiHandler.sendMessage(msgInit);
                        isRunning = true;
                        switch (socType) {
                            case 0:
                                if (modelType == 0) {
                                    result = runArmClassify();
                                }

                                if (modelType == 1) {
                                    result = runArmDetect();
                                }
                                break;
                        }
                        Log.i("runacasdnhand", "1");
                        Message msg =  new Message();
                        msg.arg1 = (int) result;
                        uiHandler.sendMessage(msg);
                        isRunning = false;
                    }
                }).start();

            }
        });

        threadCountView = findViewById(R.id.test_paddle_threadNumber);
    }

    private long runArmClassify() {
        try {
            InferConfig config = new InferConfig(getAssets(), "infer-classify/config.json");
            config.setThread(getThreadNum());
            InferManager manager = new InferManager(this, config, "");
            List<ClassificationResultModel> results = null;
            InputStream is = getAssets().open(imagePath);
            Bitmap image = BitmapFactory.decodeStream(is);
            is.close();
            Log.i("TestInferClassifyTask", "images size " + image.getWidth() + ":" + image.getHeight());
            long timeStart = System.currentTimeMillis();
            for (int i = 0; i < runCount; i++) {
                results = manager.classify(image);
            }
            long time =  System.currentTimeMillis() - timeStart;
            manager.destroy();
            return time;
        } catch (Exception e) {
            Log.e("TestArmClassifyTask", "error", e);
            return -1;
        }

    }


    private long runArmDetect() {
        try {
            InferConfig config = new InferConfig(getAssets(), "infer-detect/config.json");
            config.setThread(getThreadNum());
            InferManager manager = new InferManager(this, config, "");
            InputStream is2 = getAssets().open(imagePath);
            Bitmap image = BitmapFactory.decodeStream(is2);
            is2.close();
            Log.i("TestInferDectionTask", "images size " + image.getWidth() + ":" + image.getHeight());
            long timeStart =System.currentTimeMillis();
            for (int i = 0 ; i < runCount; i++) {
                Log.i("TestInferDetectionTask", "time single start");
                List<DetectionResultModel> results = manager.detect(image, 0.3f);

            }
            long time =  System.currentTimeMillis() - timeStart;
            Log.e("TestInferDetectionTask", "time total" + time);
            manager.destroy();
            return time;

        } catch (Exception e) {
            Log.e("TestInferDetectionTask", "error", e);
            return -1;
        }
    }

    private int getThreadNum() {
        return Integer.parseInt(threadCountView.getText().toString());
    }


    /**
     * android 6.0 以上需要动态申请权限
     */
    private void initPermission() {
        String[] permissions = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.

            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // 此处为android 6.0以上动态授权的回调，用户自行实现。
    }

}
