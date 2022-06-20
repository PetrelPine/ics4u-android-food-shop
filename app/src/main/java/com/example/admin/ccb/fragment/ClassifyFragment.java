package com.example.admin.ccb.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;
import com.example.admin.ccb.utils.ResData;

import www.ccb.com.common.base.BaseFragment;


public class ClassifyFragment extends BaseFragment {

    private static Listeners onClickListeners;
    private LeftAdapter leftAdapter;
    private ClassifyRightFragment cf;
    private RecyclerView rv;
    private FrameLayout fm;

    public static void setonListeners(Listeners ls) {
        onClickListeners = ls;
    }

    @Override
    protected View initContentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order, container, false);
    }

    @Override
    public void initView(View view) {
        rv = view.findViewById(R.id.rvLeft);
        fm = view.findViewById(R.id.fmRight);
        rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        leftAdapter = new LeftAdapter(R.layout.item_classify_left);
        rv.setAdapter(leftAdapter);
        isTitleBar(true, view);
    }

    @Override
    public void loadData() {
        leftAdapter.setNewData(ResData.getClassifys());

        // create Fragment object
        cf = new ClassifyRightFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction().replace(R.id.fmRight, cf);
        // Pass values to the Fragment from the bundle
        Bundle bundle = new Bundle();
        bundle.putString("Name", ResData.getClassifys().get(0));
        cf.setArguments(bundle);
        fragmentTransaction.commit();
    }

    @Override
    public void initListener() {

    }

    interface Listeners {
        void onClick();
    }

    class LeftAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        private int mPosition;

        public LeftAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(final BaseViewHolder helper, String item) {
            TextView tv = helper.getView(R.id.tv_commclass);
            helper.setText(R.id.tv_commclass, item);
            if (mPosition == helper.getAdapterPosition()) {
                tv.setTextColor(getResources().getColor(R.color.mainColor));
                tv.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            } else {
                tv.setTextColor(getResources().getColor(R.color.defaultTextview));
                tv.setBackgroundColor(getResources().getColor(R.color.defaultBackground));
            }
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mPosition != helper.getAdapterPosition()) {
                        mPosition = helper.getAdapterPosition();
                        notifyDataSetChanged();
                        Bundle bundle = new Bundle();
                        bundle.putString("Name", ResData.getClassifys().get(mPosition));
                        cf.setArguments(bundle);
                        onClickListeners.onClick();
                    }
                }
            });
        }
    }
}
