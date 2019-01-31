package com.fqwl.hycommonsdk.present;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.HyGameCallBack;
import com.fqwl.hycommonsdk.present.apiinteface.ISdkManager;
import com.fqwl.hycommonsdk.present.network.ApiClient;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.CommonUtils;
import com.fqwl.hycommonsdk.util.ToastUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

public class HySDKManager implements ISdkManager {
	private static HySDKManager sdkManager;
	private CommonSDKCenter implsdkcenter;
	private Activity mActivity;

	private HySDKManager() {
		implsdkcenter = new CommonSDKCenter();
	}

	public static synchronized HySDKManager getInstance() {
		if (sdkManager == null) {
			sdkManager = new HySDKManager();
		}
		return sdkManager;
	}

	@Override
	public void initCommonSdk(Activity activity, CommonSdkInitInfo info, CommonSdkCallBack callBack) {
		// TODO Auto-generated method stub
		if (mActivity == null) {
			this.mActivity = activity;
		}
		info.setDebug(true);
		FLogger.init(info.isDebug(), "fq");
		implsdkcenter.init(activity, info, callBack, null);
	}

	@Override
	public void showLoginView(Activity context, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		FLogger.d("showLoginView");
		implsdkcenter.login(context, commonSdkLoginInfo);
	}

	@Override
	public void showReLogionView(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		implsdkcenter.reLogin(activity, commonSdkLoginInfo);
	}

	@Override
	public void showChargeView(Activity context, CommonSdkChargeInfo commonSdkChargeInfo) {
		if (commonSdkChargeInfo.getGoods_desc() != null)
			System.out.println(commonSdkChargeInfo.getGoods_desc());
		if (commonSdkChargeInfo.getGoods_id() != null)
			System.out.println(commonSdkChargeInfo.getGoods_id());

		// 将CP私有字段（扩展参数）保存到另一个字段,后面的代码会处理CallBackInfo字段
//		commonSdkChargeInfo.set(new String(commonSdkChargeInfo.getCallBackInfo()));
//		commonSdkChargeInfo.setCallBackInfo("");
		implsdkcenter.charge(context, commonSdkChargeInfo);
	}

	@Override
	public void controlFlowView(Activity context, boolean isShow) {
		// TODO Auto-generated method stub
		if (isShow) {
			implsdkcenter.onResume(context);
		}else {
			implsdkcenter.onPause(context);
		}

	}

	@Override
	public boolean showExitView(Activity activity) {
		// TODO Auto-generated method stub
		FLogger.d("showExitView");
		return implsdkcenter.showExitView(activity);
	}

	@Override
	public void onStart(Activity activity) {
		// TODO Auto-generated method stub
		implsdkcenter.onStart(activity);
	}

	@Override
	public void onRestart(Activity activity) {
		// TODO Auto-generated method stub
		implsdkcenter.onRestart(activity);
	}

	@Override
	public void onResume(Activity activity) {
		// TODO Auto-generated method stub
		implsdkcenter.onResume(activity);
	}

	@Override
	public void onPause(Activity activity) {
		// TODO Auto-generated method stub
		implsdkcenter.onPause(activity);
	}

	@Override
	public void onStop(Activity activity) {
		// TODO Auto-generated method stub
		implsdkcenter.onStop(activity);
	}

	@Override
	public void onDestroy(Activity activity) {
		// TODO Auto-generated method stub
		implsdkcenter.DoRelease(activity);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		implsdkcenter.onActivityResult(mActivity, requestCode, resultCode, data);
	}

	@Override
	public void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		implsdkcenter.onNewIntent(mActivity, intent);
	}

	@Override
	public void sendExtendData(final Activity mainActivity, final CommonSdkExtendData data) {
		FLogger.d("****角色进入统计接口**");
		if (TextUtils.isEmpty(data.getUserMoney()) || TextUtils.isEmpty(data.getVipLevel())) {

			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.toastInfo(mainActivity, "缺少必要参数，请参考运营统计接口文档");
				}
			});
			return;
		}
		implsdkcenter.submitExtendData(mainActivity, data);
	}

	@Override
	public void sendExtendDataRoleCreate(Activity mainActivity, CommonSdkExtendData data) {
		implsdkcenter.sendExtendDataRoleCreate(mainActivity, data);
	}

	@Override
	public void sendExtendDataRoleLevelUpdate(Activity mainActivity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		implsdkcenter.sendExtendDataRoleLevelUpdate(mainActivity, data);
	}

	@Override
	public void sendExtendDataRoleLogout(Activity mainActivity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		implsdkcenter.sendExtendDataRoleLogout(mainActivity, data);
	}

	@Override
	public void sendExtendDataRoleOther(Activity mainActivity, String behavior, CommonSdkExtendData data,
			Map<String, Object> map) {
		// TODO Auto-generated method stub
		implsdkcenter.sendExtendDataRoleOther(mainActivity, data, behavior, map);
	}

	@Override
	public void updateApk(Context context, boolean isSDK, Handler handler) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWindowFocusChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getLocalPlatformChanleId(Context context) {
		// TODO Auto-generated method stub
		return ChannelConfigUtil.getPlatformChannelId(mActivity);
	}

	
	public String getChanleId(Context context) {
		// TODO Auto-generated method stub
		return ChannelConfigUtil.getChannelId(mActivity);
	}
	@Override
	public String getCurrentChannelName() {
		// TODO Auto-generated method stub
		return implsdkcenter.getChannelName();
	}

	@Override
	public String getCurrentVersionName() {
		// TODO Auto-generated method stub
		return implsdkcenter.getVersionName();
	}

	@Override
	public String getCurrentCommonSdkVersionName() {
		// TODO Auto-generated method stub
		return "1.0";
	}

	@Override
	public String getCurrentUserId() {
		String uid = CommonBackLoginInfo.getInstance().userId;
		if (TextUtils.isEmpty(uid)) {
			return implsdkcenter.getUserId();
		}
		return uid;
	}

	@Override
	public void initPluginInAppcation(Context context) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initPluginInAppcation(Application application, Context context) {
		// TODO Auto-generated method stub
		implsdkcenter.initPluginInAppcation(application, context);

	}

	@Override
	public void initGamesApi(Application context) {
		// TODO Auto-generated method stub
implsdkcenter.initGamesApi(context);
	}

	@Override
	public boolean hasExitView(Context context) {
		// TODO Auto-generated method stub
		return implsdkcenter.hasExitView();
	}

	@Override
	public void sysUserId(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postGiftCode(String giftCodeValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void showReLoginBefore(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void openGmPage(Context context, String url) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getUserAge() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void openImmersive() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initWelcomeActivity(Activity activity,HyGameCallBack callBack) {
		// TODO Auto-generated method stub
		implsdkcenter.initWelcomeActivity(activity, callBack);
	}
	
	public String getTimeZ(){
		return CommonUtils.GetTimeZ();
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		implsdkcenter.logout();
	}


}
