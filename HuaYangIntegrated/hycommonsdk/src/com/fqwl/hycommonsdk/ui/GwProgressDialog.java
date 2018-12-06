package com.fqwl.hycommonsdk.ui;

import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.UIUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
/**
 * @Description:TODO
 * @author:fan
 * @time:2017年9月30日 下午4:22:56
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;


@SuppressLint("NewApi")
public class GwProgressDialog {
	// 默认蓝色
	private String titleColor = "#169BD5";

	private String msgColor = "#000000";

	private String lineColor = "#169B88";

	private Context context;

	private String msg = "请稍后...";

	private String title = "提示";

	private AlertDialog mDialog;

	private boolean canCancel = true;

	public GwProgressDialog(Activity context, String msg) {
		this.context = context;
		this.msg = msg;
	}

	public GwProgressDialog(Activity context, String title, String msg) {
		this.context = context;
		this.msg = msg;
		this.title = title;
	}

	private AlertDialog initView() {

		int pading = UIUtil.dip2px(context, 5);
		LinearLayout rootLayout = new LinearLayout(context);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		rootLayout.setBackgroundColor(Color.WHITE);
		rootLayout.setGravity(Gravity.CENTER);
		tv_title = new TextView(context);
		tv_title.setTextColor(Color.parseColor(titleColor));
		tv_title.setTextSize(23);
		tv_title.setText(title);
		tv_title.setPadding(UIUtil.dip2px(context, 15), pading, pading, pading);
		LayoutParams titleParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		titleParams.gravity = Gravity.CENTER;
		rootLayout.addView(tv_title, titleParams);

		View lineView = new View(context);
		lineView.setBackgroundColor(Color.parseColor(lineColor));
		LayoutParams lineParams = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
		lineParams.topMargin = UIUtil.dip2px(context, 10);
		rootLayout.addView(lineView, lineParams);

		LinearLayout bottomLayout = new LinearLayout(context);
		bottomLayout.setOrientation(LinearLayout.HORIZONTAL);
		bottomLayout.setPadding(0, 0, 0, pading);
		LayoutParams bootomParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		bootomParams.leftMargin = UIUtil.dip2px(context, 17);
		bootomParams.topMargin = UIUtil.dip2px(context, 10);

		ProgressBar progressBar = new ProgressBar(context);
		LayoutParams progressParams = new LayoutParams(UIUtil.dip2px(context, 30), UIUtil.dip2px(context, 30));
		bottomLayout.addView(progressBar, progressParams);

		tv_msg = new TextView(context);
		tv_msg.setTextColor(Color.parseColor(msgColor));
		tv_msg.setText(msg);
		tv_msg.setTextSize(20);
		tv_msg.setGravity(Gravity.CENTER_VERTICAL);
		LayoutParams msgParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		msgParams.leftMargin = UIUtil.dip2px(context, 20);
		msgParams.bottomMargin = UIUtil.dip2px(context, 10);
		bottomLayout.addView(tv_msg, msgParams);
		rootLayout.addView(bottomLayout, bootomParams);
		return createDialog(rootLayout);
	}

	private AlertDialog createDialog(LinearLayout rootLayout) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (Build.VERSION.SDK_INT >= 21) {
			builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
		} else {
			builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		}
		builder.setCancelable(canCancel);
		builder.setView(rootLayout);
		builder.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				if (Listener != null) {
					Listener.onDismiss(dialog);
					mDialog = null;
				}
			}
		});
		return builder.create();
	}

	private OnDismissListener Listener;

	private TextView tv_msg;

	private TextView tv_title;

	public void show() {
		if (mDialog == null) {
			mDialog = initView();
		}

		if (!mDialog.isShowing()) {
			mDialog.show();
		}
	}

	public void hide() {
		if (mDialog != null && mDialog.isShowing()) {
			FLogger.v("fqui", "hide");
			mDialog.hide();
		}
	}

	public void disMiss() {
		try {

			if (mDialog != null && mDialog.isShowing()) {
				FLogger.v("fqui", "disMiss");
				mDialog.dismiss();
			}
		} catch (Exception e) {
			FLogger.e("fqui", e.getMessage());
		}
	}

	public GwProgressDialog setCancelable(boolean canCancel) {
		this.canCancel = canCancel;
		return this;
	}

	public GwProgressDialog setOnDismissListener(OnDismissListener listener) {
		Listener = listener;
		return this;
	}

	public GwProgressDialog setTitleColor(String titleColor) {
		try {
			Color.parseColor(titleColor);
			this.titleColor = titleColor;
		} catch (Exception e) {
			FLogger.e("fqui", e.getMessage());
		}
		return this;
	}

	public GwProgressDialog setMsgColor(String msgColor) {
		try {
			Color.parseColor(msgColor);
			this.msgColor = msgColor;
		} catch (Exception e) {
			FLogger.e("fqui", e.getMessage());
		}
		return this;
	}

	public GwProgressDialog setLineColor(String lineColor) {
		try {
			Color.parseColor(lineColor);
			this.lineColor = lineColor;
		} catch (Exception e) {
			FLogger.e("fqui", e.getMessage());
		}
		return this;
	}

	public GwProgressDialog setMsg(String msg) {
		this.msg = msg;
		if (tv_msg != null)
			tv_msg.setText(msg);
		return this;
	}

	public GwProgressDialog setTitle(String title) {
		this.title = title;
		if (tv_title != null)
			tv_title.setText(title);
		return this;
	}

}
