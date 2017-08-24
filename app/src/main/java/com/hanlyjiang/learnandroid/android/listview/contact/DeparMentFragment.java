package com.hanlyjiang.learnandroid.android.listview.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hanlyjiang.learnandroid.R;
import com.hanlyjiang.learnandroid.android.listview.deptment.Deptment;
import com.hanlyjiang.learnandroid.android.listview.deptment.DeptmentList;
import com.hanlyjiang.learnandroid.android.listview.deptment.DeptmentResponse;
import com.hanlyjiang.learnandroid.android.listview.deptment.User;
import com.hanlyjiang.learnandroid.base.BaseFragment;
import com.hanlyjiang.learnandroid.utils.TestLogUtils;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanlyjiang on 2017/5/22-下午5:07.
 * @version 1.0
 */

public class DeparMentFragment extends BaseFragment {
    private static final String TAG = "DeparMentFragment";

    private RecyclerView recyclerView;

    private ExpandableDataAdapter mAdapter;

    private CommonIndicatorBarView<Deptment> deptIndicatorBarView;
    private RecyclerView recyclerViewIndicator;
    private Deptment rootDeptment;
    private Deptment currentShowingDept;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        System.out.println("onSaveInstanceState " + this.toString() );
        outState.putSerializable("rootDeptment", rootDeptment);
        outState.putSerializable("currentShowingDept", currentShowingDept);
        deptIndicatorBarView.onSaveInstanceState(outState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        System.out.println("onActivityCreated " + this.toString());
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            rootDeptment = (Deptment) savedInstanceState.getSerializable("rootDeptment");
            currentShowingDept = (Deptment) savedInstanceState.getSerializable("currentShowingDept");
            deptIndicatorBarView.onViewStateRestored(savedInstanceState);
        }
        if (rootDeptment == null) {
            rootDeptment = createTestData();
            currentShowingDept = rootDeptment;
            showDeptStructData(currentShowingDept);
        } else {
            showDeptStructData(currentShowingDept, false);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
//        System.out.println("onViewStateRestored");
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_deptment;
    }

    /**
     * 数据格式转换
     *
     * @param deptment
     * @return
     */
    private static List<DataWrapper> transData(Deptment deptment) {
        List<DataWrapper> datas = new ArrayList<>();
        if (deptment == null) {
            return datas;
        }
        for (Deptment item : deptment.getChildren()) {
            datas.add(new DataWrapper(item));
        }
        for (User user : deptment.getUsers()) {
            datas.add(new DataWrapper(user));
        }
        return datas;
    }


    private static boolean isDeptHasChilds(Deptment dept) {
        boolean hasDeptChild = dept.getChildren() != null && dept.getChildren().size() > 0;
        boolean hasUserChild = dept.getUsers() != null && dept.getUsers().size() > 0;
        return hasDeptChild || hasUserChild;
    }

    @Override
    public void initUI() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        recyclerViewIndicator = (RecyclerView) findViewById(R.id.recyclerview_indicatorbar);
        deptIndicatorBarView = new CommonIndicatorBarView(recyclerViewIndicator);
        deptIndicatorBarView.setOnItemClickListenr(new CommonIndicatorBarView.OnItemClickListenr<Deptment>() {
            @Override
            public void onGoBack(int position, Deptment item) {
                showDeptStructData(item, false);
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        mAdapter = new ExpandableDataAdapter();

        mAdapter.setItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClickListener(DataWrapper dataWrapper) {
                if (dataWrapper.getType() == DataWrapper.TYPE_DEPT) {
                    Deptment deptment = (Deptment) dataWrapper.getData();
                    if (isDeptHasChilds(deptment)) {
                        showDeptStructData(deptment);

                    } else {
                        Toast.makeText(getActivity(), "该部门是空的", Toast.LENGTH_SHORT).show();
                    }
                } else if (dataWrapper.getType() == DataWrapper.TYPE_USER) {
                    //点击用户
                    Toast.makeText(getActivity(), "点击用户 " + dataWrapper.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(mAdapter);
    }


    /**
     * 创建测试数据
     *
     * @return
     */
    private Deptment createTestData() {
        InputStream inputStream = getResources().openRawResource(R.raw.contact_deptments);
        InputStreamReader reader = new InputStreamReader(inputStream);
        Gson gson = new Gson();
        DeptmentResponse response = gson.fromJson(reader, DeptmentResponse.class);
        DeptmentList mDeptmentLists = response.getData();
        if (mDeptmentLists == null) {
            TestLogUtils.d("get dept data failed!!!");
        }
        rootDeptment = new Deptment();
        rootDeptment.setDeptname("组织结构");
        rootDeptment.setChildren(mDeptmentLists.getRows());
        return rootDeptment;
    }

    public boolean onBackPressed() {
        if (deptIndicatorBarView.isBackable()) {
            deptIndicatorBarView.backOne();
            return true;
        }
        return false;
    }


    /**
     * 显示指定的分组数据，但不加入到indicator
     *
     * @param deptment
     * @param updateIndicator
     */
    void showDeptStructData(Deptment deptment, boolean updateIndicator) {
        currentShowingDept = deptment;
        mAdapter.updateData(transData(deptment));
        if (updateIndicator) {
            deptIndicatorBarView.addItem(deptment.getDeptname(), deptment);
        }
    }

    void showDeptStructData(Deptment deptment) {
        showDeptStructData(deptment, true);

    }

    interface OnItemClickListener {
        void onItemClickListener(DataWrapper dataWrapper);
    }

    public class ExpandableDataAdapter extends RecyclerView.Adapter {

        private OnItemClickListener onItemClickListener;

        public void setItemClickListener(OnItemClickListener listener) {
            this.onItemClickListener = listener;
        }

        private List<DataWrapper> datas = new ArrayList<>();

        /**
         * 更新数据
         *
         * @param deptments
         */
        public void updateData(List<DataWrapper> deptments) {
            datas.clear();
            datas.addAll(deptments);
            notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;
            if (viewType == DataWrapper.TYPE_DEPT) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_deptment, parent, false);
                viewHolder = new DeptViewHolder(itemView);
            } else if (viewType == DataWrapper.TYPE_USER) {
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

        private View.OnClickListener createOnClickListener(final DataWrapper wrapper) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClickListener(wrapper);
                    }
                }
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            final DataWrapper itemData = getItemAtPositon(position);
            if (viewType == DataWrapper.TYPE_USER) {
                UserViewHolder itemHolder = (UserViewHolder) holder;
                itemHolder.textView.setText(itemData.getName());
                itemHolder.itemView.setOnClickListener(createOnClickListener(itemData));
            } else if (viewType == DataWrapper.TYPE_DEPT) {
                DeptViewHolder itemHolder = (DeptViewHolder) holder;
                itemHolder.textView.setText(itemData.getName());
                itemHolder.itemView.setOnClickListener(createOnClickListener(itemData));
            }
        }

        public DataWrapper getItemAtPositon(int position) {
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
