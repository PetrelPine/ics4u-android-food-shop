package www.ccb.com.common.utils;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import www.ccb.com.common.BaseApplication;


public class UiUtils {

    private static long lastClickTime;

    public static void copyContent(Context mContext, String copyContent) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(copyContent);
        ToastUtils.showToast(mContext, "Copied");
    }

    public static String getCopyContent(Context mContext) {
        ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        String copyContent = cm.getText().toString();
        return copyContent;
    }

    public static int getScreenWidth(Context context) {
        // if (screenWidth != 0) {
        // 	return screenWidth;
        // }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int dp2px(int dip) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dp(int px) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dp2px(Context content, int dip) {
        final float scale = content.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static int px2dp(Context content, int px) {
        final float scale = content.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static String[] getStringArray(int id) {
        return getResources().getStringArray(id);
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getDownloadPath() {
        return Environment.getExternalStorageDirectory().getPath() + "/download";
    }

    public static Context getContext() {
        return BaseApplication.get();
    }

    public static View inflate(int id) {
        return View.inflate(getContext(), id, null);
    }

    public static Drawable getDrawable(int id) {
        return getResources().getDrawable(id);
    }

    public static int getDimens(int id) {
        return getResources().getDimensionPixelSize(id);
    }

    public static String getString(int id) {
        return getResources().getString(id);
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static int getAppVersionCode(Context context) {
        int VersionCode = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            VersionCode = pi.versionCode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return VersionCode;
    }

    public static boolean isFastDoubleClick(int i) {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < i) {
            return false;
        }
        lastClickTime = time;
        return true;
    }

    public static Bitmap getBitmapFromUrl(String url, double width, double height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        // Bitmap bitmap = BitmapFactory.decodeFile(url);

        BitmapFactory.Options
                opts = new BitmapFactory.Options();

        opts.inSampleSize = 4;

        Bitmap bitmap = BitmapFactory.decodeFile(url, opts);

        // prevent OOM
        options.inJustDecodeBounds = false;
        int mWidth = bitmap.getWidth();
        int mHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = 1;
        float scaleHeight = 1;
        // try {
        //    ExifInterface exif = new ExifInterface(url);
        //    String model = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        // } catch (IOException e) {
        //    e.printStackTrace();
        // }
        if (mWidth <= mHeight) {
            scaleWidth = (float) (width / mWidth);
            scaleHeight = (float) (height / mHeight);
        } else {
            scaleWidth = (float) (height / mWidth);
            scaleHeight = (float) (width / mHeight);
        }
        // matrix.postRotate(90);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, mWidth, mHeight, matrix, true);
        bitmap.recycle();
        return newBitmap;
    }
}
