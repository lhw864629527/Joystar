package com.erbao.videoimg.videimg_utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.File;

/**
 * Created by asus on 2018/3/14.
 */

public class VideoimgInitialize {
    private static Context mContext;
    public static int screenWidth;
    public static int screenHeight;
    public static String mp3path;

    public static void initialize(Context context, String path) {
        mContext = context;
        DisplayMetrics mDisplayMetrics = context.getResources()
                .getDisplayMetrics();
        screenWidth = mDisplayMetrics.widthPixels;
        screenHeight = mDisplayMetrics.heightPixels;


        String pa = path;//项目文件的文件夹 如： String mImagePath= Environment.getExternalStorageDirectory()+"/Yoystar/";
        File file = new File(pa);
        if (!file.exists()) {
            file.mkdirs();
        }
        Log.e("TAG", "=====1======" + path.split("0")[1]);
         mp3path = path.split("0")[1];
        if (mp3path.substring(mp3path.length()-1,mp3path.length()).equals("/")){
            mp3path=mp3path+"mp3";
        }else {
            mp3path=mp3path+"/mp3";
        }
        Log.e("TAG", "=====3======" + mp3path);
        //初始化音乐文件
        FilterMuisc.getMuisc(context, mp3path);//mp3path：MP3存放的位置 如：String mp3path="/Yoystar/mp3";
    }

    public static Context getContext() {
        return mContext;
    }

}
