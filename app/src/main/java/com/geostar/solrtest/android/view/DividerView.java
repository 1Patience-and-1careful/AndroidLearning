package com.geostar.solrtest.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.geostar.solrtest.R;

/**
 * Created by hanlyjiang on 2017/5/19.
 */

public class DividerView extends View {

    private int color = 0xffe2ce28;
    private int orientation = LinearLayout.HORIZONTAL;

    private int size = 8;

    private boolean roundStartEnd = false;

    /**
     * 直接调用的初始化函数
     *
     * @param context
     * @param resId
     * @param orientation
     * @param dimenSize
     */
    public DividerView(Context context, int resId, int orientation, int dimenSize) {
        super(context);
        init(context, null, 0, 0);
        color = context.getResources().getColor(resId);
        orientation = orientation;
        size = getResources().getDimensionPixelSize(dimenSize);
    }

    public DividerView(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public DividerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DividerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);

    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        initViewPaint();

        if (attrs == null) {
            return;
        }
        TypedArray a = getResources().obtainAttributes(attrs, R.styleable.DividerView);
        if (a == null) {
            return;
        }
        color = a.getColor(R.styleable.DividerView_divide_color, color);
        size = a.getDimensionPixelSize(R.styleable.DividerView_size, 8);
        orientation = a.getInt(R.styleable.DividerView_orientation, LinearLayout.HORIZONTAL);
        roundStartEnd = a.getBoolean(R.styleable.DividerView_roundStart, false);
        a.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (orientation == LinearLayout.HORIZONTAL) { // if orientation is Horizontal
            if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
                // Parent provide a exactly value for me then use parent value
                width = measureWidth;
            } else if (widthMode == MeasureSpec.UNSPECIFIED) {
                width = size;
            }

            if (heightMode == MeasureSpec.EXACTLY) {
                height = measureHeight;
            } else {//|| heightMode == MeasureSpec.AT_MOST wrap_content
                height = size + getPaddingTop() + getPaddingBottom();
            }

            setMeasuredDimension(width, height);
            return;
        }
        if (orientation == LinearLayout.VERTICAL) {
//            height = measureHeight;
            if (heightMode == MeasureSpec.EXACTLY) { // Match_parent 或者指定大小
                // Parent provide a exactly value for me
                height = measureHeight;
            } else if (heightMode == MeasureSpec.UNSPECIFIED) {
                height = measureHeight + getPaddingTop() + getPaddingBottom();
            } else if (heightMode == MeasureSpec.AT_MOST) { // wrap_content
                height = measureHeight /*+ getPaddingTop() + getPaddingBottom()*/;
            }

            if (widthMode == MeasureSpec.EXACTLY) {
                width = measureHeight;
            } else {
                width = size + getPaddingLeft() + getPaddingRight();
            }

            setMeasuredDimension(width, height);
            return;
        }

    }

    private void initViewPaint() {
        viewPaint = new Paint();
        viewPaint.setColor(color);
        viewPaint.setAntiAlias(true);
    }

    Paint viewPaint;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int lineWidth = size;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

//        if (orientation == LinearLayout.HORIZONTAL) {
//            lineWidth = height;
//        } else if (orientation == LinearLayout.VERTICAL) {
//            lineWidth = width;
//        }
        viewPaint.setStrokeWidth(lineWidth);
        viewPaint.setColor(color);

        int delta = 0;
        if (roundStartEnd) {
            delta = lineWidth / 2;
            viewPaint.setStrokeCap(Paint.Cap.ROUND);
        } else {
            delta = 0;
            viewPaint.setStrokeCap(Paint.Cap.BUTT);
        }

        int padStart = 0;
        int padEnd = 0;
//        canvas.restore();
        if (orientation == LinearLayout.HORIZONTAL) {
            padStart = getPaddingLeft();
            padEnd = getPaddingRight();
            // center
            canvas.drawLine(delta + padStart, height / 2, width - padEnd - delta, height / 2, viewPaint);
            // center_horizontal | left
//            canvas.drawLine(delta + padStart, height / 2 + getPaddingTop(), width - padEnd - delta, height / 2 + getPaddingTop(), viewPaint);
        } else if (orientation == LinearLayout.VERTICAL) {
            padStart = getPaddingTop();
            padEnd = getPaddingBottom();
            canvas.drawLine(width / 2, delta + padStart, width / 2, height - padEnd - delta, viewPaint);

//            canvas.drawLine(width / 2 + getPaddingLeft(), delta + padStart, width / 2 + getPaddingLeft(), height - padEnd - delta , viewPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        invalidate();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        invalidate();
    }

    public boolean isRoundStartEnd() {
        return roundStartEnd;
    }

    public void setRoundStartEnd(boolean roundStartEnd) {
        this.roundStartEnd = roundStartEnd;
        invalidate();
    }
}
