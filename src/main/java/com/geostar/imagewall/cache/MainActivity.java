package com.geostar.imagewall.cache;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.geostar.imagewall.R;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * http://blog.csdn.net/guolin_blog/article/details/34093441
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 * https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 * https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html#getBitmap(android.content.ContentResolver, android.net.Uri)
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 */
public class MainActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private static final String TAG = "MainActivity" ;
    private HorizontalGridView gridView = null;
    private Cursor cursor;
    private DiskLruCache disCache;
    private LruCache<String, Bitmap> mMemoryCache;
    private Map<String,BitmapWorkerTask> mReadPicWork = Collections.synchronizedMap(new HashMap<String,BitmapWorkerTask>());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 返回M
//        final int memClass = ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // 返回byte
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory/20;
        Log.d(TAG, String.format("缓存大小: %dKB %dM",cacheSize,cacheSize/1024 ));
        // 1/8 的可用内存作为内存缓存
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                Log.d(TAG,String.format("Key=%s size = %dKB",key,value.getByteCount()/1024));
                return value.getByteCount() / 1024;
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
//                oldValue.recycle();
                Log.d(TAG,"entryRemoved : " + key);
                super.entryRemoved(evicted, key, oldValue, newValue);
            }
        };
        gridView = (HorizontalGridView) findViewById(R.id.hGridview);
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

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    private Object getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }



    public void loadBitmap(String imageFilePath, ImageView imageView) {
        final String imageKey = getImageFileKey(imageFilePath);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            if(mReadPicWork.containsKey(imageKey)){
                mReadPicWork.get(imageKey).cancel(true);
                mReadPicWork.remove(imageKey);
            }
            BitmapWorkerTask task = new BitmapWorkerTask(imageView,imageFilePath);
            mReadPicWork.put(imageKey,task);
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            task.execute(imageFilePath);
//            mWorkThreads.execute(new BitMapLoader(imageView,imageFilePath));
        }
    }

    @NonNull
    private String getImageFileKey(String imageFilePath) {
//        return String.valueOf(imageFilePath.hashCode());
        return imageFilePath.replace(" ","_").replace(File.separator,"-");

    }

    // 执行异步任务线程池
    protected java.util.concurrent.ExecutorService mWorkThreads = java.util.concurrent.Executors
            .newFixedThreadPool(3);


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    class BitMapLoader implements  Runnable{

        private ImageView mImageView;
        private String mImagePath;

        public BitMapLoader(ImageView mImageView, String mImagePath) {
            this.mImageView = mImageView;
            this.mImagePath = mImagePath;
        }

        @Override
        public void run() {
            final Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(
                    mImagePath, getResources().getDimensionPixelOffset(R.dimen.image_width),
                    getResources().getDimensionPixelOffset(R.dimen.image_height));
            addBitmapToMemoryCache(getImageFileKey(mImagePath),bitmap);
            mImageView.post(new Runnable() {
                @Override
                public void run() {
                    mImageView.setImageBitmap(bitmap);
                }
            });

        }
    }

    /**
     * 从硬盘加载到内存缓存
     */
    public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> mImageView;
        private String imagePath;

        public BitmapWorkerTask(ImageView imageView,String imagePath) {
            mImageView = new WeakReference<ImageView>(imageView);
            this.imagePath = imagePath;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            mImageView.get().setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
            mReadPicWork.remove(imagePath);
        }

        @Override
        protected void onCancelled(Bitmap bitmap) {
            super.onCancelled(bitmap);
            mReadPicWork.remove(imagePath);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            final Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromFile(
                    imagePath, getResources().getDimensionPixelOffset(R.dimen.image_width),
                    getResources().getDimensionPixelOffset(R.dimen.image_height));
            addBitmapToMemoryCache(getImageFileKey(imagePath),bitmap);
            return bitmap;
        }
    }


    private Bitmap getBitmapFromMemCache(String imageKey) {
        return mMemoryCache.get(imageKey);
    }


    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

        private static final String TAG = "ImageAdapter";
        private Cursor cursor;
        private Context mContext;

        public ImageAdapter(Context context, Cursor cursor) {
            this.cursor = cursor;
            mContext = context;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(mContext, R.layout.image_item, null);
            ImageHolder holder = new ImageHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            if (position < 0 || position > getItemCount()) return;
            cursor.moveToFirst();
            cursor.move(position);
            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            Log.d(TAG, String.format(Locale.CHINA, "postion=%d, index=%d", position, index));
            String imageData = cursor.getString(index);
            loadBitmap(imageData, holder.image);
        }


        @Override
        public int getItemCount() {
            return cursor.getCount();
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
