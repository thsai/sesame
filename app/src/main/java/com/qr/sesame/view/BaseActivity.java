package com.qr.sesame.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by wangqi on 2017/4/17.
 */

public class BaseActivity extends AppCompatActivity {

    public void hideSoftInputMethed(View v) {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null && manager != null) {
            manager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showSoftInputMethed(View v) {
        InputMethodManager manager = (InputMethodManager) this.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
