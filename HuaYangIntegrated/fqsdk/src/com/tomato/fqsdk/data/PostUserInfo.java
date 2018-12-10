package com.tomato.fqsdk.data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.models.BaseInfo;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.CLPayResult;
import com.tomato.fqsdk.models.ControlConfig;
import com.tomato.fqsdk.models.HyInitInfo;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.CryptHelper;
import com.tomato.fqsdk.utils.HJGameDataDBHelper;
import com.tomato.fqsdk.utils.HyAppUtils;
import com.tomato.fqsdk.utils.MD5;
import com.tomato.fqsdk.utils.NetWorkUtil;
import com.tomato.fqsdk.utils.Tools;
import com.tomato.fqsdk.utils.HJGameDataDBHelper.HJGameData;

public class PostUserInfo {
//	private PostUserInfo postUserInfo=new PostUserInfo();
//	public PostUserInfo GetInstance (){
//		return postUserInfo;
//	}
//	private PostUserInfo(){
//		
//	}
	private static CLNaviteHelper clNaviteHelper;
	private static String appkey;
	private static String getKey(){
		if (TextUtils.isEmpty(appkey)) {
			try {
				appkey=HyAppUtils.hyGetAppKey(HySDK.context);
			} catch (Exception e) {
				appkey="";
				}
			
		}
		
		
		return appkey; 
	}
	public static void CLLogin(Context context, String user_type,
			String account, String pwd,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			try {
				dict.put("user_name", account.toLowerCase());
			} catch (Exception e) {
				dict.put("user_name", account);
			}
			dict.put("password", MD5.get(pwd));
			dict.put("game_id", ccf.getAppId());
			dict.put("user_type", user_type);
			dict.put("channel_id", ccf.getChannel());
			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Login/doLogin",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void CLRandomRegister(Context context, 
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
//			dict.put("channel_id", ccf.getChannel());
//			dict.put("reg_type", regtype);
			dict.put("game_id", ccf.getAppId());
//			dict.put("user_type", user_type);
//			dict.put("user_name", account.toLowerCase());
//			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Register/generate",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void CLRegister(Context context, String user_type,
			String account, String pwd, String regtype,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("password", MD5.get(pwd));
			dict.put("channel_id", ccf.getChannel());
			dict.put("reg_type", regtype);
			dict.put("game_id", ccf.getAppId());
			dict.put("user_type", user_type);
			dict.put("user_name", account.toLowerCase());
			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Register/doRegister",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLPhoneRegister(Context context, String user_type,
			String account, String pwd, String regtype, String verify_code,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("password", MD5.get(pwd));
			dict.put("channel_id", ccf.getChannel());
			dict.put("reg_type", regtype);
			dict.put("game_id", ccf.getAppId());
			dict.put("user_type", user_type);
			dict.put("user_name", account.toLowerCase());
			dict.put("verify_code", verify_code);
			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Register/doRegister",jsonObject.toString(), callback);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void fq_PhoneVerifyCode(Context context, String user_type,
			String account,String verify_code,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
//			dict.put("password", MD5.get(pwd));
			dict.put("channel_id", ccf.getChannel());
//			dict.put("reg_type", regtype);
			dict.put("game_id", ccf.getAppId());
			dict.put("user_type", user_type);
			dict.put("user_name", account.toLowerCase());
			dict.put("verify_code", verify_code);
			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Register/checkVerify",jsonObject.toString(), callback);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void CLGetCode(Context context, String phone, String type,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("phone", phone);
			dict.put("game_id", ccf.getAppId());
			dict.put("type", type);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("User/sendSms",jsonObject.toString(), callback);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLIsBind(Context context, String user_name,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("user_name", user_name.toLowerCase());
			dict.put("game_id", ccf.getAppId());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("User/isBindMobile",jsonObject.toString(), callback);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLBindisBind(Context context, String user_name,
			String pwd, HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("user_name", user_name.toLowerCase());
			dict.put("password", MD5.get(pwd));
			dict.put("game_id", ccf.getAppId());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);

			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("User/isBindMobile",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLBindPhone(Context context, String user_name,
			String password, String phone, String verify_code,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("game_id", ccf.getAppId());
			dict.put("user_name", user_name.toLowerCase());
			if (TextUtils.isEmpty(password)) {
				dict.put("password", "");
			} else {
				dict.put("password", MD5.get(password));
			}
			dict.put("phone", phone);
			
			dict.put("verify_code", verify_code);
			// dict.put("token", JsonParse.TOKEN);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);

			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("User/bindMobile",jsonObject.toString(), callback);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLChangeBind(Context context, String user_name,
			String password, String new_phone, String old_phone_code,
			String new_phone_code,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("user_name", user_name.toLowerCase());
			dict.put("game_id", ccf.getAppId());
			dict.put("new_phone", new_phone);
			dict.put("old_phone_code", old_phone_code);
			dict.put("new_phone_code", new_phone_code);
			// dict.put("token", JsonParse.TOKEN);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);

			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("User/modifyMobileBind",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLFindPwd(Context context, String user_name,
			String pwd, String verify_code,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			dict.put("game_id", ccf.getAppId());
			dict.put("user_name", user_name.toLowerCase());
			dict.put("password", MD5.get(pwd));
			dict.put("verify_code", verify_code);
			dict.put("channel_id", ccf.getChannel());
			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);

			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("User/modifyPassword",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLTempBind(Context context, String user_name,
			String password, String tourist, String regtype,
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			try {
				dict.put("user_name", user_name.toLowerCase());
			} catch (Exception e) {
				dict.put("user_name", user_name);
			}
			dict.put("password", MD5.get(password));
			dict.put("tourist", tourist);
			dict.put("reg_type", regtype);
			dict.put("channel_id", ccf.getChannel());
			dict.put("game_id", ccf.getAppId());
			dict.put("equipment", CLCommon.equipment);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);

			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Register/bind",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLTempBindPhone(Context context, String user_name,
			String password, String tourist, String regtype,
			String verify_code, HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			try {
				dict.put("user_name", user_name.toLowerCase());
			} catch (Exception e) {
				dict.put("user_name", user_name);
			}
			dict.put("password", MD5.get(password));
			dict.put("tourist", tourist);
			dict.put("reg_type", regtype);
			dict.put("channel_id", ccf.getChannel());
			dict.put("game_id", ccf.getAppId());
			dict.put("equipment", CLCommon.equipment);
			dict.put("verify_code", verify_code);
			// dict.put("token", JsonParse.TOKEN);
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Register/bind",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void CLCreateOrder(Context context,HyPayInfo hyPayInfo ,
			String pay_way, 
			HttpCallback callback) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
//		CLPayResult payInfo = CLPayResult.getInstance();
		BaseInfo baseInfo=new BaseInfo(context);
		try {
			TreeMap<String, String> dict = new TreeMap<String, String>();
			String money = hyPayInfo.getMoney() * 100 + "";
			if (money.contains(".")) {
				money = money.substring(0, money.indexOf("."));
			}
			dict.put("money", money);
			dict.put("goods_count", hyPayInfo.getGoods_count() + "");//number
			dict.put("goods_name", hyPayInfo.getGoods_name());//title
			dict.put("goods_id", hyPayInfo.getGoods_id());
			dict.put("game_id", ccf.getAppId());
			dict.put("server_id", hyPayInfo.getServer_id());
			dict.put("user_id", HySDK.getInstance().HyGetCurrentUserID());
			dict.put("channel_id", ccf.getChannel());
			dict.put("pay_way", pay_way);
			dict.put("extra", hyPayInfo.getExtra());
			//-------------------
			dict.put("cp_order_id", hyPayInfo.getCp_order_id());
			dict.put("role_id", hyPayInfo.getRole_id());
			dict.put("role_name", hyPayInfo.getRole_name());
			dict.put("role_level", hyPayInfo.getRole_level());
			dict.put("vip_level", hyPayInfo.getVip_level());
			dict.put("server_name", hyPayInfo.getServer_name());
			dict.put("goods_type", hyPayInfo.getGoods_type());
			dict.put("goods_desc", hyPayInfo.getGoods_desc());
			dict.put("game_coin", hyPayInfo.getGame_coin());
			TreeMap<String, String> gep = GetEquipmentParams();
			JSONObject gep_json = GetObjectParams(gep);
			dict.put("equipment_info",gep_json.toString() );
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			dict.put("sign", sgin);
			JSONObject jsonObject = GetObjectParams(dict);
			HyApi httpUtils=new HyApi();
			httpUtils.CLSdkPost("Payment/makeOrder",jsonObject.toString(), callback);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public static void HJinstall(Context context) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		final JSONObject params = new JSONObject();
		try {
			params.put("behavior", "install");
			TreeMap<String, String> dict = GetMD5Params("install");
			JSONObject jsonObject = GetObjectParams(dict);
			params.put("data", jsonObject.toString());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			params.put("sign", sgin);
			params.put("appid", ccf.getAppId());
			HyApi httpUtils=new HyApi();

//			AsyncHttpClient localAsyncHttpClient = new AsyncHttpClient();
//			InputStream is = new ByteArrayInputStream(params.toString()
//					.getBytes());
			HttpCallback callback=new HttpCallback(
					) {
				
				@Override
				public void onSuccess(String data) {
				}
				@Override
						public void onError(String msg) {
							// TODO Auto-generated method stub
							super.onError(msg);
							SaveFailureData(params.toString());
						}
			};
			if (NetWorkUtil.IsNetWorkEnable(context)) {
				httpUtils.CLSdkDataPost(params.toString(), callback);
			} else {
				SaveFailureData(params.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void HJactivate(Context context) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		final JSONObject jsonObject2 = new JSONObject();
		try {
			jsonObject2.put("behavior", "activate");

			TreeMap<String, String> dict = GetMD5Params("activate");
			JSONObject jsonObject = GetObjectParams(dict);
			jsonObject2.put("data", jsonObject.toString());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			jsonObject2.put("sign", sgin);
			jsonObject2.put("appid", ccf.getAppId());
			HyApi httpUtils=new HyApi();
			HttpCallback callback=new HttpCallback(
					) {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					
				}
				@Override
						public void onError(String msg) {
							// TODO Auto-generated method stub
							super.onError(msg);
							SaveFailureData(jsonObject2.toString());
						}
			};
			if (NetWorkUtil.IsNetWorkEnable(context)) {
				httpUtils.postJson(CLCommon.SDK_DATA_URL, jsonObject2.toString(), callback);

			} else {
				SaveFailureData(jsonObject2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void HJregister(Context context, String sid, String user_id) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		final JSONObject jsonObject2 = new JSONObject();
		try {
			jsonObject2.put("behavior", "register");

			TreeMap<String, String> dict = GetMD5Params("register");
			dict.put("sid", sid);
			dict.put("user_id", user_id);

			JSONObject jsonObject = GetObjectParams(dict);
			jsonObject2.put("data", jsonObject.toString());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			jsonObject2.put("sign", sgin);
			jsonObject2.put("appid", ccf.getAppId());
			HyApi httpUtils=new HyApi();
			HttpCallback callback=new HttpCallback(
					) {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					
				}
				@Override
						public void onError(String msg) {
							// TODO Auto-generated method stub
							super.onError(msg);
							SaveFailureData(jsonObject2.toString());
						}
			};
			if (NetWorkUtil.IsNetWorkEnable(context)) {
				httpUtils.postJson(CLCommon.SDK_DATA_URL, jsonObject2.toString(), callback);

			} else {
				SaveFailureData(jsonObject2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void HJlogin(Context context, String sid, String user_id) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		final JSONObject jsonObject2 = new JSONObject();
		try {
			jsonObject2.put("behavior", "login");

			TreeMap<String, String> dict = GetMD5Params("login");
			dict.put("sid", sid);
			dict.put("user_id", user_id);

			JSONObject jsonObject = GetObjectParams(dict);
			jsonObject2.put("data", jsonObject.toString());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			jsonObject2.put("sign", sgin);
			jsonObject2.put("appid", ccf.getAppId());

			HttpCallback callback=new HttpCallback() {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onError(String msg) {
					// TODO Auto-generated method stub
					super.onError(msg);
					SaveFailureData(jsonObject2.toString());
				}
			};
			HyApi httpUtils=new HyApi();

			if (NetWorkUtil.IsNetWorkEnable(context)) {
				httpUtils.postJson(CLCommon.SDK_DATA_URL, jsonObject2.toString(), callback);
//				client.newCall(request).enqueue(callback);
//				localAsyncHttpClient.hjsdkdatapost(HySDK.context, is,
//						jsonHttpResponseHandler);
			} else {
				SaveFailureData(jsonObject2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void HJpayment(Context context, String sid, String user_id,
			String payment_channel, String config_money, String money,
			String currency, String order_id) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
			try {
				config_money=Math.round(Double.valueOf(config_money.toString())*100)+"";
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				money=Math.round(Double.valueOf(money.toString())*100)+"";
			} catch (Exception e) {
				e.printStackTrace();
			}
		final JSONObject jsonObject2 = new JSONObject();
		try {
			jsonObject2.put("behavior", "payment");
			TreeMap<String, String> dict = GetMD5Params("payment");
			dict.put("sid", sid);
			dict.put("user_id", user_id);
			dict.put("payment_channel", payment_channel);
			dict.put("config_money", config_money);
			dict.put("money", money);
			dict.put("currency", currency);
			dict.put("order_id", order_id);
			JSONObject jsonObject = GetObjectParams(dict);
			jsonObject2.put("data", jsonObject.toString());
			String sgin = CryptHelper.GetMD5Code(dict,
					getKey());
			jsonObject2.put("sign", sgin);
			jsonObject2.put("appid", ccf.getAppId());
			HyApi httpUtils=new HyApi();
			HttpCallback callback=new HttpCallback(
					) {
				
				@Override
				public void onSuccess(String data) {
					// TODO Auto-generated method stub
					
				}
				@Override
						public void onError(String msg) {
							// TODO Auto-generated method stub
							super.onError(msg);
							SaveFailureData(jsonObject2.toString());
						}
			};
			if (NetWorkUtil.IsNetWorkEnable(context)) {//"https://interface.sdk.clwlnet.com/sdk.php"
				httpUtils.postJson(CLCommon.SDK_DATA_URL, jsonObject2.toString(), callback);

			} else {
				SaveFailureData(jsonObject2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static TreeMap<String, String> GetMD5Params(String behavior) {
		CLControlCenter cc = CLControlCenter.getInstance();
		BaseInfo bi = cc.getBaseInfo();
		ControlConfig ccf = cc.getControlConfig();

		TreeMap<String, String> dict = new TreeMap<String, String>();

		dict.put("package_id", bi.getPacketName());
		dict.put("app_id", ccf.getAppId());
		dict.put("behavior", behavior);
		dict.put("channel_source", ccf.getChannel() + "");
		dict.put("type", CLCommon.equipment);
		dict.put("equipment_code", bi.getDeviceId());
		dict.put("equipment_name", bi.getDevice());
		dict.put("equipment_api", bi.getResolution());
		dict.put("equipment_os", bi.getBrand());
		dict.put("idfa", bi.getImei());
		dict.put("network", bi.getNetType());
		dict.put("network_isp", bi.getOperators());
		dict.put("ip", bi.getIp());
		dict.put("time", Tools.GetTimeZ());
		return dict;
	}
	public static TreeMap<String, String> GetEquipmentParams() {
		CLControlCenter cc = CLControlCenter.getInstance();
		BaseInfo bi = cc.getBaseInfo();
		ControlConfig ccf = cc.getControlConfig();
		TreeMap<String, String> dict = new TreeMap<String, String>();

		dict.put("package_id", bi.getPacketName());
		dict.put("app_id", ccf.getAppId());
		dict.put("channel_source", ccf.getChannel() + "");
		dict.put("type", CLCommon.equipment);
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
	public static JSONObject GetObjectParams(TreeMap<String, String> dict)
			throws JSONException {
		JSONObject params = new JSONObject();
		Set<Entry<String, String>> set = dict.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			params.put(entry.getKey() + "", entry.getValue() + "");
		}
		return params;
	}


	/**
	 *    淢  ʧ ܵ     
	 **/
	private static void SaveFailureData(String data) {
		HJGameDataDBHelper hjGameDataDBHelper = HJGameDataDBHelper
				.getInstance(HySDK.context);
		HJGameData hjGameData = hjGameDataDBHelper.new HJGameData();
		hjGameData.SetBeHavior(CLCommon.CODE_SDK_DATA);
		String endata = null;
		try {
			endata = Tools.EncryptAsDoNet(data,
					CLNaviteHelper.getInstance().getSdkKey());
		} catch (Exception e1) {
			endata = data;
		}
		hjGameData.setData(endata);
		hjGameDataDBHelper.open();
		try {
			hjGameDataDBHelper.insert(hjGameData);
			Log.e("HJ", "-----FAILURE SDKDATA INSERT-----");
		} catch (Exception e) {
			hjGameDataDBHelper.close();
		}

		hjGameDataDBHelper.close();

	}
	
}
