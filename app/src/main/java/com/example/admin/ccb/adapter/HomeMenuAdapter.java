package com.example.admin.ccb.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.bean.HomeMenuBean;


public class HomeMenuAdapter extends BaseQuickAdapter<HomeMenuBean.Data, BaseViewHolder> {

    public HomeMenuAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeMenuBean.Data item) {
        helper.setText(R.id.tvTitle, item.title);
        ((ImageView) helper.getView(R.id.ivIcon)).setImageResource(item.icon);
        // GlideImageUtils.display(mContext,item.icon,(ImageView) helper.getView(R.id.ivIcon));

    }
}
