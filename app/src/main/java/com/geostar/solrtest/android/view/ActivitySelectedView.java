package com.geostar.solrtest.android.view;

import android.graphics.Color;
import android.support.annotation.Size;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.geostar.solrtest.R;
import com.geostar.solrtest.android.view.colorselected.ColorSelectBar;
import com.geostar.solrtest.android.view.colorselected.ColorCircleSelectorView;

public class ActivitySelectedView extends AppCompatActivity implements View.OnClickListener, SizeSelectBar.OnItemSelectedListener {

    ColorCircleSelectorView colorCircleSelectorView;

    private int colors[] = new int[]{
            0xffa3d39c,
            0xff7da7d9,
            Color.CYAN,
            Color.LTGRAY,
            Color.YELLOW,
            Color.DKGRAY
    };
    private LinearLayout rootView;


    private ColorSelectBar selectBar;

    private Button button;

    private SizeSelectBar sizeSelectBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_view);

        rootView = (LinearLayout) findViewById(R.id.color_hookView);

        colors = getResources().getIntArray(R.array.color_bar);

        selectBar = new ColorSelectBar(rootView, colors);
        selectBar.setOnColorSelected(new ColorSelectBar.OnColorSelected() {
            @Override
            public void onColorSelected(int color) {
                //设置指示器的颜色值
                button.setBackgroundColor(color);
            }
        });
        button = (Button) findViewById(R.id.btn_change_selection);
        sizeSelectBar = (SizeSelectBar) findViewById(R.id.ssb_sizebar);
        sizeSelectBar.setOnItemSelectedListener(this);
    }


    @Override
    public void onClick(View v) {
        selectBar.getCurrentSelectColor();
    }


    @Override
    public void onItemSelected(int size) {
        Toast.makeText(this, "Selected Size:" + size, Toast.LENGTH_SHORT).show();
    }
}
