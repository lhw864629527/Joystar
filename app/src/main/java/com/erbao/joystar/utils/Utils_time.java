package com.erbao.joystar.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by asus on 2018/1/23.
 */

public class Utils_time {
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }

    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
    }



    /**
     * 获取当前时间yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getdatetime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
        String datetime = df.format(new Date());// new Date()为获取当前系统时间
        return datetime;
    }

    /**
     * 获取当前日yyyy-MM-dd
     *
     * @return
     */
    public static String getyaer() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy");// 设置日期格式
        String datetime = df.format(new Date());// new Date()为获取当前系统时间
        return datetime;
    }
    /**
     * 获取当前日yyyy-MM-dd
     *
     * @return
     */
    public static String getdate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        String datetime = df.format(new Date());// new Date()为获取当前系统时间
        return datetime;
    }

    /**
     * 获取当前时间HH:mm:ss
     *
     * @return
     */
    public static String gettime() {
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");// 设置日期格式
        String datetime = df.format(new Date());// new Date()为获取当前系统时间
        return datetime;
    }

    /**
     * 获取当前时间HHmmss
     *
     * @return
     */
    public static String gettimetwo() {
        SimpleDateFormat df = new SimpleDateFormat("HHmmss");// 设置日期格式
        String datetime = df.format(new Date());// new Date()为获取当前系统时间
        return datetime;
    }

    /**
     * 计算两日期的大小yyyy-MM-dd
     *
     * @return
     */
    public static long datesize(String newdate, String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 设置日期格式
        Date datenew;
        Date dateold;
        long minutes = 0;
        try {
            datenew = format.parse(newdate);
            dateold = format.parse(date);
            minutes = (datenew.getTime() - dateold.getTime()) / (1000 * 60);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return minutes;
    }

}
