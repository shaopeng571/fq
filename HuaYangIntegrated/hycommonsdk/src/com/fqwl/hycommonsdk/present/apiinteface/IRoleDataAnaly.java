package com.fqwl.hycommonsdk.present.apiinteface;

import com.fqwl.hycommonsdk.model.CommonSdkExtendData;

import android.app.Activity;

/**
 * 角色数据统计接口
 * @author yzj
 *
 */
public interface IRoleDataAnaly extends SdkApi{

	
	//角色登录，已经由子类在submitExtendData方法里调用
	
	/**
	 * 角色创建
	 * @param activity
	 * @param data
	 */
	void roleCreate(Activity activity, CommonSdkExtendData data);
	
	/**
	 * 角色升级
	 * @param activity
	 * @param data
	 */
	void roleLevelUpdate(Activity activity, CommonSdkExtendData data);
}
