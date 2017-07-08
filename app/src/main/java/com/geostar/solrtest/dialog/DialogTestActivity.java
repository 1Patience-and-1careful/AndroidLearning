package com.geostar.solrtest.dialog;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.geostar.solrtest.R;

public class DialogTestActivity extends AppCompatActivity {

    private CircleCommentPopup dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_test);

        findViewById(R.id.btn_dialog_001).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new CircleCommentPopup(DialogTestActivity.this);
                }
                dialog.show(v);
            }
        });

    }
}
