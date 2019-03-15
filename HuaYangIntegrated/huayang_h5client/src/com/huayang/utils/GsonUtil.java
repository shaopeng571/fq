package com.huayang.utils;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;



public class GsonUtil {
	private static Gson gson = null;
	static {
		if (gson == null) {
			gson = new Gson();
		}
	}

	private GsonUtil() {
	}
	
	/**
	 * 转成json
	 * 
	 * @param object
	 * @return
	 */
	public static String GsonToString(Object object) {
		String gsonString = null;
		try {
			if (gson != null) {
				gsonString = gson.toJson(object);
			}
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}

		return gsonString;
	}

	/**
	 * 转成bean
	 * 
	 * @param gsonString
	 * @param cls
	 * @return
	 */
	public static <T> T GsonToBean(String gsonString, Class<T> cls) {
		T t = null;
		try {
			if (gson != null) {
				t = gson.fromJson(gsonString, cls);
			}

		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
			
		}

		return t;
	}

	/**
	 * 转成list
	 * 
	 * @param gsonString
	 * @param cls
	 * @return
	 */
	public static <T> List<T> GsonToList(String gsonString, Class<T> cls) {
		List<T> list = null;
		try {

			if (gson != null) {
				list = gson.fromJson(gsonString, new TypeToken<List<T>>() {
				}.getType());
			}
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}

		return list;
	}

	/**
	 * 转成list中有map的
	 * 
	 * @param gsonString
	 * @return
	 */
	public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
		List<Map<String, T>> list = null;
		try {
			if (gson != null) {
				list = gson.fromJson(gsonString, new TypeToken<List<Map<String, T>>>() {
				}.getType());
			}
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}

		return list;
	}

	/**
	 * 转成map的
	 * 
	 * @param gsonString
	 * @return
	 */
	public static <T> Map<String, T> GsonToMaps(String gsonString) {

		Map<String, T> map = null;
		try {
			if (gson != null) {
				map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
				}.getType());
			}

		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}

		return map;
	}
}
