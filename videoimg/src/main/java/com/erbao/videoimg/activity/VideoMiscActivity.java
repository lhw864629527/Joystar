package com.erbao.videoimg.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.erbao.videoimg.R;
import com.erbao.videoimg.adapter.BaseAdapter;
import com.erbao.videoimg.cut_utils.AudioCodec;
import com.erbao.videoimg.cut_utils.VideoClipper;
import com.erbao.videoimg.cut_utils.combineVideo_muis;
import com.erbao.videoimg.gpufilter.SlideGpuFilterGroup;
import com.erbao.videoimg.gpufilter.basefilter.GPUImageFilter;
import com.erbao.videoimg.gpufilter.helper.MagicFilterFactory;
import com.erbao.videoimg.gpufilter.helper.MagicFilterType;
import com.erbao.videoimg.media.MediaPlayerWrapper;
import com.erbao.videoimg.media.VideoInfo;
import com.erbao.videoimg.videimg_utils.FilterMuisc;
import com.erbao.videoimg.videimg_utils.VideoimgInitialize;
import com.erbao.videoimg.views.HorizontalListView;
import com.erbao.videoimg.views.LoadingDialog;
import com.erbao.videoimg.views.VideoPreviewView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class VideoMiscActivity extends Activity {
    private VideoPreviewView videoView;
    private TextView videoFilesmuisCmple;
    private RelativeLayout videoFilesmuisRela;
    private ImageView videoFilesmuisBack;
    private ImageView videoFilesmuisBeaty;
    private ImageView videoFilesmuisFilter;
    private ImageView videoFilesmuisMuisc;
    private TextView videoFilesmuisClosefilter;
    private SeekBar videoFilesmuisVideseek;
    private SeekBar videoFilesmuisMuiscseek;
    private HorizontalListView videoFilesmuisMuisclistv;
    private TextView videoFilesmuisClosemuis;
    private MagicFilterType filterType = MagicFilterType.NONE;
    private boolean isPlaying = false;
    private boolean isDestroy;
    private boolean resumed;
    private LinearLayout videoFilesmuisLin1;
    private LinearLayout videoFilesmuisLin2;
    private HorizontalListView videoFilesmuisListv;
    private LinearLayout videoFilesmuisLin3;
    BaseAdapter adapter_muisc;
    BaseAdapter adapter_filter;
    List<Map<String, Object>> mapList_filter;
    List<Map<String, Object>> mapList_muisc;
    int startPoint;
    float videoVolume = 1, audioVolume = 1;
    ArrayList<String> srcList;
    MediaPlayer mp;
    GPUImageFilter imageFilter;
    String mp3path = "0";
    LoadingDialog loaddialog;
    String savepath;
    String path;
    File files;
    String video_new;
    String audio_new;
    String audio_viedo;
    String pcm_path1, pcm_path2;
    boolean audio_bool = false;
    boolean video_bool = false;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Executors.newSingleThreadExecutor().execute(update);
                    break;
                case 1:
                    isPlaying = true;
                    break;
                case 2:
                  /*  int curDuration = mVideoView.getCurDuration();
                    if (curDuration > startPoint + clipDur) {
                        mVideoView.seekTo(startPoint);
                        mVideoView.start();
                    }*/
                    break;
                case 3:
                    isPlaying = false;
                    break;
                case 4:
//                    Toast.makeText(PreviewActivity.this, "视频保存地址   " + outputPath, Toast.LENGTH_SHORT).show();
//                    Log.e("TAG","========视频保存地址======"+outputPath);
//                    endLoading();
//                    finish();
//                    //TODO　已经渲染完毕了　

                    break;
                case 5:

//                    videoView.setGPUImageFilter(MagicFilterFactory.initFilters(MagicFilterType.N1977));
                    videoView.setChangedtoFilterGPUImageFilter(imageFilter);
                    Log.e("TAG", "================" + mapList_filter);
                    adapter_filter.notifyDataSetChanged();
                    break;
                case 6:

                    try {
                        mp.reset();
                        mp.setDataSource(mp3path);
                        mp.setLooping(true);//设置循环播放
                        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mp.prepare();
                        mp.start();
                        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mp) {
                                mp.release();//释放音频资源
                                Log.e("TAG", "====mp3播放完成=======");

                            }
                        });

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    adapter_muisc.notifyDataSetChanged();
                    break;
                case 7:
                    mp.reset();
                    adapter_muisc.notifyDataSetChanged();
                    break;
                case 8:
                    if (audio_new != null) {
                        File file = new File(audio_new);
                        if (file.exists()) {
                            file.delete();
                        }
                    }
                    if (video_new != null) {
                        File videfile = new File(video_new);
                        if (videfile.exists()) {
                            videfile.delete();
                        }
                    }
                    if (audio_viedo != null) {
                        File accfile = new File(audio_viedo);
                        if (accfile.exists()) {
                            accfile.delete();
                        }
                    }
                    if (pcm_path1 != null) {
                        File pcmfile1 = new File(pcm_path1);
                        if (pcmfile1.exists()) {
                            pcmfile1.delete();
                        }
                    }
                    if (pcm_path2 != null) {
                        File pcmfile2 = new File(pcm_path2);
                        if (pcmfile2.exists()) {
                            pcmfile2.delete();
                        }

                    }






                    loaddialog.dismiss();
                    Intent intent = new Intent();
                    intent.putExtra("path", savepath);
                    setResult(1, intent);
                    finish();
                    break;
                case 9:
                    loaddialog.dismiss();
                    Toast.makeText(VideoMiscActivity.this, getResources().getText(R.string.video_not_support), Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 10:
                    if (mp3path.equals("0") && video_bool) {
                        mHandler.sendEmptyMessage(8);
                    } else {
                        if (audio_bool && video_bool) {
                            setVideo_muis();
                        }
                    }

                    break;
                case 11:
                    //选择的视频的本地播放地址
                    srcList = new ArrayList<>();
                    srcList.add(path);
                    videoView.setVideoPath(srcList);
                    videoView.start();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_misc);

        loaddialog = new LoadingDialog(this);
        loaddialog.setTips(getResources().getString(R.string.video_processing));

        savepath = getIntent().getStringExtra("savepath");
        File file = new File(savepath);
        files = new File(String.valueOf(file.getParentFile()));
        if (!files.exists()) {
            files.mkdirs();
        }
        Log.e("TAG", "====getPath====" + files.getPath());

        videoFilesmuisCmple = (TextView) findViewById(R.id.video_filesmuis_cmple);
        videoFilesmuisRela = (RelativeLayout) findViewById(R.id.video_filesmuis_rela);
        videoFilesmuisCmple.getBackground().setAlpha(80);
        videoFilesmuisRela.getBackground().setAlpha(80);

        videoFilesmuisLin1 = (LinearLayout) findViewById(R.id.video_filesmuis_lin1);
        videoFilesmuisLin2 = (LinearLayout) findViewById(R.id.video_filesmuis_lin2);
        videoFilesmuisListv = (HorizontalListView) findViewById(R.id.video_filesmuis_listv);
        videoFilesmuisLin3 = (LinearLayout) findViewById(R.id.video_filesmuis_lin3);
        videoFilesmuisLin2.setVisibility(View.GONE);
        videoFilesmuisLin3.setVisibility(View.GONE);

        videoFilesmuisBack = (ImageView) findViewById(R.id.video_filesmuis_back);
        videoFilesmuisBeaty = (ImageView) findViewById(R.id.video_filesmuis_beaty);
        videoFilesmuisFilter = (ImageView) findViewById(R.id.video_filesmuis_filter);
        videoFilesmuisMuisc = (ImageView) findViewById(R.id.video_filesmuis_muisc);
        videoFilesmuisClosefilter = (TextView) findViewById(R.id.video_filesmuis_closefilter);

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

        adapter_filter = new BaseAdapter(this, "VideoMiscActivity_filter", mapList_filter);
        videoFilesmuisListv.setAdapter(adapter_filter);
        videoFilesmuisListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;
                for (int i = 0; i < mapList_filter.size(); i++) {
                    mapList_filter.get(i).put("select", "0");
                }
                mapList_filter.get(pos).put("select", "1");
                imageFilter = MagicFilterFactory.initFilters((MagicFilterType) mapList_filter.get(pos).get("filter"));//修改滤镜
                filterType = (MagicFilterType) mapList_filter.get(pos).get("filter");
                mHandler.sendEmptyMessage(5);

            }
        });


        videoFilesmuisVideseek = (SeekBar) findViewById(R.id.video_filesmuis_videseek);
        videoFilesmuisMuiscseek = (SeekBar) findViewById(R.id.video_filesmuis_muiscseek);
        videoFilesmuisMuisclistv = (HorizontalListView) findViewById(R.id.video_filesmuis_muisclistv);
        videoFilesmuisClosemuis = (TextView) findViewById(R.id.video_filesmuis_closemuis);

        //---------音乐-----------------
        mapList_muisc = new ArrayList<>();
        List<String> mp3 = new ArrayList<>();
        mp3 = FilterMuisc.getMuisc(this, VideoimgInitialize.mp3path);
        for (int i = 0; i < mp3.size(); i++) {
            Map<String, Object> maps = new HashMap<>();
            maps.put("id", i);
            maps.put("run", "0");
            maps.put("mp3", mp3.get(i));
            mapList_muisc.add(maps);
        }
        adapter_muisc = new BaseAdapter(this, "VideoMiscActivity_muisc", mapList_muisc);
        videoFilesmuisMuisclistv.setAdapter(adapter_muisc);
        videoFilesmuisMuisclistv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int pos = position;
                String run = mapList_muisc.get(pos).get("run").toString();
                if (run.equals("1")) {
                    mapList_muisc.get(pos).put("run", "0");
                    mHandler.sendEmptyMessage(7);
                } else {
                    for (int i = 0; i < mapList_muisc.size(); i++) {
                        mapList_muisc.get(i).put("run", "0");
                    }
                    mapList_muisc.get(pos).put("run", "1");
                    mp3path = mapList_muisc.get(pos).get("mp3").toString();
                    mHandler.sendEmptyMessage(6);
                }


            }
        });


        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
