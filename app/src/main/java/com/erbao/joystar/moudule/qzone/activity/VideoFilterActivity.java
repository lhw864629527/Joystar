//package com.erbao.joystar.moudule.qzone.activity;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.erbao.joystar.R;
//import com.erbao.joystar.moudule.qzone.adapter.QzoneBaseAdapter;
//import com.erbao.joystar.moudule.qzone.videoview.ExtractDecodeEditEncodeMuxTest;
//import com.erbao.joystar.moudule.qzone.videoview.GPUImageExtTexFilter;
//import com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools;
//import com.erbao.joystar.moudule.qzone.videoview.VideoSurfaceView;
//import com.erbao.joystar.utils.LogUtils;
//import com.erbao.joystar.views.CustomProgressDialog;
//import com.erbao.joystar.views.HorizontalListView;
//
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import android.media.MediaPlayer;
//
//import io.vov.vitamio.Vitamio;
//import io.vov.vitamio.widget.MediaController;
//import io.vov.vitamio.widget.VideoView;
//import jp.co.cyberagent.android.gpuimage.GPUImage;
//import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
//import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageView;
//
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BILATERAL_BLUR;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BLEND_ALPHA;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BLEND_CHROMA_KEY;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BLEND_COLOR_DODGE;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BLEND_OVERLAY;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BLEND_SOFT_LIGHT;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.BRIGHTNESS;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.EMBOSS;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.GRAYSCALE;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.SEPIA;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.SKETCH;
//import static com.erbao.joystar.moudule.qzone.videoview.GPUImageFilterTools.FilterType.TOON;
//
//
//public class VideoFilterActivity extends AppCompatActivity implements View.OnClickListener {
//    private TextView title;
//    private ImageView back;
//    RelativeLayout rlGlViewContainer;
//    private MediaPlayer mediaPlayer;
//    private VideoSurfaceView videoSurfaceView = null;
//
//    private GPUImageFilter mFilter;
//    private GPUImageFilterTools.FilterAdjuster mFilterAdjuster;
//    private GPUImageFilterTools.FilterType currentFilterType = SEPIA;
//    private TextView save;
//    private LinearLayout videfilterLin;
//    private HorizontalListView videfilterListv;
//    private RelativeLayout lin;
//    String mImagePath;
//    CustomProgressDialog dialog;
//    public static List<GPUImageFilter> filters;
//    public static List<GPUImageFilterTools.FilterType> filterstype;
//  public static   Handler handlersd;
//Handler handler=new Handler(){
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//        switch (msg.what){
//            case 0:
//                File videp = new File(ExtractDecodeEditEncodeMuxTest.mOutputFile);
//                startActivity(new Intent(VideoFilterActivity.this, QzoneSendVideActivity.class).putExtra("filePath", videp.getAbsolutePath()));
//                finish();
//                dialog.dismiss();
//                break;
//        }
//    }
//};
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_filter);
//
//
//        handlersd=handler;
//        title = (TextView) findViewById(R.id.title);
//        title.setText("");
//        back = (ImageView) findViewById(R.id.back);
//        back.setOnClickListener(this);
//
//        mImagePath = getIntent().getStringExtra("filePath");
////        mImagePath= Environment.getExternalStorageDirectory()+"/mgb/123456.mp4";
//
//
//        rlGlViewContainer = (RelativeLayout) findViewById(R.id.rlGlViewContainer);
//
//
//        //todo
//        File file = new File(mImagePath);
//        Log.i("LHD", "视频路径： " + file.getAbsolutePath());
//        mediaPlayer = MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));
//        mediaPlayer.setLooping(true);
//        mediaPlayer.start();
//        videoSurfaceView = new VideoSurfaceView(this, mediaPlayer);
//        videoSurfaceView.setSourceSize(mediaPlayer.getVideoWidth(), mediaPlayer.getVideoHeight());
//        RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(-1, -1);
//        rlGlViewContainer.addView(videoSurfaceView, rllp);
//
//
//        lin = (RelativeLayout) findViewById(R.id.lin);
//        lin.getBackground().setAlpha(80);
//        save = (TextView) findViewById(R.id.save);
//        videfilterLin = (LinearLayout) findViewById(R.id.videfilter_lin);
//        videfilterListv = (HorizontalListView) findViewById(R.id.videfilter_listv);
//        videfilterLin.getBackground().setAlpha(80);
//        save.setText(getResources().getString(R.string.confirm));
//        save.setTextColor(getResources().getColor(R.color.color_Blackfont));
//        save.setVisibility(View.VISIBLE);
//        save.setOnClickListener(this);
//        dialog = new CustomProgressDialog(this, R.style.dialog);
//
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        mapList = getlist();
//        QzoneBaseAdapter adapter = new QzoneBaseAdapter(this, "VideoFilterActivity", mapList);
//        videfilterListv.setAdapter(adapter);
//        videfilterListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0) {
//                    switchFilterTo(filters.get(position));
//                } else {
//                    switchFilterTo(filters.get(position));
//                    currentFilterType=filterstype.get(position);
//                }
//
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.back:
//                finish();
//                break;
//            case R.id.save://完成
//                dialog.show();
//                try {
//
////                  String  mp3Path= Environment.getExternalStorageDirectory()+"/mgb/free.mp3";
////                    File map3file=new File(mp3Path);
////                    LogUtils.e("================"+map3file.getName());
////                    LogUtils.e("================"+map3file.toString());
////                    LogUtils.e("================"+map3file.length());
////                    test.setSourceAudio(map3file.getAbsolutePath());
//
//
//                    File saveappDir = new File(Environment.getExternalStorageDirectory(), "Yoystar");//视频保存路径
//                    LogUtils.e("================正在保存。。。。");
//                    ExtractDecodeEditEncodeMuxTest test = new ExtractDecodeEditEncodeMuxTest(VideoFilterActivity.this);
//                    File file = new File(mImagePath);
//                    test.setSource(file.getAbsolutePath());
//                    test.setSourceAudio(file.getAbsolutePath());//设置音频
//                    test.setFilterType(currentFilterType);
//                    test.testExtractDecodeEditEncodeMuxAudioVideo(saveappDir.getAbsolutePath());
//                    LogUtils.e("================保存成功" + ExtractDecodeEditEncodeMuxTest.mOutputFile);
//
//
//                } catch (Throwable throwable) {
//                    throwable.printStackTrace();
//                    LogUtils.e("================保存失败");
//                }
//
//
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    public  static void  setstart(){
//        if (handlersd!=null){
//            handlersd.sendEmptyMessage(0);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        if (mediaPlayer != null) {
//            mediaPlayer.pause();
//        }
//        super.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mediaPlayer.start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//        super.onDestroy();
//    }
//
//    //熏染视频
//    private void switchFilterTo(final GPUImageFilter filter) {
//        if (mFilter == null
//                || (filter != null && !mFilter.getClass().equals(filter.getClass()))) {
//            mFilter = filter;
//            GPUImageFilterGroup filters = new GPUImageFilterGroup();
//            filters.addFilter(new GPUImageExtTexFilter());
//            filters.addFilter(mFilter);
//            videoSurfaceView.setFilter(filters);
//            mFilterAdjuster = new GPUImageFilterTools.FilterAdjuster(mFilter);
//            mFilterAdjuster.adjust(50);
//        }
//    }
//
//    public List<Map<String, Object>> getlist() {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        filters = new ArrayList<>();
//        filterstype = new ArrayList<>();
////        String[] text={"none","beauty","village","elegance","afternoon","1977","mint","moon","gray","fading","","","",""};
////        String[] text = {"原图", "美颜", "乡村", "优雅", "午后", "1977", "薄荷", "月光", "灰", "褪色", "0.2f", "素描", "减淡", "浮雕", "卡通"};
//        String[] text={getResources().getString(R.string.none),getResources().getString(R.string.beauty),getResources().getString(R.string.village),
//                getResources().getString(R.string.elegance),getResources().getString(R.string.afternoon),"1977",getResources().getString(R.string.mint),
//                getResources().getString(R.string.moon),getResources().getString(R.string.gray),getResources().getString(R.string.fading),"0.2f",
//                getResources().getString(R.string.sketch),getResources().getString(R.string.dodge),getResources().getString(R.string.relief),
//                getResources().getString(R.string.cartoon)};
//
//        for (int i = 0; i < text.length; i++) {
//            Map<String, Object> map = new HashMap<>();
//            map.put("text", text[i]);
//            map.put("id", i + "");
//            mapList.add(map);
//        }
//
//        filters.add(new GPUImageBrightnessFilter(0f));//原图
//        filterstype.add(BRIGHTNESS);
////        filters.add(new GPUImageBeautifyFilter);//美颜
////        filters.add(new FWNashvilleFilter);//乡村
////        filters.add(new GPUImageSoftEleganceFilter());//优雅
////        filters.add(new FWLordKelvinFilter());//午后
////        filters.add(new FW1977Filter());//1977
////        filters.add(new GPUImageMissEtikateFilter());//薄荷
////        filters.add(new GPUImageAmatorkaFilter());//月光
//
//        filters.add(new GPUImageBilateralFilter());//美颜
//        filterstype.add(BILATERAL_BLUR);
//
//        filters.add(new GPUImageBrightnessFilter(-0.2f));//乡村
//        filterstype.add(BRIGHTNESS);
//
//        filters.add(new GPUImageSoftLightBlendFilter());//优雅
//        filterstype.add(BLEND_SOFT_LIGHT);
//
//        filters.add(new GPUImageChromaKeyBlendFilter());//午后
//        filterstype.add(BLEND_CHROMA_KEY);
//
//        filters.add(new GPUImageAlphaBlendFilter());//1977
//        filterstype.add(BLEND_ALPHA);
//
//        filters.add(new GPUImageOverlayBlendFilter());//薄荷
//        filterstype.add(BLEND_OVERLAY);
//
//        filters.add(new GPUImageAlphaBlendFilter());//月光
//        filterstype.add(BLEND_ALPHA);
//
//        filters.add(new GPUImageGrayscaleFilter());//灰
//        filterstype.add(GRAYSCALE);
//
//        filters.add(new GPUImageSepiaFilter());//褪色
//        filterstype.add(SEPIA);
//
//        filters.add(new GPUImageBrightnessFilter(0.2f));
//        filterstype.add(BRIGHTNESS);
//
//        filters.add(new GPUImageSketchFilter());
//        filterstype.add(SKETCH);
//
//        filters.add(new GPUImageColorDodgeBlendFilter());
//        filterstype.add(BLEND_COLOR_DODGE);
//
//        filters.add(new GPUImageEmbossFilter());
//        filterstype.add(EMBOSS);
//
//        filters.add(new GPUImageToonFilter());
//        filterstype.add(TOON);
//        return mapList;
//    }
//}
