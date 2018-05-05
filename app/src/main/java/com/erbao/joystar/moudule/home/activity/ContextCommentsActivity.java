package com.erbao.joystar.moudule.home.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.home.adapter.HomeBaseAdapter;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.CheckData;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.AutoListView;
import com.erbao.joystar.views.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.erbao.joystar.okhttp.HttpUrls.isLogin;

public class ContextCommentsActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView concommImg;
    private TextView title;
    private ImageView back;
    private AutoListView concommListv;
    private EditText commentEdt;
    private TextView commentSend;
    List<Map<String, Object>> maplist;
    List<Map<String, Object>> newmaplist;
    HomeBaseAdapter baseAdapter;
    int page = 1, index = 1;
    String usetid;
    CustomProgressDialog dialog;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.show(ContextCommentsActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    concommListv.onRefreshComplete(); //完成刷新
                    maplist.clear();   //清除数据
                    maplist.addAll(newmaplist);  //从新加载数据
                    concommListv.setResultSize(newmaplist.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 2:
                    concommListv.stare="1";
                    concommListv.onLoadComplete();   //完成加载
                    maplist.addAll(newmaplist);     //加载数据
                    concommListv.setResultSize(newmaplist.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 3:
                    commentEdt.setText("");
                    break;
                default:
                    break;
            }

        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_comments);


        concommImg = (ImageView) findViewById(R.id.concomm_img);
        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.Discussiongroups));
        back = (ImageView) findViewById(R.id.back);
        concommListv = (AutoListView) findViewById(R.id.concomm_listv);
        commentEdt = (EditText) findViewById(R.id.comment_edt);
        commentSend = (TextView) findViewById(R.id.comment_send);

        dialog = new CustomProgressDialog(this, R.style.dialog);
        usetid = getIntent().getStringExtra("userId");
        String coverimg = getIntent().getStringExtra("user_coverimg");

        GlideImageLoader.showBlur25(this,coverimg,concommImg);
//        GlideImageLoader.show(this, coverimg, concommImg);
        back.setOnClickListener(this);
        commentSend.setOnClickListener(this);

        maplist = new ArrayList<>();


        baseAdapter = new HomeBaseAdapter(this, "ContextCommentsActivity", maplist);
        concommListv.setAdapter(baseAdapter);

        concommListv.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                index = 1;
                Map<String, Object> map = new HashMap<>();
                map.put("page", page);
                map.put("superUserId", usetid);
                map.put("size", "15");
                GetData(HttpUrls.commentDetails, map,"get");
            }
        });
        concommListv.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                Map<String, Object> map = new HashMap<>();
                page = page + 1;
                index = 2;
                map.put("page", page);
                map.put("superUserId", usetid);
                map.put("size", "15");
                GetData(HttpUrls.commentDetails, map,"get");
            }
        });


        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("superUserId", usetid);
        map.put("size", "15");
        GetData(HttpUrls.commentDetails, map,"get");


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.comment_send:
                CheckData.inputmanger(this);
                String text = commentEdt.getText().toString().trim();
                if (!isLogin){
                    startActivity(new Intent(this, LoginActivity.class));
                }else if (text.length() < 1) {
                    ToastUtils.show(this, getResources().getString(R.string.entercomments));
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userId", SpUtil.get("user_id", ""));
                    map.put("superUserId", usetid);
                    map.put("userComment", "text");
                    GetData(HttpUrls.addComment, map, "send");
                }


                break;
            default:
                break;
        }
    }


    String msgg;

    public void GetData(String url, final Map<String, Object> map, final String type) {
        newmaplist = new ArrayList<>();
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        if (type.equals("get")){
                            JSONObject result = object.getJSONObject("result");
                            JSONArray array = result.getJSONArray("array");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject objects = array.getJSONObject(i);
                                Map<String, Object> maps = new HashMap<String, Object>();
                                maps.put("user_review", objects.getString("user_review"));
                                maps.put("user_id", objects.getString("user_id"));
                                maps.put("user_nickName", objects.getString("user_nickName"));
                                maps.put("isMe_status", objects.getString("isMe_status"));
                                maps.put("review_time", objects.getString("review_time"));
                                maps.put("user_photo", objects.getString("user_photo"));
                                newmaplist.add(maps);
                            }
                            handler.sendEmptyMessage(index);
                        }else if (type.equals("send")){

                            JSONObject result=object.getJSONObject("result");
                            JSONObject commentData=result.getJSONObject("commentData");
                            Map<String,Object> mapsd=new HashMap<String, Object>();
                            mapsd.put("user_review",commentData.getString("user_review"));
                            mapsd.put("user_id",commentData.getString("user_id"));
                            mapsd.put("user_nickName",commentData.getString("user_nickName"));
                            mapsd.put("isMe_status",commentData.getString("isMe_status"));
                            mapsd.put("review_time",commentData.getString("review_time"));
                            mapsd.put("user_photo",commentData.getString("user_photo"));
                            newmaplist.add(mapsd);
                            handler.sendEmptyMessage(2);
                            handler.sendEmptyMessage(3);







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
