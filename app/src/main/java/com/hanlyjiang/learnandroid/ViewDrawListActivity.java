package com.hanlyjiang.learnandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hanlyjiang.learnandroid.android.CircleMarkTimeViewActivity;
import com.hanlyjiang.learnandroid.android.listview.DeptmentActivity;
import com.hanlyjiang.learnandroid.android.simple.NestedViewActivity;
import com.hanlyjiang.learnandroid.android.view.MainDividerActivity;
import com.hanlyjiang.learnandroid.android.view.SimpleViewGroupActivity;
import com.hanlyjiang.learnandroid.viewdraw.SelfDrawView01Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jianghanghang on 2017/2/10.
 */
public class ViewDrawListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    private List<Map<String, Object>> mActivities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list_entry);
        setStartItems();

        SimpleAdapter adapter = new SimpleAdapter(this, mActivities, android.R.layout.simple_list_item_2,
                new String[]{"title", "desc"},
                new int[]{android.R.id.text1, android.R.id.text2,});
        ListView mItemList = (ListView) findViewById(R.id.listView);
        mItemList.setAdapter(adapter);
        mItemList.setOnItemClickListener(this);

    }


    private void setStartItems() {
        mActivities = new ArrayList<>();
        mActivities.add(makeActItem(SelfDrawView01Activity.class, "自定义View绘制", "自定义View绘制"));
        mActivities.add(makeActItem(CircleMarkTimeViewActivity.class, "自定义时间View", "自定义时间View"));
        mActivities.add(makeActItem(SimpleViewGroupActivity.class, "ViewGroup 测试 ", "测试ViewGroup 自定义\n "));
        mActivities.add(makeActItem(MainDividerActivity.class, "自定义View 测试 ", "自定义分割线\n "));
        mActivities.add(makeActItem(DeptmentActivity.class, "组织结构 测试 ", "组织结构\n "));
        mActivities.add(makeActItem(NestedViewActivity.class, "嵌套滚动View测试", "嵌套滚动View测试\n "));
    }

    private Map<String, Object> makeActItem(Class activity, String title, String decs) {
        Map<String, Object> mapItem;
        mapItem = new ArrayMap<>();
        mapItem.put("class", activity);
        mapItem.put("title", title);
        mapItem.put("desc", decs);
        return mapItem;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        @SuppressWarnings("unchecked")
        Map<String, Object> item = (Map<String, Object>) parent.getAdapter().getItem(position);
        intent.setClass(this, (Class<?>) item.get("class"));
        startActivity(intent);
    }

}