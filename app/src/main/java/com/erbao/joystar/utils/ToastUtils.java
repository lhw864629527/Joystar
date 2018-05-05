package com.erbao.joystar.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by asus on 2018/1/16.
 */

public class ToastUtils {
    public static void show(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
}
