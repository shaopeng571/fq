package com.fqwl.hycommonsdk.present.apiinteface;

import com.fqwl.hycommonsdk.model.CommonSdkExtendData;

import android.app.Activity;

/**
 * 角色数据统计接口和应用宝社区功能
 * @author yzj
 *
 */
public interface YYBRoleDataAnaly extends SdkApi{

	
	//角色登录，已经由子类在submitExtendData方法里调用
	/**
	 * 角色信息
	 * @param activity
	 * @param data
	 */
	void roleMsg(Activity activity, CommonSdkExtendData data);
	/**
	 * 角色任务
	 * @param activity
	 * @param data
	 */
	void roleTask(Activity activity, CommonSdkExtendData data);
	
	/**
	 * 角色荣誉
	 * @param activity
	 * @param data
	 */
	void roleHonor(Activity activity, CommonSdkExtendData data);
	/**
	 * 应用宝社区功能
	 */
	public void openImmersive();
	void share(String title, String content);
}