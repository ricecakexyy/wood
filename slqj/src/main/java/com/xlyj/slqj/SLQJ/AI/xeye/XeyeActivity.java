package com.xlyj.slqj.SLQJ.AI.xeye;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.ai.edge.core.base.BaseException;
import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.detect.DetectionResultModel;
import com.baidu.ai.edge.core.xeye.XeyeConfig;
import com.baidu.ai.edge.core.xeye.XeyeJni;
import com.baidu.ai.edge.core.xeye.XeyeManager;
import com.baidu.ai.edge.ui.util.ThreadPoolManager;
import com.xlyj.BaseActivity;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarUtil;


import java.util.ArrayList;

import static com.baidu.ai.edge.ui.activity.MainActivity.MODEL_CLASSIFY;
import static com.baidu.ai.edge.ui.activity.MainActivity.MODEL_DETECT;

/**
 * 仅用于十目软硬一体方案，不适用SNPE、DDK、ARM
 */
public class XeyeActivity extends BaseActivity implements XeyeJni.XeyeListener {
    private XeyeManager xeyeManager;
    private boolean runningStatus = true;
    private String serialNum;
    XeyePreviewView previewView;
    int modelType;
    XeyeConfig xeyeConfig;
    Boolean errorFlg = true;
    Boolean previewFlg = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xeye);
        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
        previewView = findViewById(R.id.surfaceView);
        modelType = getIntent().getIntExtra("model_type", MODEL_CLASSIFY);
        serialNum = getIntent().getStringExtra("serial_num");
        requestPermissionAlbum();

    }

    private void start() {
        ThreadPoolManager.executeSingle(new Runnable() {
            @Override
            public void run() {
                initXeyeManager();
            }
        });
    }

    private void initXeyeManager() {

        try {
            if (modelType == MODEL_CLASSIFY) {
                xeyeConfig = new XeyeConfig(getAssets(), "xeye-classify/config.json");

            } else if (modelType == MODEL_DETECT) {
                xeyeConfig = new XeyeConfig(getAssets(), "xeye-detect/config.json");

            }
            xeyeConfig.setModelType(modelType);
            xeyeManager = new XeyeManager(this, xeyeConfig, serialNum, this);

        } catch (BaseException e) {
            showMessage("errorcode: " + e.getErrorCode() + ":" + e.getMessage());
            Log.e("XeyeActivity", e.getClass().getSimpleName() + ":" + e.getErrorCode() + ":" + e.getMessage());

        }

    }

    private void requestPermissionAlbum() {
        // 判断是否已经赋予权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // 如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            initXeyeManager();
        }
    }

    protected void showMessage(final String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setTitle("提示")
                        .setMessage(msg)
                        .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:
                if (grantResults[0] == -1) {
                    Toast toast = Toast.makeText(this, "请选择权限", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    start();
                }
                break;
            default:
                break;
        }

    }

    @Override
    protected void onPause() {
        previewFlg = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        previewFlg = true;
        super.onResume();
    }

    @Override
    protected void onDestroy() {

        if (xeyeManager != null) {
            Log.i("xeye", "destroy manager");
            xeyeManager.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onClassifyResult(final float[] result, final Bitmap bitmap) {
        if (!previewFlg) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<ClassificationResultModel> resultModels = new ArrayList<>();
                for (int i = 0; i < result.length; i++) {
                    ClassificationResultModel model = new ClassificationResultModel(xeyeConfig.getLabels()[i], result[i]);
                    resultModels.add(model);
                }
                previewView.drawClassifyResult(bitmap, resultModels);
            }
        });

    }

    @Override
    public void onDetectionResult(final float result[], final Bitmap bitmap) {
        if (!previewFlg) {
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<DetectionResultModel> resultModels = new ArrayList<>();
                for (int i = 0; i < result.length / 6; i++) {
                    float confidence = result[i * 6 + 1];
                    if (confidence < xeyeConfig.getRecommendedConfidence() || confidence > 1.0) {
                        continue;
                    }
                    int index = (int) result[i * 6];
                    if (index < 0 || index >= xeyeConfig.getLabels().length) {
                        continue;
                    }
                    Rect rect = new Rect();
                    rect.left = (int) (result[i * 6 + 2] * bitmap.getWidth());
                    rect.top = (int) (result[i * 6 + 3] * bitmap.getHeight());
                    rect.right = (int) (result[i * 6 + 4] * bitmap.getWidth());
                    rect.bottom = (int) (result[i * 6 + 5] * bitmap.getHeight());
                    if (rect.left >= rect.right || rect.top >= rect.bottom) {
                        continue;
                    }
                    rect.left = rect.left < 0 ? 0 : rect.left;
                    rect.left = rect.left > bitmap.getWidth() ? bitmap.getWidth() : rect.left;
                    rect.top = rect.top < 0 ? 0 : rect.top;
                    rect.top = rect.top > bitmap.getHeight() ? bitmap.getHeight() : rect.top;
                    rect.right = rect.right < 0 ? 0 : rect.right;
                    rect.right = rect.right > bitmap.getWidth() ? bitmap.getWidth() : rect.right;
                    rect.bottom = rect.bottom < 0 ? 0 : rect.bottom;
                    rect.bottom = rect.bottom > bitmap.getHeight() ? bitmap.getHeight() : rect.bottom;

                    DetectionResultModel model = new DetectionResultModel(xeyeConfig.getLabels()[index], confidence, rect);
                    resultModels.add(model);
                }
//                Log.i("xeye", "result model size:" + resultModels.size());
                previewView.drawDetectionResult(bitmap, resultModels);
            }
        });
    }

    @Override
    public void onComputeResult(final float[] result) {
        // compute mode test
//        Bitmap bitmap = xeyeManager.imageBitmap;
//        onDetectionResult(result,bitmap);
    }

    @Override
    public void onError(int code, final String msg) {
        if (!previewFlg) {
            return;
        }
        if (errorFlg) {
            errorFlg = false;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    builder.setTitle("提示")
                            .setMessage(msg)
                            .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    XeyeActivity.this.finish();
                                }
                            })
                            .show();
                }
            });
        }
    }

}
