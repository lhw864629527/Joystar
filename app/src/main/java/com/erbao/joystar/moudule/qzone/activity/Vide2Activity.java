package com.erbao.joystar.moudule.qzone.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.erbao.joystar.R;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;

public class Vide2Activity extends AppCompatActivity {
    private VideoView video2Vide;
    CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vide2);

        video2Vide = (VideoView) findViewById(R.id.video2_vide);

        customProgressDialog = new CustomProgressDialog(this, R.style.dialog);
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();



        String filePath = getIntent().getStringExtra("img_url");
        LogUtils.e("============" + filePath);
        //网络地址
        video2Vide.setVideoURI(Uri.parse(filePath));
        //开始播放
        video2Vide.start();

        //设置相关的监听
        //播放准备
        video2Vide.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                customProgressDialog.dismiss();
            }
        });
        //播放错误
        video2Vide.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ToastUtils.show(Vide2Activity.this,getResources().getString(R.string.errvoide));
                finish();
                return false;
            }
        });
        //播放结束
        video2Vide.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });


        MediaController controller=new MediaController(this);
        video2Vide.setMediaController(controller);

    }
}
