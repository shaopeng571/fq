//package com.tomato.fqsdk.utils;
//
//import com.tomato.fqsdk.control.HySDK;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//
///**
// * SharedPreference存取数据工具类
// * @author Administrator：mhm 2016.06.01
// *
// */
//public class SharedPreferencesUtil {
//
//private static SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(HySDK.getAppContext());
//
// public static String getPrefString(String key,final String defaultValue) {return settings.getString(key, defaultValue);}
//public static void setPrefString(final String key,final String value) {settings.edit().putString(key, value).commit();  
//        }
//public static void clearPreference(final SharedPreferences p){final Editor editor = p.edit();editor.clear();editor.commit(); 
//        }
//
//}