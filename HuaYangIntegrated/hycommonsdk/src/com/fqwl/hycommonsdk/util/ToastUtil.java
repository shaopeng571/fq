package com.fqwl.hycommonsdk.util;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

public class ToastUtil {
	public static void toastInfo(Context context,String message){
			
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
