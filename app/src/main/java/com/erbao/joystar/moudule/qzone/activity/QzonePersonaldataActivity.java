package com.erbao.joystar.moudule.qzone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.utils.GlideImageLoader;
import com.erbao.joystar.utils.SpUtil;

public class QzonePersonaldataActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private ImageView QzonePdataImg;
    private TextView QzonePdataNickname;
    private TextView QzonePdataName;
    private TextView QzonePdataUnsubs;
    private TextView QzonePdataPeople;
    private TextView QzonePdataGallery;
    private TextView QzonePdataAbout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qzonepersonaldata);


        title = (TextView) findViewById(R.id.title);
        title.setText("");
        back = (ImageView) findViewById(R.id.back);
        QzonePdataImg = (ImageView) findViewById(R.id.QzonePdata_img);
        QzonePdataNickname = (TextView) findViewById(R.id.QzonePdata_nickname);
        QzonePdataName = (TextView) findViewById(R.id.QzonePdata_name);
        QzonePdataUnsubs = (TextView) findViewById(R.id.QzonePdata_unsubs);
        QzonePdataPeople = (TextView) findViewById(R.id.QzonePdata_people);
        QzonePdataGallery = (TextView) findViewById(R.id.QzonePdata_gallery);
        QzonePdataAbout = (TextView) findViewById(R.id.QzonePdata_about);

        back.setOnClickListener(this);
        QzonePdataUnsubs.setOnClickListener(this);
        QzonePdataPeople.setOnClickListener(this);
        QzonePdataGallery.setOnClickListener(this);
        QzonePdataAbout.setOnClickListener(this);


        String usetid = getIntent().getStringExtra("user_id");
        GlideImageLoader.showCircle(this, getIntent().getStringExtra("user_photo"), QzonePdataImg, R.mipmap.circle_bg_img);
        QzonePdataNickname.setText(getIntent().getStringExtra("user_nickname"));
        QzonePdataName.setText(getIntent().getStringExtra("user_partname"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.QzonePdata_unsubs:
                break;
            case R.id.QzonePdata_people:
                break;
            case R.id.QzonePdata_gallery:
                break;
            case R.id.QzonePdata_about:
                break;
            default:
                break;


        }
    }
}
