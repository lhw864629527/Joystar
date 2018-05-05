package com.erbao.joystar.utils;

import android.content.Context;

/**
 * Created by asus on 2017/5/20.
 */

public class ProviderUtil {
    public static String getFileProviderName(Context context){
        return context.getPackageName()+".provider";
    }
}
