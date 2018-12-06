package com.fqwl.hygamedemo.a;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.ActivityCycle;
import com.fqwl.hycommonsdk.present.apiinteface.HyGameCallBack;
import com.fqwl.hycommonsdk.present.apiinteface.IDataShare;
import com.fqwl.hycommonsdk.present.apiinteface.IWelcome;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.present.apiinteface.YYBRoleDataAnaly;
import com.fqwl.hycommonsdk.present.network.ApiClient;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.ToastUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.Global;
import com.hywl.huayang_channelsdk_yyb.present.QQLoginDialog.QQLoginDialogCallback;
import com.tencent.ysdk.api.YSDKApi;
import com.tencent.ysdk.framework.common.BaseRet;
import com.tencent.ysdk.framework.common.eFlag;
import com.tencent.ysdk.framework.common.ePlatform;
import com.tencent.ysdk.module.bugly.BuglyListener;
import com.tencent.ysdk.module.immersiveicon.ImmersiveIconApi;
import com.tencent.ysdk.module.pay.PayListener;
import com.tencent.ysdk.module.pay.PayRet;
import com.tencent.ysdk.module.share.ShareApi;
import com.tencent.ysdk.module.share.ShareCallBack;
import com.tencent.ysdk.module.share.impl.ShareRet;
import com.tencent.ysdk.module.user.UserListener;
import com.tencent.ysdk.module.user.UserLoginRet;
import com.tencent.ysdk.module.user.UserRelationRet;
import com.tencent.ysdk.module.user.WakeupRet;


public class SdkApi_yyb implements SdkApi, ActivityCycle, IDataShare, YYBRoleDataAnaly, IWelcome {

	protected Activity mActivity;
	private CommonSdkCallBack mBack;
	private String appId;
	// private String uid;
	// private QQuserInfo sdkuser;
	private String orderId;
//	private int rate;
	/** 测试环境还是正式环境 **/
	String ysdkType = null;

