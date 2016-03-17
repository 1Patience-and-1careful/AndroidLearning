package com.github.hanlyjiang.draglistdemo;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.woxthebox.draglistview.DragItemAdapter;

import java.util.List;


/**
 * Created by HanlyJiang on 2016/3/17.
 */
public class MyItemAdapter extends DragItemAdapter<DragData,MyItemAdapter.ItemHolder> {

    private LayoutInflater mInflater;
    private int mResId = R.layout.list_item;
    private int mGrabHandleId;

    public MyItemAdapter(Context context, List<DragData> data, int layoutid, int grabHandleId, boolean dragOnLongPress) {
        super(dragOnLongPress);
        setHasStableIds(true);
        setItemList(data);
        mResId = layoutid;
        mInflater = LayoutInflater.from(context);
        mGrabHandleId = grabHandleId;

    }


    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentItem = mInflater.inflate(mResId, parent, false);
        return new ItemHolder(contentItem);
    }


    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = getItemList().get(position).getName() + getItemList().get(position).getPos();
        holder.text1.setText(text);
        holder.itemView.setTag(text);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ItemHolder extends  DragItemAdapter.ViewHolder{

        TextView text1;

        public ItemHolder(View itemView) {
            super(itemView, mGrabHandleId);
            text1 = (TextView) itemView.findViewById(R.id.textView);
        }

        @Override
        public void onItemClicked(View view) {
            super.onItemClicked(view);
        }

        @Override
        public boolean onItemLongClicked(View view) {
            return super.onItemLongClicked(view);
        }

        @Override
        public boolean onItemTouch(View view, MotionEvent event) {
            return super.onItemTouch(view, event);
        }
    }


}


