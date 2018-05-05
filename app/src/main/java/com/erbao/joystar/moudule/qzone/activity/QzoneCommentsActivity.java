package com.erbao.joystar.moudule.qzone.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.qzone.adapter.QzoneBaseAdapter;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.CheckData;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.utils.Utils_time;
import com.erbao.joystar.views.AutoListView;
import com.erbao.joystar.views.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QzoneCommentsActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private AutoListView commentListv;
    private EditText commentEdt;
    private TextView commentSend;
    List<Map<String, Object>> mapList;
    List<Map<String, Object>> newmapList;
    private ImageView commentImg;
    CustomProgressDialog dialog;
    QzoneBaseAdapter baseAdapter;
    String superUserId = "", imgId = "";
    int page = 1, index = 1;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(QzoneCommentsActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    break;
                case 1:
                    commentListv.onRefreshComplete(); //完成刷新
                    mapList.clear();   //清除数据
                    mapList.addAll(newmapList);  //从新加载数据
                    commentListv.setResultSize(newmapList.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 2:
                    commentListv.stare="1";
                    commentListv.onLoadComplete();   //完成加载
                    mapList.addAll(newmapList);     //加载数据
                    commentListv.setResultSize(newmapList.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 3:
                    commentEdt.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzone_comments);

        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.comments));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        dialog = new CustomProgressDialog(this, R.style.dialog);

        superUserId = getIntent().getStringExtra("superUserId");//动态人的id
        imgId = getIntent().getStringExtra("imgId");

        mapList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("superUserId", superUserId);
        map.put("size", "15");
        map.put("imgId", imgId);
        GetData(HttpUrls.ReviewDetails, map, "get");




        commentImg = (ImageView) findViewById(R.id.comment_img);

        GlideImageLoader.showBlur25(this,getIntent().getStringExtra("img_url"),commentImg);
        commentListv = (AutoListView) findViewById(R.id.comment_listv);
        commentEdt = (EditText) findViewById(R.id.comment_edt);
        commentSend = (TextView) findViewById(R.id.comment_send);
        commentSend.setOnClickListener(this);
        baseAdapter = new QzoneBaseAdapter(this, "QzoneCommentsActivity", mapList);
        commentListv.setAdapter(baseAdapter);

        commentListv.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index = 1;
                page = 1;
                Map<String, Object> map = new HashMap<>();
                map.put("page", page);
                map.put("superUserId", superUserId);
                map.put("size", "15");
                map.put("imgId", imgId);
                GetData(HttpUrls.ReviewDetails, map, "get");
            }
        });
        commentListv.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                index = 2;
                page = page + 1;
                Map<String, Object> map = new HashMap<>();
                map.put("page", page);
                map.put("superUserId", superUserId);
                map.put("size", "15");
                map.put("imgId", imgId);
                GetData(HttpUrls.ReviewDetails, map, "get");
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.comment_send:
            String edt=commentEdt.getText().toString().trim();
                if (edt.length()<1){
                ToastUtils.show(this,getResources().getString(R.string.input_comments));
                }else {
                    CheckData.inputmanger(this);
                    Map<String,Object> map=new HashMap<>();
                    map.put("userId", SpUtil.get("user_id",""));
                    map.put("userReview",edt);
                    map.put("likeType","0");
                    map.put("imgId",imgId);
                    map.put("superUserId",superUserId);
                    GetData(HttpUrls.addReview,map,"send");
                }


                break;
            default:
                break;
        }
    }


    String msgg;

    public void GetData(String url, final  Map<String, Object> map, final String type) {
        LogUtils.e("====url=="+url);
        LogUtils.e("===map==="+map);
        newmapList = new ArrayList<>();
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        if (type.equals("get")) {


                            JSONObject result = object.getJSONObject("result");
                            JSONArray array = result.getJSONArray("array");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objects = array.getJSONObject(i);
                                Map<String, Object> maps = new HashMap<String, Object>();
                                maps.put("user_review", objects.getString("user_review"));
                                maps.put("user_id", objects.getString("user_id"));
                                maps.put("user_nickName", objects.getString("user_nickName"));
                                maps.put("isMe_status", objects.getString("isMe_status"));//1 是自己    0不是自己
                                maps.put("review_time", objects.getString("review_time"));
                                maps.put("user_photo", objects.getString("user_photo"));
                                newmapList.add(maps);
                            }
                            handler.sendEmptyMessage(index);
                        } else if (type.equals("send")) {

                            JSONObject results = object.getJSONObject("result");
                            JSONObject result = results.getJSONObject("commentData");
                            Map<String, Object> maps = new HashMap<String, Object>();
                            maps.put("user_review", result.getString("user_review"));
                            maps.put("user_id",result.getString("user_id"));
                            maps.put("user_nickName",result.getString("user_nickName"));
                            maps.put("isMe_status",result.getString("isMe_status"));
                            maps.put("review_time",result.getString("review_time"));
                            maps.put("user_photo",result.getString("user_photo"));
                            newmapList.add(maps);
                            handler.sendEmptyMessage(2);
                            handler.sendEmptyMessage(3);

                            msgg = object.getString("promptMessage") + "";
                            handler.sendEmptyMessage(0);
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


}
