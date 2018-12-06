package com.huayang.common.platformsdk.present;

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
import com.fqwl.hycommonsdk.present.apiinteface.IApplication;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.vivo.unionsdk.open.VivoAccountCallback;
import com.vivo.unionsdk.open.VivoExitCallback;
import com.vivo.unionsdk.open.VivoPayCallback;
import com.vivo.unionsdk.open.VivoPayInfo;
import com.vivo.unionsdk.open.VivoRoleInfo;
import com.vivo.unionsdk.open.VivoUnionSDK;

import android.R.integer;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

public class platformApi implements SdkApi, IApplication {
	public Activity mActivity;
	private CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	private String sdkuid;
	private String vivoSin = "";
//	private VivoAccountManager mVivoAccountManager;

	private String vivo_appid;
	private String vivo_cpid;

	@Override
	public void init(Activity context, CommonSdkInitInfo info, CommonSdkCallBack back, ImplCallback implCallback) {
		// TODO Auto-generated method stub
		this.mActivity = context;
		this.mBack = back;
		this.implCallback = implCallback;
		vivo_appid = ChannelConfigUtil.getMetaMsg(context, "vivo_appid");
		vivo_cpid = ChannelConfigUtil.getMetaMsg(context, "vivo_cpid");
		FLogger.d("cpid = " + vivo_cpid);
		if (TextUtils.isEmpty(vivo_cpid)) {
			back.initOnFinish("参数为空", -1);
			return;
		}

		back.initOnFinish("初始化成功", 0);
	}

	@Override
	public void login(Activity context, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		this.mActivity = context;
		FLogger.d("login");
		VivoUnionSDK.login(context);

		if (accountCallback == null) {
			accountCallback = new MyVivoAccountCallback();
			VivoUnionSDK.registerAccountCallback(context, accountCallback);
		}
	}

	private MyVivoAccountCallback accountCallback;

	class MyVivoAccountCallback implements VivoAccountCallback {

		@Override
		public void onVivoAccountLogout(int code) {
			CommonBackLoginInfo.getInstance().userId = "";
			sdkuid = "";
			mBack.ReloginOnFinish("切换成功", 0);
		}

		@Override
		public void onVivoAccountLoginCancel() {
			implCallback.onLoginFail(2);
		}

		@Override
		public void onVivoAccountLogin(String username, String openid, String authToken) {
			sdkuid = openid;
//			Logger.d("onAccountLogin uid = " + uid);
			JSONObject object = new JSONObject();
			try {
				object.put("authtoken", authToken);
				object.put("platform_api_version", 1);
			} catch (Exception e) {

			}
			implCallback.onLoginVerify(object);
//			implCallback.onLoginSuccess(openid, username, object,null, null);
		}
	}

	@Override
	public void charge(Activity context, CommonSdkChargeInfo ChargeInfo) {
		// TODO Auto-generated method stub
		this.mActivity = context;
		showChargeView(context, ChargeInfo);
	}

	protected void showChargeView(final Activity context, final CommonSdkChargeInfo chargeInfo) {

		if (callback == null) {
			callback = new MyVivoPayCallback();
		}
//		Logger.d("ProductName = " + chargeInfo.getProductName());
//		Logger.d("Des = " + chargeInfo.getDes());
//		Logger.d("orderAmount = " + orderAmount);
//		Logger.d("vivoSin = " + vivoSin);
//		Logger.d("appId = " + appId);
//		Logger.d("vivoOrder = " + vivoOrder);
//		Logger.d("uid = " + uid);

//		Logger.d("des = " + des);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("appId", vivo_appid);
			jsonObject.put("cpId", vivo_cpid);
			jsonObject.put("cpOrderNumber", chargeInfo.getOrder());
			jsonObject.put("extInfo", chargeInfo.getOrder());
			jsonObject.put("orderAmount", (int)chargeInfo.getMoney()*100);
			jsonObject.put("orderDesc", chargeInfo.getGoods_desc());
			jsonObject.put("orderTitle", chargeInfo.getGoods_name());
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		implCallback.getPaySign(jsonObject, new CommonSDKHttpCallback() {

			@Override
			public void onResult(ResultInfo resultInfo, String msg) {
				// TODO Auto-generated method stub
				String orderAmount = "";
				String orderNumber = "";
				try {
					JSONObject signjs = new JSONObject(resultInfo.data);
					FLogger.d(signjs.toString());
					vivoSin = signjs.optString("accessKey");
					orderAmount=signjs.optString("orderAmount");
					orderNumber=signjs.optString("orderNumber");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String des = chargeInfo.getGoods_desc();
				if (TextUtils.isEmpty(des)) {
					des = chargeInfo.getGoods_name();
				}
				VivoPayInfo payInfo = new VivoPayInfo(chargeInfo.getGoods_name(), des,orderAmount, vivoSin, vivo_appid,
						orderNumber, sdkuid);
				VivoUnionSDK.pay(context, payInfo, callback);
			}
		});

	}

	private MyVivoPayCallback callback;

	class MyVivoPayCallback implements VivoPayCallback {

		@Override
		public void onVivoPayResult(String transNo, boolean isSucc, String code) {
			if (code.equals("0")) {
				implCallback.onPayFinish(0);
			} else {
				implCallback.onPayFinish(-2);
			}
		}

	}

	@Override
	public boolean showExitView(Activity activity) {
		VivoUnionSDK.exit(activity, new VivoExitCallback() {

			@Override
			public void onExitConfirm() {
				mBack.exitViewOnFinish("游戏退出", 0);
			}

			@Override
			public void onExitCancel() {
				mBack.exitViewOnFinish("继续游戏", -1);
			}
		});
		return true;
	}

	@Override
	public boolean getAdult(Activity activity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDebug(boolean b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reLogin(Activity context, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		this.mActivity = context;
		login(context, commonSdkLoginInfo);
	}

	@Override
	public boolean showPersonView(Activity activity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void controlFlow(Activity context, boolean isShow) {
		// TODO Auto-generated method stub

	}

	@Override
	public void DoRelease(Activity activity) {
		// TODO Auto-generated method stub
		accountCallback = null;
		callback = null;
	}

	@Override
	public void submitExtendData(final Activity activity, final CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			public void run() {
				Looper.prepare();

				FLogger.d("submitExtendData");
				sendRoleData(activity, data);

				Looper.loop();
			}
		}).start();
	}

	private static void sendRoleData(Activity activity, CommonSdkExtendData data) {
		// 进入游戏调用就可以了
		VivoUnionSDK.reportRoleInfo(new VivoRoleInfo(data.getRoleId(), data.getRoleLevel(), data.getRoleName(),
				data.getServerId(), data.getServerName()));
	}

	@Override
	public void getOderId(CommonSdkChargeInfo info, Activity context, CommonSDKHttpCallback httpCallback) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return CommonBackLoginInfo.getInstance().userId;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return "4.2.0";
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return "vivo";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "vivo";
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initGamesApi(Application context) {
		// TODO Auto-generated method stub
		FLogger.d("vivo init sdk");
		vivo_appid = ChannelConfigUtil.getMetaMsg(context, "vivo_appid");
		// 必须在这里init 否则不装插件弹不出支付
		VivoUnionSDK.initSdk(context, vivo_appid, false);
	}

	@Override
	public void initPluginInAppcation(Application application, Context context) {
		// TODO Auto-generated method stub

	}

}
