package com.example.admin.ccb.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.FrameAActivity;
import com.example.admin.ccb.activity.FrameBActivity;
import com.example.admin.ccb.activity.FrameCActivity;
import com.example.admin.ccb.activity.FrameZActivity;
import com.example.admin.ccb.activity.SearchActivity;
import com.example.admin.ccb.activity.SplashActivity;
import com.example.admin.ccb.activity.URLAActivity;
import com.example.admin.ccb.adapter.HomeMenuAdapter;
import com.example.admin.ccb.bean.HomeMenuBean;
import com.example.admin.ccb.utils.DialogUtils;
import com.example.admin.ccb.utils.GlideImageLoader;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.example.admin.ccb.utils.ResData;
import com.example.admin.ccb.view.NotConflictViewPager;
import com.example.admin.ccb.view.UPMarqueeView;
import com.gyf.barlibrary.ImmersionBar;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import www.ccb.com.common.base.BaseCacheFragment;
import www.ccb.com.common.base.BaseFragment;
import www.ccb.com.common.utils.ToastUtils;
import www.ccb.com.common.utils.UiUtils;
import www.ccb.com.common.widget.dialog.SingleSelectDialog;


public class HomeFragment extends BaseFragment {

    private final int REQUEST_CODE = 106;
    private final int MANIFESTPERMISSIONCAMERA = 17;
    private final List<String> mDataList = Arrays.asList("Main Topic", "Topic2", "Topic3", "Topic4", "Topic5", "Topic6");
    private MagicIndicator indicator;
    private NotConflictViewPager mPager;
    private AppBarLayout appBarLayout;
    private SwipeRefreshLayout swipeRefresh;
    private Banner banner;
    private UPMarqueeView upView;
    private RecyclerView menuRecyclerView;
    private View vScan;
    private ImageView ivAd;
    private RelativeLayout llChange;
    private TextView tvSearch;
    private List<BaseCacheFragment> mFragmentList;
    private int height;  // Where does the slide change color completely
    private int ScrollUnm = 0;  // sum of the sliding distances

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void initView(View v) {
        appBarLayout = v.findViewById(R.id.appBarLayout);
        indicator = v.findViewById(R.id.Indicator);
        mPager = v.findViewById(R.id.vp);
        swipeRefresh = v.findViewById(R.id.swipe);
        banner = v.findViewById(R.id.hBanner);
        upView = v.findViewById(R.id.upView);
        ivAd = v.findViewById(R.id.ivImg);
        vScan = v.findViewById(R.id.vScan);
        menuRecyclerView = v.findViewById(R.id.menuRecyclerView);
        llChange = v.findViewById(R.id.llChange);
        tvSearch = v.findViewById(R.id.tvSearch);

        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.titleBar(llChange).statusBarDarkFont(false).init();

        CommonNavigator commonNavigator = new CommonNavigator(mContext);
        commonNavigator.setAdjustMode(false); // whether it fills the screen
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            long currentTime = 0;

            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int i) {
                ClipPagerTitleView tv = new ClipPagerTitleView(context);
                tv.setText(mDataList.get(i));
                tv.setTextColor(Color.parseColor("#424242"));
                tv.setClipColor(Color.parseColor("#FF294C"));
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPager.setCurrentItem(i);

                        if (mPager.getCurrentItem() == i && System.currentTimeMillis() - currentTime < 300) {
                            mFragmentList.get(i).onRefresh();
                            currentTime = 0;
                        }
                        currentTime = System.currentTimeMillis();
                    }
                });
                return tv;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT); // underline length is the same as the font
                indicator.setColors(Color.parseColor("#FF294C"));// underline color
                return indicator;
            }
        });
        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, mPager);
    }

    @Override
    public void loadData() {
        setBannerOneData();
        setUpView(ResData.getAds());
        setMenu(ResData.getMenus());
        GlideImageUtils.displayGif(mContext, R.mipmap.long_mao, ivAd);
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new PalFragment());
        mFragmentList.add(new NewsTopFragment());
        mFragmentList.add(new PalFragment());
        mFragmentList.add(new PalFragment());
        mFragmentList.add(new PalFragment());
        mFragmentList.add(new PalFragment());
        // mFragmentList.add(new QiuBaiImgFragment());
        // mFragmentList.add(new GossipFragment());
        // mFragmentList.add(new NewsTopFragment());
        // mFragmentList.add(new GirlWelfareFragment());
        // mFragmentList.add(new QiuBaiFragment());
        mPager.setAdapter(new Myadapter(getFragmentManager()));
    }

    @Override
    public void initListener() {
        swipeRefresh.setColorSchemeColors(getResources().getColor(R.color.main_green), getResources().getColor(R.color.colorOrange), getResources().getColor(R.color.mainColor));
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        ToastUtils.showCenterToast("Page Refreshed!");
                    }
                }, 3000);
            }
        });
        upView.setOnItemClickListener((position, view) -> {
            Intent intent = new Intent(getActivity(), SplashActivity.class);
            intent.putExtra(SplashActivity.WEATHER, true);
            startActivity(intent);
        });
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                indicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                indicator.onPageScrollStateChanged(state);
            }
        });
        mPager.setCurrentItem(0);

        height = UiUtils.dp2px(mContext, 200 - 45);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                ScrollUnm = -verticalOffset; // Total sliding distance
                if (ScrollUnm <= 0) {  // Completely transparent at the top
                    llChange.setBackgroundColor(Color.argb(0, 255, 41, 76));
                } else if (ScrollUnm > 0 && ScrollUnm <= height) {  // When sliding height, set opacity percentage (current height/total height)
                    double d = (double) ScrollUnm / height;
                    double alpha = (d * 255);
                    llChange.setBackgroundColor(Color.argb((int) alpha, 255, 41, 76));
                } else { // The total height of the slide is completely opaque
                    llChange.setBackgroundColor(Color.argb(255, 255, 41, 76));
                }
                if (verticalOffset == 0) {
                    // show
                    swipeRefresh.setEnabled(true);
//                    mImmersionBar.statusBarDarkFont(false,0.2f).init();
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    // hide
//                    mImmersionBar.statusBarDarkFont(true,0.2f).init();
                } else {
                    swipeRefresh.setEnabled(false);
                }
            }
        });

        vScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) { // Check whether the user has camera permissions enabled
                    // not opened
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA))) {  // Whether the user disallows the camera permission
                        // jumps to the app's settings screen and lets the user manually authorize it
                        DialogUtils.showDialogForIosStyle(mContext, "Notice", "Please turn on the camera permission.", "OK", new View.OnClickListener() {
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
                        // ask camera permission
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

    private void setBannerOneData() {
//       Animation animation = AnimationUtils.loadAnimation(getActivity() , R.anim.banner_anim);
//       animation.setInterpolator(new OvershootInterpolator());
//        banner.setAnimation(animation);
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
        // sets the banner animation effect
        banner.setBannerAnimation(Transformer.Default);
        // set auto rotation. Default is true
        banner.isAutoPlay(true);
        // set auto rotation time
        banner.setDelayTime(3000);
        // set indicator position (when there is indicator in banner mode)
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        // after the banner setup method is completely called
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
        LayoutAnimationController controller = new LayoutAnimationController(AnimationUtils.loadAnimation(getActivity(), R.anim.menu_anim));
        menuRecyclerView.setLayoutAnimation(controller);
        menuRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 5));
        HomeMenuAdapter hap = new HomeMenuAdapter(R.layout.item_homemenu);
        menuRecyclerView.setAdapter(hap);
        hap.setOnItemClickListener((adapter, view, position) -> showVideoDialog());
        HomeMenuBean b = new HomeMenuBean();
        b.data = new ArrayList<>();
        for (int i = 0; i < menu.size(); i++) {
            HomeMenuBean.Data data = new HomeMenuBean.Data();
            data.icon = ResData.icon_lol_imangs[i];
            data.title = menu.get(i);
            b.data.add(data);
        }
        hap.setNewData(b.data);

    }

    private void showVideoDialog() {
        String[] data = {"Frame 1 Activity", "Frame 2 Activity", "Frame 3 Activity"};
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * QR code result process
         */
        if (requestCode == REQUEST_CODE) {
            // Process scan results (display on interface)
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    DialogUtils.showDialogForIosStyle(mContext, "Notice", result, "OK", new View.OnClickListener() {
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

    class Myadapter extends FragmentPagerAdapter {

        public Myadapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }

}
