package com.fqwl.hycommonsdk.util;

import com.fqwl.hycommonsdk.util.logutils.FLogger;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;

public class ChannelConfigUtil {
	public static int getPlatformChannelId(Context context){
		if (context!=null) {
			PackageManager pm=context.getPackageManager();
			try {
				ApplicationInfo applicationInfo=pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
				Bundle bundle=applicationInfo.metaData;
				return bundle.getInt("HyGamePlatformChannleId");
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return 0;
	}
	
	public static String getGameId(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getInt("HyGame_GameId") + "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getChannelId(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getInt("HyGame_ChannelId") + "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getGameKey(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getString("HYGAME_APPKEY") + "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static boolean getHasSplashPic(Context mActivity) {
		// TODO Auto-generated method stub
				PackageManager pm = mActivity.getPackageManager();
				ApplicationInfo appinfo;
				try {
					appinfo = pm.getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
					Bundle metaData = appinfo.metaData;
					return metaData.getBoolean("HyGame_HasSplashPic");
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				return false;
		
		
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
	
	public static String getMetaMsg(Context mActivity,String msg) {
		// TODO Auto-generated method stub
				PackageManager pm = mActivity.getPackageManager();
				ApplicationInfo appinfo;
				try {
					appinfo = pm.getApplicationInfo(mActivity.getPackageName(), PackageManager.GET_META_DATA);
					Bundle metaData = appinfo.metaData;
					Object object=metaData.get(msg);
					String strmsg=String.valueOf(object);
					return strmsg;
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				return "";
		
		
	}
	public static long getMetaILong(Context context, String key) {

		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getLong(key);
		} catch (Exception e) {
			FLogger.Ex(e);
		}
		return 0;
	}
	public static int getMetaInt(Context context, String key) {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getInt(key);
		} catch (Exception e) {
			FLogger.Ex(e);
		}
		return 0;
	}
	public static float getMetaFloat(Context context, String key) {
		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getFloat(key);
		} catch (Exception e) {
			FLogger.Ex(e);
		}
		return 0;
	}
	public static String getMetaString(Context context, String key) {

		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getString(key);
		} catch (Exception e) {
			FLogger.Ex( e);
		}
		return "";
	}
	public static boolean getMetaBoolean(Context context, String key) {

		PackageManager pm = context.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.getBoolean(key, false);
		} catch (Exception e) {
			FLogger.Ex(e);
		}
		return false;
	}
}
