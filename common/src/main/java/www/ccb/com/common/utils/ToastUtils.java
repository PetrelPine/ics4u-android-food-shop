package www.ccb.com.common.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class ToastUtils {
    public static boolean isShow = true;
    private static Toast toast;
    private static Toast centerToast;

    private ToastUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showToast(Context context, String text) {

        if (toast == null)
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setText(text);
        toast.show();
    }

    public static void makeText(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(String text) {
        if (toast == null) {
            toast = Toast.makeText(UiUtils.getContext(), text, Toast.LENGTH_SHORT);
        }
        toast.setText(text);
        toast.show();
    }

    public static void showCenterToast(String text) {
        if (centerToast == null) {
            centerToast = Toast.makeText(UiUtils.getContext(), text, Toast.LENGTH_SHORT);
        }
        centerToast.setGravity(Gravity.CENTER, 0, 0);
        centerToast.setText(text);
        centerToast.show();
    }

    public static void showCenterToast(Context context, String text) {
        if (centerToast == null) {
            centerToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        centerToast.setGravity(Gravity.CENTER, 0, 0);
        centerToast.setText(text);
        centerToast.show();
    }

    public static Toast showImageToast(Context context, String text, int image) {
        Toast imagetoast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        LinearLayout toastView = (LinearLayout) imagetoast.getView();
        ImageView imageCodeProject = new ImageView(context);
        imageCodeProject.setPadding(0, 0, 5, 0);
        imageCodeProject.setImageResource(image);
        toastView.addView(imageCodeProject, 0);
        imagetoast.setGravity(Gravity.CENTER, 0, 0);
        imagetoast.setText(text);
        return imagetoast;
    }

    public static void GsonExtremely() {
        if (toast == null) {
            toast = Toast.makeText(UiUtils.getContext(), "Server Not Working", Toast.LENGTH_SHORT);
        }
        toast.setText("Server Not Working");
        toast.show();
    }

    public static void failNetRequest() {
        if (toast == null) {
            toast = Toast.makeText(UiUtils.getContext(), "Request Server Failed", Toast.LENGTH_SHORT);
        }
        toast.setText("Request Server Failed");
        toast.show();
    }

    public static void failRSADecrypt() {
        if (toast == null) {
            toast = Toast.makeText(UiUtils.getContext(), "Decrypt Failed", Toast.LENGTH_SHORT);
        }
        toast.setText("Decrypt Failed");
        toast.show();
    }

    public static void showStaticToast(final Activity act, final String msg) {
        String nowThreadName = Thread.currentThread().getName();
        if ("main".equals(nowThreadName)) {
            if (isShow)
                showToast(act, msg);
        } else {
            act.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isShow)
                        showToast(act, msg);
                }
            });
        }
    }
}
