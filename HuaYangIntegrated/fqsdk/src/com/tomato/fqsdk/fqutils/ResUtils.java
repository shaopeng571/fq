package com.tomato.fqsdk.fqutils;

/**
 * @Description:TODO
 * @author:fan
 * @time:2017 ?8 ?25 ? 下午3:15:34
 */

import java.lang.reflect.Field;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;

public class ResUtils {
	
	TypedValue sTempValue = null;

	private static ResUtils mInstance;

	private static Context ctx;

	public static void init(Context context) {
		ctx = context;
	}

	private ResUtils() {
	}

	public static ResUtils getInstance() {
		if (mInstance == null) {
			synchronized (ResUtils.class) {
				if (mInstance == null) {
					mInstance = new ResUtils();
				}
			}
		}
		return mInstance;
	}

	public int getIdResByName(String name) {
		return ctx.getResources().getIdentifier(name, "id", ctx.getPackageName());
	}

	public int getStringResByName(String name) {
		return ctx.getResources().getIdentifier(name, "string", ctx.getPackageName());
	}

	public int getLayoutResByName(String name) {
		int x = ctx.getResources().getIdentifier(name, "layout", ctx.getPackageName());
		return x;
	}

	public int getDrawableResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "drawable", ctx.getPackageName());
	}

	public int getColorResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "color", ctx.getPackageName());
	}

	public int getAttrResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "attr", ctx.getPackageName());
	}

	public int getStyleResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "style", ctx.getPackageName());
	}

	public int getAnimResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "anim", ctx.getPackageName());
	}

	public int getArrayResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "array", ctx.getPackageName());

	}

	public int getDimenResIDByName(String name) {
		return ctx.getResources().getIdentifier(name, "dimen", ctx.getPackageName());
	}

	// 通过反射实现
	public final int[] getStyleableIntArray(String name) {
		try {
			if (ctx == null)
				return null;
			Field field = Class.forName(ctx.getPackageName() + ".R$styleable").getDeclaredField(name);
			int[] ret = (int[]) field.get(null);
			return ret;
		} catch (Throwable e) {
			FLogger.Ex(Global.UTIL_TAG, e);
		}
		return null;
	}

	public final int getStyleableIntArrayIndex(String name) {
		try {
			if (ctx == null)
				return 0;
			Field field = Class.forName(ctx.getPackageName() + ".R$styleable").getDeclaredField(name);
			int ret = (Integer) field.get(null);
			return ret;
		} catch (Throwable e) {
			FLogger.Ex(Global.UTIL_TAG, e);
		}
		return 0;
	}

	/**
	 * 根据资源名获得drawable
	 * 
	 * @param name
	 * @return
	 */
	public Drawable getDrawable(String name) {

		Drawable drawable;
		if (Build.VERSION.SDK_INT >= 21) {
			drawable = ctx.getDrawable(ResUtils.getInstance().getDrawableResIDByName(name));
		} else if (Build.VERSION.SDK_INT >= 16) {
			drawable = ctx.getResources().getDrawable(ResUtils.getInstance().getDrawableResIDByName(name));
		} else {
			final int resolvedId;
			synchronized (ResUtils.class) {
				if (sTempValue == null) {
					sTempValue = new TypedValue();
				}
				ctx.getResources().getValue(ResUtils.getInstance().getDrawableResIDByName(name), sTempValue, true);
				resolvedId = sTempValue.resourceId;
			}
			drawable = ctx.getResources().getDrawable(resolvedId);
		}
		return drawable;
	}

	/**
	 * 根据ID获得Drawable
	 * 
	 * @param id
	 * @return
	 */
	public Drawable getDrawable(int id) {
		Drawable drawable;
		if (Build.VERSION.SDK_INT >= 21) {
			drawable = ctx.getDrawable(id);
		} else if (Build.VERSION.SDK_INT >= 16) {
			drawable = ctx.getResources().getDrawable(id);
		} else {
			final int resolvedId;
			synchronized (ResUtils.class) {
				if (sTempValue == null) {
					sTempValue = new TypedValue();
				}
				ctx.getResources().getValue(id, sTempValue, true);
				resolvedId = sTempValue.resourceId;
			}
			drawable = ctx.getResources().getDrawable(resolvedId);
		}
		return drawable;
	}
}
