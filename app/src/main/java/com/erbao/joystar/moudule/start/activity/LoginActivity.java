package com.erbao.joystar.moudule.start.activity;

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
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.CheckData;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.MD5;
import com.erbao.joystar.utils.SpUtil;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private EditText loginPhone;
    private EditText loginPassword;
    private TextView loginForgotpassword;
    private TextView loginRegister;
    private TextView loginLogin;
    private ImageView loginWechat;
    String msgg;
    String phone;
    CustomProgressDialog dialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(LoginActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    setResult(1);
                    finish();
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
        setContentView(R.layout.activity_login);

        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.Login));
        back = (ImageView) findViewById(R.id.back);
        loginPhone = (EditText) findViewById(R.id.login_phone);
        loginPassword = (EditText) findViewById(R.id.login_password);
        loginForgotpassword = (TextView) findViewById(R.id.login_forgotpassword);
        loginRegister = (TextView) findViewById(R.id.login_register);
        loginLogin = (TextView) findViewById(R.id.login_login);
        loginWechat = (ImageView) findViewById(R.id.login_wechat);
        back.setOnClickListener(this);
        loginForgotpassword.setOnClickListener(this);
        loginRegister.setOnClickListener(this);
        loginLogin.setOnClickListener(this);
        loginWechat.setOnClickListener(this);

        dialog = new CustomProgressDialog(this, R.style.dialog);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.login_login:
                CheckData.inputmanger(this);
                 phone = loginPhone.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();
                if (phone.length() < 5) {
                    ToastUtils.show(this, getResources().getString(R.string.errphone));
                } else if (password.length() < 8) {
                    ToastUtils.show(this, getResources().getString(R.string.edtpassword));
                } else {

                    Map<String, Object> map = new HashMap<>();
                    map.put("userPhone", phone);
                    map.put("userPassword", MD5.getMD5(password).subSequence(0, 16));
                    GetData(HttpUrls.userLoginPhone, map);
                    dialog.show();

                }


                break;
            case R.id.login_forgotpassword:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.login_register:
                startActivity(new Intent(this, RegisteredActivity.class));
                break;
            case R.id.login_wechat:
                break;
            default:
                break;

        }

    }

    public void GetData(String url, Map<String, Object> map) {
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        JSONObject result = object.getJSONObject("result");
                        SpUtil.put("user_partname", result.getString("user_partname"));//本名
                        SpUtil.put("user_profession", result.getString("user_profession"));//职业
//                        SpUtil.put("user_status",result.getString("user_status"));//用户角色（1：表示高级用户   0：表示普通用户）
                        SpUtil.put("user_country", result.getString("user_country"));
                        SpUtil.put("user_id", result.getString("user_id"));
                        SpUtil.put("user_tag", result.getString("user_tag"));// 标签
                        SpUtil.put("user_nickname", result.getString("user_nickname"));
                        SpUtil.put("user_phone", result.getString("user_phone"));
                        SpUtil.put("user_coverimg", result.getString("user_coverimg"));//封面图
                        SpUtil.put("user_photo", result.getString("user_photo"));

                        SpUtil.put("phone", phone);

                        HttpUrls.isLogin = true;
                        SpUtil.put("isLogin", "1");
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
}
