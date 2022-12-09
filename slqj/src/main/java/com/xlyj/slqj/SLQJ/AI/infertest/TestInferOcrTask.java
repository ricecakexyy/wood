package com.xlyj.slqj.SLQJ.AI.infertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.baidu.ai.edge.core.infer.InferConfig;
import com.baidu.ai.edge.core.infer.InferManager;
import com.baidu.ai.edge.core.ocr.OcrResultModel;

import java.io.InputStream;
import java.util.List;

/**
 * 通用arm 开源OCR模型
 */
public class TestInferOcrTask extends AsyncTask<Void, Void, String> {

    private static final String SERIAL_NUM = "XXXX-XXXX-XXXX-XXXX";

    private TextView tv;

    private Context context;

    private String serialNum;

    public TestInferOcrTask(Context context, TextView tv, String serialNum) {
        this.tv = tv;
        this.context = context;
        this.serialNum = serialNum;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            InferConfig config = new InferConfig(context.getAssets(), "infer-ocr/config.json");
            InferManager manager = new InferManager(context, config, serialNum);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 3; i++) {
                Log.i("TestInferOcrTask", "image test BEGIN :" + i);
                InputStream is2 = context.getAssets().open("7.jpg");
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap image = BitmapFactory.decodeStream(is2, null, options);
                // 在模型销毁前可以不断调用。但是不支持多线程。
                List<OcrResultModel> results = manager.ocr(image, 0.3f);

                sb.append("result box size: ").append(results.size()).append(":\n");
                for (OcrResultModel model : results) {
                    sb.append("BOX :");
                    for (Point point : model.getPoints()) {
                        sb.append("[").append(point.x).append(",").append(point.y).append("];");
                    }
                    sb.append("{");
                    sb.append(model.getLabel());
                    sb.append("}\n");
                }
                is2.close();
                Log.i("TestInferDectionTask", "image test END :" + i + " ;" + image.getWidth() + ":" + image.getHeight());
            }


            manager.destroy();
            return sb.toString();
        } catch (Exception e) {
            Log.e("TestInferDetectionTask", "error", e);
        }

        return "error";
    }

    protected void onPostExecute(String result) {
        tv.setText(result);
    }
}
