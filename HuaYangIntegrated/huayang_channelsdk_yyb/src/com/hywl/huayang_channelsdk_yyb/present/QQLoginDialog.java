package com.hywl.huayang_channelsdk_yyb.present;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

public class QQLoginDialog extends Dialog {
	private ImageView qqlogin, wxlogin;
//	private TextView textView;
	private ImageView iv_bg;
	private boolean isNoticeLogin = false;
	private boolean isCancelLogin = false;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			isNoticeLogin = true;
			if(callback != null)
				callback.onAutoLogin();
		};
	};
	
	public QQLoginDialog(Context context) {
		super(context);
		initView(context);
	}

	public QQLoginDialog(Context context, int theme) {
		super(context, theme);
		initView(context);
	}
	
	
	private void initView(Context context) {
		setCancelable(true);
		setCanceledOnTouchOutside(false);
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//		getWindow().set
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		int vh;//弹框高度
		int vw;//弹框宽度
		int bottomM;
		int h;//按钮高度
		int w;//按钮宽度
		int marginValue;
		int lm;
		View localView = ((LayoutInflater)context.getSystemService("layout_inflater")).inflate(getContext().getResources()
				.getIdentifier("gowan_commonsdk_tencent_login", "layout",
						getContext().getPackageName()), null);

		   qqlogin = (ImageView)localView.findViewById(getContext().getResources()
				.getIdentifier("login_by_qq", "id",
						getContext().getPackageName()));
		   wxlogin = (ImageView)localView.findViewById(getContext().getResources()
				.getIdentifier("login_by_wx", "id",
						getContext().getPackageName()));
		this.setContentView(localView);

		
	
	}

	public void setQQloginListener(android.view.View.OnClickListener clickListener) {
		if (null != clickListener) {
			qqlogin.setOnClickListener(clickListener);
		}
	}

	public void setWXloginListener(android.view.View.OnClickListener clickListener) {
		if (null != clickListener) {
			wxlogin.setOnClickListener(clickListener);
		}
	}
	
	@Override
	public void show() {
		super.show();
		isNoticeLogin = false;
		isCancelLogin = false;

	}
	
	public void showAutoLogin(String loginType){
		isNoticeLogin = true;
		if(callback != null)
			callback.onAutoLogin();
	
	}
	
	public void setQQLoginDialogCallback(QQLoginDialogCallback callback) {
		this.callback = callback;
	}
	
	/**
	 * 主动通知自动登录完成
	 */
	public void onAutoLoginFinish(){
		isNoticeLogin = true;
		handler.sendEmptyMessage(0);
	}
	/**
	 * 主动通知自动登录取消
	 */
	public void onAutoLoginCancel(){
		isCancelLogin = true;
	}
	
	QQLoginDialogCallback callback;
	public interface QQLoginDialogCallback{

		void onAutoLogin();
	}

}
