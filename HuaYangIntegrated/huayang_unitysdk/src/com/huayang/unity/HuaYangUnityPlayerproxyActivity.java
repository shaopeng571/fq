package com.huayang.unity;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.present.HySDKManager;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.unity3d.player.UnityPlayer;

public abstract class HuaYangUnityPlayerproxyActivity extends com.unity3d.player.UnityPlayerActivity
		implements Callback {

	private static final String TAG = "fq_unity";

	private static final int MSG_LOGIN = 101;
	private static final int MSG_LOGOUT = 102;
	private static final int MSG_PAY = 103;
	private static final int MSG_EXIT = 104;
	private static final int MSG_ROLEINFO = 105;
	private static final int MSG_ROLECTEATEINFO = 106;
	private static final int MSG_ROLELEVELUPINFO = 107;
	private static final int MSG_ROLELOGINOUTINFO = 108;
//	private static final int MSG_EXTEND_FUNC = 106;
//	private static final int MSG_EXTEND_CALLPLUGIN = 107;
//	private static final int MSG_EXTEND_FUNC_SHARE = 108;

	private static final int INIT_SUCCESS = 1;
	private static final int INIT_FAILED = -1;
	private static final int INIT_DEFAULT = 0;


	private String gameObjectName;

	private int initState = INIT_DEFAULT;

	private String mInitMsg = "";

	Handler mHandler = new Handler(this);

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		doInit();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		HySDKManager.getInstance().onNewIntent(intent);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// HySDKManager.getInstance().onWindowFocusChanged();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		HySDKManager.getInstance().onStart(this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		HySDKManager.getInstance().onRestart(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		HySDKManager.getInstance().onResume(this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		HySDKManager.getInstance().onPause(this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		HySDKManager.getInstance().onStop(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		HySDKManager.getInstance().onDestroy(this);
		System.exit(0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		HySDKManager.getInstance().onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		HySDKManager.getInstance().onActivityResult(requestCode, resultCode,
				data);
	}
/**
 * 初始化
 */
	public void doInit() {
		
		CommonSdkInitInfo initInfo = new CommonSdkInitInfo();
		initInfo.setGameVersion(ChannelConfigUtil.getMetaMsg(this, "HyGame_GameVersion").toString()==null ? "":ChannelConfigUtil.getMetaMsg(this, "HyGame_GameVersion").toString());
		
		initInfo.setDebug((ChannelConfigUtil.getMetaMsg(this, "HyGame_IsDebug").toString()).equals("true")? false:true);
		HySDKManager.getInstance().initCommonSdk(this, initInfo, sdkcallback);

	}

	// ------------------------------------------------------------------------------------
	// -------------------------------------------------------------------------------unity
	// interface
	// ------------------------------------------------------------------------------------

	public void requestLogin() {
		mHandler.sendEmptyMessage(MSG_LOGIN);

	}

	public void requestLogout() {
		mHandler.sendEmptyMessage(MSG_LOGOUT);
	}

	public void requestPay(String goodsId, String goodsName, String goodsDesc,String goodsType, String gameCoin,String quantifier, String cpOrderId,
			 String extraParams, String money, String serverName,
			String serverId, String roleName, String roleId, String vipLevel, String roleLevel
			) {

		CommonSdkChargeInfo hyPayInfo = new CommonSdkChargeInfo();
		hyPayInfo.setCp_order_id(cpOrderId);
		hyPayInfo.setRole_id(roleId);
		hyPayInfo.setRole_name(roleName);
		hyPayInfo.setRole_level(roleLevel);
		hyPayInfo.setVip_level(vipLevel);
		
		hyPayInfo.setServer_id(serverId);
		hyPayInfo.setServer_name(serverName);
		hyPayInfo.setMoney(Double.parseDouble(money));
		
		hyPayInfo.setGoods_type(goodsType);
		hyPayInfo.setGoods_id(goodsId);
		hyPayInfo.setGoods_name(goodsName);
		hyPayInfo.setGoods_count(Integer.valueOf(quantifier));
		hyPayInfo.setGoods_desc(goodsDesc);
		
		hyPayInfo.setGame_coin(gameCoin);
		hyPayInfo.setExtra(extraParams);//游戏cp 私有字段，可传
		
		Message msg = mHandler.obtainMessage(MSG_PAY);
		msg.obj = hyPayInfo;
		msg.sendToTarget();

	}

	public void requestExit() {
		mHandler.sendEmptyMessage(MSG_EXIT);
	}



	public String getUserId() {
		if (HySDKManager.getInstance().getCurrentUserId()!= null) {
			Log.e(TAG, "userId->" + HySDKManager.getInstance().getCurrentUserId());
			return HySDKManager.getInstance().getCurrentUserId();
		} else {
			Log.e(TAG, "user is null");
			return null;
		}

	}
//requestUpdateRole
	public void requestSendRoleData(String roleId, String roleName, String roleLevel, String serverId,
			String serverName, String userMoney, String vipLevel, String roleCreateTime, String partyName,
			String uid) {
		
		CommonSdkExtendData data = new CommonSdkExtendData();
		data.setRoleId(roleId);// 角色id
		data.setRoleName(roleName);// 角色名
		data.setRoleLevel(roleLevel);// 角色等级
		data.setServerId(serverId);// 所在服id
		data.setServerName(serverName);
		data.setUserMoney(userMoney);
		data.setVipLevel(vipLevel);
		data.setRoleCTime(roleCreateTime);
		data.setParty_name(partyName);
		data.setUser_id(uid);
		HySDKManager.getInstance().sendExtendData(this, data);
		
	}
	public void requestSendRoleCreateData(String roleId, String roleName, String roleLevel, String serverId,
			String serverName, String userMoney, String vipLevel, String roleCreateTime, String partyName,
			String uid) {
		
		CommonSdkExtendData data = new CommonSdkExtendData();
		data.setRoleId(roleId);// 角色id
		data.setRoleName(roleName);// 角色名
		data.setRoleLevel(roleLevel);// 角色等级
		data.setServerId(serverId);// 所在服id
		data.setServerName(serverName);
		data.setUserMoney(userMoney);
		data.setVipLevel(vipLevel);
		data.setRoleCTime(roleCreateTime);
		data.setParty_name(partyName);
		data.setUser_id(uid);
		HySDKManager.getInstance().sendExtendDataRoleCreate(this, data);
	}
	public void requestSendRoleLevelUpData(String roleId, String roleName, String roleLevel, String serverId,
			String serverName, String userMoney, String vipLevel, String roleCreateTime, String partyName,
			String uid,String roleLevel_begin) {
		
		CommonSdkExtendData data = new CommonSdkExtendData();
		data.setRoleId(roleId);// 角色id
		data.setRoleName(roleName);// 角色名
		data.setRoleLevel(roleLevel);// 角色等级
		data.setServerId(serverId);// 所在服id
		data.setServerName(serverName);
		data.setUserMoney(userMoney);
		data.setVipLevel(vipLevel);
		data.setRoleLevel_begin(roleLevel_begin);//升级前等级
		data.setRoleCTime(roleCreateTime);
		data.setParty_name(partyName);
		data.setUser_id(uid);
		HySDKManager.getInstance().sendExtendDataRoleLevelUpdate(this, data);
	}
	public void requestSendRoleLoginOutData(String roleId, String roleName, String roleLevel, String serverId,
			String serverName, String userMoney, String vipLevel, String roleCreateTime, String partyName,
			String uid) {
		
		CommonSdkExtendData data = new CommonSdkExtendData();
		data.setRoleId(roleId);// 角色id
		data.setRoleName(roleName);// 角色名
		data.setRoleLevel(roleLevel);// 角色等级
		data.setServerId(serverId);// 所在服id
		data.setServerName(serverName);
		data.setUserMoney(userMoney);
		data.setVipLevel(vipLevel);
		data.setRoleCTime(roleCreateTime);
		data.setParty_name(partyName);
		data.setUser_id(uid);
		HySDKManager.getInstance().sendExtendDataRoleLogout(this, data);
	}
//	public int callFunc(int funcType) {
//		if (isFuncSupport(funcType)) {
//
//			Message msg = mHandler.obtainMessage(MSG_EXTEND_FUNC);
//			msg.arg1 = funcType;
//			msg.sendToTarget();
//
//			return 1;
//		} else {
//			return 0;
//		}
//	}

//	public boolean isFuncSupport(int funcType) {
//
//		return Extend.getInstance().isFunctionSupported(funcType);
//	}

	public void setUnityGameObjectName(String gameObjectName) {
		this.gameObjectName = gameObjectName;
		Log.d("fq", "gameObjectName=" + gameObjectName);
		switch (initState) {
		case INIT_SUCCESS:
			callUnityFunc("onInitSuccess", new JSONObject().toString());
			break;
		case INIT_FAILED:
			JSONObject json = new JSONObject();
			try {

				json.put("msg", mInitMsg);

			} catch (Exception e) {
				e.printStackTrace();
			}
			callUnityFunc("onInitFailed", new JSONObject().toString());
			break;

		default:
			break;
		}
		initState = INIT_DEFAULT;

	}



	public boolean isChannelHasExitDialog() {
		return HySDKManager.getInstance().hasExitView(this);
	}

	public void exitGame() {
		Log.d("fq", "exitGame()");
		this.finish();
		System.exit(0);
	}

	// ------------------------------------------------------------------------------------
	// ------------------------------------------------------------------------------------
	// notifiers
	// ------------------------------------------------------------------------------------
	

	


	public void callUnityFunc(String funcName, String paramStr) {
		if (TextUtils.isEmpty(gameObjectName)) {
			Log.e(TAG, "gameObject is null, please set gameObject first");
			return;
		}

		UnityPlayer.UnitySendMessage(gameObjectName, funcName, paramStr);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_LOGIN:

			Log.e(TAG, "login");
			HySDKManager.getInstance().showLoginView(HuaYangUnityPlayerproxyActivity.this,null);
			break;
		case MSG_LOGOUT:

			Log.e(TAG, "logout");

			HySDKManager.getInstance().logout();
			break;
		case MSG_PAY: {

			Log.e(TAG, "pay");

			CommonSdkChargeInfo hyPayInfo = (CommonSdkChargeInfo) msg.obj;
			HySDKManager.getInstance().showChargeView(this, hyPayInfo);
			break;
		}
		case MSG_EXIT: {

			Log.e(TAG, "exit");
			if (HySDKManager.getInstance().hasExitView(this)) {
				HySDKManager.getInstance().showExitView(this);
			} else {
				exit();
			}
			break;
		}
		


		default:
			break;
		}
		return false;
	}
	private void exit() {
		// TODO Auto-generated method stub
		this.finish();
		System.exit(0);
	}

	private CommonSdkCallBack sdkcallback = new CommonSdkCallBack() {

		@Override
		public void realNameOnFinish(int arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void logoutOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 注销成功
				callUnityFunc("onLogoutSuccess", "success");
			}
		}

		@Override
		public void loginOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			switch (arg1) {
			case 0:// 登录成功
				try {
					JSONObject object = new JSONObject(arg0);
					JSONObject json = new JSONObject();
				

						json.put("userName", object.getString("userName") == null ? "" : object.getString("userName"));
						json.put("userId", object.getString("userId") == null ? "" : object.getString("userId"));
						json.put("token",object.getString("token") == null ? "" : object.getString("token"));
						json.put("isFloatChangeUser", object.getString("isFloatChangeUser") == null ? "" : object.getString("isFloatChangeUser"));
						json.put("timestamp", object.getString("timestamp") == null ? "" : object.getString("timestamp"));
						json.put("platformChanleId",object.getString("platformChanleId") == null ? "" : object.getString("platformChanleId"));
						json.put("statusCode", arg1);

						json.put("msg", "success");

				
					callUnityFunc("onLoginSuccess", json.toString());
	
					// sendExtData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			case 2:// sdk登录页面退出 （不是所有渠道都有该状态）
				JSONObject json = new JSONObject();
				try {

					json.put("msg", "cancel");

				} catch (Exception e) {
				}

				callUnityFunc("onLoginFailed", json.toString());
				break;
			default:
				break;
			}
		}

		@Override
		public void initOnFinish(String msg, int arg1) {
			// TODO Auto-generated method stub
			Log.e("fq", msg);
			if (arg1 == 0) {
				// 初始化成功 继续游戏
				if (!TextUtils.isEmpty(gameObjectName)) {
					callUnityFunc("onInitSuccess", new JSONObject().toString());
				} else {
					initState = INIT_SUCCESS;
				}
			} else if (arg1 == 1) {
				// 初始化成功 同时用户自动登录成功，不要再次调用登录接口
			} else {
				// 初始化失败 建议在有网络的情况下再次调用，失败还是失败，建议重启游戏或者退出游戏
				if (!TextUtils.isEmpty(gameObjectName)) {
					JSONObject json = new JSONObject();
					try {

						json.put("msg", msg);

					} catch (Exception e) {
						e.printStackTrace();
					}
					callUnityFunc("onInitFailed", new JSONObject().toString());
				} else {
					mInitMsg = msg;
					initState = INIT_FAILED;
				}
			}
			
			
		
		}

		@Override
		public void getAdultOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public void exitViewOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 用户推出游戏
				// 这里结束游戏
				callUnityFunc("onExitSuccess", "success");
				HuaYangUnityPlayerproxyActivity.this.finish();
			} else {
				// 用户继续游戏
			}
		}

		@Override
		public void chargeOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 充值流程完成
				Log.d("fq", "充值流程完成");
				JSONObject json = new JSONObject();
				try {
					json.put("msg", arg0);

				} catch (Exception e) {
				}

				callUnityFunc("onPaySuccess", json.toString());
			} else {
				// 充值流程未完成
				Log.d("fq", "充值流程未完成");
				JSONObject json = new JSONObject();
				try {
					json.put("msg", arg0);

				} catch (Exception e) {
				}

				callUnityFunc("onPayFailed", json.toString());
			}
		}

		@Override
		public void ReloginOnFinish(String arg0, int arg1) {
			switch (arg1) {
			case 0:
			case 3:
				// 收到回调，立即退出游戏回到登陆页面，让用户重新调用sdk登陆页面
				FLogger.d(arg0);
				Log.d("fq","收到回调，立即退出游戏回到登陆页面，让用户重新调用sdk登陆页面");
//				JSONObject json = new JSONObject();
//				try {
//
//					json.put("userName", userInfo.getUserName() == null ? "" : userInfo.getUserName());
//					json.put("userId", userInfo.getUID() == null ? "" : userInfo.getUID());
//					json.put("userToken", userInfo.getToken() == null ? "" : userInfo.getToken());
//					json.put("channelToken", userInfo.getChannelToken());
//
//					json.put("msg", "success");
//
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
				callUnityFunc("onSwitchAccountSuccess", arg0);
				break;
			case 1:
				// 用户已经处于注销状态,sdk登录界面已经打开
				break;
			case 4:
				// 用户已经处于登录状态,sdk登录界面关闭，用户信息返回(不要再次调用登录)
				break;
			default:
				break;
			}
			
		}
	};
}
