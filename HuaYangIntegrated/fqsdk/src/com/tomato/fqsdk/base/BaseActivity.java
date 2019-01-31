package com.tomato.fqsdk.base;

import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.widget.CustomProgressDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
public abstract class BaseActivity extends Activity {
	protected CustomProgressDialog progressDialog;
	public static int setvisibility=0;
@Override 
protected void onCreate(Bundle savedInstanceState) {
	hideNavigationBar();
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
//	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
	progressDialog=CustomProgressDialog.createDialog(this);
}
@Override
public void onWindowFocusChanged(boolean hasFocus) {

	super.onWindowFocusChanged(hasFocus);
	if (hasFocus) {
		hideNavigationBar();
	}
}

//singleTask Ӻ ̨          õķ   
@Override
protected void onNewIntent(Intent intent) {
	// TODO Auto-generated method stub
	super.onNewIntent(intent);
	progressDialog=CustomProgressDialog.createDialog(HySDK.getAppContext());
}
abstract public void initView();



public void onLoginFinished(int ret,HyLoginResult paramUser) {
	if (ret!=1) {
	if (paramUser.getBehavior().equals(CLCommon.REGISTER)) {
		PostUserInfo.HJregister(BaseActivity.this, "0", paramUser.getUid());
	}
	PostUserInfo.HJlogin(BaseActivity.this, "0", paramUser.getUid());
	}else {
		paramUser.setBehavior("cancel");
	}
	CLControlCenter.getInstance().onLoginFinished(ret, paramUser);
	finish();
}
//api19    ȫ    ʾ
public void hideNavigationBar() {
	int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
			| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
			| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
			| View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar
if (setvisibility==0) {
	if (android.os.Build.VERSION.SDK_INT >= 19) {
		uiFlags |= 0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
								// navigation bars - compatibility: building
								// API level is lower thatn 19, use magic
								// number directly for higher API target
								// level
		try {
			getWindow().getDecorView().setSystemUiVisibility(uiFlags);
		} catch (Exception e) {
			
		}
}
	
		
	} 
//	else {
//		uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
//	}

}


}
