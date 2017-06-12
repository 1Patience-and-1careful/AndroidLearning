package com.geostar.solrtest.android.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;

import com.geostar.solrtest.R;

/**
 * @author hanlyjiang on 2017/6/12-18:44.
 * @version 1.0
 */

public class SelectedBar {

    LinearLayout rootView;

    private int margining = 0;
    private int width = 0;
    private int[] colors;
    private Context context;
    private OnColorSelected onColorSelected;

    /**
     * 选中一个颜色时的监听
     */
    interface OnColorSelected {
        void onColorSelected(int color);
    }

    /**
     * @param layout
     * @param colors
     */
    public SelectedBar(LinearLayout layout, int[] colors) {
        this.rootView = layout;
        this.colors = colors;
        context = layout.getContext();
        init();
    }

    /**
     * 设置选中某个颜色时的监听
     *
     * @param onColorSelected
     */
    public void setOnColorSelected(OnColorSelected onColorSelected) {
        this.onColorSelected = onColorSelected;
    }

    /**
     * 获取当前选中的颜色
     *
     * @return
     */
    public int getCurrentSelectColor() {
        View child = null;
        for (int i = 0; i < rootView.getChildCount(); i++) {
            child = rootView.getChildAt(i);
            if (child instanceof SelectedCircleView) {
                if (child.isSelected()) {
                    return ((SelectedCircleView) child).getColor();
                }
            }
        }
        return Color.BLACK;
    }


    private void init() {
        margining = getResources().getDimensionPixelOffset(R.dimen.common_margin);
        width = getResources().getDimensionPixelOffset(R.dimen.common_width);

        SelectedCircleView singleView = null;
        int index = 0;
        for (int color : colors) {
            singleView = new SelectedCircleView(context);
            singleView.setColor(color);
            singleView.setHoleColor(0xffffffff);
            singleView.setSelected(index == 0);
            addSingleView(singleView);
            index++;
        }
    }

    private Resources getResources() {
        return context.getResources();
    }


    private void addSingleView(SelectedCircleView singleView) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        layoutParams.setMargins(margining, margining, margining, margining);
        singleView.setLayoutParams(layoutParams);

        singleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SelectedCircleView) v).setSelected(!v.isSelected());
                if (v.isSelected()) {
                    unselectOthers((SelectedCircleView) v);
                    if (onColorSelected != null) {
                        onColorSelected.onColorSelected(((SelectedCircleView) v).getColor());
                    }
                }
            }
        });
        rootView.addView(singleView);
    }

    private void unselectOthers(SelectedCircleView self) {
        View child = null;
        for (int i = 0; i < rootView.getChildCount(); i++) {
            child = rootView.getChildAt(i);
            if (child instanceof SelectedCircleView && child != self) {
                ((SelectedCircleView) child).setSelected(false);
            }
        }
    }
}
