package com.erbao.joystar.moudule.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.qzone.activity.QzoneActivity;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;

import static com.erbao.joystar.okhttp.HttpUrls.isLogin;


/**
 * Created by asus on 2018/1/22.
 */

public class HomeFragment_left extends Fragment implements View.OnClickListener {
    Activity activity;
    private ImageView homeLeftImg;
    private LinearLayout homeLeftSetup;
    private LinearLayout homeLeftQzone;
    private TextView homeLeftName;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (isLogin) {
                        homeLeftQzone.setVisibility(View.VISIBLE);
                        GlideImageLoader.showCircle(activity, SpUtil.get("user_photo","").toString(), homeLeftImg, R.mipmap.circle_bg_img);
                        homeLeftName.setText("");
                    } else {
                        homeLeftImg.setImageResource(R.mipmap.circle_bg_img);
                        homeLeftQzone.setVisibility(View.GONE);
                        homeLeftName.setText("");
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment_left, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        homeLeftImg = (ImageView) activity.findViewById(R.id.home_left_img);


        homeLeftSetup = (LinearLayout) activity.findViewById(R.id.home_left_setup);
        homeLeftQzone = (LinearLayout) activity.findViewById(R.id.home_left_qzone);
        homeLeftName = (TextView) activity.findViewById(R.id.home_left_name);
        homeLeftName.setText("");
        homeLeftSetup.setOnClickListener(this);
        homeLeftImg.setOnClickListener(this);
        homeLeftQzone.setOnClickListener(this);

        if (isLogin) {
            homeLeftQzone.setVisibility(View.VISIBLE);
            GlideImageLoader.showCircle(activity, SpUtil.get("user_photo","").toString(), homeLeftImg, R.mipmap.circle_bg_img);
            homeLeftName.setText("");
        } else {
            homeLeftQzone.setVisibility(View.GONE);
            homeLeftName.setText("");
        }


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_left_setup:
                startActivity(new Intent(activity, SetupActivity.class));
                break;
            case R.id.home_left_img:

                if (isLogin) {
                    startActivity(new Intent(activity, MyProfileActivity.class));
                } else {
                    startActivityForResult(new Intent(activity, LoginActivity.class), 123);
                }
                break;
            case R.id.home_left_qzone:
                startActivity(new Intent(activity, QzoneActivity.class).putExtra("userId",SpUtil.get("user_id","").toString()));
                break;
            default:
                break;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.sendEmptyMessage(0);
        LogUtils.e("=====onPause=========");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == 1) {
            handler.sendEmptyMessage(0);
        }
    }
}
