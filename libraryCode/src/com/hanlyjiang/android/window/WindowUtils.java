package com.hanlyjiang.android.window;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WindowUtils {

	public WindowUtils() {
		// TODO Auto-generated constructor stub
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

}
