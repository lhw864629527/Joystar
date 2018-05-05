package com.erbao.joystar.moudule.home.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.home.adapter.HomeBaseAdapter;
import com.erbao.joystar.moudule.qzone.activity.QzoneActivity;
import com.erbao.joystar.moudule.qzone.activity.QzoneCommentsActivity;
import com.erbao.joystar.moudule.start.activity.LoginActivity;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
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

import io.vov.vitamio.utils.Log;

/**
 * Created by asus on 2018/1/22.
 */

public class HomeFragment_context extends Fragment {
    Activity activity;
    private AutoListView contentList;
    HomeBaseAdapter baseAdapter;
    List<Map<String, Object>> mapList;
    List<Map<String, Object>> newmapList;
    int page = 1, index = 1;
    CustomProgressDialog dialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(activity, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    contentList.onRefreshComplete(); //完成刷新
                    mapList.clear();   //清除数据
                    mapList.addAll(newmapList);  //从新加载数据
                    contentList.setResultSize(newmapList.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 2:
                    contentList.onLoadComplete();   //完成加载
                    mapList.addAll(newmapList);     //加载数据
                    contentList.setResultSize(newmapList.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 3:
                    break;
                default:
                    break;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.homefragment_content, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();
        contentList = (AutoListView) activity.findViewById(R.id.content_list);


        mapList = new ArrayList<>();
        newmapList = new ArrayList<>();
        baseAdapter = new HomeBaseAdapter(activity, "homefragment_content", mapList);
        contentList.setAdapter(baseAdapter);
        contentList.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {//刷新
                index = 1;
                page = 1;
                Map<String, Object> maps = new HashMap<>();
                maps.put("page", page);
                maps.put("size", "15");
                GetData(HttpUrls.superList, maps);
            }
        });
        contentList.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {//加载
                index = 2;
                page = page + 1;
                Map<String, Object> maps = new HashMap<>();
                maps.put("page", page);
                maps.put("size", "15");
                GetData(HttpUrls.superList, maps);
            }
        });




        dialog = new CustomProgressDialog(activity, R.style.dialog);

        Map<String, Object> maps = new HashMap<>();
        maps.put("page", page);
        maps.put("size", "15");
        GetData(HttpUrls.superList, maps);

    }


    String msgg;

    public void GetData(String url, Map<String, Object> map) {
        newmapList = new ArrayList<>();
        OkhttpUtils.getInstance(activity).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result = object.getJSONObject("result");
                        JSONArray array = result.getJSONArray("array");
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject objects = array.getJSONObject(i);
                            map.put("user_partname", objects.getString("user_partname"));//用户本名
                            map.put("user_profession", objects.getString("user_profession"));// 职业
                            map.put("dynamic_type", objects.getString("dynamic_type"));//动态类型
                            map.put("user_country", objects.getString("user_country"));// 国家
                            map.put("user_tag", objects.getString("user_tag"));// 标签
                            map.put("user_id", objects.getString("user_id"));
                            map.put("dynamic_time", objects.getString("dynamic_time"));// 动态发布时间
                            map.put("user_nickname", objects.getString("user_nickname"));
//                            map.put("backgroundImage", objects.getString("backgroundImage"));//动态展示图片
                            map.put("user_coverimg", objects.getString("user_coverimg"));//用户背景图
                            map.put("user_photo", objects.getString("user_photo"));//头像
                            newmapList.add(map);
                        }
                        LogUtils.e("============" + newmapList);
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
}
