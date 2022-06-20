package www.ccb.com.common.utils;

import android.util.Log;


public class L {
    static boolean isDebag = true;

    public static void i(String content) {
        if (isDebag) {
            Log.i("Log", content);
        }
    }

    public static void e(Object content) {
        if (isDebag) {
            Log.e("Log", content + "");
        }
    }

    public static void i(String tag, String content) {
        if (isDebag) {
            Log.i(tag, content);
        }
    }

    public static void v(String content) {
        if (isDebag) {
            Log.v("Log", content);
        }
    }

    public static void v(String tag, String content) {
        if (isDebag) {
            Log.v(tag, content);
        }
    }

    public static void out(String tag, String msg) {
        if (isDebag) {
            if (tag == null || tag.length() == 0 || msg == null || msg.length() == 0) return;
            int segmentSize = 3 * 1024;
            long length = msg.length();
            if (length <= segmentSize) {
                Log.i(tag, msg);
            } else {
                while (msg.length() > segmentSize) {
                    String logContent = msg.substring(0, segmentSize);
                    msg = msg.replace(logContent, "");
                    Log.i(tag, logContent);
                }
                Log.i(tag, msg);
            }
        }
    }

    public static void cc(String msg) {
        cc("CCBB", msg);
    }

    public static void cc(String tag, String msg) {
        Log.i(tag, msg);
    }
}
