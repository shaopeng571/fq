package com.tomato.fqsdk.utils;

import org.json.JSONObject;


import com.tomato.fqsdk.models.HyLoginResult;

public class JsonParse {
	public static HyLoginResult parseResponseList(JSONObject response) {
		HyLoginResult rl = HyLoginResult.getInstance();
		rl.setTokenKey(response.optString("token"));
		rl.setUid(response.optString("user_id"));
		rl.setAccount(response.optString("user_name"));
		return rl;
	}
	
	public static String HJJsonGetRet(JSONObject jsonObject){
//		Log.e("HJ", jsonObject.toString());
		return jsonObject.optString("code");
	}
	public static String HJJsonGetMsg(JSONObject jsonObject){
		return jsonObject.optString("msg");
	}
	public static String HJJsonGetData(JSONObject jsonObject){
		return jsonObject.optString("data");
	}
}
