package com.geostar.solrtest.android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListAdapter;

/**
 * Created by jianghanghang on 2017/4/26.
 */

public class SimpleAbsListView extends AbsListView {

    public SimpleAbsListView(Context context) {
        super(context);
    }

    public SimpleAbsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleAbsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SimpleAbsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public ListAdapter getAdapter() {
        return null;
    }

    @Override
    public void setSelection(int position) {

    }
}
