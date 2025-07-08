package com.example.vlcctrl;

import android.util.Base64;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtil {
    private static final OkHttpClient client = new OkHttpClient();

    public interface ResponseCallback {
        void onSuccess(String response);
        void onFailure(Exception e);
    }


    public static void sendGetRequest(String url, String password, ResponseCallback callback) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Credentials.basic("", password))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body().string());
                } else {
                    callback.onFailure(new IOException("Unexpected code " + response));
                }
            }
        });
    }
}
