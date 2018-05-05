package com.erbao.joystar.moudule.home.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;

public class MyProfile_NameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    String type, text;
    private EditText profileEdts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile__name);
        title = (TextView) findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("title"));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        profileEdts = (EditText) findViewById(R.id.profile_edts);

        type = getIntent().getStringExtra("type");
        text = getIntent().getStringExtra("text");
        profileEdts.setText(text);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                setdata(profileEdts.getText().toString().trim());
                finish();
                break;
            default:
                break;

        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setdata(profileEdts.getText().toString().trim());
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    public void setdata(String text) {
        if (type.equals("name")) {
            MyProfileActivity.mapdata.put("name", text);
        } else if (type.equals("nickname")) {
            MyProfileActivity.mapdata.put("nickname", text);
        } else if (type.equals("county")) {
            MyProfileActivity.mapdata.put("county", text);
        } else if (type.equals("pro")) {
            MyProfileActivity.mapdata.put("pro", text);
        } else if (type.equals("tag")) {
            MyProfileActivity.mapdata.put("tag", text);
        }
    }

}
