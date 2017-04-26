package com.qr.sesame.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qr.sesame.R;
import com.qr.sesame.api.QRService;
import com.qr.sesame.entiy.SuccessData;
import com.qr.sesame.util.IPSharedPrefsUtil;
import com.qr.sesame.util.ToastUtil;
import com.qr.sesame.util.UserInfoSharedPrefsUtil;

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

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_psd)
    EditText etPsd;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.register)
    Button register;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.ip_setting)
    Button ipSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        etName = (EditText) findViewById(R.id.et_name);
        etPsd = (EditText) findViewById(R.id.et_psd);
        etId = (EditText) findViewById(R.id.et_id);
        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        ipSetting = (Button) findViewById(R.id.ip_setting);
        ipSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (isValid()) {
                    register();
                }
                break;
            case R.id.login:
                if (isValid()) {
                    if (etName.getText().toString().equals(UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getName())
                            && etPsd.getText().toString().equals(UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getPassword())
                            && etId.getText().toString().equals(UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getIdcard())) {
                        ToastUtil.shortToast(MainActivity.this, "登录成功");
                        startQTActivity();
                    } else {
                        ToastUtil.shortToast(MainActivity.this, "请填写正确信息");
                    }
                }
                break;
            case R.id.ip_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
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

    //注册
    private void register() {
        String baseUrl = "http://" + IPSharedPrefsUtil.getIPCache(this) + ":8080/qrcls/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        QRService qrService = retrofit.create(QRService.class);
        qrService.register(etId.getText().toString(), etName.getText().toString(), etPsd.getText().toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SuccessData>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.shortToast(MainActivity.this, "注册失败!");

                    }

                    @Override
                    public void onNext(SuccessData successData) {
                        if (successData.getStatus() == 1) {
                            ToastUtil.shortToast(MainActivity.this, "注册成功!");
                            setUserInfoCache();
                            startQTActivity();
                        } else
                            ToastUtil.shortToast(MainActivity.this, "注册失败!");
                    }
                });
    }


    private void startQTActivity() {
        hideSoftInputMethed(null);
        finish();
        startActivity(new Intent(this, QTActivity.class));
    }


    private void setUserInfoCache() {
        UserInfoSharedPrefsUtil.setUserInfoCache(this, etName.getText().toString(), etPsd.getText().toString(), etId.getText().toString());
    }

    //判断各项输入是否为空
    private boolean isValid() {
        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtil.shortToast(this, "用户名不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etPsd.getText().toString())) {
            ToastUtil.shortToast(this, "密码不能为空");
            return false;
        }
        if (TextUtils.isEmpty(etId.getText().toString())) {
            ToastUtil.shortToast(this, "身份证不能为空");
            return false;
        }
        return true;
    }

}
