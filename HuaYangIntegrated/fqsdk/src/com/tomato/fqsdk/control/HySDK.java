package com.tomato.fqsdk.control;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.tomato.fqsdk.HyLoginActivity;
import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.data.CLData;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.models.HyRoleData;
import com.tomato.fqsdk.models.ControlConfig;
import com.tomato.fqsdk.models.HyInitInfo;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.HJGameDBHelper;
import com.tomato.fqsdk.utils.HyAppUtils;
import com.tomato.fqsdk.utils.NetWorkUtil;
import com.tomato.fqsdk.utils.SpUtils;
import com.tomato.fqsdk.utils.Tools;
import com.tomato.fqsdk.utils.HJGameDBHelper.HJGame;

public class HySDK {
	private static HySDK clsdk;
	private CLControlCenter controlCenter;
	public static Context context;
	public String path;
	public boolean LoginToast = false;
	private boolean HJisInit = false;
	private final int install = 2;
	private final int activate = 1;
	private HJGameDBHelper gameDBHelper;
	
	public void openLoginToast(boolean loginToast) {
		LoginToast = loginToast;
	}
	
	

	public static HySDK getInstance() {
		if (clsdk == null) {
			synchronized (HySDK.class) {
				if (clsdk == null) {
					clsdk = new HySDK();
				}
			}
		}

		return clsdk;

	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle.getInt("type") == activate) {
				storeGame(bundle.getString("channel"), bundle.getString("gameid"));
			}
			switch (msg.what) {
			case install:
				if (!SpUtils.getStringValue(context, "install").equals(CLCommon.YES)) {
					// Log.e("CL", "install");
					SpUtils.setStringValue(context, "install", CLCommon.YES);
					PostUserInfo.HJinstall(context);
				}
				break;
			default:
				break;
			}
		};
	};

	public String hyGetChannel(Activity activity) {
		String channel;
		try {
			ApplicationInfo appInfo = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), 128);
			channel = appInfo.metaData.get("hychannel875cn") + "";
		} catch (Exception e) {
			channel = "1";
		}
		return channel;
	}

	public void CLSetVisibility() {
		BaseActivity.setvisibility = 1;
	}

	public void HyInitSDK(Activity activity, HyInitInfo initInfo,FQSdkCallBack iBack) {
		boolean isDebug = initInfo.isDebug();
		FLogger.init(isDebug, "fq");
		
//		try {
//			if (!Tools.initInfoCheck(initInfo)) {
//				iBack.onInitFinish(0, "初始化失败");
//				return;
//			}
//			
//		} catch (InvocationTargetException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} catch (IllegalAccessException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}

		String appid = initInfo.getGameId();
		String appversion = initInfo.getGameVersion();
		

//		CrashHandler crashHandler = CrashHandler.getInstance();
//		crashHandler.init(activity);
		
		context = activity.getApplicationContext();
		controlCenter = CLControlCenter.getInstance();

		String appkey = HyAppUtils.hyGetAppKey(context);
//		if (TextUtils.isEmpty(appkey)) {
//			iBack.onInitFinish(0, "初始化失败，配置文件缺少AppKey");
//			return;
//		}
		
		ControlConfig config = new ControlConfig.Builder(activity).setAppId(appid).setChannel(Tools.getChannelId(activity)+"")
				.setAppVersion(appversion).build();
		// debug log
		
		controlCenter.setConfig(config);
		controlCenter.init(activity);
		// controlCenter.setCurUser(new GameUser());
//		controlCenter.setGameicon(GameIcon);
//		controlCenter.setGamename(GameName);
		HJisInit = true;
		gameDBHelper = HJGameDBHelper.getInstance(activity);
		CheckInstall(Tools.getChannelId(activity), appid);
		if (NetWorkUtil.IsNetWorkEnable(context)) {
			CLData hjData = new CLData();
			hjData.getFailureAllData();
		}
		
		iBack.initOnFinish(1, "初始化成功");
	}

	private void CheckInstall(String channel, String gameid) {
		handler.sendEmptyMessage(install);
		Message msg = new Message();
		Bundle bundle = new Bundle();
		bundle.putString("channel", channel);
		bundle.putString("gameid", gameid);
		bundle.putInt("type", activate);
		msg.setData(bundle);
		msg.what = activate;
		handler.sendMessage(msg);
		// Tools.readFile(filePath)
	}

	public void HyShowLoginView(Activity activity, FQSdkCallBack iBack) {
		if (HyLoginActivity.LOGINACTIVITY != 1) {
			if (!HJisInit) {
				com.tomato.fqsdk.fqutils.FLogger.d("----HySDK Hasn't been initialized----");
				return;
			}
			controlCenter.HJstartLogin(activity, iBack);
		}
	}

	/**
	 * 
	 * 
	 * @param context
	 * @param hjPayRequest
	 * @param paramOnPayFinishedListener
	 */

	public void HyShowPayView(Activity context, HyPayInfo hyPayInfo) {
		if (!HJisInit) {
			FLogger.d("----HYSDK Hasn't been initialized----");
			return;
		}
		controlCenter.HJstartPay(context, hyPayInfo);
	}

	/**
	 * 提交运营数据 fsp 2018年9月13日
	 * 
	 * @param roleData
	 */
	public void HySubmitRoleData(HyRoleData roleData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", roleData.getServerId());
		map.put("role_id", roleData.getRoleId());
		map.put("user_id", roleData.getUser_id());
		map.put("role_name", roleData.getRoleName());
		map.put("level", roleData.getRoleLevel());
		map.put("server_name", roleData.getServceName());
		map.put("party_name", roleData.getParty_name());
		map.put("balance", roleData.getUserMoney());
		map.put("vip_level", roleData.getVipLevel());
		map.put("role_create_time", roleData.getRoleCTime());
		CLData.submit("login", map);
	}

	public void HySubmitDataRoleCreate(HyRoleData roleData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", roleData.getServerId());
		map.put("role_id", roleData.getRoleId());
		map.put("user_id", roleData.getUser_id());
		map.put("role_name", roleData.getRoleName());
		map.put("server_name", roleData.getServceName());
		map.put("party_name", roleData.getParty_name());
		map.put("balance", roleData.getUserMoney());
		map.put("vip_level", roleData.getVipLevel());
		map.put("role_create_time", roleData.getRoleCTime());
		CLData.submit("register", map);
	}

	public void HySubmitDataRoleLevelUpdate(HyRoleData roleData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", roleData.getServerId());
		map.put("role_id", roleData.getRoleId());
		map.put("user_id", roleData.getUser_id());
		map.put("role_name", roleData.getRoleName());
		map.put("level", roleData.getRoleLevel());
		map.put("level_begin", roleData.getRoleLevel_begin());
		map.put("server_name", roleData.getServceName());
		map.put("party_name", roleData.getParty_name());
		map.put("balance", roleData.getUserMoney());
		map.put("vip_level", roleData.getVipLevel());
		map.put("role_create_time", roleData.getRoleCTime());
		CLData.submit("level_up", map);
	}
	public void HySubmitDataLogout(HyRoleData roleData) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("sid", roleData.getServerId());
		map.put("role_id", roleData.getRoleId());
		map.put("user_id", roleData.getUser_id());
		map.put("role_name", roleData.getRoleName());
		map.put("level", roleData.getRoleLevel());
		map.put("server_name", roleData.getServceName());
		map.put("party_name", roleData.getParty_name());
		map.put("balance", roleData.getUserMoney());
		map.put("vip_level", roleData.getVipLevel());
		map.put("role_create_time", roleData.getRoleCTime());
		CLData.submit("login_out", map);
	}
	public void HySubmitDataOther(HyRoleData roleData,String behavior,Map<String, Object> data_map) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(data_map);
		map.put("sid", roleData.getServerId());
		map.put("role_id", roleData.getRoleId());
		map.put("user_id", roleData.getUser_id());
		map.put("role_name", roleData.getRoleName());
		map.put("level", roleData.getRoleLevel());
		map.put("server_name", roleData.getServceName());
		map.put("party_name", roleData.getParty_name());
		map.put("balance", roleData.getUserMoney());
		map.put("vip_level", roleData.getVipLevel());
		map.put("role_create_time", roleData.getRoleCTime());
		CLData.submit(behavior, map);
	}
	public void HyCleanUser() {
		if (!HJisInit) {
			FLogger.d("----HYSDK Hasn't been initialized----");
			return;
		}
		controlCenter.CleanUser(context);
	}

	public String HyGetCurrentUserID() {
		String uid = HyLoginResult.getInstance().getUid();
		if (TextUtils.isEmpty(uid)) {
			uid = "";
		}
		return uid;
	}
