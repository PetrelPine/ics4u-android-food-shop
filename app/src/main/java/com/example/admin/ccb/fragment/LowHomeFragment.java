package com.example.admin.ccb.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.BaseWebViewActivity;
import com.example.admin.ccb.activity.FrameAActivity;
import com.example.admin.ccb.activity.FrameBActivity;
import com.example.admin.ccb.activity.FrameCActivity;
import com.example.admin.ccb.activity.FrameZActivity;
import com.example.admin.ccb.activity.GoodsInfoActivity;
import com.example.admin.ccb.activity.SearchActivity;
import com.example.admin.ccb.activity.URLAActivity;
import com.example.admin.ccb.adapter.HomeAdapter;
import com.example.admin.ccb.adapter.HomeMenuAdapter;
import com.example.admin.ccb.bean.HomeGoodsBean;
import com.example.admin.ccb.bean.HomeMenuBean;
import com.example.admin.ccb.utils.DialogUtils;
import com.example.admin.ccb.utils.GlideImageLoader;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.example.admin.ccb.utils.ResData;
import com.example.admin.ccb.view.UPMarqueeView;
import com.gyf.barlibrary.ImmersionBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import www.ccb.com.common.base.BaseFragment;
import www.ccb.com.common.utils.UiUtils;
import www.ccb.com.common.widget.dialog.SingleSelectDialog;


public class LowHomeFragment extends BaseFragment {

