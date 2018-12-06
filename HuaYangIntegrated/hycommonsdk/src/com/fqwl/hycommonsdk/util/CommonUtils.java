package com.fqwl.hycommonsdk.util;

import java.util.Date;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;

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
}