package com.geostar.solrtest.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

/**
 * @author hanlyjiang on 2017/6/12-17:40.
 * @version 1.0
 */

public class SelectedCircleView extends BaseCustomView {

    Paint paint;
    Paint centerHolePaint;

    int color = Color.BLUE;
    int holeColor = Color.BLACK;

    public SelectedCircleView(Context context) {
        super(context);
    }

    public SelectedCircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectedCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SelectedCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(color);
        centerHolePaint.setColor(holeColor);
        if( isSelected() ){
            drawSelected(canvas);
        }else{
            drawUnSelected(canvas);
        }
    }

    private void drawUnSelected(Canvas canvas) {
        int width = getWidth();
        int heiht = getHeight();
        canvas.drawCircle(width/2,heiht/2,width/3,paint);
    }

    private void drawSelected(Canvas canvas) {
        int width = getWidth();
        int heiht = getHeight();
        canvas.drawCircle(width/2,heiht/2,width/2,paint);
        // 绘制中心园
        canvas.drawCircle(width/2,heiht/2,width/4,centerHolePaint);
    }

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
