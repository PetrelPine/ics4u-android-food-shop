package com.example.admin.ccb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;


/**
 * solve gridview only shows one line
 */
public class AllGridView extends GridView {
    public AllGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AllGridView(Context context) {
        super(context);
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }
}
