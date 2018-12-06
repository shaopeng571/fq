package com.fqwl.hycommonsdk.present;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackChargeInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.present.apiinteface.IDataShare;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.present.network.ApiClient;
import com.fqwl.hycommonsdk.present.network.HyApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.CommonUtils;
import com.fqwl.hycommonsdk.util.ToastUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.Global;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


public class ResultNotify {

	/**
	 * 支付结果
	 * 
	 * @param callBack
	 * @param desc
	 *            0：成功， -2：失败
	 */
	public static void showPayResult(CommonSdkCallBack callBack, int statusCode) {
		CommonBackChargeInfo info = new CommonBackChargeInfo();
		if (statusCode == CommonBackChargeInfo.status_success) {
			info.statusCode = CommonBackChargeInfo.status_success;
			info.desc = CommonBackChargeInfo.success;
		} else {
			info.statusCode = CommonBackChargeInfo.status_error;
			info.desc = CommonBackChargeInfo.fail;
		}
		if (null != callBack) {
			callBack.chargeOnFinish(info.toString(), info.statusCode);
		}

	}

	/**
	 * 回调客户端登录成功
	 * 
	 * @param info
	 * @param callBack
	 */
	private static void noticeLoginSucess(CommonBackLoginInfo info, CommonSdkCallBack callBack) {
		FLogger.i(Global.INNER_TAG, "回调登录验证成功:" + info.toString());
		callBack.loginOnFinish(info.toString(), info.statusCode);

	}

	public static void ShowLoginFail(final Activity activity, CommonSdkCallBack callBack, int i) {
		CommonBackLoginInfo info = CommonBackLoginInfo.getInstance();
		info.userId = "";
		// info.sdkUserId = "";
		info.timestamp = System.currentTimeMillis() + "";
		info.statusCode = i;
//		info.guid = "";
		info.cp_ext = "";
//		info.new_sign = "";
		FLogger.d("ShowLoginFail=" + info.toString());
		FLogger.w(Global.INNER_TAG, "回调登录验证失败:" + info.toString());
		callBack.loginOnFinish(info.toString(), info.statusCode);

	}

