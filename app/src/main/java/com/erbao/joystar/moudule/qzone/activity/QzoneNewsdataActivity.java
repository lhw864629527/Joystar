package com.erbao.joystar.moudule.qzone.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.qzone.adapter.QzoneBaseAdapter;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.AutoListView;
import com.erbao.joystar.views.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.erbao.joystar.okhttp.HttpUrls.isLogin;

public class QzoneNewsdataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private ListView qzoneNewsListv;
    static Activity activity;
   static List<Map<String, Object>> mapList;
    QzoneBaseAdapter qzoneBaseAdapter;
    CustomProgressDialog dialog;
    static Handler handlers;
   static int pos=0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(QzoneNewsdataActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    qzoneBaseAdapter.notifyDataSetChanged();
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
        setContentView(R.layout.activity_qzone_newsdata);

        title = (TextView) findViewById(R.id.title);

        back = (ImageView) findViewById(R.id.back);
        qzoneNewsListv = (ListView) findViewById(R.id.qzone_news_listv);
        back.setOnClickListener(this);
        dialog = new CustomProgressDialog(this, R.style.dialog);
        activity = QzoneNewsdataActivity.this;
        handlers = handler;
        int index = Integer.parseInt(getIntent().getStringExtra("index"));

        mapList = new ArrayList<>();


        mapList = (List<Map<String, Object>>) QzoneActivity.mapList.get(index).get("data");
        String text = mapList.get(0).get("text") + "";
        if (text.length() > 10) {
            title.setText(text.substring(0, 10));
        } else {
            title.setText(text);
        }

        LogUtils.e("==========mapList=====" + mapList);
         qzoneBaseAdapter = new QzoneBaseAdapter(this, "QzoneNewsdataActivity", mapList);
        qzoneNewsListv.setAdapter(qzoneBaseAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }


    //点赞
    public static void setdata(int index, String superUserId, String imgId) {

            pos=index;
            Map<String, Object> map = new HashMap<>();
            map.put("userId", SpUtil.get("user_id", ""));
            map.put("likeType", "1");
            map.put("imgId", imgId);
            map.put("superUserId", superUserId);
            GetData(HttpUrls.addReview, map);

    }

    public static String msgg;

    public static void GetData(String url, Map<String, Object> map) {
        OkhttpUtils.getInstance(activity).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result=object.getJSONObject("result");
                        if (result.toString().contains("\"status\":1")){
                            mapList.get(pos).put("status","1");
                        }else {
                            mapList.get(pos).put("status","0");
                        }
                        handlers.sendEmptyMessage(1);
                        msgg = object.getString("promptMessage") + "";
                        handlers.sendEmptyMessage(0);
                    } else {
                        msgg = object.getString("promptMessage") + "";
                        handlers.sendEmptyMessage(0);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Object tag, String msg) {
                msgg = activity.getResources().getText(R.string.Serverexception) + "";
                handlers.sendEmptyMessage(0);
            }
        });


    }


}
