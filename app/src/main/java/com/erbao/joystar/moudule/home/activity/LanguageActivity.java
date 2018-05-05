package com.erbao.joystar.moudule.home.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.base.BaseApplication;
import com.erbao.joystar.moudule.main.activity.MainActivity;
import com.erbao.joystar.moudule.start.activity.SplashActivity;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;

import java.util.Locale;

public class LanguageActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private TextView languageCn;
    private TextView languageEn;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.language));
        back = (ImageView) findViewById(R.id.back);
        languageCn = (TextView) findViewById(R.id.language_cn);
        languageEn = (TextView) findViewById(R.id.language_en);
        back.setOnClickListener(this);
        languageCn.setOnClickListener(this);
        languageEn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.language_cn:
                SpUtil.put("Language","cn");
                changeReceiver(0);
                finish();
                break;
            case R.id.language_en:
                SpUtil.put("Language","en");
                changeReceiver(1);
                finish();
                break;
            default:
                break;
        }
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
        BaseApplication.finishAll();
        startActivity(new Intent(this, SplashActivity.class));

    }
//--------------------设置语言----------------


}
