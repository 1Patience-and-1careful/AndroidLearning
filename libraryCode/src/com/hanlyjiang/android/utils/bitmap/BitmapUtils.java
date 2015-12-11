package com.hanlyjiang.android.utils.bitmap;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

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
	
	/**
	 * 从Assets中获取图片资源
	 * 
	 * @param filename
	 *            文件目录
	 * @return 图片对象
	 */
	public Bitmap getBitmapFromAssets(Context context,String filename) {
		InputStream is = null;
		Bitmap bitmap = null;
		try {
			// 获取字节流
			is = context.getAssets().open(filename);
			// 转成图片对象
			bitmap = BitmapFactory.decodeStream(is);
		} catch (IOException e) {
			// TODO 
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					// 关闭字节流
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return bitmap;
	}
}
