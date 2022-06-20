package com.example.admin.ccb;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.multidex.MultiDex;
import android.widget.ImageView;

import com.example.admin.ccb.utils.GlideImageUtils;
import com.lzy.ninegrid.NineGridView;
import com.lzy.okgo.OkGo;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import www.ccb.com.common.BaseApplication;


public class MyApplication extends BaseApplication {
    // private IWXAPI wxapi;
    // public static final String APP_ID = "wxd930ea5d5a258f4f";
    // public static final String AMAP_ID = "2a68f8e2a9f7c57c29a563bf50d3051d";
    // public static final String AMAP_WEB_API_ID = "229c3f9914d37931e34605a704bd84e7";
    private static MyApplication instance;

    /**
     * get context object
     *
     * @return
     */
    public static Context getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        ZXingLibrary.initDisplayOpinion(this);
        // wxapi = WXAPIFactory.createWXAPI(this, APP_ID, false);
        // wxapi.registerApp(APP_ID);
        try {
            OkGo.getInstance().init(this);
        } catch (NoClassDefFoundError ncdfe) {
        }
        NineGridView.setImageLoader(new GlideImageLoader());
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * Glide load
     */
    private class GlideImageLoader implements NineGridView.ImageLoader {

        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            GlideImageUtils.display(context, url, imageView, true);
        }

        @Override
        public void onDisplayImage(Context context, ImageView imageView, Integer src) {
            GlideImageUtils.display(context, src, imageView, true);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
    }
}
