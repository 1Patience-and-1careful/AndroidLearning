package com.hanly.bitmapdemo.paintpad.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hanly.bitmapdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Hanly on 2015/4/20.
 */
public class ImageAdapter extends BaseAdapter{

    private Context mContext = null;
    private int mLayoutResId = R.layout.simple_imageview_with_text;
    private List<Map<String,Integer>> mData = new ArrayList<Map<String, Integer>>();

    private LayoutInflater mInflater  = null;

    public static final String KEY_IMAGE_RESOURCE = "image_res_id";

    public ImageAdapter(Context context){
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        initData();
    }

    private void initData(){
        final int[] imageResId = {
                R.drawable.bg1,
                R.drawable.bg2,
                R.drawable.bg3,
                R.drawable.bg4,
                R.drawable.bg5,
                R.drawable.bg6,
        };
        for ( int i = 0 ; i < imageResId.length; i++ ){
            Map<String,Integer> item = new HashMap<String,Integer>();
            item.put(KEY_IMAGE_RESOURCE,imageResId[i]);
            mData.add(item);
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mInflater.inflate(mLayoutResId,null).findViewById(R.id.simple_image);
        }
        convertView.setBackgroundResource(mData.get(position).get(KEY_IMAGE_RESOURCE));
        return convertView;
    }
}
