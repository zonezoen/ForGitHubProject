package com.zone.forgithubproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


import com.zone.forgithubproject.myApplication.MyApplication;
import com.zone.forgithubproject.weidge.ProgressWebView;
import com.zone.forgithubproject.base.BaseActivity;
import com.zone.forgithubproject.utils.SPUtils;

import butterknife.ButterKnife;

/**
 * Created by john on 2016/7/18.
 */
public class WebViewActivity extends BaseActivity {


    ProgressWebView wv;
    private long djtime[] = new long[2];
    private Toolbar toolbar;
    private String from;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        setTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        wv = (ProgressWebView) findViewById(R.id.wv);
        setSupportActionBar(toolbar);
        iniBase();
        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");
        from = getIntent().getStringExtra("from");
        toolbar.setTitle(title);
        wv.setWebViewClient(new WebViewClient() {//防止调用系统的浏览器
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
//        wv.setWebChromeClient(new WebChromeClient(){
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                //get the newProgress and refresh progress bar
//                L.d("==progress==>"+String.valueOf(newProgress));
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                L.d("==Title==>"+title);
//
//            }
//        });
        wv.getSettings().setDefaultTextEncodingName("utf-8");
        //支持js
        wv.getSettings().setJavaScriptEnabled(true);
        //设置背景颜色 透明
//        wv.setBackgroundColor(Color.argb(0, 0, 0, 0));
        WebSettings webSettings = wv.getSettings();
        webSettings.setSupportZoom(true);
        //启用数据库
//        webSettings.setDatabaseEnabled(true);
//
////设置定位的数据库路径
//
//        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
//
//        webSettings.setGeolocationDatabasePath(dir);
//启用地理定位
        webSettings.setGeolocationEnabled(true);
//开启DomStorage缓存

        webSettings.setDomStorageEnabled(true);

//        wv.setWebChromeClient(new WebChromeClient() {//有这个好像就能通过webview定位了
//
//            @Override
//            public void onReceivedIcon(WebView view, Bitmap icon) {
//                super.onReceivedIcon(view, icon);
//            }
//
//            @Override
//            public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback) {
//                callback.invoke(origin, true, false);
//                super.onGeolocationPermissionsShowPrompt(origin, callback);
//            }
//        });
        wv.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (from != null && from.equals("advertising")) {
            Intent intent = null;
            String token = (String) SPUtils.get(WebViewActivity.this, "token", "");
            if (TextUtils.isEmpty(token)) {
                intent = new Intent(WebViewActivity.this, LoginActivity.class);
            } else {
                intent = new Intent(WebViewActivity.this, MainActivity.class);
                MyApplication.setToken(token);
            }
            startActivity(intent);
            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (from != null && from.equals("advertising")) {//判断是否来自广告页面
            toNextActivity();
            finish();
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK && wv.canGoBack()) {
            wv.goBack();
            return true;
        } else {
            System.arraycopy(djtime, 1, djtime, 0, djtime.length - 1);
            djtime[djtime.length - 1] = SystemClock.uptimeMillis();
            if (djtime[0] >= (SystemClock.uptimeMillis() - 1000)) {
                finish();
            } else {
                Toast.makeText(this, "再按一次返回", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }

    private void toNextActivity() {//判断去登录界面还是主界面
        Intent intent = null;
        String token = (String) SPUtils.get(WebViewActivity.this, "token", "");
        if (TextUtils.isEmpty(token)) {
            intent = new Intent(WebViewActivity.this, LoginActivity.class);
        } else {
            intent = new Intent(WebViewActivity.this, MainActivity.class);
            MyApplication.setToken(token);
        }
        startActivity(intent);
    }


}
