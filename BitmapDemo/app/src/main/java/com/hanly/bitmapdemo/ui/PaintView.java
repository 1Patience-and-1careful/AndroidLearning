package com.hanly.bitmapdemo.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.hardware.display.DisplayManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

/**
 * Created by Hanly on 2015/4/20.
 */
public class PaintView extends View {

    private static final String TAG = "PaintView";
    private Context mContext = null;
    private Canvas mCanvas = null;
    private Bitmap mBitmap = null;

    public PaintView(Context context) {
        super(context);
        initPaintView(context);
    }


    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaintView(context);
    }

    public PaintView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaintView(context);
    }


    private void initPaintView(Context context){
        mContext = context;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        mBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(this.mBitmap);

        mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

    }
    @Override
    protected void onDraw(Canvas canvas) {
        Log.e(TAG, "onDraw");
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,new Paint(Paint.DITHER_FLAG));
    }


    public void setPaintBackGround(Bitmap bitmap) {
        mCanvas.drawBitmap(bitmap,0,0,new Paint(Paint.ANTI_ALIAS_FLAG));
    }
}