//	public void HyThirdLogin(Activity activity,int i){
//		SHARE_MEDIA iMedia = null;
//		switch (i) {
//		case 0:
//			iMedia=SHARE_MEDIA.WEIXIN;
//			break;
//		case 1:
//			iMedia=SHARE_MEDIA.QQ;
//			break;
//		default:
//			break;
//		}
//		 final boolean isauth=UMShareAPI.get(activity).isAuthorize(activity,iMedia);
//		 if (isauth) {
//             UMShareAPI.get(activity).deleteOauth(activity, iMedia, authListener);
//         } else {
//             UMShareAPI.get(activity).doOauthVerify(activity, iMedia, authListener);
//         }
//
//	}
//	public void HyShare(Activity activity){
//		new ShareAction(activity)
//		.setPlatform(SHARE_MEDIA.WEIXIN)//传入平台
//		.withText("hello")//分享内容
//		.setCallback(shareListener)//回调监听器
//		.share();
//	}
//	 UMAuthListener authListener = new UMAuthListener() {
//	        @Override
//	        public void onStart(SHARE_MEDIA platform) {
//	        }
//
//	        @Override
//	        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
//	           HyToast("成功了");
//	        }
//
//	        @Override
//	        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
//	        	HyToast("失败");
//	        }
//
//	        @Override
//	        public void onCancel(SHARE_MEDIA platform, int action) {
//	        	HyToast("取消了");
//	        }
//	    };
//
//	 private UMShareListener shareListener = new UMShareListener() {
//	        /**
//	         * @descrption 分享开始的回调
//	         * @param platform 平台类型
//	         */
//	        @Override
//	        public void onStart(SHARE_MEDIA platform) {
//
//	        }
//
//	        /**
//	         * @descrption 分享成功的回调
//	         * @param platform 平台类型
//	         */
//	        @Override
//	        public void onResult(SHARE_MEDIA platform) {
//	        	HyToast("成功了");
//	        }
//
//	        /**
//	         * @descrption 分享失败的回调
//	         * @param platform 平台类型
//	         * @param t 错误原因
//	         */
//	        @Override
//	        public void onError(SHARE_MEDIA platform, Throwable t) {
//	        	HyToast("失败");
//	        }
//
//	        /**
//	         * @descrption 分享取消的回调
//	         * @param platform 平台类型
//	         */
//	        @Override
//	        public void onCancel(SHARE_MEDIA platform) {
//	        	HyToast("取消");
//
//	        }
//	    };
	private void storeGame(final String channel, final String gameid) {

		try {
			gameDBHelper.open();
			if (gameDBHelper.isOneExist(channel, gameid)) {

			} else {
				gameDBHelper.open();
				HJGame game = gameDBHelper.new HJGame();
				game.setmChannel(channel);
				game.setmGameId(gameid);
				gameDBHelper.insert(game);
				gameDBHelper.close();
				PostUserInfo.HJactivate(context);

			}
		} catch (Exception e) {
			gameDBHelper.close();
		}

		gameDBHelper.close();

	}

	public static void HyToast(String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}
	
	public void getApplication(Application application) {
		context=application.getApplicationContext();
		FindResHelper.init(application);
	}
	 public static Context getAppContext() { 
         return HySDK.context; 
     } 
}