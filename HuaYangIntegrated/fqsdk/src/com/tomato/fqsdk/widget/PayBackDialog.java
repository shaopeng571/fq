package com.tomato.fqsdk.widget;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.tomato.fqsdk.utils.FindResHelper;

public class PayBackDialog {
	//    ú   ʾPopupWindow
	@SuppressWarnings("deprecation")
	public static void showPopupWindow(final PopupWindow popupWindow,final Activity activity) {
		LayoutInflater inflater = activity.getLayoutInflater();
		View view = inflater.inflate(FindResHelper.RLayout("hj_pay_isback"), null);
//		 popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
//				LayoutParams.WRAP_CONTENT);
		TextView tv_back = (TextView) view.findViewById(FindResHelper.RId("tv_back"));
		tv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				activity.finish();
			}
		});
		TextView tv_notback = (TextView) view.findViewById(FindResHelper
				.RId("tv_notback"));
		tv_notback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(activity.getResources().getDrawable(
				FindResHelper.RDrawable("hj_pay_backshape")));
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.alpha = 0.3f;
		activity.getWindow().setAttributes(lp);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
				lp.alpha = 1f;
				activity.getWindow().setAttributes(lp);
			}
		});
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);

		//   ʾPopupWindow  3      
		// popupWindow.showAsDropDown(view);
		// popupWindow.showAsDropDown(anchor, xoff, yoff)
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		//   Ҫע                      ڴ    ¼   ʹ ã      ڵ  ĳ    ť  ʱ  

	}
}
