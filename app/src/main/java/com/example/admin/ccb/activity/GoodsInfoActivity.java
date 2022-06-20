package com.example.admin.ccb.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.ccb.R;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.example.admin.ccb.utils.ResData;
import com.example.admin.ccb.view.AllListView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import www.ccb.com.common.base.BaseActivity;
import www.ccb.com.common.base.DefaultBaseAdapter;
import www.ccb.com.common.utils.ToastUtils;
import www.ccb.com.common.utils.ViewSaveImageUtils;


public class GoodsInfoActivity extends BaseActivity {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_goods_info;
    }

    @Override
    protected void initView() {
        UpTitle("Detail");
        MZBannerView banner = findViewById(R.id.banner);
        AllListView allListView = findViewById(R.id.alv);
        banner.setIndicatorVisible(false);

        banner.setPages(ResData.getGoodsimages2(), new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });
        banner.start();
        allListView.setAdapter(new DefaultBaseAdapter<Integer>(this, ResData.getDetailsImages()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                Vh vh = null;
                if (convertView == null) {
                    vh = new Vh();
                    convertView = View.inflate(mContext, R.layout.item_details, null);
                    vh.imageView = convertView.findViewById(R.id.iv);
                    convertView.setTag(vh);
                } else {
                    vh = (Vh) convertView.getTag();
                }
                vh.imageView.setImageResource(dataList.get(position));
                final Vh finalVh = vh;
                vh.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ViewSaveImageUtils.viewSaveToImage(finalVh.imageView, new ViewSaveImageUtils.OnSaveListEner() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSucceed(String filePath) {
                                ToastUtils.showCenterToast("Image Saved " + filePath);
                            }

                            @Override
                            public void onFailure(String error) {
                                ToastUtils.showCenterToast("Save Image Failed " + error);
                            }

                            @Override
                            public void onFinish() {

                            }
                        });
                        return false;
                    }
                });
                return convertView;
            }

            class Vh {
                public ImageView imageView;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initList() {
        findViewById(R.id.tvInfo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(GoodsInfoActivity.this, ShopHomeActivity.class));
            }
        });

        // AliPay
        findViewById(R.id.tvAli).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(mContext, "AliPay!");
            }
        });

        // WeChat Pay
        findViewById(R.id.tvWx).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(mContext, "WeChat Pay!");
            }
        });
    }

    public class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;

        @Override
        public View createView(Context context) {
            // return to page layout
            View view = LayoutInflater.from(context).inflate(R.layout.banner_details, null);
            mImageView = view.findViewById(R.id.iv);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // bond data
            GlideImageUtils.display(context, data, mImageView);
        }
    }
}