	/**
	 * 将登录验证的数据整理好
	 * 
	 * @param activity
	 * @param uid
	 * @param username
	 * @param channel
	 * @param data
	 *            渠道登录验证需要的数据
	 * @param dataMap
	 */
//	public static void putUserLoginInfo(Activity activity, String uid, String username, String channel, JSONObject data,
//			HashMap<String, String> dataMap) {
//		if (TextUtils.isEmpty(username)) {
//			username = "";
//		}
//		CommonBackLoginInfo.getInstance().userId = uid;
//		CommonBackLoginInfo.getInstance().userName = username;
//		CommonBackLoginInfo.getInstance().statusCode = CommonBackLoginInfo.login_success;
//		CommonBackLoginInfo.getInstance().hasCheck = false;
//		if (data != null) {
//			try {
//				if (data.has("time")) {
//					CommonBackLoginInfo.getInstance().timestamp = data.opt("time").toString();
//					FLogger.d("data.gettime: " + data.get("time").toString());
//					FLogger.d(
//							"CommonBackLoginInfo.timestamp: " + CommonBackLoginInfo.getInstance().timestamp.toString());
//
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//
//		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().timestamp)) {
//
//			// 没有服务器时间
//			CommonBackLoginInfo.getInstance().timestamp = System.currentTimeMillis() + "";
//		}
//		CommonBackLoginInfo.getInstance().platformChanleId = ChannelConfigUtil
//				.getChannelNum(activity);
//
//		if (data != null) {
//			CommonBackLoginInfo.getInstance().setSessionData(data);
//			CommonBackLoginInfo.getInstance().hasCheck = true;
//
//			dataMap.put("data", data.toString());
//		} else {
//			dataMap.put("uid", uid);
//		}
//	}
	/**
	 * 用户登录验证接口
	 * 
	 * @param activity
	 * @param chanleId
	 * @param dataMap
	 * @param handler
	 * @param mBack
	 * @param mImpl
	 */
	public static void hyUserLoginVerify(final Activity activity, final JSONObject data, final Handler handler, final CommonSdkCallBack mBack,
			final SdkApi mImpl) {
		ApiClient.getInstance().userLoginVerify(activity, data, new CommonSDKHttpCallback() {
			
		@Override
		public void onResult(ResultInfo resultInfo, String msg) {
			//要在线程中才可以使用消息队列
//			Looper.prepare();
		
			if (resultInfo.code != 1 || TextUtils.isEmpty(resultInfo.data)) {
				FLogger.e("login err " + resultInfo.msg);
				ShowLoginFail(activity, mBack, -1);
				ToastUtil.toastInfo(activity, resultInfo.msg);
			} else {
				// 检验成功
				// result:{"code":"1","msg":"登录成功！","data":{"user_id":"4ff036a13254eafe","user_name":"4ff036a13254eafe","token":"selfServer1539159406172","la":""}}
				String ext = null;
				try {
					JSONObject jsonObject = new JSONObject(resultInfo.data);
					FLogger.d(jsonObject.toString());
					// user_id
					CommonBackLoginInfo.getInstance().statusCode = 0;
					CommonBackLoginInfo.getInstance().userId = jsonObject.getString("user_id");
					CommonBackLoginInfo.getInstance().userName = jsonObject.getString("user_name");
					CommonBackLoginInfo.getInstance().channelToken = jsonObject.getString("token");
					CommonBackLoginInfo.getInstance().timestamp= CommonUtils.GetTimeZ();
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
				CommonBackLoginInfo.getInstance().platformChanleId = HySDKManager.getInstance()
						.getChanleId(activity);
				// 通知回调cp登录
				noticeLoginSucess(CommonBackLoginInfo.getInstance(), mBack);
				// 处理渠道失败的订单
				JSONObject refreshDataJson = null;
				if (mImpl instanceof IDataShare) {
					// 获取登录后，刷新的值
					refreshDataJson = ((IDataShare) mImpl).getDataJson(activity, null);
				}
				// 检查是否有历史订单没发送完成，并执行发送
				OrderManager.checkHistoryOrder(activity, mImpl.getChannelID(), refreshDataJson);
		}
//			Looper.loop();
		}
	});
	}
	/**
	 * 用户登录验证接口
	 * 
	 * @param activity
	 * @param chanleId
	 * @param dataMap
	 * @param handler
	 * @param mBack
	 * @param mImpl
	 */
	public static void userLoginVerify(final Activity activity, final String chanleId,
			final HashMap<String, String> dataMap, final Handler handler, final CommonSdkCallBack mBack,
			final SdkApi mImpl) {
		ApiClient.getInstance().userLoginVerify(activity, null, new CommonSDKHttpCallback() {
			
		@Override
		public void onResult(ResultInfo resultInfo, String msg) {
			Looper.prepare();
		
			if (resultInfo.code != 0 || TextUtils.isEmpty(resultInfo.data)) {
				FLogger.e("login err " + resultInfo.msg);
				ShowLoginFail(activity, mBack, -1);
				ToastUtil.toastInfo(activity, resultInfo.msg);
			} else {
				// 检验成功
				// {"timestamp":1500538955,"sign":"08c478650faef27d4423ad4c9896ac1c","user_id":"1500538608.5","sdk_user_id":"2001470264","ext":[],"v":2}
				String ext = null;
				try {
					JSONObject jsonObject = new JSONObject(resultInfo.data);
					FLogger.d(jsonObject.toString());
					// user_id
					CommonBackLoginInfo.getInstance().userId = jsonObject.getString("user_id");
//					CommonBackLoginInfo.getInstance().guid = jsonObject.getString("guid");
					CommonBackLoginInfo.getInstance().cp_ext = jsonObject.getString("cp_ext");
//					CommonBackLoginInfo.getInstance().new_sign = jsonObject.getString("new_sign");
					CommonBackLoginInfo.getInstance().timestamp = jsonObject.getString("timestamp");
					
					
					if (jsonObject.has("ext")) {
						if (!jsonObject.getString("ext").equals("[]")) {
							ext = jsonObject.getString("ext");
						}
					}
					
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// 通知回调cp登录
				noticeLoginSucess(CommonBackLoginInfo.getInstance(), mBack);
				
		}
			Looper.loop();
		}
	});
	}

//	/**
//	 * 回调客户端登录成功
//	 * 
//	 * @param info
//	 * @param callBack
//	 */
//	private static void samsungNoticeLoginSucess(CommonBackLoginInfo info, SamSungCommonSdkCallBack callBack) {
//		Logger.d(info.toString());
//		callBack.initOnFinish(info.toString(), info.statusCode);
//	}
//
//	public static void samsungUserLoginVerify(final Activity activity, final String chanleId,
//			final HashMap<String, String> dataMap, final Handler handler, final SamSungCommonSdkCallBack mBack,
//			final CommonInterface mImpl) {
//
//		ThreadManager.getInstance().execute(new Runnable() {
//			@Override
//			public void run() {
//				Looper.prepare();
//				ResultInfo resultInfo = ApiClient.getInstance(activity)
//						.samsungGetUserId("http://yisdk-api.gowan8.com/?ct=user&ac=verifysign", dataMap);
//				if (resultInfo.code != 0 || TextUtils.isEmpty(resultInfo.data)) {
//					Log.e("commonsdk", "login err " + resultInfo.msg);
//				} else {
//					// 检验成功
//					// {"timestamp":1500538955,"sign":"08c478650faef27d4423ad4c9896ac1c","user_id":"1500538608.5","sdk_user_id":"2001470264","ext":[],"v":2}
//					String ext = null;
//					try {
//						JSONObject jsonObject = new JSONObject(resultInfo.data);
//						// user_id
//						CommonBackLoginInfo.getInstance().userId = jsonObject.getString("uid");
//
//					} catch (JSONException e) {
//						e.printStackTrace();
//					}
//
//					// 通知回调cp登录
//					samsungNoticeLoginSucess(CommonBackLoginInfo.getInstance(), mBack);
//
//				}
//				Looper.loop();
//			}
//		});
//	}
}
