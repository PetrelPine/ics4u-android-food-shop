package com.example.admin.ccb.utils;

import android.content.Context;

import java.util.List;


public class PhotoDgUtils {
    public static void show(Context context, List<String> photoLists, int position) {
        PhotoShowDialog.get(context, photoLists, position).show();
    }

    public static void show(Context context, String photos) {
        PhotoShowDialog.get(context, photos).show();
    }
}
