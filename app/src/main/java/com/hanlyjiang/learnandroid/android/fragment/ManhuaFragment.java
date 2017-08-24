package com.hanlyjiang.learnandroid.android.fragment;

import android.app.Activity;
import android.view.View;

import com.hanlyjiang.learnandroid.R;
import com.hanlyjiang.learnandroid.base.frag.BaseV4Fragment;
import com.hanlyjiang.learnandroid.utils.LogUtils;

/**
 * @author hanlyjiang on 2017/7/8-19:05.
 * @version 1.0
 */

public class ManhuaFragment extends BaseV4Fragment {

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_manhua;
    }

    @Override
    public void initUI(View rootView, Activity activity) {
        LogUtils.d("initUI");
    }

    @Override
    public void refreshUI() {

    }
}
