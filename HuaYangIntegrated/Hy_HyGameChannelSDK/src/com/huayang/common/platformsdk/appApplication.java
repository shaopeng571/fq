package com.huayang.common.platformsdk;

import com.fqwl.hycommonsdk.present.HySDKManager;

import android.app.Application;
import android.content.Context;

public class appApplication extends Application {
	@Override
	public void onCreate() {
		 HySDKManager.getInstance().initGamesApi(this);
		super.onCreate();
		
	}
	@Override
	  protected void attachBaseContext(Context base) {
		HySDKManager.getInstance().initPluginInAppcation(base);
	super.attachBaseContext(base);
	 }
} 