package com.erbao.joystar.moudule.qzone.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.qzone.adapter.QzoneBaseAdapter;
import com.erbao.joystar.moudule.qzone.utils.UriToPath;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;
import com.erbao.videoimg.activity.ImageFilterActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.erbao.joystar.moudule.qzone.utils.BitmapImage.getFileFromMediaUri;
import static com.erbao.joystar.okhttp.HttpUrls.QzoneSendnewslist;
import static com.erbao.joystar.okhttp.HttpUrls.mImagePath;

public class QzoneSendnewsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private TextView save;
    private ImageView qzonesendnewsImg;
    private TextView qzonesendnewsPermiss;
    private ListView qzonesendnewsListv;
    private ImageView qzonesendnewsSelect;
    CustomProgressDialog dialog;
    QzoneBaseAdapter qzoneBaseAdapter;
    List<Map<String, Object>> mapList;
    public static Handler handlers;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(QzoneSendnewsActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    finish();
                    break;
                case 2:
                    qzoneBaseAdapter.notifyDataSetInvalidated();
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
        setContentView(R.layout.activity_qzone_sendnews);


        title = (TextView) findViewById(R.id.title);
        title.setText("");
        back = (ImageView) findViewById(R.id.back);
        save = (TextView) findViewById(R.id.save);
        save.setVisibility(View.VISIBLE);
        save.setText(getResources().getString(R.string.upload));
        qzonesendnewsImg = (ImageView) findViewById(R.id.qzonesendnews_img);
        qzonesendnewsPermiss = (TextView) findViewById(R.id.qzonesendnews_permiss);
        qzonesendnewsListv = (ListView) findViewById(R.id.qzonesendnews_listv);
        qzonesendnewsSelect = (ImageView) findViewById(R.id.qzonesendnews_select);
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        qzonesendnewsPermiss.setOnClickListener(this);
        qzonesendnewsSelect.setOnClickListener(this);
        handlers = handler;

        if (QzoneSendnewslist == null) {
            QzoneSendnewslist = new ArrayList<>();
        }
        mapList = QzoneSendnewslist;

        qzoneBaseAdapter = new QzoneBaseAdapter(this, "QzoneSendnewsActivity", mapList);
        qzonesendnewsListv.setAdapter(qzoneBaseAdapter);

        GlideImageLoader.showCircle(this, SpUtil.get("user_photo", "").toString(), qzonesendnewsImg, R.mipmap.circle_bg_img);
        dialog = new CustomProgressDialog(this, R.style.dialog);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.save:
                LogUtils.e("=====QzoneSendnewslist====" + QzoneSendnewslist);
                if (QzoneSendnewslist.size() > 0) {

                    List<File> files = new ArrayList<>();
                    JSONObject object = new JSONObject();
                    JSONArray array = new JSONArray();
                    for (int i = 0; i < QzoneSendnewslist.size(); i++) {
                        File file = new File(QzoneSendnewslist.get(i).get("img") + "");
                        files.add(file);
                        array.put(QzoneSendnewslist.get(i).get("text"));
                    }
                    try {
                        object.put("text", array);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LogUtils.e("=====files========" + files);
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SpUtil.get("user_id", ""));
                    map.put("strJson", object);
                    GetData(HttpUrls.saveDynamic, map, files, "dynamicimg");
                    dialog.show();
                } else {
                    ToastUtils.show(this, getResources().getString(R.string.select_upload));
                }


                break;
            case R.id.qzonesendnews_permiss:
                break;
            case R.id.qzonesendnews_select:
                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 103);
                break;
            default:
                break;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(2);
    }

    //删除
    public static void setdata(int index) {
        QzoneSendnewslist.remove(QzoneSendnewslist.get(index));
        handlers.sendEmptyMessage(2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:  //相机
                if (resultCode == Activity.RESULT_OK) {
                    //裁剪图片
//                    crop(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
//                            ".provider", mCurrentFile));
                }
                break;
            case 103: //相册
                if (resultCode == Activity.RESULT_OK) {
                    //裁剪图片
                    Uri originalUri = null;
                    File file = null;
                    if (null != data && data.getData() != null) {
                        originalUri = data.getData();
                        file = getFileFromMediaUri(this, originalUri);// 通过Uri获取文件
                    }

                    String savepath = mImagePath + "Imgvdieo/" + System.currentTimeMillis() + ".jpg";//保存地址
                    startActivityForResult(new Intent(QzoneSendnewsActivity.this, ImageFilterActivity.class).putExtra("path", file.getAbsolutePath()).putExtra("savepath", savepath), 104);

//                startActivity(new Intent(QzoneSendnewsActivity.this, BitmapFilterActivity.class).putExtra("filePath", file.getAbsolutePath() + "").putExtra("type", "QzoneSendnewsActivity"));
                }

                break;
            case 104:
                if (resultCode == 1) {
                    System.out.println("====img_path=====" + data.getExtras().getString("path"));
                    if (QzoneSendnewslist == null) {
                        QzoneSendnewslist = new ArrayList<>();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("img", data.getExtras().getString("path"));
                    map.put("text", "");
                    QzoneSendnewslist.add(map);
                    handler.sendEmptyMessage(2);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                msgg = getResources().getText(R.string.Serverexception) + "";
                handler.sendEmptyMessage(0);
            }
        });


    }
}
