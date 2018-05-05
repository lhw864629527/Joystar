//package com.erbao.joystar.moudule.qzone.activity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.erbao.joystar.R;
//import com.erbao.joystar.moudule.qzone.adapter.QzoneBaseAdapter;
//import com.erbao.joystar.okhttp.HttpUrls;
//import com.erbao.joystar.utils.LogUtils;
//import com.erbao.joystar.views.HorizontalListView;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import jp.co.cyberagent.android.gpuimage.GPUImage;
//import jp.co.cyberagent.android.gpuimage.GPUImage3x3ConvolutionFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageAlphaBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageBilateralFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageBrightnessFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageChromaKeyBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageColorDodgeBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageDissolveBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageEmbossFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageHazeFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageMultiplyBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageNormalBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageOverlayBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSepiaFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSketchFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageSoftLightBlendFilter;
//import jp.co.cyberagent.android.gpuimage.GPUImageToonFilter;
//
//import static android.R.id.progress;
//import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.getBitmapDegree;
//import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.getBitmapFormUri;
//import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.rotateBitmapByDegree;
//import static com.erbao.joystar.okhttp.HttpUrls.QzoneSendnewslist;
//
//public class BitmapFilterActivity extends AppCompatActivity implements View.OnClickListener {
//    private TextView title;
//    private ImageView back;
//    private ImageView bitmapfilterImg;
//    private LinearLayout bitmapfilterLin;
//    private RelativeLayout lin;
//    private TextView save;
//public  static  List<GPUImageFilter> filters;
//    String type="";
//    //-------------滤镜效果---------------------
//    private GPUImage gpuImage;
//    Bitmap newBitmap,newBitmapto;
//    //-------------滤镜效果---------------------
//    private HorizontalListView bitmapfilterListv;
//
//    Handler handler=new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case 0:
//                    //显示处理后的图片
//                    bitmapfilterImg.setImageBitmap(newBitmapto);
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bitmap_filter);
//
//
//
//
//        lin = (RelativeLayout) findViewById(R.id.lin);
//        save = (TextView) findViewById(R.id.save);
//        lin.getBackground().setAlpha(80);
//        save.setText(getResources().getString(R.string.confirm));
//        save.setTextColor(getResources().getColor(R.color.color_Blackfont));
//        save.setVisibility(View.VISIBLE);
//        save.setOnClickListener(this);
//        title = (TextView) findViewById(R.id.title);
//        title.setText("");
//        back = (ImageView) findViewById(R.id.back);
//        back.setOnClickListener(this);
//        bitmapfilterImg = (ImageView) findViewById(R.id.bitmapfilter_img);
//        bitmapfilterLin = (LinearLayout) findViewById(R.id.bitmapfilter_lin);
//        bitmapfilterLin.getBackground().setAlpha(80);
//
//
//        type=getIntent().getStringExtra("type");//QzoneSendnewsActivity  //QzoneActivity
//
//        //---------------------获取图片显示-----------------------------
//        String filePath = getIntent().getStringExtra("filePath");
//        LogUtils.e("========="+filePath);
//        File file = new File(filePath);
//        Bitmap photoBmp = null;
//        try {
//            photoBmp = getBitmapFormUri(this, Uri.fromFile(file));//通过uri获取图片并进行压缩
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        int degree = getBitmapDegree(file.getAbsolutePath());//读取图片的旋转的角度
//        System.out.println("-======degree=======" + degree);
//        if (degree == 0) {
//            bitmapfilterImg.setImageBitmap(photoBmp);
//            newBitmap=photoBmp;
//        } else {
//            /**
//             * 把图片旋转为正的方向
//             */
//            Bitmap newbitmap = rotateBitmapByDegree(photoBmp, degree);//将图片按照某个角度进行旋转
//            newBitmap=newbitmap;
//            bitmapfilterImg.setImageBitmap(newbitmap);
//        }
//
//
//        //---------------------获取图片显示-----------------------------
//
//
//
//
//
//
//        bitmapfilterListv = (HorizontalListView) findViewById(R.id.bitmapfilter_listv);
//
//        List<Map<String,Object>> mapList=new ArrayList<>();
//        mapList=getlist();
//        QzoneBaseAdapter adapter=new QzoneBaseAdapter(this,"BitmapFilterActivity",mapList);
//        bitmapfilterListv.setAdapter(adapter);
//        bitmapfilterListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position==0){
//                    newBitmapto=newBitmap;
//                }else {
//                    gpuImage = new GPUImage(BitmapFilterActivity.this);
//                    gpuImage.setImage(newBitmap);
//                    gpuImage.setFilter(filters.get(position));//设置滤镜效果
//                    newBitmapto = gpuImage.getBitmapWithFilterApplied();
//                }
//
//               handler.sendEmptyMessage(0);
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
//            case R.id.save://图片滤镜效果，修改setFilter中的参数就可
//                // 使用GPUImage处理图像
//                LogUtils.e("=====保存。。。。======");
//                File file;
//                if (newBitmapto==null){
//                    file=saveImage(newBitmap);
//                }else {
//                    file=saveImage(newBitmapto);
//                }
//
//                List<Map<String,Object>> mapList;
//                if (QzoneSendnewslist==null){
//                    QzoneSendnewslist=new ArrayList<>();
//                }
//                mapList=QzoneSendnewslist;
//                Map<String,Object> map=new HashMap<>();
//                map.put("img",file.getAbsolutePath());
//                map.put("text","");
//                mapList.add(map);
//
//                if (type.equals("QzoneSendnewsActivity")){
//
//                }else {
//                    startActivity(new Intent(BitmapFilterActivity.this,QzoneSendnewsActivity.class));
//                }
//                finish();
//                break;
//            default:
//                break;
//        }
//    }
//    //保存到本地：
//    public File saveImage(Bitmap bmp) {
//        File appDir = new File(Environment.getExternalStorageDirectory(), "Yoystar");
//        if (!appDir.exists()) {
//            appDir.mkdirs();
//        }
//        String fileName = System.currentTimeMillis() + ".jpg";
////        String fileName = "vkahead.jpg";
//        File file = new File(appDir, fileName);
//        try {
//            FileOutputStream fos = new FileOutputStream(file);
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//            fos.flush();
//            fos.close();
//            LogUtils.e("=====保存成功======");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return file;
//    }
//
//    public  List<Map<String,Object>> getlist(){
//        List<Map<String,Object>> mapList=new ArrayList<>();
//        filters=new ArrayList<>();
////        String[] text={"none","beauty","village","elegance","afternoon","1977","mint","moon","gray","fading","","","",""};
////        String[] text={"原图","美颜","乡村","优雅","午后","1977","薄荷","月光","灰","褪色","0.2f","素描","减淡","浮雕","卡通"};
//        String[] text={getResources().getString(R.string.none),getResources().getString(R.string.beauty),getResources().getString(R.string.village),
//                getResources().getString(R.string.elegance),getResources().getString(R.string.afternoon),"1977",getResources().getString(R.string.mint),
//                getResources().getString(R.string.moon),getResources().getString(R.string.gray),getResources().getString(R.string.fading),"0.2f",
//                getResources().getString(R.string.sketch),getResources().getString(R.string.dodge),getResources().getString(R.string.relief),
//                getResources().getString(R.string.cartoon)};
//
//        for (int i=0;i<text.length;i++){
//            Map<String,Object> map=new HashMap<>();
//            map.put("text",text[i]);
//            map.put("id",i+"");
//            mapList.add(map);
//        }
//
//        filters.add(new GPUImageBrightnessFilter(0f));//原图
////        filters.add(new GPUImageBeautifyFilter);//美颜
////        filters.add(new FWNashvilleFilter);//乡村
////        filters.add(new GPUImageSoftEleganceFilter());//优雅
////        filters.add(new FWLordKelvinFilter());//午后
////        filters.add(new FW1977Filter());//1977
////        filters.add(new GPUImageMissEtikateFilter());//薄荷
////        filters.add(new GPUImageAmatorkaFilter());//月光
//
//        filters.add(new GPUImageBilateralFilter());//美颜
//        filters.add(new GPUImageBrightnessFilter(-0.2f));//乡村
//        filters.add(new GPUImageSoftLightBlendFilter());//优雅
//        filters.add(new GPUImageChromaKeyBlendFilter());//午后
//        filters.add(new GPUImageAlphaBlendFilter());//1977
//        filters.add(new GPUImageOverlayBlendFilter());//薄荷
//        filters.add(new GPUImageAlphaBlendFilter());//月光
//        filters.add(new GPUImageGrayscaleFilter());//灰
//
//        filters.add(new GPUImageSepiaFilter());//褪色
//
//        filters.add(new GPUImageBrightnessFilter(0.2f));
//        filters.add(new GPUImageSketchFilter());
//        filters.add(new GPUImageColorDodgeBlendFilter());
//        filters.add(new GPUImageEmbossFilter());
//        filters.add(new GPUImageToonFilter());
////        filters.add(new GPUImageNormalBlendFilter());
////        filters.add(new GPUImageNormalBlendFilter());
////        filters.add(new GPUImageNormalBlendFilter());
////        filters.add(new GPUImageNormalBlendFilter());
////        filters.add(new GPUImageNormalBlendFilter());
////        filters.add(new GPUImageNormalBlendFilter());
//
//
////        1. GPUImageBeautifyFilter
////        2.FWNashvilleFilter
////        3.GPUImageSoftEleganceFilter
////        4.FWLordKelvinFilter
////        5.FW1977Filter
////        6.GPUImageMissEtikateFilter
////        7.GPUImageAmatorkaFilter
////        8. GPUImageGrayscaleFilter
////        9.GPUImageSepiaFilter    setIntensity:0.6  //设定值
//
//
//
//
//
//
//    return  mapList;
//    }
//}
