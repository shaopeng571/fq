package com.huayang.common.platformsdk.present;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.xiaomi.gamecenter.sdk.GameInfoField;
import com.xiaomi.gamecenter.sdk.MiCommplatform;
import com.xiaomi.gamecenter.sdk.MiErrorCode;
import com.xiaomi.gamecenter.sdk.OnExitListner;
import com.xiaomi.gamecenter.sdk.OnLoginProcessListener;
import com.xiaomi.gamecenter.sdk.OnPayProcessListener;
import com.xiaomi.gamecenter.sdk.entry.MiAccountInfo;
import com.xiaomi.gamecenter.sdk.entry.MiAppInfo;
import com.xiaomi.gamecenter.sdk.entry.MiBuyInfo;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class platformApi implements SdkApi {
	private Activity mActivity;
	private String xiaomi_appid;
	private String xiaomi_appkey;
	protected CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	private Bundle userData;
	@Override
	public void DoRelease(Activity arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void charge(Activity activity, CommonSdkChargeInfo params) {
		MiBuyInfo miBuyInfo = new MiBuyInfo();
		miBuyInfo.setCpOrderId(params.getOrder());
		miBuyInfo.setAmount((int) (params.getMoney()));
		miBuyInfo.setPurchaseName(params.getGoods_name());
		miBuyInfo.setCpUserInfo("");
		miBuyInfo.setExtraInfo(userData); // 设置用户信息

		MiCommplatform.getInstance().miUniPay(mActivity, miBuyInfo, new OnPayProcessListener() {

			@Override
			public void finishPayProcess(int code) {
//				Log.d("code:" + code);
				if (code == MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS) {
					implCallback.onPayFinish(0);
				}
			}
		});
	}

	@Override
	public void controlFlow(Activity arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getAdult(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "小米";
	}

	@Override
	public void getOderId(CommonSdkChargeInfo arg0, Activity arg1, CommonSDKHttpCallback arg2) {
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
		return "2.0.2";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void init(Activity activity, CommonSdkInitInfo initInfo, CommonSdkCallBack callBack, ImplCallback implCallback) {
		// TODO Auto-generated method stub
		this.mActivity = activity;
		this.mBack = callBack;
		this.implCallback = implCallback;
		xiaomi_appid = ChannelConfigUtil.getMetaMsg(activity, "xiaomi_appid");

		xiaomi_appkey = ChannelConfigUtil.getMetaMsg(activity, "xiaomi_appkey");
		Log.d("fq", xiaomi_appid+"   "+xiaomi_appkey);
		MiAppInfo miAppInfo = new MiAppInfo();
		miAppInfo.setAppId(xiaomi_appid);
		miAppInfo.setAppKey(xiaomi_appkey);
		MiCommplatform.Init(activity, miAppInfo);
		
	}

	@Override
	public void login(Activity arg0, CommonSdkLoginInfo arg1) {
		// TODO Auto-generated method stub
		MiCommplatform.getInstance().miLogin(mActivity, new OnLoginProcessListener() {

			@Override
			public void finishLoginProcess(int code, MiAccountInfo info) {
				if(info == null) {
					implCallback.onLoginFail(2);
					return;
				}
				switch (code) {
				case MiErrorCode.MI_XIAOMI_PAYMENT_SUCCESS:
					String uid = info.getUid();
					String sessionId = info.getSessionId();
					JSONObject jsonObject=new JSONObject();
					try {
						jsonObject.put("appid", xiaomi_appid);
						jsonObject.put("session", sessionId);
						jsonObject.put("uid", uid);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					implCallback.onLoginVerify(jsonObject);
					break;
				case MiErrorCode.MI_XIAOMI_PAYMENT_ERROR_ACTION_EXECUTED:
					// 正在登录
//					FLogger.d("正在登录");
					break;
				default:
					implCallback.onLoginFail(2);
					break;
				}
			}
		});
	}

	@Override
	public void reLogin(Activity arg0, CommonSdkLoginInfo arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDebug(boolean arg0) {
		// TODO Auto-generated method stub

	}
	long exitTime;
	@Override
	public boolean showExitView(Activity arg0) {
		MiCommplatform.getInstance().miAppExit(mActivity, new OnExitListner() {

			@Override
			public void onExit(int code) {
				String brand = Build.BRAND;
				if (brand.equalsIgnoreCase("xiaomi") || brand.contains("xiaomi") || brand.contains("Xiaomi")) {
					if (code == MiErrorCode.MI_XIAOMI_EXIT) {
						// SDK已经退出，此处可以调用游戏的退出函数
						mBack.exitViewOnFinish("游戏退出", 0);
					}
				}
				long currentTimeMillis = System.currentTimeMillis();
//				FLogger.d(TAG, "currentTimeMillis=" + currentTimeMillis + " exitTime=" + exitTime);
				if ((currentTimeMillis - exitTime) > 2000) {
					Toast.makeText(mActivity, "再按一次退出程序", Toast.LENGTH_SHORT).show();
					exitTime = currentTimeMillis;
				} else {
					if (code == MiErrorCode.MI_XIAOMI_EXIT) {
						mBack.exitViewOnFinish("游戏退出", 0);
					}
				}
			}
		});
		return false;
	}

	@Override
	public boolean showPersonView(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void submitExtendData(Activity activity, CommonSdkExtendData data) {
		// 用户信息，网游必须设置、单机游戏或应用可选
				Bundle mBundle = new Bundle();
				mBundle.putString(GameInfoField.GAME_USER_BALANCE, data.getUserMoney()+ ""); // 用户余额
				mBundle.putString(GameInfoField.GAME_USER_GAMER_VIP, data.getVipLevel()); // vip等级
				mBundle.putString(GameInfoField.GAME_USER_LV, data.getRoleLevel() + ""); // 角色等级
				mBundle.putString(GameInfoField.GAME_USER_PARTY_NAME, data.getParty_name() + ""); // 工会，帮派
				mBundle.putString(GameInfoField.GAME_USER_ROLE_NAME, data.getRoleName()); // 角色名称
				mBundle.putString(GameInfoField.GAME_USER_ROLEID, data.getRoleId()); // 角色id
				mBundle.putString(GameInfoField.GAME_USER_SERVER_NAME, data.getServerName()); // 所在服务器
				userData=mBundle;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}

	
}
