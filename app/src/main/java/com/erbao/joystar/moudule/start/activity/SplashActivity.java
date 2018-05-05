package com.erbao.joystar.moudule.start.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.erbao.joystar.R;
import com.erbao.joystar.base.BaseActivity;
import com.erbao.joystar.base.BaseApplication;
import com.erbao.joystar.moudule.main.activity.MainActivity;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;

import com.erbao.videoimg.videimg_utils.FilterMuisc;
import com.erbao.videoimg.videimg_utils.VideoimgInitialize;

import java.io.File;
import java.util.Locale;

import static com.erbao.joystar.okhttp.HttpUrls.mImagePath;


public class SplashActivity extends BaseActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                    break;
                case 1:
                    break;
                default:
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_splash);

        if (SpUtil.get("isLogin", "").equals("1")) {
            HttpUrls.isLogin = true;
        }



        //--------------初始化videoimg.aar文件------------------

        VideoimgInitialize.initialize(this,mImagePath);

        //--------------初始化videoimg.aar文件------------------

        if (SpUtil.get("Language", "").equals("cn")) {
            changeReceiver(0);
            FilterMuisc.Language(true); //设置语言 true:cn    false:en    默认true
        } else if (SpUtil.get("Language", "").equals("en")) {
            changeReceiver(1);
            FilterMuisc.Language(false);//设置语言 true:cn    false:en    默认true
        }




        //--------------------权限处理-----------------------

String[] permissions={
        Manifest.permission.INTERNET,
        Manifest.permission.VIBRATE,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            for (int i=0;i<permissions.length;i++){
                int per = ContextCompat.checkSelfPermission(SplashActivity.this, permissions[i]);
                LogUtils.e("======per====="+per);
                if (per!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(SplashActivity.this, permissions, 123);//权限permission.READ_CONTACTS
                }
            }



        }else {
            handler.sendEmptyMessageDelayed(0, 3 * 1000);
        }




        //--------------------权限处理-----------------------

















    }

    //--------------------设置语言----------------
    public void changeReceiver(int index) {

// 获取当前Locale（包含语言信息）
        Locale curLocale = getResources().getConfiguration().locale;
        LogUtils.e("=====curLocale==========" + curLocale);
        LogUtils.e("===============" + Locale.SIMPLIFIED_CHINESE);
        if (index == 1) {
            setLang(Locale.ENGLISH);
        } else {
            setLang(Locale.SIMPLIFIED_CHINESE);
        }
    }

    private void setLang(Locale l) {
        LogUtils.e("=====Locale=====" + l);
        // 获得res资源对象
        Resources resources = getResources();
        // 获得设置对象
        Configuration config = resources.getConfiguration();
        // 获得屏幕参数：主要是分辨率，像素等。
        DisplayMetrics dm = resources.getDisplayMetrics();
        // 语言
        config.locale = l;
        resources.updateConfiguration(config, dm);
    }
//--------------------设置语言----------------


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 123:
                handler.sendEmptyMessageDelayed(0, 2 * 1000);
                break;
        }
    }
}
