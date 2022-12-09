package com.xlyj.slqj.SLQJ.AI;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.util.Log;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.baidu.ai.edge.core.base.BaseConfig;
import com.baidu.ai.edge.core.base.BaseException;
import com.baidu.ai.edge.core.base.CallException;
import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.classify.ClassifyException;
import com.baidu.ai.edge.core.classify.ClassifyInterface;
import com.baidu.ai.edge.core.classify.ClassifyOnline;
import com.baidu.ai.edge.core.ddk.DDKConfig;
import com.baidu.ai.edge.core.ddk.DDKManager;
import com.baidu.ai.edge.core.infer.DDKDaVinciConfig;
import com.baidu.ai.edge.core.detect.DetectInterface;
import com.baidu.ai.edge.core.detect.DetectOnline;
import com.baidu.ai.edge.core.detect.DetectionResultModel;
import com.baidu.ai.edge.core.infer.InferConfig;
import com.baidu.ai.edge.core.infer.InferManager;
import com.baidu.ai.edge.core.ocr.OcrInterface;
import com.baidu.ai.edge.core.ocr.OcrResultModel;
import com.baidu.ai.edge.core.segment.SegmentInterface;
import com.baidu.ai.edge.core.segment.SegmentationResultModel;
import com.baidu.ai.edge.core.snpe.SnpeConfig;
import com.baidu.ai.edge.core.snpe.SnpeManager;
import com.baidu.ai.edge.core.util.HttpUtil;
import com.baidu.ai.edge.core.util.Util;
import com.baidu.ai.edge.ui.activity.MainActivity;
import com.baidu.ai.edge.ui.activity.ResultListener;
import com.baidu.ai.edge.ui.util.ThreadPoolManager;
import com.baidu.ai.edge.ui.util.UiLog;
import com.baidu.ai.edge.ui.view.model.BasePolygonResultModel;
import com.baidu.ai.edge.ui.view.model.ClassifyResultModel;
import com.baidu.ai.edge.ui.view.model.DetectResultModel;
import com.baidu.ai.edge.ui.view.model.OcrViewResultModel;
import com.baidu.ai.edge.ui.view.model.SegmentResultModel;
import com.xlyj.slqj.R;
import com.xlyj.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruanshimin on 2018/10/31.
 */

public class CameraActivity extends MainActivity {

    private String serialNum;

    ClassifyInterface mClassifyDLManager;
    ClassifyInterface mOnlineClassify;
    DetectInterface mDetectManager;
    DetectInterface mOnlineDetect;
    SegmentInterface mSegmentManager;
    OcrInterface mOcrManager;
    public static final int TYPE_INFER = 0;
    public static final int TYPE_DDK150 = 1;
    public static final int TYPE_DDK200 = 11;
    public static final int TYPE_SNPE = 2;
    public static final int TYPE_DDK_DAVINCI = 10;

    private static final int CODE_FOR_WRITE_PERMISSION = 0;

    private int platform = TYPE_INFER;

    private boolean isInitializing = false;

    private boolean hasOnlineApi = false;
    // 模型加载状态
    private boolean modelLoadStatus = false;

    @Override
    /**
     * onCreate中调用
     */
    public void onActivityCreate() {

        int hasWriteStoragePermission =
                ActivityCompat.checkSelfPermission(getApplication(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            start();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CODE_FOR_WRITE_PERMISSION);
        }
        choosePlatform();
    }

    private void choosePlatform() {
        String soc = getIntent().getStringExtra("soc");
        switch (soc) {
            case "dsp":
                platform = TYPE_SNPE;
                break;
            case "npu-vinci":
                platform = TYPE_DDK_DAVINCI;
                break;
            case "npu150":
                platform = TYPE_DDK150;
                break;
            case "npu200":
                platform = TYPE_DDK200;
                break;
            default:
            case "arm":
                platform = TYPE_INFER;
        }
    }


