package com.hanlyjiang.learnandroid.android.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.hanlyjiang.learnandroid.R;

public class MainDividerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_divider);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.rootview);

        DividerView dividerView = new DividerView(getApplicationContext());
        dividerView.setColor(R.color.divider_color);
        dividerView.setOrientation(LinearLayout.HORIZONTAL);
        dividerView.setRoundStartEnd(true);
        dividerView.setSize(10);

        linearLayout.addView(dividerView);

    }
}
