package com.erbao.joystar.moudule.qzone.activity;

import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;
import com.erbao.videoimg.media.MediaPlayerWrapper;
import com.erbao.videoimg.media.VideoInfo;
import com.erbao.videoimg.views.VideoPreviewView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.utils.Log;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class QzoneSendVideActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private ImageView qzonesendvidoImg;
    private TextView qzonesendvidoPermiss;
    private LinearLayout qzonesendvidoLin;

    private EditText qzonesendvidoEdt;
    private ImageView qzonesendvidoSelect;
    CustomProgressDialog dialog;
    String filePath;
    private TextView save;
    int startPoint;
    VideoPreviewView videoView;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(QzoneSendVideActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    finish();
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzone_send_vide);


        title = (TextView) findViewById(R.id.title);
        title.setText("");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        qzonesendvidoImg = (ImageView) findViewById(R.id.qzonesendvido_img);
        qzonesendvidoPermiss = (TextView) findViewById(R.id.qzonesendvido_permiss);
        qzonesendvidoLin = (LinearLayout) findViewById(R.id.qzonesendvido_lin);

        qzonesendvidoEdt = (EditText) findViewById(R.id.qzonesendvido_edt);
        qzonesendvidoSelect = (ImageView) findViewById(R.id.qzonesendvido_select);
        save = (TextView) findViewById(R.id.save);
        save.setVisibility(View.VISIBLE);
        save.setText(getResources().getString(R.string.upload));
        save.setOnClickListener(this);
        qzonesendvidoPermiss.setOnClickListener(this);
        qzonesendvidoSelect.setOnClickListener(this);
        GlideImageLoader.showCircle(this, SpUtil.get("user_photo", "").toString(), qzonesendvidoImg, R.mipmap.circle_bg_img);
        dialog = new CustomProgressDialog(this, R.style.dialog);

        filePath = getIntent().getStringExtra("filePath");
//        filePath = Environment.getExternalStorageDirectory() + "/mgb/123456.mp4";
        LogUtils.e("=====filePath========" + filePath);

        if (filePath.length() < 1) {
            qzonesendvidoLin.setVisibility(View.GONE);
        } else {
            qzonesendvidoSelect.setVisibility(View.GONE);
        }




        videoView = (VideoPreviewView) findViewById(R.id.videoView);
        //选择的视频的本地播放地址
        ArrayList<String> srcList = new ArrayList<>();
        srcList.add(filePath);
        videoView.setVideoPath(srcList);
        videoView.setIMediaCallback(new MediaPlayerWrapper.IMediaCallback() {
            @Override
            public void onVideoPrepare() {

            }

            @Override
            public void onVideoStart() {

            }

            @Override
            public void onVideoPause() {

            }

            @Override
            public void onCompletion(android.media.MediaPlayer mp) {
                videoView.seekTo(startPoint);
                videoView.start();
            }

            @Override
            public void onVideoChanged(VideoInfo info) {

            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                videoView.onDestroy();
                finish();
                break;
            case R.id.save:
                String text = qzonesendvidoEdt.getText().toString();
                Map<String, Object> map = new HashMap<>();
                map.put("userId", SpUtil.get("user_id", ""));
                map.put("text", text);
                File file = new File(filePath);
                List<File> files = new ArrayList<>();
                files.add(file);
                GetData(HttpUrls.saveVideo, map, files, "dynamicVideo");
                dialog.show();

                LogUtils.e("===========" + files);
                LogUtils.e("===========" + HttpUrls.saveVideo);


                break;
            case R.id.qzonesendvido_permiss:

                break;
            case R.id.qzonesendvido_select:

                break;
            default:
                break;
        }
    }


    String msgg;

    public void GetData(String url, Map<String, Object> map, List<File> files, String Filename) {
        OkhttpUtils.getInstance(this).sendMultipart(url, map, files, Filename, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {

                        msgg = object.getString("promptMessage") + "";
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(1);
                    } else {
                        msgg = object.getString("promptMessage") + "";
                        handler.sendEmptyMessage(0);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFail(Object tag, String msg) {
                LogUtils.e("=========" + msg);
                msgg = getResources().getText(R.string.Serverexception) + "";
                handler.sendEmptyMessage(0);
            }
        });


    }

}
