package com.qr.sesame.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.zhy.m.permission.MPermissions;

/**
 * Created by wangqi on 2017/4/17.
 */

public class BaseActivity extends AppCompatActivity {

    //隐藏软键盘
    public void hideSoftInputMethed(View v) {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null && manager != null) {
            manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    //弹出软键盘
    public void showSoftInputMethed(View v) {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
