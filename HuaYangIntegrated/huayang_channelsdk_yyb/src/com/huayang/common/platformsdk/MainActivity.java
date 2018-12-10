package com.huayang.common.platformsdk;
//package com.hywl.huayang_channelsdk_yyb;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v4.content.LocalBroadcastManager;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
//import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
//import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
//import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
//import com.fqwl.hycommonsdk.present.HySDKManager;
//import com.fqwl.hycommonsdk.util.ToastUtil;
//import com.fqwl.hycommonsdk.util.logutils.FLogger;
//import com.hywl.huayang_channelsdk_yyb.present.Util;
//import com.hywl.huayang_channelsdk_yyb.present.YSDKCallback;
//import com.tencent.tmgp.lqzgzs.R;
//
//import com.tencent.ysdk.api.YSDKApi;
//import com.tencent.ysdk.framework.common.BaseRet;
//import com.tencent.ysdk.framework.common.eFlag;
//import com.tencent.ysdk.framework.common.ePlatform;
//import com.tencent.ysdk.module.share.ShareApi;
//import com.tencent.ysdk.module.share.ShareCallBack;
//import com.tencent.ysdk.module.share.impl.IScreenImageCapturer;
//import com.tencent.ysdk.module.share.impl.ShareRet;
//import com.tencent.ysdk.module.user.UserLoginRet;
//
//import java.util.ArrayList;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * 说明: 每个模块相关的接口调用示例在 .module.submodule 中; 每个接口的详细使用说明在
// * jni/CommonFiles/YSDKApi.h 中; PlatformTest是 YSDK c++ 接口调用示例; 标签 TODO GAME
// * 标识之处是游戏需要关注并处理的!!
// */
//public class MainActivity extends Activity implements OnClickListener {
//
////    private ArrayList<BaseModule> nameList;
////    private BaseModule seletedModule;
//	private String uid;
////	public static ProgressDialog mAutoLoginWaitingDlg;
////	public static LinearLayout mModuleView;
////	public static LinearLayout mResultView;
////	public LocalBroadcastManager lbm;
////	public BroadcastReceiver mReceiver;
//	public static String title = "";
//	public static String callAPI = "";
//	public static String desripton = "";
//
//	public static final String LOG_TAG = "YSDK DEMO";
//	public static final String LOCAL_ACTION = "com.tencent.ysdkdemo";
//
//	protected static int platform = ePlatform.None.val();
//	private HySDKManager hySDKManager;
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
////        setContentView(R.layout.main_layout);
//		hySDKManager=HySDKManager.getInstance();
//		FLogger.init(true, "fq");
//// YSDKDemo 界面实现
//		initview();
//		CommonSdkInitInfo initInfo = new CommonSdkInitInfo();
//		initInfo.setGameVersion("v1.0");
//		initInfo.setDebug(true);
//		hySDKManager.initCommonSdk(MainActivity.this, initInfo, callback);
//
//	}
//
////    static{
////        System.loadLibrary("YSDKDemo"); // 游戏不需要这个
////    }
//
//	// TODO GAME 在onActivityResult中需要调用YSDKApi.onActivityResult
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		Log.d(LOG_TAG, "onActivityResult");
//		super.onActivityResult(requestCode, resultCode, data);
////		YSDKApi.onActivityResult(requestCode, resultCode, data);
//		hySDKManager.onActivityResult(requestCode, resultCode, data);
//	}
//
//	// TODO GAME 游戏需要集成此方法并调用YSDKApi.onRestart()
//	@Override
//	protected void onRestart() {
//		super.onRestart();
//		hySDKManager.onRestart(this);
//	}
//
//	// TODO GAME 游戏需要集成此方法并调用YSDKApi.onResume()
//	@Override
//	protected void onResume() {
//		super.onResume();
//		hySDKManager.onResume(this);
//	}
//
//	// TODO GAME 游戏需要集成此方法并调用YSDKApi.onPause()
//	@Override
//	protected void onPause() {
//		super.onPause();
//		hySDKManager.onPause(this);
//	}
//
//	// TODO GAME 游戏需要集成此方法并调用YSDKApi.onStop()
//	@Override
//	protected void onStop() {
//		super.onStop();
//		hySDKManager.onStop(this);
//	}
//
//	// TODO GAME 游戏需要集成此方法并调用YSDKApi.onDestory()
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		hySDKManager.onDestroy(this);
//		Log.d(LOG_TAG, "onDestroy");
//
////		if (null != lbm) {
////			lbm.unregisterReceiver(mReceiver);
////		}
////
////		if (null != mAutoLoginWaitingDlg) {
////			mAutoLoginWaitingDlg.cancel();
////		}
//	}
//
//	
//	
//
//	
//	
//
//	public void showToastTips(String tips) {
//		Toast.makeText(this, tips, Toast.LENGTH_LONG).show();
//	}
//
//	
//
//	private void initview() {
//		LinearLayout layout = new LinearLayout(this);
//		layout.setOrientation(LinearLayout.VERTICAL);
//		this.setContentView(layout);
//
//		Button button = new Button(this);
//		button.setText("登陆");
//		button.setId(0);
//		button.setOnClickListener(this);
//		layout.addView(button);
//
//		Button button1 = new Button(this);
//		button1.setText("定额充值");
//		button1.setId(1);
//		button1.setOnClickListener(this);
//		layout.addView(button1);
//
//		Button button2 = new Button(this);
//		button2.setText("角色等创建");
//		button2.setId(2);
//		button2.setOnClickListener(this);
//		layout.addView(button2);
//
//		Button button3 = new Button(this);
//		button3.setText("角色登录");
//		button3.setId(3);
//		button3.setOnClickListener(this);
//		layout.addView(button3);
//
//		Button button4 = new Button(this);
//		button4.setText("角色等级变化");
//		button4.setId(4);
//		button4.setOnClickListener(this);
//		layout.addView(button4);
//
//		Button button5 = new Button(this);
//		button5.setText("角色退出");
//		button5.setId(5);
//		button5.setOnClickListener(this);
//		layout.addView(button5);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case 0:
//			HySDKManager.getInstance().showLoginView(MainActivity.this,null);
////			YSDKApi.login(ePlatform.WX);
//			break;
//		case 1:
//			CommonSdkChargeInfo hyPayInfo = new CommonSdkChargeInfo();
//			hyPayInfo.setCp_order_id("cp_order_id");
//			hyPayInfo.setRole_id("123");
//			hyPayInfo.setRole_name("角色名");
//			hyPayInfo.setRole_level("20");
//			hyPayInfo.setVip_level("9");
//
//			hyPayInfo.setServer_id("100");
//			hyPayInfo.setServer_name("服务器名字");
//			hyPayInfo.setMoney(0.01);
//
//			hyPayInfo.setGoods_type("商品类型");
//			hyPayInfo.setGoods_id("goodsid");
//			hyPayInfo.setGoods_name("元宝");
//			hyPayInfo.setGoods_count(1);
//			hyPayInfo.setGoods_desc("元宝");
//
//			hyPayInfo.setGame_coin("60");
//			hyPayInfo.setExtra("extra");// 游戏cp 私有字段，可传
//			HySDKManager.getInstance().showChargeView(MainActivity.this, hyPayInfo);
//			break;
//		case 2:
//			CommonSdkExtendData rolecreate_data = new CommonSdkExtendData();
//			rolecreate_data.setRoleId("123");// 角色id
//			rolecreate_data.setRoleName("roletest");// 角色名
//			rolecreate_data.setRoleLevel("33");// 角色等级
//			rolecreate_data.setServerId("333");// 所在服id
//			rolecreate_data.setServerName("server1");
//			rolecreate_data.setUserMoney("100");
//			rolecreate_data.setVipLevel("2");
//			rolecreate_data.setRoleCTime(HySDKManager.getInstance().getTimeZ());
//			rolecreate_data.setParty_name("帮派");
//			rolecreate_data.setServerName("服务器名字");
//			rolecreate_data.setUser_id(uid);
//			HySDKManager.getInstance().sendExtendDataRoleCreate(MainActivity.this, rolecreate_data);
//			break;
//		case 3:
//			CommonSdkExtendData data = new CommonSdkExtendData();
//			data.setRoleId("123");// 角色id
//			data.setRoleName("roletest");// 角色名
//			data.setRoleLevel("33");// 角色等级
//			data.setServerId("333");// 所在服id
//			data.setServerName("server1");
//			data.setUserMoney("100");
//			data.setVipLevel("2");
//			data.setRoleCTime(HySDKManager.getInstance().getTimeZ());
//			data.setParty_name("帮派");
//			data.setServerName("服务器名字");
//			data.setUser_id(uid);
//			HySDKManager.getInstance().sendExtendData(MainActivity.this, data);
//			break;
//		case 4:// 角色升级
//			CommonSdkExtendData data3 = new CommonSdkExtendData();
//			data3.setRoleId("123");// 角色id
//			data3.setRoleName("roletest");// 角色名
//			data3.setServerId("333");// 所在服id
//			data3.setServerName("server1");
//			data3.setUserMoney("100");
//			data3.setVipLevel("2");
//			data3.setRoleLevel_begin("1");// 升级前等级
//			data3.setRoleLevel("2");// 升级后等级
//			data3.setRoleCTime(HySDKManager.getInstance().getTimeZ());
//			data3.setParty_name("帮派");
//			data3.setServerName("服务器名字");
//			data3.setUser_id(uid);
//			HySDKManager.getInstance().sendExtendDataRoleLevelUpdate(MainActivity.this, data3);
//			break;
//		case 5:// 角色退出
//			CommonSdkExtendData data4 = new CommonSdkExtendData();
//			data4.setRoleId("123");// 角色id
//			data4.setRoleName("roletest");// 角色名
//			data4.setRoleLevel("33");// 角色等级
//			data4.setServerId("333");// 所在服id
//			data4.setServerName("server1");
//			data4.setUserMoney("100");
//			data4.setVipLevel("2");
//			data4.setRoleCTime(HySDKManager.getInstance().getTimeZ());
//			data4.setParty_name("帮派");
//			data4.setServerName("服务器名字");
//			data4.setUser_id(uid);
//			HySDKManager.getInstance().sendExtendDataRoleLogout(MainActivity.this, data4);
//			break;
//
//		default:
//			break;
//		}
//	}
//
//private CommonSdkCallBack callback=new CommonSdkCallBack() {
//		
//		@Override
//		public void realNameOnFinish(int arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void logoutOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void loginOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
////			Log.e("fq", "登录成功！！！"+arg0);
//			switch (arg1) {
//			case 0:// 登录成功
//				try {
//					JSONObject object = new JSONObject(arg0);
//					String uid = object.getString("userId");
//					Log.d("fq", object.toString());
//					int chanlid = object.getInt("platformChanleId");
//					Toast.makeText(MainActivity.this, "登录成功！", 0).show();
//					
//					
//					if (TextUtils.isEmpty(uid)) {
//						uid = hySDKManager.getCurrentUserId();
//						if (TextUtils.isEmpty(uid)) {
//							// 重新显示登录页面
//							hySDKManager.showLoginView(MainActivity.this, null);
//						}
//					}
//					MainActivity.this.uid=uid;
//					// sendExtData();
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//			case 2:// sdk登录页面退出 （不是所有渠道都有该状态）
//				break;
//			default:
//				break;
//			}
//		}
//		
//		@Override
//		public void initOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			Log.e("fq", arg0);
//		}
//		
//		@Override
//		public void getAdultOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void exitViewOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void chargeOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			if (arg1 == 0) {// 充值流程完成
//				Log.d("fq", "充值流程完成");
//			} else {
//				// 充值流程未完成
//				Log.d("fq", "充值流程未完成");
//			}
//		}
//		
//		@Override
//		public void ReloginOnFinish(String arg0, int arg1) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
//}
