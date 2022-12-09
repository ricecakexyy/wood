package com.xlyj.slqj.SLQJ.AI.infertest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.infer.InferConfig;
import com.baidu.ai.edge.core.infer.InferManager;

import java.io.InputStream;
import java.util.List;

/**
 * 通用arm 分类
 */
public class TestInferClassifyTask extends AsyncTask<Void, Void, String> {

    private TextView tv;

    private Context context;

    private String serialNum;

    public TestInferClassifyTask(Context context, TextView tv, String serialNum) {
        this.tv = tv;
        this.context = context;
        this.serialNum = serialNum;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            // 以下逻辑请放在一个线程里执行，比如使用ThreadHandler

            // 可以在onCreate或onResume中触发，请在新线程里调用
            InferConfig config = new InferConfig(context.getAssets(), "infer-classify/config.json");
            InferManager manager = new InferManager(context, config, serialNum);

            // 准备你的图片Bitmap
            List<ClassificationResultModel> results = null;
            InputStream is = context.getAssets().open("test.jpg");
            Bitmap image = BitmapFactory.decodeStream(is);
            is.close();

            // 在模型销毁前可以不断调用。但是不支持多线程。
            results = manager.classify(image);

            // 可以在onDestory或onPause中触发，请在新线程里调用
            manager.destroy();
            return results.get(0).getLabelIndex() + ":" + results.get(0).getLabel() + ":"
                    + results.get(0).getConfidence();
        } catch (Exception e) {
            Log.e("TestSnpeClassifyTask", "error", e);
        }

        return "error";
    }

    protected void onPostExecute(String result) {
        tv.setText(result);
    }

}
