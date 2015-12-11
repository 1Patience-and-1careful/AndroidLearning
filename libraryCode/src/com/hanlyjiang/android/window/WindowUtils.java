package com.hanlyjiang.android.window;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.hanlyjiang.android.LogUtils;

public class WindowUtils {

	public WindowUtils() {
	}
	
	/** 
	 * 输出设备信息
	 */
    public static  void testDeviceDPI(Activity mActivity){
		DisplayMetrics outMetrics = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);;
		LogUtils.logDebug(mActivity, "DPI Str:" + getDPIString(outMetrics.densityDpi) 
				+ "; dpi:" + getDensityStr(outMetrics.density));
	}
	
	 /**
     * 调整窗口的透明度
     * @param from>=0&&from<=1.0f
     * @param to>=0&&to<=1.0f
     * 
     */
    @SuppressWarnings("unused")
	private void dimBackground(final Window window ,final float from, final float to) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(500);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                WindowManager.LayoutParams params = window.getAttributes();
                params.alpha = (Float) animation.getAnimatedValue();
                window.setAttributes(params);
            }
        });
        valueAnimator.start();
    }
	
	/** 
	 * 设置添加屏幕的背景透明度 0是全透明
	 * @param bgAlpha
	 */
    @SuppressWarnings("unused")
	public void setBackgroundAlpha( Window window, float bgAlpha)
	{
		WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        window.setAttributes(lp);
	}
    
    public static String getDPIString(int de){
		String str = null;
		switch (de) {
		case DisplayMetrics.DENSITY_400:
			str = "DENSITY_400";
			break;
		case DisplayMetrics.DENSITY_DEFAULT:
			str = "DENSITY_DEFAULT == DENSITY_MEDIUM";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			str = "DENSITY_HIGH";
			break;
		case DisplayMetrics.DENSITY_LOW:
			str = "DENSITY_LOW";
			break;
		case DisplayMetrics.DENSITY_TV:
			str = "DENSITY_TV";
			break;
		case DisplayMetrics.DENSITY_XHIGH:
			str = "DENSITY_XHIGH";
			break;
		case DisplayMetrics.DENSITY_XXHIGH:
			str = "DENSITY_XXHIGH";
			break;
		case DisplayMetrics.DENSITY_XXXHIGH:
			str = "DENSITY_XXXHIGH";
			break;
		default:
			break;
		}
		return str;
	}
    
	public static String getDensityStr(float dd){
		return String.valueOf(dd*160);
	}
    
}
