package com.zone.forgithubproject.weidge;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.GeolocationPermissions;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.zone.forgithubproject.utils.L;

/**
 * Created by john on 2016/11/3.
 */
@SuppressWarnings("deprecation")
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 12, 0, 0));
//        progressbar.setBackgroundColor(getResources().getColor(R.color.colorBlackFont));
//        progressbar.setDrawingCacheBackgroundColor(getResources().getColor(R.color.colorBlackFont));
        addView(progressbar);
        //        setWebViewClient(new WebViewClient(){});
        setWebChromeClient(new WebChromeClient());
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onReceivedIcon(WebView view, Bitmap icon) {

            super.onReceivedIcon(view, icon);

        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin,GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
                L.d("====>"+String.valueOf(newProgress));
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}