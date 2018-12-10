package com.tomato.fqsdk.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import com.tomato.fqsdk.models.CLCommon;

public class CommonUtil {
	private static long RLong;

	public static boolean checkPermissions(Context context, String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission,
				context.getPackageName()) == 0;
	}

	public static void SetSdkNameAndVer(String sdkname, String sdkver) {
		// ReYunConst.ry_sdk_name = sdkname;
		// ReYunConst.ry_sdk_ver = sdkver;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getTime(long interval) {
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		localSimpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
		return localSimpleDateFormat.format(Long.valueOf(interval));
	}

	public static long GetLong(Context context, String XMLName, String key) {
		SharedPreferences mysharedPreferences = context.getSharedPreferences(
				XMLName, 0);
		RLong = mysharedPreferences.getLong(key, 0L);
		if (RLong == 0L) {
			RLong = System.currentTimeMillis();
		}
		return RLong;
	}

	@SuppressWarnings("deprecation")
	public static String getActivityName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService("activity");
		if (checkPermissions(context, "android.permission.GET_TASKS")) {
			ComponentName cn = ((ActivityManager.RunningTaskInfo) am
					.getRunningTasks(1).get(0)).topActivity;
			return cn.getShortClassName();
		}
		if (CLCommon.DebugMode) {
			Log.e("lost permission", "android.permission.GET_TASKS");
		}

		return null;
	}

	@SuppressWarnings("deprecation")
	public static String getPackageName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService("activity");

		if (checkPermissions(context, "android.permission.GET_TASKS")) {
			ComponentName cn = ((ActivityManager.RunningTaskInfo) am
					.getRunningTasks(1).get(0)).topActivity;
			return cn.getPackageName();
		}
		if (CLCommon.DebugMode) {
			Log.e("lost permission", "android.permission.GET_TASKS");
		}

		return null;
	}

	public static String getDeviceID(Context context) {
		if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
			String deviceId = "";
			if (checkPhoneState(context)) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService("phone");
				deviceId = tm.getDeviceId();

				if (deviceId == null) {
					deviceId = Settings.Secure.getString(
							context.getContentResolver(), "android_id");
				}
			}
			if (deviceId != null) {
				return "unknown";
			}
			return "unknown";
		}
		return "unknown";
	}

	public static String getAndroidId(Context context) {
		String androidid = Settings.Secure.getString(
				context.getContentResolver(), "android_id");
		if (androidid == null) {
			return "unknown";
		}
		return androidid;
	}

	public static String getOperatorName(Context context) {
		if (checkPermissions(context, "android.permission.READ_PHONE_STATE")) {
			String op = "unknown";
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService("phone");
			op = tm.getSimOperatorName();
			if (op != null) {
				if (op.equals("")) {
					op = "unknown";
				}
				if (CLCommon.DebugMode) {
					printLog("commonUtil", "op:" + op);
				}
				return op;
			}
			if (CLCommon.DebugMode) {
				Log.e("commonUtil", "deviceId is null");
			}
			return "unknown";
		}

		if (CLCommon.DebugMode) {
			Log.e("lost permissioin",
					"lost----->android.permission.READ_PHONE_STATE");
		}
		return "unknown";
	}

	public static String getPhoneResolution(Context context) {
		String resolution = "unknown";
		WindowManager manager = (WindowManager) context
				.getSystemService("window");
		DisplayMetrics displaysMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(displaysMetrics);

		resolution = displaysMetrics.widthPixels + "*"
				+ displaysMetrics.heightPixels;

		return resolution;
	}

	public static boolean checkPhoneState(Context context) {
		PackageManager packageManager = context.getPackageManager();

		return packageManager
				.checkPermission("android.permission.READ_PHONE_STATE",
						context.getPackageName()) == 0;
	}

	public static String getSdkVersion(Context paramContext) {
		String osVersion = "";
		if (!checkPhoneState(paramContext)) {
			osVersion = Build.VERSION.RELEASE;
			if (CLCommon.DebugMode) {
				Log.e("android_osVersion", "OsVerson" + osVersion);
			}

			return osVersion;
		}
		if (CLCommon.DebugMode) {
			Log.e("android_osVersion", "OsVerson get failed");
		}

		return null;
	}

	public static String getCurVersion(Context context) {
		String curversion = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			curversion = pi.versionName;
			if ((curversion == null) || (curversion.length() <= 0))
				return "";
		} catch (Exception e) {
			if (CLCommon.DebugMode) {
				Log.e("VersionInfo", "Exception", e);
			}
		}

		return curversion;
	}

	public static boolean isHaveGravity(Context context) {
		SensorManager manager = (SensorManager) context
				.getSystemService("sensor");

		return manager != null;
	}

	private static String getNetworkType(Context context) {
		TelephonyManager manager = (TelephonyManager) context
				.getSystemService("phone");
		int type = manager.getNetworkType();
		String typeString = "UNKOWN";
		if (type == 4) {
			typeString = "CDMA";
			typeString = "2G";
		} else if (type == 2) {
			typeString = "EDGE";
			typeString = "2G";
		} else if (type == 5) {
			typeString = "EVDO_0";
			typeString = "3G";
		} else if (type == 6) {
			typeString = "EVDO_A";
			typeString = "3G";
		} else if (type == 1) {
			typeString = "GPRS";
			typeString = "2G";
		} else if (type == 8) {
			typeString = "HSDPA";
			typeString = "3G";
		} else if (type == 10) {
			typeString = "HSPA";
		} else if (type == 9) {
			typeString = "HSUPA";
		} else if (type == 3) {
			typeString = "UMTS";
			typeString = "3G";
		} else if (type == 13) {
			typeString = "4G";
		} else if (type == 0) {
			typeString = "UNKOWN";
		}

		return typeString;
	}

	public static boolean isNetworkTypeWifi(Context context) {
		if (checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			NetworkInfo info = cManager.getActiveNetworkInfo();

			if ((info != null) && (info.isAvailable())
					&& (info.getTypeName().equals("WIFI"))) {
				return true;
			}
			if (CLCommon.DebugMode) {
				Log.e("error", "Network not wifi");
			}
			return false;
		}

		if (CLCommon.DebugMode) {
			Log.e(" lost  permission", "lost----> android.permission.INTERNET");
		}
		return false;
	}

	public static String getConnectType(Context context) {
		if (checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			NetworkInfo mWifi = cManager.getNetworkInfo(1);
			NetworkInfo mMobile = cManager.getNetworkInfo(0);

			if ((mWifi != null) && (mWifi.isAvailable())
					&& (mWifi.isConnected())) {
				return "WIFI";
			}
			if ((mMobile != null) && (mMobile.isAvailable())
					&& (mMobile.isConnected())) {
				String connectType = getNetworkType(context);
				return connectType;
			}
			return "unknown";
		}

		if (CLCommon.DebugMode) {
			Log.e(" lost  permission", "lost----> android.permission.INTERNET");
		}

		return "unknown";
	}

	@SuppressWarnings("deprecation")
	public static boolean isWapConnected(Context context) {
		if (checkPermissions(context, "android.permission.INTERNET")) {
			ConnectivityManager cManager = (ConnectivityManager) context
					.getSystemService("connectivity");
			NetworkInfo mMobile = cManager.getNetworkInfo(0);
			if ((mMobile != null) && (mMobile.isAvailable())
					&& (mMobile.isConnected())) {
				String proxyHost = Proxy.getDefaultHost();
				if ((proxyHost != null) && (!proxyHost.equals(""))) {
					return true;
				}
			}

		} else if (CLCommon.DebugMode) {
			Log.e(" lost  permission", "lost----> android.permission.INTERNET");
		}

		return false;
	}

	public static String getVersion(Context context) {
		String versionName = "";
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			if ((versionName == null) || (versionName.length() <= 0))
				return "";
		} catch (Exception e) {
			if (CLCommon.DebugMode) {
				Log.e("reyunsdk", "Exception", e);
			}
		}

		return versionName;
	}

	public static void printLog(String tag, String log) {
		if (CLCommon.DebugMode)
			Log.d(tag, log);
	}

	public static void printWarningLog(String tag, String log) {
		if (CLCommon.DebugMode)
			Log.w(tag, log);
	}

	public static void printErrLog(String tag, String log) {
		if (CLCommon.DebugMode)
			Log.e(tag, log);
	}

	public static boolean checkAppid(String appId) {
		return !isNullOrEmpty(appId);
	}

	public static String GetMetaData(Context appContext, String DataName)
			throws PackageManager.NameNotFoundException {
		ApplicationInfo appInfo = appContext.getPackageManager()
				.getApplicationInfo(appContext.getPackageName(), 128);
		String msg = appInfo.metaData.getString(DataName);
		return msg;
	}

	public static int GetMetaint(Context appContext, String DataName)
			throws PackageManager.NameNotFoundException {
		ApplicationInfo appInfo = appContext.getPackageManager()
				.getApplicationInfo(appContext.getPackageName(), 128);
		int msg = appInfo.metaData.getInt(DataName);
		return msg;
	}

	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout & 0xF) >= 3;
	}

	public static boolean isNullOrEmpty(String str) {
		return (str == null) || (str.trim().length() == 0);
	}

	public static String checkStringValue(String str, String defaultStr,
			String errorMsg) {
		String value = isNullOrEmpty(str) ? defaultStr : str;
		if (defaultStr.equals(value)) {
			printWarningLog("reyunsdk", errorMsg);
		}
		return value;
	}
}