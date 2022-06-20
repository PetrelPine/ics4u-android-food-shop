package com.example.admin.ccb.utils;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import www.ccb.com.common.utils.ToastUtils;


public class PictureUtils {

    public static void saveThePicture(String UriPath, Context context) {
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(UriPath));
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.allowScanningByMediaScanner();
            request.setShowRunningNotification(false);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            String loadName = UriPath.replace("/", "");
            request.setDestinationInExternalPublicDir("/download/FreshestFood/", System.currentTimeMillis() + ".jpg");
            long id = downloadManager.enqueue(request);
            IntentFilter intentFilter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            DownloadMangerReceiver mDownloadMangerReceiver = new DownloadMangerReceiver();
            context.registerReceiver(mDownloadMangerReceiver, intentFilter);
        } catch (SecurityException re) {
            re.printStackTrace();
            ToastUtils.showToast("Image download failed, please give storage permission.");
        } catch (JsonSyntaxException je) {
            je.printStackTrace();
            ToastUtils.showToast("Data Type Error");
        } catch (Exception e) {
            e.printStackTrace();
            ToastUtils.showToast("Image download failed, please give storage permission.");
        }
    }

    static class DownloadMangerReceiver extends BroadcastReceiver {
        private DownloadManager manager;

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            manager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                Toast.makeText(context, "File Download Complete", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
