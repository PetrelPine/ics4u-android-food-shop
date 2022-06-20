package com.example.admin.ccb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.activity.GoodsInfoActivity;
import com.example.admin.ccb.utils.GlideImageUtils;
import com.example.admin.ccb.utils.ResData;

import java.util.List;

import www.ccb.com.common.base.BaseFragment;


public class ShopFragment extends BaseFragment {

    public static final String SHOPFRAGMENTTYPE = "SHOPFRAGMENTTYPE";
    private RecyclerView recyclerView;

    public static ShopFragment getInstance(String type) {
        ShopFragment shopFragment = new ShopFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SHOPFRAGMENTTYPE, type);
        shopFragment.setArguments(bundle);
        return shopFragment;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_fragment1, container, false);
    }

    @Override
    public void initView(View view) {
        recyclerView = view.findViewById(R.id.rv);
        Bundle bundle = getArguments();
        if (bundle != null && TextUtils.equals(bundle.getString(SHOPFRAGMENTTYPE), "grid")) {
            recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
            recyclerView.setAdapter(new MyAdapter(R.layout.item_shop1, ResData.getGoodsImages()));
        } else {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setAdapter(new MyAdapter2(R.layout.item_shop2, ResData.getGoodsImages()));
        }

    }

    @Override
    public void loadData() {

    }

    @Override
    public void initListener() {

    }

    class MyAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

        public MyAdapter(int layoutResId, @Nullable List<Integer> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Integer item) {
            helper.setText(R.id.tv, "ITEM" + helper.getAdapterPosition() + ".");
            GlideImageUtils.DisplayRoundCorner(mContext, item, helper.getView(R.id.iv), 5);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), GoodsInfoActivity.class));
                }
            });
        }
    }

    class MyAdapter2 extends BaseQuickAdapter<Integer, BaseViewHolder> {

        public MyAdapter2(int layoutResId, @Nullable List<Integer> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Integer item) {
            helper.setText(R.id.tv, "ITEM 2" + helper.getAdapterPosition() + ".");
            helper.setText(R.id.tv2, "It's very fresh, a few are a bit soft and ripe and can be eaten straight away. It's very smooth and creamy.");
            helper.setText(R.id.tv3, "$ 10" + helper.getAdapterPosition());
            GlideImageUtils.DisplayRoundCorner(mContext, item, helper.getView(R.id.iv), 5);
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), GoodsInfoActivity.class));
                }
            });
        }
    }
}
