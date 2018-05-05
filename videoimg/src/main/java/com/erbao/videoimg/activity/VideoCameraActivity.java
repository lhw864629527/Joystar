package com.erbao.videoimg.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.erbao.videoimg.R;
import com.erbao.videoimg.adapter.BaseAdapter;

import com.erbao.videoimg.camera.SensorControler;
import com.erbao.videoimg.gpufilter.SlideGpuFilterGroup;
import com.erbao.videoimg.gpufilter.basefilter.GPUImageFilter;
import com.erbao.videoimg.gpufilter.helper.MagicFilterFactory;
import com.erbao.videoimg.gpufilter.helper.MagicFilterType;
import com.erbao.videoimg.videimg_utils.FilterMuisc;
import com.erbao.videoimg.videimg_utils.VideoimgInitialize;
import com.erbao.videoimg.views.CameraView;
import com.erbao.videoimg.views.CircularProgressView;
import com.erbao.videoimg.views.FocusImageView;
import com.erbao.videoimg.views.HorizontalListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoCameraActivity extends Activity {
    private CameraView videoCameraView;
    private ImageView videoCameraBack;
    private ImageView videoCameraCame;
    private ImageView videoCameraBeauty;
    private CircularProgressView videoCameraCapyure;
    private ImageView videoCameraFilter;
    private FocusImageView videoCameraFocus;
    ExecutorService executorService;
    private SensorControler mSensorControler;
    private LinearLayout videoCameraLin;
    private SeekBar videoCameraSeek;
    private HorizontalListView videoCameraListv;
    public static int maxTime = 20000;//最长录制20s
    private boolean recordFlag = false;//是否正在录制
    private boolean pausing = false;
    long timeCount = 0;//用于记录录制时间
    private boolean autoPausing = false;
    private long timeStep = 50;//进度条刷新的时间
    String path;
    boolean Lin_bool = false, listv_bool = false;
    List<Map<String, Object>> mapList_filter;
    BaseAdapter adapter_filter;
    private LinearLayout videoCameraLinbg;
    GPUImageFilter imageFilter;
    int CameraSeek = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    videoCameraView.setChangedtoFilterGPUImageFilter(imageFilter);
                    adapter_filter.notifyDataSetChanged();
                    break;
                case 1:
                    recordFlag = false;
                    videoCameraCapyure.setProcess(0);

                    Intent intent = new Intent();
                    intent.putExtra("path", path);
                    setResult(1, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_camera);

        int time = Integer.parseInt(getIntent().getStringExtra("time"));
        if (time > 1) {
            maxTime = time;
        }
        path = getIntent().getStringExtra("path");//录制保存地址
        File file = new File(path);
        File files = new File(String.valueOf(file.getParentFile()));
        if (!files.exists()) {
            files.mkdirs();
        }





        videoCameraView = (CameraView) findViewById(R.id.video_camera_view);
        videoCameraBack = (ImageView) findViewById(R.id.video_camera_back);
        videoCameraCame = (ImageView) findViewById(R.id.video_camera_came);
        videoCameraBeauty = (ImageView) findViewById(R.id.video_camera_beauty);
        videoCameraCapyure = (CircularProgressView) findViewById(R.id.video_camera_capyure);
        videoCameraFilter = (ImageView) findViewById(R.id.video_camera_filter);
        videoCameraFocus = (FocusImageView) findViewById(R.id.video_camera_focus);
        videoCameraCapyure.setTotal(maxTime);////录制时间20s

        videoCameraLin = (LinearLayout) findViewById(R.id.video_camera_lin);
        videoCameraSeek = (SeekBar) findViewById(R.id.video_camera_seek);
        videoCameraListv = (HorizontalListView) findViewById(R.id.video_camera_listv);
        videoCameraLin.setVisibility(View.GONE);
        videoCameraListv.setVisibility(View.GONE);

        videoCameraLinbg = (LinearLayout) findViewById(R.id.video_camera_linbg);
        videoCameraLinbg.getBackground().setAlpha(0);


        executorService = Executors.newSingleThreadExecutor();
        mSensorControler = SensorControler.getInstance();
        mSensorControler.setCameraFocusListener(new SensorControler.CameraFocusListener() {
            @Override
            public void onFocus() {
                if (videoCameraView.getCameraId() == 1) {
                    return;
                }
                Point point = new Point(VideoimgInitialize.screenWidth / 2, VideoimgInitialize.screenHeight / 2);
                videoCameraView.onFocus(point, callback);
            }
        });


        videoCameraSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("TAG", "=============" + progress);
                CameraSeek = progress;
                videoCameraView.changeBeautyLevel(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        //---------滤镜-----------------
        mapList_filter = new ArrayList<>();
        List<Map<String, Object>> mapfilter = FilterMuisc.getFilter();
        for (int i = 0; i < mapfilter.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", i);
            map.put("select", "0");
            map.put("filter", mapfilter.get(i).get("filter"));
            map.put("str_en", mapfilter.get(i).get("str_en"));
            map.put("str_cn", mapfilter.get(i).get("str_cn"));
            mapList_filter.add(map);
        }

        adapter_filter = new BaseAdapter(this, "VideoCameraActivity_filter", mapList_filter);
        videoCameraListv.setAdapter(adapter_filter);
        videoCameraListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;
                for (int i = 0; i < mapList_filter.size(); i++) {
                    mapList_filter.get(i).put("select", "0");
                }
                mapList_filter.get(pos).put("select", "1");
                imageFilter = MagicFilterFactory.initFilters((MagicFilterType) mapList_filter.get(pos).get("filter"));//修改滤镜
                handler.sendEmptyMessage(0);
            }
        });


        videoCameraView.setOnFilterChangeListener(new SlideGpuFilterGroup.OnFilterChangeListener() {
            @Override
            public void onFilterChange(MagicFilterType type) {

                Log.e("TAG", "=======滤镜=======" + type);

            }
        });
        videoCameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                videoCameraView.onTouch(event);
                if (videoCameraView.getCameraId() == 1) {
                    return false;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        float sRawX = event.getRawX();
                        float sRawY = event.getRawY();
                        float rawY = sRawY * VideoimgInitialize.screenWidth / VideoimgInitialize.screenHeight;
                        float temp = sRawX;
                        float rawX = rawY;
                        rawY = (VideoimgInitialize.screenWidth - temp) * VideoimgInitialize.screenHeight / VideoimgInitialize.screenWidth;

                        Point point = new Point((int) rawX, (int) rawY);
                        videoCameraView.onFocus(point, callback);
                        videoCameraFocus.startFocus(new Point((int) sRawX, (int) sRawY));
                }
                return true;
            }
        });


        //----------------点击事件------------------------
        videoCameraBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        videoCameraCame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoCameraView.switchCamera();
                if (videoCameraView.getCameraId() == 1) {
                    //前置摄像头 使用美颜
                    videoCameraView.changeBeautyLevel(3);
                } else {
                    //后置摄像头不使用美颜
                    videoCameraView.changeBeautyLevel(3);
                }
            }
        });
        videoCameraBeauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Lin_bool) {
                    Lin_bool = false;
                    videoCameraLin.setVisibility(View.GONE);
                    if (CameraSeek == 0) {
                        videoCameraBeauty.setImageResource(R.mipmap.beauty_off);
                    }
                    videoCameraLinbg.getBackground().setAlpha(0);
                } else {
                    videoCameraLinbg.getBackground().setAlpha(80);
                    Lin_bool = true;
                    videoCameraLin.setVisibility(View.VISIBLE);
                    videoCameraBeauty.setImageResource(R.mipmap.beauty);
                    if (listv_bool) {
                        listv_bool = false;
                        videoCameraListv.setVisibility(View.GONE);
                    }
                }
            }
        });
        videoCameraFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listv_bool) {
                    listv_bool = false;
                    videoCameraListv.setVisibility(View.GONE);
                    videoCameraLinbg.getBackground().setAlpha(0);
                } else {
                    listv_bool = true;
                    videoCameraListv.setVisibility(View.VISIBLE);
                    videoCameraLinbg.getBackground().setAlpha(80);
                    if (Lin_bool) {
                        Lin_bool = false;
                        videoCameraLin.setVisibility(View.GONE);
                        if (CameraSeek == 0) {
                            videoCameraBeauty.setImageResource(R.mipmap.beauty_off);
                        }
                    }
                }
            }
        });
        videoCameraCapyure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoCameraLin.setVisibility(View.GONE);
                videoCameraListv.setVisibility(View.GONE);

                if (!recordFlag) {
                    executorService.execute(recordRunnable);
                } else if (!pausing) {
                    videoCameraView.pause(false);
                    pausing = true;
                } else {
                    videoCameraView.resume(false);
                    pausing = false;
                }
            }
        });


    }



    Camera.AutoFocusCallback callback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            //聚焦之后根据结果修改图片
            Log.e("hero", "----onAutoFocus====" + success);
            if (success) {
                videoCameraFocus.onFocusSuccess();
            } else {
                //聚焦失败显示的图片
                videoCameraFocus.onFocusFailed();

            }
        }
    };
    Runnable recordRunnable = new Runnable() {
        @Override
        public void run() {
            recordFlag = true;
            pausing = false;
            autoPausing = false;
            timeCount = 0;
            long time = System.currentTimeMillis();
//            String savePath = SyncStateContract.Constants.getPath("record/", time + ".mp4");
            String savePath = path;

            try {
                videoCameraView.setSavePath(savePath);
                videoCameraView.startRecord();
                while (timeCount <= maxTime && recordFlag) {
                    if (pausing || autoPausing) {
                        continue;
                    }
                    videoCameraCapyure.setProcess((int) timeCount);
                    Thread.sleep(timeStep);
                    timeCount += timeStep;
                }
                recordFlag = false;
                videoCameraView.stopRecord();
                if (timeCount < 2000) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(VideoCameraActivity.this, getResources().getText(R.string.video_short), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    videoCameraView.stopRecord();
                    videoCameraCapyure.setProcess(0);
                    Log.e("TAG", "======录制完成，文件保存路径========" + savePath);
                    handler.sendEmptyMessageDelayed(1,1*1000);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        videoCameraView.onResume();
        if (recordFlag && autoPausing) {
            videoCameraView.resume(true);
            autoPausing = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (recordFlag && !pausing) {
            videoCameraView.pause(true);
            autoPausing = true;
        }
        videoCameraView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
