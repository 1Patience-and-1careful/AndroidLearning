package com.geostar.solrtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.geostar.solrtest.ftp.FTPDownloadActivity;
import com.geostar.solrtest.solr.SolrTestActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jianghanghang on 2017/2/10.
 */
public class LauncherMenuListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


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
        mActivities.add(makeActItem(SolrTestActivity.class, "Solr 服务测试 ", "连接Solr服务查询测试"));
        mActivities.add(makeActItem(FTPDownloadActivity.class, "FTP 服务连接下载测试 ", "使用Apache 包连接FTP服务"));
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