package com.geostar.solrtest.android.fragment;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import com.geostar.solrtest.R;
import com.geostar.solrtest.base.CachedObjFactory;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentNestedActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private CachedObjFactory<Fragment> fragmentCachedObjFactory;

    @BindView(R.id.rgp_menu)
    RadioGroup mainMenuGrp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_nested);
        ButterKnife.bind(this);

        initialUI();

        initialFragmentFactory();
    }

    private void initialUI() {
        mainMenuGrp.setOnCheckedChangeListener(this);
    }


    private void initialFragmentFactory() {
        fragmentCachedObjFactory = new CachedObjFactory<>();
        fragmentCachedObjFactory.addGenerator("message", new CachedObjFactory.ObjGenerator<Fragment>() {
            @Override
            public Fragment generateObj() {
                return new MessageFragment();
            }
        });
        fragmentCachedObjFactory.addGenerator("contact", new CachedObjFactory.ObjGenerator<Fragment>() {
            @Override
            public Fragment generateObj() {
                return new ContactFragment();
            }
        });
        // 动态
        fragmentCachedObjFactory.addGenerator("status", new CachedObjFactory.ObjGenerator<Fragment>() {
            @Override
            public Fragment generateObj() {
                return new StatusFragment();
            }
        });
    }



    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (checkedId == R.id.rb_contact) {
            switchPage("contact");
        } else if (checkedId == R.id.rb_message) {
            switchPage("message");
        } else if (checkedId == R.id.rb_status) {
            switchPage("status");
        }
    }

    private void switchPage(String fragKey) {
        Fragment fragment = fragmentCachedObjFactory.get(fragKey);
        if (!fragment.isAdded()) {
            Bundle args = new Bundle();
            args.putString("title",fragKey);
            fragment.setArguments(args);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (getSupportFragmentManager().findFragmentById(R.id.fl_fragment_stub) != null) {
            transaction.replace(R.id.fl_fragment_stub, fragment);
        } else {
            transaction.add(R.id.fl_fragment_stub, fragment);
        }
        transaction.commit();
    }
}
