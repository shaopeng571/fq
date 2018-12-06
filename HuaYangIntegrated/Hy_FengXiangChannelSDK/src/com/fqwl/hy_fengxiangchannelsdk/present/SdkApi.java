package com.fqwl.hy_fengxiangchannelsdk.present;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.IRoleDataAnaly;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.game.sdk.HuosdkManager;
import com.game.sdk.domain.CustomPayParam;
import com.game.sdk.domain.LoginErrorMsg;
import com.game.sdk.domain.LogincallBack;
import com.game.sdk.domain.PaymentCallbackInfo;
import com.game.sdk.domain.PaymentErrorMsg;
import com.game.sdk.domain.RoleInfo;
import com.game.sdk.domain.SubmitRoleInfoCallBack;
import com.game.sdk.listener.OnInitSdkListener;
import com.game.sdk.listener.OnLoginListener;
import com.game.sdk.listener.OnLogoutListener;
import com.game.sdk.listener.OnPaymentListener;

import android.app.Activity;
import android.util.Log;

public class SdkApi implements com.fqwl.hycommonsdk.present.apiinteface.SdkApi, IRoleDataAnaly {
	CommonSdkCallBack callback;
	ImplCallback implCallback;
	RoleInfo roleInfo;
	private String sdkUid;
	private HuosdkManager huosdk;
	@Override
	public void DoRelease(Activity arg0) {
		// TODO Auto-generated method stub
		huosdk.logout();
		huosdk.recycle();
	}

