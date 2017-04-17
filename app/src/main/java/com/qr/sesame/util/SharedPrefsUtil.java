package com.qr.sesame.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.qr.sesame.constant.BundleKey;
import com.qr.sesame.entiy.UserInfo;

/**
 * Created by wangqi on 2017/4/17.
 */

public class SharedPrefsUtil {
    public static String KEY_USER_INFO;

    public static void setUserInfoCache(Context context, String name, String psd, String id) {
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY_USER_INFO, Context.MODE_PRIVATE).edit();
        editor.putString(BundleKey.KEY_NAME, name);
        editor.putString(BundleKey.KEY_PSD, psd);
        editor.putString(BundleKey.KEY_ID, id);
        editor.commit();
    }

    public static UserInfo getUserInfoCache(Context context) {
        UserInfo userInfo = new UserInfo();
        SharedPreferences sp = context.getSharedPreferences(KEY_USER_INFO, Context.MODE_PRIVATE);
        userInfo.setName(sp.getString(BundleKey.KEY_NAME, ""));
        userInfo.setPassword(sp.getString(BundleKey.KEY_PSD, ""));
        userInfo.setIdcard(sp.getString(BundleKey.KEY_ID, ""));
        return userInfo;
    }

}
