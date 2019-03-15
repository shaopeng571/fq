package com.huayang.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * @author fan
 * @version 1.0
 * @time 2017年8月11日 上午12:09:32
 */
public class NetWorkUtils {

	/**
	 * 用多种方法获取手机mac地址
	 * 
	 * @param context
	 * @return 手机mac地址或"02:00:00:00:00:00"（当获取失败时）
	 */
	public static String getMacAddr(Context context) {
		String invalid = "02:00:00:00:00:00";
		try {
			String mac0 = getMac0(context);
			String mac1 = getMac1(context);
			String mac2 = getMac2(context);
			String mac3 = getMac3();
			String mac4 = getMac4();
			String mac5 = getMac5();

			/*
			 * FLogger.i(LogTag.UTIL_TAG, "mac0:" + mac0); FLogger.i(LogTag.UTIL_TAG,
			 * "mac1:" + mac1); FLogger.i(LogTag.UTIL_TAG, "mac2:" + mac2);
			 * FLogger.i(LogTag.UTIL_TAG, "mac3:" + mac3); FLogger.i(LogTag.UTIL_TAG,
			 * "mac4:" + mac4); FLogger.i(LogTag.UTIL_TAG, "mac5:" + mac5);
			 */

			if (checkMac(mac0)) {
				return mac0;
			}
			if (checkMac(mac1)) {
				return mac1;
			}
			if (checkMac(mac2)) {
				return mac2;
			}
			if (checkMac(mac3)) {
				return mac3;
			}
			if (checkMac(mac4)) {
				return mac4;
			}
			if (checkMac(mac5)) {
				return mac5;
			}
		} catch (Exception ex) {
			FLogger.Ex(LogTag.UTIL_TAG, ex);
		}
		return invalid;
	}

	private static boolean checkMac(String mac) {
		if (TextUtils.isEmpty(mac)) {
			return false;
		}
		if ("00:00:00:00:00:00".equals(mac)) {
			return false;
		}

		if ("02:00:00:00:00:00".equals(mac)) {
			return false;
		}

		return true;
	}

