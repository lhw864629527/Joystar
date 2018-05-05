package com.erbao.videoimg.videimg_utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

/**
 * Created by asus on 2018/3/14.
 */

public class ImgVideoTimeUtils {

    //获取本地的图片宽高
    public static int[] getImageWidthHeight(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        /**
         * 最关键在此，把options.inJustDecodeBounds = true;
         * 这里再decodeFile()，返回的bitmap为空，但此时调用options.outHeight时，已经包含了图片的高了
         */
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        /**
         *options.outHeight为原始图片的高
         */
        Log.e("TAG", "---w---" + options.outWidth + "----h---" + options.outHeight);
        return new int[]{options.outWidth, options.outHeight};
    }

//    //获取网络的图片宽高
//    public static int[] getnetworkImageWidthHeight(Context context,String path){
//        int width=0;
//        int height=0;
//        //获取图片真正的宽高
//        Glide.with(context)
//                .load(path)
//                .asBitmap()//强制Glide返回一个Bitmap对象
//                .into(new SimpleTarget<Bitmap>() {
//                    @Override
//                    public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
//                         width = bitmap.getWidth();
//                         height = bitmap.getHeight();
//                        Log.d(TAG, "width " + width); //200px
//                        Log.d(TAG, "height " + height); //200px
//                    }
//                });
//
//        Log.e("TAG","---w---"+width+"----h---"+height);
//        return new int[]{width.outWidth,height};
//    }


    public static int[] getvideomuisctime(String path) {
        int[] time = {0,0,0};
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
           int times = mediaPlayer.getDuration();
            int Height= mediaPlayer.getVideoHeight();
           int Width= mediaPlayer.getVideoWidth();
            time= new int[]{times, Width, Height};
        } catch (IOException e) {
            e.printStackTrace();
        }

        return time;
    }

}
