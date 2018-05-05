package com.erbao.joystar.moudule.home.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.home.adapter.HomeBaseAdapter;
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

/**
 * Created by asus on 2018/1/22.
 */

public class HomeFragment_right extends Fragment {
    Activity activity;
    private AutoListView homeRightList;
    private View vHead;
    CustomProgressDialog dialog;
    List<Map<String, Object>> mapList;
    List<Map<String, Object>> newmapList;
    HomeBaseAdapter baseAdapter;
    int page = 1, index = 1;
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
                    homeRightList.onRefreshComplete(); //完成刷新
                    mapList.clear();   //清除数据
                    mapList.addAll(newmapList);  //从新加载数据
                    homeRightList.setResultSize(newmapList.size());    //添加数据
                    baseAdapter.notifyDataSetChanged();       //刷新UI
                    break;
                case 2:
                    homeRightList.onLoadComplete();   //完成加载
                    mapList.addAll(newmapList);     //加载数据
                    homeRightList.setResultSize(newmapList.size());    //添加数据
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
        return inflater.inflate(R.layout.homefragment_right, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity = getActivity();

        dialog = new CustomProgressDialog(activity, R.style.dialog);
        mapList = new ArrayList<>();
        homeRightList = (AutoListView) activity.findViewById(R.id.home_right_list);
        //      头布局
        vHead = View.inflate(activity, R.layout.homefragment_reght_listhand_item, null);
        homeRightList.addHeaderView(vHead);
        baseAdapter = new HomeBaseAdapter(activity, "homefragment_right", mapList);
        homeRightList.setAdapter(baseAdapter);
        homeRightList.setOnRefreshListener(new AutoListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                index = 1;
                page = 1;
                Map<String, Object> map = new HashMap<>();
                map.put("userId", "");
                map.put("page", page);
                map.put("size", "15");
                GetData(HttpUrls.dynamicList, map);
            }
        });
        homeRightList.setOnLoadListener(new AutoListView.OnLoadListener() {
            @Override
            public void onLoad() {
                index = 2;
                page = page + 1;
                Map<String, Object> map = new HashMap<>();
                map.put("userId", "");
                map.put("page", page);
                map.put("size", "15");
                GetData(HttpUrls.dynamicList, map);
            }
        });

        Map<String, Object> map = new HashMap<>();
        map.put("userId","");
        map.put("page", page);
        map.put("size", "15");
        GetData(HttpUrls.dynamicList, map);







    }

    //dynamicList
    String msgg;

    public void GetData(String url, Map<String, Object> map) {
        newmapList = new ArrayList<>();
        OkhttpUtils.getInstance(activity).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response==right===" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result = object.getJSONObject("result");
                        JSONArray array = result.getJSONArray("array");
                        for (int i = 0; i < array.length(); i++) {
                            Map<String, Object> map = new HashMap<String, Object>();
                            JSONObject objects = array.getJSONObject(i);
                            map.put("user_profession", objects.getString("user_profession"));
                            map.put("user_status", objects.getString("user_status"));
                            map.put("user_country", objects.getString("user_country"));
                            map.put("user_tag", objects.getString("user_tag"));
                            map.put("dynamic_time", objects.getString("dynamic_time"));
                            map.put("dynamic_id", objects.getString("dynamic_id"));
                            map.put("user_coverimg", objects.getString("user_coverimg"));
                            map.put("user_partname", objects.getString("user_partname"));
                            map.put("dynamic_type", objects.getString("dynamic_type"));
                            map.put("user_id", objects.getString("user_id"));
                            map.put("user_nickname", objects.getString("user_nickname"));
                            map.put("user_photo", objects.getString("user_photo"));

                            JSONArray data = objects.getJSONArray("data");
                            List<Map<String,Object>> mapList=new ArrayList<Map<String, Object>>();
                            for (int j = 0; j < data.length(); j++) {
                                JSONObject object1 = data.getJSONObject(j);
                                Map<String,Object> map1=new HashMap<String, Object>();
                                map1.put("img_url", object1.getString("img_url"));
                                map1.put("dynamic_text", object1.getString("dynamic_text"));
                                map1.put("img_id", object1.getString("img_id"));
                                map1.put("screenshot", object1.getString("screenshot"));
                                mapList.add(map1);
                            }
                            map.put("data", mapList);
                            map.put("dataArray", data);
                            newmapList.add(map);

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

}
