package com.erbao.joystar.moudule.qzone.activity;

import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class VideActivity extends AppCompatActivity implements View.OnClickListener{
    private VideoView video;
    private ImageView videPlay;
    private LinearLayout videLin;
    private SeekBar videProgre;
    private ImageView videStop;
    private TextView videTime;
    private RelativeLayout videRela;
    boolean payf = true;
    CustomProgressDialog customProgressDialog;
    CountDownTimer timer;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    videProgre.setProgress((int) video.getCurrentPosition());
                    videProgre.setSecondaryProgress(video.getBufferPercentage());
                    videTime.setText(gettime(video.getCurrentPosition()));
                    timer.onTick(video.getBufferPercentage());
                    break;
                case 1:
                    if (payf) {
                        videLin.setVisibility(View.GONE);
                        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
                    }

                    break;
                case 2:

                    /** 倒计时60秒，一次1秒 */
                    timer = new CountDownTimer(video.getDuration(),1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                            handler.sendEmptyMessage(0);
                        }

                        @Override
                        public void onFinish() {
                            video.stopPlayback();
                            videPlay.setVisibility(View.VISIBLE);
                            videStop.setImageResource(R.mipmap.vide_stop);
                            videLin.setVisibility(View.VISIBLE);
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
                        }
                    }.start();

                    break;
                default:
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_vide);


        Vitamio.initialize(this);//初始化视频框架

        String filePath = getIntent().getStringExtra("img_url");
        LogUtils.e("============" + filePath);
        video = (VideoView) findViewById(R.id.video);
        video.setVideoURI(Uri.parse(filePath));

        MediaController mediaController = new MediaController(this);
        video.setMediaController(mediaController);
        mediaController.show();
        video.start();


        videTime = (TextView) findViewById(R.id.vide_time);
        videPlay = (ImageView) findViewById(R.id.vide_play);
        videLin = (LinearLayout) findViewById(R.id.vide_lin);
        videProgre = (SeekBar) findViewById(R.id.vide_progre);
        videStop = (ImageView) findViewById(R.id.vide_stop);
        videLin.getBackground().setAlpha(30);
        videPlay.setOnClickListener(this);
        videStop.setOnClickListener(this);
        videRela = (RelativeLayout) findViewById(R.id.vide_rela);
        videRela.setOnClickListener(this);
        videPlay.setVisibility(View.GONE);
        customProgressDialog = new CustomProgressDialog(this, R.style.dialog);
        customProgressDialog.setCancelable(false);
        customProgressDialog.show();

        video.requestFocus();


//在视频预处理完成后调用
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                customProgressDialog.dismiss();
//                videLin.setVisibility(View.GONE);
//3.56
                LogUtils.e("=====视频时长=======" + video.getDuration());
                videProgre.setMax((int) video.getDuration());
                handler.sendEmptyMessage(2);

            }
        });
        //视频播放完成后调用
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                LogUtils.e("======播放完成==========" + mp.getDuration());
                LogUtils.e("======播放完成==========" + mp.getCurrentPosition());
                video.stopPlayback();
                videPlay.setVisibility(View.VISIBLE);
                videLin.setVisibility(View.VISIBLE);
            }
        });
        //视频打开失败
        video.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                LogUtils.e("======播放失败==========");
                ToastUtils.show(VideActivity.this,"视频错误，无法播放！");
                video.stopPlayback();
                videPlay.setVisibility(View.VISIBLE);
                videLin.setVisibility(View.VISIBLE);
                return false;
            }
        });

        videProgre.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                video.seekTo((long)progress);
                LogUtils.e("====progress====" + progress);
//                LogUtils.e("============="+progress);
//                video.start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                LogUtils.e("====onStartTrackingTouch====");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                LogUtils.e("====onStopTrackingTouch====" + seekBar.getProgress());
                video.seekTo((long) seekBar.getProgress());
            }
        });
        handler.sendMessageDelayed(handler.obtainMessage(1), 3000);





    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vide_play:
                video.start();
                videPlay.setVisibility(View.GONE);
                videStop.setImageResource(R.mipmap.vide_plays);
                handler.sendMessageDelayed(handler.obtainMessage(1), 3000);
                payf = true;
                timer.start(); //開始
                break;
            case R.id.vide_rela:
                videLin.setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
                handler.sendMessageDelayed(handler.obtainMessage(1), 6000);
                break;
            case R.id.vide_stop:
                if (payf) {
                    payf = false;
                    video.pause();
                    videStop.setImageResource(R.mipmap.vide_suspendeds);
                    videPlay.setVisibility(View.VISIBLE);
                    timer.cancel(); //停止
                } else {
                    payf = true;
                    video.start();
                    videStop.setImageResource(R.mipmap.vide_plays);
                    videPlay.setVisibility(View.GONE);
                    handler.sendMessageDelayed(handler.obtainMessage(1), 3000);
                    timer.start(); //開始
                }
                break;
            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.stopPlayback();
    }


    public String gettime(long s) {
        String res;
        res = s / (3600 * 1000) + ":" + s % (3600 * 1000) / (60 * 1000) + ":" + s % (3600 * 1000) % (60 * 1000) / 1000;
        return res;
    }
}
