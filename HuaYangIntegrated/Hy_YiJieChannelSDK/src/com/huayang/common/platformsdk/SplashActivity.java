package com.huayang.common.platformsdk;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.snowfish.cn.ganga.helper.SFOnlineSplashActivity;

public class SplashActivity extends SFOnlineSplashActivity {

	public int getBackgroundColor() {
		return Color.WHITE;
	}

	@Override
	public void onSplashStop() {
		
		Class<?> channelsdk;
				try {
					channelsdk = Class.forName(getMainActivityName(SplashActivity.this));
					 Intent intent = new Intent(this, channelsdk);

			         startActivity(intent);

			         this.finish();
				
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
		

	public void getSingInfo() {
		try {
			PackageInfo packageInfo = getPackageManager().getPackageInfo(
					SplashActivity.this.getPackageName(), PackageManager.GET_SIGNATURES);
			Signature[] signs = packageInfo.signatures;
			Signature sign = signs[0];
			parseSignature(sign.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void parseSignature(byte[] signature) {
		try {
			CertificateFactory certFactory = CertificateFactory
					.getInstance("X.509");
			X509Certificate cert = (X509Certificate) certFactory
					.generateCertificate(new ByteArrayInputStream(signature));
			String pubKey = cert.getPublicKey().toString();
			String signNumber = cert.getSerialNumber().toString();
			Log.v("aaa","signName:" + cert.getSigAlgName());
			Log.v("aaa","pubKey:" + pubKey);
			Log.v("aaa","signNumber:" + signNumber);
			Log.v("aaa","subjectDN:" + cert.getSubjectDN().toString());
			
			
		} catch (CertificateException e) {
			e.printStackTrace();
		}
	}

	public static String getMainActivityName(Context mActivity) {
		// TODO Auto-generated method stub
				PackageManager pm = mActivity.getPackageManager();
				ApplicationInfo appinfo;
				try {
					appinfo = pm.getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
					Bundle metaData = appinfo.metaData;
					return metaData.getString("HyGame_MainActivityName");
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				return "";
		
		
	}
}
