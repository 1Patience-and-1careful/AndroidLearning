package com.geostar.imagewall;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * http://blog.csdn.net/guolin_blog/article/details/34093441
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 * https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 * https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html#getBitmap(android.content.ContentResolver, android.net.Uri)
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 */
public class MainActivity2 extends AppCompatActivity  {

    private static final String TAG = "MainActivity" ;
    private HorizontalGridView gridView = null;
    private Cursor cursor;
    private int mLastVisableItem;
    private ImageView mPicView;
    private PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = (HorizontalGridView) findViewById(R.id.hGridview);
        mPicView = (ImageView) findViewById(R.id.iv_pic_viewer);
        mAttacher = new PhotoViewAttacher(mPicView);

//        gridView.setLayoutManager(new LinearLayoutManager(this));
//        //recyclerview滚动监听
//        gridView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                //0：当前屏幕停止滚动；1时：屏幕在滚动 且 用户仍在触碰或手指还在屏幕上；2时：随用户的操作，屏幕上产生的惯性滑动；
//                // 滑动状态停止并且剩余少于两个item时，自动加载下一页
//                if (newState == RecyclerView.SCROLL_STATE_IDLE
//                        && mLastVisableItem +2>=mLayoutManager.getItemCount()) {
//                    new GetData().execute("http://gank.io/api/data/福利/10/"+(++page));
//                }
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                //获取加载的最后一个可见视图在适配器的位置。
//                mLastVisableItem = gridView.getLayoutManager().get.findLastVisibleItemPosition();
//            }
//        });
        queryImages();
        Picasso.Builder builder = new Picasso.Builder(this);
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
        gridView.addOnScrollListener(new MyScrollListener());


    }

    @NonNull
    private String getImageFileKey(String imageFilePath) {
        return imageFilePath.replace(" ","_").replace(File.separator,"-");
    }

    class MyScrollListener extends RecyclerView.OnScrollListener{

        public MyScrollListener() {
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG,String.format(Locale.CHINA,"dx=%d ; dy=%d ; ",dx,dy));
        }
    }
//    @Override
//    public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//    }
//
//    @Override
//    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.d(TAG,String.format(Locale.CHINA,"firstVisibleItem=%d ; visibleItemCount=%d ; totalItemCount = %d",firstVisibleItem,visibleItemCount,totalItemCount));
//
//    }



    class ImageAdapter extends RecyclerView.Adapter{

        private static final String TAG = "ImageAdapter";
        private Cursor cursor;
        private Context mContext;
        private int mCurrentPage = 1; // 从1开始
        private int mPageSize = 20;
        

        public ImageAdapter(Context context, Cursor cursor) {
            this.cursor = cursor;
            mContext = context;
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
            if(viewType == -1 || viewType == 1 ){
                TextView convertView = (TextView) View.inflate(mContext,android.R.layout.simple_list_item_1,null);
                convertView.setText(viewType==-1?"上一页":"下一页");
                return new TextHolder(convertView);
            }
            View v = View.inflate(mContext, R.layout.image_item, null);
            ImageHolder holder = new ImageHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof  ImageHolder){
                if (position < 0 || position > getItemCount()) return;
                cursor.moveToFirst();
                cursor.move(getRealDataPosition(position));
                int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                Log.d(TAG, String.format(Locale.CHINA, "postion=%d, index=%d", getRealDataPosition(position), index));
                String imageData = cursor.getString(index);
                ImageView imageView = ((ImageHolder) holder).image;
                imageView.setOnClickListener(createOnClickListener(imageData));
                Picasso.with(imageView.getContext()).load(new File(imageData)).resize(120,90).into(imageView);
            }

        }


        private OnClickListener createOnClickListener(final String imageData) {
            return new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Picasso.with(getApplicationContext()).load(new File(imageData)).into(mPicView);
                    mAttacher.update();
                }
            };
        }

        private int getRealDataPosition(int postion){
//            return postion + (mCurrentPage-1)*mPageSize;
            return postion;
        }


        @Override
        public int getItemCount() {
//            return cursor.getCount()>mPageSize?mPageSize:cursor.getCount();
            return cursor.getCount();
        }

        class ImageHolder extends RecyclerView.ViewHolder {

            ImageView image;

            public ImageHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.image);
            }
        }

        class  TextHolder extends  RecyclerView.ViewHolder{
            TextView textView;

            public TextHolder(View itemView) {
                super(itemView);
                this.textView = (TextView) itemView;
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
