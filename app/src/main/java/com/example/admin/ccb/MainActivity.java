package com.example.admin.ccb;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.admin.ccb.fragment.ClassifyFragment;
import com.example.admin.ccb.fragment.HomeFragment;
import com.example.admin.ccb.fragment.MyFragment;
import com.example.admin.ccb.fragment.SpcFragment;

import www.ccb.com.common.base.BaseActivity;


public class MainActivity extends BaseActivity {

    private FrameLayout fl;
    private RadioGroup rg;
    private Fragment HomeFm = null, SpcFm = null, OrderFm = null, MyFm = null;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        fl = findViewById(R.id.fl);
        rg = findViewById(R.id.rg);

    }

    @Override
    protected void initData() {
        // Dynamically obtain memory storage permissions
        // Manifest.permission.ACCESS_COARSE_LOCATION,
        // Manifest.permission.ACCESS_FINE_LOCATION,
        // Manifest.permission.WRITE_EXTERNAL_STORAGE,
        // Manifest.permission.READ_EXTERNAL_STORAGE,
        // Manifest.permission.READ_PHONE_STATE
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
                                , Manifest.permission.READ_EXTERNAL_STORAGE
                                , Manifest.permission.ACCESS_FINE_LOCATION
                                , Manifest.permission.ACCESS_COARSE_LOCATION
                                , Manifest.permission.CAMERA},
                        1);
            }
        }
    }

    @Override
    protected void initList() {
        rg.setOnCheckedChangeListener(new RadioGroupOnCheckedChangeListener());
        rg.check(R.id.rb_home);
    }

    private final class RadioGroupOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
            if (HomeFm != null) {
                getSupportFragmentManager().beginTransaction().hide(HomeFm).commit();
            }
            if (SpcFm != null) {
                getSupportFragmentManager().beginTransaction().hide(SpcFm).commit();
            }
            if (OrderFm != null) {
                getSupportFragmentManager().beginTransaction().hide(OrderFm).commit();
            }
            if (MyFm != null) {
                getSupportFragmentManager().beginTransaction().hide(MyFm).commit();
            }
            switch (checkedId) {
                case R.id.rb_home:
                    if (HomeFm == null) {
                        HomeFm = new HomeFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.fl, HomeFm).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().show(HomeFm).commit();
                    }
                    break;
                case R.id.rb_shoppingcart:
                    if (SpcFm == null) {
                        SpcFm = new SpcFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.fl, SpcFm).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().show(SpcFm).commit();
                    }
                    break;

                case R.id.rb_orderfrom:
                    if (OrderFm == null) {
                        OrderFm = new ClassifyFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.fl, OrderFm).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().show(OrderFm).commit();
                    }
                    break;
                case R.id.rb_my:
                    if (MyFm == null) {
                        MyFm = new MyFragment();
                        getSupportFragmentManager().beginTransaction().add(R.id.fl, MyFm).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().show(MyFm).commit();
                    }
                    break;
            }

            // replace fragment
            // switch (checkedId) {
            //     case R.id.rb_home:
            //         getSupportFragmentManager().beginTransaction().replace(R.id.main_fl, fmHome).commit();
            //         break;
            //     case R.id.rb_shoppingcart:
            //         getSupportFragmentManager().beginTransaction().replace(R.id.main_fl, fmShoppingCart).commit();
            //         break;
            //
            //     case R.id.rb_orderfrom:
            //         getSupportFragmentManager().beginTransaction().replace(R.id.main_fl, fmOrderfrom).commit();
            //         break;
            //     case R.id.rb_my:
            //         getSupportFragmentManager().beginTransaction().replace(R.id.main_fl, fmMy).commit();
            //         break;
            // }
        }
    }
}
