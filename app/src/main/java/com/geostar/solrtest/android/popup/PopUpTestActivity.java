package com.geostar.solrtest.android.popup;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.geostar.solrtest.R;

public class PopUpTestActivity extends AppCompatActivity {

    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_test);

        findViewById(R.id.btn_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopUp(v);
            }
        });
    }

    private void showPopUp(View v) {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(v.getContext());
            View view = LayoutInflater.from(v.getContext()).inflate(R.layout.popup_layout_01, null);
            popupWindow.setContentView(view);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup_bg_3));
            popupWindow.setOutsideTouchable(true);
        }
        popupWindow.showAsDropDown(v);

    }
}
