package com.xlyj.wood.utils;
 
import android.app.Activity;
import android.util.Log;
import android.widget.Toast;
 
 
 
public class ToastUtil {
    public static void showToast(final Activity activity, final String message) {
        if ("main".equals(Thread.currentThread().getName())) {
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
        } else {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}