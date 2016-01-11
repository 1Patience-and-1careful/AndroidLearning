package com.hanlyjiang.android.utils.bitmap;

import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

/**
 * 
 * @author hanlyjiang@outlook.com
 *
 */
public class BitmapUtils {

	/**
	 * 通过Uri 获取 bitmap 对象
	 * 
	 * @param context
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static Bitmap getBitmapFromUri(Context context, Uri uri)
			throws IOException {
		ParcelFileDescriptor parcelFileDescriptor = context
				.getContentResolver().openFileDescriptor(uri, "r");
		FileDescriptor fileDescriptor = parcelFileDescriptor
				.getFileDescriptor();
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
	public Bitmap getBitmapFromAssets(Context context, String filename) {
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

	public static int calculate() {
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inJustDecodeBounds = true;
		// BitmapFactory.decodeResource(getResources(), R.id.myimage, options);
		// int imageHeight = options.outHeight;
		// int imageWidth = options.outWidth;
		// String imageType = options.outMimeType;
		return 0;
	}

	
	/** 从ResId 获得一定采样率的bitmap
	 * @param res Resource对象
	 * @param resId 资源id
	 * @param reqWidth 期望的宽(px)
	 * @param reqHeight 期望的高
	 * @return
	 */
	public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeResource(res, resId, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeResource(res, resId, options);
	}
	
	/**从图片路径获得一定采样率的bitmap
	 * @param filePath 路径
	 * @param reqWidth 期望宽
	 * @param reqHeight 期望高
	 * @return  图片
	 */
	public static Bitmap decodeSampledBitmapFromResource(String filePath,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(filePath, options);
	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return BitmapFactory.decodeFile(filePath, options);
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
	
	private Resources mRes;
	class BitmapWorkerTask extends AsyncTask<Integer, Void, Bitmap> {
	    private final WeakReference<ImageView> imageViewReference;
	    private int data = 0;

	    public BitmapWorkerTask(ImageView imageView) {
	        // Use a WeakReference to ensure the ImageView can be garbage collected
	        imageViewReference = new WeakReference<ImageView>(imageView);
	    }

	    // Decode image in background.
	    @Override
	    protected Bitmap doInBackground(Integer... params) {
	        data = params[0];
	        return decodeSampledBitmapFromResource(mRes, data, 100, 100);
	    }

	    // Once complete, see if ImageView is still around and set bitmap.
	    @Override
	    protected void onPostExecute(Bitmap bitmap) {
	        if (imageViewReference != null && bitmap != null) {
	            final ImageView imageView = imageViewReference.get();
	            if (imageView != null) {
	                imageView.setImageBitmap(bitmap);
	            }
	        }
	    }
	}
}
