package com.fqwl.hycommonsdk.present.apiinteface;

import java.util.Map;

import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;

public interface ISdkManager {

	void initWelcomeActivity(Activity activity,HyGameCallBack callBack);

	void initCommonSdk(Activity activity, CommonSdkInitInfo info, CommonSdkCallBack callBack);

	void showLoginView(Activity context, CommonSdkLoginInfo commonSdkLoginInfo);

	void showReLogionView(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo);

	void showChargeView(Activity context, CommonSdkChargeInfo commonSdkChargeInfo);

	void controlFlowView(Activity context, boolean isShow);

	boolean showExitView(Activity activity);

	public void onStart(Activity activity);

	public void onRestart(Activity activity);

	public void onResume(Activity activity);

	public void onPause(Activity activity);

	public void onStop(Activity activity);

	public void onDestroy(Activity activity);

	public void onActivityResult(int requestCode, int resultCode, Intent data);

	public void onNewIntent(Intent intent);

	void sendExtendData(final Activity mainActivity, CommonSdkExtendData data);

	void sendExtendDataRoleCreate(final Activity mainActivity, CommonSdkExtendData data);

	void sendExtendDataRoleLevelUpdate(final Activity mainActivity, CommonSdkExtendData data);

	void sendExtendDataRoleLogout(final Activity mainActivity, CommonSdkExtendData data);

	void sendExtendDataRoleOther(final Activity mainActivity, String behavior, CommonSdkExtendData data,
			Map<String, Object> map);

	void updateApk(Context context, boolean isSDK, Handler handler);

	void onWindowFocusChanged();

	void onConfigurationChanged(Configuration newConfig);

//	int getPlatformChanleId(Context context);

	String getCurrentChannelName();

	String getCurrentVersionName();

	String getCurrentCommonSdkVersionName();

	String getCurrentUserId();

	void initPluginInAppcation(Context context);

	void initPluginInAppcation(Application application, Context context);

	void initGamesApi(Application context);

	boolean hasExitView(Context context);

	void sysUserId(String userId);

	void postGiftCode(String giftCodeValue);

	void showReLoginBefore(Activity activity);

	void openGmPage(Context context, String url);

	// boolean isRealName();

	int getUserAge();

	// 应用宝接口
	void openImmersive();

	int getLocalPlatformChanleId(Context context);
	
	void logout();
}
