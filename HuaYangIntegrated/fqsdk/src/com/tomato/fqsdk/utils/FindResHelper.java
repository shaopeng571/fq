package com.tomato.fqsdk.utils;

import android.content.Context;

public class FindResHelper
{
  private static Context AppContext;
  private static String AppPkg;

  public static void init(Context paramContext)
  {
    AppContext = paramContext.getApplicationContext();
    AppPkg = paramContext.getPackageName();
  }

  public static int R(String paramString1, String paramString2) {
    return AppContext.getResources().getIdentifier(paramString1, paramString2, AppPkg);
  }

  public static int RString(String paramString) {
    return R(paramString, "string");
  }

  public static String RStringStr(String paramString) {
    return AppContext.getString(R(paramString, "string"));
  }

  public static int RLayout(String paramString) {
    return R(paramString, "layout");
  }

  public static int RDrawable(String paramString) {
    return R(paramString, "drawable");
  }

  public static int RId(String paramString) {
    return R(paramString, "id");
  }

  public static int RColor(String paramString) {
    return R(paramString, "color");
  }

  public static int RStyle(String paramString) {
    return R(paramString, "style");
  }

  public static int RAnim(String paramString) {
    return R(paramString, "anim");
  }

  public static int RDimen(String paramString) {
    return R(paramString, "dimen");
  }
}