package com.fqwl.hycommonsdk.model;

public abstract interface CommonSdkCallBack {
	
	/**
	 * 初始化回调
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void initOnFinish(String paramObject, int paramInt);

	/**
	 * 登陆，切换账号回调
	 * @param paramObject
	 * @param paramInt
	 */
	public abstract void loginOnFinish(String paramObject, int paramInt);
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
