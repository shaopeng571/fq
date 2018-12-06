package com.fqwl.hycommonsdk.present;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.SplashActivity;
import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.present.apiinteface.ActivityCycle;
import com.fqwl.hycommonsdk.present.apiinteface.HyGameCallBack;
import com.fqwl.hycommonsdk.present.apiinteface.IApplication;
import com.fqwl.hycommonsdk.present.apiinteface.IRoleDataAnaly;
import com.fqwl.hycommonsdk.present.apiinteface.IWelcome;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.present.network.ApiClient;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.CommonUtils;
import com.fqwl.hycommonsdk.util.ToastUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.Global;
import com.tomato.fqsdk.clinterface.HyInterface.OnInitFinishedListener;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.models.HyInitInfo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

//在对外接口与子类渠道sdk的中间层
public class CommonSDKCenter implements SdkApi, IRoleDataAnaly, ActivityCycle, IWelcome, IApplication {
	// 是否调用了 此接口
	private boolean isSubmitData = false;
	private boolean isInitOK = false;
	public static String chandID = "";
	public static int chanleId = -1;
	public static String VersionName = "0.0";
	private SdkApi sdkImpl;
	private CommonSdkCallBack mBack;
	private CommonSdkInitInfo sdkInitInfo;

	@Override
	public void init(Activity activity, CommonSdkInitInfo info, CommonSdkCallBack callBack, ImplCallback implCallback) {
		// TODO Auto-generated method stub
		this.mBack = callBack;
		this.sdkInitInfo = info;
		FLogger.init(info.isDebug());
		sdkImpl = getChannelImpl(activity);
		isInitOK = true;
		FLogger.d("初始化渠道...");
		// =======
		HyInitInfo initInfo = new HyInitInfo();
		initInfo.setDebug(info.isDebug());
		initInfo.setGameId(ChannelConfigUtil.getGameId(activity));
		initInfo.setGameVersion(info.getGameVersion());
		HySDK.getInstance().HyInitSDK(activity, initInfo, new OnInitFinishedListener() {

			@Override
			public void onInitFinish(int code, String desc) {

			}
		});
		// ===========
		sdkImpl.init(activity, info, callBack, new MyImplCallback(activity, callBack, sdkImpl));

		chandID = sdkImpl.getChannelID();
		VersionName = sdkImpl.getVersionName();

	}

	@Override
	public void login(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		sdkImpl.login(activity, commonSdkLoginInfo);
	}

	@Override
	public void charge(final Activity activity, final CommonSdkChargeInfo chargeInfo) {
		if (!isInitOK) {
			ToastUtil.toastInfo(activity, "初始化失败，停止充值");
			FLogger.e("初始化失败，停止充值");
			return;
		}

		if (false == isSubmitData) {
			ToastUtil.toastInfo(activity, "角色登录、切换后请发送运营统计数据！~");
		}
		getOderId(chargeInfo, activity, new CommonSDKHttpCallback() {
			@Override
			public void onResult(ResultInfo resultInfo, String msg) {
				// TODO Auto-generated method stub
//				super.onResult(resultInfo, msg);
				if (resultInfo != null && resultInfo.code == 1) {
					// 数据不是空的
					if (!TextUtils.isEmpty(resultInfo.data)) {
						JSONObject object = null;
						try {
							object = new JSONObject(resultInfo.data);
							chargeInfo.setOrder(object.getString("order_number"));
							// 服务器返回订单状态了
							chargeInfo.setState(true);
						} catch (JSONException e) {

							chargeInfo.setState(false);
							e.printStackTrace();
						}
					}

				} else {

					chargeInfo.setState(false);
					chargeInfo.setMsg(resultInfo.msg);
				}
				sdkImpl.charge(activity, chargeInfo);
			}
		});

	}

	@Override
	public boolean showExitView(Activity activity) {
		// TODO Auto-generated method stub
		return sdkImpl.showExitView(activity);
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
		if (sdkImpl != null) {
			sdkImpl.reLogin(activity, commonSdkLoginInfo);
		}
	}

	@Override
	public boolean showPersonView(Activity activity) {
		if (sdkImpl != null) {
			return sdkImpl.showPersonView(activity);

		} else {
			return false;
		}
	}

