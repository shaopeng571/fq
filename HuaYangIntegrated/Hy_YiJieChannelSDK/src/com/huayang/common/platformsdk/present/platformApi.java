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
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class platformApi implements com.fqwl.hycommonsdk.present.apiinteface.SdkApi, com.fqwl.hycommonsdk.present.apiinteface.ActivityCycle,
		com.fqwl.hycommonsdk.present.apiinteface.IRoleDataAnaly {
	private CommonSdkCallBack callBack;
	private ImplCallback implCallback;
	private Activity mActivity;
	@Override
	public void init(final Activity activity, CommonSdkInitInfo info, final CommonSdkCallBack callBack,
			final ImplCallback implCallback) {
		this.mActivity=activity;
		this.callBack = callBack;
		this.implCallback = implCallback;
		// TODO Auto-generated method stub
		SFOnlineHelper.onCreate(activity, new SFOnlineInitListener() {

			@Override
			public void onResponse(String tag, String value) {
				if (tag.equalsIgnoreCase("success")) {

					// 初始化成功的回调
					callBack.initOnFinish("初始化成功", 0);
				} else if (tag.equalsIgnoreCase("fail")) {
					callBack.initOnFinish("初始化失败", 1);
					// 初始化失败的回调，value：如果SDK返回了失败的原因，会给value赋值

				}
				// TODO Auto-generated method stub

			}
		});
		SFOnlineHelper.setLoginListener(activity, new SFOnlineLoginListener() {

			@Override
			public void onLogout(Object arg0) {
				// TODO Auto-generated method stub
				platformApi.this.callBack.logoutOnFinish("退出账号成功", 0);
			}

			@Override
			public void onLoginSuccess(SFOnlineUser user, Object arg1) {
				String uid = user.getChannelUserId();
				JSONObject result_data = new JSONObject();
				try {
					result_data.put("appId", user.getProductCode());
					result_data.put("channelId", user.getChannelId());
					result_data.put("userId", user.getChannelUserId());
					result_data.put("token", user.getToken());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				implCallback.onLoginVerify(result_data);
//				SdkApi.this.callBack.loginOnFinish("登录成功", 0);
			}

			@Override
			public void onLoginFailed(String arg0, Object arg1) {
				// TODO Auto-generated method stub
				callBack.loginOnFinish("登录失败", 2);
			}
		});

	}

	@Override
	public void login(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		SFOnlineHelper.login(activity, "Login");
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return CommonBackLoginInfo.getInstance().userId;
	}

	@Override
	public void reLogin(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		logout(activity);

		login(activity, commonSdkLoginInfo);
	}

	public boolean logout(Activity activity) {
		// TODO Auto-generated method stub
		SFOnlineHelper.logout(activity, "LoginOut");
		return true;
	}

	@Override
	public void charge(Activity activity, CommonSdkChargeInfo ChargeInfo) {
		// TODO Auto-generated method stub
		SFOnlineHelper.pay(activity, (int)ChargeInfo.getMoney()*100, ChargeInfo.getGoods_name(), 1,
				ChargeInfo.getOrder(), "", new SFOnlinePayResultListener() {

					@Override
					public void onFailed(String arg0) {
						// TODO Auto-generated method stub
						implCallback.onPayFinish(1);
					}

					@Override
					public void onOderNo(String arg0) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(String arg0) {
						// TODO Auto-generated method stub
						implCallback.onPayFinish(0);
					}

				});
	}

	@Override
	public void DoRelease(Activity activity) {
		// TODO Auto-generated method stub
		SFOnlineHelper.onDestroy(activity);

	}
	private AlertDialog exitDialog;
	@Override
	public boolean showExitView(final Activity activity) {
		// TODO Auto-generated method stub
		FLogger.d("来到了退出界面");
		SFOnlineHelper.exit(activity, new SFOnlineExitListener() {

			@Override
			public void onSDKExit(boolean bool) {
				// TODO Auto-generated method stub
				if (bool) {
					FLogger.d("onSDKExit");
					// SDK已经退出，此处可以调用游戏的退出函数
					callBack.exitViewOnFinish("退出成功", 0);
					activity.finish();
					android.os.Process.killProcess(android.os.Process.myPid());// 杀进程
				} else {
					callBack.exitViewOnFinish("退出失败", 2);
				}
			}

			@Override
			public void onNoExiterProvide() {
				
				// TODO Auto-generated method stub
				// 账号退出
//				callBack.exitViewOnFinish("退出成功", 0);
				if (exitDialog != null) {
					FLogger.d("fq", "已弹出");
					return;
				}
				FLogger.d("onNoExiterProvide");
				// cp自己实现退出界面
				AlertDialog.Builder builder;
				if (Build.VERSION.SDK_INT >= 21) {
					builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
				} else {
					builder = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_LIGHT);
				}
				builder.setMessage("确定退出游戏?");
				builder.setCancelable(true);
				builder.setPositiveButton("继续游戏", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						FLogger.i("fq", "点击了继续游戏");
						exitDialog = null;
					}
				});
				builder.setNeutralButton("退出游戏", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						FLogger.i("fq", "点击了退出游戏");
						exitDialog = null;
						logout();
						activity.finish();// 销毁Activity
						android.os.Process.killProcess(android.os.Process.myPid());// 杀进程
						System.exit(0);// 退出
					}
				});
				exitDialog = builder.create();
				exitDialog.show();
				
			}
		});
		return true;
	}

	@Override
	public void onActivityResult(Activity arg0, int arg1, int arg2, Intent arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNewIntent(Activity arg0, Intent arg1) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onPause(Activity arg0) {
		// TODO Auto-generated method stub
		SFOnlineHelper.onPause(arg0);
	}

	@Override
	public void onRestart(Activity arg0) {
		// TODO Auto-generated method stub
		SFOnlineHelper.onRestart(arg0);
	}

	@Override
	public void onResume(Activity arg0) {
		// TODO Auto-generated method stub
		SFOnlineHelper.onResume(arg0);
	}

	@Override
	public void onStart(Activity arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop(Activity arg0) {
		// TODO Auto-generated method stub
		SFOnlineHelper.onStop(arg0);
	}

	@Override
	public void submitExtendData(Activity activity, CommonSdkExtendData data) {

		JSONObject roleInfo = new JSONObject();

		try {
			roleInfo.put("roleId", data.getRoleId());// 当前登录的玩家角色ID，必须为数字
			roleInfo.put("roleName", data.getRoleName());// 当前登录的玩家角色名，不能为空，不能为null
			roleInfo.put("roleLevel", data.getRoleLevel());// 当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneId", data.getServerId());// 当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneName", data.getServerName());// 当前登录的游戏区服名称，不能为空，不能为null
			roleInfo.put("balance", data.getUserMoney()); // 用户游戏币余额，必须为数字，若无，传入0
			roleInfo.put("vip", data.getVipLevel());// 当前用户VIP等级，必须为数字，若无，传入1
			roleInfo.put("partyName", data.getParty_name());// 当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
			roleInfo.put("roleCTime", data.getRoleCTime()); // 单位为秒，创建角色的时间
			roleInfo.put("roleLevelMTime", data.getRoleLevelMTime()); // 单位为秒，角色等级变化时间
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("fqcommonsdk", roleInfo.toString());
		SFOnlineHelper.setRoleData(activity, data.getRoleId(), data.getRoleName(), data.getRoleLevel(),
				data.getServerId(), data.getServerName());
		SFOnlineHelper.setData(activity, "enterServer", roleInfo.toString());
	}

	@Override
	public void roleCreate(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		JSONObject roleInfo = new JSONObject();

		try {
			roleInfo.put("roleId", data.getRoleId());// 当前登录的玩家角色ID，必须为数字
			roleInfo.put("roleName", data.getRoleName());// 当前登录的玩家角色名，不能为空，不能为null
			roleInfo.put("roleLevel", data.getRoleLevel());// 当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneId", data.getServerId());// 当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneName", data.getServerName());// 当前登录的游戏区服名称，不能为空，不能为null
			roleInfo.put("balance", data.getUserMoney()); // 用户游戏币余额，必须为数字，若无，传入0
			roleInfo.put("vip", data.getVipLevel());// 当前用户VIP等级，必须为数字，若无，传入1
			roleInfo.put("partyName", data.getParty_name());// 当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
			roleInfo.put("roleCTime", data.getRoleCTime()); // 单位为秒，创建角色的时间
			roleInfo.put("roleLevelMTime", data.getRoleLevelMTime()); // 单位为秒，角色等级变化时间
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("fqcommonsdk", roleInfo.toString());
		SFOnlineHelper.setData(activity, "createrole", roleInfo.toString());
	}

	@Override
	public void roleLevelUpdate(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		JSONObject roleInfo = new JSONObject();
		
		try {
			roleInfo.put("roleId", data.getRoleId());// 当前登录的玩家角色ID，必须为数字
			roleInfo.put("roleName", data.getRoleName());// 当前登录的玩家角色名，不能为空，不能为null
			roleInfo.put("roleLevel", data.getRoleLevel());// 当前登录的玩家角色等级，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneId", data.getServerId());// 当前登录的游戏区服ID，必须为数字，且不能为0，若无，传入1
			roleInfo.put("zoneName", data.getServerName());// 当前登录的游戏区服名称，不能为空，不能为null
			roleInfo.put("balance", data.getUserMoney()); // 用户游戏币余额，必须为数字，若无，传入0
			roleInfo.put("vip", data.getVipLevel());// 当前用户VIP等级，必须为数字，若无，传入1
			roleInfo.put("partyName", data.getParty_name());// 当前角色所属帮派，不能为空，不能为null，若无，传入“无帮派”
			roleInfo.put("roleCTime", data.getRoleCTime()); // 单位为秒，创建角色的时间
			roleInfo.put("roleLevelMTime", data.getRoleLevelMTime()); // 单位为秒，角色等级变化时间
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("fqcommonsdk", roleInfo.toString());
		SFOnlineHelper.setData(activity, "levelup", roleInfo.toString());
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
		return "1";
	}

	@Override
	public void getOderId(CommonSdkChargeInfo arg0, Activity arg1, CommonSDKHttpCallback arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return "2.7";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void setDebug(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean showPersonView(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "易接sdk";
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		logout(mActivity);
	}

}
