package com.geostar.solrtest.android.view.colorselected;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

import com.geostar.solrtest.R;

/**
 * 颜色选择条
 *
 * @author hanlyjiang on 2017/6/12-18:44.
 * @version 1.0
 */

public class ColorSelectBar {

    private ColorCircleSelectorView currentColorCircleSelectorView;

    private LinearLayout rootView;

    private int margining = 0;
    private int width = 0;
    private int[] colors;
    private Context context;
    private OnColorSelected onColorSelected;
    private int padding = 0;

    /**
     * 选中一个颜色时的监听
     */
    public interface OnColorSelected {
        void onColorSelected(int color);
    }

    /**
     * @param layout
     * @param colors
     */
    public ColorSelectBar(LinearLayout layout, int[] colors) {
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
        return currentColorCircleSelectorView.getColor();
    }


    private void init() {
        margining = getResources().getDimensionPixelOffset(R.dimen.common_margin);
        width = getResources().getDimensionPixelOffset(R.dimen.common_width);
        padding = getResources().getDimensionPixelOffset(R.dimen.common_padding);

        ColorCircleSelectorView singleView = null;
        int index = 0;
        for (int color : colors) {
            singleView = new ColorCircleSelectorView(context);
            singleView.setColor(color);
            singleView.setHoleColor(0xffffffff);
            singleView.setSelected(index == 0);
            singleView.setPadding(padding, padding, padding, padding);
            if (index == 0) {
                currentColorCircleSelectorView = singleView;
            }
            addSingleView(singleView);
            index++;
        }
    }

    private Resources getResources() {
        return context.getResources();
    }


    private void addSingleView(ColorCircleSelectorView singleView) {

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, width);
        layoutParams.setMargins(margining, margining, margining, margining);
        singleView.setLayoutParams(layoutParams);

        singleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View circleView) {
                if (circleView.isSelected()) {
                    return;
                }
                unselectOthers();
                circleView.setSelected(true);
                currentColorCircleSelectorView = (ColorCircleSelectorView) circleView;
                if (onColorSelected != null) {
                    onColorSelected.onColorSelected(((ColorCircleSelectorView) circleView).getColor());
                }
            }
        });
        rootView.addView(singleView);
    }

    private void unselectOthers() {
        currentColorCircleSelectorView.setSelected(false);
    }
}
