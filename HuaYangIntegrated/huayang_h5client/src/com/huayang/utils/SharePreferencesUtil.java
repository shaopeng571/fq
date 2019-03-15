package com.huayang.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferencesUtil {
	   //注意这个名称不要更改
	   private static final String USER_INFO = "gowan_user_shell";

	   public static final String UUID = "UUID";
	   
	  public static boolean setString(Context context, String key,String value)
	  {
		    SharedPreferences.Editor edit = context.getSharedPreferences(USER_INFO, Context.MODE_PRIVATE).edit();
		    edit.putString(key, value);
		    return  edit.commit();
	  }

	  public static String getString(Context context, String key) {
	    return context.getSharedPreferences(USER_INFO,  Context.MODE_PRIVATE).getString(
	    		key, "");
	  }
	  
	  public static void setLong(Context context, String key,long value)
	  {
	    SharedPreferences.Editor edit = context.getSharedPreferences(USER_INFO,  Context.MODE_PRIVATE).edit();
	    edit.putLong(key, value);
	    edit.commit();
	  }

	  public static long getLong(Context context, String key) {
	    return context.getSharedPreferences(USER_INFO,  Context.MODE_PRIVATE).getLong(key, 0);
	  }
	  public static void setInt(Context context, String key,int value)
	  {
	    SharedPreferences.Editor edit = context.getSharedPreferences(USER_INFO,  Context.MODE_PRIVATE).edit();
	    edit.putInt(key, value);
	    edit.commit();
	  }
	  public static int getInt(Context context, String key) {
		    return context.getSharedPreferences(USER_INFO,  Context.MODE_PRIVATE).getInt(key, 0);
	}
	  
}
