package com.hanlyjiang.learnandroid.android.listview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hanlyjiang.learnandroid.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActivityNestedListView extends AppCompatActivity {

    @BindView(R.id.list_view)
    ListView listView;
    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nested_list_view);
        unbinder = ButterKnife.bind(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                loadData());
        listView.setAdapter(arrayAdapter);
    }

    private List<String> loadData() {
        List<String> data = new ArrayList<>();
        for(int i = 0; i < 100; i++){
            data.add("测试数据 我是测试数据 " + i);
        }
        return data;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
