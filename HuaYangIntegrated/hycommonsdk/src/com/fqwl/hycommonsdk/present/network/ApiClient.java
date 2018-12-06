package com.fqwl.hycommonsdk.present.network;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.present.HySDKManager;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.CryptHelper;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.models.BaseInfo;
import com.tomato.fqsdk.models.HyRoleData;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

public class ApiClient {
	private static ApiClient apiClient;

	public static ApiClient getInstance() {
		if (apiClient == null) {
			apiClient = new ApiClient();
		}
		return apiClient;
	}

	public void userLoginVerify(Context context, JSONObject data, CommonSDKHttpCallback httpCallback) {
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("game_id", ChannelConfigUtil.getGameId(context));
			dict.put("channel_id", ChannelConfigUtil.getChannelId(context));
			dict.put("data", data.toString());
			String sgin = CryptHelper.GetMD5Code(dict, ChannelConfigUtil.getGameKey(context));
			JSONObject jsonObject = GetObjectParams(dict);
//		jsonObject.put("game_id", ChannelConfigUtil.getGameId(context));
//		jsonObject.put("channel_id", num.toString());
//		jsonObject.put("data", data);
			jsonObject.put("sign", sgin);
			HyApi httpUtils = new HyApi();
			httpUtils.HyCommonSdkPost("login", jsonObject.toString(), httpCallback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void orderCreate(Context context, CommonSdkChargeInfo hyPayInfo, CommonSDKHttpCallback httpCallback) {

		// http://api-sdk.huayang.fun/v1/channel/makeOrder
//	CLPayResult payInfo = CLPayResult.getInstance();
		BaseInfo baseInfo = new BaseInfo(context);
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			String money = hyPayInfo.getMoney() * 100 + "";
			if (money.contains(".")) {
				money = money.substring(0, money.indexOf("."));
			}
			dict.put("money", money);
			dict.put("goods_count", hyPayInfo.getGoods_count() + "");// number
			dict.put("goods_name", hyPayInfo.getGoods_name());// title
			dict.put("goods_id", hyPayInfo.getGoods_id());
			dict.put("game_id", ChannelConfigUtil.getGameId(context));
			dict.put("server_id", hyPayInfo.getServer_id());
			dict.put("user_id", HySDKManager.getInstance().getCurrentUserId());
			dict.put("channel_id", ChannelConfigUtil.getChannelId(context));
//		dict.put("pay_way", pay_way);
			dict.put("extra", hyPayInfo.getExtra());
			// -------------------
			dict.put("cp_order_id", hyPayInfo.getCp_order_id());
			dict.put("role_id", hyPayInfo.getRole_id());
			dict.put("role_name", hyPayInfo.getRole_name());
			dict.put("role_level", hyPayInfo.getRole_level());
			dict.put("vip_level", hyPayInfo.getVip_level());
			dict.put("server_name", hyPayInfo.getServer_name());
			dict.put("goods_type", hyPayInfo.getGoods_type());
			dict.put("goods_desc", hyPayInfo.getGoods_desc());
			dict.put("game_coin", hyPayInfo.getGame_coin());
			TreeMap<String, String> gep = GetEquipmentParams(context, baseInfo);
			JSONObject gep_json = GetObjectParams(gep);
			dict.put("equipment_info", gep_json.toString());
			String sgin = CryptHelper.GetMD5Code(dict, ChannelConfigUtil.getGameKey(context));
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils = new HyApi();
			httpUtils.HyCommonSdkPost("makeOrder", jsonObject.toString(), httpCallback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getPaySign(Context context, JSONObject data, CommonSDKHttpCallback httpCallback) {
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("game_id", ChannelConfigUtil.getGameId(context));
			dict.put("channel_id", ChannelConfigUtil.getChannelId(context));
			dict.put("data", data.toString());
			String sgin = CryptHelper.GetMD5Code(dict, ChannelConfigUtil.getGameKey(context));
			JSONObject jsonObject = GetObjectParams(dict);

			jsonObject.put("sign", sgin);
			HyApi httpUtils = new HyApi();
			httpUtils.HyCommonSdkPost("get_payment_sign", jsonObject.toString(), httpCallback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void orderNotice(Context context, JSONObject jsonObject, CommonSDKHttpCallback httpCallback) {
//		try {
//			TreeMap<String, String> dict = new TreeMap<String, String>();
//		String url = null;
//		if (channel.equals("yaowanqq") || channel.equals("chmsdk") || channel.equals("qqgowan")
//				|| channel.equals("yybgowan") || channel.equals("qq")) {
//			url = Constants.BASIC_URL_TX_PAY;
//		}
//		if (url == null) {
//			FLogger.e("commonsdk", "orderNotice url is null");
//			new Thread(new Runnable() {
//
//				@Override
//				public void run() {
//					// TODO Auto-generated method stub
//					Looper.prepare();
//					ToastUtil.toastInfo(mContext, "支付通知url为空");
//					Looper.loop();
//				}
//			}).start();
//			return null;
//		}
		HyApi httpUtils = new HyApi();
		httpUtils.hyPayNotify("pay_notify/game_code/cytl/channel_code/2001105?", jsonObject.toString(), httpCallback);
//		return HttpsRequest.postData(url, headerMaps, dataMap);
//		HttpRequest.post("http://api-sdk.huayang.fun/v1/channel/pay_notify/game_code/cytl/channel_code/2001105", params);
		}
	public void roleLogin(CommonSdkExtendData data, HashMap<String, String> dataMap) {
		HyRoleData role_data = new HyRoleData();
		role_data.setRoleId(data.getRoleId());// 角色id
		role_data.setRoleName(data.getRoleName());// 角色名
		role_data.setRoleLevel(data.getRoleLevel());// 角色等级
		role_data.setServerId(data.getServerId());// 所在服id
		role_data.setServerName(data.getServerName());
		role_data.setUserMoney(data.getUserMoney());
		role_data.setVipLevel(data.getVipLevel());
		role_data.setUser_id(CommonBackLoginInfo.getInstance().userId);
		role_data.setParty_name(data.getParty_name());
		role_data.setRoleCTime(data.getRoleCTime());
		HySDK.getInstance().HySubmitRoleData(role_data);
	}

	public void roleCreate(CommonSdkExtendData data, HashMap<String, String> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		HyRoleData role_data = new HyRoleData();
		role_data.setRoleId(data.getRoleId());// 角色id
		role_data.setRoleName(data.getRoleName());// 角色名
		role_data.setRoleLevel(data.getRoleLevel());// 角色等级
		role_data.setServerId(data.getServerId());// 所在服id
		role_data.setServerName(data.getServerName());
		role_data.setUserMoney(data.getUserMoney());
		role_data.setVipLevel(data.getVipLevel());
		role_data.setUser_id(CommonBackLoginInfo.getInstance().userId);
		role_data.setParty_name(data.getParty_name());
		role_data.setRoleCTime(data.getRoleCTime());
		HySDK.getInstance().HySubmitDataRoleCreate(role_data);
	}

	public void roleLevelUpdate(CommonSdkExtendData data, HashMap<String, String> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		HyRoleData role_data = new HyRoleData();
		role_data.setRoleId(data.getRoleId());// 角色id
		role_data.setRoleName(data.getRoleName());// 角色名
		role_data.setRoleLevel(data.getRoleLevel());// 角色等级
		role_data.setServerId(data.getServerId());// 所在服id
		role_data.setServerName(data.getServerName());
		role_data.setUserMoney(data.getUserMoney());
		role_data.setVipLevel(data.getVipLevel());
		role_data.setUser_id(CommonBackLoginInfo.getInstance().userId);
		role_data.setParty_name(data.getParty_name());
		role_data.setRoleCTime(data.getRoleCTime());
		HySDK.getInstance().HySubmitDataRoleLevelUpdate(role_data);
	}

	public void rolelogOut(CommonSdkExtendData data, HashMap<String, String> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		HyRoleData role_data = new HyRoleData();
		role_data.setRoleId(data.getRoleId());// 角色id
		role_data.setRoleName(data.getRoleName());// 角色名
		role_data.setRoleLevel(data.getRoleLevel());// 角色等级
		role_data.setServerId(data.getServerId());// 所在服id
		role_data.setServerName(data.getServerName());
		role_data.setUserMoney(data.getUserMoney());
		role_data.setVipLevel(data.getVipLevel());
		role_data.setUser_id(CommonBackLoginInfo.getInstance().userId);
		role_data.setParty_name(data.getParty_name());
		role_data.setRoleCTime(data.getRoleCTime());
		HySDK.getInstance().HySubmitDataLogout(role_data);
	}

	public void roleOther(CommonSdkExtendData data, String behavior, Map<String, Object> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		HyRoleData role_data = new HyRoleData();
		role_data.setRoleId(data.getRoleId());// 角色id
		role_data.setRoleName(data.getRoleName());// 角色名
		role_data.setRoleLevel(data.getRoleLevel());// 角色等级
		role_data.setServerId(data.getServerId());// 所在服id
		role_data.setServerName(data.getServerName());
		role_data.setUserMoney(data.getUserMoney());
		role_data.setVipLevel(data.getVipLevel());
		role_data.setUser_id(CommonBackLoginInfo.getInstance().userId);
		role_data.setParty_name(data.getParty_name());
		role_data.setRoleCTime(data.getRoleCTime());
		HySDK.getInstance().HySubmitDataOther(role_data, behavior, dataMap);
	}

	public void roleMSG(CommonSdkExtendData data, String appid, String openid) {
		// TODO Auto-generated method stub
//		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
//			return;
//		}
//
//		JSONObject object = getHeadObject(data, "1", appid, openid);
//		try {
//
//			object.put("level", data.getRoleLevel());
//			object.put("name", data.getRoleName());
//			object.put("fight_value", data.getPower());
//			object.put("profession", data.getProfession());
//			object.put("pay_total", data.getPay_total());
//			object.put("coin_1", data.getCoin_1());
//			object.put("coin_2", data.getCoin_2());
//			object.put("vip_level", data.getVipLevel());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String value = EncoderUtil.getHmacStr(PhoneInfoUtil.getYYBTlogKey(mContext), object.toString());
//
//		HttpsRequest.postYYB(Constants.YYB_URL_FORMAL, object.toString(), value);
	}

	
	public void roleTask(CommonSdkExtendData data, String appid, String openid) {
		// TODO Auto-generated method stub
//		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
//			return;
//		}
//		FLogger.d("role_task");
//		JSONObject object = getHeadObject(data, "2", appid, openid);
//		try {
//
//			object.put("task_id", data.getTask_id());
//			object.put("task_name", data.getTask_name());
//			object.put("status", data.getStatus());
//			object.put("task_detail", data.getTask_detail());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String value = EncoderUtil.getHmacStr(PhoneInfoUtil.getYYBTlogKey(mContext), object.toString());
//
//		HttpsRequest.postYYB(Constants.YYB_URL_FORMAL, object.toString(), value);
	}

	
	public void roleHonor(CommonSdkExtendData data, String appid, String openid) {
		// TODO Auto-generated method stub
//		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
//			return;
//		}
//		FLogger.d("role_honor");
//		JSONObject object = getHeadObject(data, "3", appid, openid);
//		try {
//
//			object.put("honor_id", data.getHonor_id());
//			object.put("honor_name", data.getHonor_name());
//			object.put("honor_detail", data.getHonor_detail());
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String value = EncoderUtil.getHmacStr(PhoneInfoUtil.getYYBTlogKey(mContext), object.toString());
//
//		HttpsRequest.postYYB(Constants.YYB_URL_FORMAL, object.toString(), value);
	}
	protected JSONObject getHeadObject(CommonSdkExtendData data,String event_id,String appid,String openid) {
		JSONObject object=new JSONObject();
//		try {
//			object.put("event_id",event_id);
//			object.put("event_time", data.getRoleCTime());
//			object.put("appid", appid);
//			object.put("openid", openid);
//			object.put("zone_id", data.getServerId());
//			object.put("zone_name", data.getServerName()());
//			object.put("platform", data.get);
//			object.put("imei", PhoneInfoUtil.getImeiCode(mContext));
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
		return object;
		
	}
	public static TreeMap<String, String> GetEquipmentParams(Context context, BaseInfo bi) {

		TreeMap<String, String> dict = new TreeMap<String, String>();

		dict.put("package_id", bi.getPacketName());
		dict.put("app_id", ChannelConfigUtil.getGameId(context));
		dict.put("channel_source", ChannelConfigUtil.getChannelId(context) + "");
		dict.put("type", "android");
		dict.put("equipment_code", bi.getDeviceId());
		dict.put("equipment_name", bi.getDevice());
		dict.put("equipment_api", bi.getResolution());
		dict.put("equipment_os", bi.getBrand());
		dict.put("idfa", bi.getImei());
		dict.put("network", bi.getNetType());
		dict.put("network_isp", bi.getOperators());
		dict.put("ip", bi.getIp());
		return dict;
	}

	public static JSONObject GetObjectParams(TreeMap<String, String> dict) throws JSONException {
		JSONObject params = new JSONObject();
		Set<Entry<String, String>> set = dict.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			params.put(entry.getKey() + "", entry.getValue() + "");
		}
		return params;
	}
}
