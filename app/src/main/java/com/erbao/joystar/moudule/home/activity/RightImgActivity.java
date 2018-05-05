package com.erbao.joystar.moudule.home.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.home.adapter.HomeBaseAdapter;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.views.HorizontalListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RightImgActivity extends AppCompatActivity {

    private TextView title;
    private ImageView back;
    private HorizontalListView lstv;
    List<Map<String,Object>> mapLis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_right_img);

        title = (TextView) findViewById(R.id.title);
        title.setText("");
        back = (ImageView) findViewById(R.id.back);


        lstv = (HorizontalListView) findViewById(R.id.lstv);


        LogUtils.e("========="+getIntent().getStringExtra("img_url"));
        LogUtils.e("========="+getIntent().getStringExtra("dataArray"));


        JSONArray data = null;
        try {
            data = new JSONArray(getIntent().getStringExtra("dataArray"));
            mapLis=new ArrayList<Map<String, Object>>();
            for (int j = 0; j < data.length(); j++) {
                JSONObject object1 = data.getJSONObject(j);
                Map<String,Object> map1=new HashMap<String, Object>();
                map1.put("img_url", object1.getString("img_url"));
                map1.put("dynamic_text", object1.getString("dynamic_text"));
                map1.put("img_id", object1.getString("img_id"));
                map1.put("screenshot", object1.getString("screenshot"));
                mapLis.add(map1);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        HomeBaseAdapter baseAdapter=new HomeBaseAdapter(this,"RightImgActivity",mapLis);
        lstv.setAdapter(baseAdapter);





         back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
