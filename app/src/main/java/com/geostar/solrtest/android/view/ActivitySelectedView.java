package com.geostar.solrtest.android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.geostar.solrtest.R;

import java.io.BufferedInputStream;

public class ActivitySelectedView extends AppCompatActivity implements View.OnClickListener {

    SelectedCircleView selectedCircleView;

    private int colors[] = new int[]{
            0xffa3d39c,
            0xff7da7d9,
            Color.CYAN,
            Color.LTGRAY,
            Color.YELLOW,
            Color.DKGRAY
    };
    private LinearLayout rootView;


    private SelectedBar selectBar;

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_view);

        rootView = (LinearLayout) findViewById(R.id.color_hookView);

        colors = getResources().getIntArray(R.array.color_bar);

        selectBar = new SelectedBar(rootView,colors);
        selectBar.setOnColorSelected(new SelectedBar.OnColorSelected() {
            @Override
            public void onColorSelected(int color) {
                //设置指示器的颜色值
                button.setBackgroundColor(color);
            }
        });
        button = (Button) findViewById(R.id.btn_change_selection);
    }


    @Override
    public void onClick(View v) {
        selectBar.getCurrentSelectColor();
    }


}
