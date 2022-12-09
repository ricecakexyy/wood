package com.xlyj.slqj.SLQJ.AI.snpetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.baidu.ai.edge.core.detect.DetectionResultModel;
import com.baidu.ai.edge.core.snpe.SnpeConfig;
import com.baidu.ai.edge.core.snpe.SnpeManager;

import java.io.InputStream;
import java.util.List;


public class TestSnpeDetectionTask extends AsyncTask<Void, Void, String> {

    // 请替换为您的序列号
    private static final String SERIAL_NUM = "XXXX-146D-4E5D-XXXX";
    private TextView tv;

    private Context context;

    public TestSnpeDetectionTask(Context context, TextView tv) {
        this.tv = tv;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            SnpeConfig config = new SnpeConfig(context.getAssets(), "snpe-detect/config.json");
            SnpeManager manager = new SnpeManager(context, config, SERIAL_NUM);
            InputStream is = context.getAssets().open("snpe-detect/test.jpg");
            Bitmap image = BitmapFactory.decodeStream(is);
            is.close();
            List<DetectionResultModel> results = manager.detect(image, 0.1f);
            manager.destroy();
            return results.get(0).getLabel() + ":"
                    + results.get(0).getConfidence() + ":" + results.get(0).getBounds();
        } catch (Exception e) {
            Log.e("TestSnpeDetectionTask", "error", e);
        }

        return SnpeManager.getAvailableRuntimes(context).toString();
    }

    protected void onPostExecute(String result) {

        tv.setText(result);
    }
}
