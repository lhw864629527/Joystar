package com.erbao.joystar.moudule.qzone.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.erbao.joystar.utils.LogUtils;

import java.io.IOException;

/**
 * Created by asus on 2018/3/15.
 */

public class ImgVideoWH {
    public static int width = 0;
    public static int height = 0;

    //    //获取网络的图片宽高
    public static int[] getnetworkImageWidthHeight(Context context, String path) {
        LogUtils.e("===path======"+path);
        //获取图片真正的宽高
        Glide.with(context)
                .load(path)
                .asBitmap()//强制Glide返回一个Bitmap对象
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                        width = bitmap.getWidth();
                        height = bitmap.getHeight();
                        LogUtils.e("========="+width+"========="+height);

//                        Log.d(TAG, "width " + width); //200px
//                        Log.d(TAG, "height " + height); //200px
                    }
                });

        return new int[]{width, height};
    }


    public static int[] getvideomuisctime(String path) {
        int[] time = {0, 0, 0};
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            int times = mediaPlayer.getDuration();
            int Height = mediaPlayer.getVideoHeight();
            int Width = mediaPlayer.getVideoWidth();
            time = new int[]{times, Width, Height};
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }




    public static  void setImagexiewwhRela(ImageView img){

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        img.measure(w, h);
        int height = img.getMeasuredHeight();
        int width = img.getMeasuredWidth();

        //   设置控件的高度：
        RelativeLayout.LayoutParams linearParams = (RelativeLayout.LayoutParams) img.getLayoutParams(); //取控件textView当前的布局参数
//                    linearParams.height = wh[1];// 控件的高强制设成20
        linearParams.width = width;// 控件的宽强制设成30
        img.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

    }
}
