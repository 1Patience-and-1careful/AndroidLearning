package com.geostar.solrtest.android.recyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geostar.solrtest.R;

import java.util.ArrayList;

/**
 * Created by jianghanghang on 2017/4/25.
 */

public class RecyclerViewHorizonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<TestBean> mDatas = new ArrayList<>();
    private TestBeanAdapter mBeanAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_horilzonal);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(false);

        recyclerView.setLayoutManager(linearLayoutManager);
        for( int i =0; i<20; i++){
            mDatas.add(new TestBean("武汉市武大吉奥信息技术有限公司第 " + i  + " 分部"));
        }
        mBeanAdapter = new TestBeanAdapter();
        recyclerView.setAdapter(mBeanAdapter);
    }

    /**

     */
    protected class TestBeanAdapter extends RecyclerView.Adapter<ApprovalHolder> {

        @Override
        public ApprovalHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hori_recy_info, parent, false);
            ApprovalHolder holder = new ApprovalHolder(rootView);
            return holder;
        }

        @Override
        public void onBindViewHolder(ApprovalHolder holder, final int position) {
            final TestBean info = getItem(position);

            holder.name.setText(info.getName());

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        public TestBean getItem(int position) {
            return mDatas.get(position);
        }

    }

    class ApprovalHolder extends RecyclerView.ViewHolder {

        TextView name, frdb, gwhy, qydz, xy, anjianju, time;
        TextView checkStatus;
        ImageView checkStatusImg;

        public ApprovalHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.tv_enterprise_name);
            frdb = (TextView) itemView.findViewById(R.id.tv_frdb);
            gwhy = (TextView) itemView.findViewById(R.id.tv_gwhy);
            qydz = (TextView) itemView.findViewById(R.id.tv_qydz);
            xy = (TextView) itemView.findViewById(R.id.tv_x_y);
            anjianju = (TextView) itemView.findViewById(R.id.tv_belong);
            time = (TextView) itemView.findViewById(R.id.tv_time);
            checkStatus = (TextView) itemView.findViewById(R.id.tv_check_status);
            checkStatusImg = (ImageView) itemView.findViewById(R.id.iv_check_status);
        }
    }

    class TestBean {

        private String name;

        public String getName() {
            return name;
        }

        public TestBean(String name) {
            this.name = name;
        }
    }
}
