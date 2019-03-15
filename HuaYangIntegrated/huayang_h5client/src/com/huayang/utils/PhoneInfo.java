package com.huayang.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.provider.Settings.Global;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;

import com.huayang.utils.encode.MD5Provider;
import com.huayang.utils.misc.LocationUtil;

/**
 * @author fan
 * @version 1.0
 * @time 2017年8月10日 下午10:22:37
 */
public class PhoneInfo {

	private TelephonyManager mTelephonyManager;
	private static volatile PhoneInfo instance;
	private static Context context;

	/** 手机品牌 **/
	public static String brand = Build.BRAND;
	/** 手机型号 **/
	public static String model = Build.MODEL;

	/** 安卓api level 25 **/
	public int androidLevel = Build.VERSION.SDK_INT;
	/** 安卓版本 7.1 **/
	public String androidVersion = Build.VERSION.RELEASE;

	/** 手机识别码IMEI */
	public String IMEI = "000000000000000";
	/** 移动用户识别码 */
	public String IMSI = "ffffffffffffffff";
	/** 运营商 */
	public String operatCodeStr = "1";

	public String android_Id ="";
	/** 分辨率 */
	public String resolution = "";

	public String mac = "";
	/**
	 * 是否模拟器 0不是 1是
	 */
	public String isEmulator = "0";
	/**
	 * 是否root 0不是1是
	 */
	public String isRoot = "0";
	public String serial = "";
	public String serialNumber = "";
	//计算唯一标识
	public String utma  = "";
	//硬件信息
	public String deviceInfo = "";
	//用于本地保存一个唯一标识(卸载后失效)
	public String uuidstr;
	//语言
	public String lang;

	public static PhoneInfo getInstance(Context ctx) {

		if (context == null) {
			context = ctx.getApplicationContext();
		}
		if (instance == null) {
			synchronized (PhoneInfo.class) {
				if (instance == null) {
					instance = new PhoneInfo(ctx);
				}
			}
		}
		return instance;
	}

	/**
	 * 获取网络类型， 因为网络可能切换 所以提供public方法
	 * 
	 * @return
	 */
	public String getNetWorkType() {
		return NetWorkUtils.getNetworkType(context) + "";
	}

	/**
	 * 获取当前位置
	 * 
	 * @return
	 */
	public String getLocation() {
		return LocationUtil.getLocationStr(context);
	}

	private PhoneInfo(Context ctx) {
		context = ctx.getApplicationContext();
		mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		collectMachineInfomation();
	}

