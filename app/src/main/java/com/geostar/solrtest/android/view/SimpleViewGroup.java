package com.geostar.solrtest.android.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 将所有子View 环形方式居中
 * <br/> Created by jianghanghang on 2017/4/25.
 */

public class SimpleViewGroup extends ViewGroup {

    public static final String TAG = "SimpleViewGroup";

    public SimpleViewGroup(Context context) {
        super(context);
    }

    public SimpleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return super.generateLayoutParams(attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获得它的父容器为它设置的测量模式和大小
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec); // 都是excat
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int count = getChildCount();

        int[][] record = new int[4][2];

        int childWidthMeasureSpec = 0;
        int childHeightMeasureSpec = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            // 合成child 宽度测量参数
            if( child.getLayoutParams().width == LayoutParams.MATCH_PARENT ){
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth/2,MeasureSpec.EXACTLY);
            }else if(child.getLayoutParams().width == LayoutParams.WRAP_CONTENT){
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(sizeWidth/2,MeasureSpec.AT_MOST);
            }else{
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(child.getLayoutParams().width,MeasureSpec.EXACTLY);
            }

            // 合成child 高度测量参数
            if( child.getLayoutParams().height == LayoutParams.MATCH_PARENT ){
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight/2,MeasureSpec.EXACTLY);
            }else if(child.getLayoutParams().height == LayoutParams.WRAP_CONTENT){
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(sizeHeight/2,MeasureSpec.AT_MOST);
            }else{
                childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getLayoutParams().height,MeasureSpec.EXACTLY);
            }
            child.measure(childWidthMeasureSpec,childHeightMeasureSpec);

            int childHeight = child.getMeasuredHeight();
            int childWidth = child.getMeasuredWidth();

            record[i % 4][0] = Math.max(record[i % 4][0], childWidth);// 记录每个维度的最大值
            record[i % 4][1] = Math.max(record[i % 4][1], childHeight);// 记录每个维度的最大值
        }

        int maxWidth = Math.max(record[0][0] + record[1][0], record[2][0] + record[3][0]);
        int maxHeight = Math.max(record[0][1] + record[2][1], record[1][1] + record[3][1]);

        setMeasuredDimension(modeWidth == MeasureSpec.EXACTLY ? sizeWidth : maxWidth,
                modeHeight == MeasureSpec.EXACTLY ? sizeHeight : maxHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        View childView = null;

        int fourChildCount = 0;
        int centerX = (r-l) / 2;
        int centerY = (b-t) / 2;

        for (int i = 0; i < count; i++) {
            Log.d(TAG, "onLayout " + i + " View");
            childView = getChildAt(i);
            fourChildCount++;
            if (fourChildCount == 5) {
                fourChildCount = 1;
            }

            final int width = childView.getMeasuredWidth();
            final int height = childView.getMeasuredHeight();

            int cW = Math.min(centerX, width);
            int cH = Math.min(centerY, height);
            if (fourChildCount == 1) {// top left
                childView.layout(0, 0, cW, cH);
            } else if (fourChildCount == 2) {
                childView.layout(centerX, 0, centerX+cW, cH);
            } else if (fourChildCount == 3) {
                childView.layout(0, centerY, cW, centerY+cH);
            } else if (fourChildCount == 4) {
                childView.layout(centerX, centerY,centerX+cW, centerY+cH);
            }
        }
    }
}
