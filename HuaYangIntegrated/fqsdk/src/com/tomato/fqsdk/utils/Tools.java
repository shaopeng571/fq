package com.tomato.fqsdk.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import com.tomato.fqsdk.fqutils.FLogger;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;

public class Tools {

	public Tools() {
		// TODO Auto-generated constructor stub
	}
	public static String getChannelId(Context ctx) {
		PackageManager pm = ctx.getPackageManager();
		ApplicationInfo appinfo;
		try {
			appinfo = pm.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
			Bundle metaData = appinfo.metaData;
			return metaData.get("HyGame_ChannelId") + "";
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getRealIp() throws SocketException {
		String localip = null;// IP û IP 򷵻
		String netip = null;// IP

		Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
		InetAddress ip = null;
		boolean finded = false;// Ƿ ҵ IP
		while (netInterfaces.hasMoreElements() && !finded) {
			NetworkInterface ni = netInterfaces.nextElement();
			Enumeration<InetAddress> address = ni.getInetAddresses();
			while (address.hasMoreElements()) {
				ip = address.nextElement();
				if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1) {// IP
					netip = ip.getHostAddress();
					finded = true;
					break;
				} else if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
						&& ip.getHostAddress().indexOf(":") == -1) {// IP
					localip = ip.getHostAddress();
				}
			}
		}

		if (netip != null && !"".equals(netip)) {
			return netip;
		} else {
			return localip;
		}
	}

