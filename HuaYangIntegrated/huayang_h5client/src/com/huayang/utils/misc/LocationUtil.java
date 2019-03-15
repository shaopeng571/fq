package com.huayang.utils.misc;

import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.huayang.utils.LogTag;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

public class LocationUtil {

	/**
	 * 获取经纬度
	 *
	 * @param context
	 * @return
	 */
	public static String getLocationStr(Context context) {
		double latitude = 0;
		double longitude = 0;
		try {
			LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // 从gps获取经纬度
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location != null) {
					latitude = location.getLatitude();
					longitude = location.getLongitude();
				} else {// 当GPS信号弱没获取到位置的时候又从网络获取
					return getLngAndLatWithNetwork(context);
				}
			} else {
				getLngAndLatWithNetwork(context);
			}
		} catch (Exception e) {
			
			FLogger.e(LogTag.UTIL_TAG, e.getMessage());
		}

		return latitude + "," + longitude;
	}

	// 从网络获取经纬度
	private static String getLngAndLatWithNetwork(Context context ) {
		
		double latitude = 0;
		double longitude = 0;
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, locationListener,Looper.getMainLooper());
		if (location != null) {
			latitude = location.getLatitude();
			longitude = location.getLongitude();
		}
		return latitude + "," + longitude;
	}

	static LocationListener locationListener = new LocationListener() {
		
		// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}

		// Provider被enable时触发此函数，比如GPS被打开
		@Override
		public void onProviderEnabled(String provider) {
			FLogger.d(LogTag.UTIL_TAG,"onProviderEnabled ");
		}

		// Provider被disable时触发此函数，比如GPS被关闭
		@Override
		public void onProviderDisabled(String provider) {
			FLogger.d(LogTag.UTIL_TAG,"onProviderDisabled ");
		}

		// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
		@Override
		public void onLocationChanged(Location location) {
			if(location !=null)
				FLogger.d(LogTag.UTIL_TAG, location.getLatitude()+"_"+location.getLongitude());
		}
	};
}