	@Override
	public void controlFlow(Activity context, boolean isShow) {
		// TODO Auto-generated method stub

	}



	/**
	 * 角色创建接口
	 * 
	 * @param mainActivity
	 * @param data
	 */
	public void sendExtendDataRoleCreate(final Activity mainActivity, final CommonSdkExtendData data) {
		isSubmitData = true;
		mainActivity.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(data.getRoleCTime())) {
					ToastUtil.toastInfo(mainActivity, "角色创建时间不能为空！");
				}

			}
		});

		roleCreate(mainActivity, data);

		ApiClient.getInstance().roleCreate(data, null);

	}

	@Override
	public void roleCreate(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		// 子类必须要实现IRoleDataAnaly接口
		if (sdkImpl instanceof IRoleDataAnaly) {
			// Log.i("commonsdk", "sdk.roleCreate");
			((IRoleDataAnaly) sdkImpl).roleCreate(activity, data);
		}
	}

	/**
	 * 角色升级接口
	 * 
	 * @param mainActivity
	 * @param data
	 */
	public void sendExtendDataRoleLevelUpdate(final Activity mainActivity, final CommonSdkExtendData data) {
		roleLevelUpdate(mainActivity, data);

		ApiClient.getInstance().roleLevelUpdate(data, null);
	}

	@Override
	public void roleLevelUpdate(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		if (sdkImpl instanceof IRoleDataAnaly) {
			// Log.i("commonsdk", "sdk.roleLevelUpdate");
			((IRoleDataAnaly) sdkImpl).roleLevelUpdate(activity, data);
		}
	}

	public void sendExtendDataRoleLogout(final Activity mainActivity, final CommonSdkExtendData data) {

		ApiClient.getInstance().rolelogOut(data, null);
	}

	public void sendExtendDataRoleOther(final Activity mainActivity, final CommonSdkExtendData data, String behavior,
			Map<String, Object> dataMap) {

		ApiClient.getInstance().roleOther(data, behavior, dataMap);
	}

	@Override
	public void submitExtendData(final Activity activity, final CommonSdkExtendData data) {
		isSubmitData = true;
		FLogger.d("****角色进入统计接口**");
		if (TextUtils.isEmpty(data.getUserMoney()) || TextUtils.isEmpty(data.getVipLevel())) {

			activity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.toastInfo(activity, "缺少必要参数，请参考运营统计接口文档");
				}
			});
			return;
		}

		isSubmitData = true;
//		if (TextUtils.isEmpty(sdkImpl.getUserId())) {
//			return;
//		}
		ApiClient.getInstance().roleLogin(data, null);
