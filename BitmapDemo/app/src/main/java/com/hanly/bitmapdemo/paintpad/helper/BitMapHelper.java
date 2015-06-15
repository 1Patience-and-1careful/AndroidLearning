package com.hanly.bitmapdemo.paintpad.helper;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by Hanly on 2015/4/19.
 */
public class BitMapHelper {


    private static final String TAG = "BitMapHelper";

    // mImageView.setImageBitmap(
    // decodeSampledBitmapFromResource(getResources(), R.id.myimage, 100, 100));
    /*
    get it from google:training/displaying-bitmaps/load-bitmap.html
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inScaled = true;

        Log.e(TAG, "options.inSampleSize" + options.inSampleSize);
        Log.e(TAG, "options.outHeight:" + options.outHeight);
        Log.e(TAG, "options.outwidth:" + options.outWidth);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
