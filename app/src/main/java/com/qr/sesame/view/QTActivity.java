package com.qr.sesame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qr.sesame.R;
import com.qr.sesame.api.QRService;
import com.qr.sesame.entiy.SuccessData;
import com.qr.sesame.entiy.UserInfo;
import com.qr.sesame.util.IPSharedPrefsUtil;
import com.qr.sesame.util.UserInfoSharedPrefsUtil;
import com.qr.sesame.util.ToastUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wangqi on 2017/4/17.
 */

public class QTActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.scan)
    Button scan;
    @BindView(R.id.result)
    TextView tvResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qt_activity);
        ButterKnife.bind(this);
        scan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan:
                customScan();
                break;
        }
    }

    //启动扫描二维码界面
    public void customScan() {
        startActivityForResult(new Intent(this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //扫描二维码成功
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                tvResult.setText(result);
                Gson gson = new Gson();
                try {
                    UserInfo userInfo = gson.fromJson(result, UserInfo.class);
                    //二维码内容和本地保存的用户信息一致则开门
                    if (userInfo.getName().equals(UserInfoSharedPrefsUtil.getUserInfoCache(this).getName())
                            && userInfo.getPassword().equals(UserInfoSharedPrefsUtil.getUserInfoCache(this).getPassword())
                            && userInfo.getIdcard().equals(UserInfoSharedPrefsUtil.getUserInfoCache(this).getIdcard())) {
                        openDoor();
                    } else {
                        ToastUtil.shortToast(this, "未通过");
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    ToastUtil.shortToast(this, "未通过");
                }
            }
        }

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

    //扫描成功后发送请求给服务端
    private void openDoor() {
        String baseUrl = "http://" + IPSharedPrefsUtil.getIPCache(this) + ":8080/qrcls/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        QRService qrService = retrofit.create(QRService.class);
        qrService.scan("true")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SuccessData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(QTActivity.this, "未通过");
                    }

                    @Override
                    public void onNext(SuccessData successData) {
                        ToastUtil.shortToast(QTActivity.this, "芝麻开门");
                    }
                });
    }

}
