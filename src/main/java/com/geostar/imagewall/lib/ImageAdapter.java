package com.geostar.imagewall.lib;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.geostar.imagewall.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

/**
 * Created by jianghanghang on 2016/8/25.
 */
public class ImageAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ImageAdapter";
    private Cursor mDataCursor;
    private Context mContext;
    private int mCurrentPage = 1; // 从1开始
    private int mPageSize = 20;

    private int imageHeight,imageWidth;


    private OnItemClickListener mOnItemClickListener;



    public ImageAdapter(Context context, Cursor cursor) {
        this.mDataCursor = cursor;
        mContext = context;
        imageWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.image_width);
        imageHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.image_height);
    }

    @Override
    public int getItemViewType(int position) {
//            if(position == 0 ){
//                return -1;
//            }else if (position == mPageSize){
//                return 1;
//            }
        return 0;
//            return super.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == -1 || viewType == 1) {
            TextView convertView = (TextView) View.inflate(mContext, android.R.layout.simple_list_item_1, null);
            convertView.setText(viewType == -1 ? "上一页" : "下一页");
            return new TextHolder(convertView);
        }
        View v = View.inflate(mContext, R.layout.image_item, null);
        ImageHolder holder = new ImageHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ImageHolder) {
            if (position < 0 || position > getItemCount()) return;
            mDataCursor.moveToFirst();
            mDataCursor.move(getRealDataPosition(position));
            int index = mDataCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            Log.d(TAG, String.format(Locale.CHINA, "postion=%d, index=%d", getRealDataPosition(position), index));
            String imageData = mDataCursor.getString(index);
            ImageView imageView = ((ImageHolder) holder).image;
            imageView.setOnClickListener(createOnClickListener(imageData));

            Picasso.with(imageView.getContext()).load(new File(imageData)).resize(imageWidth, imageHeight)
                    .centerInside()
                    .into(imageView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View v, String itemImagePath);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    private View.OnClickListener createOnClickListener(final String imageData) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, imageData);
                }
            }
        };
    }

    private int getRealDataPosition(int postion) {
//            return postion + (mCurrentPage-1)*mPageSize;
        return postion;
    }


    @Override
    public int getItemCount() {
//            return cursor.getCount()>mPageSize?mPageSize:cursor.getCount();
        return mDataCursor.getCount();
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    class TextHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView;
        }
    }
}
