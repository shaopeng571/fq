package com.fqwl.hygamedemo;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.ActivityCycle;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.ui.GwProgressDialog;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.ResUtils;
import com.fqwl.hycommonsdk.util.logutils.UIUtil;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.BaseRet;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.user.PersonInfo;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;

@SuppressLint("NewApi")
public class SdkApi_yyb implements SdkApi ,ActivityCycle{
	protected CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	public String qq_appid;
	private String qq_appkey;
	private String wx_appid;
	private String wx_appkey;
	private String tlog;
	private YsdkCallBack mYsdkCallBack;
	private Activity mActivity;
	private static String TAG="fq";
	// 刷新token url
//	private static final String REFRESH_TOKEN = CommonApis.BASIC_URL_TITLE + "?ct=init&ac=refresh_token";

	// 现网 false 沙箱true
	private boolean is_test;
	
	private AlertDialog loginDialog;
	private String zoneid;

	
	@Override
	public void init(Activity activity, CommonSdkInitInfo info, CommonSdkCallBack callBack, ImplCallback implCallback) {
		this.mActivity = activity;
		this.mBack = callBack;
		this.implCallback = implCallback;
		
		try {

			qq_appid = ChannelConfigUtil.getMetaMsg(activity, "qq_appid") + "";
			qq_appkey = ChannelConfigUtil.getMetaString(activity, "qq_appkey");
			wx_appid = ChannelConfigUtil.getMetaString(activity, "wx_appid");
			wx_appkey = ChannelConfigUtil.getMetaString(activity, "wx_appkey");
			tlog = ChannelConfigUtil.getMetaString(activity, "tlog");
			if (ChannelConfigUtil.getMetaMsg(activity, "is_test").equals("true")) {
				is_test=true;
			}else {
				is_test=false;
			}
			zoneid = ChannelConfigUtil.getMetaMsg(activity, "qq_zoneid");

			FLogger.d("qq_appid:" + qq_appid + " qq_appkey:" + qq_appkey
					+ " wx_appid:" + wx_appid + " wx_appkey:" + wx_appkey + "tlog:" + tlog);

			if (TextUtils.isEmpty(qq_appid) || TextUtils.isEmpty(qq_appkey)) {
				callBack.initOnFinish("qq_appid or qq_appkey is null", 2);
				return;
			}
			if (TextUtils.isEmpty(wx_appid) || TextUtils.isEmpty(wx_appkey)) {
				callBack.initOnFinish("wx_appid or wx_appkey is null",2);
				return;
			}
//			Tlog.tlogKey = tlog;
			this.mActivity = activity;
			YSDKApi.onCreate(mActivity);
			mYsdkCallBack = new YsdkCallBack();
			YSDKApi.setBuglyListener(mYsdkCallBack);
			YSDKApi.setUserListener(mYsdkCallBack);
			YSDKApi.handleIntent(mActivity.getIntent());
//			ShareApi.getInstance().regShareCallBack(mShareCallBack);
			mBack.initOnFinish("初始化成功", 0);
		} catch (Exception e) {
			FLogger.Ex( e);
			mBack.initOnFinish("渠道初始化异常:" + e.getMessage(),2);
		}
	}

