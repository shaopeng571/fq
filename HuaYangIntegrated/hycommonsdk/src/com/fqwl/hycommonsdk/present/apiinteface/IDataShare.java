package com.fqwl.hycommonsdk.present.apiinteface;

import java.util.HashMap;

import org.json.JSONObject;

import android.app.Activity;

/**
 * 数据扩展接口，如百度cps初始化需要传额外渠道参数
 * @author yzj
 *
 */
public interface IDataShare extends SdkApi{

	/**
	 * 获取实现类数据
	 * @param activity
	 * @param ext 拓展参数
	 * @return
	 */
	HashMap<String, String> getDataMap(Activity activity, String ext);
	/**
	 * 获取实现类数据
	 * @param activity
	 * @param ext 拓展参数
	 * @return
	 */
	JSONObject getDataJson(Activity activity, String ext);
}