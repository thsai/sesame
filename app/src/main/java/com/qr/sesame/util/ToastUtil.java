package com.qr.sesame.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by wangqi on 2017/4/17.
 */

public class ToastUtil {
    public static void shortToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }


}
