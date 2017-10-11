package com.hanlyjiang.learnandroid.android.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;

import com.hanlyjiang.learnandroid.R;

import java.util.TimeZone;

/**
 * @author hanlyjiang on 2017/10/11-10:19.
 * @version 1.0
 */

public class CircleMarkTimeView extends View {

    private Time mCalendar;
    /**
     *
     */
    private int mDialColor;
    private int mShadowColor;
    private int mShadowWidth;
    private int mMarkTextColor;
    private int mTimeTextColor;

    private int mMarkTextSize;
    private int mTimeTextSize;
    private String mMarkTextContent;


    private boolean mAttached;

    private final Handler mHandler = new Handler();
    private float mSeconds;
    private float mMinutes;
    private float mHour;
    private boolean mChanged;
    private final Context mContext;
    private String mTimeZoneId;

    private Paint mTextPaint, mDialPaint, mShadowPaint;
    private float mDialWidth = 100;
    private float mDialHeight = 100;

    public CircleMarkTimeView(Context context) {
        this(context, null);
    }

    public CircleMarkTimeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMarkTimeView(Context context, AttributeSet attrs,
                              int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        Resources r = mContext.getResources();


        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleMarkTimeView);
        mDialColor = a.getColor(R.styleable.CircleMarkTimeView_dialColor, 0xff6b96ef);
        mShadowWidth = a.getDimensionPixelOffset(R.styleable.CircleMarkTimeView_shadowWidth, 4);
        mMarkTextColor = a.getColor(R.styleable.CircleMarkTimeView_markTextColor, Color.WHITE);
        mTimeTextColor = a.getColor(R.styleable.CircleMarkTimeView_timeTextColor, Color.WHITE);
        mMarkTextSize = a.getDimensionPixelOffset(R.styleable.CircleMarkTimeView_markTextSize, 28);
        mTimeTextSize = a.getDimensionPixelOffset(R.styleable.CircleMarkTimeView_timeTextSize, 28);
        mMarkTextContent = a.getString(R.styleable.CircleMarkTimeView_markText);

        if (mDialColor != 0) {
            mDialPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mDialPaint.setColor(mDialColor);
            mDialPaint.setStyle(Paint.Style.FILL);
        }

        if (mDialColor != 0) {
            mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mShadowPaint.setColor(mDialColor);
            mShadowPaint.setAlpha(64);
            mShadowPaint.setStrokeWidth(mShadowWidth);
            mShadowPaint.setStyle(Paint.Style.STROKE);
        }

        if (mMarkTextColor != 0 || mTimeTextColor != 0) {
            mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mTextPaint.setColor(mMarkTextColor);
            mTextPaint.setTextSize(mMarkTextSize);
        }
        mCalendar = new Time();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = new Time();

        // Make sure we update to the current time
        onTimeChanged();

        // tick the seconds
        post(mClockTick);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            removeCallbacks(mClockTick);
            mAttached = false;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        float scale = Math.min(hScale, vScale);

        setMeasuredDimension(resolveSizeAndState((int) (mDialWidth * scale), widthMeasureSpec, 0),
                resolveSizeAndState((int) (mDialHeight * scale), heightMeasureSpec, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mChanged = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        boolean changed = mChanged;
        if (changed) {
            mChanged = false;
        }

        int availableWidth = getWidth();
        int availableHeight = getHeight();

        int x = availableWidth / 2;
        int y = availableHeight / 2;

        int w = availableWidth - getPaddingLeft() - getPaddingRight();
        int h = availableHeight - getPaddingBottom() - getPaddingTop();

        boolean scaled = false;

        if (availableWidth < w || availableHeight < h) {
            scaled = true;
            float scale = Math.min((float) availableWidth / (float) w,
                    (float) availableHeight / (float) h);
            canvas.save();
            canvas.scale(scale, scale, x, y);
        }

        float shadowRingRadius = (w - mShadowWidth) / 2f;
        float circleRadius = w / 2f - mShadowWidth;

        // 画外部圈
        canvas.drawCircle(x, y, shadowRingRadius, mShadowPaint);
        // 画内部圈
        canvas.drawCircle(x, y, circleRadius, mDialPaint);

        mTextPaint.setTextSize(mMarkTextSize);
        mTextPaint.setAlpha(255);
        mTextPaint.setFakeBoldText(true);
        String mark = mMarkTextContent;
        float markTextWidth = mTextPaint.measureText(mark);
        canvas.drawText(mark, x - markTextWidth / 2f, y, mTextPaint);
        float markTextHeight = mTextPaint.descent() - mTextPaint.ascent();

        mTextPaint.setTextSize(mTimeTextSize);
        mTextPaint.setAlpha(222);
        mTextPaint.setFakeBoldText(false);
        String timeText = String.format("%02.0f:%02.0f:%02.0f", mHour, mMinutes, mSeconds);
        float timeTextWidth = mTextPaint.measureText(timeText);
        canvas.drawText(timeText, x - timeTextWidth / 2f, y + markTextHeight, mTextPaint);

        if (scaled) {
            canvas.restore();
        }
    }

    private void onTimeChanged() {
        mCalendar.setToNow();

        if (mTimeZoneId != null) {
            mCalendar.switchTimezone(mTimeZoneId);
        }

        int hour = mCalendar.hour;
        int minute = mCalendar.minute;
        int second = mCalendar.second;
        //      long millis = System.currentTimeMillis() % 1000;

        mSeconds = second;//(float) ((second * 1000 + millis) / 166.666);
        mMinutes = minute /*+ second / 60.0f*/;
        mHour = hour /*+ mMinutes / 60.0f*/;
        mChanged = true;

        updateContentDescription(mCalendar);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
            }
            onTimeChanged();
            invalidate();
        }
    };

    private final Runnable mClockTick = new Runnable() {

        @Override
        public void run() {
            onTimeChanged();
            invalidate();
            CircleMarkTimeView.this.postDelayed(mClockTick, 1000);
        }
    };

    private void updateContentDescription(Time time) {
        final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
        String contentDescription = DateUtils.formatDateTime(mContext,
                time.toMillis(false), flags);
        setContentDescription(contentDescription);
    }

    public void setTimeZone(String id) {
        mTimeZoneId = id;
        onTimeChanged();
    }

    public void enableSeconds(boolean enable) {
        mNoSeconds = !enable;
    }

}
