package com.hanlyjiang.android.utils.bitmap;

import java.io.FileDescriptor;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

/**
 * 
 * @author hanlyjiang@outlook.com
 *
 */
public class BitmapUtils {
	
	/**
	 * 通过Uri 获取 bitmap 对象
	 * @param context
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmapFromUri(Context context,Uri uri) throws IOException {
	    ParcelFileDescriptor parcelFileDescriptor =
	    		context.getContentResolver().openFileDescriptor(uri, "r");
	    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
	    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
	    parcelFileDescriptor.close();
	    return image;
	}
}
