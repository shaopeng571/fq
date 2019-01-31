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
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.present.HySDKManager;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSDKMustPutData;
import com.fqwl.hycommonsdk.model.CommonSdkBaseInfo;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.CommonUtils;
import com.fqwl.hycommonsdk.util.CryptHelper;
import com.fqwl.hycommonsdk.util.NetWorkUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;


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
		CommonSdkBaseInfo baseInfo = new CommonSdkBaseInfo(context);
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
	public void roleLogin(Context context,CommonSdkExtendData data, HashMap<String, String> dataMap) {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", data.getServerId());
		map.put("role_id", data.getRoleId());
		map.put("user_id", data.getUser_id());
		map.put("role_name", data.getRoleName());
		map.put("level", data.getRoleLevel());
		map.put("server_name", data.getServerName());
		map.put("party_name", data.getParty_name());
		map.put("balance", data.getUserMoney());
		map.put("vip_level", data.getVipLevel());
		map.put("role_create_time", data.getRoleCTime());
		submitRoleData(context,"login", map);
	}

	public void roleCreate(Context context,CommonSdkExtendData data, HashMap<String, String> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", data.getServerId());
		map.put("role_id", data.getRoleId());
		map.put("user_id", data.getUser_id());
		map.put("role_name", data.getRoleName());
		map.put("server_name", data.getServerName());
		map.put("party_name", data.getParty_name());
		map.put("balance", data.getUserMoney());
		map.put("vip_level", data.getVipLevel());
		map.put("role_create_time", data.getRoleCTime());
		submitRoleData(context,"register", map);
	}

	public void roleLevelUpdate(Context context,CommonSdkExtendData data, HashMap<String, String> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", data.getServerId());
		map.put("role_id", data.getRoleId());
		map.put("user_id", data.getUser_id());
		map.put("role_name", data.getRoleName());
		map.put("level", data.getRoleLevel());
		map.put("level_begin", data.getRoleLevel_begin());
		map.put("server_name", data.getServerName());
		map.put("party_name", data.getParty_name());
		map.put("balance", data.getUserMoney());
		map.put("vip_level", data.getVipLevel());
		map.put("role_create_time", data.getRoleCTime());
		submitRoleData(context,"level_up", map);
	}

	public void rolelogOut(Context context,CommonSdkExtendData data, HashMap<String, String> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", data.getServerId());
		map.put("role_id", data.getRoleId());
		map.put("user_id", data.getUser_id());
		map.put("role_name", data.getRoleName());
		map.put("level", data.getRoleLevel());
		map.put("server_name", data.getServerName());
		map.put("party_name", data.getParty_name());
		map.put("balance", data.getUserMoney());
		map.put("vip_level", data.getVipLevel());
		map.put("role_create_time", data.getRoleCTime());
		submitRoleData(context,"login_out", map);
	}

	public void roleOther(Context context,CommonSdkExtendData data, String behavior, Map<String, Object> dataMap) {
		if (TextUtils.isEmpty(CommonBackLoginInfo.getInstance().userId)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(dataMap);
		map.put("sid", data.getServerId());
		map.put("role_id", data.getRoleId());
		map.put("user_id", data.getUser_id());
		map.put("role_name", data.getRoleName());
		map.put("level", data.getRoleLevel());
		map.put("server_name", data.getServerName());
		map.put("party_name", data.getParty_name());
		map.put("balance", data.getUserMoney());
		map.put("vip_level", data.getVipLevel());
		map.put("role_create_time", data.getRoleCTime());
		submitRoleData(context,behavior, map);
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
	public static TreeMap<String, String> GetEquipmentParams(Context context, CommonSdkBaseInfo bi) {

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
	//数据模块
	public static void submitRoleData(Context context,final String behavior, Map<String, Object> map) {
		final JSONObject jsonObject2 = new JSONObject();
		FLogger.d("准备提交："+behavior);
		try {
			jsonObject2.put("behavior", behavior);

			TreeMap<String, Object> dict = GetMD5Params(context,behavior);
			dict.putAll(map);
			JSONObject jsonObject = GetDataParams(dict);
			jsonObject2.put("data", jsonObject.toString());
			String sgin = CryptHelper.GetMD5Code2(dict,
					ChannelConfigUtil.getGameKey(context));
			jsonObject2.put("sign", sgin);
			jsonObject2.put("appid", (ChannelConfigUtil.getGameId(context)));
			HyApi httpUtils = new HyApi();
			CommonSDKHttpCallback callback = new CommonSDKHttpCallback() {


				@Override
				public void onResult(ResultInfo resultInfo, String msg) {
					
					 FLogger.d("---"+behavior+" submit --- code:"+resultInfo.code);
				}
			};
				httpUtils.CLDataPost(behavior, jsonObject2.toString(), callback);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static JSONObject GetDataParams(TreeMap<String, Object> dict)
			throws JSONException {
		JSONObject params = new JSONObject();
		Set<Entry<String, Object>> set = dict.entrySet();
		Iterator<Entry<String, Object>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			params.put(entry.getKey() + "", entry.getValue() + "");
		}
		return params;
	}
	private static TreeMap<String, Object> GetMD5Params(Context context,Object behavior) {
		CommonSdkBaseInfo bi = new CommonSdkBaseInfo(context);

		TreeMap<String, Object> dict = new TreeMap<String, Object>();

		dict.put("package_id", bi.getPacketName());
		dict.put("app_id", ChannelConfigUtil.getGameId(context));
		dict.put("behavior", behavior);
		dict.put("channel_source", ChannelConfigUtil.getChannelId(context));
		dict.put("type","android");
		dict.put("equipment_code", bi.getDeviceId());
		dict.put("equipment_name", bi.getDevice());
		dict.put("equipment_api", bi.getResolution());
		dict.put("equipment_os", bi.getBrand());
		dict.put("idfa", bi.getImei());
		dict.put("network", bi.getNetType());
		dict.put("network_isp", bi.getOperators());
		dict.put("ip", bi.getIp());
		dict.put("time", CommonUtils.GetTimeZ());
		dict.put("version",CommonSDKMustPutData.getInstance().getGameVersion() );

		
		return dict;
	}
}
