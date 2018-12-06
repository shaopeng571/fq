package com.fqwl.hycommonsdk.present.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.util.ToastUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.Global;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

/**
 * okhttp
 * 
 * @author Flyjun
 *
 */
public class HyApi {

	public static String SDKMAIN = "http://api-sdk.huayang.fun/v1/channel/";
	public static String DATA_GAME = "http://api-tj.huayang.fun/game/";
	public static String DATA_SDK = "http://api-tj.huayang.fun/sdk.php";
	public static String TAG = "debug-okhttp";
	public static boolean isDebug = false;
//

	private OkHttpClient client;
	// ʱʱ
	public static final int TIMEOUT = 1000 * 60;

	// json
	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private Handler handler = new Handler(Looper.getMainLooper());

	private static String s = "";

	public HyApi() {
		// TODO Auto-generated constructor stub
		this.init();
	}

	private void init() {

		client = new OkHttpClient();

		// ó ʱʱ
		client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(TIMEOUT, TimeUnit.SECONDS).build();

	}

	/**
	 * post json Ϊbody
	 * 
	 * @param url
	 * @param json
	 * @param callback
	 */
	public void postJson(final String url, String json, final CommonSDKHttpCallback callback) {
		RequestBody body = RequestBody.create(JSON, json);
		
		Request request = new Request.Builder().url(url).post(body).addHeader("Cookie", s).build();
		FLogger.d("url"+url+"params:"+json);

		onStart(callback);
		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub

				onError(callback, arg1.getMessage());
				arg1.printStackTrace();
				FLogger.e(arg1.getMessage());
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO Auto-generated method stub

				try {
					String string = response.header("Set-Cookie");
					s = string.substring(0, string.indexOf(";"));
				} catch (Exception e) {
					// TODO: handle exception

				}
				String result = response.body().string();
//				String result="";
		
				// 是否有响应
				if (response.isSuccessful()) {
					FLogger.e(Global.INNER_TAG, "success");
					
					onResult(callback, result.toString(),true);

				} else {
					FLogger.e(Global.INNER_TAG, "failed");
					onResult(callback, result.toString(),false);
				}

			}
		});
	}

	/**
	 * post mapΪbody
	 * 
	 * @param url
	 * @param map
	 * @param callback
	 */
	public void post(String url, Map<String, Object> map, final CommonSDKHttpCallback callback) {

		// FormBody.Builder builder = new FormBody.Builder();
		// FormBody body=new FormBody.Builder().add("key", "value").build();

		/**
		 * Ĳ body
		 */
		FormBody.Builder builder = new FormBody.Builder();

		/**
		 * key
		 */
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {

				FLogger.d("Key = " + entry.getKey() + ", Value = " + entry.getValue());
				builder.add(entry.getKey(), entry.getValue().toString());

			}
		}

		RequestBody body = builder.build();

		Request request = new Request.Builder().url(url).post(body).build();

		onStart(callback);

		client.newCall(request).enqueue(new Callback() {
			@Override
			public void onFailure(Call arg0, IOException arg1) {
				// TODO Auto-generated method stub
				FLogger.e(arg1.getMessage());
				arg1.printStackTrace();
				onError(callback, arg1.getMessage());
			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO Auto-generated method stub
				String resultmsg = response.body().string();
				String result="";
//				try {
//					result = URLDecoder.decode(resultmsg, "UTF-8");
//				} catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					FLogger.e(Global.INNER_TAG, "解析数据出错");
//				}
				// 是否有响应
				if (response.isSuccessful()) {
					FLogger.e(Global.INNER_TAG, "success");
					
					onResult(callback, result.toString(),true);

				} else {
					FLogger.e(Global.INNER_TAG, "failed");
					onResult(callback, result.toString(),false);
				}
			}
		});

	}

	/**
	 * get
	 * 
	 * @param url
	 * @param callback
	 */
	public void get(String url, final CommonSDKHttpCallback callback) {

		Request request = new Request.Builder().url(url).build();

		onStart(callback);

		client.newCall(request).enqueue(new Callback() {

			@Override
			public void onFailure(Call arg0, IOException arg1) {
				onError(callback, arg1.getMessage());
				arg1.printStackTrace();

			}

			@Override
			public void onResponse(Call arg0, Response response) throws IOException {
				// TODO Auto-generated method stub
				if (response.isSuccessful()) {
					onSuccess(callback, response.body().string());
				} else {
					onError(callback, response.message());
				}
			}
		});

	}

	/**
	 * log Ϣ ӡ
	 * 
	 * @param params
	 */
	public void debug(String params) {
		if (false == isDebug) {
			return;
		}

		if (null == params) {
			Log.d(TAG, "params is null");
		} else {
			Log.d(TAG, params);
		}
	}

	private void onStart(CommonSDKHttpCallback callback) {
		if (null != callback) {
//			callback.onStart();
		}
	}

	private void onResult(final CommonSDKHttpCallback callback, final String result,final boolean isSuccessful) {
		
		if (null != callback) {
			handler.post(new Runnable() {
				public void run() {
					ResultInfo resultInfo = new ResultInfo();
					// 是否有响应
					if (isSuccessful) {
							FLogger.d(result);
							// 数据是否为空
							if (!TextUtils.isEmpty(result)) {
								// TODO Auto-generated method stub
								JSONObject jsonObject = null;
								String code = null ;
								String data = null ;
								String msg = null;
								try {
//									JSONParser parser_obj = new JSONParser();
//									JSONArray array_obj = (JSONArray) parser_obj.parse(result); 
//									String string=array_obj.toJSONString();
									jsonObject=new JSONObject(result);
									
								} catch (JSONException e) {
									FLogger.e(Global.INNER_TAG, "解析数据出错");
									e.printStackTrace();
									resultInfo.msg = "解析数据出错";
									resultInfo.code = -1;
								} 
								try {
									code= jsonObject.get("code")+"";
								} catch (JSONException e1) {
									FLogger.e(Global.INNER_TAG, "解析数据出错");
									resultInfo.msg = "解析数据出错";
									resultInfo.code = -1;
								}
								try {
								data = jsonObject.getString("data");
							} catch (JSONException e) {
								FLogger.e(Global.INNER_TAG, "解析数据出错");
								e.printStackTrace();
								resultInfo.msg = "解析数据出错";
								resultInfo.code = -1;
							}
								try {
								msg = jsonObject.getString("msg");
								} catch (JSONException e) {
									FLogger.e(Global.INNER_TAG, "解析数据出错");
									e.printStackTrace();
									resultInfo.msg = "解析数据出错";
									resultInfo.code = -1;
								}
								resultInfo.code=Integer.valueOf(code);
								resultInfo.data=data;
								resultInfo.msg=msg;
							} else {
								FLogger.e(Global.INNER_TAG, "请求接口出错");
								resultInfo.msg = "网络出小差了，请开启或切换网络后重新打开游戏尝试";
								resultInfo.code = -1;
							}
						
						

					} else {

						FLogger.e(Global.INNER_TAG, "请求接口出现异常");
						resultInfo.msg = "网络连接出现异常，请稍后再试";
						resultInfo.code = -1;
					}

					callback.onResult(resultInfo, result);
				}
			});
		}

		
	}
	public static final String removeBOM(String data) {

		if (TextUtils.isEmpty(data)) {

		return data;

		}

		 

		if (data.startsWith("\ufeff")) {

		return data.substring(1);

		} else {

		return data;

		}

		}
	private void onSuccess(final CommonSDKHttpCallback callback, final String data) {

		debug(data);

		if (null != callback) {
			handler.post(new Runnable() {
				public void run() {
					// Ҫ ̵߳Ĳ
//					callback.onSuccess(data);
				}
			});
		}
	}

	private void onError(final CommonSDKHttpCallback callback, final String msg) {
		if (null != callback) {
			handler.post(new Runnable() {
				public void run() {
					// Ҫ ̵߳Ĳ

//					callback.onError(msg);
				}
			});
		}
	}

	/**
	 * http ص
	 * 
	 * @author Flyjun
	 *
	 */
