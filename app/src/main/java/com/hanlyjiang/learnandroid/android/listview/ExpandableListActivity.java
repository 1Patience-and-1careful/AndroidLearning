package com.hanlyjiang.learnandroid.android.listview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hanlyjiang.learnandroid.R;
import com.hanlyjiang.learnandroid.android.listview.deptment.DeptmentList;
import com.hanlyjiang.learnandroid.android.listview.deptment.DeptmentResponse;
import com.hanlyjiang.learnandroid.utils.TestLogUtils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;

public class ExpandableListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private DeptmentList mDeptmentLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expandable_list);

        createTestData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter( new ExpandableDataAdapter());
    }

    private void createTestData() {
        InputStream inputStream = getResources().openRawResource(R.raw.contact_deptments);
        InputStreamReader reader = new InputStreamReader(inputStream);
        Gson gson = new Gson();
        DeptmentResponse response = gson.fromJson(reader, DeptmentResponse.class);
        mDeptmentLists = response.getData();
        if(mDeptmentLists == null){
            TestLogUtils.d("get dept data failed!!!");
        }
    }


    private class ExpandableDataAdapter extends RecyclerView.Adapter {

        public static final int VIEW_TYPE_GROUP = 0;
        public static final int VIEW_TYPE_ITEM = 1;

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return null;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
