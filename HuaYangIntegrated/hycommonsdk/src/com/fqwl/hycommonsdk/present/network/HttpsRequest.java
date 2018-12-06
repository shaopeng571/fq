//package com.fqwl.hycommonsdk.present.network;
//
//import java.net.SocketException;
//import java.net.UnknownHostException;
//import java.security.KeyStore;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.HttpVersion;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ClientConnectionManager;
//import org.apache.http.conn.params.ConnManagerParams;
//import org.apache.http.conn.scheme.PlainSocketFactory;
//import org.apache.http.conn.scheme.Scheme;
//import org.apache.http.conn.scheme.SchemeRegistry;
//import org.apache.http.conn.ssl.SSLSocketFactory;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.params.BasicHttpParams;
//import org.apache.http.params.HttpConnectionParams;
//import org.apache.http.params.HttpParams;
//import org.apache.http.params.HttpProtocolParams;
//import org.apache.http.protocol.HTTP;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import com.fqwl.hycommonsdk.bean.ResultInfo;
//import com.fqwl.hycommonsdk.util.logutils.FLogger;
//import com.fqwl.hycommonsdk.util.logutils.Global;
//
//import android.text.TextUtils;
//import android.util.Log;
//
///**
// * https 请求
// * 
// * @author yzj
// *
// */
//@SuppressWarnings("deprecation")
//public class HttpsRequest {
//
//	public static ResultInfo postData(String url, HashMap<String, String> headerMaps,
//			HashMap<String, String> dataMaps) {
//		ResultInfo resultInfo = new ResultInfo();
//		try {
//			JSONObject jsonData = new JSONObject();
//			// 添加参数，将HashMap转换成json
//			if (dataMaps != null) {
//				Iterator<Entry<String, String>> bodyIt = dataMaps.entrySet().iterator();
//				while (bodyIt.hasNext()) {
//					Map.Entry entry = (Map.Entry) bodyIt.next();
//					String k = (String) entry.getKey();
//					String v = (String) entry.getValue();
//					try {
//						jsonData.put(k, v);
//
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//			FFLogger.d(Global.INNER_TAG, "参数加密前:" + jsonData.toString());
//			String time = System.currentTimeMillis() + "";
//			String key = EncoderUtil.encodeByMD5(time + Utils.ranNumber());
//			String md5Result = EncoderUtil.encodeByMD5(key);
//			String keyNew = Utils.getEvenStr(md5Result + md5Result);
//			String p = Encryption.encrypt(jsonData.toString(), keyNew);
//		//	FFLogger.d(Global.INNER_TAG, "参数加密后:" + p);
//
//			FLogger.d("url post =" + url);
//			FLogger.d("json=" + jsonData.toString());
//
//			List<NameValuePair> valueList = new ArrayList<NameValuePair>();
//			valueList.add(new BasicNameValuePair("p", p));
//			valueList.add(new BasicNameValuePair("ts", key));
//
//			StringBuffer param = new StringBuffer("");
//			param.append("&p=" + p);
//			param.append("&ts=" + key);
//			FLogger.d("url=" + url + param.toString());
//			FFLogger.d(Global.INNER_TAG, "完整访问url:" + url + param.toString());
//			String result = post(url, valueList);
//			FFLogger.i(Global.INNER_TAG, url + " 返回内容:" + result);
//			if (result != null) {
//				try {
//					JSONObject jsonResult = new JSONObject(result);
//					resultInfo.msg = jsonResult.getString("msg");
//					resultInfo.code = jsonResult.getInt("code");
//					if (jsonResult.has("data")) {
//						JSONObject object = new JSONObject(jsonResult.getString("data"));
//						resultInfo.time = object.getString("ts");
//						resultInfo.data = Utils.decodeResult(jsonResult.getString("data"));
//						FFLogger.i(Global.INNER_TAG, url + " 返回内容解密后:" + resultInfo.data);
//					}
//				} catch (JSONException e) {
//					
//					e.printStackTrace();
//					resultInfo.msg = "解析数据出错";
//					resultInfo.code = -1;
//				}
//				// FLogger.d("result=" + result);
//				FLogger.d("resultInfo=" + resultInfo.toString());
//			} else {
//		
//				resultInfo.msg = "网络出小差了，请开启或切换网络后重新打开游戏尝试";
//				resultInfo.code = -1;
//			}
//		} catch (Exception e) {
//
//			e.printStackTrace();
//			resultInfo.msg = "网络连接出现异常，请稍后再试";
//			resultInfo.code = -1;
//		}
//
//		return resultInfo;
//	}
//
//	public static String get(String url, String param) {
//		String result = null;
//		try {
//
//			String tempUrl = url;
//			if (!TextUtils.isEmpty(param)) {
//				if (url.contains("?")) {
//					tempUrl = url + "&" + param;
//				} else {
//					tempUrl = url + "?" + param;
//				}
//			}
//			FLogger.d("" + tempUrl);
//			HttpParams httpParameters = new BasicHttpParams();
//			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
//			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
//			HttpClient hc = getHttpClient(httpParameters);
//			HttpGet get = new HttpGet(tempUrl);
//			get.setParams(httpParameters);
//			HttpResponse response = null;
//			try {
//				response = hc.execute(get);
//			} catch (UnknownHostException e) {
//				throw new Exception("Unable to access " + e.getLocalizedMessage());
//			} catch (SocketException e) {
//				throw new Exception(e.getLocalizedMessage());
//			}
//			int sCode = response.getStatusLine().getStatusCode();
//
//			if (sCode == HttpStatus.SC_OK) {
//				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//				FLogger.d("result=" + result);
//			} else {
//				Log.e("commonsdk", "https post exception code:" + sCode);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	private static String post(String url, List<NameValuePair> valueList) {
//		String result = null;
//		try {
//
//			HttpParams httpParameters = new BasicHttpParams();
//			// 设置连接管理器的超时
//			ConnManagerParams.setTimeout(httpParameters, 10000);
//			// 设置连接超时
//			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
//			// 设置socket超时
//			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
//			HttpClient hc = getHttpClient(httpParameters);
//			HttpPost post = new HttpPost(url);
//			post.setEntity(new UrlEncodedFormEntity(valueList, HTTP.UTF_8));
//			post.setParams(httpParameters);
//			HttpResponse response = null;
//			try {
//				response = hc.execute(post);
//			} catch (UnknownHostException e) {
//				throw new Exception("Unable to access " + e.getLocalizedMessage());
//			} catch (SocketException e) {
//				throw new Exception(e.getLocalizedMessage());
//			}
//			int sCode = response.getStatusLine().getStatusCode();
//			if (sCode == HttpStatus.SC_OK) {
//				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//				FLogger.d("post url=" + url);
//				FLogger.d("result=" + result);
//			} else {
//				FFLogger.e(Global.INNER_TAG, "访问异常:" + sCode);
//				Log.e("commonsdk", "https post exception code:" + sCode);
//			}
//		} catch (Exception e) {
//			CommonReporter.getInstance().reportInfo(new ReportInfo(EReportCode.HTTP_ERROR1, "请求gowan服务器异常!", e,url));
//			FFLogger.Ex(Global.INNER_TAG, e);
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//
//	/**
//	 * 获取HttpClient
//	 * 
//	 * @param params
//	 * @return
//	 */
//	private static HttpClient getHttpClient(HttpParams params) {
//		try {
//			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
//			trustStore.load(null, null);
//
//			SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
//			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
//			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
//			HttpProtocolParams.setUseExpectContinue(params, true);
//
//			// 设置http https支持
//			SchemeRegistry registry = new SchemeRegistry();
//			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
//			registry.register(new Scheme("https", sf, 443));// SSL/TSL的认证过程，端口为443
//			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
//			return new DefaultHttpClient(ccm, params);
//		} catch (Exception e) {
//			return new DefaultHttpClient(params);
//		}
//	}
//
//	public static String postYYB(String url, String jsonString, String value) {
//		String result = null;
//		try {
//
//			HttpParams httpParameters = new BasicHttpParams();
//			// 设置连接管理器的超时
//			ConnManagerParams.setTimeout(httpParameters, 10000);
//			// 设置连接超时
//			HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
//			// 设置socket超时
//			HttpConnectionParams.setSoTimeout(httpParameters, 10000);
//			HttpClient hc = getHttpClient(httpParameters);
//			// HttpHost host=new HttpHost("gamelog.3g.qq.com");
//			HttpPost post = new HttpPost(url);
//			post.setHeader("Authorization", value);
//			StringEntity entity = new StringEntity(jsonString, HTTP.UTF_8);
//			post.setEntity(entity);
//			post.setParams(httpParameters);
//
//			HttpResponse response = null;
//			try {
//				response = hc.execute(post);
//			} catch (UnknownHostException e) {
//				throw new Exception("Unable to access " + e.getLocalizedMessage());
//			} catch (SocketException e) {
//				throw new Exception(e.getLocalizedMessage());
//			}
//			int sCode = response.getStatusLine().getStatusCode();
//			if (sCode == HttpStatus.SC_OK) {
//				result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
//				FLogger.d("post url=" + url);
//				FLogger.d("result=" + result);
//			} else {
//				FLogger.d("post url=" + post.getURI() + post.getAllHeaders() + post.getEntity());
//				FLogger.d("post url=" + post.getURI() + post.getParams());
//				Log.e("commonsdk", "https post exception code:" + sCode);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return result;
//	}
//	
//	public static String getUrl(String url, HashMap<String, String> headerMaps,
//			HashMap<String, String> dataMaps) {
//			JSONObject jsonData = new JSONObject();
//			// 添加参数，将HashMap转换成json
//			if (dataMaps != null) {
//				Iterator<Entry<String, String>> bodyIt = dataMaps.entrySet().iterator();
//				while (bodyIt.hasNext()) {
//					Map.Entry entry = (Map.Entry) bodyIt.next();
//					String k = (String) entry.getKey();
//					String v = (String) entry.getValue();
//					try {
//						jsonData.put(k, v);
//
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//
//			FFLogger.d(Global.INNER_TAG, "参数加密前:" + jsonData.toString());
//
//			 JSONObject ob = new JSONObject();
//	            String time = System.currentTimeMillis() / 1000 + "";
//	            String md5Result = EncoderUtil.encodeByMD5(time);
//	            String key = StrUtil.getEvenStr_H5(md5Result + md5Result);
//
//			String p = Encryption.encrypt(jsonData.toString(), key);
//		//	FFLogger.d(Global.INNER_TAG, "参数加密后:" + p);
//
//			FLogger.d("url post =" + url);
//			FLogger.d("json=" + jsonData.toString());
//
//			List<NameValuePair> valueList = new ArrayList<NameValuePair>();
//			valueList.add(new BasicNameValuePair("p", p));
//			valueList.add(new BasicNameValuePair("ts", time));
//			
//			StringBuffer param = new StringBuffer("");
//			param.append("&p=" + p);
//			param.append("&ts=" + time);
//			FLogger.d("url=" + url + param.toString());
//			FLogger.d(Global.INNER_TAG, "完整访问url:" + url + param.toString());
//			
//
//		return url + param.toString();
//	}
//}
