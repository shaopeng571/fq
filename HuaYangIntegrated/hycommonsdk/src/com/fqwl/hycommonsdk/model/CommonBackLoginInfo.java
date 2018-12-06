package com.fqwl.hycommonsdk.model;

import org.json.JSONException;
import org.json.JSONObject;

public class CommonBackLoginInfo {
	public static final int login_success = 0;
	public static final int login_board_btn = 1;// 点击公告栏确认或者关闭按钮
	public static final int login_sdk_fail = -2;
	public static final int login_platform_fail = -1;
	public static final int login_platform_back = -3;
	private static CommonBackLoginInfo loginCallbackInfo;
	public int statusCode;
	/** uid **/
	public String userId = "";
//	/** 渠道原生uid，渠道用户id,这个只用于传给渠道接口时的用户id。 **/
//	public String sdkUserId = "";
	/** 玩家ID  （这个值只是给cp那边显示作用）
CP在角色信息页或设置页添加即可,具体可跟go玩对应的游戏运营沟通协商.
如昵称下方，设置页顶部标题处 **/
//	public String guid = "";
	public String userName = "";
	/**
	 * 时间
	 */
	public String timestamp;

	/**
	 * 服务器验证 数据
	 */
	private JSONObject sessionData;
	public String platformChanleId;
	/**
	 * 是否有登录验证 有登录验证 ，需要验证后才能登录成功
	 */
//	public boolean hasCheck;
	/**
	 * 是否是切换账号的用户
	 */
	public boolean isFloatChangeUser = false;

	/**
	 * 本地验证数据
	 */
//	public String sign = "";
	/**
	 * 第三方渠道token数据
	 */
	public String channelToken = "";
	/** Cp扩展参数, 目前传demo  {‘test’:”test”}
例如：{‘test’:”test”}  方便以后增加多参数给cp那边用就不用改签名规则 **/
	public String cp_ext;

	private CommonBackLoginInfo() {

	}

	public static CommonBackLoginInfo getInstance() {
		if (loginCallbackInfo == null)
			loginCallbackInfo = new CommonBackLoginInfo();
		return loginCallbackInfo;
	}

	public String toString() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("statusCode", this.statusCode);
			jsonObject.put("userName", this.userName);
			jsonObject.put("token", this.channelToken);
			jsonObject.put("userId", this.userId);
			jsonObject.put("platformChanleId", this.platformChanleId);
			jsonObject.put("timestamp", this.timestamp);
			jsonObject.put("isFloatChangeUser", this.isFloatChangeUser);
			jsonObject.put("cp_ext", this.cp_ext);
		} catch (JSONException e) {
			e.printStackTrace();
		}
//		return "{\"statusCode\":" + "\"" + this.statusCode + "\""
//				+ ", \"userName\":" + "\"" + this.userName + "\", \"sign\":"
//				+ "\"" + this.sign + "\"" + ",\"userId\" : " + "\""
//				+ this.userId + "\"" + ",\"platformChanleId\" : " + "\""
//				+ this.platformChanleId + "\"" + " ,\"timestamp\" :  " + "\""
//				+ this.timestamp + "\"" + " ,\"isChangeUser\" :  " + "\""
//				+ this.isChangeUser + "\"" + " ,\"hasCheck\" :  " + "\""
//				+ this.hasCheck + "\"" + "}";
		return jsonObject.toString();
	}

	public JSONObject getSessionData() {
		return sessionData;
	}

	public void setSessionData(JSONObject sessionData) {
		this.sessionData = sessionData;
	}
}
