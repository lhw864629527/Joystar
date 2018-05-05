package com.erbao.joystar.moudule.home.activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.base.BaseApplication;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.views.MyHorizontalScrollView;

import static com.erbao.joystar.okhttp.HttpUrls.isLogin;

public class SetupActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private TextView setupLanguage;
    private TextView setupModifiypassword;
    private TextView setupLogout;
    private TextView title;
    private View setupModifiypasswordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

BaseApplication.addActivity(this);

        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.setup));
        back = (ImageView) findViewById(R.id.back);
        setupLanguage = (TextView) findViewById(R.id.setup_language);
        setupModifiypassword = (TextView) findViewById(R.id.setup_modifiypassword);
        setupModifiypasswordView = (View) findViewById(R.id.setup_modifiypassword_view);

        setupLogout = (TextView) findViewById(R.id.setup_logout);
        back.setOnClickListener(this);
        setupLanguage.setOnClickListener(this);
        setupModifiypassword.setOnClickListener(this);
        setupLogout.setOnClickListener(this);
        if (isLogin) {
            setupModifiypassword.setVisibility(View.VISIBLE);
            setupLogout.setVisibility(View.VISIBLE);
            setupModifiypasswordView.setVisibility(View.VISIBLE);
        } else {
            setupModifiypassword.setVisibility(View.GONE);
            setupLogout.setVisibility(View.GONE);
            setupModifiypasswordView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.setup_language:
                startActivity(new Intent(this, LanguageActivity.class));
                break;
            case R.id.setup_modifiypassword:
                startActivity(new Intent(this, ModifypasswordActivity.class));
                break;
            case R.id.setup_logout:
                setlotout();
                break;
            default:
                break;

        }
    }

    public void setlotout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SetupActivity.this);
        View layout = LayoutInflater.from(SetupActivity.this).inflate(R.layout.setup_withdrawaccount, null);
        builder.setView(layout);
        final Dialog dialog = builder.show();
        dialog.setCancelable(true);
        layout.findViewById(R.id.lotout_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        layout.findViewById(R.id.lotout_confim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUrls.isLogin = false;
                SpUtil.put("isLogin", "0");
                SpUtil.put("user_id", "");
                dialog.dismiss();
                finish();
            }
        });
    }


}
