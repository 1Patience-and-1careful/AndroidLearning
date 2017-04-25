package com.geostar.solrtest.android.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        FrameLayout fl;
        TextView textView;
        int count = getChildCount();
        View child = null;

        int cWidth,cHeight;

        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            child.getMeasuredWidth();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        View childView = null;

        int fourChildCount = 0;
        int centerX = (l + r) / 2;
        int centerY = (t + b) / 2;

        for (int i = 0; i < count; i++) {
            Log.d(TAG, "onLayout " + i + " View");
            childView = getChildAt(i);
            fourChildCount++;
            if (fourChildCount == 5) {
                fourChildCount = 1;
            }

            final int width = childView.getMeasuredWidth();
            final int height = childView.getMeasuredHeight();
            childView.getWidth();

            if (fourChildCount == 1) {// top left
                childView.layout(centerX - width, centerY - height, centerX, centerY);
            } else if (fourChildCount == 2) {
                childView.layout(centerX, t, r, centerY);
            } else if (fourChildCount == 3) {
                childView.layout(centerX, centerY, r, b);
            } else if (fourChildCount == 4) {
                childView.layout(l, centerY, centerX, b);
            }
        }
    }
}
