package com.geostar.solrtest.android.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.geostar.solrtest.R;
import com.geostar.solrtest.android.listview.contact.DeparMentFragment;

public class DeptmentActivity extends AppCompatActivity {

    private DeparMentFragment deptMentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deptment);
        if(deptMentFragment == null) {
            deptMentFragment = new DeparMentFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fl_fragment_stub, deptMentFragment)
                    .commit();
        }
    }


    @Override
    public void onBackPressed() {
        if(deptMentFragment.onBackPressed()){
            return ;
        }
        super.onBackPressed();

    }
}
