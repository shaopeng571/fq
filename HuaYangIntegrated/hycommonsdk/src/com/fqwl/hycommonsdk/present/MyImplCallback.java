package com.fqwl.hycommonsdk.present;

import java.util.HashMap;

import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.present.network.ApiClient;
import com.fqwl.hycommonsdk.util.logutils.FLogger;

import android.app.Activity;
import android.os.Handler;

/**
 * 实现类回调，所有渠道实现类，操作完成都会回调到这统一处理
 * 
 * @author yzj
 * 
 */
public class MyImplCallback implements ImplCallback {

	Activity mActivity;
	CommonSdkCallBack mBack;
	SdkApi mImpl;

	public MyImplCallback(Activity mActivity, CommonSdkCallBack mBack,
			SdkApi mImpl) {
		this.mActivity = mActivity;
		this.mImpl = mImpl;
		this.mBack = mBack;
	}

	@Override
	public void onLoginVerify(JSONObject data) {
		// TODO Auto-generated method stub
		ResultNotify.hyUserLoginVerify(mActivity, data, null, mBack, mImpl);
	}
	@Override
	public void onLoginSuccess(String uid, String username, JSONObject data,
			String expand, Handler handler) {
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		ResultNotify.putUserLoginInfo(mActivity, uid, username, mImpl.getChannelID(), data, dataMap);
//		
//		// 默认，请求接口校验用户信息，再回调登录
//		if (expand == null) {
//			CommonBackLoginInfo.getInstance().isChangeUser = false;
//			ResultNotify.userLoginVerify(mActivity, mImpl.getChannelID(), dataMap, handler, mBack, mImpl);
//			return;
//		}
//		// 1.特殊登录，浮标切换账号登录
//		if (expand.equals("1")) {
//			CommonBackLoginInfo.getInstance().isChangeUser = true;
//			ResultNotify.userLoginVerify(mActivity, mImpl.getChannelID(), dataMap, handler, mBack, mImpl);
//		}

	}
//	@Override
//	public void samsungOnLoginSuccess(String appid, String signValue, JSONObject data,
//			String expand, Handler handler,SamSungCommonSdkCallBack mBack) {
//		Logger.d("实现类回调发送登录");
//		
//		HashMap<String, String> dataMap = new HashMap<String, String>();
//		dataMap.put("appid", appid);
//		dataMap.put("gameAuthSign", signValue);
//		
////		dataMap.put("data", data.toString());
//		ResultNotify.putUserLoginInfo(mActivity, appid, signValue, mImpl.getChannelID(), data, dataMap);
//		
//		// 默认，请求接口校验用户信息，再回调登录
//		if (expand == null) {
//			CommonBackLoginInfo.getInstance().isChangeUser = false;
//			ResultNotify.samsungUserLoginVerify(mActivity, mImpl.getChannelID(), dataMap, handler, mBack, mImpl);
//			return;
//		}
//
//
//	}
	@Override
	public void onLoginFail(int code) {
		ResultNotify.ShowLoginFail(mActivity, mBack, code);
	}

	@Override
	public ResultInfo getOrderId(JSONObject channelJson,
			CommonSdkChargeInfo info) {
//		ResultInfo resultInfo = ApiClient.getInstance(mActivity).orderCreate(info, channelJson);
		return null;
	}

	@Override
	public void onPayFinish(int statusCode) {
		ResultNotify.showPayResult(mBack, statusCode);
	}


	@Override
	public void onInit(String chandID, HashMap<String, String> map) {
		
	}

	@Override
	public void noticeOrder(final JSONObject jsonObject) {
		if (jsonObject == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
//				Logger.d("notice order to service...");

				OrderManager.sendOrder(mActivity, mImpl.getChannelID(), jsonObject);
			}
		}).start();
	}

	@Override
	public ResultInfo getPayMethod(String channel, HashMap<String, String> dataMap) {
		ResultInfo resultInfo = null;
//		resultInfo = ApiClient.getInstance(mActivity).getPayMethod(dataMap);
		return resultInfo;
	}

	/**
	 * 获取支付加密签名，目前只有腾讯ysdk使用
	 */
	@Override
	public void getPaySign(JSONObject data,CommonSDKHttpCallback callback) {
		ApiClient.getInstance().getPaySign(mActivity, data, callback);
	}

	/**
	 * 刷新登录token，目前只有ysdk使用
	 */
	@Override
	public void refreshToken(HashMap<String, String> data) {
//		ApiClient.getInstance(mActivity).refreshToken(data);
	}

}
