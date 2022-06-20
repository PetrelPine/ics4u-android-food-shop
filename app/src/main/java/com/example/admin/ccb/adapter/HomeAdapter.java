package com.example.admin.ccb.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.ShopHomeActivity;
import com.example.admin.ccb.bean.HomeGoodsBean;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.example.admin.ccb.utils.PhotoShowDialog;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import www.ccb.com.common.base.DefaultBaseAdapter;


public class HomeAdapter extends BaseQuickAdapter<HomeGoodsBean.Data, BaseViewHolder> {

    public HomeAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeGoodsBean.Data item) {
        helper.setText(R.id.title, item.title)
                .setText(R.id.content, item.content)
                .setOnClickListener(R.id.icon, new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        mContext.startActivity(new Intent(mContext, ShopHomeActivity.class)
                                , ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, view, "TRANSITIONIMAGE").toBundle());
                    }
                })
                .setOnClickListener(R.id.title, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            mContext.startActivity(new Intent(mContext, ShopHomeActivity.class)
                                    , ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, helper.getView(R.id.icon), "TRANSITIONIMAGE").toBundle());
                        } else {
                            mContext.startActivity(new Intent(mContext, ShopHomeActivity.class));
                        }
                    }
                });
        helper.setImageResource(R.id.icon, item.icon);
        NineGridView nineGrid = helper.getView(R.id.nineGrid);
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        for (HomeGoodsBean.Data.PicList imageDetail : item.images) {
            ImageInfo info = new ImageInfo();
            info.setSrcUrl(imageDetail.src);
            imageInfo.add(info);
        }
        nineGrid.setAdapter(new NineGridViewClickAdapter(mContext, imageInfo));
    }

    class PicAdapter extends DefaultBaseAdapter<HomeGoodsBean.Data.PicList> {

        public PicAdapter(Context context, List<HomeGoodsBean.Data.PicList> dataList) {
            super(context, dataList);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = View.inflate(mContext, R.layout.item_pic, null);
            ImageView iv = v.findViewById(R.id.iv_pic);
            GlideImageUtils.display(mContext, dataList.get(position).pic, iv);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List iamges = new ArrayList();
                    for (int i = 0; i < dataList.size(); i++) {
                        iamges.add(dataList.get(i).pic);
                    }
                    new PhotoShowDialog(mContext, iamges, position).show();

                }
            });
            return v;
        }
    }
}
