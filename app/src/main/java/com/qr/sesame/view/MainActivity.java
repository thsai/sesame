package com.qr.sesame.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qr.sesame.R;
import com.qr.sesame.util.SharedPrefsUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                if (isValid()) {
                    if (SharedPrefsUtil.getUserInfoCache(MainActivity.this) == null || !SharedPrefsUtil.getUserInfoCache(MainActivity.this).getName().equals(etName.getText().toString())) {
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
                    if (SharedPrefsUtil.getUserInfoCache(MainActivity.this) == null) {
                        ToastUtil.shortToast(MainActivity.this, "请注册");
                    } else {
                        if (etName.getText().toString().equals(SharedPrefsUtil.getUserInfoCache(MainActivity.this).getName())
                                && etPsd.getText().toString().equals(SharedPrefsUtil.getUserInfoCache(MainActivity.this).getPassword())
                                && etId.getText().toString().equals(SharedPrefsUtil.getUserInfoCache(MainActivity.this).getIdcard())) {
                            ToastUtil.shortToast(MainActivity.this, "登录成功");
                            startQTActivity();
                        } else {
                            ToastUtil.shortToast(MainActivity.this, "请填写正确信息");
                        }
                    }
                }
                break;
        }
    }

    private void startQTActivity() {
        hideSoftInputMethed(null);
        finish();
        startActivity(new Intent(this, QTActivity.class));
    }


    private void setUserInfoCache() {
        SharedPrefsUtil.setUserInfoCache(this, etName.getText().toString(), etPsd.getText().toString(), etId.getText().toString());
    }

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
