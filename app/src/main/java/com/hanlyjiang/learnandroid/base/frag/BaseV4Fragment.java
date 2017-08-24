package com.hanlyjiang.learnandroid.base.frag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * V4 包的基础Fragment
 *
 * @author hanlyjiang on 2017/6/15-15:01.
 * @version 1.0
 */

public abstract class BaseV4Fragment extends Fragment implements IFragment {

    private List<IFragmentCallback> callbacks;

    public BaseV4Fragment() {
        this.callbacks = new ArrayList<>();
        callbacks.add(new FragmentCallbackDebugImpl(getClass().getSimpleName()));
    }

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
        for (IFragmentCallback callback : callbacks) {
            callback.onActivityCreated(savedInstanceState);
        }
        initUI(getView(), getActivity());
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (IFragmentCallback callback : callbacks) {
            callback.onCreate(savedInstanceState);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        for (IFragmentCallback callback : callbacks) {
            callback.onViewCreated(view, savedInstanceState);
        }
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        for (IFragmentCallback callback : callbacks) {
            callback.onAttach(activity);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        for (IFragmentCallback callback : callbacks) {
            callback.onAttach(context);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        for (IFragmentCallback callback : callbacks) {
            callback.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (IFragmentCallback callback : callbacks) {
            callback.onResume();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for (IFragmentCallback callback : callbacks) {
            callback.onDestroyView();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (IFragmentCallback callback : callbacks) {
            callback.onDetach();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (IFragmentCallback callback : callbacks) {
            callback.onDestroy();
        }
    }

}
