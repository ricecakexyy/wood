package com.xlyj.slqj.SLQJ.AI.xeye;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.detect.DetectionResultModel;

import java.util.List;

/**
 * 用于XeyeActivity预览画面
 * Created by lvxiangxiang on 2020/4/7.
 */
public class XeyePreviewView extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;
    private SurfaceHolder mHolder;
    private Canvas mCanvas;
    Bitmap currentBitmap;
    Boolean drawFlg = false;
    Paint mPaint;
    int modelType = 0;//0:classify 1:detection
    List<ClassificationResultModel> classificationResultModels;
    List<DetectionResultModel> detectionResultModels;

    public XeyePreviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public XeyePreviewView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        mHolder = this.getHolder();
        mHolder.addCallback(this);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(24);
    }

    public void drawClassifyResult(Bitmap bitmap, List<ClassificationResultModel> resultModel) {
        currentBitmap = bitmap;
        drawFlg = true;
        modelType = 0;
        classificationResultModels = resultModel;
        start();

    }

    public void drawDetectionResult(Bitmap bitmap, List<DetectionResultModel> resultModel) {
        currentBitmap = bitmap;
        drawFlg = true;
        modelType = 1;
        detectionResultModels = resultModel;
        start();

    }

    private void start() {
        if (drawFlg && currentBitmap != null) {
//            Log.i("xeye", "start drawing...");
            // TODO: 2020/4/8 适配
            int metricsWidth = context.getResources().getDisplayMetrics().widthPixels > currentBitmap.getWidth() ?
                    context.getResources().getDisplayMetrics().widthPixels : currentBitmap.getWidth();
            int bitmapLeft = (metricsWidth - currentBitmap.getWidth()) / 2;
            mCanvas = mHolder.lockCanvas();
            mCanvas.drawBitmap(currentBitmap, bitmapLeft, 0, null);
            if (modelType == 0) {
                for (int i = 0; i < classificationResultModels.size(); i++) {
                    ClassificationResultModel model = classificationResultModels.get(i);
                    mCanvas.drawText(model.getLabel() + ":" + model.getConfidence(), bitmapLeft + currentBitmap.getWidth() - 200, 50 * (i + 1), mPaint);
                }
            } else if (modelType == 1) {
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.STROKE);
                for (int i = 0; i < detectionResultModels.size(); i++) {
                    DetectionResultModel model = detectionResultModels.get(i);
                    mCanvas.drawRoundRect(new RectF(bitmapLeft + model.getBounds().left, model.getBounds().top,
                                    bitmapLeft + model.getBounds().right, model.getBounds().bottom),
                            2, 2, mPaint);
                    mCanvas.drawText(model.getLabel() + ":" + model.getConfidence(), bitmapLeft + model.getBounds().left, model.getBounds().top-5, mPaint);
                }
            }
            mHolder.unlockCanvasAndPost(mCanvas);
            drawFlg = false;
            currentBitmap.recycle();
            currentBitmap = null;
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        drawFlg = false;
    }
}
