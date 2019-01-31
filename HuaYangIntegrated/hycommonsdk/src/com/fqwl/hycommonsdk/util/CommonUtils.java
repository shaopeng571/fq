package com.fqwl.hycommonsdk.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

public class CommonUtils{
	public static String getProcessName(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> runningApps = am
				.getRunningAppProcesses();
		if (runningApps == null) {
			return null;
		}
		for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
			if (proInfo.pid == android.os.Process.myPid()) {
				if (proInfo.processName != null) {
					return proInfo.processName;
				}
			}
		}
		return null;
	}
	public static String GetTimeZ(Date date) {
		if (date == null)
			return null;

		long time = date.getTime();
		String timeline = time + "";
		if (timeline.length() > 10) {
			timeline = timeline.substring(0, 10);
		}

		return timeline;
	}

	public static String GetTimeZ() {
		return GetTimeZ(new Date());
	}
	
	public static String getLocalMacAddressFromIp(Context context) {
		String mac_s = "";
		try {
			byte[] mac;
			NetworkInterface ne = NetworkInterface.getByInetAddress(InetAddress.getByName(GetGprsIp()));
			mac = ne.getHardwareAddress();
			mac_s = byte2hex(mac);
		} catch (Exception e) {
			return "";
		}
		return mac_s;
	}
	private static String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer(b.length);
		String stmp = "";
		int len = b.length;
		for (int n = 0; n < len; n++) {
			stmp = Integer.toHexString(b[n] & 0xFF);
			if (stmp.length() == 1) {
				hs = hs.append("0").append(stmp);
			} else {
				hs = hs.append(stmp);
			}
			if (n + 1 < len)
				hs = hs.append(":");
		}
		return String.valueOf(hs);
	}
	public static String getLocalMacAddressFromWifiInfo(Context context) {
		WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//		if (!wifi.isWifiEnabled()) {
//			return null;
//		}
		WifiInfo info = wifi.getConnectionInfo();
		String macAdress = info.getMacAddress(); // ȡmac ַ
		return macAdress;
	}
	public static String getMacAddress(Context context) {
		String macAddress = getLocalMacAddressFromWifiInfo(context);
		if (macAddress != null) {
			return macAddress;
		}
		
		return getLocalMacAddressFromIp(context);
	}
	private static String GetGprsIp() {
//		boolean finded = false;//  Ƿ  ҵ     IP
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {

		}

		return "";
	}
	public static String GetIp(Context paramContext) {
		// ȡwifi
		WifiManager wifiManager = (WifiManager) paramContext.getSystemService(Context.WIFI_SERVICE);
		// ж wifi Ƿ
		if (!wifiManager.isWifiEnabled()) {
			// wifiManager.setWifiEnabled(true);
			String gprsIp = GetGprsIp();
			return gprsIp;
		}
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		int ipAddress = wifiInfo.getIpAddress();
		String ip = intToIp(ipAddress);
		return ip;
	}
	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
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
	public static boolean checkPermissions(Context context, String permission) {
		PackageManager localPackageManager = context.getPackageManager();
		return localPackageManager.checkPermission(permission,
				context.getPackageName()) == 0;
	}
	public static boolean checkPhoneState(Context context) {
		PackageManager packageManager = context.getPackageManager();

		return packageManager
				.checkPermission("android.permission.READ_PHONE_STATE",
						context.getPackageName()) == 0;
	}
}