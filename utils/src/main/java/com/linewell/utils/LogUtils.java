package com.linewell.utils;



import android.util.Log;

import com.linewell.constants.Constants;

/**
 * @author lzhiwei@linewell.com
 * @since 2014-8-7 下午4:13:40
 * @Description Log常用工具类
 */
public class LogUtils {

	/**
	 * Log显示级别，当要屏蔽应用中Log信息时可以设置级别大于7.
	 */
	public static int LOG_LEVEL = Constants.LOG_LEVEL;

	public static void i(String tag, String msg) {
		if (LOG_LEVEL <= Log.INFO)
			Log.i(tag, msg);
	}

	public static void i(String tag, String msg, Exception e) {
		if (LOG_LEVEL <= Log.INFO)
			Log.i(tag, msg, e);
	}

	public static void e(String tag, String msg) {
		if (LOG_LEVEL <= Log.ERROR)
			Log.e(tag, msg);
	}

	public static void e(String tag, String msg, Exception e) {
		if (LOG_LEVEL <= Log.ERROR)
			Log.e(tag, msg, e);
	}

	public static void d(String tag, String msg) {
		if (LOG_LEVEL <= Log.DEBUG)
			Log.d(tag, msg);
	}

	public static void d(String tag, String msg, Exception e) {
		if (LOG_LEVEL <= Log.DEBUG)
			Log.d(tag, msg, e);
	}

	public static void v(String tag, String msg) {
		if (LOG_LEVEL <= Log.VERBOSE)
			Log.v(tag, msg);
	}

	public static void v(String tag, String msg, Exception e) {
		if (LOG_LEVEL <= Log.VERBOSE)
			Log.v(tag, msg, e);
	}

	public static void w(String tag, String msg) {
		if (LOG_LEVEL <= Log.WARN)
			Log.w(tag, msg);
	}

	public static void w(String tag, String msg, Exception e) {
		if (LOG_LEVEL <= Log.WARN)
			Log.w(tag, msg, e);
	}
}
