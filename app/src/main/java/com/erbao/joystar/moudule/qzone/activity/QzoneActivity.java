package com.erbao.joystar.moudule.qzone.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.erbao.joystar.BuildConfig;
import com.erbao.joystar.R;
import com.erbao.joystar.moudule.qzone.adapter.QzoneBaseAdapter;
import com.erbao.joystar.moudule.qzone.utils.UriToPath;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.AutoListView;
import com.erbao.joystar.views.CustomProgressDialog;
import com.erbao.videoimg.activity.ImageFilterActivity;
import com.erbao.videoimg.activity.MainActivity;
import com.erbao.videoimg.activity.VideoCameraActivity;
import com.erbao.videoimg.activity.VideoMiscActivity;
import com.erbao.videoimg.videimg_utils.ImgVideoTimeUtils;
import com.erbao.videoimg.videimg_utils.VideoimgInitialize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.getBitmapDegree;
import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.getBitmapFormUri;
import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.getFileFromMediaUri;
import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.rotateBitmapByDegree;
import static com.erbao.joystar.okhttp.HttpUrls.QzoneSendnewslist;
import static com.erbao.joystar.okhttp.HttpUrls.isLogin;

public class QzoneActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView qzoneImg;
    private AutoListView qzoneListv;
    private View vHead;
    private ImageView handciewMenu;
    private ImageView handciewCenter;
    private ImageView handciewImg;
    private ImageView handciewEdt;
    private TextView handciewNickname;
    private TextView handciewName;
    private TextView handciewViews;
    private TextView handciewFans;
    private TextView handciewLike;
    private ImageView handciewXiangce;
    private ImageView handciewXiangji;
    private ImageView handciewVideo;
    private ImageView handciewAdd;
    private TextView handciewText;
    private TextView handciewFocus;
    private RelativeLayout handciewRale1;
    private TextView handciewCounty;
    private TextView handciewPro;
    private TextView handciewTag;
    private ImageView handciewLikeimg;
    private TextView handciewLikenumm;
    private RelativeLayout handciewRale2;
    QzoneBaseAdapter qzoneBaseAdapter;
    public static List<Map<String, Object>> mapList;
    List<Map<String, Object>> newmapList;
    Map<String, Object> mapdata;
    String userId;
    int page = 1, index = 2;
    private LinearLayout handciewLints;
    int idexs = 0;
    String fileName;
    File videFile;
    CustomProgressDialog cusdialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(QzoneActivity.this, msgg);
                    if (cusdialog.isShowing()) {
                        cusdialog.dismiss();
                    }
                    break;
                case 1:
                    GlideImageLoader.show(QzoneActivity.this, mapdata.get("user_coverimg").toString(), qzoneImg);
                    GlideImageLoader.showCircle(QzoneActivity.this, mapdata.get("user_photo").toString(), handciewImg, R.mipmap.circle_bg_img);
                    handciewNickname.setText(mapdata.get("user_nickname").toString());
                    handciewName.setText(mapdata.get("user_partname").toString());
                    handciewViews.setText(mapdata.get("pageNum").toString());
                    handciewFans.setText(mapdata.get("like_count").toString());
                    handciewLike.setText(mapdata.get("cover_like").toString());

                    String like_status = mapdata.get("like_status").toString();
                    if (like_status.equals("1")) {
                        handciewFocus.setVisibility(View.GONE);
//                        handciewFocus.setText("已关注");
                    } else {
//                        handciewFocus.setText("关注");
                    }

                    if (mapdata.get("user_country").toString().length() > 0) {
                        handciewCounty.setText(mapdata.get("user_country").toString());
                    } else {
                        handciewCounty.setVisibility(View.GONE);
                    }
                    if (mapdata.get("user_profession").toString().length() > 0) {
                        handciewPro.setText(mapdata.get("user_profession").toString());
                    } else {
                        handciewPro.setVisibility(View.GONE);
                    }
                    if (mapdata.get("user_tag").toString().length() > 0) {
                        handciewTag.setText(mapdata.get("user_tag").toString());
                    } else {
                        handciewTag.setVisibility(View.GONE);
                    }

                    handciewLikenumm.setText(mapdata.get("like_count").toString());
                    if (mapdata.get("coverLike_status").toString().equals("1")) {
                        handciewLikeimg.setImageResource(R.mipmap.heart_view);
                    } else {
                        handciewLikeimg.setImageResource(R.mipmap.white_heart);
                    }


                    break;
                case 2:
                    qzoneListv.onRefreshComplete(); //完成刷新
                    mapList.clear();   //清除数据
                    mapList.addAll(newmapList);  //从新加载数据
                    qzoneListv.setResultSize(newmapList.size());    //添加数据
                    qzoneBaseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 3:
                    qzoneListv.onLoadComplete();   //完成加载
                    mapList.addAll(newmapList);     //加载数据
                    qzoneListv.setResultSize(newmapList.size());    //添加数据
                    qzoneBaseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 4:
                    handciewFocus.setVisibility(View.GONE);
                    break;
                case 5:
                    LogUtils.e("=====55555555=========");
                    GlideImageLoader.show(QzoneActivity.this, SpUtil.get("user_coverimg", "").toString(), qzoneImg);
                    if (cusdialog.isShowing()) {
                        cusdialog.dismiss();
                    }
                    break;
                case 6:
                    handciewLikeimg.setImageResource(R.mipmap.heart_view);
                    int ds = Integer.parseInt(handciewLikenumm.getText().toString().trim()) + 1;
                    handciewLikenumm.setText(ds + "");
                    break;
                case 7:
                    handciewLikeimg.setImageResource(R.mipmap.white_heart);
                    int dss = Integer.parseInt(handciewLikenumm.getText().toString().trim()) - 1;
                    handciewLikenumm.setText(dss + "");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzone);

        qzoneImg = (ImageView) findViewById(R.id.qzone_img);
        qzoneListv = (AutoListView) findViewById(R.id.qzone_listv);
        cusdialog = new CustomProgressDialog(this, R.style.dialog);
        //---------------------头部--------------------------------
        vHead = View.inflate(this, R.layout.activity_qzone_item_handview, null);
        qzoneListv.addHeaderView(vHead);

        handciewMenu = (ImageView) vHead.findViewById(R.id.handciew_menu);
        handciewCenter = (ImageView) vHead.findViewById(R.id.handciew_center);
        handciewImg = (ImageView) vHead.findViewById(R.id.handciew_img);
        handciewEdt = (ImageView) vHead.findViewById(R.id.handciew_edt);//封面修改
        handciewNickname = (TextView) vHead.findViewById(R.id.handciew_nickname);
        handciewName = (TextView) vHead.findViewById(R.id.handciew_name);
        handciewViews = (TextView) vHead.findViewById(R.id.handciew_views);
        handciewFans = (TextView) vHead.findViewById(R.id.handciew_fans);
        handciewLike = (TextView) vHead.findViewById(R.id.handciew_like);
        handciewXiangce = (ImageView) vHead.findViewById(R.id.handciew_xiangce);
        handciewXiangji = (ImageView) vHead.findViewById(R.id.handciew_xiangji);
        handciewVideo = (ImageView) vHead.findViewById(R.id.handciew_video);
        handciewAdd = (ImageView) vHead.findViewById(R.id.handciew_add);
        handciewText = (TextView) vHead.findViewById(R.id.handciew_text);
        handciewFocus = (TextView) findViewById(R.id.handciew_focus);//关注


        handciewRale1 = (RelativeLayout) findViewById(R.id.handciew_rale1);
        handciewCounty = (TextView) findViewById(R.id.handciew_county);
        handciewPro = (TextView) findViewById(R.id.handciew_pro);
        handciewTag = (TextView) findViewById(R.id.handciew_tag);
        handciewLikeimg = (ImageView) findViewById(R.id.handciew_likeimg);//封面点赞
        handciewLikenumm = (TextView) findViewById(R.id.handciew_likenumm);
        handciewRale2 = (RelativeLayout) findViewById(R.id.handciew_rale2);
        handciewRale1.setVisibility(View.GONE);
        handciewLints = (LinearLayout) findViewById(R.id.handciew_lints);


        handciewMenu.setOnClickListener(this);
        handciewCenter.setOnClickListener(this);
        handciewImg.setOnClickListener(this);
        handciewEdt.setOnClickListener(this);
        handciewXiangce.setOnClickListener(this);
        handciewXiangji.setOnClickListener(this);
        handciewVideo.setOnClickListener(this);
        handciewAdd.setOnClickListener(this);
        handciewText.setOnClickListener(this);
        handciewFocus.setOnClickListener(this);
        handciewLikeimg.setOnClickListener(this);

        //---------------------头部--------------------------------


        mapList = new ArrayList<>();
        newmapList = new ArrayList<>();
        mapdata = new HashMap<>();

        qzoneBaseAdapter = new QzoneBaseAdapter(this, "QzoneActivity", mapList);
        qzoneListv.setAdapter(qzoneBaseAdapter);

        userId = getIntent().getStringExtra("userId");
        GetData(HttpUrls.pageList, page);

        if (!userId.equals(SpUtil.get("user_id", ""))) {
            handciewEdt.setVisibility(View.GONE);
            handciewLints.setVisibility(View.GONE);
        }


        qzoneListv.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index = 2;
                page = 1;
                GetData(HttpUrls.pageList, page);
            }
        });

        qzoneListv.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                index = 3;
                page = page + 1;
                GetData(HttpUrls.pageList, page);
            }
        });


        qzoneListv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > 0) {
                    handciewRale1.setVisibility(View.GONE);
                    handciewRale2.setVisibility(View.VISIBLE);
                    idexs = 0;
                } else {
                    idexs = idexs + 1;
                    if (idexs > 10) {
                        handciewRale1.setVisibility(View.VISIBLE);
                        handciewRale2.setVisibility(View.GONE);
                    }
                }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.handciew_menu:
                finish();
                break;
            case R.id.handciew_center:
                startActivity(new Intent(this, QzonePersonaldataActivity.class)
                        .putExtra("user_id", mapdata.get("user_id").toString())
                        .putExtra("user_partname", mapdata.get("user_partname").toString())
                        .putExtra("user_nickname", mapdata.get("user_nickname").toString())
                        .putExtra("user_photo", mapdata.get("user_photo").toString())
                );
                break;
            case R.id.handciew_img:
                break;
            case R.id.handciew_edt:
                showDialog("img");
                break;
            case R.id.handciew_xiangce://调相册

                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 103);
                break;
            case R.id.handciew_xiangji://调相机

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    File path = new File(mImagePath);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    fileName = System.currentTimeMillis() + ".jpg";
                    mCurrentFile = new File(mImagePath, fileName);

                    Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", mCurrentFile);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(cameraIntent, 102);
                }

                break;
            case R.id.handciew_video://视频调相机/相册
                showDialog("vide");

                break;
            case R.id.handciew_add:
                startActivity(new Intent(this, QzoneSendnewsActivity.class));
                break;
            case R.id.handciew_text:
                break;
            case R.id.handciew_focus://关注
                LogUtils.e("-----12-----" + SpUtil.get("user_id", "").toString());
                if (!isLogin) {
                    ToastUtils.show(this, getResources().getString(R.string.plaselogin));
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SpUtil.get("user_id", "").toString());
                    map.put("superUserId", userId);
                    GetDatas(HttpUrls.addFllow, map, "focus");
                }

                break;
            case R.id.handciew_likeimg://关注
                LogUtils.e("---22-------" + SpUtil.get("user_id", "").toString());
                if (!isLogin) {
                    ToastUtils.show(this, getResources().getString(R.string.plaselogin));
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    Map<String, Object> maps = new HashMap<>();
                    maps.put("userId", SpUtil.get("user_id", "").toString());
                    maps.put("superUserId", userId);
                    GetDatas(HttpUrls.saveLike, maps, "like");
                }
                break;
            default:
                break;
        }
    }

    String mImagePath = Environment.getExternalStorageDirectory() + "/Yoystar/";
    File mCurrentFile;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:  //图片相机
                if (resultCode == Activity.RESULT_OK) {

                    File file = new File(mImagePath, fileName);
//                    startActivity(new Intent(QzoneActivity.this, BitmapFilterActivity.class)
//                            .putExtra("filePath", file.getAbsolutePath() + "")
//                            .putExtra("type", "QzoneActivity"));


                    String savepath = mImagePath + "Imgvdieo/" + System.currentTimeMillis() + ".jpg";//保存地址

                    LogUtils.e("===图片相机=====" + file.getAbsolutePath());
                    LogUtils.e("===图片相机=====" + savepath);

                    startActivityForResult(new Intent(QzoneActivity.this, ImageFilterActivity.class).putExtra("path", file.getAbsolutePath()).putExtra("savepath", savepath), 107);

                }
                break;
            case 103: //图片相册
                if (resultCode == Activity.RESULT_OK) {
                    Uri originalUri = null;
                    File file = null;
                    if (null != data && data.getData() != null) {
                        originalUri = data.getData();
                        file = getFileFromMediaUri(this, originalUri);// 通过Uri获取文件
                    }

                    String savepath = mImagePath + "Imgvdieo/" + System.currentTimeMillis() + ".jpg";//保存地址

                    LogUtils.e("====图片相册====" + file.getAbsolutePath());
                    LogUtils.e("====图片相册====" + savepath);

                    startActivityForResult(new Intent(QzoneActivity.this, ImageFilterActivity.class).putExtra("path", file.getAbsolutePath()).putExtra("savepath", savepath), 107);
//                    startActivity(new Intent(QzoneActivity.this, BitmapFilterActivity.class)
//                            .putExtra("filePath", file.getAbsolutePath() + "")
//                            .putExtra("type", "QzoneActivity")
//                    );
                }
                break;
            case 1022://封面相机
                if (resultCode == Activity.RESULT_OK) {
                    File filesd = new File(mImagePath, "bg.jpg");
                    LogUtils.e("===1022=======" + filesd.getAbsolutePath());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", SpUtil.get("user_id", ""));
                    List<File> files = new ArrayList<>();
                    files.add(filesd);
                    GetDataphoto(HttpUrls.updateCover, map, files, "userCoverimg");
                }
                break;
            case 1033://封面相册
                if (resultCode == Activity.RESULT_OK) {
                    Uri originalUris = null;
                    File files = null;
                    if (null != data && data.getData() != null) {
                        originalUris = data.getData();
                        files = getFileFromMediaUri(this, originalUris);// 通过Uri获取文件
                    }
                    LogUtils.e("===1033=======" + files.getAbsolutePath());
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", SpUtil.get("user_id", ""));
                    List<File> filesd = new ArrayList<>();
                    filesd.add(files);
                    GetDataphoto(HttpUrls.updateCover, map, filesd, "userCoverimg");

                }
                break;
            case 105://视频相册
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    System.out.println("====img_path=====" + UriToPath.getRealFilePath(this, uri));
//
                    int[] tme = ImgVideoTimeUtils.getvideomuisctime(UriToPath.getRealFilePath(this, uri));
                    LogUtils.e("====tme=====" + tme);
                    LogUtils.e("====tme=====" + tme[0]);
                    LogUtils.e("====tme=====" + tme[1]);
                    LogUtils.e("====tme=====" + tme[2]);
                    if (tme[0] > 5) {
                        String videoPath = UriToPath.getRealFilePath(this, uri);
                        String savevideoPath = mImagePath + "Imgvdieo/" + System.currentTimeMillis() + ".mp4";//视频保存地址
                        startActivityForResult(new Intent(QzoneActivity.this, VideoMiscActivity.class).putExtra("path", videoPath).putExtra("savepath", savevideoPath), 106);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.errvoide), Toast.LENGTH_SHORT).show();
                    }


                }
                break;
            case 106://视频跳转
                if (resultCode == 1) {
//
                    String path = data.getExtras().getString("path");
                    System.out.println("====img_path=====" + path);
                    int tme[] = ImgVideoTimeUtils.getvideomuisctime(path);
                    LogUtils.e("====tme=====" + tme);
                    if (tme[0]>5){
                        startActivity(new Intent(QzoneActivity.this, QzoneSendVideActivity.class).putExtra("filePath", path));
                    }else {
                        Toast.makeText(this,getResources().getString(R.string.errvoide),Toast.LENGTH_SHORT).show();
                    }



                }
                break;
            case 107:
                if (resultCode == 1) {
                    System.out.println("====img_path=====" + data.getExtras().getString("path"));

                    if (QzoneSendnewslist == null) {
                        QzoneSendnewslist = new ArrayList<>();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("img", data.getExtras().getString("path"));
                    map.put("text", "");
                    QzoneSendnewslist.add(map);
                    startActivity(new Intent(QzoneActivity.this, QzoneSendnewsActivity.class));


                }


                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    //保存到本地：
    public File saveImage(Bitmap bmp) {
        File appDir = new File(Environment.getExternalStorageDirectory(), "Yoystar");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
//        String fileName = "vkahead.jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }


    String msgg;

    public void GetData(String url, int page) {
        newmapList = new ArrayList<>();
        if (index == 2) {
            mapdata = new HashMap<>();
        }
        Map<String, Object> maps = new HashMap<>();
        maps.put("userId", userId);
        maps.put("memberId", SpUtil.get("user_id", "").toString());
        maps.put("page", page);
        maps.put("size", "15");

        OkhttpUtils.getInstance(this).Post(url, maps, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result = object.getJSONObject("result");
                        mapdata.put("user_profession", result.getString("user_profession"));//职业
                        mapdata.put("user_status", result.getString("user_status"));// 用户状态 1超级用户  0普通用户
                        mapdata.put("like_count", result.getString("like_count"));//关注数,
                        mapdata.put("user_country", result.getString("user_country"));// 国家
                        mapdata.put("user_tag", result.getString("user_tag"));//标签
                        mapdata.put("coverLike_status", result.getString("coverLike_status"));//是否给封面图点赞过 1表示点过0表示没有
                        mapdata.put("user_coverimg", result.getString("user_coverimg"));//封面图
                        mapdata.put("pageNum", result.getString("pageNum"));//动态数
                        mapdata.put("user_partname", result.getString("user_partname"));//用户名字
                        mapdata.put("like_status", result.getString("like_status"));//是否关注过 1表示关注 0表示没关注
                        mapdata.put("user_id", result.getString("user_id"));
                        mapdata.put("cover_like", result.getString("cover_like"));// 封面图点赞数
                        mapdata.put("user_nickname", result.getString("user_nickname"));//   用户昵称
                        mapdata.put("user_photo", result.getString("user_photo"));//头像
                        JSONArray array = result.getJSONArray("array");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject objects = array.getJSONObject(i);
                            Map<String, Object> map = new HashMap<String, Object>();
                            map.put("like_counts", objects.getString("like_counts"));//  动态点赞总数
                            map.put("dynamic_type", objects.getString("dynamic_type"));// 动态类型  1是图片 0是视频
                            map.put("dynamic_text", objects.getString("dynamic_text"));//动态本文
                            map.put("dynamic_id", objects.getString("dynamic_id"));//  动态id
                            map.put("review_counts", objects.getString("review_counts"));//评论总数
                            map.put("crated_time", objects.getString("crated_time"));//动态创建时间
                            List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
                            JSONArray data = objects.getJSONArray("data");
                            for (int j = 0; j < data.length(); j++) {
                                JSONObject object1 = data.getJSONObject(j);
                                Map<String, Object> map1 = new HashMap<String, Object>();
                                map1.put("user_id", result.getString("user_id"));//
                                map1.put("dynamic_type", objects.getString("dynamic_type"));//
                                map1.put("gif_img", object1.getString("gif_img"));// gif图  视频才有
                                map1.put("like_count", object1.getString("like_count"));//图片点赞数
                                map1.put("img_url", object1.getString("img_url"));//动态图片
                                map1.put("review_count", object1.getString("review_count"));// 图片评论数
                                map1.put("img_id", object1.getString("img_id"));// 图片id
                                map1.put("text", object1.getString("text"));//动态文本
                                map1.put("status", object1.getString("status"));// 是否点赞过 0表示没有 1表示
                                map1.put("crated_time", objects.getString("crated_time"));// 是否点赞过 0表示没有 1表示
                                dataList.add(map1);
                            }
                            map.put("data", dataList);
                            newmapList.add(map);
                        }

                        LogUtils.e("======newmapList======" + newmapList);
                        if (index == 2) {
                            handler.sendEmptyMessage(1);
                        }
                        handler.sendEmptyMessage(index);
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
                msgg = getResources().getText(R.string.Serverexception) + "";
                handler.sendEmptyMessage(0);
            }
        });


    }

    public void GetDatas(String url, Map<String, Object> map, final String type) {
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        if (type.equals("focus")) {
                            msgg = object.getString("promptMessage") + "";
                            handler.sendEmptyMessage(0);
                            handler.sendEmptyMessage(4);
                        } else if (type.equals("like")) {
                            msgg = object.getString("promptMessage") + "";
                            handler.sendEmptyMessage(0);
                            JSONObject result = object.getJSONObject("result");
                            if (result.getString("status").equals("1")) {
                                handler.sendEmptyMessage(6);
                            } else {
                                handler.sendEmptyMessage(7);
                            }

                        }


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
                msgg = getResources().getText(R.string.Serverexception) + "";
                handler.sendEmptyMessage(0);
            }
        });


    }

    //相机相册弹框
    private Dialog dialog;
    private View inflate;
    TextView camera, Photoalbum, cancel;

    public void showDialog(final String type) {
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        //填充对话框的布局
        inflate = LayoutInflater.from(this).inflate(R.layout.profile_img, null);
        //初始化控件
        camera = (TextView) inflate.findViewById(R.id.camera);
        Photoalbum = (TextView) inflate.findViewById(R.id.Photoalbum);
        cancel = (TextView) inflate.findViewById(R.id.cancel);
        //将布局设置给Dialog
        dialog.setContentView(inflate);
        //获取当前Activity所在的窗体
        Window dialogWindow = dialog.getWindow();
        //设置Dialog从窗体底部弹出
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        //获得窗体的属性
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.y = 20;//设置Dialog距离底部的距离
//       将属性设置给窗体
        dialogWindow.setAttributes(lp);
        dialog.show();//显示对话框

        camera.setOnClickListener(new View.OnClickListener() {//相机
            @Override
            public void onClick(View v) {
                if (type.equals("vide")) {

//                    Intent intent = new Intent();
//                    intent.setAction("android.media.action.VIDEO_CAPTURE");
//                    intent.addCategory("android.intent.category.DEFAULT");
//                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
////                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, size * 1024 * 1024L);//限制录制大小(10M=10 * 1024 * 1024L)
//                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 15);//限制录制时间(10秒=10)
//
//                    String fileName = System.currentTimeMillis() + ".mp4";
//                    videFile = new File(mImagePath, fileName);
//                    if (videFile.exists()) {
//                        videFile.delete();
//                    }
//                    Uri uri = Uri.fromFile(videFile);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//                    startActivityForResult(intent, 106);

                    //视频保存地址
                    String savevideoPath = mImagePath + "Imgvdieo/" + System.currentTimeMillis() + ".mp4";//视频保存地址
                    videFile = new File(savevideoPath);
                    startActivityForResult(new Intent(QzoneActivity.this, VideoCameraActivity.class).putExtra("path", savevideoPath).putExtra("time", "15000"), 106);//time录制时间1000=1s


                } else if (type.equals("img")) {
                    getcamera();
                }
                dialog.dismiss();
            }
        });
        Photoalbum.setOnClickListener(new View.OnClickListener() {//相册
            @Override
            public void onClick(View v) {
                if (type.equals("vide")) {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 105);
                } else if (type.equals("img")) {
                    getPhotoalbum();
                }
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


    }

    //调用相机
    public void getcamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File path = new File(mImagePath);
            if (!path.exists()) {
                path.mkdir();
            }
            mCurrentFile = new File(mImagePath, "bg.jpg");
            Uri uri = FileProvider.getUriForFile(this, getPackageName() + ".provider", mCurrentFile);
            cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(cameraIntent, 1022);
        }

    }

    //调用相册
    public void getPhotoalbum() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(pickIntent, 1033);

    }

    public void GetDataphoto(String url, Map<String, Object> map, List<File> files, String Filename) {
        cusdialog.show();
        OkhttpUtils.getInstance(this).sendMultipart(url, map, files, Filename, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result = object.getJSONObject("result");
                        SpUtil.put("user_coverimg", result.getString("user_coverimg"));//封面图
                        msgg = object.getString("promptMessage") + "";
                        handler.sendEmptyMessage(0);
                        handler.sendEmptyMessage(5);
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
                msgg = getResources().getText(R.string.Serverexception) + "";
                handler.sendEmptyMessage(0);
            }
        });
    }
}
