package com.erbao.joystar.moudule.home.activity;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;
import com.erbao.joystar.moudule.start.activity.ForgetPasswordActivity;
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

public class ModifypasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView back;
    private TextView title;
    CustomProgressDialog dialog;
    CountDownTimer timer;
    private TextView modifyPhone;
    private EditText modifyCode;
    private TextView modifyGetcode;
    private EditText modifyPassword;
    private TextView modifyComple;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    ToastUtils.show(ModifypasswordActivity.this, msgg);
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
                            modifyGetcode.setText(getResources().getString(R.string.In_left) + millisUntilFinished / 1000 + getResources().getString(R.string.seconds));
//                            modifyGetcode.setBackgroundColor(getResources().getColor(R.color.color_Gray));
                            modifyGetcode.setEnabled(false);
                        }

                        @Override
                        public void onFinish() {
                            modifyGetcode.setText(getResources().getString(R.string.Again_send));
//                            modifyGetcode.setBackgroundColor(getResources().getColor(R.color.color_setupline));
                            modifyGetcode.setEnabled(true);
                        }
                    }.start();
                    break;
                case 2:
                    timer.cancel();
                    modifyGetcode.setText(getResources().getString(R.string.Get_code));
//                    modifyGetcode.setBackgroundColor(getResources().getColor(R.color.color_setupline));
                    modifyGetcode.setEnabled(true);
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
        setContentView(R.layout.activity_modifypassword);
        title = (TextView) findViewById(R.id.title);
        title.setText(getResources().getString(R.string.modifiypassword));
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);




        modifyPhone = (TextView) findViewById(R.id.modify_phone);
        modifyPhone.setText(SpUtil.get("phone","").toString());
        modifyCode = (EditText) findViewById(R.id.modify_code);
        modifyGetcode = (TextView) findViewById(R.id.modify_getcode);
        modifyPassword = (EditText) findViewById(R.id.modify_password);
        modifyComple = (TextView) findViewById(R.id.modify_comple);
        modifyGetcode.setOnClickListener(this);
        modifyComple.setOnClickListener(this);
        dialog=new CustomProgressDialog(this,R.style.dialog);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.modify_getcode:
                String phone = modifyPhone.getText().toString().trim();
                if (phone.length() < 5) {
                    ToastUtils.show(this, getResources().getString(R.string.errphone));
                } else {
                    handler.sendEmptyMessage(1);
                    CheckData.inputmanger(this);
                    Map<String, Object> map = new HashMap<>();
                    map.put("userPhone", phone);
                    map.put("id", SpUtil.get("user_id",""));
                    map.put("type", "2");
                    GetData(HttpUrls.sendCode, map, "send");
                }

                break;
            case R.id.modify_comple:
                String phones = modifyPhone.getText().toString().trim();
                String code=modifyCode.getText().toString().trim();
                String password=modifyPassword.getText().toString().trim();

                if (phones.length()<5){
                    ToastUtils.show(this,getResources().getString(R.string.errphone));
                }else if (code.length()<3){
                    ToastUtils.show(this,getResources().getString(R.string.edtcode));
                }else if (password.length()<8){
                    ToastUtils.show(this,getResources().getString(R.string.edtpassword));
                }else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", SpUtil.get("user_id",""));
                    map.put("code", code);
                    map.put("userPassword", MD5.getMD5(password).subSequence(0,16));
                    map.put("checkPassword", MD5.getMD5(password).subSequence(0,16));
                    GetData(HttpUrls.alterPassword, map,"modify");
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

                        } else if (type.equals("modify")) {
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
