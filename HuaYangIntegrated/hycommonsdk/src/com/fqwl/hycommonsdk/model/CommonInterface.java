//package com.fqwl.hycommonsdk.model;
//
//import com.fqwl.hycommonsdk.present.ImplCallback;
//
//import android.app.Activity;
//
//public abstract interface CommonInterface {
//	/**
//	 * 初始化
//	 * @param context
//	 * @param info
//	 * @param back
//	 */
//	public abstract void init(Activity activity,CommonSdkInitInfo info,CommonSdkCallBack back, ImplCallback implCallback);
//	/**
//	 * 登陆
//	 * @param context
//	 * @param commonSdkLoginInfo
//	 * @param callBack
//	 */
//	public abstract void login(Activity activity,CommonSdkLoginInfo commonSdkLoginInfo);
//	/**
//	 * 充值
//	 * @param context
//	 * @param ChargeInfo
//	 * @param callBack
//	 */
//	public abstract void charge(Activity activity,
//			CommonSdkChargeInfo ChargeInfo);
//	/**
//	 * sdk 退出界面
//	 * 
//	 * @param context
//	 * @param callBack
//	 */
//	public abstract boolean showExitView(Activity activity);
//	/**
//	 * 获取防沉迷信息
//	 * @param context
//	 * @param back
//	 */
//	public abstract boolean getAdult(Activity activity);
//	/**
//	 * 设置调试模式
//	 * @param b
//	 */
//	public abstract void setDebug(boolean b);
//	/**
//	 * 切换账号
//	 * @param activity
//	 * @param commonSdkLoginInfo
//	 * @param callBack
//	 */
//	public abstract void reLogin(Activity activity,CommonSdkLoginInfo commonSdkLoginInfo);
//	public abstract boolean showPersonView(Activity activity);
//	/**
//	 * 显示浮动图标
//	 * @param context
//	 * @param isShow
//	 */
//	public abstract void controlFlow(Activity context,boolean isShow);
////	/**
////	 * 设置切换账号监听
////	 * @param back
////	 */
////	public abstract void setReloginLisentener();
////	/**
////	 * 退出监听
////	 * @param back
////	 */
////	public abstract void setLogoutListener(CommonSdkCallBack back);
//	/**
//	 * 释放sdk资源
//	 * @param activity
//	 */
//	public abstract void DoRelease(Activity activity);
//
//	/**
//	 * 发送充值扩展参数
//	 * @param activity
//	 * @param data
//	 */
//	public abstract void submitExtendData(Activity activity,CommonSdkExtendData data);
////	/**
////	 * 发送金币消耗数
////	 * @param golds
////	 */
////	public abstract void sendGolds(int golds);
////	/**
////	 * 注销登录
////	 * @param activity
////	 * @param callBack
////	 */
////	public abstract boolean logout(Activity activity);
//	public abstract ResultInfo getOderId(CommonSdkChargeInfo info,Activity context);
//	
//	/**
//	 * 获取userid【登录验证接口返回】 传给融合接口和游戏那边,返回给cp的uid（经过加密）
//	 * @return
//	 */
//	public abstract String getUserId();
//	/**
//	 * 渠道版本，如360 的2.0
//	 * @return
//	 */
//	public abstract String getVersionName();
//	/**
//	 * 渠道名，如360 UC
//	 * @return
//	 */
//	public abstract String getChannelID();
//	public abstract boolean hasExitView();
//	
//	
//}
