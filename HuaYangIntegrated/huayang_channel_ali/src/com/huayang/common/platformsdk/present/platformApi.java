package com.huayang.common.platformsdk.present;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.HyGameCallBack;
import com.fqwl.hycommonsdk.present.apiinteface.IRoleDataAnaly;
import com.fqwl.hycommonsdk.present.apiinteface.IWelcome;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.EncoderUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.Global;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import cn.uc.gamesdk.UCGameSdk;
import cn.gundam.sdk.shell.even.SDKEventKey;
import cn.gundam.sdk.shell.even.SDKEventReceiver;
import cn.gundam.sdk.shell.even.Subscribe;
import cn.gundam.sdk.shell.exception.AliLackActivityException;
import cn.gundam.sdk.shell.exception.AliNotInitException;
import cn.gundam.sdk.shell.open.ParamInfo;
import cn.gundam.sdk.shell.open.OrderInfo;
import cn.gundam.sdk.shell.open.UCLogLevel;
import cn.gundam.sdk.shell.open.UCOrientation;
import cn.gundam.sdk.shell.param.GameParams;
import cn.gundam.sdk.shell.param.SDKParamKey;
import cn.gundam.sdk.shell.param.SDKParams;

public class platformApi implements SdkApi, IRoleDataAnaly, IWelcome {
	protected Activity mActivity;
	public CommonSdkCallBack mBack;
	protected int x = 0, y = 0;
	public SDKEventReceiver receiver;
	private boolean isRelogin;
	private boolean initUC = false;
	ImplCallback implCallback;
	boolean isFloatLogin = false;
	
	
	boolean isInitSuc = false;
	
	private String ucappid;
	private String ucappkey;
	
	@Override
	public void init(final Activity context, final CommonSdkInitInfo info, final CommonSdkCallBack back,
			ImplCallback implCallback) {
		this.mActivity = context;
		this.mBack = back;
		this.implCallback = implCallback;
		ucappid = ChannelConfigUtil.getMetaMsg(context, "ucappid");
		FLogger.d(ucappid);
		if (TextUtils.isEmpty(ucappid)) {
			FLogger.d(Global.INNER_TAG, "初始化失败");
			back.initOnFinish("初始化失败", -1);
			return; 
		}
		if (!initUC) {
			FLogger.d(Global.INNER_TAG, "未初始化..");
			initUC(context, null);
		}else {
			FLogger.d(Global.INNER_TAG, "已初始化过..");
			FLogger.d(Global.INNER_TAG, "回调初始化成功 ");
			mBack.initOnFinish("初始化成功", 0);
		}
		

	}

