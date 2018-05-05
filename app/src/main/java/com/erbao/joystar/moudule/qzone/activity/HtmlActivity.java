package com.erbao.joystar.moudule.qzone.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.erbao.joystar.R;

public class HtmlActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView title;
    private ImageView back;
    private WebView htmlWeb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);



        title = (TextView) findViewById(R.id.title);
        title.setText("");
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        htmlWeb = (WebView) findViewById(R.id.html_web);
        //设置WebView属性，能够执行Javascript脚本
        htmlWeb.getSettings().setJavaScriptEnabled(true);
        //加载需要显示的网页
        htmlWeb.loadUrl("http://www.baidu.com/");
        //设置Web视图
        htmlWeb.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }
}
