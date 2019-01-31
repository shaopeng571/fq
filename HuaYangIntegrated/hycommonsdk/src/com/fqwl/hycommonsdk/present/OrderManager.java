package com.fqwl.hycommonsdk.present;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.present.network.ApiClient;
import com.fqwl.hycommonsdk.util.DataEnCoderUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

/**
 * sdk 推送订单 1.添加单个订单 2.删除单个订单 3.获取单个订单 4.对外接口
 * 
 * @author yzj
 * 
 */
public class OrderManager {
	public static Activity mActivity;
	static Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
//			msg.obj
			final Bundle bundle=msg.getData();
			JSONObject jsonObject=(JSONObject) msg.obj;
			ApiClient.getInstance().orderNotice(mActivity,jsonObject, new CommonSDKHttpCallback() {
				
				@Override
				public void onResult(ResultInfo resultInfo, String msg) {
					// TODO Auto-generated method stub
					if (resultInfo.code == 0) {
						
						deleteOrder(mActivity,bundle.getString("chanleId") , bundle.getString("json"));
					}else {
						Message faildmsg=new Message();
						Bundle bundle=new Bundle();
						bundle.putString("chanleId",bundle.getString("chanleId"));
						bundle.putString("json",bundle.getString("json"));
						faildmsg.setData(bundle);
						faildmsg.what=10001;
						handler.sendMessageDelayed(faildmsg, 30000);
					}
				}
			});

		};
	};

	/**
	 * 发送订单到服务器
	 * 
	 * @param activity
	 * @param chanleId
	 * @param result
	 */
	public static void sendOrder(Activity activity, String chanleId, JSONObject result) {
		FLogger.d("保存订单信息");
		saveOrder(activity, chanleId, result);
		FLogger.d("发送订单信息");
		sendOrderHttp(activity, chanleId, result);
	}

	private static void sendOrderHttp(final Activity activity, final String chanleId, final JSONObject result) {

		boolean isSuccess = false;
//		HashMap<String, String> dataMap = null;
//
//		Iterator it = result.keys();
//		while (it.hasNext()) {
//			if (dataMap == null) {
//				dataMap = new HashMap<String, String>();
//			}
//			// 获得key
//			String key = (String) it.next();
//			try {
//				// 根据key获得value, value也可以是JSONObject,JSONArray,使用对应的参数接收即可
//				String value = result.getString(key);
//				System.out.println("key: " + key + ",value" + value);
//				dataMap.put(key, value);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//
//		}
		ApiClient.getInstance().orderNotice(mActivity, result, new CommonSDKHttpCallback() {
			
			@Override
			public void onResult(ResultInfo resultInfo, String data) {
				// TODO Auto-generated method stub
				if (resultInfo == null) {
					FLogger.e("commonsdk", "请求支付回调失败");
					return;
				}
				int count = 0;
				FLogger.d(resultInfo.toString());
				if (resultInfo.code != 0) {// 发送不成功，重复发送
//					try {
							Message msg=new Message();
							Bundle bundle=new Bundle();
							bundle.putString("chanleId", chanleId);
							bundle.putString("json", result.toString());
							msg.setData(bundle);
							msg.what=10001;
							handler.sendMessageDelayed(msg, 30000);
//							Thread.sleep(30 * 1000);

							FLogger.d("发送订单失败，重新发送，刷新");
							// 重新加密
//							ApiClient.getInstance(activity).putYSDKPaySign(result);

//							ResultInfo temp = ApiClient.getInstance().orderNotice(chanleId, dataMap);

//							if (temp.code == 0) {
//								isSuccess = true;
//								break a;
//							}
//							count++;
//						}
//						if (isSuccess) {
//							deleteOrder(activity, chanleId, result);
//						}
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
				} else {
					deleteOrder(activity, chanleId, result.toString());
				}
			}
		});
		

	
	}
	/**
	 * 登录后调用，检查是否有历史订单没发送完成，并执行发送
	 * 
	 * @param activity
	 * @param chanleId
	 * @param newJson  参数登录后，刷新的新值
	 */
	public static void checkHistoryOrder(Activity activity, String chanleId, JSONObject newJson) {
		SharedPreferences sharedPreferences = activity.getSharedPreferences("huayangcommon", Context.MODE_PRIVATE);
		String data = sharedPreferences.getString(chanleId, "");
		if (TextUtils.isEmpty(data)) {
			return;
		} else {
			try {
				JSONArray array = new JSONArray(DataEnCoderUtil.decode(data));
				FLogger.d("lenth" + array.length());
				if (array.length() < 1) {
					return;
				}

				for (int i = 0; i < array.length(); i++) {
					sendHistoryOrder(activity, chanleId, newJson, array.getJSONObject(i));
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void sendHistoryOrder(Activity activity, String chanleId, JSONObject newJson, JSONObject oldJson) {
		try {
			if (chanleId.equals("yaowanqq") || chanleId.equals("chmsdk") || chanleId.equals("qqgowan")) {
				oldJson.put("openkey", newJson.getString("openkey"));
				oldJson.put("pay_token", newJson.getString("pay_token"));
				oldJson.put("openid", newJson.getString("openid"));
				oldJson.put("pf", newJson.getString("pf"));
				oldJson.put("pfkey", newJson.getString("pfkey"));
				FLogger.d("腾讯处理旧订单，刷新xx");
//				ApiClient.getInstance(activity).putYSDKPaySign(oldJson);
				sendOrderHttp(activity, chanleId, oldJson);
			} else {
				FLogger.d("渠道 " + chanleId + " 执行补单程序");
				sendOrderHttp(activity, chanleId, oldJson);
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

	}

	

	/**
	 * 清除该笔订单
	 * 
	 * @param chanleId
	 * @param result
	 */
	private static void deleteOrder(Activity activity, String chanleId, String msg) {
		JSONObject result = null;
		try {
			result = new JSONObject(msg);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// TODO Auto-generated method stub
		FLogger.d("clearOrder");
		SharedPreferences sharedPreferences = activity.getSharedPreferences("huayangcommon", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String data = sharedPreferences.getString(chanleId, "");
		if (TextUtils.isEmpty(data)) {
			return;
		} else {
			try {
				JSONArray array = new JSONArray(DataEnCoderUtil.decode(data));
				if (array.length() < 1) {
					return;
				}
				List<JSONObject> list = new ArrayList<JSONObject>();
				for (int i = 0; i < array.length(); i++) {
					list.add(array.getJSONObject(i));
				}
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getString("order_id").equals(result.getString("order_id"))) {
						list.remove(i);
					}
				}
				if (list.size() < 1) {
					editor.putString(chanleId, "");
				} else {
					editor.putString(chanleId, DataEnCoderUtil.encode(list.toString()));
				}
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存订单，下次登录发送
	 * 
	 * @param result
	 */
	private static void saveOrder(Activity activity, String chanleId, JSONObject result) {
		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = activity.getSharedPreferences("huayangcommon", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		String data = sharedPreferences.getString(chanleId, "");
		if (TextUtils.isEmpty(data)) {
			JSONArray array = new JSONArray();
			array.put(result);
			editor.putString(chanleId, DataEnCoderUtil.encode(array.toString()));
			editor.commit();
		} else {
			try {
				JSONArray array = new JSONArray(DataEnCoderUtil.decode(data));
				array.put(result);
				editor.putString(chanleId, DataEnCoderUtil.encode(array.toString()));
				editor.commit();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