	protected int chanleId = 0;
	private CommonSdkInitInfo mInitInfo;
	/** 是否用3K支付 **/
	private boolean is3kPay = false;
	private String login_type;
	// 默认为true，本地token失效，会为false
	private boolean isAutoLogin;
	private boolean isLogin;
	private boolean isNoticeLogin;
	ImplCallback implCallback;
	boolean isKKKInit = false;
	JSONObject loginJson = null;
	//================fq
	public String qq_appid;
	private String qq_appkey;
	private String wx_appid;
	private String wx_appkey;
	private String tlog;
	private String zoneid;
	// 现网 false 沙箱true
		private boolean is_test;
	@Override
	public void init(Activity context, CommonSdkInitInfo info, CommonSdkCallBack back, ImplCallback implCallback) {
		this.mActivity = context;
		this.mBack = back;
		this.mInitInfo = info;
		this.implCallback = implCallback;
		qq_appid = ChannelConfigUtil.getMetaMsg(context, "qq_appid") + "";
		qq_appkey = ChannelConfigUtil.getMetaString(context, "qq_appkey");
		wx_appid = ChannelConfigUtil.getMetaString(context, "wx_appid");
		wx_appkey = ChannelConfigUtil.getMetaString(context, "wx_appkey");
		tlog = ChannelConfigUtil.getMetaString(context, "tlog");
		if (ChannelConfigUtil.getMetaMsg(context, "is_test").equals("true")) {
			is_test=true;
		}else {
			is_test=false;
		}
		zoneid = ChannelConfigUtil.getMetaMsg(context, "qq_zoneid");

		FLogger.d("qq_appid:" + qq_appid + " qq_appkey:" + qq_appkey
				+ " wx_appid:" + wx_appid + " wx_appkey:" + wx_appkey + "tlog:" + tlog);
		chanleId = getplatformChanleId(mActivity);
		
		// 防止第一次登陆有票据
		// sdkuser = null;

		initsdkParam(mActivity);

		YSDKApi.onCreate(mActivity);
		ShareApi.getInstance().regShareCallBack(mShareCallBack);
		// 全局回调类，游戏自行实现
		YSDKApi.setUserListener(new YSDKCallback());
		YSDKApi.setBuglyListener(new YSDKCallback());

		// YSDK的handleIntent方法，游戏在主activity的onNewIntent调用
//		YSDKApi.handleIntent(mActivity.getIntent());

		mBack.initOnFinish("初始化成功", 0);
		String xg_appid = getMetaString(context, "xg_appid");
		String xg_accesskey = getMetaString(context, "xg_accesskey");
		FLogger.d("xg_appid:"+xg_appid+" xg_accesskey:"+xg_accesskey);
		isAutoLogin = true;
		
		ShareApi.getInstance().regShareCallBack(new ShareCallBack() {
			
			@Override
			public void onSuccess(ShareRet ret) {
				 Log.d("Share","分享成功！  分享路径："+ret.shareType.name()+" 透传信息："+ret.extInfo);
				
			}
			
			@Override
			public void onError(ShareRet ret) {
				 Log.d("Share","分享失败  分享路径："+ret.shareType.name()+" 错误码："+ret.retCode+" 错误信息："+ret.retMsg+" 透传信息："+ret.extInfo);
			}
			
			@Override
			public void onCancel(ShareRet ret) {
			    Log.d("Share","分享用户取消！  分享路径："+ret.shareType.name()+" 透传信息："+ret.extInfo);
				
			}
		});
	}
	static Bundle metaData;
	public static long getMetaILong(Context context, String key) {
		if (metaData != null) {
			return metaData.getInt(key);
		}
		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			metaData = appinfo.metaData;
			return metaData.getLong(key);
		} catch (Exception e) {
			FLogger.Ex(e);
		}
		return 0;
	}
	public static String getMetaString(Context context, String key) {
		if (metaData != null) {
			return metaData.getString(key);
		}
		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			metaData = appinfo.metaData;
			return metaData.getString(key);
		} catch (Exception e) {
			FLogger.Ex( e);
		}
		return "";
	}
	
	private ShareCallBack mShareCallBack = new ShareCallBack() {
		
		@Override
		public void onSuccess(ShareRet ret) {
			FLogger.d(Global.INNER_TAG,"分享成功！  分享路径："+ret.shareType.name()+" 透传信息："+ret.extInfo);
		}
		
		@Override
		public void onError(ShareRet ret) {
			FLogger.d(Global.INNER_TAG,"分享失败  分享路径："+ret.shareType.name()+" 错误码："+ret.retCode+" 错误信息："+ret.retMsg+" 透传信息："+ret.extInfo);
			
		}
		
		@Override
		public void onCancel(ShareRet ret) {
			FLogger.d(Global.INNER_TAG,"分享用户取消！  分享路径："+ret.shareType.name()+" 透传信息："+ret.extInfo);
			
		}
	};

	
	private QQLoginDialog qqLoginDialog;

	private void initLoginDialog(final Activity context) {
		qqLoginDialog = new QQLoginDialog(context,
				context.getResources().getIdentifier("ContentOverlay", "style", context.getPackageName()));

		qqLoginDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				qqIsShow = false;
				mBack.loginOnFinish("关闭登陆界面", 2);
			}
		});
		qqLoginDialog.setQQloginListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// YSDKApi.login(ePlatform.QQ);
				qqIsShow = false;
				qqLoginDialog.dismiss();

				// 如果已经授权自动登录了
				if (isAutoLogin) {
					ePlatform platform = getPlatform();
					if (ePlatform.QQ == platform) {
						qqLoginDialog.onAutoLoginFinish();
						FLogger.d("已经授权自动登录，取消qq事件，通知登录");
						return;
					} else {
						qqLoginDialog.onAutoLoginCancel();
						FLogger.d("取消自动登录，清除票据");
						YSDKlogout();
					}
					// if (sdkuser.loginType.equals("qq")) {
					//
					// } else {
					//
					// }
				}

				if (login_type != null && !login_type.equals("qq")) {
					// 登出
					YSDKApi.logout();
					login_type = "qq";
					callLogin(0, true);
				} else {
					callLogin(0, false);
				}
				setLastLoginType("qq");
				if (!isPackageInstalled(context, "com.tencent.mobileqq")) {
					ToastUtil.toastInfo(context, "未安装QQ");
				}
			}
		});
		qqLoginDialog.setWXloginListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// YSDKApi.login(ePlatform.WX);
				qqIsShow = false;
				qqLoginDialog.dismiss();

				// 如果已经授权自动登录了
				if (isAutoLogin) {
					ePlatform platform = getPlatform();
					if (ePlatform.WX == platform) {
						qqLoginDialog.onAutoLoginFinish();
						FLogger.d("已经授权自动登录，取消wx事件，通知登录");
						return;
					} else {
						qqLoginDialog.onAutoLoginCancel();
						FLogger.d("取消自动登录，清除票据");
						YSDKlogout();
					}
				}

				if (login_type != null && !login_type.equals("wx")) {
					// 登出
					YSDKApi.logout();
					login_type = "wx";
					callLogin(1, true);
				} else {
					callLogin(1, false);
				}
				setLastLoginType("wx");
				if (!isPackageInstalled(context, "com.tencent.mm")) {
					ToastUtil.toastInfo(context, "未安装微信");
				}

			}
		});
		qqLoginDialog.setQQLoginDialogCallback(new QQLoginDialogCallback() {
			@Override
			public void onAutoLogin() {
				qqIsShow = false;
				FLogger.d("自动登录发送登录回调");
				// ResultNotify.ShowLoginSucessByTencens(mActivity, user,
				// StatConfig.getMid(mActivity), chanle, mBack);
				noticeLogin();
			}
		});
	}

	private void callLogin(int type, boolean isChangeLogin) {
		switch (type) {
		case 0:
			login_type = "qq";
			YSDKApi.login(ePlatform.QQ);
			break;
		case 1:
			login_type = "wx";
			YSDKApi.login(ePlatform.WX);
			break;
		}

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
	public void login(final Activity context, CommonSdkLoginInfo commonSdkLoginInfo) {
		this.mActivity = context;

		FLogger.d("登陆开始--");
		if (!isLogin) {
			isLogin = true;
		}
		isNoticeLogin = false;
		login_type = getLastLoginType();
		if (login_type.equals("")) {
			login_type = null;
		}
		FLogger.d("最后一次登陆type为 " + login_type);

		// context.startActivity(new Intent(context, YybActivity.class));
		// 清除票据
		// user = null;

		if (qqLoginDialog == null) {
			initLoginDialog(context);
		}
		/**
		 * 423尝试修改
		 */
//		UserLoginRet ret = new UserLoginRet();
//		YSDKApi.getLoginRecord(ret);
//
//		if (ret.ret != 0) {
			YSDKlogout();
//		}
		ePlatform platform = getPlatform();
		if (ePlatform.WX == platform) {
            // 如已登录直接进入相应模块视图
			ToastUtil.toastInfo(mActivity, "直接进入响应");
//            startModule();
        } else if (ePlatform.None == platform) {
                YSDKApi.login(ePlatform.WX);
            
//            startWaiting();
        }else{
            Log.d("fq","微信登录中~~~");
        }
//		if (ePlatform.None == platform) {
//			isAutoLogin = false;
//			qqLoginDialog.show();
//			FLogger.d("qqLoginDialog.show ");
//		} else if (ePlatform.QQ == platform) {
//			FLogger.d("自动登录，弹出自动登录窗口");
//			qqLoginDialog.showAutoLogin("QQ");
//		} else if (ePlatform.WX == platform) {
//			FLogger.d("自动登录，弹出自动登录窗口");
//			qqLoginDialog.showAutoLogin("微信");
//		}



	}

	@Override
	public void charge(Activity context, CommonSdkChargeInfo ChargeInfo) {
		this.mActivity = context;
//		rate = ChargeInfo.getRate();
		orderId = ChargeInfo.getOrder();

		Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
				context.getResources().getIdentifier("gowan_commonsdk_qq_picon", "drawable", context.getPackageName()));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] appResData = baos.toByteArray();

		if (appResData != null) {
			Log.d("commonsdk", "图片appResData != null");
		} else {
			Log.d("commonsdk", "图片appResData == null");
		}

