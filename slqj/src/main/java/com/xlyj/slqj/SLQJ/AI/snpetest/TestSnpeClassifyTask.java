package com.xlyj.slqj.SLQJ.AI.snpetest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.baidu.ai.edge.core.classify.ClassificationResultModel;
import com.baidu.ai.edge.core.snpe.SnpeConfig;
import com.baidu.ai.edge.core.snpe.SnpeManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;


public class TestSnpeClassifyTask extends AsyncTask<Void, Void, String> {

    // 请替换为您的序列号
    private static final String SERIAL_NUM = "XXXX-146D-4E5D-XXXX";

    private TextView tv;

    private Context context;

    public TestSnpeClassifyTask(Context context, TextView tv) {
        this.tv = tv;
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... voids) {
        try {
            SnpeConfig config = new SnpeConfig(context.getAssets(), "snpe-classify/config.json");
            SnpeManager manager = new SnpeManager(context, config, SERIAL_NUM);
            // config.setModelFilePath("/sdcard/baiduASR/VGG_ILSVRC_16_layers_quantized.dlc");

            List<ClassificationResultModel> results = null;
            InputStream is = context.getAssets().open("snpe-classify/1.jpg");
            Bitmap image = BitmapFactory.decodeStream(is);
            results = manager.classify(image);
            is.close();

            // InputStream is = context.getAssets().open("snpe-classify/3.raw");
            // List<ClassificationResultModel> results = manager.classifyRaw(toFloat(is));

            manager.destroy();
            return results.get(0).getLabelIndex() + ":" + results.get(0).getLabel() + ":"
                    + results.get(0).getConfidence();
        } catch (Exception e) {
            Log.e("TestSnpeClassifyTask", "error", e);
        }

        return SnpeManager.getAvailableRuntimes(context).toString();
    }

    protected void onPostExecute(String result) {
        tv.setText(result);
    }


    private float[] toFloat(InputStream is) {
        float[] result = null;
        try {
            int size = is.available();
            byte[] bytes = new byte[size];
            int readSize = is.read(bytes);
            if (size != readSize || size <= 0) {
                throw new RuntimeException("size is not equal:" + size + ":" + readSize);
            }
            result = new float[size / 4];
            ByteBuffer bf = ByteBuffer.allocate(size);
            bf.order(ByteOrder.LITTLE_ENDIAN);
            bf.put(bytes);

            for (int i = 0; i < size / 4; i++) {
                result[i] = bf.getFloat(i * 4);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("TestSnpeClassifyTask", "toFloat error", e);
        }
        return result;
    }


}
