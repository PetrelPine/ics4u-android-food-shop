package com.example.admin.ccb.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.admin.ccb.R;

import java.util.Arrays;

import www.ccb.com.common.base.BaseActivity;
import www.ccb.com.common.utils.L;


public class OrderDetailsActivity extends BaseActivity {

    private RecyclerView rv;

    @Override
    public int getContentViewResource() {
        return R.layout.activity_order_details;
    }

    @Override
    protected void initView() {
        rv = findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv.setLayoutManager(llm);
        rv.setAdapter(new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_order_details, Arrays.asList("", "")) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setIsRecyclable(false);
                if (helper.getAdapterPosition() == 0) {
                    helper.getView(R.id.layout1).setVisibility(View.GONE);
                    helper.getView(R.id.layout2).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.layout1).setVisibility(View.VISIBLE);
                    helper.getView(R.id.layout2).setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initList() {
        rv.setOnScrollListener(new RecyclerView.OnScrollListener() {
            final int TRIGGERDISTANCE = 200;
            boolean isY2;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                // 当滚动结束时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    L.i("getItemCount--" + manager.getItemCount());
                    L.i("findFirstCompletelyVisibleItemPosition--" + manager.findFirstCompletelyVisibleItemPosition());
                    L.i("findFirstVisibleItemPosition--" + manager.findFirstVisibleItemPosition());
                    L.i("findLastCompletelyVisibleItemPosition--" + manager.findLastCompletelyVisibleItemPosition());
                    L.i("findLastVisibleItemPosition--" + manager.findLastVisibleItemPosition());
                    if (manager.getItemCount() != 2) return;
                    View v1 = manager.findViewByPosition(0);
                    if (v1 == null) {
                        // In the second layer, no action needed
                        isY2 = true;
                        return;
                    }
                    int height = v1.getMeasuredHeight(); // Gets the first layer layout height
                    int bottom = v1.getBottom(); // Gets the distance of the first layer layout from the top of the parent container
                    L.i(height + "---" + v1.getBottom());
                    if (height - bottom > TRIGGERDISTANCE && !isY2) { // In the first layer, the sliding distance exceeds the specified value, it will automatically slide to the second layer;
                        moveToPosition(recyclerView, 1);
                        isY2 = true;
                        return;
                    } else if (height - bottom < TRIGGERDISTANCE && !isY2) { // In the first layer, the sliding distance does not exceed the specified value, automatically back to the first layer;
                        moveToPosition(recyclerView, 0);
                        isY2 = false;
                        return;
                    }

                    if (bottom > TRIGGERDISTANCE && isY2) { // When it is in the second layer, automatic sliding is triggered after sliding.
                        moveToPosition(recyclerView, 0);
                        isY2 = false;
                        return;
                    } else if (bottom < TRIGGERDISTANCE && isY2) { // When it is at the second layer, automatic sliding is not triggered after sliding.
                        moveToPosition(recyclerView, 1);
                        isY2 = true;
                        return;
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // dx is used to determine the direction of lateral slide, dy is used to determine the direction of longitudinal slide
                L.i("dx--" + dx + ",dy--" + dy);
            }
        });
    }

    /**
     * RecyclerView - Slide to the specified position and stay on top
     *
     * @param rv
     * @param position
     */
    private void moveToPosition(RecyclerView rv, int position) {
        LinearSmoothScroller smoothScroller = new LinearSmoothScroller(this) {
            @Override
            protected int getVerticalSnapPreference() {
                // SNAP_TO_START: Aligns the left or top of the child view with the left or top of the parent view.
                // SNAP_TO_END: Aligns the right or bottom of the child view with the right side or bottom of the parent view.
                // SNAP_TO_ANY: Determines whether a child view follows from beginning to end based on its current position in relation to the parent layout.
                return LinearSmoothScroller.SNAP_TO_START;
            }
        };
        smoothScroller.setTargetPosition(position);
        rv.getLayoutManager().startSmoothScroll(smoothScroller);
    }
}
