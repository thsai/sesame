package com.qr.sesame.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.qr.sesame.R;
import com.qr.sesame.entiy.UserInfo;
import com.qr.sesame.util.SharedPrefsUtil;
import com.qr.sesame.util.ToastUtil;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    private void openDoor() {
    }

}