	/**
	 * 收集手机信息
	 */
	private void collectMachineInfomation() {

		try {
			IMEI = getIMEI();
			IMSI = getIMSI();
			android_Id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
		} catch (SecurityException e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}
		operatCodeStr = getOperator() + "";
		resolution = getResolution();
		serial = getSerial();
		serialNumber = getSerialNumber();
		mac = NetWorkUtils.getMacAddr(context);
		isEmulator = getEmulatorNUm();
		isRoot = getRootNum();
		deviceInfo = getDeviceInfo();
		utma = generateUtma();
		uuidstr = generateUUID();
		lang = getLanguage();
		
	}
	public  String getLanguage() {
		try {
			String locale = Locale.getDefault().getLanguage();
			return locale;
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return "zh_CN";
	}
	/**
	 * 生成唯一标识
	 * 
	 * @return
	 */
	private String generateUtma() {
		String string = new UUID(getDeviceInfo().hashCode(), serial.hashCode()).toString();
		return MD5Provider.md5string(string);
	}

	@TargetApi(22)
	private static String getDeviceInfo() {

		String m_szDevIDShort = "11";
		try {
			m_szDevIDShort =""+ Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

					Build.DEVICE.length() % 10 +

					Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

					Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

					Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

					Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

					Build.USER.length() % 10;
			if (Build.VERSION.SDK_INT > 21) {
				int x = Arrays.toString(Build.SUPPORTED_ABIS).length() % 10;
				m_szDevIDShort = m_szDevIDShort + x;
			} else {
				m_szDevIDShort = m_szDevIDShort + Build.CPU_ABI.length() % 10;
			}
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return m_szDevIDShort;
	}

	// 0 不是 1是
	private String getRootNum() {

			if (isRoot()) {
				return "1";
			}
		return "0";
	}

	private boolean isRoot() {
		if (checkRootMethod1()) {
			return true;
		}
		if (CheckRootMethod2()) {
			return true;
		}
		return false;
	}

	public boolean checkRootMethod1() {
		String buildTags = Build.TAGS;
		if (buildTags != null && buildTags.contains("test-keys")) {
			return true;
		}
		return false;
	}

	public boolean CheckRootMethod2() {
		try {
			File file = new File("/system/app/Superuser.apk");
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return false;
	}

	// 0不是 1是
	private String getEmulatorNUm() {
		if (isEmulator()) {
			return "1";
		}
		return "0";
	}

	private boolean isEmulator() {
		try {
			boolean goldfish = getSystemProperty("ro.hardware").contains("goldfish");
			boolean emu = getSystemProperty("ro.kernel.qemu").length() > 0;
			boolean sdk = getSystemProperty("ro.product.model").equals("sdk");
			if (emu || goldfish || sdk) {
				return true;
			}
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return false;
	}

	private String getSystemProperty(String name) throws Exception {
		String result = null;
		try {
			Class systemPropertyClazz = Class.forName("android.os.SystemProperties");
			result = (String) systemPropertyClazz.getMethod("get", new Class[] { String.class })
					.invoke(systemPropertyClazz, new Object[] { name });
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}

		return result;
	}

	private String getIMEI() {
		String res = "000000000000000";
		try {
			if (null != mTelephonyManager) {
				res = mTelephonyManager.getDeviceId();
			}
		} catch (SecurityException e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return res;
	}

	private String getIMSI() {
		String res = "ffffffffffffffff";
		try {
			if (null != mTelephonyManager) {
				res = mTelephonyManager.getSubscriberId();
			}
		} catch (SecurityException e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return res;
	}

	/**
	 * 获取屏幕分辨率
	 * @return
	 */
	private String getResolution() {
		try {
			int w = UIUtil.getScreenWidth(context);
			int h = UIUtil.getScreenHeight(context);
			if (h < w) {
				return w + "x" + h;
			} else {
				return h + "x" + w;
			}
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return "0x0";
	}

	private String getSerial() {
		String serial = "serial";
		try {
			serial = Build.class.getField("SERIAL").get(null).toString();
		} catch (Exception e) {
			serial = "serial";
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return serial;
	}

	/**
	 * 获取手机序列号
	 * 
	 * @return
	 */
	private String getSerialNumber() {
		String serial = null;
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class);
			serial = (String) get.invoke(c, "ro.serialno");
		} catch (Exception e) {
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}
		return serial;
	}

	/**
	 * 获取当前的运营商
	 * @return 运营商名字
	 */
	private int getOperator() {
		Oper pro = Oper.OTHER;
		if (!TextUtils.isEmpty(IMSI)) {
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002") || IMSI.startsWith("46007")
					|| IMSI.startsWith("46020")) {
				pro = Oper.CHINA_MOBILE;
			} else if (IMSI.startsWith("46001") || IMSI.startsWith("46006") || IMSI.startsWith("46009")) {
				pro = Oper.CHINA_UNI;
			} else if (IMSI.startsWith("46003") || IMSI.startsWith("46005") || IMSI.startsWith("46011")) {
				pro = Oper.CHCHINATELE;
			}
		}
		return pro.getCode();
	}

	// 1、移动；2、联通；3、电信；4、其他
	enum Oper {
		CHINA_MOBILE(1), CHINA_UNI(2), CHCHINATELE(3), OTHER(4);
		int code;

		Oper(int code) {
			this.code = code;
		}

		protected int getCode() {
			return code;
		}
	}

	private String generateUUID() {
		String uuid_sp = SharePreferencesUtil.getString(context, SharePreferencesUtil.UUID);
		if(TextUtils.isEmpty(uuid_sp)) {
			String uuid = UUID.randomUUID().toString().replace("-", "");
			FLogger.e(LogTag.UTIL_TAG, "本地没有保存UUID,重新生成保存："+uuid);
			SharePreferencesUtil.setString(context,  SharePreferencesUtil.UUID, uuid);
			return  uuid;
		}else {
			FLogger.e(LogTag.UTIL_TAG, "本地已保存有UUID:"+uuid_sp);
			return  uuid_sp;
		}
	}
	
	@Override
	public String toString() {
		return "PhoneInfo [ androidLevel=" + androidLevel + ", androidVersion=" + androidVersion + ", IMEI=" + IMEI
				+ ", IMSI=" + IMSI + ", operatCodeStr=" + operatCodeStr + ", android_Id=" + android_Id + ", resolution="
				+ resolution + ", mac=" + mac + ", isEmulator=" + isEmulator + ", isRoot=" + isRoot + ", serial="
				+ serial + ", serialNumber=" + serialNumber + " Location:" + getLocation() + " NetWorkType:"
				+ getNetWorkType() +" uuid:" + uuidstr+ " brand="+brand+" modle:"+model+"]";
	}

}
