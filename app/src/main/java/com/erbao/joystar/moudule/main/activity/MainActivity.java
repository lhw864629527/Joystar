package com.erbao.joystar.moudule.main.activity;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.erbao.joystar.R;
import com.erbao.joystar.base.BaseActivity;

import com.erbao.joystar.base.BaseApplication;
import com.erbao.joystar.moudule.home.activity.HomeFragment_context;
import com.erbao.joystar.moudule.home.activity.HomeFragment_left;
import com.erbao.joystar.moudule.home.activity.HomeFragment_right;
import com.erbao.joystar.utils.LogUtils;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;


public class MainActivity extends BaseActivity {
    private DrawerLayout drawerLayout;
    private LinearLayout drawerContent;
    private LinearLayout idLeftMenu;
    private LinearLayout idRightMenu;
    final int RIGHT = 0;
    final int LEFT = 1;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        BaseApplication.addActivity(this);

        HomeFragment_context context = new HomeFragment_context();
        HomeFragment_left left = new HomeFragment_left();
        HomeFragment_right right = new HomeFragment_right();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_left, left);
        transaction.show(left).commit();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_content, context);
        transaction.show(context).commit();
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_right, right);
        transaction.show(right).commit();




    }


}
