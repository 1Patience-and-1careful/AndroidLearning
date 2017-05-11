package com.geostar.solrtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.geostar.solrtest.utils.FileViewer;

public class IntentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent_test);
        findViewById(R.id.btn_open_excel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FileViewer.getExcelFileIntent("/sdcard/GeoTempFiles/T_10_440512_D_GH_YSB_JGTZ_000_1494306074.xlsx"));
            }
        });
    }
}
