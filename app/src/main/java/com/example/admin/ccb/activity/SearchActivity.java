package com.example.admin.ccb.activity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.admin.ccb.R;
import com.example.admin.ccb.utils.ResData;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import www.ccb.com.common.base.BaseActivity;
import www.ccb.com.common.utils.ToastUtils;


public class SearchActivity extends BaseActivity {
    private TagFlowLayout tagFlowLayout;
    private EditText et;
    private TagAdapter<String> tagAdapter;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_sraech;
    }

    @Override
    protected void initView() {
        mImmersionBar.titleBarMarginTop(R.id.home_searchs).statusBarDarkFont(true, 0.2f).init();
        et = findViewById(R.id.search_content);
        findViewById(R.id.searchBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tagFlowLayout = findViewById(R.id.flowlayout);
    }

    @Override
    protected void initData() {
        // TagFlowLayout Adaptor for the flow layout
        tagAdapter = new TagAdapter<String>(ResData.searchLists) {
            @Override
            public View getView(FlowLayout parent, int position, String data) {
                TextView tv = (TextView) LayoutInflater.from(SearchActivity.this).inflate(R.layout.item_search_flow_tv, tagFlowLayout, false);
                tv.setText(data);
                return tv;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter);
    }

    @Override
    protected void initList() {
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Intent intent = new Intent(mContext, SplashActivity.class);
                intent.putExtra(SplashActivity.WEATHER, true);
                startActivity(intent);
                ToastUtils.showToast(mContext, "Searching...");
                et.setText(ResData.searchLists[position]);
                return true;
            }
        });
        findViewById(R.id.ivSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SplashActivity.class);
                intent.putExtra(SplashActivity.WEATHER, true);
                startActivity(intent);
                ToastUtils.showToast(mContext, "Searching...");
            }
        });
    }
}


