package com.geostar.solrtest.components;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.geostar.solrtest.R;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 图片浏览Activity
 * <br/>
 * intent 启动参数：
 * <li>1. images - String 类型list 存储图片url </li>
 * <li>2. index - int 型，存储第启动时查看的图片位于images 中的位置 </li>
 *
 * @author hanlyjiang on 2017/6/26-22:08.
 * @version 1.0
 */

public class ImageViewActivity extends Activity {

    private static final String TAG = "ImageViewActivity";
    private static final String IMGS = "images";
    private static final String CUR_INDEX = "index";

    private ViewPager viewPager;
    private PageIndicatorView pageIndicatorView;

    private int index;
    private List<String> imageUrls = new ArrayList<>();
    private ImagePageAdapter imagePageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!handleIntent()) {
            finish();
        }

        setContentView(R.layout.activity_image_view);

        initialUi();
    }

    private void initialUi() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        pageIndicatorView = (PageIndicatorView) findViewById(R.id.pageIndicatorView);
        imagePageAdapter = new ImagePageAdapter();
        viewPager.setAdapter(imagePageAdapter);
        viewPager.setCurrentItem(index, true);
        pageIndicatorView.setViewPager(viewPager);
        pageIndicatorView.setCount(getMaxPage());
        pageIndicatorView.setSelection(index);

    }

    private boolean handleIntent() {
        imageUrls = (List<String>) getIntent().getSerializableExtra(IMGS);
        if (imageUrls == null || imageUrls.isEmpty()) {
            Log.e(TAG, "images 数据项为空，无法启动");
            return false;
        }
        index = getIntent().getIntExtra(CUR_INDEX, 0);
        return true;
    }


    public int getMaxPage() {
        return imageUrls.size();
    }

    public String getImageUrl(int index) {
        return imageUrls.get(index);
    }

    public class ImagePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getMaxPage();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            String url = getImageUrl(position);
            View rootView = LayoutInflater.from(container.getContext())
                    .inflate(R.layout.image_view_layout, container, false);
            PhotoView imageView = (PhotoView) rootView.findViewById(R.id.photoview);
            Picasso.with(container.getContext()).load(url).into(imageView);
            container.addView(rootView);
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
