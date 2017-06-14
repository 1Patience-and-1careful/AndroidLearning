package com.geostar.solrtest.android.view.colorselected;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.geostar.solrtest.android.view.BaseCustomView;

/**
 * 一个简单的颜色选择控件
 *
 * @author hanlyjiang on 2017/6/12-17:40.
 * @version 1.0
 */

public class ColorCircleSelectorView extends BaseCustomView {

    private Paint paint;
    private Paint centerHolePaint;

    /**
     * 代表当前选中的颜色
     */
    private int color = Color.BLUE;
    /**
     * 选中时中间的填充色
     */
    private int holeColor = Color.BLACK;
    /**
     * 去除Padding 之后的实际宽度
     */
    private int paddingWidth;
    /**
     * 去除上下padding 之后的高度值
     */
    private int paddingHeight;
    private float animatorRato = 1;

    private LinearInterpolator accelerateDecelerateInterpolator;

    public ColorCircleSelectorView(Context context) {
        super(context);
    }

    public ColorCircleSelectorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorCircleSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ColorCircleSelectorView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        centerHolePaint = new Paint();
        centerHolePaint.setColor(Color.BLACK);
        centerHolePaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paddingWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        paddingHeight = getHeight() - getPaddingTop() - getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        centerHolePaint.setColor(holeColor);
        if (isSelected()) {
            drawSelected(canvas);
        } else {
            drawUnSelected(canvas);
        }
    }


    private void drawUnSelected(Canvas canvas) {
        canvas.drawCircle(
                paddingWidth / 2 + getPaddingLeft()
                , paddingHeight / 2 + getPaddingTop()
                , paddingWidth / 3 * animatorRato
                , paint);
    }

    private void drawSelected(Canvas canvas) {
        canvas.drawCircle(paddingWidth / 2 + getPaddingLeft()
                , paddingHeight / 2 + getPaddingTop()
                , paddingWidth / 2
                , paint);
        // 绘制中心园
        canvas.drawCircle(paddingWidth / 2 + getPaddingLeft()
                , paddingHeight / 2 + getPaddingTop()
                , paddingWidth / 4, centerHolePaint);
    }

    /**
     * 获取控件的颜色
     *
     * @return
     */
    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getHoleColor() {
        return holeColor;
    }

    public void setHoleColor(int holeColor) {
        this.holeColor = holeColor;
    }
}
