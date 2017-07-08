package com.geostar.solrtest.base.frag;

import android.app.Activity;
import android.view.View;

/**
 * 通用的片段接口
 *
 * @author hanlyjiang on 2017/6/15-14:56.
 * @version 1.0
 */

public interface IFragment {

    /**
     * 返回布局文件的 layout id
     *
     * @return 布局id
     */
    int getLayoutResId();

    /**
     * 在此函数中初始化视图控件，此方法在 onActivity中进行
     *
     * @param rootView Frag 的根View
     * @param activity activity
     */
    void initUI(View rootView, Activity activity);

    /**
     * 在此处添加视图刷新代码
     */
    void refreshUI();

    /**
     * 查找指定 ID
     *
     * @param id 视图ID
     */
    View findViewById(int id);

}
