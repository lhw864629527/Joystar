package com.erbao.joystar.moudule.start.activity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
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

import io.vov.vitamio.utils.Log;

public class RegisteredActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView title;
    private ImageView back;
    private EditText registerPhone;
    private LinearLayout registerLin;
    private EditText registerCode;
    private TextView registerGetcode;
    private EditText registerPasswork;
    private EditText registerNickname;
    private TextView registerComple;
    CountDownTimer timer;
    String msgg = "";
    CustomProgressDialog dialog;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:

                    /** 倒计时60秒，一次1秒 */
                    timer = new CountDownTimer(100 * 1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            // TODO Auto-generated method stub
                            registerGetcode.setText(getResources().getString(R.string.In_left) + millisUntilFinished / 1000 + getResources().getString(R.string.seconds));
                            registerGetcode.setBackgroundColor(getResources().getColor(R.color.color_Gray));
                            registerGetcode.setEnabled(false);
                        }

                        @Override
                        public void onFinish() {
                            registerGetcode.setText(getResources().getString(R.string.Again_send));
                            registerGetcode.setBackgroundColor(getResources().getColor(R.color.color_setupline));
                            registerGetcode.setEnabled(true);
                        }
                    }.start();
                    break;
                case 1:
                    timer.cancel();
                    registerGetcode.setText(getResources().getString(R.string.Get_code));
                    registerGetcode.setBackgroundColor(getResources().getColor(R.color.color_setupline));
                    registerGetcode.setEnabled(true);
                    break;
                case 2:
                    ToastUtils.show(RegisteredActivity.this, msgg);
                    if (dialog.isShowing()){
                        dialog.dismiss();
                    }
                    break;
                case 3:
                    finish();
                    break;
                case 4:
                    break;
                case 5:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);


        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.register));
        back = (ImageView) findViewById(R.id.back);
        registerPhone = (EditText) findViewById(R.id.register_phone);
        registerLin = (LinearLayout) findViewById(R.id.register_lin);
        registerCode = (EditText) findViewById(R.id.register_code);
        registerGetcode = (TextView) findViewById(R.id.register_getcode);
        registerPasswork = (EditText) findViewById(R.id.register_passwork);
        registerNickname = (EditText) findViewById(R.id.register_nickname);
        registerComple = (TextView) findViewById(R.id.register_comple);

        registerPhone.getBackground().setAlpha(80);
        registerLin.getBackground().setAlpha(80);
        registerPasswork.getBackground().setAlpha(80);
        registerNickname.getBackground().setAlpha(80);


        back.setOnClickListener(this);
        registerGetcode.setOnClickListener(this);
        registerComple.setOnClickListener(this);

         dialog=new CustomProgressDialog(this,R.style.dialog);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.register_getcode:
                CheckData.inputmanger(this);
                String phone = registerPhone.getText().toString().trim();
                if (phone.length() < 5) {
                    ToastUtils.show(this, getResources().getString(R.string.errphone));
                } else {
                    handler.sendEmptyMessage(0);
                    Map<String, Object> map = new HashMap<>();
                    map.put("userPhone", phone);
                    map.put("type", "1");
                    GetData(HttpUrls.sendCode, map,"send");


                }

                break;
            case R.id.register_comple:
                CheckData.inputmanger(this);
                String phones = registerPhone.getText().toString().trim();
                String code=registerCode.getText().toString().trim();
                String password=registerPasswork.getText().toString().trim();
                String name=registerNickname.getText().toString().trim();
                if (phones.length()<5){
                    ToastUtils.show(this,getResources().getString(R.string.errphone));
                }else if (code.length()<3){
                    ToastUtils.show(this,getResources().getString(R.string.edtcode));
                }else if (password.length()<8){
                    ToastUtils.show(this,getResources().getString(R.string.edtpassword));
                }else if (name.length()<1){
                    ToastUtils.show(this,getResources().getString(R.string.edtname));
                }else {

                    Map<String, Object> map = new HashMap<>();
                    map.put("userPhone", phones);
                    map.put("userNickname", name);
                    map.put("code", code);
                    map.put("userPassword", MD5.getMD5(password).subSequence(0,16));
                    map.put("checkPassword", MD5.getMD5(password).subSequence(0,16));
                    GetData(HttpUrls.registerPhone, map,"rede");

                    dialog.show();


                }



                break;
            default:
                break;
        }
    }


    public void GetData(String url, Map<String, Object> map, final String type) {
        OkhttpUtils.getInstance(this).Post(url, map, new HttpCallBack<String>() {
            @Override
            public void onSuccess(Object tag, String response) {
                LogUtils.e("========response=====" + response);
                try {
                    JSONObject object=new JSONObject(response);
                    if (object.getString("code").equals("200")){
                        if (type.equals("send")){
                            msgg=object.getString("promptMessage")+"";
                            handler.sendEmptyMessage(2);
                        }else if (type.equals("rede")){
                            msgg=object.getString("promptMessage")+"";
                            handler.sendEmptyMessage(2);
                            handler.sendEmptyMessage(3);
                        }

                    }else {
                        msgg=object.getString("promptMessage")+"";
                        handler.sendEmptyMessage(2);
                        if (type.equals("send")){
                            handler.sendEmptyMessage(1);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(Object tag, String msg) {
               msgg=getResources().getText(R.string.Serverexception)+"";
                handler.sendEmptyMessage(2);
            }
        });


    }
}
