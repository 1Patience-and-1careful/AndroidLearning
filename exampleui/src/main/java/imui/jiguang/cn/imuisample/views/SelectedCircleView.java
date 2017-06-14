package imui.jiguang.cn.imuisample.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author hanlyjiang on 2017/6/12-17:40.
 * @version 1.0
 */

public class SelectedCircleView extends BaseCustomView {

    Paint paint;

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if( isSelected() ){
            drawSelected(canvas);
        }else{
            drawUnSelected(canvas);
        }
    }

    private void drawUnSelected(Canvas canvas) {
        int width = getWidth();
        int heiht = getHeight();
        canvas.drawCircle(width/2,heiht/2,width/2,paint);
    }

    private void drawSelected(Canvas canvas) {
        int width = getWidth();
        int heiht = getHeight();
        canvas.drawCircle(width/2,heiht/2,width/2,paint);
    }
}
