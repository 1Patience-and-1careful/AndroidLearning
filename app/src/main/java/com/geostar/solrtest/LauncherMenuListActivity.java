package com.geostar.solrtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.geostar.solrtest.android.listview.DeptmentActivity;
import com.geostar.solrtest.android.listview.ExpandableListActivity;
import com.geostar.solrtest.android.recyclerview.RecyclerViewHorizonActivity;
import com.geostar.solrtest.android.view.MainDividerActivity;
import com.geostar.solrtest.android.view.SimpleViewGroupActivity;
import com.geostar.solrtest.ftp.FTPDownloadActivity;
import com.geostar.solrtest.solr.SolrTestActivity;
import com.geostar.solrtest.solr.request.TwoFuncTestActivity;

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
        mActivities.add(makeActItem(TwoFuncTestActivity.class, "HTTP 接口测试 ", "测试两个HTTP接口\n 1. 通过坐标获取行政区划；2. 获取excel下载路径"));
        mActivities.add(makeActItem(RecyclerViewHorizonActivity.class, "RecyclerView 测试 ", "测试RecyvlerView 的水平布局\n "));
        mActivities.add(makeActItem(SimpleViewGroupActivity.class, "ViewGroup 测试 ", "测试ViewGroup 自定义\n "));
        mActivities.add(makeActItem(SimpleViewGroupActivity.class, "Intent 测试 ", "测试Intent 4.4 打开文件\n "));
        mActivities.add(makeActItem(MainDividerActivity.class, "自定义View 测试 ", "自定义分割线\n "));
        mActivities.add(makeActItem(ExpandableListActivity.class, "自定义 Expandable View 测试 ", "Expandables\n "));
        mActivities.add(makeActItem(DeptmentActivity.class, "组织结构 测试 ", "组织结构\n "));
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