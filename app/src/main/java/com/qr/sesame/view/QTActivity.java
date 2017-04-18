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
import com.qr.sesame.api.MovieService;
import com.qr.sesame.api.QRService;
import com.qr.sesame.entiy.SuccessData;
import com.qr.sesame.entiy.UserInfo;
import com.qr.sesame.util.SharedPrefsUtil;
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

    public void customScan() {
        startActivityForResult(new Intent(this, CaptureActivity.class), 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                String result = bundle.getString("result");
                tvResult.setText(result);
                Gson gson = new Gson();
                try {
                    UserInfo userInfo = gson.fromJson(result, UserInfo.class);
                    if (userInfo.getName().equals(SharedPrefsUtil.getUserInfoCache(this).getName())
                            && userInfo.getPassword().equals(SharedPrefsUtil.getUserInfoCache(this).getPassword())
                            && userInfo.getIdcard().equals(SharedPrefsUtil.getUserInfoCache(this).getIdcard())) {
                        ToastUtil.shortToast(this, "通过了，开门");
                        openDoor();
//                        getMovie();
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

    //进行网络请求
    private void getMovie() {
        String baseUrl = "https://api.douban.com/v2/movie/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        MovieService movieService = retrofit.create(MovieService.class);

        movieService.getTopMovie(0, 10)

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Object movieEntity) {

                    }
                });
    }

    private OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("wq", "OkHttp====Message:" + message);
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

    private void openDoor() {


        String baseUrl = "http://10.17.173.21:8080/qrcls/";
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

                    }

                    @Override
                    public void onNext(SuccessData successData) {
                    }
                });
    }

}
