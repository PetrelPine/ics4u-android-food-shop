package com.example.admin.ccb.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.BaseWebViewActivity;
import com.example.admin.ccb.activity.SelectPhotoActivity;
import com.example.admin.ccb.activity.TestActivity;
import com.example.admin.ccb.utils.GlideImageUtils;

import java.util.Calendar;
import java.util.Date;

import www.ccb.com.common.base.BaseFragment;
import www.ccb.com.common.utils.ToastUtils;


public class MyFragment extends BaseFragment implements View.OnClickListener {

    private ImageView ivPhoto;
    private TextView tvSelectTime, tvSelectLocation, tvKefu, tvRes, tvSet;

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my, container, false);
    }

    @Override
    public void initView(View view) {
        tvSelectTime = view.findViewById(R.id.tvSelectTime);
        ivPhoto = view.findViewById(R.id.ivPhoto);
        tvKefu = view.findViewById(R.id.tvKefu);
        tvRes = view.findViewById(R.id.tvRes);
        tvSet = view.findViewById(R.id.tvSet);
        tvSelectLocation = view.findViewById(R.id.tvSelectLocation);
    }

    @Override
    public void loadData() {
        GlideImageUtils.DisplayCircle(mContext, R.drawable.ava_mine, ivPhoto);
    }

    @Override
    public void initListener() {
        ivPhoto.setOnClickListener(this);
        tvSelectTime.setOnClickListener(this);
        tvSelectLocation.setOnClickListener(this);
        tvKefu.setOnClickListener(this);
        tvRes.setOnClickListener(this);
        tvSet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivPhoto:
                startActivity(new Intent(mContext, SelectPhotoActivity.class));
                break;
            case R.id.tvSelectTime:
                // Time Picker
                TimePickerView pvTime = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        tvSelectTime.setText(date + "");
                    }
                }).build();
                pvTime.setDate(Calendar.getInstance());// Note: Whether to use this method or not depends on your requirements (generally accurate to the second). This item can reset the current time when the selector is popped up to avoid the problem that the selected time does not match the current time because the time has been set after initialization.
                pvTime.show();
                break;
            case R.id.tvSelectLocation:
                break;
            case R.id.tvRes:
                startActivity(new Intent(mContext, BaseWebViewActivity.class));
                break;
            case R.id.tvKefu:
                if (checkApkExist(mContext, "com.tencent.mobileqq")) {
                    ToastUtils.showToast(mContext, "Opening QQ Now...");
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=123456789&version=1")));
                } else {
                    ToastUtils.showToast(mContext, "QQ is not installed!");
                }
                break;
            case R.id.tvSet:
                startActivity(new Intent(mContext, TestActivity.class));
                break;
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
}