	public void initUC(final Activity context, final HyGameCallBack callback) {
		if (receiver != null) {
			FLogger.d("receiver已经注册初始化，停止再次调用 ");
			FLogger.d(Global.INNER_TAG, "receiver已经注册初始化，停止再次调用 ");
			return;
		}
		initUC = true;
		
		receiver = new SDKEventReceiver() {
			@Subscribe(event = SDKEventKey.ON_INIT_SUCC)
			private void onInitSucc() {
				FLogger.d("onInitSucc  初始化成功");
				FLogger.d(Global.INNER_TAG, "onInitSucc  初始化成功 ");
				isInitSuc = true;
			}

			@Subscribe(event = SDKEventKey.ON_INIT_FAILED)
			private void onInitFailed(String data) {
				FLogger.d("onInitFailed  初始化失败 msg=" + data);
				mBack.initOnFinish("初始化失败", -1);
				FLogger.d(Global.INNER_TAG, "onInitFailed  初始化失败 msg=" + data);
			}

			@Subscribe(event = SDKEventKey.ON_LOGIN_SUCC)
			private void onLoginSucc(String sid) {
				FLogger.d("onLoginSucc  登录成功");
				FLogger.d(Global.INNER_TAG, "onLoginSucc  登录成功");
				JSONObject ob = new JSONObject();
				try {
					ob.put("sid", sid);
					ob.put("gameId", ucappid);
//					ob.put("channelId", "0");
//					ob.put("serverId", PhoneInfoUtil.getServiceID(context.getApplicationContext()));
//					if (getChannelID().equals("wandou")) {
//						ob.put("platform_api_version", "2");
//					}
					implCallback.onLoginVerify(ob);
					// 浮标切换账号登录
					if (isFloatLogin) {
						FLogger.d(Global.INNER_TAG, "切换账号成功 code 4");
						FLogger.d("切换账号成功 code 4");
						mBack.ReloginOnFinish("切换账号成功", 4);
						implCallback.onLoginSuccess("", "", ob, "1", null);
					} else {
						implCallback.onLoginSuccess("", "", ob, null, null);
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			@Subscribe(event = SDKEventKey.ON_LOGIN_FAILED)
			private void onLoginFailed(String desc) {
				FLogger.d("onLoginFailed  登录失败");
				
				implCallback.onLoginFail(-1);
			}

			@Subscribe(event = SDKEventKey.ON_LOGOUT_SUCC)
			private void onLogoutSucc() {
				if (isRelogin) {
					isRelogin = false;
					FLogger.d("调用切换账号");
					login(mActivity, null);
				} else {
					FLogger.d("登录窗弹出");
					isFloatLogin = true;
				}

			}

			@Subscribe(event = SDKEventKey.ON_LOGOUT_FAILED)
			private void onLogoutFailed() {
				// Logger.d("onLogoutFailed");
				mBack.logoutOnFinish("注销失败", -2);
			}

			@Subscribe(event = SDKEventKey.ON_EXIT_SUCC)
			private void onExit(String desc) {
				mBack.exitViewOnFinish("退出成功", 0);

			}

			@Subscribe(event = SDKEventKey.ON_EXIT_CANCELED)
			private void onExitCanceled(String desc) {
				mBack.exitViewOnFinish("退出失败", -2);
			}

			@Subscribe(event = SDKEventKey.ON_CREATE_ORDER_SUCC)
			private void onCreateOrderSucc(OrderInfo orderInfo) {
				FLogger.d(getChannelID() + " onCreateOrderSucc");
				if (orderInfo != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("'orderId':'%s'", orderInfo.getOrderId()));
					sb.append(String.format("'orderAmount':'%s'", orderInfo.getOrderAmount()));
					sb.append(String.format("'payWay':'%s'", orderInfo.getPayWay()));
					sb.append(String.format("'payWayName':'%s'", orderInfo.getPayWayName()));

					FLogger.d(getChannelID() + " create order: " + sb.toString());
				} else {
					FLogger.d(getChannelID() + " create order: is null");
				}
			}
			@Subscribe(event = SDKEventKey.ON_PAY_USER_EXIT)
			private void onPayUserExit(OrderInfo orderInfo) {
				FLogger.d(getChannelID() + " onPayUserExit");
				if (orderInfo != null) {
					StringBuilder sb = new StringBuilder();
					sb.append(String.format("'orderId':'%s'", orderInfo.getOrderId()));
					sb.append(String.format("'orderAmount':'%s'", orderInfo.getOrderAmount()));
					sb.append(String.format("'payWay':'%s'", orderInfo.getPayWay()));
					sb.append(String.format("'payWayName':'%s'", orderInfo.getPayWayName()));
					FLogger.d(getChannelID() + " create order: " + sb.toString());
				}
			}

		};

		// uc的
		UCGameSdk.defaultSdk().registerSDKEventReceiver(receiver);
		FLogger.d("init uc registerSDKEventReceiver ");

		// //豌豆荚的
		// UCGameSdk.defaultSdk().registeSDKEventReceiver(receiver);
		ParamInfo gameParamInfo=new ParamInfo();

		gameParamInfo.setGameId(Integer.valueOf(ucappid));
		// 在九游社区设置显示查询充值历史和显示切换账号按钮，
		// 在不设置的情况下，默认情况情况下，生产环境显示查询充值历史记录按钮，不显示切换账户按钮
		// 测试环境设置无效
		gameParamInfo.setEnablePayHistory(true);// 开启查询充值历史功能
		gameParamInfo.setEnableUserChange(false);// 开启账号切换功能,默认关闭

		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 设置SDK充值方式界面为横屏，初始化、登录接口与游戏配置的横竖屏方向一致，非此处修改
			gameParamInfo.setOrientation(UCOrientation.LANDSCAPE);
			// Logger.d("uc LANDSCAPE");
		} else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 设置SDK充值方式界面为横屏，初始化、登录接口与游戏配置的横竖屏方向一致，非此处修改
			gameParamInfo.setOrientation(UCOrientation.PORTRAIT);
			// Logger.d("uc PORTRAIT");
		}
		Intent intent = context.getIntent();
		String pullupInfo = intent.getDataString();
		FLogger.d(Global.INNER_TAG, "pullupInfo:"+pullupInfo);
		if (TextUtils.isEmpty(pullupInfo)) {
			pullupInfo = intent.getStringExtra("data");
			FLogger.d(Global.INNER_TAG, " getStringExtra pullupInfo:"+pullupInfo);
		}
		FLogger.d("uc pullupInfo " + pullupInfo);
		// uc的
		SDKParams sdkParams = new SDKParams();
		sdkParams.put(SDKParamKey.GAME_PARAMS, gameParamInfo);
		sdkParams.put(SDKParamKey.PULLUP_INFO, pullupInfo);
		

		try {
			if(callback != null) {
				callback.onSuccess();
			}
			UCGameSdk.defaultSdk().initSdk(context, sdkParams);
			FLogger.d("initSdk chanle is " + getChannelID());
			
		} catch (AliLackActivityException e) {
			FLogger.Ex(Global.INNER_TAG, e);
		}

	}

