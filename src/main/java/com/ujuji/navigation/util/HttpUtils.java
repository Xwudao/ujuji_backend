package com.ujuji.navigation.util;

import com.ujuji.navigation.core.Constants;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class HttpUtils {

    public static String getHtml(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Agent", Constants.Common.UA)
                .build();

        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            assert response.body() != null;
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