	@Override
	public void login(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub

		ePlatform platform = getPlatform();
		if (platform == ePlatform.QQ || platform == ePlatform.WX) {
			FLogger.d("登录缓存有效 :" + platform);
			UserLoginRet ret = new UserLoginRet();
			// FLogger.d(  "before getLoginRecord" + ret.toString());
			com.tencent.ysdk.api.YSDKApi.getLoginRecord(ret);// 获得openid
			// FLogger.d(  "after getLoginRecord" + ret.toString());
			return;
		}
		
		YSDKApi.logout();
		showLoginDialogView();
	}
	// 显示登录对话框 //老sdk的UI
		private synchronized void showLoginDialogView() {
			if (loginDialog != null) {
				FLogger.d("登录对话框已弹出...");
				return;
			}
			View rootView = View.inflate(mActivity,
					ResUtils.getInstance().getLayoutResByName(mActivity,"gowan_commonsdk_tencent_login"), null);
			View loginByQQ = rootView.findViewById(ResUtils.getInstance().getIdResByName(mActivity,"login_by_qq"));
			loginByQQ.setOnClickListener(loginClickListener);
			View loginByWX = rootView.findViewById(ResUtils.getInstance().getIdResByName(mActivity,"login_by_wx"));
			loginByWX.setOnClickListener(loginClickListener);
			Builder builder = new AlertDialog.Builder(mActivity);
			builder.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					loginDialog = null;
					FLogger.d("loginDialog Dismiss");
				}
			});
			loginDialog = builder.create();
			loginDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			loginDialog.show();
			loginDialog.setContentView(rootView);
		}
	// 获取当前登录平台
		public ePlatform getPlatform() {
			UserLoginRet ret = new UserLoginRet();
			YSDKApi.getLoginRecord(ret);
			if (ret.flag == eFlag.Succ) {
				return ePlatform.getEnum(ret.platform);
			}
			return ePlatform.None;
		}

	@Override
	public void charge(Activity activity, CommonSdkChargeInfo ChargeInfo) {
		// TODO Auto-generated method stub
		// 商品图片
				Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(),
						ResUtils.getInstance().getDrawableResIDByName("gowan_commonsdk_qq_picon"));
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
				byte[] appResData = baos.toByteArray();

				String money = ChargeInfo.getMoney()+"";// 这里是购买多少游戏币
				if (zoneid.equals("0")) {
					zoneid="1";
				}
				YSDKApi.recharge(zoneid, money, false, appResData, ChargeInfo.getOrder(),
						new PayListener() {

							@Override
							public void OnPayNotify(PayRet ret) {
								if (ret != null) {
									FLogger.d(ret.toString());

									if (ret.ret == PayRet.PAYSTATE_PAYSUCC) {
										FLogger.d("成功");
										
										mBack.chargeOnFinish("应用宝渠道支付成功", 0);
									} else {
										FLogger.d("失败");
									}
								}

							}
						});
	}

	@Override
	public boolean showExitView(Activity activity) {
		// TODO Auto-generated method stub
		return false;
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
	public void reLogin(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		
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
		YSDKApi.onDestroy(activity);
	}

	@Override
	public void submitExtendData(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getOderId(CommonSdkChargeInfo info, Activity context, CommonSDKHttpCallback httpCallback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return null;
	}

	class YsdkCallBack implements UserListener, BuglyListener, PayListener {

		@Override
		public void OnPayNotify(PayRet ret) {

			FLogger.i( "ret:" + ret.toString());

		}

		@Override
		public byte[] OnCrashExtDataNotify() {
			// TODO Auto-generated method stub
			FLogger.i( "OnCrashExtDataNotify");
			return null;
		}

		@Override
		public String OnCrashExtMessageNotify() {
			FLogger.e(String.format(Locale.CHINA, "OnCrashExtMessageNotify called"));
			Date nowTime = new Date();
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return "new Upload extra crashing message for bugly on " + time.format(nowTime);
		}

		@Override
		public void OnLoginNotify(UserLoginRet ret) {
			stopWaitProgress();
			String accessToken = ret.getAccessToken();
			String payToken = ret.getPayToken();
			int flag = ret.flag;
			String open_id = ret.open_id;
			
			FLogger.w( "OnLoginNotify accessToken=" + accessToken + " payToken=" + payToken + " flag=" + flag
					+ " open_id=" + open_id+" ret.ret="+ret.ret);
			switch (flag) {
			
			case eFlag.Succ:
				if (ret.ret != BaseRet.RET_SUCC) {
					FLogger.e( "ret.ret != BaseRet.RET_SUCC");
					YSDKApi.logout();
					if (mBack != null) {
						mBack.logoutOnFinish("", 0);
					}
					return;
				}
				int platform=ret.platform;
//				int platform = YSDKApi.getLoginRecord(ret);
				FLogger.e(  "platform :" + platform + " ret.platform:" + ret.platform + " flag:" + flag);
				// 登录成功 ,验证access_token和openid TODO
				callGetLoginRecord(ret, platform);
				break;
			case eFlag.QQ_UserCancel:
				UIUtil.toastShortOnMain(mActivity, "取消授权");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.QQ_LoginFail:
				UIUtil.toastShortOnMain(mActivity, "QQ登录失败,请重试");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.QQ_NetworkErr:
				UIUtil.toastShortOnMain(mActivity, "QQ登录异常,请重试");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.QQ_NotInstall:
				UIUtil.toastShortOnMain(mActivity, "未安装手机QQ");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.QQ_NotSupportApi:
				UIUtil.toastShortOnMain(mActivity, "手机QQ版本太低");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.WX_NotInstall:
				UIUtil.toastShortOnMain(mActivity, "未安装微信");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.WX_NotSupportApi:
				UIUtil.toastShortOnMain(mActivity, "微信版本太低");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.WX_UserCancel:
				UIUtil.toastShortOnMain(mActivity, "取消授权");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.WX_UserDeny:
				UIUtil.toastShortOnMain(mActivity, "拒绝授权");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.WX_LoginFail:
				UIUtil.toastShortOnMain(mActivity, "微信登录失败");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.Login_TokenInvalid:
				UIUtil.toastShortOnMain(mActivity, "您尚未登录或者之前的登录已过期，请重试");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			case eFlag.Login_NotRegisterRealName:
				UIUtil.toastShortOnMain(mActivity, "账号没有进行实名认证,请实名认证后重试");
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			default:
				if (mBack != null) {
					mBack.logoutOnFinish("", 0);
				}
				YSDKApi.logout();
				break;
			}

		}
		// 调用queryUserInfo 后获取用户信息的回调
				@Override
				public void OnRelationNotify(UserRelationRet relationRet) {
					String result = "";
					result = result + "flag:" + relationRet.flag + "\n";
					result = result + "msg:" + relationRet.msg + "\n";
					result = result + "platform:" + relationRet.platform + "\n";
					if (relationRet.persons != null && relationRet.persons.size() > 0) {
						PersonInfo personInfo = (PersonInfo) relationRet.persons.firstElement();
						StringBuilder builder = new StringBuilder();
						builder.append("UserInfoResponse json: \n");
						builder.append("nick_name: " + personInfo.nickName + "\n");
						builder.append("open_id: " + personInfo.openId + "\n");
						builder.append("userId: " + personInfo.userId + "\n");
						builder.append("gender: " + personInfo.gender + "\n");
						builder.append("picture_small: " + personInfo.pictureSmall + "\n");
						builder.append("picture_middle: " + personInfo.pictureMiddle + "\n");
						builder.append("picture_large: " + personInfo.pictureLarge + "\n");
						builder.append("provice: " + personInfo.province + "\n");
						builder.append("city: " + personInfo.city + "\n");
						builder.append("country: " + personInfo.country + "\n");
						result = result + builder.toString();
					} else {
						result = result + "relationRet.persons is bad";
					}
					FLogger.d(  "OnRelationNotify = " + result);
				}

				// 异账号 游戏中授权的账号和手Q/微信中授权的账号不相同，此种场景称之为异账号。
				@Override
				public void OnWakeupNotify(WakeupRet ret) {

					FLogger.e(  "OnWakeupNotify:" + ret.toString());
					int flag = ret.flag;
					switch (flag) {
					case eFlag.Wakeup_YSDKLogining:
						// TODO 用拉起的账号登录，登录结果在OnLoginNotify()中回调
						return;
					case eFlag.Wakeup_NeedUserSelectAccount:
						// 异账号时，游戏需要弹出提示框让用户选择需要登录的账号
					case eFlag.Wakeup_NeedUserLogin:
						// 没有有效的票据，登出游戏让用户重新登录
					default:
						YSDKApi.logout();
						if (mBack != null) {
							mBack.logoutOnFinish("", 0);
						}

						UIUtil.toastShortOnMain(mActivity, "手Q或微信与游戏使用的账号不一致,请重新授权登录 :" + flag);
						FLogger.e( 
								"异账号，需要重新登录 ret.flag = " + flag + " ret.msg=" + ret.msg + " ret.platform=" + ret.platform);
						break;
					}

				}

			}
	// 选择登录方式监听
		private OnClickListener loginClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				int id = v.getId();
				if (id == ResUtils.getInstance().getIdResByName(mActivity,"login_by_qq")) {
					if (getPlatform() == ePlatform.None) {
						YSDKApi.login(ePlatform.QQ);
						showWaitProgress("正在拉起QQ登录..");
					} else {
						FLogger.w(  "QQ登录中....");
					}
					if (loginDialog != null)
						loginDialog.dismiss();

				} else if (id == ResUtils.getInstance().getIdResByName(mActivity,"login_by_wx")) {
					// 登出
					YSDKApi.logout();
					FLogger.d("先退出账号");
					if (getPlatform() == ePlatform.None) {
						YSDKApi.login(ePlatform.WX);
						showWaitProgress("正在拉起微信登录..");
					} else {
						FLogger.w(  "微信登录中....");
					}
					if (loginDialog != null)
						loginDialog.dismiss();
				}
			}
		};
		private GwProgressDialog gwProgressDialog;

		// 显示等待对话框
		private void showWaitProgress(final String msg) {
			FLogger.i(  "showWaitProgress");
			if (gwProgressDialog == null) {
				gwProgressDialog = new GwProgressDialog(mActivity, msg);
			}
			mActivity.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					gwProgressDialog.setMsg(msg);
					gwProgressDialog.show();
				}
			});
		}

		private void stopWaitProgress() {
			mActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					FLogger.i(  "stopWaitProgress");
					if (gwProgressDialog != null) {
						gwProgressDialog.disMiss();
						gwProgressDialog = null;
					}
				}
			});
		}
		private void callGetLoginRecord(UserLoginRet ret, int platform) {

			try {
				String platformName = null;
				// 用户授权票据，获取此票据以后可以认为用户已经授权，分享/支付等功能需要此票据；手Q的accessToken有效时间为90天；微信的accessToken有效时间为2小时。
				String accessToken = ret.getAccessToken();
				// 支付票据，此票据用于手Q支付，手Q授权会返回此票据。微信授权不会返回此票据。有效时间为6天。
				String payToken = ret.getPayToken();
				// 微信平台特有票据，有效期为30天，用于微信accessToken过期之后刷新accessToken。
				String refreshToken;
				// 用户授权后平台返回的唯一标识
				String openid = ret.open_id;
				// 支付需要使用到的字段，用于数据分析使用，pf的组成为：唤起平台-账号体系-注册渠道-操作系统-安装渠道-账号体系-appid-openid。
				// 例如：desktop_m_qq-73213123-android-73213123-qq-100703379-A65A1614A2F930A0CD4C2FB2C4C5DBE1
				int flag = ret.flag;
				String msg = ret.msg;
				String pf = ret.pf;
				String pf_key = ret.pf_key;
				String login_type = "";
				JSONObject verifyMap = new JSONObject();
				if (platform == ePlatform.PLATFORM_ID_QQ) {
					platformName = ret.platform + " QQ登录";
					login_type = "qq";
					verifyMap.put("appkey", qq_appkey);
				} else if (ret.platform == ePlatform.PLATFORM_ID_WX) {
					platformName = ret.platform + "  微信登录 ";
					refreshToken = ret.getRefreshToken();
					login_type = "wx";
					verifyMap.put("appkey", wx_appkey);
				} else {
					FLogger.e(TAG, "wtf!!!");
					return;
				}
				FLogger.i(TAG, "callGetLoginRecord ret:" + ret.toString());
				// 异步更新token
//				refreshToken(ret, accessToken, payToken, login_type);

				verifyMap.put("appid", qq_appid);
				verifyMap.put("openid", openid);
				verifyMap.put("openkey", accessToken);
				verifyMap.put("platform_api_version", "2");
				verifyMap.put("type", is_test ? "test" : "online");
				verifyMap.put("login_type", login_type);
				if (mBack == null) {
					return;
				}
				implCallback.onLoginVerify(verifyMap);
//				mBack.onLoginSuc(verifyMap, new IverifyListener() {
//
//					@Override
//					public Map<String, String> onVerifySuccess(GowanServerLoginResult loginResult) {
//						HashMap<String, String> returnPram = new HashMap<String, String>();
//						returnPram.put("forum", "0");// 先给个默认0
//						//开启分享
//						returnPram.put("share", "1");//必须接入
//						
//						// 取出论坛参数 is_show 然后返回给cp
//						Map ext = loginResult.getExt();
//						if (!ext.isEmpty()) {
//							String isShow = ext.get("is_show") + "";
//							FLogger.d(TAG, "具有特殊参数 :" + ext);
//							//开启论坛
//							returnPram.put("forum", isShow);
//						}
//						return returnPram;
//					}
//				});

			} catch (Exception e) {
				FLogger.e(TAG, "callGetLoginRecord:" + e.getMessage());
				FLogger.Ex(TAG, e);
			}

		}