	//

	@Override
	public void login(Activity context, CommonSdkLoginInfo commonSdkLoginInfo) {
		this.mActivity = context;
		try {
			UCGameSdk.defaultSdk().login(context, null);
		} catch (AliLackActivityException e) {
			e.printStackTrace();
		} catch (AliNotInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void charge(final Activity context, final CommonSdkChargeInfo ChargeInfo) {
		this.mActivity = context;
		showChargeView(context, ChargeInfo);
	}

	protected void showChargeView(final Activity context, CommonSdkChargeInfo ChargeInfo) {

		final Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put(SDKParamKey.CALLBACK_INFO, ChargeInfo.getOrder());
		// paramMap.put(SDKParamKey.SERVER_ID, ChargeInfo.getServerId());
		// paramMap.put(SDKParamKey.ROLE_ID, ChargeInfo.getRoleId());
		// paramMap.put(SDKParamKey.ROLE_NAME, ChargeInfo.getRoleName());
		// paramMap.put(SDKParamKey.GRADE, ChargeInfo.getRoleLevel());
		// if (getChannelID().equals("wandou")) {
		// // uc
		// paramMap.put(SDKParamKey.NOTIFY_URL, Constants.NOTIFI_URL_WANDOUJIA);
		// } else {
		// // uc
		// paramMap.put(SDKParamKey.NOTIFY_URL, Constants.NOTIFI_URL_UC);
		// }

		DecimalFormat decimalFormat = new DecimalFormat(".00");// 构造方法的字符格式这里如果小数不足2位,会以0补足.
		String price = decimalFormat.format(ChargeInfo.getMoney());// format
																			// 返回的是字符串
		FLogger.d("price = " + price);
		paramMap.put(SDKParamKey.AMOUNT, price);
		paramMap.put(SDKParamKey.CP_ORDER_ID, ChargeInfo.getOrder());
		paramMap.put(SDKParamKey.ACCOUNT_ID, CommonBackLoginInfo.getInstance().userId);
		FLogger.d("Chargeuid = " + CommonBackLoginInfo.getInstance().userId);
		paramMap.put(SDKParamKey.SIGN_TYPE, "MD5");

		final SDKParams sdkParams = new SDKParams();

		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(paramMap);
		sdkParams.putAll(map);
		
		JSONObject jsonObject=new JSONObject(paramMap);
		
		implCallback.getPaySign(jsonObject, new CommonSDKHttpCallback() {

			@Override
			public void onResult(ResultInfo resultInfo, String msg) {
				// TODO Auto-generated method stub
				try {
					JSONObject signjs = new JSONObject(resultInfo.data);
					sdkParams.put(SDKParamKey.SIGN,  signjs.optString("sign"));

					try {
						UCGameSdk.defaultSdk().pay(context, sdkParams);
					} catch (Exception e) {
						e.printStackTrace();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				String sign = sign(paramMap, ChannelConfigUtil.getMetaMsg(context, "ucappkey"));
				
			}
		});
		
	}

	/**
	 * 签名工具方法
	 * 
	 * @param reqMap
	 * @return
	 */
	public static String sign(Map<String, String> reqMap, String signKey) {
		// 将所有key按照字典顺序排序
		TreeMap<String, String> signMap = new TreeMap<String, String>(reqMap);
		StringBuilder stringBuilder = new StringBuilder(1024);
		for (Map.Entry<String, String> entry : signMap.entrySet()) {
			// sgin和signType不参与签名
			if ("sign".equals(entry.getKey()) || "signType".equals(entry.getKey())) {
				continue;
			}
			// 值为null的参数不参与签名
			if (entry.getValue() != null) {
				stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
			}
		}

		// 拼接签名秘钥
		stringBuilder.append(signKey);
		FLogger.d("要签名的参数：" + stringBuilder.toString());
		// 剔除参数中含有的'&'符号
		String signSrc = stringBuilder.toString().replaceAll("&", "");
		return EncoderUtil.encodeByMD5(signSrc).toLowerCase();
	}

	@Override
	public boolean showExitView(Activity context) {
		this.mActivity = context;
		try {
			UCGameSdk.defaultSdk().exit(context, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean getAdult(Activity context) {
		this.mActivity = context;
		return false;
	}

	@Override
	public void setDebug(boolean b) {
		
	}

	@Override
	public void reLogin(Activity activity, CommonSdkLoginInfo info) {
		this.mActivity = activity;
		if (isFloatLogin == true) {
			FLogger.d("浮标切换账号已登录");
			isFloatLogin = false;
			return;
		}
		try {
			isRelogin = true;
			UCGameSdk.defaultSdk().logout(activity, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean showPersonView(Activity activity) {
		this.mActivity = activity;
		return false;
	}

	@Override
	public void controlFlow(Activity context, boolean isShow) {

	}

	@Override
	public void DoRelease(Activity activity) {
		if (this.mRepeatCreate) {
			FLogger.d(Global.INNER_TAG, "onActivityResult is repeat activity!");
            return;
        }
		FLogger.d(Global.INNER_TAG, "DoRelease!");
		UCGameSdk.defaultSdk().unregisterSDKEventReceiver(receiver);
	}

	public void submitExtendData(final Activity activity, final CommonSdkExtendData data) {
		new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				FLogger.d("uc submitExtendData data:" + data.toString());
				sendUCData(activity, data);
				Looper.loop();
			}
		}).start();

	}

	private static void sendUCData(Activity activity, final CommonSdkExtendData data) {

		if (TextUtils.isEmpty(data.getRoleCTime())) {
			Log.e("commonsdk", "DATA.GETROLECTIME() IS NULL, UC审核会被拒!!!");
		}
		long RoleCTime = 0;
		try {
			RoleCTime = Long.valueOf(data.getRoleCTime());
		} catch (Exception e) {
			// TODO: handle exception
		}
		long RoleLevel = 0;
		try {
			RoleLevel = Long.valueOf(data.getRoleLevel());
		} catch (Exception e) {
			// TODO: handle exception
		}
		Log.e("commonsdk", "RoleCTime IS " + RoleCTime);
		SDKParams sdkParams = new SDKParams();
		sdkParams.put(SDKParamKey.STRING_ROLE_ID, data.getRoleId());
		sdkParams.put(SDKParamKey.STRING_ROLE_NAME, data.getRoleName());
		sdkParams.put(SDKParamKey.LONG_ROLE_LEVEL, RoleLevel);
		sdkParams.put(SDKParamKey.STRING_ZONE_ID, data.getServerId());
		sdkParams.put(SDKParamKey.STRING_ZONE_NAME, data.getServerName());
		sdkParams.put(SDKParamKey.LONG_ROLE_CTIME, RoleCTime);

		FLogger.i(Global.INNER_TAG,
				"role_id=" + data.getRoleId() + " roleName=" + data.getRoleName() + "roleLevel=" + RoleLevel
						+ " serverId=" + data.getServerId() + " serverName=" + data.getServerName() + " RoleCTime"
						+ RoleCTime);

		try {
			UCGameSdk.defaultSdk().submitRoleData(activity, sdkParams);
			Log.e("commonsdk", "uc submitRoleData");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("commonsdk", "uc submitRoleData exception:" + e.getMessage());
		}

	}

	void showLoginFail(int i) {
		implCallback.onLoginFail(-1);
	}

	public boolean logout(Activity activity) {
		return false;
	}


	@Override
	public void roleCreate(final Activity activity, final CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		FLogger.d("uc roleCreate data:" + data.toString());

		new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				sendUCData(activity, data);
				Looper.loop();
			}
		}).start();
	}

	@Override
	public void roleLevelUpdate(final Activity activity, final CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		FLogger.d("uc roleUpdate data:" + data.toString());

		new Thread(new Runnable() {
			public void run() {
				Looper.prepare();
				sendUCData(activity, data);
				Looper.loop();
			}
		}).start();
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return CommonBackLoginInfo.getInstance().userId;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return "8.0.4";
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return "uc";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return true;
	}

	boolean mRepeatCreate;
	@Override
	public void initWelcomeActivity(Activity activity, HyGameCallBack callback) {
		if ((activity.getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            FLogger.i(Global.INNER_TAG, "onCreate with flag FLAG_ACTIVITY_BROUGHT_TO_FRONT");
            mRepeatCreate = true;
            activity.finish();
            return;
        }else {
        	 FLogger.i(Global.INNER_TAG, "initWelcomeActivity");
        	 initUC(activity, callback);
        }
		
	}

	


	@Override
	public void getOderId(CommonSdkChargeInfo info, Activity context, CommonSDKHttpCallback httpCallback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

}