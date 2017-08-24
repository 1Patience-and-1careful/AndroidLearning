package com.hanlyjiang.learnandroid.android.listview;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * @author hanlyjiang on 2017/5/22-下午4:32.
 * @version 1.0
 */

public class CircleExpandableListView extends ExpandableListView {

    public CircleExpandableListView(Context context) {
        super(context);
    }

    public CircleExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CircleExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
