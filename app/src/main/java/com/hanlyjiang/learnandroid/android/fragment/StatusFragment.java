package com.hanlyjiang.learnandroid.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioGroup;

import com.hanlyjiang.learnandroid.R;
import com.hanlyjiang.learnandroid.base.CachedObjFactory;
import com.hanlyjiang.learnandroid.base.frag.BaseV4Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author hanlyjiang on 2017/7/8-18:03.
 * @version 1.0
 */

public class StatusFragment extends BaseV4Fragment implements RadioGroup.OnCheckedChangeListener {

    private CachedObjFactory<Fragment> fragFactory;

    @BindView(R.id.rgp_status_switcher)
    RadioGroup rgpSwitcher;
    private Unbinder unbinder;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_status;
    }

    @Override
    public void initUI(View rootView, Activity activity) {
        unbinder = ButterKnife.bind(this, rootView);
        initFragmentFactory();
        FragmentManager.FragmentLifecycleCallbacks lifecycleCallbacks = getFragmentDebugLifecycleCallbacks();
        getChildFragmentManager().registerFragmentLifecycleCallbacks(lifecycleCallbacks,true);
        FragmentManager.enableDebugLogging(true);
        rgpSwitcher.setOnCheckedChangeListener(this);
    }

    @NonNull
    private FragmentManager.FragmentLifecycleCallbacks getFragmentDebugLifecycleCallbacks() {
        return (FragmentManager.FragmentLifecycleCallbacks) getChildFragmentManager().new FragmentLifecycleCallbacks(){
                @Override
                public void onFragmentPreAttached(FragmentManager fm, Fragment f, Context context) {
                    super.onFragmentPreAttached(fm, f, context);
                }

                @Override
                public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
                    super.onFragmentAttached(fm, f, context);
                }

                @Override
                public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    super.onFragmentCreated(fm, f, savedInstanceState);
                }

                @Override
                public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
                    super.onFragmentActivityCreated(fm, f, savedInstanceState);
                }

                @Override
                public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
                    super.onFragmentViewCreated(fm, f, v, savedInstanceState);
                }

                @Override
                public void onFragmentStarted(FragmentManager fm, Fragment f) {
                    super.onFragmentStarted(fm, f);
                }

                @Override
                public void onFragmentResumed(FragmentManager fm, Fragment f) {
                    super.onFragmentResumed(fm, f);
                }

                @Override
                public void onFragmentPaused(FragmentManager fm, Fragment f) {
                    super.onFragmentPaused(fm, f);
                }

                @Override
                public void onFragmentStopped(FragmentManager fm, Fragment f) {
                    super.onFragmentStopped(fm, f);
                }

                @Override
                public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
                    super.onFragmentSaveInstanceState(fm, f, outState);
                }

                @Override
                public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentViewDestroyed(fm, f);
                }

                @Override
                public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
                    super.onFragmentDestroyed(fm, f);
                }

                @Override
                public void onFragmentDetached(FragmentManager fm, Fragment f) {
                    super.onFragmentDetached(fm, f);
                }
            };
    }

    private void initFragmentFactory() {
        if (fragFactory == null) {
            fragFactory = new CachedObjFactory<>();
            fragFactory.addGenerator("manhua", new CachedObjFactory.ObjGenerator<Fragment>() {
                @Override
                public Fragment generateObj() {
                    return new ManhuaFragment();
                }
            });
            fragFactory.addGenerator("live_video", new CachedObjFactory.ObjGenerator<Fragment>() {
                @Override
                public Fragment generateObj() {
                    return new LiveVideoFragment();
                }
            });
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

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_manhua) {
            switchPage("manhua");
        } else if (checkedId == R.id.rb_zhibo) {
            switchPage("live_video");
        }
    }

    private void switchPage(String fragKey) {
        Fragment fragment = fragFactory.get(fragKey,false);
//        if (!fragment.isAdded()) {
//            Bundle args = new Bundle();
//            args.putString("title", fragKey);
//            fragment.setArguments(args);
//        }
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (getChildFragmentManager().findFragmentById(R.id.fl_fragment_stub_status) != null) {
            transaction.replace(R.id.fl_fragment_stub_status, fragment);
        } else {
            transaction.add(R.id.fl_fragment_stub_status, fragment);
        }
//        transaction.commit();
        transaction.addToBackStack(null).commit();
    }
}
