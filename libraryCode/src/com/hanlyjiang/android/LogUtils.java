package com.hanlyjiang.android;

import android.content.Context;
import android.util.Log;

public class LogUtils {

	
	
	private static final String TAG = "Log.Debug";

	public static void logDebug(Context context, String msg){
		Log.d(TAG,msg);
	}
}
