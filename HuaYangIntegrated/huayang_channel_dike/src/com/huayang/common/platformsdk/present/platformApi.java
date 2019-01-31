package com.huayang.common.platformsdk.present;



import org.json.JSONException;
import org.json.JSONObject;

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
import com.fqwl.hycommonsdk.util.logutils.FLogger;
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


public class platformApi implements SdkApi, IRoleDataAnaly, IWelcome {
	protected Activity mActivity;
	public CommonSdkCallBack mBack;
	protected int x = 0, y = 0;
	ImplCallback implCallback;
	
	HuosdkManager sdkManager;
	RoleInfo roleInfo;


	@Override
	public void init(final Activity context, final CommonSdkInitInfo info, final CommonSdkCallBack back,
			final ImplCallback implCallback) {
		this.mActivity = context;
		this.mBack = back;
		this.implCallback = implCallback;
		// 获得sdk单例
		sdkManager = HuosdkManager.getInstance();
		// 设置是否使用直接登陆,true为使用：第一次调用登陆时自动生成一个账号登陆
		sdkManager.setDirectLogin(false);
		sdkManager.setFloatInitXY(500, 200);
		// sdk初始化
		sdkManager.initSdk(context, new OnInitSdkListener() {
			@Override
			public void initSuccess(String code, String msg) {
				mBack.initOnFinish("初始化成功:"+msg, 0);
			}

			@Override
			public void initError(String code, String msg) {
				mBack.initOnFinish("初始化失败:"+msg, -2);
			}
		});

		// 添加sdk登陆监听,包含正常登陆，切换账号登陆，登陆过期后重新登陆
		sdkManager.addLoginListener(new OnLoginListener() {
			@Override
			public void loginSuccess(LogincallBack logincBack) {
				/**
				 * logincBack中包含额外信息，可能为空，视情况使用： public String nickname;//昵称 public String
				 * portrait;//头像url public String sex;//性别 "1"男, "2"女 public String city;//城市
				 * public String province;//省份 public String country;//国家 public String year
				 * ;//出生年
				 */
				FLogger.d("登陆成功 memId=" + logincBack.mem_id + "  token=" + logincBack.user_token + "  昵称="
						+ logincBack.extInfo.nickname);
				// 一般登陆成功后需要显示浮点
				sdkManager.showFloatView();
				JSONObject ob = new JSONObject();

				try {
					ob.put("mem_id", logincBack.mem_id);
					ob.put("user_token", logincBack.user_token);
					ob.put("nickname", logincBack.extInfo.nickname);
//					ob.put("serverId", PhoneInfoUtil.getServiceID(context.getApplicationContext()));
//					if (getChannelID().equals("wandou")) {
//						ob.put("platform_api_version", "2");
//					}
					implCallback.onLoginVerify(ob);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			@Override
			public void loginError(LoginErrorMsg loginErrorMsg) {
				FLogger.e(" code=" + loginErrorMsg.code + "  msg=" + loginErrorMsg.msg);
				mBack.loginOnFinish("登录失败："+loginErrorMsg.msg, -2);
			}
		});
		sdkManager.addLogoutListener(new OnLogoutListener() {
			@Override
			public void logoutSuccess(int type, String code, String msg) {
				FLogger.d( "登出成功，类型type=" + type + " code=" + code + " msg=" + msg);
				if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {// 正常退出成功
					mBack.logoutOnFinish("登出成功", 0);
				}
				if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {// 切换账号退出成功
					// 游戏此时可跳转到登陆页面，让用户进行切换账号
//                    Toast.makeText(MainActivity.this,"退出登陆",Toast.LENGTH_SHORT).show();
					mBack.logoutOnFinish("登出成功", 0);
				}
				if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {// 登陆过期退出成功
					// 游戏此时可跳转到登陆页面，让用户进行重新登陆
					mBack.logoutOnFinish("登出成功", 0);
					sdkManager.showLogin(true);
				}
			}

			@Override
			public void logoutError(int type, String code, String msg) {
				FLogger.d("登出失败，类型type=" + type + " code=" + code + " msg=" + msg);
				if (type == OnLogoutListener.TYPE_NORMAL_LOGOUT) {// 正常退出失败
					mBack.logoutOnFinish("登出失败", -2);
				}
				if (type == OnLogoutListener.TYPE_SWITCH_ACCOUNT) {// 切换账号退出失败

				}
				if (type == OnLogoutListener.TYPE_TOKEN_INVALID) {// 登陆过期退出失败

				}
			}
		});

	}

	
	//

	@Override
	public void login(Activity context, CommonSdkLoginInfo commonSdkLoginInfo) {
		this.mActivity = context;
		sdkManager.showLogin(true);
	}

	@Override
	public void charge(final Activity context, final CommonSdkChargeInfo ChargeInfo) {
		this.mActivity = context;
        CustomPayParam customPayParam = new CustomPayParam();
        customPayParam.setCp_order_id(ChargeInfo.getOrder());
        customPayParam.setProduct_price((float)(ChargeInfo.getMoney()));
        customPayParam.setProduct_count(1);
        customPayParam.setProduct_id(ChargeInfo.getGoods_id());
        customPayParam.setProduct_name(ChargeInfo.getGoods_name());
        customPayParam.setProduct_desc(ChargeInfo.getGoods_desc());
        customPayParam.setExchange_rate(0);
        customPayParam.setCurrency_name("");
        customPayParam.setExt(ChargeInfo.getOrder());
        customPayParam.setRoleinfo(roleInfo);
        sdkManager.showPay(customPayParam, new OnPaymentListener() {
            @Override
            public void paymentSuccess(PaymentCallbackInfo callbackInfo) {
                double money = callbackInfo.money;
                String msg = callbackInfo.msg;
mBack.chargeOnFinish("充值成功", 0);
            }

            @Override
            public void paymentError(PaymentErrorMsg errorMsg) {
                // TODO Auto-generated method stub
                int code = errorMsg.code;
                double money = errorMsg.money;
                String msg = errorMsg.msg;
                // 弹出支付失败信息，一般不用
                mBack.chargeOnFinish(msg,-2);
            }
        });
	}

	

	@Override
	public boolean showExitView(Activity context) {
		this.mActivity = context;

		return false;
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
		 sdkManager.switchAccount();
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
		 sdkManager.recycle();
	}

	public void submitExtendData(final Activity activity, final CommonSdkExtendData data) {
		roleInfo=initRoleInfo(data);
		roleInfo.setRole_type(1);
        sdkManager.setRoleInfo(roleInfo, new SubmitRoleInfoCallBack() {
            @Override
            public void submitSuccess() {
            	FLogger.d("提交成功");
            }

            @Override
            public void submitFail(String msg) {
            	FLogger.d(msg);
            }
        });
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

	}

	@Override
	public void roleLevelUpdate(final Activity activity, final CommonSdkExtendData data) {
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
		return "7.2.1";
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return "dike";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return false;
	}

	boolean mRepeatCreate;

	@Override
	public void initWelcomeActivity(Activity activity, HyGameCallBack callback) {


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
		sdkManager.logout();
		
	}
	private RoleInfo initRoleInfo(CommonSdkExtendData data) {
        RoleInfo roleInfo = new RoleInfo();
        roleInfo.setRolelevel_ctime(data.getRoleCTime());
        roleInfo.setRolelevel_mtime(data.getRoleCTime());
        roleInfo.setParty_name(data.getParty_name());
        roleInfo.setRole_balence(Float.parseFloat(data.getUserMoney()));
        roleInfo.setRole_id(data.getRoleId());
        roleInfo.setRole_level(Integer.parseInt(data.getRoleLevel()));
        roleInfo.setRole_name(data.getRoleName());
        roleInfo.setRole_vip(Integer.parseInt(data.getVipLevel()));
        roleInfo.setServer_id(data.getServerId());
        roleInfo.setServer_name(data.getServerName());
        return roleInfo;
    }
}