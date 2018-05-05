package com.erbao.joystar.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.erbao.joystar.R;
import com.youth.banner.loader.ImageLoader;


import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by asus on 2018/1/3.
 */

public class GlideImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        //Glide 加载图片简单用法
        Glide.with(context).load(path).into(imageView);
    }
    /**
     * 給定占位符
     * @param context
     * @param url
     * @param imageView
     * @param placheholder
     */
    public static void showPlaceholder(Context context, String url, ImageView imageView,int placheholder) {
        Glide.with(context)
                .load(url)

                .placeholder(placheholder) //占位符 也就是加载中的图片，可放个gif
                .error(placheholder) //失败图片
                .into(imageView);
    }
    /**
     * 普通加载
     * @param context
     * @param url
     * @param imageView
     */
    public static void show(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.bg_gray) //占位符 也就是加载中的图片，可放个gif
                .error(R.drawable.bg_gray) //失败图片
//                .placeholder(R.drawable.banner_placeholder) //占位符 也就是加载中的图片，可放个gif
//                .error(R.drawable.banner_placeholder) //失败图片
                .into(imageView);
    }

    /**
     * 圆形图
     * @param context
     * @param url
     * @param imageView
     */
    public static void showCircle(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)

                .placeholder(R.drawable.bg_gray)
                .error(R.drawable.bg_gray)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }
    public static void showCircle(Context context, String url, ImageView imageView,int placeholder) {
        Glide.with(context)
                .load(url)
                .placeholder(placeholder)
                .error(placeholder)
                .bitmapTransform(new CropCircleTransformation(context))
                .into(imageView);
    }
    /**
     * 圆角图
     * @param context
     * @param url
     * @param imageView
     */
    public static void showCorner (Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.bg_gray) //占位符 也就是加载中的图片，可放个gif
                .error(R.drawable.bg_gray) //失败图片
                .bitmapTransform(new RoundedCornersTransformation(context, 30, 0, RoundedCornersTransformation.CornerType.ALL))  //
                .into(imageView);
    }


    /**
     * 高斯模糊
     * @param context
     * @param url
     * @param imageView
     */
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void showBlur(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)

                .placeholder(R.drawable.bg_gray) //占位符 也就是加载中的图片，可放个gif
                .error(R.drawable.bg_gray) //失败图片
                .bitmapTransform(new BlurTransformation(context, 15))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageView);
    }
    public static void showBlur25(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)

                .placeholder(R.drawable.bg_gray) //占位符 也就是加载中的图片，可放个gif
                .error(R.drawable.bg_gray) //失败图片
                .bitmapTransform(new BlurTransformation(context, 25))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageView);
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void showBlur(Context context, int url, ImageView imageView) {
        Glide.with(context)
                .load(url)

                .placeholder(R.drawable.bg_gray) //占位符 也就是加载中的图片，可放个gif
                .error(R.drawable.bg_gray) //失败图片
                .bitmapTransform(new BlurTransformation(context, 15))  // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageView);
    }









}
