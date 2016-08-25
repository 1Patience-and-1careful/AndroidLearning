package com.geostar.imagewall;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

/**
 * http://blog.csdn.net/guolin_blog/article/details/34093441
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 * https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 * https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html#getBitmap(android.content.ContentResolver, android.net.Uri)
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 */
public class GridView2Activity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String TAG = "MainActivity" ;
    private GridView gridView = null;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_hor);
        gridView = (GridView) findViewById(R.id.hGridview);
        queryImages();
    }

    public void queryImages() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = this.getContentResolver();
        String[] projection = new String[]{// 要输出的字段
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
        };
        cursor = contentResolver.query(uri, projection, null, null, null);
        ImageAdapter adapter = new ImageAdapter(this, cursor);
        gridView.setAdapter(adapter);

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    class ImageAdapter extends BaseAdapter{

        private static final String TAG = "ImageAdapter";
        private Cursor cursor;
        private Context mContext;

        public ImageAdapter(Context context, Cursor cursor) {
            this.cursor = cursor;
            mContext = context;
        }

        @Override
        public int getCount() {
            return cursor.getCount();
        }

        @Override
        public Cursor getItem(int position) {
            cursor.moveToFirst();
            cursor.move(position);
            return cursor;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageHolder holder = null;
            if(convertView == null){
                convertView  = View.inflate(mContext, R.layout.image_item, null);
                holder = new ImageHolder(convertView);
                convertView.setTag(holder);
            }else{
                holder = (ImageHolder) convertView.getTag();
            }
            if (position < 0 || position > getCount()) {

            }else {
                cursor.moveToFirst();
                cursor.move(position);
                int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                Log.d(TAG, String.format(Locale.CHINA, "postion=%d, index=%d", position, index));
                String imageData = cursor.getString(index);
//                loadBitmap(imageData, holder.image);
                int width = getResources().getDimensionPixelOffset(R.dimen.image_width);
                int height = getResources().getDimensionPixelOffset(R.dimen.image_height);
                Picasso.with(GridView2Activity.this).load(new File(imageData)).resize(width,height).into(holder.image);
            }
            return convertView;

        }

        class ImageHolder extends RecyclerView.ViewHolder {

            ImageView image;

            public ImageHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cursor != null) {
            cursor.close();
        }
    }
}
