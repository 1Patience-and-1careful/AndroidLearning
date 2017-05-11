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
import com.geostar.solrtest.utils.TestLogUtils;

import java.util.ArrayList;

/**
 * Created by jianghanghang on 2017/4/25.
 */

public class RecyclerViewHorizonActivity extends AppCompatActivity {

    private static final int SCROLL_EDGE = 360;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<TestBean> mDatas = new ArrayList<>();
    private TestBeanAdapter mBeanAdapter;

    private RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        public int mDxTotal;
        public int mCurrentScrollPos = -1;
        private long timeCost, startTime, endtime;
        public boolean isAppCodeDoing;
        public boolean isNewSessionStart;


        private String parseState(int state) {
            if (state == RecyclerView.SCROLL_STATE_IDLE) {
                return "SCROLL_STATE_IDLE";
            } else if (state == RecyclerView.SCROLL_STATE_DRAGGING) {
                return "SCROLL_STATE_DRAGGING";
            } else if (state == RecyclerView.SCROLL_STATE_SETTLING) {
                return "SCROLL_STATE_SETTLING";
            }
            return "";
        }

        /*
         * 实现横向地图浮云框 时自动滚动到一个Item的效果
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            TestLogUtils.d("onScrollStateChanged ScrollState : " + parseState(newState) + " isAppCodeDoing :" + isAppCodeDoing
                    + "; isNewSessionStart: " + isNewSessionStart);

            if (newState == RecyclerView.SCROLL_STATE_DRAGGING
                    || newState == RecyclerView.SCROLL_STATE_SETTLING) {
                if (!isAppCodeDoing) { // 如果不是程序代码引起的  开始记录下一轮

                    if (!isNewSessionStart) {
                        mDxTotal = 0;
                        startTime = System.currentTimeMillis();
                        // 设置当前的基准
                        mCurrentScrollPos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                    }

                    isNewSessionStart = true;
                }
                return;
            }


            // IDLE 状态
            if (isAppCodeDoing) { // 程序设置位置完成
                isAppCodeDoing = false;
                return;
            }

            // 如果总的滑动量为0 ，则无需处理
            if (mDxTotal == 0) {
                return;
            }

            endtime = System.currentTimeMillis();
            timeCost = endtime - startTime;
            isNewSessionStart = false;

            float vec = Math.abs((mDxTotal / timeCost) * 1000);

            int posToScroll = linearLayoutManager.findFirstVisibleItemPosition();

            if (Math.abs(mDxTotal) > SCROLL_EDGE || vec >= 100) { // 符合滑动条件
                if (mDxTotal > 0) { // 正向滑动
                    posToScroll += 1;
                } else { // 反向滑动
                    posToScroll -= 1;
                }
            } else { // 不满足条件，恢复状态
                posToScroll = mCurrentScrollPos; //
            }

            // 避免超出边界
            if (posToScroll >= mBeanAdapter.getItemCount()) {
                posToScroll = mBeanAdapter.getItemCount() - 1;
            }
            if (posToScroll < 0) {
                posToScroll = 0;
            }

            TestLogUtils.d(String.format("onScrollStateChanged Before CurrPos: %d ; pos : %d ；vec :%.2f ； mDxTotal：%d",
                    mCurrentScrollPos, posToScroll, vec, mDxTotal));
            RecyclerView.State state = new RecyclerView.State();

            isAppCodeDoing = true;
            if (posToScroll == mCurrentScrollPos) {
                linearLayoutManager.smoothScrollToPosition(recyclerView, state, posToScroll);
            } else {
                mCurrentScrollPos = posToScroll;
                linearLayoutManager.smoothScrollToPosition(recyclerView, state, posToScroll);
//                highlightApprovalItemOnMap(approvalAdapter.getItem(posToScroll));
            }

            TestLogUtils.d(String.format("onScrollStateChanged End CurrPos: %d ; pos : %d ；vec :%.2f ； mDxTotal：%d",
                    mCurrentScrollPos, posToScroll, vec, mDxTotal));

        }


        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (isNewSessionStart) {
                mDxTotal += dx;
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_horilzonal);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);

        linearLayoutManager = createLayoutManager();

        recyclerView.setLayoutManager(linearLayoutManager);
        for (int i = 0; i < 20; i++) {
            mDatas.add(new TestBean("武汉市武大吉奥信息技术有限公司第 " + i + " 分部"));
        }
        mBeanAdapter = new TestBeanAdapter();
        recyclerView.setAdapter(mBeanAdapter);
        recyclerView.setOnScrollListener(onScrollListener);
    }

    private LinearLayoutManager createLayoutManager() {
        SimpleLinearLayoutManager layoutManager = new SimpleLinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        layoutManager.setSmoothScrollbarEnabled(false);

//        SimpleHorizLayoutManager  layoutManager = new SimpleHorizLayoutManager(this);
        return layoutManager;
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
