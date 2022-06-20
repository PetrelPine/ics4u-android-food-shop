package www.ccb.com.common.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import www.ccb.com.common.BaseApplication;


public final class ViewUtils {

    public static float density = 1;
    private static int statusBarHeight = 0;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static int toolbarHeight;
    private static long lastClickTime;
    private static long lastClickTime2;

    static {
        density = BaseApplication.get().getResources().getDisplayMetrics().density;
    }

    private ViewUtils() {
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    /**
     * Change from px (pixels) to dp based on your phone's resolution
     */
    public static float pxToDp(float px) {
        return px / density;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * get height of the screen
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (0 == screenHeight)
            screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        return screenHeight;
    }

    /**
     * get width of the screen
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (0 == screenWidth)
            screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        return screenWidth;
    }

    public static int getDensityDpi(Context context) {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public static int getStatusBarHeight(Context context) {
        if (statusBarHeight == 0) {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
            } else {
                statusBarHeight = 0;
            }
        }

        return statusBarHeight;
    }

    // public static int getSystemBarHeight(Context context) {
    //     int result = 0;
    //     int resourceId = context.getResources().getIdentifier("system_bar_height", "dimen", "android");
    //
    //     if (resourceId > 0) {
    //         result = context.getResources().getDimensionPixelSize(resourceId);
    //     }
    //     return result;
    // }

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int identifier = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        return resources.getDimensionPixelOffset(identifier);
    }

    public static boolean isFullScreen(final Activity activity) {
        return (activity.getWindow().getAttributes().flags &
                WindowManager.LayoutParams.FLAG_FULLSCREEN) != 0;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean isTranslucentStatus(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return (activity.getWindow().getAttributes().flags &
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS) != 0;
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static boolean isFitsSystemWindows(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0).
                    getFitsSystemWindows();
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void setDrawable(View view, Drawable drawable) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            //noinspection deprecation
            view.setBackgroundDrawable(drawable);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void showSystemUi(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public static void hideSystemUi(View view) {
        view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    public static void hideSoftKeyboard(Context context, EditText editText) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    public static void showSoftKeyboard(Context context, EditText editText) {
        if (editText.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static boolean isFastDoubleClick2() {
        final long time = System.currentTimeMillis();
        long timeD = time - lastClickTime2;
        if (0 < timeD && timeD < 3000) {
            return true;
        }
        lastClickTime2 = time;
        return false;
    }

    public static void setTextSpanColor(@NonNull TextView textView, @NonNull String text, @NonNull String spanStr, @ColorInt int color) {
        if (TextUtils.isEmpty(text))
            return;
        int start = text.indexOf(spanStr);
        if (-1 == start) {
            textView.setText(text);
            return;
        }
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), start, start + spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    public static void setTextSpanSize(@NonNull TextView textView, @NonNull String text, @NonNull String spanStr, int size) {
        if (TextUtils.isEmpty(text))
            return;
        int start = text.indexOf(spanStr);
        if (-1 == start) {
            textView.setText(text);
            return;
        }
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new AbsoluteSizeSpan(size, true), start, start + spanStr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);
    }

    public static Spanned fromHtml(String source) {
        if (TextUtils.isEmpty(source))
            return null;
        if (AndroidUtils.isN())
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        else
            return Html.fromHtml(source);
    }
}
