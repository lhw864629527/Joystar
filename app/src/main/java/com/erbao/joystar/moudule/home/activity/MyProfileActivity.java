package com.erbao.joystar.moudule.home.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erbao.joystar.BuildConfig;
import com.erbao.joystar.R;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;

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

import static android.R.string.cancel;

public class MyProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout profilePhonelin;
    private LinearLayout activityMyProfile;
    private TextView title;
    private ImageView back;
    private RelativeLayout profileImglin;
    private ImageView profileImg;
    private LinearLayout profileNamelin;
    private TextView profileName;
    private TextView profileNickname;
    private TextView profilePhone;
    private TextView profileCounty;
    private TextView profilePro;
    private TextView profileTag;
    private TextView save;
    CustomProgressDialog ueddialog;
    String name, nickname, county, pro, tag;
    public static Map<String, Object> mapdata;
    int mapdataleng;
    int find=0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(MyProfileActivity.this, msgg);
                    if (ueddialog.isShowing()) {
                        ueddialog.dismiss();
                    }
                    break;
                case 1:
                    GlideImageLoader.showCircle(MyProfileActivity.this, SpUtil.get("user_photo", "").toString(), profileImg, R.mipmap.circle_bg_img);
                    break;
                case 2:

                    profileName.setText(mapdata.get("name").toString());
                    profileNickname.setText(mapdata.get("nickname").toString());
                    profileCounty.setText(mapdata.get("county").toString());
                    profilePro.setText(mapdata.get("pro").toString());
                    profileTag.setText(mapdata.get("tag").toString());
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
        setContentView(R.layout.activity_my_profile);

        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.profile));
        back = (ImageView) findViewById(R.id.back);

        profileImglin = (RelativeLayout) findViewById(R.id.profile_imglin);
        profileImg = (ImageView) findViewById(R.id.profile_img);
        profileNamelin = (LinearLayout) findViewById(R.id.profile_namelin);
        profileName = (TextView) findViewById(R.id.profile_name);
        profileNickname = (TextView) findViewById(R.id.profile_nickname);
        profilePhone = (TextView) findViewById(R.id.profile_phone);
        profileCounty = (TextView) findViewById(R.id.profile_county);
        profilePro = (TextView) findViewById(R.id.profile_pro);
        profileTag = (TextView) findViewById(R.id.profile_tag);
        save = (TextView) findViewById(R.id.save);
        save.setVisibility(View.VISIBLE);

        profileImglin.setOnClickListener(this);
        profileName.setOnClickListener(this);
        profileNickname.setOnClickListener(this);
        profilePhone.setOnClickListener(this);
        profileCounty.setOnClickListener(this);
        profilePro.setOnClickListener(this);
        profileTag.setOnClickListener(this);
        back.setOnClickListener(this);
        save.setOnClickListener(this);


        GlideImageLoader.showCircle(this, SpUtil.get("user_photo", "").toString(), profileImg, R.mipmap.circle_bg_img);
        ueddialog = new CustomProgressDialog(this, R.style.dialog);
        profilePhone.setText(SpUtil.get("user_phone", "").toString());

        name = SpUtil.get("user_partname", "").toString();
        nickname = SpUtil.get("user_nickname", "").toString();
        county = SpUtil.get("user_country", "").toString();
        pro = SpUtil.get("user_profession", "").toString();
        tag = SpUtil.get("user_tag", "").toString();
        mapdata = new HashMap<>();
        mapdata.put("name", name);
        mapdata.put("nickname", nickname);
        mapdata.put("county", county);
        mapdata.put("pro", pro);
        mapdata.put("tag", tag);
        mapdataleng=mapdata.toString().length();
        LogUtils.e("======mapdataleng======="+mapdataleng);
        handler.sendEmptyMessage(2);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                LogUtils.e("======mapdataleng======="+mapdataleng);
                LogUtils.e("==============="+mapdata.toString().length());
                find=find+1;
                if (mapdataleng==mapdata.toString().length()){
                    finish();
                }else {
                    if (find>2){
                        finish();
                    }else {
                        ToastUtils.show(this,getResources().getString(R.string.not_save));
                    }

                }

                break;
            case R.id.profile_imglin:
                showDialog();
                break;
            case R.id.profile_name:
                startActivity(new Intent(this, MyProfile_NameActivity.class)
                        .putExtra("type", "name")
                        .putExtra("title", getResources().getString(R.string.Modify_name))
                        .putExtra("text", mapdata.get("name").toString())
                );
                break;
            case R.id.profile_nickname:
                startActivity(new Intent(this, MyProfile_NameActivity.class)
                        .putExtra("type", "nickname")
                        .putExtra("title", getResources().getString(R.string.Modify_nickname))
                        .putExtra("text", mapdata.get("nickname").toString())
                );
                break;
            case R.id.profile_phone://
                break;
            case R.id.profile_county:
                startActivity(new Intent(this, MyProfile_NameActivity.class)
                        .putExtra("type", "county")
                        .putExtra("title", getResources().getString(R.string.Modify_country))
                        .putExtra("text", mapdata.get("county").toString())
                );
                break;
            case R.id.profile_pro:
                startActivity(new Intent(this, MyProfile_NameActivity.class)
                        .putExtra("type", "pro")
                        .putExtra("title", getResources().getString(R.string.Modify_professional))
                        .putExtra("text", mapdata.get("pro").toString())
                );
                break;
            case R.id.profile_tag:
                startActivity(new Intent(this, MyProfile_NameActivity.class)
                        .putExtra("type", "tag")
                        .putExtra("title", getResources().getString(R.string.Modify_label))
                        .putExtra("text", mapdata.get("tag").toString())
                );
                break;
            case R.id.save:

                Map<String, Object> map = new HashMap<>();
                map.put("id",SpUtil.get("user_id",""));
                map.put("userCountry",mapdata.get("county"));
                map.put("userTag",mapdata.get("tag"));
                map.put("userProfession",mapdata.get("pro"));
                map.put("userNickname",mapdata.get("nickname"));
                map.put("userPartname",mapdata.get("name"));
                GetDatasave(HttpUrls.updateUser, map);
                ueddialog.show();
                break;
            default:
                break;


        }
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            find=find+1;
            if (mapdataleng==mapdata.toString().length()){
                finish();
            }else {
                if (find>2){
                    finish();
                }else {
                    ToastUtils.show(this,getResources().getString(R.string.Two_more_exits));
                }

            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.sendEmptyMessage(2);
    }

    String mImagePath = HttpUrls.mImagePath;
    File mCurrentFile;
    private Dialog dialog;
    private View inflate;
    TextView camera, Photoalbum, cancel;

    public void showDialog() {
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
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    File path = new File(mImagePath);
                    if (!path.exists()) {
                        path.mkdir();
                    }
                    mCurrentFile = new File(mImagePath, "photo.jpg");

                    Uri uri = FileProvider.getUriForFile(MyProfileActivity.this, getPackageName() + ".provider", mCurrentFile);
//                    Log.d(TAG,"filePath = "+uri.toString());
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(cameraIntent, 102);
                    dialog.dismiss();
                }

            }
        });
        Photoalbum.setOnClickListener(new View.OnClickListener() {//相册
            @Override
            public void onClick(View v) {
                Intent pickIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, 103);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 102:  //相机
                if (resultCode == Activity.RESULT_OK) {
                    //裁剪图片
                    crop(FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID +
                            ".provider", mCurrentFile));
                }
                break;
            case 103: //相册
                if (resultCode == Activity.RESULT_OK) {
                    //裁剪图片
                    crop(data.getData());
                }
                break;
            case 104://裁剪
                //保存到本地
                Bitmap bitmap = data.getParcelableExtra("data");
                File file = saveImage(bitmap);
                //上传图片 （okhttp上传文件）
                List<File> files = new ArrayList<>();
                files.add(file);
                Map<String, Object> map = new HashMap<>();
                map.put("id", SpUtil.get("user_id", ""));
                GetData(HttpUrls.updatePhoto, map, files, "userPhoto");
                ueddialog.show();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //裁剪图片：
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 104);
    }

    //保存到本地：
    public File saveImage(Bitmap bmp) {
        File appDir = new File(HttpUrls.mImagePath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
//        String fileName = System.currentTimeMillis() + ".jpg";
        String fileName = "photo.jpg";
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

    public void GetData(String url, Map<String, Object> map, List<File> files, String Filename) {
        OkhttpUtils.getInstance(this).sendMultipart(url, map, files, Filename, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result = object.getJSONObject("result");
                        SpUtil.put("user_photo", result.getString("user_photo"));
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


    public void GetDatasave(String url, Map<String, Object> map) {
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {

                        JSONObject result=object.getJSONObject("result");
                        SpUtil.put("user_partname", result.getString("user_partname"));//本名
                        SpUtil.put("user_profession", result.getString("user_profession"));//职业
                        SpUtil.put("user_country", result.getString("user_country"));
                        SpUtil.put("user_tag", result.getString("user_tag"));// 标签
                        SpUtil.put("user_nickname", result.getString("user_nickname"));
                        msgg = object.getString("promptMessage") + "";
                        handler.sendEmptyMessage(0);
                        find=3;
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