//		private void refreshToken(UserLoginRet ret, String accessToken, String payToken, String login_type) {
//			JSONObject refreshDataJson = new JSONObject();
//			try {
//				refreshDataJson.put("user_id", ret.open_id);
//				refreshDataJson.put("login_type", login_type);
//				refreshDataJson.put("openkey", accessToken);
//				refreshDataJson.put("pay_token", payToken);
//				refreshDataJson.put("pfkey", ret.pf_key);
//			} catch (JSONException e1) {
//				e1.printStackTrace();
//			}
//			final HashMap<String, String> refreshMap = new HashMap<String, String>();
//			refreshMap.put("data", refreshDataJson.toString());
//			refreshMap.put("user_id", ret.open_id);
//			ThreadManager.getInstance().execute(new Runnable() {
//
//				@Override
//				public void run() {
//					CommonApiUtil.postGowanDataApi(YybSdk.this, REFRESH_TOKEN, refreshMap);
//				}
//			});
//		}

		@Override
		public void onStart(Activity activity) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onRestart(Activity activity) {
			// TODO Auto-generated method stub
			YSDKApi.onRestart(activity);
		}

		@Override
		public void onResume(Activity activity) {
			// TODO Auto-generated method stub
			YSDKApi.onResume(activity);
		}

		@Override
		public void onPause(Activity activity) {
			// TODO Auto-generated method stub
			YSDKApi.onPause(activity);
		}

		@Override
		public void onStop(Activity activity) {
			// TODO Auto-generated method stub
			YSDKApi.onStop(activity);
		}

		@Override
		public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			YSDKApi. onActivityResult(requestCode, resultCode,data);
		}

		@Override
		public void onNewIntent(Activity activity, Intent intent) {
			// TODO Auto-generated method stub
			YSDKApi.handleIntent(intent);
		}
}
