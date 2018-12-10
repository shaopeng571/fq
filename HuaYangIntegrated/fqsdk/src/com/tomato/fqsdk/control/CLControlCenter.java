package com.tomato.fqsdk.control;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import com.tomato.fqsdk.HyAgreementActivity;
import com.tomato.fqsdk.HyLoginActivity;
import com.tomato.fqsdk.HyPayMainActivity;
import com.tomato.fqsdk.HyRegActivity;
import com.tomato.fqsdk.clinterface.HyInterface.OnInitFinishedListener;
import com.tomato.fqsdk.clinterface.HyInterface.OnLoginFinishedListener;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.models.BaseInfo;
import com.tomato.fqsdk.models.CLCommon;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.models.CLPayResult;
import com.tomato.fqsdk.models.ControlConfig;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.HJAccountDBHelper;
import com.tomato.fqsdk.utils.JsonParse;
import com.tomato.fqsdk.utils.Tools;

public class CLControlCenter {

	//     ģʽ
	private static CLControlCenter controlcenter;

	private CLControlCenter() {

	}

	//   ȡʵ  
	public static CLControlCenter getInstance() {
		synchronized (CLControlCenter.class) {
			if (controlcenter == null)
				controlcenter = new CLControlCenter();
		}
		return controlcenter;
	}

	private HyLoginResult rl; //   ǰ  ¼ û   Ϣ
	private BaseInfo baseInfo; //   ǰ      Ϣ

	private ControlConfig config; //   Ϸ      
	private String gameicon = "";//  Ϸͼ  
//	private String gameRegion = "1";//    
	private String gamename = "";//  Ϸ  
	private HJAccountDBHelper hjAccountDBHelper;
	public static String uid="";

	public boolean isLogin() {
		return rl != null;
	}
	//  ǰ ʺ 
	private String currentName;
	public void setCurrentName(String name){
		currentName=name;
	}
	public String getCurrentName(){
		return currentName;
	}

	// ο     
	public String getTempName(Context context) {
		return Tools.getSharedPreference(context, CLCommon.TEMPACCOUNT);
	}

	public void setTempName(Context context,String tempName) {
			Tools.saveSharedPreference(context, CLCommon.TEMPACCOUNT, tempName);
	}

	public boolean isTemp(Context context){
		return !Tools.getSharedPreference(context, CLCommon.TEMPACCOUNT).equals("");
	}

	public BaseInfo getBaseInfo() {
	return this.baseInfo;
}


	public ControlConfig getControlConfig() {
		return this.config;
	}

	public void setConfig(ControlConfig config) {
		this.config = config;
//		this.gameRegion = config.getGameRegion();
	}

	public String getGameicon() {
		return gameicon;
	}

	public void setGameicon(String gameicon) {
		this.gameicon = gameicon;
	}

	public String getGamename() {
		return gamename;
	}

	public void setGamename(String gamename) {
		this.gamename = gamename;
	}

	public void init(final Context context) {
		this.baseInfo = new BaseInfo(context);
		FindResHelper.init(context);
		hjAccountDBHelper =HJAccountDBHelper.getInstance(context);
        if (isTemp(context)) {
			setTempName(context,Tools.getSharedPreference(context, CLCommon.TEMPACCOUNT));
		}
	}
	public void initBaseInfo(Context context){
		this.baseInfo = new BaseInfo(context);
	}

	/**
	 *      û   Ϣ
	 */
	public void CleanUser(Context fromContext) {
		
		Tools.saveSharedPreference(fromContext, CLCommon.TEMPACCOUNT, "");
		setCurrentName("");
		hjAccountDBHelper.open();
		hjAccountDBHelper.deleteAllData();
		hjAccountDBHelper.close();
	}

	


	//   ע ᴰ  
	public void HJstartReg(Context paramContext) {
		paramContext
				.startActivity(new Intent(paramContext, HyRegActivity.class));
	}

	//           
	public void HJstartAgreement(Context paramContext) {
		paramContext.startActivity(new Intent(paramContext,
				HyAgreementActivity.class));
	}

