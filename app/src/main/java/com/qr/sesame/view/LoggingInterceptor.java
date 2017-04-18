package com.qr.sesame.view;

import android.util.Log;

import com.qr.sesame.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by wangqi on 2017/4/18.
 */

public class LoggingInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (BuildConfig.DEBUG) {
            Log.v("wq",String.format("发送请求 %s on %s%n%s",
                    request.url(), chain.connection(), request.headers()));
//            Logger.e(String.format("发送请求 %s on %s%n%s",
//                    request.url(), chain.connection(), request.headers()));
        }
        return chain.proceed(request);
    }
}
