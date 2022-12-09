package com.xlyj.wood.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    public static void sendHttpRequest(Request request, okhttp3.Callback callback) {
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(callback);
    }
}
