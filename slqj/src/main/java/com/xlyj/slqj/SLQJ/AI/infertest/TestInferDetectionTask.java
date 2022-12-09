package com.xlyj.slqj.SLQJ.AI.infertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.baidu.ai.edge.core.detect.DetectionResultModel;
import com.baidu.ai.edge.core.infer.InferConfig;
import com.baidu.ai.edge.core.infer.InferManager;

import java.io.InputStream;
import java.util.List;

/**
 * 通用arm 物体检测
 */
public class TestInferDetectionTask extends AsyncTask<Void, Void, String> {

    private TextView tv;

    private Context context;

    private String serialNum;

    public TestInferDetectionTask(Context context, TextView tv, String serialNum) {
        this.tv = tv;
        this.context = context;
        this.serialNum = serialNum;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // 以下逻辑请放在一个线程里执行，比如使用ThreadHandler

            // 可以在onCreate或onResume中触发，请在新线程里调用
            InferConfig config = new InferConfig(context.getAssets(), "infer-detect/config.json");
            InferManager manager = new InferManager(context, config, serialNum);

            // 准备你的图片Bitmap
            InputStream is2 = context.getAssets().open("test.jpg");
            Bitmap image = BitmapFactory.decodeStream(is2);
            is2.close();
            Log.i("TestInferDectionTask", "images size " + image.getWidth() + ":" + image.getHeight());

            // 在模型销毁前可以不断调用。但是不支持多线程。
            List<DetectionResultModel> results = manager.detect(image, 0.3f);
            for (DetectionResultModel result : results) {
                Log.i("TestInferDectionTask", result.getLabelIndex() + ":" + result.getConfidence() + ":" + result.getBounds());
            }

            // 可以在onDestory或onPause中触发，请在新线程里调用
            manager.destroy();
            if (results.isEmpty()) {
                return "empty result";
            } else {
                return results.get(0).getLabel() + ":"
                        + results.get(0).getConfidence() + ":" + results.get(0).getBounds();
            }
        } catch (Exception e) {
            Log.e("TestInferDetectionTask", "error", e);
        }

        return "error";
    }

    protected void onPostExecute(String result) {
        tv.setText(result);
    }
}
