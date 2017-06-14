package com.geostar.solrtest.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import com.geostar.solrtest.R;
import com.geostar.solrtest.utils.DisplayHelper;


/**
 * 样式大小选中器
 *
 * @author hanlyjiang on 2017/6/13-21:44.
 * @version 1.0
 */

public class SizeSelectBar extends BaseCustomView {

    private static final String TAG = "SizeSelectBar";
    /**
     * 有多少个层级
     */
    private int levelTotal;
    private int minSize;
    private int stepSize;
    private int itemWidth;
    private Paint sizePotPaint;
    private int itemPadding;
    private int itemNormalColor;
    private int itemSelectedColor;

    private int mDownPointY, mDownPointX;
    private int mTouchSlop = 8;
    private boolean isClick;
    private boolean isTouchTriggered;

    /**
     * 当前选中的Item
     */
    private int mSelectedItemIndex = 0;
    private OnItemSelectedListener onItemSelectedListener;

    public interface OnItemSelectedListener {
        void onItemSelected(int size);
    }

    public SizeSelectBar(Context context) {
        super(context);
    }

    public SizeSelectBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SizeSelectBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SizeSelectBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        TypedArray typedArray = retrTypedArray(context, attrs, R.styleable.SizeSelectBar);
        levelTotal = typedArray.getInteger(R.styleable.SizeSelectBar_sizeBarLevelTotal, 5);
        minSize = typedArray.getDimensionPixelSize(R.styleable.SizeSelectBar_sizeBarMinSize, dp2px(4));
        stepSize = typedArray.getDimensionPixelOffset(R.styleable.SizeSelectBar_sizeBarStepSize, dp2px(2));
        itemWidth = typedArray.getDimensionPixelOffset(R.styleable.SizeSelectBar_sizeBarItemWidth, dp2px(40));
        itemPadding = typedArray.getDimensionPixelOffset(R.styleable.SizeSelectBar_sizeBarItemPadding, (int) (itemWidth));
        itemNormalColor = typedArray.getColor(R.styleable.SizeSelectBar_sizeBarItemNormalColor, Color.BLUE);
        itemSelectedColor = typedArray.getColor(R.styleable.SizeSelectBar_sizeBaritemSelectedColor, Color.RED);
        typedArray.recycle();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            ViewConfiguration conf = ViewConfiguration.get(getContext());
//            mMinimumVelocity = conf.getScaledMinimumFlingVelocity();
//            mMaximumVelocity = conf.getScaledMaximumFlingVelocity();
            mTouchSlop = conf.getScaledTouchSlop();
        }

        // 成员变量初始化在构造之后
        sizePotPaint = new Paint();
        sizePotPaint.setAntiAlias(true);
        sizePotPaint.setColor(itemNormalColor);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        itemWidth = calcItemWidth();

        // 计算视图最小需要的高度
        int minRequiredHeight = itemWidth + getPaddingTop() + getPaddingBottom();
        int minRequiredWidth = calculateTotalWidth() + getPaddingLeft() + getPaddingRight();

        int outWidth = 0;
        int outHeidht = 0;
        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            outWidth = minRequiredWidth;
        } else {
            outWidth = measuredWidth;
        }

        if (heightMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            outHeidht = minRequiredHeight;
        } else {
            outHeidht = measureHeight;
        }
        setMeasuredDimension(outWidth, outHeidht);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int radius = 0;
        for (int i = 0; i < levelTotal; i++) {
            if (mSelectedItemIndex == i) {
                sizePotPaint.setColor(itemSelectedColor);
            } else {
                sizePotPaint.setColor(itemNormalColor);
            }
            drawOneCircle(canvas, i);
        }
    }


    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final int action = event.getAction();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownPointX = (int) x;
                mDownPointY = (int) y;
                isTouchTriggered = true;
                isClick = true;
//                if (null != getParent())
//                    getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownPointY - event.getY()) < mTouchSlop) {
                    isClick = true;
                    break;
                }
                isClick = false;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
//                if (null != getParent())
//                    getParent().requestDisallowInterceptTouchEvent(false);
                if (isClick) {
                    triggerOnItemSelected();
                    return true;
                }
                break;
            default:
                break;
        }
        return isTouchTriggered;
    }

    private void triggerOnItemSelected() {
        Rect rect = null;
        for (int i = 0; i < minSize; i++) {
            rect = getItemRect(i);
            if (rect.contains(mDownPointX, mDownPointY)) {
                mSelectedItemIndex = i;
                invalidate();
                Log.d(TAG, "Item Selected" + mDownPointX + "," + mDownPointY + "； " + i);
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(getItemDotSize(i));
                }
                break;
            }
        }
    }

    /**
     * 到某一级的padding
     *
     * @param which
     * @return
     */
    private int getTotalPadding(int which) {
        float count = 0;
        for (int i = 0; i < which; i++) {
            count += getItemMargin(i);
        }
        return (int) count;
    }

    private void drawOneCircle(Canvas canvas, int i) {
        int radius = (minSize + i * stepSize) / 2;
        int centerX = (int) (getPaddingLeft() + (itemWidth * (i + 1)) - itemWidth / 2 + getTotalPadding(i));
        int centerY = getPaddingTop() + itemWidth / 2;
        canvas.drawCircle(centerX, centerY, radius, sizePotPaint);
    }

    /**
     * 某一点到下一个点之间的间隔大小
     *
     * @return
     */
    private float getItemMargin(int index) {
        int currentLev = (itemWidth - getItemDotSize(index)) / 2;
        int nextLev = (itemPadding - getItemDotSize(index + 1)) / 2;
        int totalLev = currentLev + nextLev;
        return itemPadding - totalLev;
    }

    /**
     * 获取单个Item 的点直径大小
     *
     * @param index
     * @return
     */
    private int getItemDotSize(int index) {
        return minSize + index * stepSize;
    }

    /**
     * 获取某个item 的点击范围
     *
     * @param i
     * @return
     */
    private Rect getItemRect(int i) {
        Rect rect = new Rect();
        rect.left = getPaddingLeft() + itemWidth * i + getTotalPadding(i);
        rect.top = getPaddingTop();
        rect.bottom = getPaddingTop() + itemWidth;
        rect.right = rect.left + itemWidth;
        return rect;
    }


    /**
     * 计算视图最小需要的高度
     *
     * @return
     */
    private int calcItemWidth() {
        int requiredSize = minSize + levelTotal * stepSize;
        return Math.max(requiredSize, itemWidth);
    }

    /**
     * 视图需要的最小宽度
     *
     * @return
     */
    private int calculateTotalWidth() {
        return itemWidth * levelTotal + getTotalPadding(levelTotal - 1);
    }


    private int dp2px(int dp) {
        return DisplayHelper.dp2px(dp, getContext());
    }
}
