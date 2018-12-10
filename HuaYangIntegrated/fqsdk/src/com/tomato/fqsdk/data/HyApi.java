package com.tomato.fqsdk.data;

import java.io.IOException;
import java.net.Authenticator.RequestorType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.models.CLCommon;

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

	public static String SDKMAIN = "http://api-sdk.huayang.fun/v1/";
	public static String DATA_GAME = "http://api-tj.huayang.fun/game/";
	public static String DATA_SDK = "http://api-tj.huayang.fun/sdk.php";
	public static String TAG = "debug-okhttp";
	public static boolean isDebug = false;
//

	private OkHttpClient client;
	//   ʱʱ  
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

		//    ó ʱʱ  
		client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS).writeTimeout(TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(TIMEOUT, TimeUnit.SECONDS).build();

	}

	/**
	 * post    json    Ϊbody
	 * 
	 * @param url
	 * @param json
	 * @param callback
	 */
	public void postJson(final String url, String json, final HttpCallback callback) {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).addHeader("Cookie", s).build();
		FLogger.d("params:"+json);

		onStart(callback);
		client.newCall(request).enqueue(new Callback(
				) {

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
				FLogger.d("result:"+result);//"postUrl:"+url+
				if (response.isSuccessful()) {

					onSuccess(callback, result);

				} else {
					onError(callback, result);
				}
			}
		});
	}

	/**
	 * post     mapΪbody
	 * 
	 * @param url
	 * @param map
	 * @param callback
	 */
	public void post(String url, Map<String, Object> map, final HttpCallback callback) {

		// FormBody.Builder builder = new FormBody.Builder();
		// FormBody body=new FormBody.Builder().add("key", "value").build();

		/**
		 *         Ĳ   body
		 */
		FormBody.Builder builder = new FormBody.Builder();

		/**
		 *     key
		 */
		if (null != map) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {

				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
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
				arg1.printStackTrace();
				onError(callback, arg1.getMessage());
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
	 * get    
	 * 
	 * @param url
	 * @param callback
	 */
	public void get(String url, final HttpCallback callback) {

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
	 * log  Ϣ  ӡ
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

	private void onStart(HttpCallback callback) {
		if (null != callback) {
			callback.onStart();
		}
	}

	private void onSuccess(final HttpCallback callback, final String data) {

		debug(data);

		if (null != callback) {
			handler.post(new Runnable() {
				public void run() {
					//   Ҫ     ̵߳Ĳ     
					callback.onSuccess(data);
				}
			});
		}
	}

	private void onError(final HttpCallback callback, final String msg) {
		if (null != callback) {
			handler.post(new Runnable() {
				public void run() {
					//   Ҫ     ̵߳Ĳ     
					callback.onError(msg);
				}
			});
		}
	}

	/**
	 * http    ص 
	 * 
	 * @author Flyjun
	 *
	 */
	public static abstract class HttpCallback {
		//   ʼ
		public void onStart() {
		};

		//  ɹ  ص 
		public abstract void onSuccess(String data);

		// ʧ ܻص 
		public void onError(String msg) {
		};
	}

	public void CLSdkPost(String behavior, String data, final HttpCallback callback) {
		String url = SDKMAIN + behavior;
		postJson(url, data, callback);
	}

	public void CLSdkDataPost(String data, final HttpCallback callback) {
		String url = DATA_SDK;
		postJson(url, data, callback);
	}

	public void CLDataPost(String behavior, String data, final HttpCallback callback) {
		String url = DATA_GAME + behavior + ".php";
		postJson(url, data, callback);
	}

	public void CLMissDataPost(String missdatacode, String data, final HttpCallback callback) {
		String url = null;
		if (missdatacode.equals(CLCommon.CODE_SDK_DATA)) {
			url = CLCommon.SDK_DATA_URL;
		}
		if (missdatacode.equals(CLCommon.CODE_GAME_DATA)) {
			url = CLCommon.GAME_DATA_URL;
		}

		postJson(url, data, callback);

	}
	/**
	 *     url  ַ
	 * 
	 * @param params
	 * @return
	 */
	protected String getUrl(String url,HashMap<String, String> params) {
		//    url    
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
     *   json  ʽ   ַ   ת  Map    
     */
    private static HashMap<String, String> jsonToHashMap( JSONObject jsonObject)
    {
    	HashMap<String, String> map= new HashMap<String, String>();
        Iterator it = jsonObject.keys();
        //     jsonObject   ݣ   ӵ Map    
        while (it.hasNext())
        {
            String key = String.valueOf(it.next());
            //ע ⣺     ȡvalueʹ õ   optString
            // optString   getString     𣺵   ˵    optString   ڵò       Ҫ  ֵʱ 򷵻ؿ  ַ           getString   ׳  쳣  
            String value = (String) jsonObject.optString(key);
            map.put(key, value);
        }
        return map;
    }
}