	/**
	 * 
	 * @param ipaddr
	 * @return public ip
	 */
	public static String GetNetIp(String ipaddr) {
		URL infoUrl = null;
		InputStream inStream = null;
		try {
			infoUrl = new URL(ipaddr);
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();
				return strber.toString();
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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

	// /**
	// * Ƿ Ϊ ֻ
	// *
	// * @param mobiles
	// * @return
	// */
	// public static boolean isMobileNo(String mobiles) {
	// Pattern p = Pattern
	// .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	// Matcher m = p.matcher(mobiles);
	// return m.matches();
	// }

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

	private static String intToIp(int i) {

		return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
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

//	//         Json    
//	public static void parseJson(String strResult) {
//		try {
////			JSONObject jsonObj = new JSONObject(strResult)
////					.getJSONObject("singer");
////			int id = jsonObj.getInt("id");
////			String name = jsonObj.getString("name");
////			String gender = jsonObj.getString("gender");
//
//		} catch (JSONException e) {
//			System.out.println("Json parse error");
//			e.printStackTrace();
//		}
//	}

	// Json
//	public static void parseJsonMulti(String strResult) {
//		try {
//			JSONArray jsonObjs = new JSONObject(strResult)
//					.getJSONArray("singers");
//			String s = "";
//			for (int i = 0; i < jsonObjs.length(); i++) {
//				JSONObject jsonObj = ((JSONObject) jsonObjs.opt(i))
//						.getJSONObject("singer");
//				int id = jsonObj.getInt("id");
//				String name = jsonObj.getString("name");
//				String gender = jsonObj.getString("gender");
//			}
//
//		} catch (JSONException e) {
//			System.out.println("Jsons parse error !");
//			e.printStackTrace();
//		}
//	}

	/**
	 * ʹ SharedPreferences
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveSharedPreference(Context context, String key, String value) {
		// ȡSharedPreferences
		SharedPreferences sharedPre = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		// ȡEditor
		Editor editor = sharedPre.edit();
		// ò
		editor.putString(key, value);
		// ύ
		editor.commit();
	}

	/**
	 * û Ȩ
	 * 
	 * @param paramContext
	 * @param paramString
	 * @return
	 */
	public static boolean checkPermission(Context paramContext, String paramString) {
		return paramContext.checkCallingOrSelfPermission(paramString) == 0;
	}

	/**
	 * ʹ SharedPreferences ȡ
	 * 
	 * @param context
	 * @param key
	 * @return value
	 */
	public static String getSharedPreference(Context context, String key) {
		SharedPreferences sharedPre = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPre.getString(key, "");

	}

	protected static final String PREFS_FILE = "gank_device_id.xml";
	protected static final String PREFS_DEVICE_ID = "gank_device_id";
	protected static String uuid;

	static public String getUDID(Context context) {
		if (uuid == null) {
			synchronized (Tools.class) {
				if (uuid == null) {
					final SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, 0);
					final String id = prefs.getString(PREFS_DEVICE_ID, null);

					if (id != null) {
						// Use the ids previously computed and stored in the
						// prefs file
						uuid = id;
					} else {

						final String androidId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);

						// Use the Android ID unless it's broken, in which case
						// fallback on deviceId,
						// unless it's not available, then fallback on a random
						// number which we store
						// to a prefs file
						try {
							if (!"9774d56d682e549c".equals(androidId)) {
								uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
							} else {
								final String deviceId = ((TelephonyManager) context
										.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
								uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString()
										: UUID.randomUUID().toString();
							}
						} catch (UnsupportedEncodingException e) {
							throw new RuntimeException(e);
						}

						// Write the value out to the prefs file
						prefs.edit().putString(PREFS_DEVICE_ID, uuid).commit();
					}
				}
			}
		}
		return uuid;
	}

	/**
	 * * ļ * @param toSaveString * @param filePath
	 */
	public static void saveFile(String toSaveString, String filePath) {
		try {
			File saveFile = new File(filePath);
			if (!saveFile.exists()) {
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}

			FileOutputStream outStream = new FileOutputStream(saveFile);
			outStream.write(toSaveString.getBytes());
			outStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * ȡ ļ
	 */
	public static String readFile(String filePath) {
		String str = "";
		try {
			File readFile = new File(filePath);
			if (!readFile.exists()) {
				return "";
			}
			FileInputStream inStream = new FileInputStream(readFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			str = stream.toString();
			stream.close();
			inStream.close();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	// ֻ
	public static String screenNumber(String number) {
		String phone = "";
		try {
			if (number.length() == 11) {
				phone = number.substring(0, 4) + "****" + number.substring(8, 11);
			}
		} catch (Exception e) {
			return number;
		}

		return phone;

	}

	/**
	 * ʵ ı ƹ add by wangqianzhou
	 * 
	 * @param content
	 */
	@SuppressWarnings("deprecation")
	public static boolean copy(String content, Context context) {
		try {
			// õ
			ClipboardManager cmb = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
			cmb.setText(content.trim());
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	//
	public static String DecryptDoNet(String message, String key) throws Exception {
		byte[] bytesrc = Base64.decode(message, 0);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}

	//
	public static String EncryptAsDoNet(String message, String key) throws Exception {
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
		byte[] encryptbyte = cipher.doFinal(message.getBytes());
		return new String(Base64.encode(encryptbyte, Base64.DEFAULT));
	}

	public static boolean IsAccount(String userName) {
		String regEx = "^[a-zA-Z0-9_][a-zA-Z0-9_]{5,19}$";
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(userName);
		return matcher.matches();
	}

	public static String GetMD5Code(TreeMap<String, String> treeMap, String k) {
		String str = "";
		Set<Entry<String, String>> set = treeMap.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			str += entry.getValue();
		}
		return MD5.get(str + k);
	}

	/**
	 * 检查类是否缺参数
	 * zjwl  2018年9月18日
	 * @param o
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static boolean initInfoCheck(Object o) throws InvocationTargetException, IllegalAccessException {
		Method[] declaredMethods = o.getClass().getDeclaredMethods();
		for (Method method : declaredMethods) {
			String name = method.getName();
			if (name.startsWith("get")) {
				Object invoke = method.invoke(o);
				if (invoke == null || "".equals(invoke.toString())) {
					FLogger.e("init : missing "+name.substring(3));
					return false;
				}
			}
		}
		return true;
	}
}
