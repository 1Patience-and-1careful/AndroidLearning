package imui.jiguang.cn.imuisample.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 自定义Linearlayout 需要继承的基类 <b>--> 避免手打 init 函数</b>
 *
 * @author hanlyjiang on 2017/6/2-下午5:31.
 * @version 1.0
 */

public abstract class BaseCommonLinearLayout extends LinearLayout {

    public BaseCommonLinearLayout(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public BaseCommonLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public BaseCommonLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseCommonLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    protected TypedArray retrTypedArray(Context context, AttributeSet attrs,int[] attrsArrayStyle){
        return context.obtainStyledAttributes(attrs,attrsArrayStyle);
    }

    protected abstract void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes);

}
