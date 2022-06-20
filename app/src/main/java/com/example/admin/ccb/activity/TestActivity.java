package com.example.admin.ccb.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;

import com.example.admin.ccb.R;


public class TestActivity extends AppCompatActivity {

    private AppCompatButton btn1;
    private AppCompatImageView iv1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        initView();
        initList();
    }

    protected void initView() {
        btn1 = findViewById(R.id.btn1);
        iv1 = findViewById(R.id.iv1);
    }

    // settings -> test button
    protected void initList() {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setVisibility(View.VISIBLE);
                iv1.setTranslationY(iv1.getMeasuredHeight());
                iv1.animate().setDuration(500L).translationY(0).start();
            }
        });
    }
}
