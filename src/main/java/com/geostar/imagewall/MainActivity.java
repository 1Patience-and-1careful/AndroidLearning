package com.geostar.imagewall;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v17.leanback.widget.HorizontalGridView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geostar.imagewall.cache.DiskLruCache;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * http://blog.csdn.net/guolin_blog/article/details/34093441
 * http://blog.csdn.net/guolin_blog/article/details/28863651
 * https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 * https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html#getBitmap(android.content.ContentResolver, android.net.Uri)
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 * https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 */
public class MainActivity extends AppCompatActivity {

    private HorizontalGridView gridView = null;
    private Cursor cursor;
    private  DiskLruCache disCache;
    private LruCache<String, Bitmap> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        // 1/8 的可用内存作为内存缓存
        mMemoryCache = new LruCache<String,Bitmap>(maxMemory / 4){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };

        try {
            File cacheDir = getDiskCacheDir(this, "bitmap");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            disCache = DiskLruCache.open(getDiskCacheDir(this,"bitmap"),getAppVersion(this)
            ,1, (long) (100*Math.pow(1024,2)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        gridView = (HorizontalGridView) findViewById(R.id.hGridview);
        queryImages();
    }


    public String hashKeyForDisk(String key) {
        String cacheKey = key;
//        try {
//            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
//            mDigest.update(key.getBytes());
//            cacheKey = bytesToHexString(mDigest.digest());
//        } catch (NoSuchAlgorithmException e) {
//            cacheKey = String.valueOf(key.hashCode());
//        }
//        return cacheKey;
        return String.valueOf(key.hashCode());
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

    public void queryImages(){
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = this.getContentResolver();
        String[] projection = new String[]{// 要输出的字段
//                MediaStore.Images.Thumbnails._ID,
//                MediaStore.Images.Thumbnails.IMAGE_ID,
            MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
        };
        cursor = contentResolver.query(uri,projection,null,null,null);
        ImageAdapter adapter = new ImageAdapter(this,cursor);
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

    Bitmap mRecyleBitmap = null;
    private Bitmap getImageData(String imageData) throws IOException {

        String key = hashKeyForDisk(imageData);
        if( mMemoryCache.get(key) != null ){
            return mMemoryCache.get(key);
        }

        DiskLruCache.Snapshot snapshot = disCache.get(key);
        if(snapshot != null){
//            if(mRecyleBitmap != null) {
//                mRecyleBitmap.recycle();
//            }
            mRecyleBitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
            addBitmapToMemoryCache(key,mRecyleBitmap);
            return mRecyleBitmap;
        }else{
            DiskLruCache.Editor editor = disCache.edit(key);
            OutputStream outStream = editor.newOutputStream(0);
            byte[] buffer = new byte[4096];
            InputStream inStream = new FileInputStream(imageData);
            int onByte = -1;
            while((onByte = inStream.read(buffer,0,4096))!= -1){
                outStream.write(buffer,0,onByte);
            }
            if(inStream != null){
                inStream.close();
            }
            if(outStream != null){
                outStream.close();
            }
            editor.commit();
            disCache.flush();
            return getImageData(imageData);
        }
    }





    class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder>{

        private static final String TAG = "ImageAdapter";
        private Cursor cursor;
        private Context mContext;

        public ImageAdapter(Context context, Cursor cursor) {
            this.cursor = cursor;
            mContext = context;
        }

        @Override
        public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = View.inflate(mContext,R.layout.image_item,null);
            ImageHolder holder = new ImageHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(ImageHolder holder, int position) {
            if(position < 0 || position > getItemCount()) return;
            cursor.moveToFirst();
            cursor.move(position);
            int index =  cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            Log.d(TAG,String.format(Locale.CHINA,"postion=%d, index=%d",position,index));
            String imageData = cursor.getString(index);
            try {
                holder.image.setImageBitmap(getImageData(imageData));
            } catch (IOException e) {
                e.printStackTrace();
            }
//            holder.image.setImageURI(Uri.fromFile(new File(imageData)));
                /*setImageBitmap(MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), Uri.withAppendedPath(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,imageData   )));*/
        }



        @Override
        public int getItemCount() {
            return cursor.getCount();
        }

        class ImageHolder extends RecyclerView.ViewHolder{

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
        if(cursor != null){
            cursor.close();
        }
    }
}