//		String money = (ChargeInfo.getAmount() / ChargeInfo.getRate()) + "";
		String money = ChargeInfo.getMoney()+"";// 这里是购买多少游戏币
		// 对应腾讯后台的 货币分区id
		if (zoneid.equals("0")) {
			zoneid="1";
		}
//		String zoneId = PhoneInfoUtil.getSerVerId(context);
		FLogger.d("money = " + money);
		FLogger.d("zoneId = " + zoneid);
		// 1.大区id 2.充值数额 3.充值数额是否可改
		com.tencent.ysdk.api.YSDKApi.recharge(zoneid, money, false, appResData, ChargeInfo.getOrder(),
				new YSDKCallback());

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
	public void reLogin(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		FLogger.d("----relogin----");
		this.mActivity = activity;
		isNoticeLogin = false;
		// 清除票据
		YSDKlogout();
		login(activity, commonSdkLoginInfo);
	}

	@Override
	public boolean showPersonView(Activity activity) {
		return false;
	}

	@Override
	public void controlFlow(Activity context, boolean isShow) {
		this.mActivity = context;

	}

	@Override
	public void DoRelease(Activity activity) {
		FLogger.d("YSDKApi.onDestroy");
		YSDKApi.onDestroy(activity);
		// if (sdkuser != null && !TextUtils.isEmpty(sdkuser.uid)) {
		// try {
		// /*************** mta统计接口 *****************/
		// // 【时长统计】带有统计时长的自定义参数事件
		// StatService.trackCustomEndEvent(mActivity, "userlogout",
		// sdkuser.uid);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// sdkuser = null;
		if (qqLoginDialog != null) {
			qqLoginDialog.dismiss();
		}
		qqLoginDialog = null;
	}

	@Override
	public void submitExtendData(final Activity activity, final CommonSdkExtendData data) {

	}

	public boolean logout(Activity activity) {
		YSDKlogout();
		return true;
	}

	@Override
	public void getOderId(CommonSdkChargeInfo info, Activity context, CommonSDKHttpCallback httpCallback) {
		JSONObject dataJson = new JSONObject();
		try {
			UserLoginRet ret = new UserLoginRet();
			YSDKApi.getLoginRecord(ret);
			dataJson.put("appid", qq_appid);
			dataJson.put("openkey", ret.getAccessToken());
			dataJson.put("openid", ret.open_id);
			dataJson.put("pf", ret.pf);
			// dataJson.put("pfkey", user.pkey);
			dataJson.put("type", ysdkType);
			dataJson.put("saveNum", info.getMoney());//(info.getMoney() / 100 * info.getRate()) + "");
			dataJson.put("zoneid",zoneid);
			ePlatform platform = getPlatform();
			if (ePlatform.QQ == platform) {
				dataJson.put("login_type", "qq");
				// 微信时不用传paytoken
				dataJson.put("pay_token", ret.getPayToken());
			} else {
				dataJson.put("login_type", "wx");
			}

			// String sb = null;

			dataJson.put("pfkey", ret.pf_key);
			if (getChannelID().equals("yaowanqq") || getChannelID().equals("chmsdk")) {
				dataJson.put("sdk_type", "ysdk");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

//		return implCallback.getOrderId(dataJson, info);
	}

	void showLoginFail(int i) {
		implCallback.onLoginFail(-1);
	}

	/**
	 * 通知登录
	 */
	void noticeLogin() {
		if (isNoticeLogin) {
			FLogger.d("已通知登录，无须重复通知");
			return;
		}
		isNoticeLogin = true;
		// ResultNotify.ShowLoginSucessByTencens(mActivity, user,
		// StatConfig.getMid(mActivity), chanle, mBack);
		// implCallback.onLoginSuccessByTencen(sdkuser,
		// StatConfig.getMid(mActivity));
		UserLoginRet userLoginRet = new UserLoginRet();
		YSDKApi.getLoginRecord(userLoginRet);
		if (userLoginRet.ret != 0) {
			YSDKlogout();

		} else {
			implCallback.onLoginSuccess(userLoginRet.open_id, userLoginRet.open_id, loginJson, null, null);
		}

	}

	class YSDKCallback implements UserListener, BuglyListener, PayListener {

		public void OnLoginNotify(UserLoginRet ret) {
			FLogger.d(ret.toString());
			String accessToken = ret.getAccessToken();
			String payToken = ret.getPayToken();
			int flag = ret.flag;
			String open_id = ret.open_id;
			ToastUtil.toastInfo(mActivity, "有货了");
			FLogger.w( "OnLoginNotify accessToken=" + accessToken + " payToken=" + payToken + " flag=" + flag
					+ " open_id=" + open_id+" ret.ret="+ret.ret);
			if (qqLoginDialog != null) {
				qqLoginDialog.dismiss();
			}
			qqLoginDialog = null;

			Log.d("commonsdk", "YSDKCallback.OnLoginNotify ret.flag=" + ret.flag);
			// ToastUtil.toastInfo(mActivity, ret.flag + "");

			// if (!isLogin) {
			// Logger.d("没有调登录事件，就自动登录了，拦截登录回调");
			// return;
			// }
			switch (ret.flag) {

			case eFlag.Succ:
				UserLoginRet ret2 = new UserLoginRet();
				YSDKApi.getLoginRecord(ret2);
				if (ret.ret != BaseRet.RET_SUCC) {
					Log.d("commonsdk", "UserLogin error!!!");
					YSDKlogout();
					return;
				}
				// if (sdkuser == null) {
				// sdkuser = new QQuserInfo();
				// } else {
				// // 为了保证提供给游戏的票据的有效性，
				// // YSDK会在三种情况下（每次游戏启动、后台运行一分钟以上切换回前台、在前台持续运行30分钟以上）
				// // 触发自动验证票据有效性的逻辑
				// // 已经登陆过
				// // if (!TextUtils.isEmpty(user.uid)) {
				// // //Logger.d("----------刷新票据，结果如下---------");
				// // //Logger.d("ret.tostring=" + ret.toString());
				// // Log.d("commonsdk", "票据已刷新，不触发登陆回调");
				// // return;
				// // }
				// }
				Log.d("commonsdk", "登陆成功");
				loginJson = new JSONObject();

				// 微信要getLoginRecord一下获取AccessToken，不然怎么死的都不知道！
				// com.tencent.ysdk.api.YSDKApi.getLoginRecord(ret);
				if (ret.platform == ePlatform.PLATFORM_ID_QQ) {
					// QQ登录
					FLogger.d("QQ登录成功");
					// Logger.d("open_id = " + ret.open_id);
					// Logger.d("user_id = " + ret.user_id);
					// Logger.d("ret.tostring = " + ret.toString());
					// sdkuser.loginType = "qq";
					login_type = "qq";
					com.tencent.ysdk.api.YSDKApi.queryUserInfo(ePlatform.QQ);

					try {
						loginJson.put("appkey", qq_appkey);//PhoneInfoUtil.getAppkey(mActivity));
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else if (ret.platform == ePlatform.PLATFORM_ID_WX) {
					// 微信登录
					FLogger.d("微信登录成功");
					// Logger.d("open_id = " + ret.open_id);
					// Logger.d("user_id = " + ret.user_id);
					// Logger.d("ret.tostring = " + ret.toString());
					// sdkuser.loginType = "wx";
					login_type = "wx";
					com.tencent.ysdk.api.YSDKApi.queryUserInfo(ePlatform.WX);

					try {
						loginJson.put("appkey", wx_appkey);//PhoneInfoUtil.getQQGameData(mActivity)[3]);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				// sdkuser.accessToken = ret.getAccessToken();
				// sdkuser.payToken = ret.getPayToken();
				//
				// sdkuser.uid = ret.open_id;
				// sdkuser.pf = ret.pf;
				// sdkuser.pkey = ret.pf_key;
				// sdkuser.openid = ret.open_id;
				FLogger.d("openid: " + ret.open_id);
				try {
					// 登陆验证
					loginJson.put("appid", appId);
					loginJson.put("openid", ret.open_id);
					loginJson.put("openkey", ret.getAccessToken());
					loginJson.put("platform_api_version", 2);
					loginJson.put("type", ysdkType);
					loginJson.put("login_type", login_type);

				} catch (JSONException e) {
					e.printStackTrace();
				}
				// Log.d("commonsdk","登陆成功");
				// ResultNotify.ShowLoginSucessByTencens(mActivity, user,
				// StatConfig.getMid(mActivity), chanle, mBack);
				/*************** mta统计接口 *****************/
				// 【时长统计】带有统计时长的自定义参数事件
				// StatService.trackCustomBeginEvent(mActivity, "userlogin",
				// sdkuser.uid);

				if (qqLoginDialog != null && qqLoginDialog.isShowing()) {
					qqLoginDialog.dismiss();
				}

				// if (isAutoLogin) {
				// Logger.d("自动登录，弹出自动登录窗口");
				// if (qqLoginDialog == null) {
				// initLoginDialog(mActivity);
				// }
				// if (ret.platform == ePlatform.PLATFORM_ID_QQ)
				// qqLoginDialog.showAutoLogin("QQ");
				// else
				// qqLoginDialog.showAutoLogin("微信");
				// }else{
				// Logger.d("正常登录，发送回调");
				isAutoLogin = true;
				// ResultNotify.ShowLoginSucessByTencens(mActivity, user,
				// StatConfig.getMid(mActivity), chanle, mBack);

				if (!isLogin) {
					FLogger.d("没有调登录事件，就自动登录了，暂停登录回调");
				} else {
					noticeLogin();
				}

				// }

				JSONObject refreshDataJson = new JSONObject();
				try {
					refreshDataJson.put("user_id", ret.open_id);
					refreshDataJson.put("login_type", login_type);
					refreshDataJson.put("openkey", ret.getAccessToken());
					refreshDataJson.put("pay_token", ret.getPayToken());
					refreshDataJson.put("pfkey", ret.pf_key);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				final HashMap<String, String> mapData = new HashMap<String, String>();
				mapData.put("data", refreshDataJson.toString());
				mapData.put("user_id", ret.open_id);
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// ApiClient.getInstance(mActivity).refreshTokenByYSDK(
						// mapData);
						implCallback.refreshToken(mapData);
					}
				}).start();

				break;
			// 游戏逻辑，对登陆失败情况分别进行处理
			case eFlag.QQ_UserCancel:
			case eFlag.QQ_NetworkErr:
			case eFlag.QQ_LoginFail:
			case eFlag.WX_UserCancel:
			case eFlag.WX_LoginFail:
			case eFlag.WX_UserDeny:

				Log.d("commonsdk", "微信 or qq 登陆异常");
				// 注销
				YSDKlogout();
				showLoginFail(-1);
				// 触发登出回调游戏马上调起登录窗口没有用，所以延迟调用
				new Thread(new Runnable() {
					@Override
					public void run() {
						for (int i = 0; i < 10; i++) {
							if (qqIsShow) {
								qqIsShow = false;
								break;
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							FLogger.d("延迟1秒，触发登录，次数" + (i + 1));
							// 微信取消登陆后，不进行登陆操作
							mHandler.sendEmptyMessage(1);
						}

					}
				}).start();

				break;

			case eFlag.Login_TokenInvalid:
				// Log.d("commonsdk", ret.toString());
				Log.d("commonsdk", "Login_TokenInvalid");
				// 注销
				YSDKlogout();

				mHandler.sendEmptyMessage(1);

				break;
			default:
				Log.d("commonsdk", "ysdk login default");
				// 注销
				YSDKlogout();
				// showLoginFail(-1);
				mHandler.sendEmptyMessage(1);
				break;
			}
		}

		public void OnWakeupNotify(WakeupRet ret) {
			FLogger.d("OnWakeupNotify");
			// Logger.d("called");
			FLogger.d(ret.toString() + ":flag:" + ret.flag);
			// Logger.d("msg:" + ret.msg);
			// Logger.d("platform:" + ret.platform);

			// TODO GAME 游戏需要在这里增加处理异账号的逻辑
			if (eFlag.Wakeup_YSDKLogining == ret.flag) {
				// 用拉起的账号登录，登录结果在OnLoginNotify()中回调
			} else if (ret.flag == eFlag.Wakeup_NeedUserSelectAccount) {
				// 异账号时，游戏需要弹出提示框让用户选择需要登录的账号
				mBack.ReloginOnFinish("切换账号", 0);
			} else if (ret.flag == eFlag.Wakeup_NeedUserLogin) {
				// 没有有效的票据，登出游戏让用户重新登录
				mBack.ReloginOnFinish("切换账号", 0);
			} else {
				mBack.ReloginOnFinish("切换账号", 0);
			}

		}

		@Override
		public void OnRelationNotify(UserRelationRet relationRet) {
			FLogger.d("OnRelationNotify.relationRet = " + relationRet.toString());

		}

		@Override
		public String OnCrashExtMessageNotify() {
			// 此处游戏补充crash时上报的额外信息
			Log.d("commonsdk", String.format(Locale.CHINA, "OnCrashExtMessageNotify called"));
			Date nowTime = new Date();
			SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			return "new Upload extra crashing message for bugly on " + time.format(nowTime);
		}

		@Override
		public byte[] OnCrashExtDataNotify() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void OnPayNotify(PayRet ret) {
			FLogger.d("OnPayNotify ret = " + ret.toString());
			if (PayRet.RET_SUCC == ret.ret) {
				// 支付流程成功
				switch (ret.payState) {
				// 支付成功
				case PayRet.PAYSTATE_PAYSUCC:
					FLogger.d("UnipayPlugAPI",
							"UnipayCallBack \n" + "\nresultCode = " + PayRet.RET_SUCC + "\npayChannel = "
									+ ret.payChannel + "\npayState = " + ret.payState + "\nproviderState = "
									+ ret.provideState + "\nsaveType = " + ret.extendInfo);
					// retCode = ret.resultCode;
					JSONObject result = new JSONObject();
					try {
						result.put("resultCode", PayRet.PAYSTATE_PAYSUCC);
						result.put("payChannel", ret.payChannel);
						result.put("payState", ret.payState);
						result.put("providerState", ret.provideState);
						result.put("saveNum", ret.realSaveNum);
						result.put("resultMsg", ret.msg);
						result.put("extendInfo", ret.extendInfo);
						result.put("order_id", orderId);
						result.put("amount", ret.realSaveNum );/// rate);
						result.put("appid", appId);
						UserLoginRet login_ret = new UserLoginRet();
						YSDKApi.getLoginRecord(login_ret);
						result.put("openkey", login_ret.getAccessToken());
						result.put("openid", login_ret.open_id);
						result.put("pf", login_ret.pf);
						result.put("pfkey", login_ret.pf_key);
						result.put("type", ysdkType);

						// 新版ysdk，多加这个参数sdk_type
						result.put("sdk_type", "ysdk");
						if (ret.platform == ePlatform.PLATFORM_ID_QQ) {
							result.put("login_type", "qq");
							// 微信时不用传paytoken
							result.put("pay_token", login_ret.getPayToken());
						} else {
							result.put("login_type", "wx");
						}
						result.put("zoneid", zoneid);//PhoneInfoUtil.getServiceID(mActivity));
						// 4.1.8融合，新加参数
//						result.put("game_version", PhoneInfoUtil.getGameVersion(mActivity));
//						result.put("platform_version", getVersionName());// 第三方平台版本号
//						result.put("version", Constants.SDK_Version);
						// 2 ysdk版本，主要增加全参数加密签名验证
						result.put("platform_api_version", "2");
						result.put("income_way", "qq");

						// ApiClient.getInstance(mActivity).putYSDKPaySign(result);
						implCallback.getPaySign(result,new CommonSDKHttpCallback() {
							
							@Override
							public void onResult(ResultInfo resultInfo, String msg) {
								// TODO Auto-generated method stub
								
							}
						});
					} catch (JSONException e) {
						e.printStackTrace();

					}

					mHandler.sendEmptyMessage(9000);
					implCallback.noticeOrder(result);

					break;
				// 取消支付
				case PayRet.PAYSTATE_PAYCANCEL:
					// 支付结果未知
				case PayRet.PAYSTATE_PAYUNKOWN:
					// 支付失败
				case PayRet.PAYSTATE_PAYERROR:
					mHandler.sendEmptyMessage(9001);
					break;
				}
			} else {
				switch (ret.flag) {
				case eFlag.Pay_User_Cancle:
					// 用户取消支付
					FLogger.d("OnPayNotify 用户取消支付 = " + ret.toString());
					break;
				case eFlag.Pay_Param_Error:
					FLogger.d("OnPayNotify 支付失败，参数错误 = " + ret.toString());
					break;
				case eFlag.Error:
				default:
					FLogger.d("OnPayNotify 支付异常 = " + ret.toString());
					break;
				}
				implCallback.onPayFinish(-2);
			}
		}
	}

	boolean qqIsShow = false;
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				if (qqLoginDialog != null && qqIsShow == false) {
					qqLoginDialog.show();
					FLogger.d("登录窗口弹出");
					qqIsShow = true;
				}
			}
			// 处理充值回调
			if (msg.what == 9000) {
				implCallback.onPayFinish(0);
			} else if (msg.what == 9001) {
				implCallback.onPayFinish(-2);
			}
		};
	};

	private void YSDKlogout() {
		// if (sdkuser != null) {
		// 登出
		YSDKApi.logout();
		// }
		// sdkuser = null;
		login_type = "";
		setLastLoginType("");
		Log.i("commonsdk", "YSDKApi.logout()");
		isAutoLogin = false;
	}

	private void initsdkParam(Activity context) {
		try {

			InputStream is = context.getAssets().open("ysdkconf.ini");
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			String line = null;
			// Log.i(TAG, "!read ysdkconf.ini ");
			while ((line = br.readLine()) != null) {
				// Log.i(TAG, "!" + line + "!");

				// appid
				if (line.trim().contains("QQ_APP_ID")) {
					appId = line.trim().split("=")[1];
				}

				// 测试环境
				if (line.trim().equals(";YSDK_URL=https://ysdk.qq.com")) {
					ysdkType = "test";
				}
				// 正式环境
				else if (line.trim().equals("YSDK_URL=https://ysdk.qq.com")) {
					ysdkType = "online";
				}
			}
			FLogger.d("ysdk的 appId = " + appId);
			FLogger.d("ysdk的环境是 " + ysdkType);
			if (ysdkType == null) {
				Log.e("commonsdk", "ysdkconf.ini 配置错误，请检查");
			}
		} catch (IOException e) {
			Log.e("commonsdk", "not find ysdkconf.ini on assets");
			throw new RuntimeException(e);
		}
	}

	protected String getLastLoginType() {
		SharedPreferences share = mActivity.getSharedPreferences("userinfo", mActivity.MODE_PRIVATE);
		return share.getString("logintype", "");
	}

	protected void setLastLoginType(String loginType) {
		SharedPreferences share = mActivity.getSharedPreferences("userinfo", mActivity.MODE_PRIVATE);
		SharedPreferences.Editor edit = share.edit();
		edit.putString("logintype", loginType);
		edit.commit(); // 保存数据信息
	}

	@Override
	public void onStart(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRestart(Activity activity) {
		FLogger.d("YSDKApi.onRestart");
		YSDKApi.onRestart(activity);
	}

	@Override
	public void onResume(Activity activity) {
		FLogger.d("YSDKApi.onResume");
		YSDKApi.onResume(activity);
		// StatService.onResume(activity);
	}

	@Override
	public void onPause(Activity activity) {
		FLogger.d("YSDKApi.onPause");
		YSDKApi.onPause(activity);
		// StatService.onPause(activity);
	}

	@Override
	public void onStop(Activity activity) {
		FLogger.d("YSDKApi.onStop");
		YSDKApi.onStop(activity);
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		FLogger.d("YSDKApi.onActivityResult");
		YSDKApi.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onNewIntent(Activity activity, Intent intent) {
		// YSDK的handleIntent方法，游戏在主activity的onNewIntent调用
		FLogger.d("YSDKApi.onNewIntent");
		YSDKApi.handleIntent(intent);
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return CommonBackLoginInfo.getInstance().userId;
	}

	@Override
	public String getVersionName() {
		String version = "1.4.5";
		return version;
	}

	@Override
	public String getChannelID() {

		return "应用宝";
	}

	// 应用宝
	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return false;
	}

	private int getplatformChanleId(Activity mActivity) {
		if (chanleId == 0) {
			chanleId = 5;//PhoneInfoUtil.getplatformChanleId(mActivity);
		}
		return chanleId;
	}

	@Override
	public HashMap<String, String> getDataMap(Activity activity, String ext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JSONObject getDataJson(Activity activity, String ext) {
		// if (sdkuser == null) {
		// return null;
		// }
		JSONObject jsonObject = new JSONObject();
		try {
			UserLoginRet ret = new UserLoginRet();
			int platform = YSDKApi.getLoginRecord(ret);
			jsonObject.put("openkey", ret.getAccessToken());
			jsonObject.put("pay_token", ret.getPayToken());
			jsonObject.put("openid", ret.open_id);
			jsonObject.put("pf", ret.pf);
			jsonObject.put("pfkey", ret.pf_key);
		} catch (Exception e) {

		}
		return jsonObject;
	}

	@Override
	public void roleMsg(final Activity activity, final CommonSdkExtendData data) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				try {
					UserLoginRet ret = new UserLoginRet();
					YSDKApi.getLoginRecord(ret);

					FLogger.d("appid" + appId + " openid:" + ret.open_id);
					ApiClient.getInstance().roleMSG(data, appId, ret.open_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop();
			}
		}).start();
	}

	@Override
	public void roleTask(final Activity activity, final CommonSdkExtendData data) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				try {
					UserLoginRet ret = new UserLoginRet();
					YSDKApi.getLoginRecord(ret);
					ApiClient.getInstance().roleTask(data, appId, ret.open_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop();
			}
		}).start();
	}

	@Override
	public void roleHonor(final Activity activity, final CommonSdkExtendData data) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Looper.prepare();
				try {
					UserLoginRet ret = new UserLoginRet();
					YSDKApi.getLoginRecord(ret);
					ApiClient.getInstance().roleHonor(data, appId, ret.open_id);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Looper.loop();
			}
		}).start();
	}

	/**
	 * 应用宝社区功能
	 */
	@Override
	public void openImmersive() {
		// TODO Auto-generated method stub
		// 要在主线程上调用
		mActivity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ImmersiveIconApi.getInstance().performFeature("bbs");

			}
		});

	}

	private static Activity splashActivity;
	
	//应用宝由于要处理拉起游戏问题 不能设置gowan 闪屏
	@Override
	public void initWelcomeActivity(Activity activity, HyGameCallBack callback) {
		FLogger.d(Global.INNER_TAG,
				"initWelcomeActivity");
		if (null != splashActivity && !splashActivity.equals(activity)) {
			FLogger.d(Global.INNER_TAG,
					"Warning!Reduplicate game activity was detected.Activity will finish immediately.");
			YSDKApi.handleIntent(activity.getIntent());
			activity.finish();
			return;
		} else {
			FLogger.d(Global.INNER_TAG,
					"initWelcomeActivity else");
			YSDKApi.onCreate(activity);
			// TODO GAME 处理游戏被拉起的情况
			YSDKApi.handleIntent(activity.getIntent());
		}
		splashActivity = activity;
		callback.onSuccess();
//		callback.onSuccess("闪屏成功");---old
	}
	

	@Override
	public void share(String title, String content) {
		ShareApi.getInstance().share(capForBitmap(), title, content, "分享");
	}
	
	//当前界面
		private Bitmap capForBitmap(){
				View view = mActivity.getWindow().getDecorView();
				view.setDrawingCacheEnabled(true);
				view.buildDrawingCache();
				Rect rect = new Rect();
				view.getWindowVisibleDisplayFrame(rect);
				int statusBarHeight = rect.top;
				WindowManager windowManager = mActivity.getWindowManager();
				DisplayMetrics outMetrics = new DisplayMetrics();
				windowManager.getDefaultDisplay().getMetrics(outMetrics);
				int width = outMetrics.widthPixels;
				int height = outMetrics.heightPixels;
				Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache(), 0, statusBarHeight, width,
						height - statusBarHeight);
				view.destroyDrawingCache();
				view.setDrawingCacheEnabled(false);
			return createBitmap;
		}
		/**
		 * 判断是否已安装
		 * 
		 * @param ctx
		 * @param pkgName
		 * @return
		 */
		public static boolean isPackageInstalled(Context ctx, String pkgName) {
			if (pkgName == null || "".equals(pkgName.trim())) {
				return false;
			}
			try {
				ctx.getPackageManager().getPackageInfo(pkgName.trim(),
						PackageManager.GET_GIDS);
			} catch (NameNotFoundException e) {
				return false;
			}
			return true;
		}

		@Override
		public String getChannelName() {
			// TODO Auto-generated method stub
			return "应用宝";
		}
}