    private final int REQUEST_CODE = 106;
    private final int MANIFESTPERMISSIONCAMERA = 17;
    private HomeAdapter rvAp;
    private RecyclerView rv;
    private View headerView;
    private Banner banner;
    private RelativeLayout llChange;
    private UPMarqueeView upView;
    private RecyclerView menuRecyclerView;
    private View vScan;
    private ImageView ivAd;
    private int height;  // Where does the slide change color completely
    private int ScrollUnm = 0;  // The sum of the sliding distances
    private HomeGoodsBean homeGoods;

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lowhome, container, false);
    }

    @Override
    public void initView(View view) {
        llChange = view.findViewById(R.id.llChange);
        rv = view.findViewById(R.id.rv);
        vScan = view.findViewById(R.id.vScan);
        rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        rvAp = new HomeAdapter(R.layout.home_item);
        rv.setAdapter(rvAp);
        headerView = View.inflate(mContext, R.layout.header_banner, null);
        banner = headerView.findViewById(R.id.hBanner);
        upView = headerView.findViewById(R.id.upView);
        ivAd = headerView.findViewById(R.id.ivImg);
        menuRecyclerView = headerView.findViewById(R.id.menuRecyclerView);
        rvAp.setHeaderView(headerView);
        height = UiUtils.dp2px(mContext, 200 - 45);
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.titleBar(llChange).statusBarDarkFont(false).init();
    }

    @Override
    public void initListener() {
        upView.setOnItemClickListener((position, view) -> start(BaseWebViewActivity.class));
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                ScrollUnm = ScrollUnm + dy; // The sum of the sliding distances
                if (ScrollUnm <= 0) {  // Completely transparent at the top
                    llChange.setBackgroundColor(Color.argb(0, 255, 41, 76));
                } else if (ScrollUnm > 0 && ScrollUnm <= height) {  // In slider's height, set opacity percentage (current height/total height)
                    double d = (double) ScrollUnm / height;
                    double alpha = (d * 255);
                    llChange.setBackgroundColor(Color.argb((int) alpha, 255, 41, 76));
                } else { // become completely opaque when slide out
                    llChange.setBackgroundColor(Color.argb(255, 255, 41, 76));
                }
            }
        });
        rvAp.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, GoodsInfoActivity.class));
            }
        });
        vScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { //检查用户是否打开相机权限
                    // not opened
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))) {  //用户是否禁止使用相机权限
                        // jumps to the app's Settings screen and lets the user manually authorize it
                        DialogUtils.showDialogForIosStyle(mContext, "Notice", "Scan feature needs camera permission", "OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                                DialogUtils.dissmissDialog();
                            }
                        }, true);
                    } else {
                        // ask for camera permission
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, MANIFESTPERMISSIONCAMERA);
                    }
                } else {
                    Intent intent = new Intent(mContext, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
        llChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SearchActivity.class));
            }
        });
        ivAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(FrameZActivity.class);
            }
        });

    }

    private void showVideoDialog() {
        String[] data = {"Data 1", "Data 2", "Data 3"};
        new SingleSelectDialog.Builder(mContext).setItems(data, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == 0) {
                    startActivity(new Intent(mContext, FrameAActivity.class));
                } else if (i == 1) {
                    startActivity(new Intent(mContext, FrameBActivity.class));
                } else {
                    startActivity(new Intent(mContext, FrameCActivity.class));
                }
            }
        }).show();
    }

    @Override
    public void loadData() {
        Random random = new Random();
        homeGoods = new HomeGoodsBean();
        homeGoods.data = new ArrayList<>();
        for (int i = 0; i < ResData.getData1().size(); i++) {  // add item
            HomeGoodsBean.Data data = new HomeGoodsBean.Data();
            data.icon = ResData.getIconImages().get(random.nextInt(ResData.getIconImages().size()));
            data.content = ResData.getData1().get(i);
            data.title = ResData.getData1().get(i).substring(ResData.getData1().size() - 10, ResData.getData1().size() - 1);
            int jj = random.nextInt(11) + 1;
            data.images = new ArrayList<>();
            for (int j = 0; j < jj + 1; j++) {  // add item image
                HomeGoodsBean.Data.PicList pics = new HomeGoodsBean.Data.PicList();
                pics.pic = ResData.getBannerImages().get(random.nextInt(ResData.getGoodsImages().size() - 1));
                data.images.add(pics);
            }
            homeGoods.data.add(data);
        }
        rvAp.setNewData(homeGoods.data);
        GlideImageUtils.displayGif(mContext, R.mipmap.long_mao, ivAd);
        setBannerOneData();
        setUpView(ResData.getData());
        setMenu(ResData.getMenus());
    }

    private void setBannerOneData() {

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        // Set up the image loader
        banner.setImageLoader(new GlideImageLoader());
        banner.setImages(ResData.getBannerRes());
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                startActivity(new Intent(mContext, URLAActivity.class));
            }
        });
        // Sets the banner animation effect
        banner.setBannerAnimation(Transformer.Default);
        // Set auto rotation. Default is true
        banner.isAutoPlay(true);
        // Set the rotation time
        banner.setDelayTime(5000);
        // Set indicator position (when there is indicator in banner mode)
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        // Call this when the banner setup method is complete
        banner.start();
    }

    public void setUpView(List<String> data) {
        List<String> data2 = new ArrayList<>();
        List<View> views = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            data2.add(data.get(i));
        }
        for (int i = 0; i < data2.size(); i++) {
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.item_upview, null);
            TextView tv = moreView.findViewById(R.id.tv);
            tv.setText(Html.fromHtml(data2.get(i)));
            views.add(moreView);
        }
        upView.setViews(views);
    }

    public void setMenu(List<String> menu) {
        menuRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        HomeMenuAdapter hap = new HomeMenuAdapter(R.layout.item_homemenu);
        menuRecyclerView.setAdapter(hap);
        hap.setOnItemClickListener((adapter, view, position) -> showVideoDialog());
        HomeMenuBean b = new HomeMenuBean();
        b.data = new ArrayList<>();
        for (int i = 0; i < ResData.getMenus().size(); i++) {
            HomeMenuBean.Data data = new HomeMenuBean.Data();
            data.icon = ResData.icon_lol_imangs[i];
            data.title = ResData.getMenus().get(i);
            b.data.add(data);
        }
        hap.setNewData(b.data);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * Process qr code scan results
         */
        if (requestCode == REQUEST_CODE) {
            // Process scan results (display on screen)
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    DialogUtils.showDialogForIosStyle(mContext, "Result", result, "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogUtils.dissmissDialog();
                        }
                    }, false);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    DialogUtils.showDialogForIosStyle(mContext, "Notice", "Scan Failed", "OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            DialogUtils.dissmissDialog();
                        }
                    }, false);
                }
            }
        }
    }
}
