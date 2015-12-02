package com.hanlyjiang.android.intent;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

/**
 * 常见的Intent使用方式
 * @author hanyjiang(hanyjiang@outlook.com)
 *
 */
public class IntentUtils {

	
	public void choosePictures(Activity activity, int req) {
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		activity.startActivityForResult(intent, req);
	}
	
	/**
	 * 需要权限  android.Manifest.permission.WRITE_EXTERNAL_STORAGE 
	 * 
	 * @param activity
	 * @param req
	 */
	public void takePhoto(Activity activity,int req) {
		Intent intent = new Intent();
		intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		File path = new File(Environment.getExternalStorageDirectory(), "images");
		if (!path.exists()) {
			path.mkdirs();
		}
		File photo = new File(path, SimpleDateFormat.getDateTimeInstance()
				.format(new Date()) + ".jpg");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		activity.startActivityForResult(intent, req);
	}
}
