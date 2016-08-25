package com.geostar.imagewall;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.geostar.imagewall.lib.ImageAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * <br/>http://blog.csdn.net/guolin_blog/article/details/34093441
 * <br/>http://blog.csdn.net/guolin_blog/article/details/28863651
 * <br/>https://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 * <br/>https://developer.android.com/reference/android/provider/MediaStore.Images.Media.html#getBitmap(android.content.ContentResolver, android.net.Uri)
 * <br/>https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 * <br/>https://developer.android.com/reference/android/support/v17/leanback/widget/HorizontalGridView.html
 * <br/> 分页加载
 * <br/> picasso 文档：  http://square.github.io/picasso/
 * <br/> PhotoView： https://github.com/chrisbanes/PhotoView
 * <br/>
 */
public class MainActivityWithRecycleView extends AppCompatActivity {

    private static final String TAG = "MainActivity";


    private RecyclerView mHorizontalView = null;
    private Cursor mImagesCursor;
    private int mLastVisableItem;
    private ImageView mPhotoImageView;
    private PhotoViewAttacher mPhotoOperateAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHorizontalView = (RecyclerView) findViewById(R.id.hGridview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHorizontalView.setLayoutManager(layoutManager);

        mPhotoImageView = (ImageView) findViewById(R.id.iv_pic_viewer);
        mPhotoOperateAttacher = new PhotoViewAttacher(mPhotoImageView);

        mImagesCursor = queryMediaStoreImages();
        initialHorizontalView();
    }


    public Cursor queryMediaStoreImages() {
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = this.getContentResolver();
        String[] projection = new String[]{// 要输出的字段
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.SIZE,
        };
        return contentResolver.query(uri, projection, null, null, null);
    }

    private void initialHorizontalView() {
        ImageAdapter adapter = new ImageAdapter(this, mImagesCursor);
        adapter.setOnItemClickListener(new ImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, String itemImagePath) {
                Picasso.with(getApplicationContext()).load(new File(itemImagePath)).into(mPhotoImageView);
                mPhotoOperateAttacher.update();
            }
        });
        adapter.setOnPageChangedListener(new ImageAdapter.OnPageChangedListener() {

            @Override
            public void toPrivPage(ImageAdapter adapter) {
                mHorizontalView.getLayoutManager().scrollToPosition(1);
            }

            @Override
            public void toNextPage(ImageAdapter adapter) {
                mHorizontalView.getLayoutManager().scrollToPosition(1);
            }
        });
        mHorizontalView.setAdapter(adapter);
        mHorizontalView.addOnScrollListener(new MyScrollListener());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mImagesCursor != null) {
            mImagesCursor.close();
        }
    }


    @NonNull
    private String getImageFileKey(String imageFilePath) {
        return imageFilePath.replace(" ", "_").replace(File.separator, "-");
    }

    class MyScrollListener extends RecyclerView.OnScrollListener {

        public MyScrollListener() {
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG, String.format(Locale.CHINA, "dx=%d ; dy=%d ; ", dx, dy));
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
}
