package com.example.admin.ccb.activity;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.admin.ccb.R;
import com.example.admin.ccb.utils.PictureUtils;

import www.ccb.com.common.base.BaseActivity;
import www.ccb.com.common.utils.ToastUtils;


public class BaseWebViewActivity extends BaseActivity {
    private ProgressBar progressBar;
    private WebView mWebView;
    private String loadUrl;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_base_webview;
    }

    @Override
    public void initView() {
        UpTitle(null);
        progressBar = findViewById(R.id.progressBar);
        mWebView = findViewById(R.id.webView);
        loadUrl = getIntent().getStringExtra("url");
        if (TextUtils.isEmpty(loadUrl)) {
            loadUrl = "https://github.com/PetrelPine/ics4u-android-food-shop";
        }
        mWebView.loadUrl(loadUrl);
        // enable support for javascript
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        /*
         * Add js interface, parameter 1 is the local class name, parameter 2 is the tag; The H5 call requires the "window. flag. The method name in the class name can be called
         */
        mWebView.addJavascriptInterface(new JavaScriptObject(this), "android");
        // Not using cache
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        // Overrides the WebView's default behavior of using a third party or the system's default browser to open a web page with the WebView
        mWebView.setWebChromeClient(new MyWebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // Return value is true when control goes to WebView open, false calls system browser or third party browser
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                UpTitle(view.getTitle());
            }
        });
    }

    @Override
    public void initData() {
    }

    @Override
    protected void initList() {
        mWebView.setOnLongClickListener(view -> {
            final WebView.HitTestResult hitTestResult = mWebView.getHitTestResult();
            if (hitTestResult.getType() == WebView.HitTestResult.IMAGE_TYPE ||
                    hitTestResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
                PictureUtils.saveThePicture(hitTestResult.getExtra(), mContext);
                return true;
            } else {
                return true;
                // If it's not a picture, disable the long-press copy function
                // return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) mWebView.destroy();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    public class JavaScriptObject {
        public JavaScriptObject(Activity activity) {
        }

        @JavascriptInterface
        public void setToken(String token) {
            // token is the ID of the product. After you get the ID of the product, you can directly jump to the product details page and pass the ID to it
        }

        @JavascriptInterface
        public void definedShare(String ShareJson) {
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        /**
         * loading progress
         *
         * @param view
         * @param newProgress
         */
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(newProgress);
            if (progressBar.getProgress() == progressBar.getMax()) {
                progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }

        /**
         * alert pop-up window
         *
         * @return
         */
        @Override
        public boolean onJsAlert(WebView view, String url, final String message, JsResult result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showToast(mContext, message);
                }
            });
            result.confirm();
            return true;
        }
    }
}
