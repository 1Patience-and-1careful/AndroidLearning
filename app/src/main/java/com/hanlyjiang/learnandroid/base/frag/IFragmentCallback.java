package com.hanlyjiang.learnandroid.base.frag;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Fragment 生命周期方法
 *
 * @author hanlyjiang on 2017/7/7-10:54.
 * @version 1.0
 */

public interface IFragmentCallback {

    public void onCreate(@Nullable Bundle savedInstanceState);

//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState);

    public void onActivityCreated(@Nullable Bundle savedInstanceState);

    public void onAttach(Activity activity);

    public void onAttach(Context context);

    public void onPause();

    public void onResume();

    public void onDestroyView();

    public void onDetach();

    public void onDestroy();
}
