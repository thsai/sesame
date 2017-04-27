package com.qr.sesame.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qr.sesame.R;
import com.qr.sesame.api.QRService;
import com.qr.sesame.entiy.SuccessData;
import com.qr.sesame.util.IPSharedPrefsUtil;
import com.qr.sesame.util.UserInfoSharedPrefsUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangqi on 2017/4/27.
 */

public class SplashActivity extends BaseActivity {

    Retrofit retrofit;
    String baseUrl;

    int tag = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        baseUrl = "http://" + IPSharedPrefsUtil.getIPCache(this) + ":8080/qrcls/";
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        login();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tag == 1) {
                    startActivity(new Intent(SplashActivity.this, QTActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }
        }, 2000);


    }

    private OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.v("wq", message.toString());
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }

    private void login() {
        QRService loginService = retrofit.create(QRService.class);
        loginService.login(UserInfoSharedPrefsUtil.getUserInfoCache(this).getIdcard(), UserInfoSharedPrefsUtil.getUserInfoCache(this).getName(),
                UserInfoSharedPrefsUtil.getUserInfoCache(this).getPassword())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SuccessData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        tag = 0;
                    }

                    @Override
                    public void onNext(SuccessData successData) {
                        if (successData.getStatus() == 1) {
                            tag = 1;
                        } else {
                            tag = 0;
                        }

                    }
                });
    }


}