	@Override
	public void charge(Activity arg0, CommonSdkChargeInfo chargeInfo) {
		CustomPayParam payParam = new CustomPayParam();
		payParam.setCp_order_id(chargeInfo.getCp_order_id());
		payParam.setCurrency_name(chargeInfo.getGoods_name());
		payParam.setExchange_rate(0);
		payParam.setExt(chargeInfo.getExtra());
		payParam.setProduct_count(Integer.valueOf(chargeInfo.getGame_coin()));
		payParam.setProduct_desc(chargeInfo.getGoods_desc());
		payParam.setProduct_id(chargeInfo.getGoods_id());
		payParam.setProduct_name(chargeInfo.getGoods_name());
		payParam.setProduct_price((float) chargeInfo.getMoney());
		payParam.setRoleinfo(roleInfo);

		huosdk.showPay(payParam, new OnPaymentListener() {

			@Override
			public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
				callback.chargeOnFinish(callbackInfo.msg, 0);
			}

			@Override
			public void paymentError(PaymentErrorMsg callbackInfo) {
				// TODO Auto-generated method stub
				callback.chargeOnFinish(callbackInfo.msg, 2);
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
		return "3";
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "风向SDK";
	}

	@Override
	public void getOderId(CommonSdkChargeInfo arg0, Activity arg1, CommonSDKHttpCallback arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return sdkUid;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return "V7.2";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(Activity arg0, CommonSdkInitInfo arg1, final CommonSdkCallBack callback,
			final ImplCallback implCallback) {
		this.callback = callback;
		this.implCallback = implCallback;
		huosdk=HuosdkManager.getInstance();
		huosdk.setDirectLogin(true);
		huosdk.setFloatInitXY(500,200);
		huosdk.initSdk(arg0, new OnInitSdkListener() {

			@Override
			public void initSuccess(String arg0, String arg1) {
				// TODO Auto-generated method stub
				SdkApi.this.callback.initOnFinish(arg1, 0);
			}

			@Override
			public void initError(String arg0, String arg1) {
				// TODO Auto-generated method stub
				SdkApi.this.callback.initOnFinish(arg1, 2);
			}
		});
		
		huosdk.addLogoutListener(new OnLogoutListener() {

			@Override
			public void logoutSuccess(int type, String code, String msg) {
				// TODO Auto-generated method stub
				if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {// 正常退出成功
					SdkApi.this.callback.logoutOnFinish(msg, 0);
				}
				if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {// 切换账号退出成功
					// 游戏此时可跳转到登陆页面，让用户进行切换账号
//	                    Toast.makeText(MainActivity.this,"退出登陆",Toast.LENGTH_SHORT).show();
					SdkApi.this.callback.ReloginOnFinish(msg, 4);
				}
				if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {// 登陆过期退出成功
					// 游戏此时可跳转到登陆页面，让用户进行重新登陆
					huosdk.showLogin(true);
					SdkApi.this.callback.ReloginOnFinish(msg, 1);
				}

			}

			@Override
			public void logoutError(int type, String code, String msg) {
				if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {// 正常退出失败
					SdkApi.this.callback.logoutOnFinish(msg, 2);
				}
				if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {// 切换账号退出失败

				}
				if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {// 登陆过期退出失败

				}

			}
		});
	}

	@Override
	public void login(Activity arg0, CommonSdkLoginInfo arg1) {
		// TODO Auto-generated method stub
		Log.e("fq", "fengxiangapi line:182 login");
		huosdk.addLoginListener(new OnLoginListener() {

			@Override
			public void loginSuccess(LogincallBack arg0) {
				// TODO Auto-generated method stub
				sdkUid = arg0.mem_id;
				JSONObject result_data = new JSONObject();
				try {
					result_data.put("mem_id", arg0.mem_id);
					result_data.put("user_token", arg0.user_token);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				implCallback.onLoginVerify(result_data);
			}

			@Override
			public void loginError(LoginErrorMsg arg0) {
				// TODO Auto-generated method stub
				callback.loginOnFinish(arg0.msg, 2);
			}
		});
		try {
			Log.e("fq", "showLogin(true)");
			huosdk.showLogin(true);
		} catch (Exception e) {
			Log.e("fq", "风向sdk line208 登录报错");
			e.printStackTrace();
			Log.e("fq", "showLogin(false)");
			huosdk.showLogin(false);
		}
	}

	@Override
	public void reLogin(Activity arg0, CommonSdkLoginInfo arg1) {
		// TODO Auto-generated method stub
		huosdk.switchAccount();
	}

	@Override
	public void setDebug(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean showExitView(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean showPersonView(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	private RoleInfo setRoleInfo(CommonSdkExtendData data) {
		RoleInfo roleInfo = new RoleInfo();
		roleInfo.setParty_name(data.getParty_name());
		roleInfo.setRole_balence(Float.valueOf(data.getUserMoney()));
		roleInfo.setRole_id(data.getRoleId());
		roleInfo.setRole_level(Integer.valueOf(data.getRoleLevel()));
		roleInfo.setRole_name(data.getRoleName());
		
		roleInfo.setRole_vip(Integer.valueOf(data.getVipLevel()));
		roleInfo.setRolelevel_ctime(data.getRoleCTime());
		roleInfo.setRolelevel_mtime(data.getRoleCTime());
		roleInfo.setServer_id(data.getServerId());
		roleInfo.setServer_name(data.getServerName());
		return roleInfo;

	}

	@Override
	public void submitExtendData(Activity arg0, CommonSdkExtendData data) {
		RoleInfo roleInfo = setRoleInfo(data);
		this.roleInfo = roleInfo;
		roleInfo.setRole_type(1);
		huosdk.setRoleInfo(roleInfo, submitRoleInfoCallBack);
	}

	@Override
	public void roleCreate(Activity arg0, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		RoleInfo roleInfo = setRoleInfo(data);
		this.roleInfo = roleInfo;
		roleInfo.setRole_type(2);
		huosdk.setRoleInfo(roleInfo, submitRoleInfoCallBack);
	}

	@Override
	public void roleLevelUpdate(Activity arg0, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		RoleInfo roleInfo = setRoleInfo(data);
		this.roleInfo = roleInfo;
		roleInfo.setRole_type(3);
		huosdk.setRoleInfo(roleInfo, submitRoleInfoCallBack);
	}
	private SubmitRoleInfoCallBack submitRoleInfoCallBack=new SubmitRoleInfoCallBack() {
		
		@Override
		public void submitSuccess() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void submitFail(String arg0) {
			// TODO Auto-generated method stub
			
		}
	};
}
