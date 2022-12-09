package com.xlyj.slqj.SLQJ.AI.infertest;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xlyj.slqj.R;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // 请替换为你自己的序列号
    private static final String SERIAL_NUM = "XXXX-XXXX-XXXX-XXXX"; //"XXXX-XXXX-XXXX-XXXX"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);

        Button b = (Button) findViewById(R.id.button2);
        final Application app = getApplication();
        initPermission();
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MainActivity", app.getApplicationInfo().nativeLibraryDir);
                TextView tv = (TextView) findViewById(R.id.sample_text);
                try {
                    String[] dir_arr = app.getAssets().list("");
                    for (String dir : dir_arr) {
                        if (dir.startsWith("infer-") && !dir.contains("davinci")) { // 表示通用arm
                            AsyncTask<Void, Void, String> at = null;
                            if (dir.endsWith("classify")) {
                                at = new TestInferClassifyTask(app, tv, SERIAL_NUM);
                            } else if (dir.endsWith("detect")) {
                                at = new TestInferDetectionTask(app, tv, SERIAL_NUM);
                            } else if (dir.endsWith("ocr")) {
                                at = new TestInferOcrTask(app, tv, SERIAL_NUM);
                            }
                            if (at != null) {
                                at.execute();
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
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
