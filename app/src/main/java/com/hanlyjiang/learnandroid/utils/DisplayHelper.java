package com.hanlyjiang.learnandroid.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

/**
 * 屏幕密度获取 及dxpx转换
 */
public class DisplayHelper {
    private static DisplayHelper mInstance;

    private Context mContext;
    private DisplayMetrics mMetrics;

    public static DisplayHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DisplayHelper(context);
        }
        return mInstance;
    }

    protected DisplayHelper(Context context) {
        mContext = context;
    }

    public DisplayMetrics getMetrics() {
        if (mMetrics == null) {
            WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
            mMetrics = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(mMetrics);
        }
        return mMetrics;
    }

    public float getDensity() {
        return getMetrics().density;
    }

    /**
     * dp 转换到像素的过程
     *
     * @param dp
     * @param context
     * @return
     */
    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
