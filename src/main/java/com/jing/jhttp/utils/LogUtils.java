package com.jing.jhttp.utils;

import android.util.Log;

public class LogUtils {

	public static boolean debug = true;

	public static void d(String tag, String msg) {
		
		if (debug) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		if (debug) {
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		if (debug) {
			Log.e(tag, msg);
		}
	}

	public static void w(String tag, String msg) {
		if (debug) {
			Log.w(tag, msg);
		}
	}

}
