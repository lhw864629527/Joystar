package com.erbao.joystar.moudule.start.activity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.okhttp.HttpCallBack;
import com.erbao.joystar.okhttp.HttpUrls;
import com.erbao.joystar.okhttp.OkhttpUtils;
import com.erbao.joystar.utils.CheckData;
import com.erbao.joystar.utils.LogUtils;
import com.erbao.joystar.utils.MD5;
import com.erbao.joystar.utils.ToastUtils;
import com.erbao.joystar.views.CustomProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private EditText forgetpassPhone;
    private LinearLayout forgetpassLin;
    private EditText forgetpassCode;
    private TextView forgetpassGetcode;
    private EditText forgetpassPasswork;
    private TextView forgetpassComple;
    CustomProgressDialog dialog;
    CountDownTimer timer;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(ForgetPasswordActivity.this, msgg);
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
                case 1:
                    /** 倒计时60秒，一次1秒 */
                    timer = new CountDownTimer(100 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                            forgetpassGetcode.setText(getResources().getString(R.string.In_left) + millisUntilFinished / 1000 + getResources().getString(R.string.seconds));
                            forgetpassGetcode.setBackgroundColor(getResources().getColor(R.color.color_Gray));
                            forgetpassGetcode.setEnabled(false);
                        }

                        @Override
                        public void onFinish() {
                            forgetpassGetcode.setText(getResources().getString(R.string.Again_send));
                            forgetpassGetcode.setBackgroundColor(getResources().getColor(R.color.color_setupline));
                            forgetpassGetcode.setEnabled(true);
                        }
                    }.start();
                    break;
                case 2:
                    timer.cancel();
                    forgetpassGetcode.setText(getResources().getString(R.string.Get_code));
                    forgetpassGetcode.setBackgroundColor(getResources().getColor(R.color.color_setupline));
                    forgetpassGetcode.setEnabled(true);
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
        setContentView(R.layout.activity_forget_password);


        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.foegetpassword));
        back = (ImageView) findViewById(R.id.back);
        forgetpassPhone = (EditText) findViewById(R.id.forgetpass_phone);
        forgetpassLin = (LinearLayout) findViewById(R.id.forgetpass_lin);
        forgetpassCode = (EditText) findViewById(R.id.forgetpass_code);
        forgetpassGetcode = (TextView) findViewById(R.id.forgetpass_getcode);
        forgetpassPasswork = (EditText) findViewById(R.id.forgetpass_passwork);
        forgetpassComple = (TextView) findViewById(R.id.forgetpass_comple);

        back.setOnClickListener(this);
        forgetpassGetcode.setOnClickListener(this);
        forgetpassComple.setOnClickListener(this);

        forgetpassPhone.getBackground().setAlpha(80);
        forgetpassLin.getBackground().setAlpha(80);
        forgetpassPasswork.getBackground().setAlpha(80);

        dialog = new CustomProgressDialog(this, R.style.dialog);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.forgetpass_getcode:
                String phone = forgetpassPhone.getText().toString().trim();
                if (phone.length() < 5) {
                    ToastUtils.show(this, getResources().getString(R.string.errphone));
                } else {
                    handler.sendEmptyMessage(1);
                    CheckData.inputmanger(this);
                    Map<String, Object> map = new HashMap<>();
                    map.put("userPhone", phone);
                    map.put("type", "3");
                    GetData(HttpUrls.sendCode, map, "send");
                }

                break;
            case R.id.forgetpass_comple:
                String phones = forgetpassPhone.getText().toString().trim();
                String code=forgetpassCode.getText().toString().trim();
                String password=forgetpassPasswork.getText().toString().trim();

                if (phones.length()<5){
                    ToastUtils.show(this,getResources().getString(R.string.errphone));
                }else if (code.length()<3){
                    ToastUtils.show(this,getResources().getString(R.string.edtcode));
                }else if (password.length()<8){
                    ToastUtils.show(this,getResources().getString(R.string.edtpassword));
                }else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("userPhone", phones);
                    map.put("code", code);
                    map.put("userPassword", MD5.getMD5(password).subSequence(0,16));
                    map.put("checkPassword", MD5.getMD5(password).subSequence(0,16));
                    GetData(HttpUrls.forgetPassword, map,"forget");
                    dialog.show();
                }








                break;
            default:
                break;
        }
    }

    String msgg;

    public void GetData(String url, Map<String, Object> map, final String type) {
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("code").equals("200")) {
                        if (type.equals("send")) {
                            msgg = object.getString("promptMessage") + "";
                            handler.sendEmptyMessage(0);

                        } else if (type.equals("forget")) {
                            msgg = object.getString("promptMessage") + "";
                            handler.sendEmptyMessage(0);
                            finish();
                        }

                    } else {
                        msgg = object.getString("promptMessage") + "";
                        handler.sendEmptyMessage(0);
                        if (type.equals("send")) {
                            handler.sendEmptyMessage(2);
                        }
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
