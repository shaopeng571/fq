//package com.huayang.network;
//
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Set;
//import java.util.TreeMap;
//import java.util.Map.Entry;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//
//import android.content.Context;
//import cn.gowan.h5game.network.HyApi;
//import cn.gowan.h5game.network.HyApi.HttpCallback;
//import cn.gowan.h5game.util.CryptHelper;
//import cn.gowan.h5game.util.SpUtils;
//import cn.gowan.sdk.common.utils.FLogger;
//
//public class NetWork {
//	
//	private static String appid="0001";
//	private static String appkey="PM5CCB43EB7BG93795CBBD1F5FDB0FF1";
//	private static String channelid="001001";
//	public static void HJinstall(final Context context) {
//		final JSONObject params = new JSONObject();
//		try {
//			params.put("behavior", "install");
//			TreeMap<String, String> dict = GetMD5Params(context,"install");
//			JSONObject jsonObject = GetObjectParams(dict);
//			params.put("data", jsonObject.toString());
//			String sgin = CryptHelper.GetMD5Code(dict,
//					appkey);
//			params.put("sign", sgin);
//			params.put("appid", appid);
//			HyApi httpUtils=new HyApi();
//
////			AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
////			InputStream is = new ByteArrayInputStream(params.toString()
////					.getBytes());
//			HttpCallback callback=new HttpCallback(
//					) {
//				
//				@Override
//				public void onSuccess(String data) {
//					FLogger.d("设备安装成功");
//					SpUtils.setBooleanValue(context, "install", true);
//				}
//				@Override
//						public void onError(String msg) {
//							// TODO Auto-generated method stub
//							super.onError(msg);
//							SpUtils.setBooleanValue(context, "install", false);
//						}
//			};
//			httpUtils.CLSdkDataPost(params.toString(), callback);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void HJactivate(final Context context) {
//
//		final JSONObject jsonObject2 = new JSONObject();
//		try {
//			jsonObject2.put("behavior", "activate");
//
//			TreeMap<String, String> dict = GetMD5Params(context,"activate");
//			JSONObject jsonObject = GetObjectParams(dict);
//			jsonObject2.put("data", jsonObject.toString());
//			String sgin = CryptHelper.GetMD5Code(dict,
//					appkey);
//			jsonObject2.put("sign", sgin);
//			jsonObject2.put("appid", appid);
//			HyApi httpUtils=new HyApi();
//			HttpCallback callback=new HttpCallback(
//					) {
//				
//				@Override
//				public void onSuccess(String data) {
//					// TODO Auto-generated method stub
//					FLogger.d("设备激活成功");
//					Util.saveInitData(context, "activity");
//				}
//				@Override
//						public void onError(String msg) {
//							// TODO Auto-generated method stub
//							super.onError(msg);
//							Util.saveInitData(context, "noactivity");
//						}
//			};
//			httpUtils.CLSdkDataPost(jsonObject2.toString(), callback);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//	}
//	
//	public static TreeMap<String, String> GetMD5Params(Context context,String behavior) {
//		BaseInfo bi = new BaseInfo(context);
//
//		TreeMap<String, String> dict = new TreeMap<String, String>();
//
//		dict.put("package_id", bi.getPacketName());
//		dict.put("app_id",appid);
//		dict.put("behavior", behavior);
//		dict.put("channel_source", channelid);
//		dict.put("type", "android");
//		dict.put("equipment_code", bi.getDeviceId());
//		dict.put("equipment_name", bi.getDevice());
//		dict.put("equipment_api", bi.getResolution());
//		dict.put("equipment_os", bi.getBrand());
//		dict.put("idfa", bi.getImei());
//		dict.put("network", bi.getNetType());
//		dict.put("network_isp", bi.getOperators());
//		dict.put("ip", bi.getIp());
//		dict.put("time", GetTimeZ());
//		return dict;
//	}
//	public static JSONObject GetObjectParams(TreeMap<String, String> dict)
//			throws JSONException {
//		JSONObject params = new JSONObject();
//		Set<Entry<String, String>> set = dict.entrySet();
//		Iterator<Entry<String, String>> iterator = set.iterator();
//		while (iterator.hasNext()) {
//			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
//			params.put(entry.getKey() + "", entry.getValue() + "");
//		}
//		return params;
//	}
//	public static String GetTimeZ(Date date) {
//		if (date == null)
//			return null;
//
//		long time = date.getTime();
//		String timeline = time + "";
//		if (timeline.length() > 10) {
//			timeline = timeline.substring(0, 10);
//		}
//
//		return timeline;
//	}
//
//	public static String GetTimeZ() {
//		return GetTimeZ(new Date());
//	}
//}
