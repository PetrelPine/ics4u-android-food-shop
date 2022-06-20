package com.example.admin.ccb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.GoodsInfoActivity;
import com.example.admin.ccb.adapter.HomeAdapter;
import com.example.admin.ccb.bean.HomeGoodsBean;
import com.example.admin.ccb.utils.ResData;

import java.util.ArrayList;
import java.util.Random;

import www.ccb.com.common.base.BaseCacheFragment;


public class PalFragment extends BaseCacheFragment {

    private HomeAdapter homeAdapter;
    private RecyclerView rv;

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_recyclerview, container, false);
    }

    @Override
    public void initView(View view) {
        rv = view.findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        homeAdapter = new HomeAdapter(R.layout.home_item);
        rv.setAdapter(homeAdapter);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        rv.scrollToPosition(0);
    }

    @Override
    public void loadData() {
        Random random = new Random();
        HomeGoodsBean homeGoods = new HomeGoodsBean();
        homeGoods.data = new ArrayList<>();
        for (int i = 0; i < ResData.getData1().size(); i++) {  // add item
            HomeGoodsBean.Data data = new HomeGoodsBean.Data();
            data.icon = ResData.getIconImages().get(random.nextInt(ResData.getIconImages().size()));
            data.content = ResData.getData1().get(i);
            data.title = ResData.getData().get(random.nextInt(ResData.getData().size()));
            int picCount;
            switch (i) {
                case 1:
                    picCount = 1;
                    break;
                case 2:
                    picCount = 2;
                    break;
                case 3:
                    picCount = 4;
                    break;
                default:
                    picCount = random.nextInt(ResData.getGoodsImages().size());
                    break;
            }
            if (picCount == 0) picCount = 1;
            data.images = new ArrayList<>();
            for (int j = 0; j < picCount; j++) {  // add item image
                HomeGoodsBean.Data.PicList pics = new HomeGoodsBean.Data.PicList();
                pics.src = ResData.getGoodsImages().get(random.nextInt(ResData.getGoodsImages().size() - 1));
                data.images.add(pics);
            }
            homeGoods.data.add(data);
        }
        homeAdapter.setNewData(homeGoods.data);
    }

    @Override
    public void initListener() {
        homeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                startActivity(new Intent(mContext, GoodsInfoActivity.class));
            }
        });
    }
}
