package com.hanlyjiang.learnandroid.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * author:wangenzhao
 * date:2017/5/3 16:46
 */

public abstract class BaseFragment extends Fragment {

    public void refresh() {

    }

    /**
     * 获取布局文件id
     *
     * @return
     */
    abstract public int getLayoutResId();

    /**
     * 在此方法中进行ui布局和数据初始化，此时activity已经created
     */
    abstract public void initUI();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getLayoutResId() == 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        return inflater.inflate(getLayoutResId(), container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
    }

    /**
     * 查找 View
     *
     * @param viewId
     * @return
     */
    public View findViewById(int viewId) {
        if (getView() == null) {
            throw new RuntimeException("请为Fragment设置View 后再调用此方法");
        }
        return getView().findViewById(viewId);
    }
}
