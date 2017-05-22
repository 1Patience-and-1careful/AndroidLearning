package com.geostar.solrtest.android.listview;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.geostar.solrtest.R;
import com.geostar.solrtest.android.listview.deptment.Deptment;
import com.geostar.solrtest.android.listview.deptment.DeptmentList;
import com.geostar.solrtest.android.listview.deptment.DeptmentResponse;
import com.geostar.solrtest.android.listview.deptment.User;
import com.geostar.solrtest.base.BaseFragment;
import com.geostar.solrtest.utils.TestLogUtils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static com.geostar.solrtest.android.listview.DeptUnqData.TYPE_DEPT;
import static com.geostar.solrtest.android.listview.DeptUnqData.TYPE_USER;

/**
 * @author hanlyjiang on 2017/5/22-下午5:07.
 * @version 1.0
 */

public class DeptMentFragment extends BaseFragment implements DeptInterface.OnDeptClickListener,
        DeptInterface.OnUserClickListener {

    private RecyclerView recyclerView;

    private LinearLayout deptIndicator;
    private DeptmentList mDeptmentLists;
    private ExpandableDataAdapter mAdapter;

    private Stack<Deptment> naviHistory = new Stack<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_deptment;
    }

    @Override
    public void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        deptIndicator = (LinearLayout) findViewById(R.id.ll_indicator_bar);
        createTestData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ExpandableDataAdapter();
        mAdapter.updateData(transData(mDeptmentLists.getRows()));
        mAdapter.setDeptClickListener(this);
        mAdapter.setUserClickListener(this);
        recyclerView.setAdapter(mAdapter);


    }

    /**
     * 数据格式转换
     *
     * @param rows
     * @return
     */
    private List<DeptUnqData> transData(List<Deptment> rows) {
        List<DeptUnqData> datas = new ArrayList<>();
        if (rows == null && rows.size() == 0) {
            return datas;
        }
        for (Deptment deptment : rows) {
            datas.add(new DeptUnqData(deptment));
        }
        return datas;
    }


    /**
     * 数据格式转换
     *
     * @param deptment
     * @return
     */
    private List<DeptUnqData> transData(Deptment deptment) {
        List<DeptUnqData> datas = new ArrayList<>();
        if (deptment == null) {
            return datas;
        }
        for (Deptment item : deptment.getChildren()) {
            datas.add(new DeptUnqData(item));
        }
        for (User user : deptment.getUsers()) {
            datas.add(new DeptUnqData(user));
        }
        return datas;
    }


    private void createTestData() {
        InputStream inputStream = getResources().openRawResource(R.raw.contact_deptments);
        InputStreamReader reader = new InputStreamReader(inputStream);
        Gson gson = new Gson();
        DeptmentResponse response = gson.fromJson(reader, DeptmentResponse.class);
        mDeptmentLists = response.getData();
        if (mDeptmentLists == null) {
            TestLogUtils.d("get dept data failed!!!");
        }
    }

    @Override
    public void onDeptClick(Deptment dept) {

        if (isDeptHasChilds(dept)) {
            mAdapter.updateData(transData(dept));
            naviHistory.push(dept);
        } else {
            Toast.makeText(getActivity(), "该部门是空的", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isDeptHasChilds(Deptment dept) {
        boolean hasDeptChild = dept.getChildren() != null && dept.getChildren().size() > 0;
        boolean hasUserChild = dept.getUsers() != null && dept.getUsers().size() > 0;
        return hasDeptChild || hasUserChild;
    }

    @Override
    public void onUserClick(User user) {
        Toast.makeText(getActivity(), "user Click", Toast.LENGTH_SHORT).show();

    }

    public boolean onBackPressed() {
        if (naviHistory.size() == 1) {
            naviHistory.pop();
            mAdapter.updateData(transData(mDeptmentLists.getRows()));
            return true;
        } else if (naviHistory.size() > 1) {
            naviHistory.pop();
            mAdapter.updateData(transData(naviHistory.peek()));
            return true;
        }
        return false;
    }


    private class ExpandableDataAdapter extends RecyclerView.Adapter {

        private DeptInterface.OnUserClickListener onUserClickListener;
        private DeptInterface.OnDeptClickListener deptClickListener;

        public void setUserClickListener(DeptInterface.OnUserClickListener onUserClickListener) {
            this.onUserClickListener = onUserClickListener;
        }

        public void setDeptClickListener(DeptInterface.OnDeptClickListener deptClickListener) {
            this.deptClickListener = deptClickListener;
        }

        private List<DeptUnqData> datas = new ArrayList<>();

        /**
         * 更新数据
         *
         * @param deptments
         */
        public void updateData(List<DeptUnqData> deptments) {
            datas.clear();
            datas.addAll(deptments);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            if (viewType == DeptUnqData.TYPE_DEPT) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_deptment, parent, false);
                viewHolder = new DeptViewHolder(itemView);
            } else if (viewType == TYPE_USER) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_user, parent, false);
                viewHolder = new UserViewHolder(itemView);
            }
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return getItemAtPositon(position).getType();
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            DeptUnqData itemData = getItemAtPositon(position);
            if (viewType == TYPE_USER) {
                UserViewHolder itemHolder = (UserViewHolder) holder;
                final User user = (User) itemData.getData();
                itemHolder.textView.setText(itemData.getName());
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onUserClickListener != null) {
                            onUserClickListener.onUserClick(user);
                        }
                    }
                });
            } else if (viewType == TYPE_DEPT) {
                DeptViewHolder itemHolder = (DeptViewHolder) holder;
                final Deptment deptment = (Deptment) getItemAtPositon(position).getData();
                itemHolder.textView.setText(itemData.getName());
                itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (deptClickListener != null) {
                            deptClickListener.onDeptClick(deptment);
                        }
                    }
                });
            }
        }

        public DeptUnqData getItemAtPositon(int position) {
            return datas.get(position);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

    }


    class DeptViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public DeptViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public UserViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
