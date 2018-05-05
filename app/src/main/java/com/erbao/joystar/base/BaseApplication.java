package com.erbao.joystar.base;

import android.app.Activity;
import android.app.Application;
import android.os.Environment;

import com.erbao.joystar.utils.SpUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 2018/1/29.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        SpUtil.init(this);//初始化SpUtil


        File appDir = new File(Environment.getExternalStorageDirectory(), "Yoystar");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

    }



    //用于存放我们所有activity的数组
    public static List<Activity> activities;
    //向集合中添加一个activity
    public static void addActivity(Activity activity){
        if(activities == null){
            //如果集合为空  则初始化
            activities = new ArrayList<>();
        }
        //将activity加入到集合中
        activities.add(activity);
    }
    //结束掉所有的Activity
    public static void finishAll(){
        //循环集合,  将所有的activity finish
        if (activities==null){
        }else {
            for(Activity activity : activities){
                if(! activity.isFinishing()){
                    activity.finish();
                }
            }
        }
    }
    public static void removeActivity(Activity activity){
        //移除已经销毁的Activity
        activities.remove(activity);
    }

}
