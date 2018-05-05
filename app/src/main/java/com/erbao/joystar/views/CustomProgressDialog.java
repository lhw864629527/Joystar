package com.erbao.joystar.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.erbao.joystar.R;


/**
 * Created by Android on 2017/5/1.
 */

public class CustomProgressDialog extends ProgressDialog {
    public CustomProgressDialog(Context context) {
        super(context);
    }
    public CustomProgressDialog(Context context, int theme) {
        super(context,theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customprogressdialog);
        LinearLayout lin = (LinearLayout) findViewById(R.id.dialo_lin);
        lin.getBackground().setAlpha(180);

//  setContentView(android.R.layout.alert_dialog_progress);
    }
    public static CustomProgressDialog show (Context context) {
        CustomProgressDialog dialog = new CustomProgressDialog(context,R.style.dialog);

//        Window wd= dialog.getWindow();
//        WindowManager.LayoutParams lp = wd.getAttributes();
//        lp.alpha = 0.1f;
//        wd.setAttributes(lp);
//        //lp.alpha = 0.5f 设置透明度，值可以自己测试
//        dialog.setCancelable(false);
//        dialog.show();
        return dialog;
    }




//    代码调用：
//    CustomProgressDialog loading_Dialog = new CustomProgressDialog(LoginActivity.this,R.style.dialog);
//    Window wd= loading_Dialog.getWindow();
//    WindowManager.LayoutParams lp = wd.getAttributes();
//    lp.alpha = 0.5f;
//        wd.setAttributes(lp);
//    //lp.alpha = 0.5f 设置透明度，值可以自己测试
//        loading_Dialog.setCancelable(false);
//        loading_Dialog.show();




}