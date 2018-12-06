package com.fqwl.hycommonsdk.present.network;

import java.util.Map;

import com.fqwl.hycommonsdk.util.http.GowanHttp;
import com.fqwl.hycommonsdk.util.http.HttpCtx;
import com.fqwl.hycommonsdk.util.http.HttpMode;
import com.fqwl.hycommonsdk.util.logutils.FLogger;

public class HttpRequest {

	/**
	 * get 方式访问服务器,直接返回服务器内容
	 * 
	 * @param url
	 *            不带？的url
	 * @param params
	 *            a=1&b=2
	 * @return
	 */
	public static String get(String url, String params) {
		String result = "";
		try {
			GowanHttp http = new GowanHttp(url, params);
			HttpCtx mHttpCtx = new HttpCtx();
			mHttpCtx.setHttpMode(HttpMode.Get);
			mHttpCtx.setReadTimeout(15000);
			mHttpCtx.setTimeout(15000);
			FLogger.d("fq", "准备访问:" + url +"?"+ params);
			http.access(mHttpCtx);
			if (http.isSucceed()) {
				result = http.getHtml();
				FLogger.d("fq", "访问成功  返回内容:" + result);
			} else {
				FLogger.d("fq", "访问失败" + http.getHttpStatus().getCode());
			}
		} catch (Exception e) {
			FLogger.Ex("fq", e);
		}
		return result;
	}

	/**
	 * Get方式访问网络,直接返回服务器内容
	 * 
	 * @param url
	 * @param params
	 * @return 传入的class类型的实例
	 */
	public static String get(String url, Map<String, String> params) {
		return get(url, prepareurl(params));
	}

	/**
	 * Get方式访问网络,返回内容转成class的实例
	 * 
	 * @param url
	 * @param params
	 * @param clazz
	 * @return
	 */
//	public static <T> T get(String url, Map<String, String> params, Class<T> clazz) {
//		return GsonUtil.GsonToBean(get(url, params), clazz);
//	}

	/**
	 * Post 方式访问服务器,直接返回服务器内容
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params) {
		return post(url, prepareurl(params),null);
	}
	/**
	 * Post 方式访问服务器,直接返回服务器内容
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String post(String url, Map<String, String> params,Map<String,String> headers) {
		return post(url, prepareurl(params),headers);
	}



	/**
	 * Post 方式访问服务器,返回内容转成class的实例
	 * 
	 * @param url
	 * @param params
	 * @return 传入的class类型的实例
	 */
//	public static <T> T post(String url, Map<String, String> params, Class<T> clazz) {
//		return GsonUtil.GsonToBean(post(url, params), clazz);
//	}

	/**
	 * Post 方式访问服务器,直接返回服务器内容
	 * 
	 * @param url
	 *            不带？的url
	 * @param params
	 *            a=1&b=2
	 * @return
	 */
	public static String post(String url, String params,Map<String,String> headers) {
		
		String result = "";
		try {
			GowanHttp fHttp = new GowanHttp(url, params);
			HttpCtx mHttpCtx = new HttpCtx();
			mHttpCtx.setHttpMode(HttpMode.Post);
			mHttpCtx.setTimeout(15000);
			mHttpCtx.setReadTimeout(15000);
			fHttp.addHeader("Content-Type", "application/x-www-form-urlencoded");
			if(headers!=null) {
				for (Map.Entry<String, String> entry:headers.entrySet()) {
					fHttp.addHeader(entry.getKey(), entry.getValue());
				}
			}
			FLogger.d("fq", "准备访问:\n" + url +"&"+ params);
			fHttp.access(mHttpCtx);
			if (fHttp.isSucceed()) {
				result = fHttp.getHtml();
				FLogger.d("fq", url+" 返回内容:\n" + result);
			} else {
				FLogger.d("fq", url+" 访问失败 " + fHttp.getHttpStatus().getCode());
				
				
			}
		} catch (Exception e) {

			FLogger.Ex("fq", e);
		}
		return result;
	}

	/**
	 * Post 方式使用okhttp访问服务器,直接返回服务器内容
	 *
	 * @param url
	 *            不带？的url
	 * @param params
	 *            a=1&b=2
	 * @return
	 */
	public static  String postByOkhttp(String url,String params){
		String result = "";
		try {
			//TODO 待实现
		}catch (Exception e){

		}
		return result;
	}
	
	/**
	 * @param params
	 * @return
	 */
	private static String prepareurl( Map<String, String> params) {
		if (params != null) {
			StringBuffer paramstringBuffer = new StringBuffer();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				paramstringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
			}
			paramstringBuffer.deleteCharAt(paramstringBuffer.length() - 1);
			return  paramstringBuffer.toString();
		}
		return null;
	}
}