//		// 统一发送进入游戏
//		new Thread(new Runnable() {
//			public void run() {
//				Looper.prepare();
//				
//				Looper.loop();
//			}
//		}).start();
		// 渠道自身需要发送在实现类里添加发送代码
		sdkImpl.submitExtendData(activity, data);
	}

	@Override
	public void getOderId(CommonSdkChargeInfo info, Activity context, CommonSDKHttpCallback httpCallback) {
		// TODO Auto-generated method stub
		ApiClient.getInstance().orderCreate(context, info, httpCallback);
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return sdkImpl.getUserId();
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return sdkImpl.getVersionName();
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return sdkImpl.getChannelID();
	}

	@Override
	public boolean hasExitView() {
		if (!isInitOK) {
			FLogger.e("初始化失败，hasExitView return false");
			return false;
		}
		return sdkImpl.hasExitView();
	}

	private SdkApi getChannelImpl(Context context) {
		int channleNum = ChannelConfigUtil.getPlatformChannelId(context);
		Class<SdkApi> channelsdk;
		try {
			channelsdk = (Class<SdkApi>) Class.forName("com.huayang.common.platformsdk.present.platformApi");
			sdkImpl = channelsdk.newInstance();
//			switch (channleNum) {
//			case 0:
//				channelsdk = (Class<SdkApi>) Class.forName("com.fqwl.hy_hygamechannelsdk.present.SdkApi");
//				sdkImpl = channelsdk.newInstance();
//				break;
//			case 1:
//				channelsdk = (Class<SdkApi>) Class.forName("com.fqwl.hy_yijiechannelsdk.present.SdkApi");
//				sdkImpl = channelsdk.newInstance(); // new ChannelSDK();
//				break;
//			case 2:
//				channelsdk = (Class<SdkApi>) Class.forName("com.sv.da.present.SdkApi");
//				sdkImpl = channelsdk.newInstance(); // new ChannelSDK();
//				break;
//			case 3:
//				channelsdk = (Class<SdkApi>) Class.forName("com.hywl.hy_xiaomichannelsdk.present.ChannelSdkApi");
//				sdkImpl = channelsdk.newInstance(); // new ChannelSDK();
//				break;
//			case 4:
//				channelsdk = (Class<SdkApi>) Class.forName("com.hywl.huayang_channelsdk_huawei.present.ChannelSDK_HuaWei");
//				sdkImpl = channelsdk.newInstance(); 
//				break;
//			case 5:
//				channelsdk = (Class<SdkApi>) Class.forName("com.hywl.huayang_channelsdk_yyb.present.SdkApi_yyb");
//				sdkImpl = channelsdk.newInstance(); 
//				break;
//			default:
//				break;
//			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sdkImpl;
	}


	@Override
	public void onStart(Activity activity) {
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onStart(activity);
		}
	}

	@Override
	public void onRestart(Activity activity) {
		// 子类必须要实现ActivityCycle接口
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onRestart(activity);
		}
	}

	@Override
	public void onResume(Activity activity) {
		// 子类必须要实现ActivityCycle接口
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onResume(activity);
			Log.i("commonsdk", "sdk.onResume");
		}
	}

	@Override
	public void onPause(Activity activity) {
		// 子类必须要实现ActivityCycle接口
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onPause(activity);
			Log.i("commonsdk", "sdk.onPause");
		}
	}

	@Override
	public void onStop(Activity activity) {
		// 子类必须要实现ActivityCycle接口
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onStop(activity);
		}
	}

	@Override
	public void DoRelease(Activity activity) {
		sdkImpl.DoRelease(activity);
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		// 子类必须要实现ActivityCycle接口
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onActivityResult(activity, requestCode, resultCode, data);
		}
	}

	@Override
	public void onNewIntent(Activity activity, Intent intent) {
		// 子类必须要实现ActivityCycle接口
		if (sdkImpl instanceof ActivityCycle) {
			((ActivityCycle) sdkImpl).onNewIntent(activity, intent);
		}
	}

	@Override
	public void initWelcomeActivity(Activity activity, final HyGameCallBack callback) {
		// TODO Auto-generated method stub
		if (sdkImpl == null) {
			sdkImpl = getChannelImpl(activity);
			FLogger.w(Global.INNER_TAG, "initWelcomeActivity ..");
		}
		// 子类实现的闪屏接口
		if (sdkImpl instanceof IWelcome) {
			((IWelcome) sdkImpl).initWelcomeActivity(activity, callback);
		}

		//
		boolean hasSplashPic = ChannelConfigUtil.getHasSplashPic(activity);
		Log.d("fq","hasSplashPic: " + hasSplashPic);
		if (hasSplashPic) {
			Animation mAnimation = new AlphaAnimation(0.3f, 1.0f);
			mAnimation.setDuration(1500);
			mAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					callback.onSuccess();
				}
			});
			((SplashActivity) activity).setView(mAnimation);
		} else {
			//BUG地方 sdkimpl不可能为空
//			if (sdkImpl == null) {// 如果sdkImpl不为空，那么callback由sdkImpl回调
				callback.onSuccess();
//			}
		}
	}

	@Override
	public void initGamesApi(Application context) {
		FLogger.d("application initGamesApi " + "Running Process is " + CommonUtils.getProcessName(context));

		if (!context.getPackageName().equals(CommonUtils.getProcessName(context))) {
			FLogger.d("return initGamesApi");
			return;
		}
		// Logger.d("IApplication ");
		if (getChannelImpl(context) instanceof IApplication) {
			((IApplication) getChannelImpl(context)).initGamesApi(context);
		}
	}

	@Override
	public void initPluginInAppcation(Application application, Context context) {
		if (getChannelImpl(context) instanceof IApplication) {
			((IApplication) getChannelImpl(context)).initPluginInAppcation(application, context);
		}
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return sdkImpl.getChannelName();
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		sdkImpl.logout();
	}

}
