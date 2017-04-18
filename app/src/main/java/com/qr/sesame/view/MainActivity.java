package com.qr.sesame.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qr.sesame.R;
import com.qr.sesame.util.UserInfoSharedPrefsUtil;
import com.qr.sesame.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        etName= (EditText) findViewById(R.id.et_name);
        etPsd= (EditText) findViewById(R.id.et_psd);
        etId= (EditText) findViewById(R.id.et_id);
        login= (Button) findViewById(R.id.login);
        login.setOnClickListener(this);
        register= (Button) findViewById(R.id.register);
        register.setOnClickListener(this);
        ipSetting= (Button) findViewById(R.id.ip_setting);
        ipSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (isValid()) {
                    //如果没有注册过并且用户名与本地保存的不一样则注册成功
                    if (UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this) == null || !UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getName().equals(etName.getText().toString())) {
                        ToastUtil.shortToast(MainActivity.this, "注册成功!");
                        setUserInfoCache();
                        startQTActivity();
                    } else {
                        ToastUtil.shortToast(MainActivity.this, "已经注册过啦");
                    }
                }
                break;
            case R.id.login:
                if (isValid()) {
                    if (UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this) == null) {
                        ToastUtil.shortToast(MainActivity.this, "请注册");
                    } else {
                        if (etName.getText().toString().equals(UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getName())
                                && etPsd.getText().toString().equals(UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getPassword())
                                && etId.getText().toString().equals(UserInfoSharedPrefsUtil.getUserInfoCache(MainActivity.this).getIdcard())) {
                            ToastUtil.shortToast(MainActivity.this, "登录成功");
                            startQTActivity();
                        } else {
                            ToastUtil.shortToast(MainActivity.this, "请填写正确信息");
                        }
                    }
                }
                break;
            case R.id.ip_setting:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                break;
        }
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