//        mp = MediaPlayer.create(this, R.raw.summer);
        mp = new MediaPlayer();

        videoFilesmuisMuiscseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Double pro = Double.parseDouble(progress + "") / 10;
                float prosss = Float.parseFloat(pro + "");
                audioVolume = progress;
                mp.setVolume(prosss, prosss);
                Log.e("TAG", "=====progress=======" + progress + "==========" + prosss + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        videoFilesmuisVideseek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Double pro = Double.parseDouble(progress + "") / 10;
                float prosss = Float.parseFloat(pro + "");
                videoVolume = prosss;
                videoView.setVideoVolume(prosss);
                Log.e("TAG", "=====progress=======" + progress + "==========" + prosss + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //----------------------------------------------------


        videoView = (VideoPreviewView) findViewById(R.id.videoView);
//        videoView.setGPUImageFilter(MagicFilterFactory.initFilters(MagicFilterType.N1977));
//        videoView.setChangedtoFilterGPUImageFilter(new MagicN1977Filter());

        videoView.setOnFilterChangeListener(new SlideGpuFilterGroup.OnFilterChangeListener() {
            @Override
            public void onFilterChange(MagicFilterType type) {
                Log.e("TAG", "=======滤镜=======" + type);

            }
        });


        path = getIntent().getStringExtra("path");
        Log.e("TAG", "=============" + path);

        File filesd = new File(path);
        Log.e("TAG","=====filePath===123=====" + filesd.exists());
        Log.e("TAG","=====filePath===123=====" + filesd.isFile());


        try {
            //选择的视频的本地播放地址
            srcList = new ArrayList<>();
            srcList.add(path);
            videoView.setVideoPath(srcList);
        }catch (Exception e){

        }



        Log.e("TAG", "=======视频长=======" + videoView.getVideoDuration());
//        if (videoView.getVideoDuration()>15000){
//            String savevideoPath = files.getPath()+"/"+System.currentTimeMillis() + ".mp4";//裁剪保存地址
//            startActivityForResult(new Intent(VideoMiscActivity.this,VideoCutActivity.class).putExtra("path", path).putExtra("savepath", savevideoPath),102);
//            videoView.onDestroy();
//        }

        videoView.setIMediaCallback(new MediaPlayerWrapper.IMediaCallback() {
            @Override
            public void onVideoPrepare() {
                mHandler.sendEmptyMessage(0);
            }

            @Override
            public void onVideoStart() {
                mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onVideoPause() {
                mHandler.sendEmptyMessage(3);
            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.seekTo(startPoint);
                videoView.start();
            }

            @Override
            public void onVideoChanged(VideoInfo info) {

            }
        });

        //----------------点击事件------------------------
        videoFilesmuisBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
                videoView.pause();
                finish();
            }
        });
        videoFilesmuisCmple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.pause();
                videoView.pause();

                loaddialog.show();


                if (!videoFilesmuisBeaty.isSelected() && mp3path.equals("0") && filterType == MagicFilterType.NONE) {
                    savepath = path;
                    mHandler.sendEmptyMessage(8);
                } else {
                    video_new = savepath;
                    if (mp3path.equals("0")) {
                        //无添加音乐
                    } else {
                        video_new = files.getPath() + "/" + System.currentTimeMillis() + "new.mp4";
                        setvideo_muis(path, mp3path);
                    }
                    setvideo_filter(videoFilesmuisBeaty.isSelected(), path, video_new, filterType);
                }
            }
        });
        videoFilesmuisBeaty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.switchBeauty();
                if (videoFilesmuisBeaty.isSelected()) {
                    videoFilesmuisBeaty.setSelected(false);
                } else {
                    videoFilesmuisBeaty.setSelected(true);
                }
            }
        });
        videoFilesmuisFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoFilesmuisLin1.setVisibility(View.GONE);
                videoFilesmuisLin2.setVisibility(View.VISIBLE);
            }
        });
        videoFilesmuisMuisc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoFilesmuisLin1.setVisibility(View.GONE);
                videoFilesmuisLin3.setVisibility(View.VISIBLE);
            }
        });
        videoFilesmuisClosefilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoFilesmuisLin1.setVisibility(View.VISIBLE);
                videoFilesmuisLin2.setVisibility(View.GONE);
            }
        });
        videoFilesmuisClosemuis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoFilesmuisLin1.setVisibility(View.VISIBLE);
                videoFilesmuisLin3.setVisibility(View.GONE);
            }
        });

    }


    private Runnable update = new Runnable() {
        @Override
        public void run() {
            while (!isDestroy) {
                if (!isPlaying) {
                    try {
                        Thread.currentThread().sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                mHandler.sendEmptyMessage(2);
                try {
                    Thread.currentThread().sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (resumed) {
            videoView.start();
        }
        resumed = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
        isDestroy = true;
        videoView.onDestroy();
    }


    //视频音频+音乐
    public void setvideo_muis(final String path, final String mp3path) {
        new Thread() {
            @Override
            public void run() {
                super.run();

                //------------------分离视频的音频------------------------
                Log.e("TAG", "===== files.getPath()======" + files.getPath());
                audio_viedo = files.getPath() + "/" + System.currentTimeMillis() + ".aac";
                audio_new = files.getPath() + "/" + System.currentTimeMillis() + "new.aac";
                pcm_path1 = files.getPath() + "/" + System.currentTimeMillis() + ".pcm";
                ;
                pcm_path2 = files.getPath() + "/" + System.currentTimeMillis() + ".pcm";
                ;
                AudioCodec.getAudioFromVideo(path, audio_viedo, new AudioCodec.AudioDecodeListener() {
                    @Override
                    public void decodeOver() {
                        Log.e("Tag", "======視頻音频保存路径为======" + audio_viedo);

                        //------------------视频的音频和音乐合成------------------------
                        final long l = System.currentTimeMillis();
                        AudioCodec.audioMix(audio_viedo, mp3path, videoVolume, audioVolume, audio_new, pcm_path1, pcm_path2, new AudioCodec.AudioDecodeListener() {
                            @Override
                            public void decodeOver() {

                                long end = System.currentTimeMillis();
                                Log.e("timee", "---音频混合消耗的时间----" + (end - l));
                                Log.e("end", "---音频混合完成----" + audio_new);
                                audio_bool = true;
                                mHandler.sendEmptyMessage(10);
                            }

                            @Override
                            public void decodeFail() {
                                mHandler.sendEmptyMessage(9);
                            }
                        });
                    }

                    @Override
                    public void decodeFail() {
                        mHandler.sendEmptyMessage(9);
                    }
                });

                //------------------视频音乐的合成------------------------
            }
        }.start();

    }


    //视频+滤镜
    public void setvideo_filter(final boolean beaty, final String mPath, final String savepath, final MagicFilterType filterType) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                VideoClipper clipper = new VideoClipper();
                if (beaty) {
                    clipper.showBeauty();
                }
                clipper.setInputVideoPath(mPath);
                clipper.setFilterType(filterType);
                clipper.setOutputVideoPath(savepath);
                clipper.setOnVideoCutFinishListener(new VideoClipper.OnVideoCutFinishListener() {
                    @Override
                    public void onFinish() {
                        video_bool = true;
                        mHandler.sendEmptyMessage(10);
                    }
                });
                try {
                    clipper.clipVideo(0, videoView.getVideoDuration() * 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    //视频加音乐的合成
    public void setVideo_muis() {
        combineVideo_muis combineVideo_muis = new combineVideo_muis();
        combineVideo_muis.setOncombineVideo_muisFinishListener(new combineVideo_muis.OncombineVideo_muisFinishListener() {
            @Override
            public void onFinish() {
                Log.e("TAG", "==========合并完成============");
                mHandler.sendEmptyMessage(8);
            }
        });
        combineVideo_muis.combineVideo(video_new, audio_new, savepath);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && resultCode == 1) {
            path = data.getExtras().getString("path");
            mHandler.sendEmptyMessage(11);
        }
    }
}
