package com.zmm.zhoumin20180730.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class HttpUtils {
    private static final String TAG = "HttpUtils-----";
    private static HttpUtils httpUtils;
    private static final int SUCCESS = 0;
    private static final int ERROR = 1;
    private MyHandler myHandler = new MyHandler();
    private static OkLoadListener okLoadListener;

    //单例模式
    public static HttpUtils getHttpUtils() {
        if (httpUtils == null) {
            synchronized (HttpUtils.class) {
                if (httpUtils == null) {
                    httpUtils = new HttpUtils();
                }
            }
        }
        return httpUtils;
    }

    static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    //成功
                    String json = (String) msg.obj;
                    okLoadListener.okLoadSuccess(json);
                    break;

                case ERROR:
                    //失败
                    String error = (String) msg.obj;
                    okLoadListener.okLoadError(error);
                    break;
            }
        }
    }


    public void okGet(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = myHandler.obtainMessage();
                message.what = ERROR;
                message.obj = e.getMessage();
                myHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = myHandler.obtainMessage();
                message.what = SUCCESS;
                message.obj = response.body().string();
                myHandler.sendMessage(message);
            }
        });
    }

    public void setOkLoadListener(OkLoadListener okLoadListener) {
        this.okLoadListener = okLoadListener;
    }

    //post
    public void okPost(String url, Map<String, String> params) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> keySet = params.keySet();
        for (String key :
                keySet) {
            String value = params.get(key);
            builder.add(key, value);
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder().url(url).post(formBody).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = myHandler.obtainMessage();
                message.what = ERROR;
                message.obj = e.getMessage();
                myHandler.sendMessage(message);
            }

            //获取成功时调用
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = myHandler.obtainMessage();
                message.what = SUCCESS;
                message.obj = response.body().string();
                myHandler.sendMessage(message);
            }
        });

    }

    class MyIntercepter implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {

            Request request = chain.request();

            String method = request.method();
            if (method.equals("GET")) {
                String url = request.url().toString();
                boolean contains = url.contains("?");
                if (contains) {
                    url = url + "&source=android";
                } else {
                    url = url + "?source=android";
                }

                Request request1 = request.newBuilder().url(url).build();

                Response response = chain.proceed(request1);

                return response;


            } else if (method.equals("POST")) {
                RequestBody body = request.body();
                if (body instanceof FormBody) {
                    FormBody.Builder newBuilder = new FormBody.Builder();
                    for (int i = 0; i < ((FormBody) body).size(); i++) {
                        String key = ((FormBody) body).name(i);
                        String value = ((FormBody) body).value(i);
                        newBuilder.add(key, value);
                    }
                    newBuilder.add("source", "android");
                    FormBody newBody = newBuilder.build();
                    Request request1 = request.newBuilder().post(newBody).build();
                    Response response = chain.proceed(request1);
                    return response;
                }
            }
            return null;
        }
    }


    public interface OkLoadListener {
        void okLoadSuccess(String success);

        void okLoadError(String error);
    }
}