	/***
	 * 网络访问是否可以访问
	 * 
	 * @return true可以访问网络 false 不可以
	 */
	public static boolean netWorkUseable(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo networkinfo = connectivity.getActiveNetworkInfo();
			if (networkinfo == null || !networkinfo.isAvailable()) {  
		           return false;  
		    }  
		}
		// 看实际能否上网
		boolean suc = false;
		List<String> ipList = new ArrayList<String>();
		ipList.add("www.baidu.com");
		ipList.add("www.qq.com");
		ipList.add("www.sina.com.cn");
		ipList.add("www.sohu.com");
		ipList.add("www.iqiyi.com");
		ipList.add("www.163.com");
		ipList.add("www.youku.com");
		for (String ip : ipList) {
			for (int i = 0; i < 3; i++) {
				Socket socket = new Socket();
				try {
					socket.connect(new InetSocketAddress(ip, 80), 5000);
				} catch (IOException e) {
					FLogger.Ex(LogTag.UTIL_TAG, e);
				}
				if (socket.isConnected()) {
					suc = true;
					break;
				} else {
					suc = false;
				}
				try {
					socket.close();
				} catch (IOException e1) {
					FLogger.Ex(LogTag.UTIL_TAG, e1);
				}
			}
		}
		return suc;

	}

	/**
	 * 手机网络 1-2G 2->3,4G|wifi 4-其他
	 * 
	 * @param context
	 * @return
	 */
	public static int getNetworkType(Context context) {

		int strNetworkType = NET_TYPE.TYPE_OTHER.getCode();
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

		try {

			if (networkInfo != null && networkInfo.isConnected()) {
				if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
					strNetworkType = NET_TYPE.TYPE_WIFI.getCode();
				} else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
					String _strSubTypeName = networkInfo.getSubtypeName();

					// TD-SCDMA networkType is 17
					int networkType = networkInfo.getSubtype();
					switch (networkType) {
					case TelephonyManager.NETWORK_TYPE_GPRS:
					case TelephonyManager.NETWORK_TYPE_EDGE:
					case TelephonyManager.NETWORK_TYPE_CDMA:
					case TelephonyManager.NETWORK_TYPE_1xRTT:
					case TelephonyManager.NETWORK_TYPE_IDEN: // api<8 : replace by 11
						strNetworkType = NET_TYPE.TYPE_2G.getCode();
						break;
					case TelephonyManager.NETWORK_TYPE_UMTS:
					case TelephonyManager.NETWORK_TYPE_EVDO_0:
					case TelephonyManager.NETWORK_TYPE_EVDO_A:
					case TelephonyManager.NETWORK_TYPE_HSDPA:
					case TelephonyManager.NETWORK_TYPE_HSUPA:
					case TelephonyManager.NETWORK_TYPE_HSPA:
					case TelephonyManager.NETWORK_TYPE_EVDO_B: // api<9 : replace by 14
					case TelephonyManager.NETWORK_TYPE_EHRPD: // api<11 : replace by 12
					case TelephonyManager.NETWORK_TYPE_HSPAP: // api<13 : replace by 15
						strNetworkType = NET_TYPE.TYPE_3_4G.getCode();
						break;
					case TelephonyManager.NETWORK_TYPE_LTE: // api<11 : replace by 13
						strNetworkType = NET_TYPE.TYPE_3_4G.getCode();
						;
						break;
					default:
						// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
						if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA")
								|| _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
							strNetworkType = NET_TYPE.TYPE_3_4G.getCode();
						} else {
							strNetworkType = NET_TYPE.TYPE_OTHER.getCode();

						}
						break;
					}

				}
			}
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}

		return strNetworkType;
	}

	enum NET_TYPE {
		TYPE_2G(1), TYPE_3_4G(2), TYPE_WIFI(3), TYPE_OTHER(4);
		int code;

		NET_TYPE(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	}

	/*** 以下为获取mac的不同方式 */
	private static String getMac0(Context context) {
		try {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo winfo = wifi.getConnectionInfo();
			String mac = winfo.getMacAddress();
			return mac;
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, "getMac0 failed:" + e.getMessage());
		}
		return "";
	}

	private static String getMac1(Context context) {
		if (!isAccessWifiStateAuthorized(context)) {
			return "";
		}
		try {
			WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo winfo = wifi.getConnectionInfo();
			String mac = winfo.getMacAddress();
			return mac;
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, "getMac1 failed:" + e.getMessage());
		}
		return "";
	}

	private static String getMac2(Context context) {
		String str = "";
		String macSerial = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (Exception ex) {
			FLogger.e(LogTag.UTIL_TAG, "getMac2 failed:" + ex.getMessage());
		}
		if (macSerial == null || "".equals(macSerial)) {
			try {
				return loadFileAsString("/sys/class/net/eth0/address").toUpperCase().substring(0, 17);
			} catch (Exception e) {
				FLogger.e(LogTag.UTIL_TAG, "getMac2 failed:" + e.getMessage());
			}
		}
		return macSerial.toUpperCase();
	}

	private static String getMac3() {
		String strMacAddr = null;
		try {
			// 获得IpD地址
			InetAddress ip = getLocalInetAddress();
			byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < b.length; i++) {
				if (i != 0) {
					buffer.append(':');
				}
				String str = Integer.toHexString(b[i] & 0xFF);
				buffer.append(str.length() == 1 ? 0 + str : str);
			}
			strMacAddr = buffer.toString().toUpperCase();
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, "getMac3 failed:" + e.getMessage());
		}

		return strMacAddr;
	}

	private static String getMac4() {
		Enumeration<NetworkInterface> interfaces = null;
		String hardWareAddress = null;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			NetworkInterface iF = null;
			if (interfaces == null) {
				return null;
			}
			while (interfaces.hasMoreElements()) {
				iF = interfaces.nextElement();
				try {
					hardWareAddress = bytesToString(iF.getHardwareAddress());
					if (hardWareAddress != null)
						break;
				} catch (SocketException e) {
					FLogger.e(LogTag.UTIL_TAG, "getMac4 failed:" + e.getMessage());
				}
			}
		} catch (SocketException e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}
		return hardWareAddress;
	}

	private static String getMac5() {
		String result = "";
		try {
			String Mac = "";
			result = callCmd("busybox ifconfig", "HWaddr");
			if (result == null) {
				return "";
			}
			// 对该行数据进行解析
			// 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
			if (result.length() > 0 && result.contains("HWaddr") == true) {
				Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
				result = Mac;
			}
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}
		return result;
	}

	private static boolean isAccessWifiStateAuthorized(Context context) {
		if (PackageManager.PERMISSION_GRANTED == context
				.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
			return true;
		} else
			return false;
	}

	private static String loadFileAsString(String fileName) throws Exception {
		FileReader reader = new FileReader(fileName);
		String text = loadReaderAsString(reader);
		reader.close();
		return text;
	}

	private static String loadReaderAsString(Reader reader) throws Exception {
		StringBuilder builder = new StringBuilder();
		char[] buffer = new char[4096];
		int readLength = reader.read(buffer);
		while (readLength >= 0) {
			builder.append(buffer, 0, readLength);
			readLength = reader.read(buffer);
		}
		return builder.toString();
	}

	/**
	 * 获取移动设备本地IP
	 * 
	 * @return
	 */
	private static InetAddress getLocalInetAddress() {
		InetAddress ip = null;
		try {
			// 列举
			Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
			while (en_netInterface.hasMoreElements()) {// 是否还有元素
				NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();// 得到下一个元素
				Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
				while (en_ip.hasMoreElements()) {
					ip = en_ip.nextElement();
					if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
						break;
					else
						ip = null;
				}

				if (ip != null) {
					break;
				}
			}
		} catch (SocketException e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return ip;
	}

	/***
	 * byte转为String
	 * 
	 * @param bytes
	 * @return
	 */
	private static String bytesToString(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		for (byte b : bytes) {
			buf.append(String.format("%02X:", b));
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	private static String callCmd(String cmd, String filter) {
		String result = "";
		String line = "";
		try {
			Process proc = Runtime.getRuntime().exec(cmd);
			InputStreamReader is = new InputStreamReader(proc.getInputStream());
			BufferedReader br = new BufferedReader(is);

			while ((line = br.readLine()) != null && line.contains(filter) == false) {
				result += line;
			}
			result = line;
		} catch (Exception e) {
			// FLogger.Ex(UtilProperties.UTIL_TAG, e); //打印这个没意义
		}
		return result;
	}

}