    private void start() {
        // paddleLite需要保证初始化与预测在同一线程保证速度
        ThreadPoolManager.executeSingle(new Runnable() {
            @Override
            public void run() {
                initManager();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ((model == MODEL_DETECT && mDetectManager != null) ||
                                (model == MODEL_CLASSIFY && mClassifyDLManager != null) ||
                                (model == MODEL_SEGMENT && mSegmentManager != null) ||
                                (model == MODEL_OCR && mOcrManager != null)) {
                            modelLoadStatus = true;
                            updateTakePictureButtonStatus();
                        }
                    }
                });
            }
        });
    }

    private void updateTakePictureButtonStatus() {
        if (modelLoadStatus || isOnline) {
            setTakePictureButtonAvailable(true);
        }
        if (!isOnline && !modelLoadStatus) {
            setTakePictureButtonAvailable(false);
        }
    }

    /**
     * 此处简化，建议一个mDetectManager对象在同一线程中调用
     */
    @Override
    public void onActivityDestory() {
        releaseEasyDL();
    }


    /**
     * 新线程中调用 ，从照相机中获取bitmap
     *
     * @param bitmap     RGBA格式
     * @param confidence [0-1）
     * @return
     */
    @Override
    public void onDetectBitmap(Bitmap bitmap, float confidence,
                               final ResultListener.DetectListener listener) {

        if (isOnline) {
            mOnlineDetect.detect(bitmap, confidence,
                    new DetectInterface.OnResultListener() {
                        @Override
                        public void onResult(List<DetectionResultModel> result) {
                            listener.onResult(fillDetectionResultModel(result));
                        }

                        @Override
                        public void onError(BaseException ex) {
                            listener.onResult(null);
                            showError(ex);
                        }
                    });
            return;
        }

        if (mDetectManager == null) {
            showMessage("模型初始化中，请稍后");
            listener.onResult(null);
            return;
        }
        try {
            List<DetectionResultModel> modelList = mDetectManager.detect(bitmap, confidence);
            listener.onResult(fillDetectionResultModel(modelList));
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    private List<BasePolygonResultModel> fillDetectionResultModel(
            List<DetectionResultModel> modelList) {
        List<BasePolygonResultModel> results = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            DetectionResultModel mDetectionResultModel = modelList.get(i);
            DetectResultModel mDetectResultModel = new DetectResultModel();
            mDetectResultModel.setIndex(i + 1);
            mDetectResultModel.setConfidence(mDetectionResultModel.getConfidence());
            mDetectResultModel.setName(mDetectionResultModel.getLabel());
            mDetectResultModel.setBounds(mDetectionResultModel.getBounds());
            results.add(mDetectResultModel);
        }
        return results;
    }

    @Override
    public void onClassifyBitmap(Bitmap bitmap, float confidence,
                                 final ResultListener.ClassifyListener listener) {
        if (isOnline) {
            mOnlineClassify.classify(bitmap, new ClassifyInterface.OnResultListener() {
                @Override
                public void onResult(List<ClassificationResultModel> result) {
                    listener.onResult(fillClassificationResultModel(result));
                }

                @Override
                public void onError(BaseException ex) {
                    listener.onResult(null);
                    showError(ex);
                }
            });
            return;
        }

        if (mClassifyDLManager == null) {
            showMessage("模型初始化中，请稍后");
            listener.onResult(null);
            return;
        }
        try {
            List<ClassificationResultModel> modelList = mClassifyDLManager.classify(bitmap, confidence);
            listener.onResult(fillClassificationResultModel(modelList));
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    @Override
    public void onSegmentBitmap(Bitmap bitmap, float confidence, final ResultListener.SegmentListener listener) {
        if (mSegmentManager == null) {
            showMessage("模型初始化中，请稍后");
            listener.onResult(null);
            return;
        }

        List<SegmentationResultModel> resultModels = null;
        try {
            resultModels = mSegmentManager.segment(bitmap, confidence);
            List<BasePolygonResultModel> results = new ArrayList<>();
            for (int i = 0; i < resultModels.size(); i++) {
                SegmentationResultModel mSegmentationResultModel = resultModels.get(i);
                SegmentResultModel mSegmentResultModel = new SegmentResultModel();
                mSegmentResultModel.setColorId(mSegmentationResultModel.getLabelIndex());
                mSegmentResultModel.setIndex(i + 1);
                mSegmentResultModel.setConfidence(mSegmentationResultModel.getConfidence());
                mSegmentResultModel.setName(mSegmentationResultModel.getLabel());
                mSegmentResultModel.setBounds(mSegmentationResultModel.getBox());
                mSegmentResultModel.setMask(mSegmentationResultModel.getMask());
                results.add(mSegmentResultModel);
            }

            listener.onResult(results);
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }

    }

    @Override
    public void onOcrBitmap(Bitmap bitmap, float confidence, ResultListener.OcrListener listener) {
        List<OcrResultModel> modelList = null;
        try {
            modelList = mOcrManager.ocr(bitmap, confidence);
            List<BasePolygonResultModel> results = new ArrayList<>();
            for (int i = 0; i < modelList.size(); i++) {
                OcrResultModel mOcrResultModel = modelList.get(i);
                OcrViewResultModel mOcrViewResultModel = new OcrViewResultModel();
                mOcrViewResultModel.setColorId(mOcrResultModel.getLabelIndex());
                mOcrViewResultModel.setIndex(i + 1);
                mOcrViewResultModel.setConfidence(mOcrResultModel.getConfidence());
                mOcrViewResultModel.setName(mOcrResultModel.getLabel());
                mOcrViewResultModel.setBounds(mOcrResultModel.getPoints());
                mOcrViewResultModel.setTextOverlay(true);
                results.add(mOcrViewResultModel);
            }
            listener.onResult(results);
        } catch (BaseException e) {
            showError(e);
            listener.onResult(null);
        }
    }

    private List<ClassifyResultModel> fillClassificationResultModel(
            List<ClassificationResultModel> modelList) {
        List<ClassifyResultModel> results = new ArrayList<>();
        for (int i = 0; i < modelList.size(); i++) {
            ClassificationResultModel mClassificationResultModel = modelList.get(i);
            ClassifyResultModel mClassifyResultModel = new ClassifyResultModel();
            mClassifyResultModel.setIndex(i + 1);
            mClassifyResultModel.setConfidence(mClassificationResultModel.getConfidence());
            mClassifyResultModel.setName(mClassificationResultModel.getLabel());
            results.add(mClassifyResultModel);
        }
        return results;
    }

    @Override
    public void dumpDetectResult(List<DetectResultModel> model, Bitmap bitmap, float min) {

    }

    @Override
    public void dumpClassifyResult(List<ClassifyResultModel> model, Bitmap bitmap, float min) {

    }

    private void showError(BaseException e) {
        showMessage(e.getErrorCode(), e.getMessage());
        Log.e("CameraActivity", e.getMessage(), e);
    }

    private void releaseEasyDL() {
        if (model == MODEL_DETECT) {
            if (mDetectManager != null) {
                try {
                    mDetectManager.destroy();
                } catch (BaseException e) {
                    showError(e);
                }
            }
        }
        if (model == MODEL_CLASSIFY) {
            if (mClassifyDLManager != null) {
                try {
                    mClassifyDLManager.destroy();
                } catch (ClassifyException e) {
                    showError(e);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (model == MODEL_SEGMENT) {
            if (mSegmentManager != null) {
                try {
                    mSegmentManager.destroy();
                } catch (ClassifyException e) {
                    showError(e);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
        if (model == MODEL_OCR) {
            if (mOcrManager != null) {
                try {
                    mOcrManager.destroy();
                } catch (ClassifyException e) {
                    showError(e);
                } catch (BaseException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (pageCode == PAGE_CAMERA && !isInitializing) {
            showMessage("模型未初始化");
        } else {
            super.onBackPressed();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_FOR_WRITE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            } else {
                Toast.makeText(getApplicationContext(),
                        "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
            }
        }
    }

    // 是离线还是在线模式
    private boolean isOnline = false;

    @Override
    protected void onSetMenu(PopupMenu actionBarMenu) {
        if (!HttpUtil.isOnline(this) || !hasOnlineApi) {
            actionBarMenu.getMenu().findItem(R.id.online_mode).setEnabled(false);
        }
        if (isOnline) {
            actionBarMenu.getMenu().findItem(R.id.online_mode).setChecked(true);
        } else {
            actionBarMenu.getMenu().findItem(R.id.offline_mode).setChecked(true);
        }
    }

    @Override
    protected void onSetMenuItem(boolean isOnline) {
        if (this.isOnline == isOnline) {
            return;
        }
        this.isOnline = isOnline;
        updateTakePictureButtonStatus();
    }

    private void initManager()  {
        serialNum = getIntent().getStringExtra("serial_num");
        String apiUrl = getIntent().getStringExtra("apiUrl");
        String ak = getIntent().getStringExtra("ak");
        String sk = getIntent().getStringExtra("sk");

        float threshold = BaseConfig.DEFAULT_THRESHOLD;

        if (apiUrl != null) {
            hasOnlineApi = true;
        }
        UiLog.info("model type is" + model);
        if (model == MODEL_DETECT) {
            if (hasOnlineApi) {
                mOnlineDetect = new DetectOnline(apiUrl, ak, sk, this);
            }
            try {
                switch (platform) {
                    case TYPE_DDK200:
                        DDKConfig mDetectConfig = new DDKConfig(getAssets(),
                                "ddk-detect/config.json");
                        threshold = mDetectConfig.getRecommendedConfidence();
                        mDetectManager = new DDKManager(this, mDetectConfig, serialNum);
                        break;
                    case TYPE_SNPE:
                        SnpeConfig mSnpeClassifyConfig = new SnpeConfig(this.getAssets(),
                                "snpe-detect/config.json");
                        threshold = mSnpeClassifyConfig.getRecommendedConfidence();
                        mDetectManager = new SnpeManager(this, mSnpeClassifyConfig, serialNum);
                        break;
                    case TYPE_DDK_DAVINCI:
                        DDKDaVinciConfig mDDKDaVinciConfig = new DDKDaVinciConfig(this.getAssets(),
                                "infer-davinci-detect/config.json");
                        threshold = mDDKDaVinciConfig.getRecommendedConfidence();
                        mDetectManager = new InferManager(this, mDDKDaVinciConfig, serialNum);
                        break;
                    case TYPE_INFER:
                    default:
                        InferConfig mInferConfig = new InferConfig(getAssets(),
                                "infer-detect/config.json");
                        // 可修改ARM推断使用的CPU核数
                        mInferConfig.setThread(Util.getInferCores());
                        threshold = mInferConfig.getRecommendedConfidence();

                        mDetectManager = new InferManager(this, mInferConfig, serialNum);
                        break;
                }

                canAutoRun = true;
                isInitializing = true;
            } catch (BaseException e) {
                showError(e);
            }
        }
        if (model == MODEL_CLASSIFY) {
            if (hasOnlineApi) {
                mOnlineClassify = new ClassifyOnline(apiUrl, ak, sk, this);
            }
            try {
                switch (platform) {
                    case TYPE_DDK150:
                    case TYPE_DDK200:
                        threshold = initDDK(platform);
                        break;
                    case TYPE_DDK_DAVINCI:
                        DDKDaVinciConfig mDDKDaVinciConfig = new DDKDaVinciConfig(this.getAssets(),
                                "infer-davinci-classify/config.json");
                        threshold = mDDKDaVinciConfig.getRecommendedConfidence();
                        mClassifyDLManager = new InferManager(this, mDDKDaVinciConfig, serialNum);
                        break;
                    case TYPE_SNPE:
                        SnpeConfig mSnpeClassifyConfig = new SnpeConfig(this.getAssets(),
                                "snpe-classify/config.json");
                        threshold = mSnpeClassifyConfig.getRecommendedConfidence();
                        mClassifyDLManager = new SnpeManager(this, mSnpeClassifyConfig, serialNum);
                        break;
                    case TYPE_INFER:
                    default:
                        threshold = initInfer();
                        break;
                }

                canAutoRun = true;
                isInitializing = true;
            } catch (BaseException e) {
                showError(e);
                Log.e("CameraActivity", e.getClass().getSimpleName() + ":" + e.getErrorCode() + ":" + e.getMessage());
            }
        }

        if (model == MODEL_SEGMENT) {
            InferConfig mInferConfig = null;
            try {
                mInferConfig = new InferConfig(getAssets(), "infer-segment/config.json");
                mInferConfig.setThread(Util.getInferCores());
                threshold = mInferConfig.getRecommendedConfidence();
                mSegmentManager = new InferManager(this, mInferConfig, serialNum);
                canAutoRun = true;
                isInitializing = true;
            } catch (CallException e) {
                showError(e);
            } catch (BaseException e) {
                showError(e);
            }
        }

        if (model == MODEL_OCR) {
            InferConfig mInferConfig = null;
            try {
                mInferConfig = new InferConfig(getAssets(), "infer-ocr/config.json");
                mInferConfig.setThread(Util.getInferCores());
                threshold = mInferConfig.getRecommendedConfidence();
                mOcrManager = new InferManager(this, mInferConfig, null);
                canAutoRun = true;
                isInitializing = true;
            } catch (CallException e) {
                showError(e);
            } catch (BaseException e) {
                showError(e);
            }
        }

        setConfidence(threshold);
    }

    private float initDDK(int type) throws BaseException {
        DDKConfig mClassifyConfig = new DDKConfig(getAssets(),
                "ddk-classify/config.json");
        switch (type) {
            case TYPE_DDK150:
                if (mClassifyConfig.getModelFileAssetPath() == null) {
                    return initInfer();
                }
                break;
            case TYPE_DDK200:
                if (mClassifyConfig.getModelFileAssetPathV200() == null) {
                    return initInfer();
                }
                break;
            default:
        }

        mClassifyDLManager = new DDKManager(this, mClassifyConfig, serialNum);
        return mClassifyConfig.getRecommendedConfidence();
    }

    private float initInfer() throws BaseException {
        InferConfig mInferConfig = new InferConfig(getAssets(),
                "infer-classify/config.json");
        mInferConfig.setThread(Util.getInferCores());
        mClassifyDLManager = new InferManager(this, mInferConfig, serialNum);
        return mInferConfig.getRecommendedConfidence();
    }
}