//	public static abstract class CommonSDKHttpCallback {
//		// ʼ
//		public void onStart() {
//		};
//
//		// ɹ ص
//		public void onSuccess(String data) {};
//
//		// ʧ ܻص
//		public void onError(String msg) {
//		};
//
//		public void onResult(ResultInfo resultInfo, String msg) {
//		};
//	}

	public void HyCommonSdkPost(String behavior, String data,final CommonSDKHttpCallback httpCallback) {
		String url = SDKMAIN + behavior;
		postJson(url, data, httpCallback);
	}
	public void hyPayNotify(String behavior, String map,final CommonSDKHttpCallback httpCallback) {
		String url = SDKMAIN + behavior;
		
		FLogger.e(HttpRequest.post(url, getMap(map)));
	}
	
	public static Map<String, String> getMap(String jsonString)

    {
        JSONObject jsonObject;
        try
        {
            jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")
        Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, String> valueMap = new HashMap<String, String>();
            while (keyIter.hasNext())
            {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key,value.toString());
            }
            return valueMap;
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        return null;

    }


	public void CLSdkDataPost(String data, final CommonSDKHttpCallback callback) {
		String url = DATA_SDK;
		postJson(url, data, callback);
	}

	public void CLDataPost(String behavior, String data, final CommonSDKHttpCallback callback) {
		String url = DATA_GAME + behavior + ".php";
		postJson(url, data, callback);
	}

//	public void CLMissDataPost(String missdatacode, String data, final CommonSDKHttpCallback callback) {
//		String url = null;
//		if (missdatacode.equals(CLCommon.CODE_SDK_DATA)) {
//			url = CLCommon.SDK_DATA_URL;
//		}
//		if (missdatacode.equals(CLCommon.CODE_GAME_DATA)) {
//			url = CLCommon.GAME_DATA_URL;
//		}
//
//		postJson(url, data, callback);
//
//	}
	/**
	 * url ַ
	 * 
	 * @param params
	 * @return
	 */
	protected String getUrl(String url, HashMap<String, String> params) {
		// url
		if (params != null) {
			Iterator<String> it = params.keySet().iterator();
			StringBuffer sb = null;
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				if (TextUtils.isEmpty(value)) {
					value = "";
				}
				if (sb == null) {
					sb = new StringBuffer();
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(key);
				sb.append("=");
				sb.append(value);
			}
			url += sb.toString();
		}
		return url;
	}

	/**
	 * json ʽ ַ ת Map
	 */
	private static HashMap<String, String> jsonToHashMap(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		Iterator it = jsonObject.keys();
		// jsonObject ݣ ӵ Map
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			// ע ⣺ ȡvalueʹ õ optString
			// optString getString 𣺵 ˵ optString ڵò Ҫ ֵʱ 򷵻ؿ ַ getString ׳ 쳣
			String value = (String) jsonObject.optString(key);
			map.put(key, value);
		}
		return map;
	}
}