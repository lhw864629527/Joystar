package com.erbao.videoimg.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.erbao.videoimg.R;
import com.erbao.videoimg.adapter.BaseAdapter;
import com.erbao.videoimg.gpufilter.basefilter.GPUImage;
import com.erbao.videoimg.gpufilter.basefilter.GPUImageFilter;
import com.erbao.videoimg.gpufilter.helper.MagicFilterFactory;
import com.erbao.videoimg.gpufilter.helper.MagicFilterType;
import com.erbao.videoimg.videimg_utils.FilterMuisc;
import com.erbao.videoimg.views.HorizontalListView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.erbao.videoimg.Image_utils.BitmapImage.getBitmapDegree;
import static com.erbao.videoimg.Image_utils.BitmapImage.getBitmapFormUri;
import static com.erbao.videoimg.Image_utils.BitmapImage.rotateBitmapByDegree;

public class ImageFilterActivity extends Activity {
    private ImageView imgfilterImg;
    private ImageView imgfilterBack;
    private TextView imgfilterComple;
    private LinearLayout imgfilterLin;
    private HorizontalListView imgfilterListv;
    Bitmap newBitmap,newBitmapto;
    List<Map<String, Object>> mapList_filter;
    GPUImageFilter imageFilter;
    BaseAdapter adapter;
    String path,savepath;
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    GPUImage gpuImage = new GPUImage(ImageFilterActivity.this);
                    gpuImage.setImage(newBitmap);
                    gpuImage.setFilter(imageFilter);
                    imgfilterImg.setImageBitmap(gpuImage.getBitmapWithFilterApplied());
                    newBitmapto=gpuImage.getBitmapWithFilterApplied();
                    break;
                case 11:
                    newBitmapto=newBitmap;
                    imgfilterImg.setImageBitmap(newBitmapto);
                    break;
                case 2:
                    Intent intent=new Intent();
                    intent.putExtra("path",savepath);
                    setResult(1,intent);
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
        setContentView(R.layout.activity_image_filter);

        imgfilterImg = (ImageView) findViewById(R.id.imgfilter_img);
        imgfilterBack = (ImageView) findViewById(R.id.imgfilter_back);
        imgfilterComple = (TextView) findViewById(R.id.imgfilter_comple);
        imgfilterLin = (LinearLayout) findViewById(R.id.imgfilter_lin);
        imgfilterListv = (HorizontalListView) findViewById(R.id.imgfilter_listv);
        imgfilterLin.getBackground().setAlpha(20);
        imgfilterComple.getBackground().setAlpha(80);


         path=getIntent().getStringExtra("path");
         savepath=getIntent().getStringExtra("savepath");



        //---------------------获取图片显示-----------------------------
        String filePath =path;
        File file = new File(filePath);
        Bitmap photoBmp = null;
        try {
            photoBmp = getBitmapFormUri(this, Uri.fromFile(file));//通过uri获取图片并进行压缩
        } catch (IOException e) {
            e.printStackTrace();
        }
        int degree = getBitmapDegree(file.getAbsolutePath());//读取图片的旋转的角度
        System.out.println("-======degree=======" + degree);
        if (degree == 0) {
            imgfilterImg.setImageBitmap(photoBmp);
            newBitmap=photoBmp;
            newBitmapto=newBitmap;
        } else {
            /**
             * 把图片旋转为正的方向
             */
            Bitmap newbitmap = rotateBitmapByDegree(photoBmp, degree);//将图片按照某个角度进行旋转
            newBitmap=newbitmap;
            imgfilterImg.setImageBitmap(newbitmap);
            newBitmapto=newBitmap;
        }


        //---------------------获取图片滤镜显示-----------------------------

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


         adapter=new BaseAdapter(this,"ImageFilterActivity",mapList_filter);
        imgfilterListv.setAdapter(adapter);
        imgfilterListv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int pos = position;
                for (int i = 0; i < mapList_filter.size(); i++) {
                    mapList_filter.get(i).put("select", "0");
                }
                mapList_filter.get(pos).put("select", "1");
                imageFilter = MagicFilterFactory.initFilters((MagicFilterType) mapList_filter.get(pos).get("filter"));//修改滤镜
                Log.e("TAG","=========="+mapList_filter.get(pos).get("filter"));
                if (mapList_filter.get(pos).get("filter").toString().equals("NONE")){
                    handler.sendEmptyMessage(11);
                }else {
                    handler.sendEmptyMessage(1);
                }

                handler.sendEmptyMessage(5);



            }
        });






        //----------------点击事件------------------------

        imgfilterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgfilterComple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(newBitmapto);
            }
        });






    }


    //保存到本地：
    public File saveImage(Bitmap bmp) {
        File file = new File(savepath);
        File appDir = new File(String.valueOf(file.getParentFile()));
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            Log.e("TAG","=====保存成功======");
            handler.sendEmptyMessage(2);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}
