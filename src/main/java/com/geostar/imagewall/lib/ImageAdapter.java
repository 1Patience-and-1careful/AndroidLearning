package com.geostar.imagewall.lib;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.geostar.imagewall.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

/**
 * Created by jianghanghang on 2016/8/25.
 */
public class ImageAdapter extends RecyclerView.Adapter {

    private static final String TAG = "ImageAdapter";

    private static final int HEADER = -1;
    private static final int CONTENT = 0;
    private static final int FOOTER = 1;

    private Cursor mDataCursor;
    private Context mContext;
    private int imageHeight, imageWidth;
    private OnItemClickListener mOnItemClickListener;
    private OnPageChangedListener mOnPageChangedListener;
    private Picasso mPicasso;

    private int mCurrentPage = 1; // 从1开始
    private int mPageSize = 20;
    private int mCurrentStart = 0; // 当前开始的地方

    public int getmCurrentStart() {
        return mCurrentStart;
    }

    public void setCurrentStart(int startIndex) {
        if(startIndex < 0 ){
            mCurrentStart = 0;
        }else if(startIndex >= mDataCursor.getCount() - mPageSize ){
            mCurrentStart = mDataCursor.getCount() - mPageSize;
        }else {
            mCurrentStart = startIndex;
        }
        notifyDataSetChanged();
    }

    public ImageAdapter(Context context, Cursor cursor, Picasso picasso) {
        this.mDataCursor = cursor;
        mContext = context;
        imageWidth = mContext.getResources().getDimensionPixelOffset(R.dimen.image_width);
        imageHeight = mContext.getResources().getDimensionPixelOffset(R.dimen.image_height);
        mPicasso = picasso;
    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        } else if (position == getItemCount() - 1) {
            return FOOTER;
        }
        return CONTENT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        View view = null;
        switch (viewType) {
            case HEADER:
                view = View.inflate(mContext, R.layout.text_item, null);
                ((Button) view.findViewById(R.id.text)).setText("上一页");
                holder = new TextHolder(view);
                break;
            case FOOTER:
                view = View.inflate(mContext, R.layout.text_item, null);
                ((Button) view.findViewById(R.id.text)).setText("下一页");
                holder = new TextHolder(view);
                break;
            case CONTENT:
                view = View.inflate(mContext, R.layout.image_item, null);
                holder = new ImageHolder(view);
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case HEADER:
                ((TextHolder) holder).textView.setVisibility(mCurrentPage == 1 ? View.GONE : View.VISIBLE);
                ((TextHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentPage -= 1;
                        notifyDataSetChanged();
                        if(mOnPageChangedListener != null){
                            mOnPageChangedListener.toPrivPage(ImageAdapter.this);
                        }
                    }
                });
                break;
            case FOOTER:
                ((TextHolder) holder).textView.setVisibility(isLastItem() ? View.GONE : View.VISIBLE);
                ((TextHolder) holder).textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentPage += 1;
                        notifyDataSetChanged();
                        if(mOnPageChangedListener != null){
                            mOnPageChangedListener.toNextPage(ImageAdapter.this);
                        }
                    }
                });
                break;
            case CONTENT:
                mDataCursor.moveToFirst();
                mDataCursor.move(getRealDataPosition(position));
                int index = mDataCursor.getColumnIndex(MediaStore.Images.Media.DATA);
                Log.d(TAG, String.format(Locale.CHINA, "postion=%d, index=%d", getRealDataPosition(position), index));
                String imageData = mDataCursor.getString(index);
                ImageView imageView = ((ImageHolder) holder).image;
                imageView.setOnClickListener(createOnClickListener(imageData));

                mPicasso.load(new File(imageData)).resize(imageWidth, imageHeight)
                        .centerInside()
                        .into(imageView);
                break;
            default:
                break;
        }
    }

    private boolean isLastItem() {
        return mCurrentPage == mDataCursor.getCount() / mPageSize + 1;
    }

    public interface OnItemClickListener {

        void onItemClick(View v, String itemImagePath);

    }

    /**
     * 翻页时调用的逻辑
     */
    public interface OnPageChangedListener{
        void toPrivPage(ImageAdapter adapter);
        void toNextPage(ImageAdapter adapter);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public void setOnPageChangedListener(OnPageChangedListener pageChangedListener) {
        this.mOnPageChangedListener = pageChangedListener;
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
        return mCurrentStart + postion;
//        return (mCurrentPage - 1) * mPageSize + postion - 1;
//        return postion;
    }


    @Override
    public int getItemCount() {
//            return cursor.getCount()>mPageSize?mPageSize:cursor.getCount();
        return mDataCursor.getCount() < mPageSize ? mDataCursor.getCount() : mPageSize + 2; // Add Header and Footer
    }

    class ImageHolder extends RecyclerView.ViewHolder {

        ImageView image;

        public ImageHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    class TextHolder extends RecyclerView.ViewHolder {
        Button textView;

        public TextHolder(View itemView) {
            super(itemView);
            this.textView = (Button) itemView.findViewById(R.id.text);
        }
    }
}
