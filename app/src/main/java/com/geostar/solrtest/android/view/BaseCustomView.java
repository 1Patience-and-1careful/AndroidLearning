package com.geostar.solrtest.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author hanlyjiang on 2017/6/12-17:41.
 * @version 1.0
 */

public abstract class BaseCustomView extends View {

    public BaseCustomView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected TypedArray retrTypedArray(Context context, AttributeSet attrs, int[] attrsArrayStyle){
        return context.obtainStyledAttributes(attrs,attrsArrayStyle);
    }

    protected abstract void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes);
}
