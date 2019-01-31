package com.tomato.fqsdk.utils;


import com.tomato.fqsdk.fqutils.FLogger;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地缓存信息
 * 
 * @author tree
 * 
 */
public class SpUtils {


	private static SharedPreferences getPreferences(Context context) {
		if (context == null) {
			return null;
		}
		SharedPreferences preferences = context.getSharedPreferences("huayang_commonsdk_sp", context.MODE_PRIVATE);

		return preferences;
	};

	public static void setIntValue(Context context, String key, int value) {
		FLogger.d("set key:" + key + ",value:" + value, "sendLog");
		SharedPreferences preferences = getPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void setLongValue(Context context, String key, long value) {
		SharedPreferences preferences = getPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static long getLongValue(Context context, String key) {
		return getPreferences(context).getLong(key, 0);
	};



	public static void setStringValue(Context context, String key, String value) {
		FLogger.d("set key:" + key + ",value:" + value, "sendLog");
		SharedPreferences preferences = getPreferences(context);
		if (preferences == null) {
			return;
		}

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void setBooleanValue(Context context, String key, Boolean value) {
		SharedPreferences preferences = getPreferences(context);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(key, value);
		editor.commit();

	}

	public static Boolean getBooleanValue(Context context, String key) {

		return getPreferences(context).getBoolean(key, false);
	};

	public static int getIntValue(Context context, String key) {
		int value = getPreferences(context).getInt(key, 0);
		FLogger.d("get key:" + key + ",value:" + value, "sendLog");
		return value;
	};



	public static String getStringValue(Context context, String key) {
		SharedPreferences preferences = getPreferences(context);
		if (preferences == null) {
			return "";
		}
		String value = getPreferences(context).getString(key, "");
		FLogger.d("get key:" + key + ",value:" + value, "sendLog");
		return value;
	};

}
