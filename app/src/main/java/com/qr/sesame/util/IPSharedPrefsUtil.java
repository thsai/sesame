package com.qr.sesame.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.qr.sesame.constant.BundleKey;
import com.qr.sesame.constant.Constant;

/**
 * Created by wangqi on 2017/4/17.
 */

public class IPSharedPrefsUtil {
    public static String KEY_IP;

    //保存ip地址
    public static void setIPCache(Context context, String ip) {
        SharedPreferences.Editor editor = context.getSharedPreferences(KEY_IP, Context.MODE_PRIVATE).edit();
        editor.putString(BundleKey.KEY_IP, ip);
        editor.commit();
    }

    //获取本地ip地址
    public static String getIPCache(Context context) {
        SharedPreferences sp = context.getSharedPreferences(KEY_IP, Context.MODE_PRIVATE);
        return sp.getString(BundleKey.KEY_IP, Constant.IP_DEFAULT);
    }

}
