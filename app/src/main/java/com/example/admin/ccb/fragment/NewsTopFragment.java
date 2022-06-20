package com.example.admin.ccb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.BaseWebViewActivity;
import com.example.admin.ccb.bean.NewsTopBean;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.example.admin.ccb.utils.PhotoDgUtils;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.List;

import www.ccb.com.common.base.BaseCacheFragment;
import www.ccb.com.common.utils.GsonUtils;
import www.ccb.com.common.utils.UrlFactory;


public class NewsTopFragment extends BaseCacheFragment {

    private NewsTopAdapter mAdapter;
    private RecyclerView recyclerView;
    private int start = 0;
    private final int count = 20;

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mAdapter = new NewsTopAdapter(new ArrayList<>());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void loadData() {
        loadHttpData();
    }

    public void loadHttpData() {
        okGetRequest("NewsTop", UrlFactory.NewsTopDataUrl + start + "-" + count + ".html");
    }

    @Override
    public void initListener() {
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                okGetRequest("NewsTop", UrlFactory.NewsTopDataUrl + start + "-" + count + ".html");
            }
        }, recyclerView);
    }

    @Override
    protected void okResponseSuccess(String whit, Object t) {
        super.okResponseSuccess(whit, t);
        if (TextUtils.equals(whit, "NewsTop")) {
            start = start + count;
            String json = String.valueOf(t);
            NewsTopBean data = null;
            try {
                data = GsonUtils.fromJson(json, NewsTopBean.class);
            } catch (JsonSyntaxException e) {
                e.fillInStackTrace();
            }
            if (data == null) return;
            mAdapter.addData(data.getDataTop());
            if (data.getDataTop().size() == 0) mAdapter.loadMoreEnd();
        }
    }

    @Override
    protected void okResponseFinish(String flag) {
        super.okResponseFinish(flag);
        mAdapter.loadMoreComplete();
    }

    class NewsTopAdapter extends BaseMultiItemQuickAdapter<NewsTopBean.DataBean, BaseViewHolder> {
        public NewsTopAdapter(List<NewsTopBean.DataBean> data) {
            super(data);
            addItemType(0, R.layout.item_newstop);
            addItemType(1, R.layout.item_newstop_image);

        }

        @Override
        protected void convert(BaseViewHolder helper, NewsTopBean.DataBean item) {
            switch (helper.getItemViewType()) {
                case 0:
                    helper.setText(R.id.tv, item.getTitle())
                            .setText(R.id.tv2, item.getDigest())
                            .setText(R.id.tv3, item.getSource() + " - " + item.getMtime());
                    GlideImageUtils.display(mContext, item.getImgsrc(), helper.getView(R.id.iv));
                    helper.setOnClickListener(R.id.llItem, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (TextUtils.isEmpty(item.getUrl())) {
                                PhotoDgUtils.show(mContext, item.getImgsrc());
                                return;
                            }
                            Intent intent = new Intent(mContext, BaseWebViewActivity.class);
                            intent.putExtra("url", item.getUrl());
                            startActivity(intent);
                        }
                    });
                    break;
                case 1:
                    GlideImageUtils.display(mContext, item.getImgsrc(), helper.getView(R.id.iv));
                    helper.setText(R.id.tv, item.getTitle())
                            .setOnClickListener(R.id.llItem, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (TextUtils.isEmpty(item.getUrl())) {
                                        PhotoDgUtils.show(mContext, item.getImgsrc());
                                        return;
                                    }
                                    Intent intent = new Intent(mContext, BaseWebViewActivity.class);
                                    intent.putExtra("url", item.getUrl());
                                    startActivity(intent);
                                }
                            });
                    break;
            }
        }
    }
}
