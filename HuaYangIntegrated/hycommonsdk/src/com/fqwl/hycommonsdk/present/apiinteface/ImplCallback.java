package com.fqwl.hycommonsdk.present.apiinteface;

import java.util.HashMap;

import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;

import android.os.Handler;

/**
 * 实现类回调
 * @author yzj
 *
 */
public interface ImplCallback {

	/**
	 * 登录校验
	 */
	void onLoginVerify(JSONObject data);
	/**
	 * 登录
	 * @param uid 
	 * @param username
	 * @param data 用户数据验证参数json
	 * @param mBack
	 * @param expand 拓展参数
	 * @param handler
	 */
	void onLoginSuccess(String uid, String username, JSONObject data, String expand, Handler handler);
	
	void onLoginFail(int code);
	
	void onPayFinish(int statusCode);
	
//	void samsungOnLoginSuccess(String uid, String username, JSONObject data, String expand, Handler handler,SamSungCommonSdkCallBack mBack);
	/**
	 * 获取订单回调
	 * @param channelJson channelJson 额外要传的参数，特殊渠道要传进来，添加一起发送，没有就传Null
	 * @param info
	 * @return String 返回请求的数据
	 */
	ResultInfo getOrderId(JSONObject channelJson,
			CommonSdkChargeInfo info);
	
	
	/**
	 * 获取充值方式
	 * 注意现在这个接口只用于混合渠道如: 腾讯3k 、应用宝3k、混合苹果3k
	 * @param channel
	 * @param dataMap
	 * @return
	 */
	ResultInfo getPayMethod(String channel, HashMap<String, String> dataMap);
	
	/**
	 * 特殊渠道，初始化会执行请求网络接口
	 * @param chandID
	 * @param map
	 */
	void onInit(String chandID, HashMap<String, String> map);
	
	/**
	 * 特殊渠道，支付完成后，订单通知到我们服务器
	 * @param jsonObject
	 */
	void noticeOrder(JSONObject jsonObject);
	
	/**
	 * 获取支付加密签名，目前只有ysdk使用
	 * @param jsonObject
	 * @return
	 */
	void getPaySign(JSONObject str,CommonSDKHttpCallback callback);
	
	/**
	 * 刷新登录token，目前只有ysdk使用
	 * @param data
	 */
	void refreshToken(HashMap<String, String> data);
}
