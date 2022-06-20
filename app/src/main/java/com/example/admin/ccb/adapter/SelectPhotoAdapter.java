package com.example.admin.ccb.adapter;

import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.luck.picture.lib.entity.LocalMedia;


public class SelectPhotoAdapter extends BaseQuickAdapter<LocalMedia, BaseViewHolder> {
    public SelectPhotoAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, LocalMedia item) {
        GlideImageUtils.display(mContext, item.getCompressPath(), helper.getView(R.id.iv_pic));
    }
}
