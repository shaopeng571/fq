/**
 * @Description:TODO
 * @author:fan
 * @time:2017年8月18日 下午4:47:22
 */
package com.huayang.utils;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;





public class TimeUtils {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static Date date = new Date();
	
	
	
	/**
	 * 网络时间
	 * unix  秒
	 * @return
	 */
	public static int getTs() {
		return (int)getTslong() / 1000;
	}
	/**
	 * unix 毫秒
	 * @return
	 */
	public static long getTslong() {
		long ts = 0;
		URL url = null;
		try {
			url = new URL("https://www.baidu.com");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			conn.connect();
			ts =  conn.getDate();
			FLogger.w(LogTag.UTIL_TAG, "获取到的百度时间:" + ts);
			if(ts == 0) {
				ts =  System.currentTimeMillis();
				FLogger.w(LogTag.UTIL_TAG, "时间更正.."+ts);
			}
			conn.disconnect();
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
			ts =System.currentTimeMillis();
			FLogger.w(LogTag.UTIL_TAG, "获取到的本地时间:" + ts);
			
		}
		return ts;
	}
	
	
	
	/**
	 * 毫秒值转日期
	 * @param time
	 * @return
	 * @throws java.text.ParseException
	 */
	public static String parse(long time) throws  java.text.ParseException{
		date.setTime(time);
		return sdf.format(date);
    }
	
	/**
	 * 判定当前时间是否是昨天
	 * @param timestamp
	 * @return
	 */
	public static boolean isYesterday(long timestamp) {
	    Calendar c = Calendar.getInstance();
	    clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND);
	    c.add(Calendar.DAY_OF_MONTH, -1);
	    long firstOfDay = c.getTimeInMillis(); // 昨天最早时间

	    c.setTimeInMillis(timestamp);
	    clearCalendar(c, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND); // 指定时间戳当天最早时间

	    return firstOfDay == c.getTimeInMillis();
	}
	
	/**
	 * 判断指定日期是否为今天
	 *
	 *            指定的日期
	 * @return
	 */
	public static boolean isToday(long  time) {
		Date workDay = new Date();
		workDay.setTime(time);
	    Calendar c1 = Calendar.getInstance();
	    Calendar c2 = Calendar.getInstance();

	    Date currTime = new Date();

	    c1.setTime(workDay);
	    c2.setTime(currTime);

	    if (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
	            && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
	            && c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH)) {
	        return true;
	    } else {
	        return false;
	    }
	}
	private static void clearCalendar(Calendar c, int... fields) {
	    for (int f : fields) {
	        c.set(f, 0);
	    }
	}
	
	
	
	/**
	 * 检查宕此时是否处于宕机时间
	 * @param startDate 宕机开始时间
	 * @param endDate 宕机结束时间
	 * @return
	 */
	public static boolean checkDownTime(String startDate, String endDate){
		
		try {
			long currentTime = getTslong();
			long startTime = parseDate(startDate).getTime();
			long endTime = parseDate(endDate).getTime();
			FLogger.w(LogTag.UTIL_TAG, "startTime:"+startTime+" currentTime:"+currentTime+" endTime:"+endTime);
			if (startTime < currentTime && currentTime < endTime) {
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
			//异常，则表示 宕机
			return false;
		}
	}
	
	private static Date parseDate(String s) {
		Date d1 = null;
		try {
			d1 = sdf.parse(s);
		} catch (Exception e) {
			FLogger.Ex(LogTag.UTIL_TAG, e);
		}
		return d1;
	}
	
}