	//  򿪵 ¼    
	public void HJstartLogin(Context fromContext, OnLoginFinishedListener iBack) {
		if (iBack != null) {
			onLoginFinishedListener = iBack;
		}
		if (isFirstGame(fromContext)) {
			gotoRegActivity((Activity)fromContext);
//			fromContext.startActivity(new Intent(fromContext, HyRegActivity.class));
		}else {
			fromContext.startActivity(new Intent(fromContext, HyLoginActivity.class));
		}
		
		
	}
	public static void onLoginFinished(int ret,HyLoginResult hjLoginResult){
		onLoginFinishedListener.onLoginFinished(ret,hjLoginResult);
	}
	/**
	 * ֧  ҳ  
	 * 
	 * @param paramContext
	 * @param hjPayRequest
	 * @param paramOnPayFinishedListener
	 */
	public void HJstartPay(Context paramContext, HyPayInfo hjPayRequest) {
//		if (paramOnPayFinishedListener != null) {
//			onPayFinishedListener = paramOnPayFinishedListener;
//		}
//		CLPayResult.clean();
		if (hjPayRequest == null) {
			Log.e(paramContext.getClass().getName(),
					"---HJPayRequest can't be empty---");
			return;
		}
		Intent intent = new Intent(paramContext, HyPayMainActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("payInfo", hjPayRequest);
		intent.putExtras(bundle);
		paramContext.startActivity(intent);
	}
	private static OnLoginFinishedListener onLoginFinishedListener = new OnLoginFinishedListener() {
		public void onLoginFinished(int resultCode,HyLoginResult paramUser) {
		}
	};

	/**
	 *  Ƿ  һ ν     Ϸ
	 * fsp  2018  9  12  
	 */
	private boolean isFirstGame(Context context) {
		
		if (Tools.getSharedPreference(context, CLCommon.ISFIRSTGAME).equals("")) {
			//  һ ν   Ϸ
			Tools.saveSharedPreference(context, CLCommon.ISFIRSTGAME, "1");
			return true;
		}
		return false;
	}
	
	/**
	 *     ˺ ע  
	 */
	private void gotoRegActivity(final Activity activity) {
		PostUserInfo.CLRandomRegister(activity,
				new HttpCallback() {
					@Override
					public void onStart() {
//						btnReg.setBackgroundResource(FindResHelper
//								.RDrawable("fq_okbtnshape_gray"));
						super.onStart();
					}
					@Override
					public void onError(String msg) {
						// TODO Auto-generated method stub
						super.onError(msg);
						Toast.makeText(HySDK.context, "     С   ˣ       ", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(String responseString) {
//						btnReg.setBackgroundResource(FindResHelper
//								.RDrawable("fq_okbtnshape"));
						FLogger.e(responseString);
						try {
							if (!TextUtils.isEmpty(responseString)) {
								
								JSONObject jsonObject;
								FLogger.d(responseString);
									jsonObject = new JSONObject(responseString);
									
									String ret =JsonParse.HJJsonGetRet(jsonObject);
									
									String msg = JsonParse.HJJsonGetMsg(jsonObject);
									String data=JsonParse.HJJsonGetData(jsonObject);
									if (ret.equals("1")) {
									JSONObject jsondata=new JSONObject(data);
									String password=jsondata.optString("password");
									String user_name=jsondata.optString("user_name");
									Intent intent = new Intent(
											activity,
											HyRegActivity.class);
									intent.putExtra("regorbind", "reg");
									intent.putExtra("account", "");
									Bundle bundle = new Bundle();
									bundle.putString("user_name", user_name);
									bundle.putString("password", password);
									bundle.putInt("code",
											HyRegActivity.FQ_LOGIN_TO_REG);
									intent.putExtras(bundle);
									activity.startActivity(intent);
								
								} else {
									HySDK.HyToast(msg);
								}
							}else {
								HySDK.HyToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});


	}
}
