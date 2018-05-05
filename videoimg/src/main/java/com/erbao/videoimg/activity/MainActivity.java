package com.erbao.videoimg.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.erbao.videoimg.R;
import com.erbao.videoimg.videimg_utils.FilterMuisc;

import java.io.File;

import iknow.android.utils.BaseUtils;

public class MainActivity extends Activity {
    private TextView tetxLocal;
    private TextView tetxCamera;
    private TextView tetxCut;
    String videoPath = "/storage/emulated/0/DCIM/Camera/VID20180307115135.mp4";
    String audioPath = "/storage/emulated/0/Codec/MP3/summer.mp3";
    String outfilePath = "/storage/emulated/0/Codec/cut/summer12345.mp4";
    private TextView tetxImg;
    private TextView tetxActivity;

  public static   String mp3path="/videoimg/mp3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mains);


        tetxLocal = (TextView) findViewById(R.id.tetx_locals);
        tetxCamera = (TextView) findViewById(R.id.tetx_cameras);
        tetxCut = (TextView) findViewById(R.id.tetx_cuts);
        tetxImg = (TextView) findViewById(R.id.tetx_imgs);
        tetxActivity = (TextView) findViewById(R.id.tetx_activitys);



        //--------复制assets下的MP3到sdk
        String pa = Environment.getExternalStorageDirectory().toString() + "/videoimg";
        File file = new File(pa);
        if (!file.exists()) {
            file.mkdirs();
        }

        //初始化音乐文件
        FilterMuisc.getMuisc(this,mp3path);
//设置语言 true:cn    false:en    默认true
        FilterMuisc.Language(true);
        BaseUtils.init(this);//初始化剪切计算









//编辑视频
        tetxLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频地址
//                String videoPath="/storage/emulated/0/DCIM/Camera/VID20180307115135.mp4";
//                String videoPath="/storage/emulated/0/DCIM/Camera/toutiao_iiilab (5).mp4";
//                String videoPath = "/storage/emulated/0/mgb/123456.mp4";
                String videoPath = "/storage/emulated/0/Codec/cut/1520927563126.mp4";
                String savevideoPath = "/storage/emulated/0/Codec/cut/cut/" + System.currentTimeMillis() + ".mp4";//视频保存地址
                startActivity(new Intent(MainActivity.this, VideoMiscActivity.class).putExtra("path", videoPath).putExtra("savepath", savevideoPath));
            }
        });


        File files = new File("/storage/emulated/0/Codec/cut");
        if (!files.exists()) {
            files.mkdirs();
        }

//录制视频
        tetxCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //视频保存地址
                String videoPath = "/storage/emulated/0/Codec/cut/" + System.currentTimeMillis() + ".mp4";
                startActivity(new Intent(MainActivity.this, VideoCameraActivity.class).putExtra("path", videoPath).putExtra("time", "15000"));//time录制时间1000=1s
            }
        });

//剪切视频
        tetxCut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = "/storage/emulated/0/Codec/cut/1520927563126.mp4";//视频地址
                String savevideoPath = "/storage/emulated/0/Codec/cut/cut/" + System.currentTimeMillis() + ".mp4";//裁剪保存地址
                startActivity(new Intent(MainActivity.this, VideoCutActivity.class).putExtra("path", videoPath).putExtra("savepath", savevideoPath));
            }
        });

        //图片编辑
        tetxImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoPath = "/storage/emulated/0/Huaban/Pins/1514377000645.jpg";//图片地址
                String savevideoPath = "/storage/emulated/0/Codec/cut/cut/" + System.currentTimeMillis() + ".jpg";//保存地址
                startActivity(new Intent(MainActivity.this, ImageFilterActivity.class).putExtra("path", videoPath).putExtra("savepath", savevideoPath));
            }
        });
        tetxActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MainActivity.class));
            }
        });


    }
}
