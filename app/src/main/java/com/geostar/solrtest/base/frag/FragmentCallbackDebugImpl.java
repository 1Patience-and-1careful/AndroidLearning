package com.geostar.solrtest.base.frag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.geostar.solrtest.utils.LogUtils;


/**
 * @author hanlyjiang on 2017/7/7-11:00.
 * @version 1.0
 */

public class FragmentCallbackDebugImpl implements IFragmentCallback {

    public static final String TAG = "FragCB";

    private boolean enable = true;
    private String className = "";

    /**
     * 构造函数
     *
     * @param className 类名，用于区别log
     */
    public FragmentCallbackDebugImpl(String className) {
        this.className = className;
    }

    private void logLifeCircle(String msg) {
        if (enable) {
            LogUtils.d(TAG, className + " --> " + msg);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        logLifeCircle("onCreate");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        logLifeCircle("onViewCreated");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        logLifeCircle("onActivityCreated");
    }

    @Override
    public void onAttach(Activity activity) {
        logLifeCircle("onAttach");
    }

    @Override
    public void onAttach(Context context) {
        logLifeCircle("onAttach");
    }

    @Override
    public void onPause() {
        logLifeCircle("onPause");
    }

    @Override
    public void onResume() {
        logLifeCircle("onResume");
    }

    @Override
    public void onDestroyView() {
        logLifeCircle("onDestroyView");
    }

    @Override
    public void onDetach() {
        logLifeCircle("onDetach");
    }

    @Override
    public void onDestroy() {
        logLifeCircle("onDestroy");
    }
}
