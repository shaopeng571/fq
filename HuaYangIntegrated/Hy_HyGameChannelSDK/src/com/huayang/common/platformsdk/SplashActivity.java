package com.huayang.common.platformsdk;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import com.fqwl.hycommonsdk.present.HySDKManager;
import com.fqwl.hycommonsdk.present.apiinteface.HyGameCallBack;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.LinearLayout;


public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		HySDKManager.getInstance().initWelcomeActivity(this,new HyGameCallBack() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				goGameActivity();
			}
			
			@Override
			public void onFailed() {
				// TODO Auto-generated method stub
				goGameActivity();
			}
		});
	}
	
	/**
	 * 跳到游戏界面
	 */
	public void goGameActivity() {
		
		Class<?> channelsdk;
		try {
			channelsdk = Class.forName(ChannelConfigUtil.getMainActivityName(SplashActivity.this));
			 Intent intent = new Intent(this, channelsdk);

	         startActivity(intent);

	         this.finish();
		
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setView(Animation mAnimation) {
		int id = 0;
		int orientation = getResources().getConfiguration().orientation;
		if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
			id = this.getResources().getIdentifier("hygame_splash_land",
					"drawable", this.getPackageName());
		} else if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			id = this.getResources().getIdentifier("hygame_splash", "drawable",
					this.getPackageName());
		}
		// TODO Auto-generated method stub
		
		if (id == 0) {
			goGameActivity();
			return;
		}
		LinearLayout layout = new LinearLayout(this);
		layout.setBackgroundResource(id);
		layout.setAnimation(mAnimation);
		setContentView(layout);
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		HySDKManager.getInstance().onNewIntent(intent);
	}
}
