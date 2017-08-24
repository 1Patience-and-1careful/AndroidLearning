package com.hanlyjiang.learnandroid.android.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import static android.support.v7.widget.RecyclerView.NO_POSITION;


/**
 * 自定义一个水平滑动的布局
 * <br/>Created by jianghanghang on 2017/4/26.
 */
public class SimpleHorizLayoutManager extends RecyclerView.LayoutManager {

    private static final String TAG = "RecyclerView";
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;

    /**
     * When LayoutManager needs to scroll to a position, it sets this variable and requests a
     * layout which will check this variable and re-layout accordingly.
     */
    int mPendingScrollPosition = NO_POSITION;

    public SimpleHorizLayoutManager(Context context) {
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /*
        获取Adapter 的item 计数： RecyclerView.LayoutManager.getItemCount()
        使用state 的 getItemCount 方法来 so you should always use this number for your position calculations and never access the adapter directly.

     */
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
//        super.onLayoutChildren(recycler, state);
        Log.d(TAG, "onLayoutChildren");
//        if (state.isPreLayout()) {
//            return;
//        }
//        if (state.getItemCount() == 0) {
//            removeAndRecycleAllViews(recycler);
//            return;
//        }


        doFill(recycler, state);
    }


    int widthMargin = 100;
    int heightMinus = 50;

    int scrollDx = 0;


    private void doFill(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        final int count = state.getItemCount();

        int childLeft ;// 每个Child 起始位置
        int childRight = 0; // 每个child 终止位置

        for (int i = 0; i < count; i++) {
            final View child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, widthMargin * 2, 0);
            // 测量子View
            int itemWidth = getDecoratedMeasuredWidth(child);
            int itemHeight = getDecoratedMeasuredHeight(child);

            if (i == 0) {
                childLeft = childRight + widthMargin;
            } else {
                childLeft = childRight;
            }
            childRight = childLeft + itemWidth;

            layoutDecorated(child,
                    childLeft - scrollDx, heightMinus, childRight - scrollDx, itemHeight);
        }
    }

    @Override
    public boolean canScrollHorizontally() {
        return true;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "scrollHorizontallyBy " + dx);
        scrollDx += dx;
        doFill(recycler, state);
//        requestLayout();
        return dx;

    }


    @Override
    public void scrollToPosition(int position) {
        mPendingScrollPosition = position;
        getChildAt(position);
        super.scrollToPosition(position);
    }

}
