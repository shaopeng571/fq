package com.tomato.fqsdk.control;

import com.tomato.fqsdk.models.HyLoginResult;

public abstract interface FQSdkCallBack {
	
	/**
	 * 初始化回调
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void initOnFinish(int resultCode,String paramObject);

	/**
	 * 登陆，切换账号回调
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void loginOnFinish(int ret,HyLoginResult hjLoginResult);
	/**
	 * 充值回调
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void chargeOnFinish(String paramObject, int paramInt);

	/** * 退出页面回调
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void exitViewOnFinish(String paramObject, int paramInt);
	/**
	 * 获取未成年信息
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void getAdultOnFinish(String paramObject, int paramInt);
	/**
	 *用户切换用户，修改密码监听
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void ReloginOnFinish(String paramObject, int paramInt);
	
	public abstract void logoutOnFinish(String paramObject, int paramInt);
	
	public abstract void realNameOnFinish(int age);

}
