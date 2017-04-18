package com.qr.sesame.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.qr.sesame.R;
import com.qr.sesame.util.IPSharedPrefsUtil;
import com.qr.sesame.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangqi on 2017/4/18.
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.sure)
    Button sure;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        ButterKnife.bind(this);
        sure.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sure:
                if (TextUtils.isEmpty(etIp.getText().toString())) {
                    ToastUtil.shortToast(SettingActivity.this,"请输入ip地址");
                } else {
                    IPSharedPrefsUtil.setIPCache(SettingActivity.this, etIp.getText().toString());
                    finish();
                }
                break;
        }
    }
}
