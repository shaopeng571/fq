package com.fqwl.hycommonsdk.model;

import java.util.Locale;

import com.fqwl.hycommonsdk.util.CommonUtils;
import com.fqwl.hycommonsdk.util.CryptHelper;
import com.fqwl.hycommonsdk.util.MD5;
import com.fqwl.hycommonsdk.util.NetWorkUtil;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class CommonSdkBaseInfo {
    private String packetname="";
	private String ip ="";
	private String mac ="";
	private String imei ="";
	private String resolution ="";
	private String osVersion ="";
	private String brand ="";
	private String language ="";
	private String netType="" ;
	private String country ="";
	private String province ="-1";
	private String operators ="";
	private String Serialid="1";
	private String udid="";
	private String Systemid="";
	private String device="";
	private String deviceid="";
	
	public String getDeviceId() { return deviceid ; }
	public String getDevice() { return device ; }
	public String getPacketName() { return packetname ; }
	public String getIp() { return ip ; }
	public String getMac() { return mac ; }
	public String getImei() { return imei ; }
	public String getResolution() { return resolution ; }
	public String getOsVersion() { return osVersion ; }
	public String getBrand() { return brand ; }
	public String getLanguage() { return language ; }
	public String getNetType() { return netType ; }
	public String getCountry() { return country ; }
	public String getProvince() { return province ; }
	public String getOperators() { return operators ; }
	public String getSerialid() { return Serialid ; }
	public String getUdid() { return udid ; }
	public String getSystemid() { return Systemid ; }
	
	public void setDeviceId(String val) { this.deviceid = val ; }
	public void setDevice(String val) { this.device = val ; }
	public void setPacketName(String val) { this.packetname = val ; }
	public void setIp(String val) { this.ip = val ; }
	public void setMac(String val) { this.mac = val ; }
	public void setImei(String val) { this.imei = val ; }
	public void setResolution(String val) { this.resolution = val ; }
	public void setOsVersion(String val) { this.osVersion = val ; }
	public void setBrand(String val) { this.brand = val ; }
	public void setLanguage(String val) { this.language = val ; }
	public void setNetType(String val) { this.netType = val ; }
	public void setCountry(String val) { this.country = val ; }
	public void setProvince(String val) { this.province = val ; }
	public void setOperators(String val) { this.operators = val ; }
	public void setSerialid(String val) { this.Serialid = val ; }
	public void setUdid(String val) { this.udid = val ; }
	public void setSystemid(String val) { this.Systemid = val ; }

	public CommonSdkBaseInfo(Context context){
		this.packetname=context.getPackageName();
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE); 
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		 String imei = tm.getDeviceId();  
	        if (imei == null) {  
	            imei = "000000000000000";  
	        }  
		this.imei = imei;
		this.deviceid=CryptHelper.GetMD5Code(tm.getDeviceId()+CommonUtils.getMacAddress(context));//
		this.country = tm.getSimCountryIso();
		this.device=android.os.Build.MODEL;
		this.brand = android.os.Build.VERSION.RELEASE;
		this.osVersion = android.os.Build.VERSION.RELEASE;
		 this.ip=CommonUtils.GetIp(context);
		 try {
			 this.mac = CommonUtils.getMacAddress(context);
		} catch (Exception e) {
			this.mac="-";
		}
		 try {
			 Locale locale = context.getResources().getConfiguration().locale; 
			 this.language = locale.getLanguage();
		} catch (Exception e) {
			this.language =tm.getSimCountryIso();
		}
		 
		 this.netType = NetWorkUtil.getCurrentNetworkType(context);
		this.operators = NetWorkUtil.getProvider(context);
		if ( dm.widthPixels>dm.heightPixels) {
			this.resolution = dm.widthPixels + "*" + dm.heightPixels;
		}else {
			this.resolution = dm.heightPixels + "*" + dm.widthPixels;
		}
		try {
			if (!TextUtils.isEmpty(tm.getSimSerialNumber())) {
				this.Serialid=tm.getSimSerialNumber();
			}
		} catch (Exception e) {
			this.Serialid="0";
		}
//		try {
//		if (!TextUtils.isEmpty(Tools.getUDID(context))) {
//			this.udid=Tools.getUDID(context);
//		}
//		} catch (Exception e) {
//			this.udid="0";
//		}
		this.udid=CommonUtils.getDeviceID(context);
		this.Systemid=MD5.get(imei);
		
	}
}
	