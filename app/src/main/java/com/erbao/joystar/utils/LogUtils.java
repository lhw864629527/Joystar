package com.erbao.joystar.utils;

import android.util.Log;

import static com.erbao.joystar.okhttp.HttpUrls.isLogUtils;

/**
 * Created by asus on 2018/1/5.
 */

public class LogUtils {
    public static  void e(String string){
        if (isLogUtils){
            Log.e("Log",string);
        }else {

        }

    }
}
