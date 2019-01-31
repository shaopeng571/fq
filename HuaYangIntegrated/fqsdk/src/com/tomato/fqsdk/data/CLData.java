package com.tomato.fqsdk.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.models.BaseInfo;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.ControlConfig;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.CryptHelper;
import com.tomato.fqsdk.utils.HJGameDataDBHelper;
import com.tomato.fqsdk.utils.HyAppUtils;
import com.tomato.fqsdk.utils.NetWorkUtil;
import com.tomato.fqsdk.utils.SpUtils;
import com.tomato.fqsdk.utils.Tools;
import com.tomato.fqsdk.utils.HJGameDataDBHelper.HJGameData;

public class CLData {
	private static CLNaviteHelper clNaviteHelper;
	private static String sid;
	private static String role_id;
	private static String role_name;
	private static String user_id;
	private static String mark;
	private static String level = "0";
//	private static int REGISTER = 2;
	private static int onresume = 1;
	private static CLControlCenter cc;
	private static HJGameDataDBHelper hjGameDataDBHelper = new HJGameDataDBHelper(
			HySDK.context);
	private static TimerTask mtimerTask = null;
	private static Timer mDataTimer = new Timer(true);

	/**
	 *   ȡ    ԭʼ      Ϣ
	 */
	private static JSONObject GetOriginalData() {
		try {
			JSONObject originaldata = new JSONObject(SpUtils.getStringValue(
					HySDK.context, CLCommon.ORIGINALDATA));
			return originaldata;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *      ɫԭʼ   ݵ     
	 */
	private static void SaveOriginalData() {
		JSONObject originaldata = new JSONObject();
		try {
			originaldata.put("sid", sid);
			originaldata.put("role_id", role_id);
			originaldata.put("role_name", role_name);
			originaldata.put("user_id", user_id);
			originaldata.put("mark", mark);
			originaldata.put("level", level);
			Tools.saveSharedPreference(HySDK.context, CLCommon.ORIGINALDATA,
					originaldata.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void configGameDataByBehavior2(String behavior,
			Map<String, Object> map) {

		//   ȡ  ¼ע  ʱ   sid,role_id,user_id ͵ ¼ʱ  ĵȼ 
		if (map.containsKey("sid")) {
			sid = map.get("sid").toString();
		}
		if (map.containsKey("role_name")) {
			role_name = map.get("role_name").toString();
		}
		if (map.containsKey("role_id")) {
			role_id = map.get("role_id").toString();
		}
		if (map.containsKey("user_id")) {
			user_id = map.get("user_id").toString();
		}else {
			user_id=HySDK.getInstance().HyGetCurrentUserID();
		}
		if (map.containsKey("level")) {
			level = map.get("level").toString();
		} else if (!map.containsKey("level")
				&& (!behavior.equals("event") && !behavior.equals("register"))) {
			map.put("level", level);
		}
		// Ϊ     ӿ  sid,roleid,userid    
		if (TextUtils.isEmpty(role_id) || TextUtils.isEmpty(user_id)
				|| TextUtils.isEmpty(sid)) {
			return;
		} else {
			//  sid,roleid,userid
			if (!map.containsKey("sid")) {
				map.put("sid", sid);
			}
			if (!map.containsKey("role_id")) {
				map.put("role_id", role_id);
			}
			if (!map.containsKey("role_name")) {
				map.put("role_name", role_name);
			}
			if (!map.containsKey("user_id")) {
				map.put("user_id", user_id);
			}
			// sdk ڲ ʶ  ǰ  ̨    
			if (behavior.equals("onresume") || behavior.equals("onstop")) {

				resumestop(behavior, map);
				//  ˳    ¼ ע     ½ǰ ˳ 
			} else if (behavior.equals("login_out") || behavior.equals("login")
					|| behavior.equals("register")
					|| behavior.equals("begin_login")) {
				LoginReg(behavior, map);
				if (behavior.equals("login_out")) {
					try {
						//  ߳ ˯  0.1  
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else {
				//         
				ElseData(behavior, map);
			}
		}

	}

	/**
	 * sdk ڲ ʶ  ǰ  ̨    
	 */
	private static void resumestop(String behavior, Map<String, Object> map) {
		if (behavior.equals("onresume")) {
			//       0  ֤   ϴ  ˳ δ ɹ   Ҫ    
			if (onresume != 0) {
				return;
			}
			// if (!TextUtils.isEmpty(markresume)) {
			// map.put("mark", markresume);
			// markresume = null;
			// submit("login_out", map);
			// }
			if (map.containsKey("role_id")) {
				role_id = map.get("role_id").toString();
			}
			mark = Tools.GetTimeZ() + role_id;
			map.put("mark", mark);
			submit("login", map);
			//  򿪼 ʱ      ʱ  鷢  ʧ      
			openTimer();
			//  Ͻ    ݣ      ԭʼ    
			SaveOriginalData();
			onresume = 1;
		}
		if (behavior.equals("onstop")) {
			//     ϴε mark      Ҫ ϴ 
			// if (!TextUtils.isEmpty(markresume)) {
			// map.put("mark", markresume);
			// markresume = null;
			// } else {
			// }
			if (onresume != 1) {
				return;
			}
			map.put("mark", mark);
			mark = null;
			submit("login_out", map);
			//  رռ ʱ  
			stopTimer();
			//  Ͻ    ݣ      ԭʼ    
			SaveOriginalData();
			onresume = 0;
		}
	}

	/**
	 *          payment  ֧         Ԫ sdk ڲ ת  Ϊ  
	 * 
	 * @param behavior
	 * @param map
	 */
	private static void ElseData(String behavior, Map<String, Object> map) {
		if (behavior.equals("payment")) {
			// if (map.containsKey("config_money")) {
			// if (map.get("config_money").toString().contains(".")) {
			// int a = map.get("config_money").toString()
			// .substring(
			// map.get("config_money").toString()
			// .indexOf(".")).length();
			//
			// if (a > 3) {
			// int index = map.get("config_money").toString()
			// .indexOf(".");
			// map.put("config_money", map.get("config_money")
			// .toString().replace(".", ""));
			// StringBuffer sBuffer = new StringBuffer(map.get(
			// "config_money").toString());
			// sBuffer.insert(index + 2, ".");
			// map.put("config_money", sBuffer);
			// } else {
			// map.put("config_money", map.get("config_money")
			// .toString().replace(".", ""));
			// if (a == 1) {
			// map.put("config_money", map.get("config_money")
			// + "00");
			// } else if (a == 2) {
			// map.put("config_money", map.get("config_money")
			// + "0");
			// }
			//
			// }
			// } else {
			// map.put("config_money", map.get("config_money") + "00");
			// }
			// }
			if (map.containsKey("config_money")) {
				try {
					map.put("config_money", Math.round(Double.valueOf(map.get(
							"config_money").toString()) * 100));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (map.containsKey("money")) {
				try {
					map.put("money", Math.round(Double.valueOf(map.get("money")
							.toString()) * 100));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// if (map.containsKey("money")) {
			// if (map.get("money").toString().contains(".")) {
			// int a = map
			// .get("money")
			// .toString()
			// .substring(map.get("money").toString().indexOf("."))
			// .length();
			// if (a > 3) {
			// int index = map.get("money").toString().indexOf(".");
			// map.put("money",
			// map.get("money").toString().replace(".", ""));
			// StringBuffer sBuffer = new StringBuffer(map
			// .get("money").toString());
			// sBuffer.insert(index + 2, ".");
			// map.put("money", sBuffer);
			// } else {
			// map.put("money",
			// map.get("money").toString().replace(".", ""));
			// if (a == 1) {
			// map.put("money", map.get("money") + "00");
			// } else if (a == 2) {
			// map.put("money", map.get("money") + "0");
			// }
			//
			// }
			// } else {
			// map.put("money", map.get("money") + "00");
			// }
			// }
		}
		submit(behavior, map);
	}

	private static void LoginReg(String behavior, Map<String, Object> map) {
		//  ˳     mark       
		if (behavior.equals("login_out")) {
			if (TextUtils.isEmpty(role_id) || TextUtils.isEmpty(mark)) {
				return;
			} else {
				map.put("mark", mark);
				mark = null;
				submit(behavior, map);
				sid = null;
				role_id = null;
				role_name = null;
				user_id = null;
				stopTimer();
				//  Ͻ    ݣ      ԭʼ    
				SaveOriginalData();
			}
		}
		//   ¼
		if (behavior.equals("login")) {
			cc = CLControlCenter.getInstance();
			cc.initBaseInfo(HySDK.context);
			//  ϴ û   ˳             л  ʺ        Ϸ        ˳ 
			//   ȡ       ݣ  鿴markʱ     
			// JSONObject hjoriginaldata=;
			try {
				mark = GetOriginalData().optString("mark");
			} catch (Exception e) {
				mark = "";
			}

			if (!TextUtils.isEmpty(mark)) {
				configGameDataByBehavior2("begin_login",
						new HashMap<String, Object>());
				configGameDataByBehavior2("login", new HashMap<String, Object>());
				//       ¼
			} else {
				if (map.containsKey("role_id")) {
					role_id = map.get("role_id").toString();
				}
				mark = Tools.GetTimeZ() + role_id;
				map.put("mark", mark);
				submit(behavior, map);
				//  Ͻ    ݣ      ԭʼ    
				SaveOriginalData();
			}
			//   ¼ 򿪶 ʱ      ʧ      
			openTimer();
		}

		// ע  
		if (behavior.equals("register")) {
			cc = CLControlCenter.getInstance();
			cc.initBaseInfo(HySDK.context);
			submit(behavior, map);
			// handler.sendEmptyMessageDelayed(REGISTER, 100);
		}
		//  л  ʺŵ ¼         
		if (behavior.equals("begin_login")) {
			JSONObject hjoriginaldata = GetOriginalData();
			try {
				map.put("sid", hjoriginaldata.optString("sid"));
			} catch (Exception e) {
				map.put("sid", sid);
			}
			try {
				map.put("role_id", hjoriginaldata.optString("role_id"));
			} catch (Exception e) {
				map.put("role_id", role_id);
			}
			try {
				map.put("role_name", hjoriginaldata.optString("role_name"));
			} catch (Exception e) {
				map.put("role_name", role_name);
			}
			try {
				map.put("user_id", hjoriginaldata.optString("user_id"));
			} catch (Exception e) {
				map.put("user_id", user_id);
			}
			try {
				map.put("level", hjoriginaldata.optString("level"));
			} catch (Exception e) {
				map.put("level", level);
			}
			try {
				mark = hjoriginaldata.optString("mark");
			} catch (Exception e) {
				mark = "";
			}
			map.put("mark", mark);
			mark = null;
			submit("login_out", map);
			//  Ͻ    ݣ      ԭʼ    
			SaveOriginalData();
		}

	}

	public static void CLOnResume() {
		if (TextUtils.isEmpty(role_id) || TextUtils.isEmpty(user_id)
				|| TextUtils.isEmpty(sid)) {
			return;
		}
		// int datacount = hjGameDataDBHelper.queryDataCount();

		cc = CLControlCenter.getInstance();
		cc.initBaseInfo(HySDK.context);
		configGameDataByBehavior2("onresume", new HashMap<String, Object>());
	}

	public static void CLOnStop() {
		if (TextUtils.isEmpty(role_id) || TextUtils.isEmpty(user_id)
				|| TextUtils.isEmpty(sid) || TextUtils.isEmpty(mark)) {
			return;
		}
		configGameDataByBehavior2("onstop", new HashMap<String, Object>());
	}

	// public static void HJExit() {
	// HJData.configGameDataByBehavior("login_out", new HashMap<String,
	// Object>());
	// stopTimer();
	// }
	// public static void HJRegisterafterlogin() {
	// if (TextUtils.isEmpty(role_id) || TextUtils.isEmpty(user_id)
	// || TextUtils.isEmpty(sid)) {
	// return;
	// }
	// configGameDataByBehavior("login", new HashMap<String, Object>());
	// }

	private static TreeMap<String, Object> GetMD5Params(Object behavior) {
		CLControlCenter cc = CLControlCenter.getInstance();
		BaseInfo bi = cc.getBaseInfo();
		ControlConfig ccf = cc.getControlConfig();

		TreeMap<String, Object> dict = new TreeMap<String, Object>();

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
		dict.put("version", ccf.getAppVersion());
		dict.put("sid", sid);
		dict.put("role_name", role_name);
		dict.put("role_id", role_id);
		dict.put("user_id", user_id);
		
		return dict;
	}

	private static JSONObject GetObjectParams(TreeMap<String, Object> dict)
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

	public static void submit(final String behavior, Map<String, Object> map) {
		CLControlCenter cc = CLControlCenter.getInstance();
		ControlConfig ccf = cc.getControlConfig();
		final JSONObject jsonObject2 = new JSONObject();
		FLogger.d("准备提交："+behavior);
		try {
			jsonObject2.put("behavior", behavior);

			TreeMap<String, Object> dict = GetMD5Params(behavior);
			dict.putAll(map);
			JSONObject jsonObject = GetObjectParams(dict);
			jsonObject2.put("data", jsonObject.toString());
			clNaviteHelper=CLNaviteHelper.getInstance();
			String sgin = CryptHelper.GetMD5Code2(dict,
					HyAppUtils.hyGetAppKey(HySDK.context));
			jsonObject2.put("sign", sgin);
			jsonObject2.put("appid", ccf.getAppId());
			HyApi httpUtils = new HyApi();
			HttpCallback callback = new HttpCallback() {

				@Override
				public void onSuccess(String data) {
				    FLogger.d("---"+behavior+" submit success---");
				}

				@Override
				public void onError(String msg) {
					// TODO Auto-generated method stub
					super.onError(msg);
					SaveFailureData(jsonObject2.toString());
				}
			};
			if (NetWorkUtil.IsNetWorkEnable(HySDK.context)) {
				httpUtils.CLDataPost(behavior, jsonObject2.toString(), callback);
			} else {
				SaveFailureData(jsonObject2.toString());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void submitDbData(final String behavior, String jsonobject,
			final String id) {
		String data = null;
		try {
			data = Tools.DecryptDoNet(jsonobject,
					CLNaviteHelper.getInstance().getSdkKey());
		} catch (Exception e1) {
			data = jsonobject;
		}

		HyApi httpUtils = new HyApi();
		HttpCallback callback = new HttpCallback() {

			@Override
			public void onSuccess(String data) {
				hjGameDataDBHelper.open();
				hjGameDataDBHelper.delete(id);
				FLogger.d(
						"-----send FailureData Success FAILURE DATA DELETE-----");

				hjGameDataDBHelper.close();
			}

			@Override
			public void onError(String msg) {
				// TODO Auto-generated method stub
				super.onError(msg);
			}
		};
		if (NetWorkUtil.IsNetWorkEnable(HySDK.context)) {

			httpUtils.CLMissDataPost(behavior, data, callback);
		}
	}

	/**
	 *    淢  ʧ ܵ     
	 **/
	private static void SaveFailureData(String data) {
		HJGameData hjGameData = hjGameDataDBHelper.new HJGameData();
		hjGameData.SetBeHavior(CLCommon.CODE_GAME_DATA);
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
			FLogger.d( "-----FAILURE DATA INSERT-----");
		} catch (Exception e) {
			hjGameDataDBHelper.close();
			return;
		}

		hjGameDataDBHelper.close();

	}

	/**
	 *     ʧ ܵ     
	 **/
	private static void getFailureData() {
		hjGameDataDBHelper.open();
		int count = hjGameDataDBHelper.queryDataCount();
		if (count > 0) {
			List<HJGameData> gameDatas = hjGameDataDBHelper
					.queryDataWithLimit(10);

			Message msg = new Message();
			msg.what = 0;
			msg.obj = gameDatas;
			dbtimer.sendMessage(msg);

		}

		hjGameDataDBHelper.close();

	}

	/**
	 *     ʧ ܵ     
	 **/
	public void getFailureAllData() {
		HJGameDataDBHelper hjGameDataDBHelper = HJGameDataDBHelper
				.getInstance(HySDK.context);
		// HJGameData hjGameData = hjGameDataDBHelper.new HJGameData();
		hjGameDataDBHelper.open();
		int count = hjGameDataDBHelper.queryDataCount();
		if (count > 0) {
			List<HJGameData> gameDatas = hjGameDataDBHelper
					.queryDataWithLimit(10);
			Message msg = new Message();
			msg.what = 0;
			msg.obj = gameDatas;
			dbtimer.sendMessage(msg);
		}
		hjGameDataDBHelper.close();
	}

	private static Handler dbtimer = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				List<HJGameData> gameDatas = new ArrayList<HJGameDataDBHelper.HJGameData>();
				gameDatas.addAll((List<HJGameData>) msg.obj);

				for (int i = 0, length = gameDatas.size(); i <= length; i++) {
					try {
						HJGameData hjGameData = gameDatas.get(i);
						submitDbData(hjGameData.getBeHavior(),
								hjGameData.getData(), hjGameData.getId() + "");
					} catch (Exception e) {
						return;
					}
				}

				break;
			default:
				break;
			}
		};
	};

	private static void openTimer() {
		stopTimer();
		if (mDataTimer == null) {
			mDataTimer = new Timer(true);
		}
		if (mtimerTask == null) {
			mtimerTask = new TimerTask() {
				public void run() {
					getFailureData();
				}
			};
		}

		if ((mDataTimer != null) && (mtimerTask != null)) {
			mDataTimer.schedule(mtimerTask, 1000, 300000);
		}

	}

	private static void stopTimer() {
		if (mDataTimer != null) {
			mDataTimer.cancel();
			mDataTimer = null;
		}
		if (mtimerTask != null) {
			mtimerTask.cancel();
			mtimerTask = null;
		}
	}
}
