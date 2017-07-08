package com.geostar.solrtest.android.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geostar.solrtest.R;
import com.geostar.solrtest.base.frag.BaseV4Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hanlyjiang on 2017/7/8-17:18.
 * @version 1.0
 */

public class TestFragment extends BaseV4Fragment {

    @BindView(R.id.toolbar)
    protected Toolbar toolbar;
    private Unbinder unbinder;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void initUI(View rootView, Activity activity) {
        if (getArguments() != null) {
            String title = getArguments().getString("title");
            if (!TextUtils.isEmpty(title)) {
                toolbar.setTitle(title);
            }
        }
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
