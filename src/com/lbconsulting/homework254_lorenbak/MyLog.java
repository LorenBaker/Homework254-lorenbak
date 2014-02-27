package com.lbconsulting.homework254_lorenbak;

import android.util.Log;

public class MyLog {
	private static String TAG = "HM254";

	public static void d(String className, String msg) {
		if (Log.isLoggable(TAG, Log.DEBUG)) {
			Log.d(TAG, className + ": " + msg);
		}
	}

	public static void i(String className, String msg) {
		if (Log.isLoggable(TAG, Log.INFO)) {
			Log.i(TAG, className + ": " + msg);
		}
	}

	public static void e(String className, String msg) {
		if (Log.isLoggable(TAG, Log.ERROR)) {
			Log.e(TAG, className + ": " + msg);
		}
	}

	public static void v(String className, String msg) {
		if (Log.isLoggable(TAG, Log.VERBOSE)) {
			Log.v(TAG, className + ": " + msg);
		}
	}

	public static void w(String className, String msg) {
		if (Log.isLoggable(TAG, Log.WARN)) {
			Log.w(TAG, className + ": " + msg);
		}
	}
}
