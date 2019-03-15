
package com.huayang.utils.encode;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.huayang.utils.LogTag;

import android.text.TextUtils;

//当字符串数据以url的形式传递给web服务器时,字符串中是不允许出现空格和特殊字符
public class UrlEncoder {

	private volatile static UrlEncoder instance = null;
	private String DEFAULT_ENC = "UTF-8";

	private UrlEncoder() {
	}

	public static UrlEncoder getInstance() {

		if (instance == null) {
			synchronized (UrlEncoder.class) {
				if (instance == null) {
					instance = new UrlEncoder();
				}
			}
		}
		return instance;
	}

	public String encode(String str) {

		if (TextUtils.isEmpty(str)) {
			return "";
		}
		String result = null;
		try {
			result = URLEncoder.encode(str, DEFAULT_ENC);
		} catch (UnsupportedEncodingException e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}
		return result;
	}

	public String decode(String str) {
		if (TextUtils.isEmpty(str)) {
			return "";
		}
		String result = null;
		try {
			result = URLDecoder.decode(str, DEFAULT_ENC);
		} catch (UnsupportedEncodingException e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}
		return result;
	}

}
