package com.geostar.solrtest.android.listview.contact;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.geostar.solrtest.R;
import com.geostar.solrtest.android.listview.deptment.Deptment;
import com.geostar.solrtest.android.listview.deptment.IndicatorItem;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 组织结构 indicator bar
 *
 * @author hanlyjiang on 2017/5/22-下午7:57.
 * @version 1.0
 */

public class CommonIndicatorBarView<T> {

    interface OnItemClickListenr<T> {
        void onGoBack(int position, T item);
    }

    private final IndicatorAdapter indicatorAdapter;
    private ArrayList<IndicatorItem<T>> data = new ArrayList<>();
    private RecyclerView recyclerView;

    private OnItemClickListenr<T> onItemClickListenr;

    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("indicators", data);
    }

    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null){
            data = (ArrayList<IndicatorItem<T>>) savedInstanceState.getSerializable("indicators");
            if(data != null){
                indicatorAdapter.notifyDataSetChanged();
            }else{
                data = new ArrayList<>();
            }
        }
    }

    public CommonIndicatorBarView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        indicatorAdapter = new IndicatorAdapter();
        recyclerView.setAdapter(indicatorAdapter);
    }

    public CommonIndicatorBarView(Context context) {
        this(new RecyclerView(context));
    }

    public void setOnItemClickListenr(OnItemClickListenr<T> onItemClickListenr) {
        this.onItemClickListenr = onItemClickListenr;
    }

    /**
     * 是否可以回退
     *
     * @return
     */
    public boolean isBackable() {
        return data.size() > 1;
    }

    /**
     * 获取View对象
     *
     * @return
     */
    public View getIndicatorView() {
        return recyclerView;
    }

    /**
     * 添加一个标签数据
     *
     * @param item
     * @param realData
     */
    public void addItem(String item, T realData) {
        data.add(new IndicatorItem<T>(item, realData));
        indicatorAdapter.notifyDataSetChanged();
        scrollToLastItem();
    }

    /**
     * 向后回退一级
     */
    public void backOne() {
        back(data.size() - 2);
        indicatorAdapter.notifyDataSetChanged();
    }

    private void scrollToLastItem() {
        if (data.size() == 0) {
            return;
        }
        recyclerView.smoothScrollToPosition(data.size() - 1);
    }

    /**
     * 回退到指定位置,该位置不会被移除
     *
     * @param where 位置
     */
    public void back(int where) {
        List<IndicatorItem<T>> retainItem = new ArrayList<>();
        for (int i = 0; i <= where; i++) {
            retainItem.add(data.get(i));
        }
        if (onItemClickListenr != null) {
            onItemClickListenr.onGoBack(retainItem.size() - 1,
                    retainItem.get(retainItem.size() - 1).getRealData());
        }
        data.clear();
        data.addAll(retainItem);
        indicatorAdapter.notifyDataSetChanged();
    }


    class IndicatorAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_indicator, parent, false);
            ViewHolder viewHolder = new ViewHolder(itemView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final IndicatorItem item = data.get(position);
            if (position == getItemCount() - 1) {
                viewHolder.textView.setText(item.getName() + "    ");
                viewHolder.itemView.setOnClickListener(null);
            } else {
                viewHolder.textView.setText(item.getName() + " >  ");
//                viewHolder.textView.setTextColor(Color.BLACK);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        back(position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(R.id.text);
            }
        }

    }

}
