package com.hanlyjiang.learnandroid.components;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * @author hanlyjiang on 2017/6/26-22:15.
 * @version 1.0
 */

public class TestImageViewActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startImageViewActivity();
        finish();
    }

    private void startImageViewActivity() {
        Intent intent = new Intent(this, ImageViewActivity.class);
        ArrayList<String> datas = createTestImageDatas();
        intent.putStringArrayListExtra("images", datas);
        intent.putExtra("index", 5);
        startActivity(intent);
    }

    private ArrayList<String> createTestImageDatas() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("http://seopic.699pic.com/photo/00015/9241.jpg_wh1200.jpg");
        arrayList.add("http://seopic.699pic.com/photo/00002/9586.jpg_wh1200.jpg");
        arrayList.add("http://seopic.699pic.com/photo/00023/6038.jpg_wh1200.jpg");
        arrayList.add("http://seopic.699pic.com/photo/00015/5357.jpg_wh1200.jpg");
        arrayList.add("http://seopic.699pic.com/photo/00045/2913.jpg_wh1200.jpg");
        arrayList.add("http://seopic.699pic.com/photo/00015/9241.jpg_wh1200.jpg");
        arrayList.add("http://seopic.699pic.com/photo/00015/9241.jpg_wh1200.jpg");
        return arrayList;
    }
}
