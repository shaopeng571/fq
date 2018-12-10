package com.tomato.fqsdk;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.tomato.fqsdk.R;
import com.tomato.fqsdk.clinterface.HyInterface.OnInitFinishedListener;
import com.tomato.fqsdk.clinterface.HyInterface.OnLoginFinishedListener;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.CLData;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.models.HyRoleData;
import com.tomato.fqsdk.models.CLPayResult;
import com.tomato.fqsdk.models.HySDKResultCode;
import com.tomato.fqsdk.models.HyInitInfo;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HyMainActivity extends Activity {
//	public void getSignInfo() {
//		try {
//			PackageInfo packageInfo = getPackageManager().getPackageInfo(
//					getPackageName(), PackageManager.GET_SIGNATURES);
//			Signature[] signs = packageInfo.signatures;
//			Signature sign = signs[0];
//			Log.e("HJ", sign.toCharsString());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	public String uid;
	public String token;
	private HySDK hySDK;

//private static TimerTask mtimerTask = null;
//private static Timer mDataTimer = new Timer(true);
	private StringBuffer getBk2() {
		StringBuffer sb = new StringBuffer();
		sb.append(getGBS(6, 5));
		return sb;
	}

	private int getGBS(int x, int y) {
		for (int i = 1; i <= x * y; i++) {
			if (i % x == 0 && i % y == 0)
				return i;
		}

		return x * y;
	}

	private StringBuilder getKey1() {
		StringBuilder sb = new StringBuilder();
		String str = FindResHelper.RStringStr("register_name_digits");
		sb.append(str.charAt(24)).append(str.charAt(20));
		return sb;
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("HJ", "-----onCreate---------");
		setContentView(R.layout.hj_activity_main);// 60004512871l457wjzc
		hySDK = HySDK.getInstance();
		HyInitInfo initInfo = new HyInitInfo();
		initInfo.setDebug(true);
		initInfo.setGameId("1001");
		initInfo.setGameVersion("v1.0");
		HySDK.getInstance().HyInitSDK(HyMainActivity.this, initInfo,new OnInitFinishedListener() {
			
			@Override
			public void onInitFinish(int code, String desc) {
				FLogger.d("code："+code+" desc:"+desc);
			}
		});// rxsgvvvttsslxwpsbbb
		CLNaviteHelper clNaviteHelper = CLNaviteHelper.getInstance();
		Log.e("CL", getBk2().toString());
		Button bn2 = (Button) findViewById(R.id.dummy_button2);
		Button bn3 = (Button) findViewById(R.id.dummy_button3);
		Button bn5 = (Button) findViewById(R.id.dummy_button5);
		Button b1 = (Button) findViewById(R.id.button1);
		Button b3 = (Button) findViewById(R.id.button3);
		Button b4 = (Button) findViewById(R.id.button4);
		HySDK.getInstance().LoginToast = true;
		HySDK.getInstance().CLSetVisibility();
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				HyRoleData data = new HyRoleData();
				data.setRoleId("123");// 角色id
				data.setRoleName("roletest");// 角色名
				data.setRoleLevel("33");// 角色等级
				data.setServerId("333");// 所在服id
				data.setServerName("server1");
				data.setUserMoney("100");
				data.setVipLevel("2");
				hySDK.HySubmitRoleData(data);
			}
		});

		HySDK.getInstance().hyGetChannel(this);
		
		bn2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HySDK.getInstance().HyShowLoginView(HyMainActivity.this, new OnLoginFinishedListener() {

					@Override
					public void onLoginFinished(int resultCode, HyLoginResult paramUser) {
						Log.e("fq", "login:int=" + resultCode + "HJLoginResult:" + paramUser.toString() + "");
						if (resultCode == HySDKResultCode.SUCCESS) {
							uid = paramUser.getUid();
							token = paramUser.getTokenKey();
							HyRoleData data = new HyRoleData();
							data.setRoleId("123");// 角色id
							data.setRoleName("roletest");// 角色名
							data.setRoleLevel("33");// 角色等级
							data.setServerId("333");// 所在服id
							data.setServerName("server1");
							data.setUserMoney("100");
							data.setVipLevel("2");
							
							hySDK.HySubmitRoleData(data);
						}
						if (resultCode == HySDKResultCode.FAILED) {

						}

					}
				});

				// ΢ ŵ ¼
				// send oauth request
//			    final SendAuth.Req req = new SendAuth.Req();
//			    req.scope = "snsapi_userinfo";
//			    req.state = "wechat_sdk_demo_test";
//			    mWXApi.sendReq(req);
			}
		});
		// ֧       paramContext        subject      detail      totalFee  ܼ۸  quantity
		//     
		bn3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				hyShowPay();
			}

		});
		bn5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				HyRoleData data = new HyRoleData();
				data.setRoleId("123");// 角色id
				data.setRoleName("roletest");// 角色名
				data.setRoleLevel("33");// 角色等级
				data.setServerId("333");// 所在服id
				data.setServerName("server1");
				data.setUserMoney("100");
				data.setVipLevel("2");
				data.setUser_id("uid123");
				HashMap<String, Object> data_map=new HashMap<String, Object>();
				data_map.put("task_id", "21");
				data_map.put("task_status", "begin");
				data_map.put("task_type", "branch");
				hySDK.HySubmitDataOther(data, "task", data_map);
//				hySDK.HyShare(HyMainActivity.this);
				// HySDK.getInstance().CLCleanUser();
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("sid", "1");
//				map.put("role_id", "15641");
//				map.put("user_id", "156241");
//				map.put("level", "15");
//				HJData.configGameDataByBehavior("login", map);
			}
		});
	}

	private void hyShowPay() {
		HyPayInfo hyPayInfo = new HyPayInfo();
		hyPayInfo.setCp_order_id("cp_order_id");
		hyPayInfo.setRole_id("123");
		hyPayInfo.setRole_name("角色名");
		hyPayInfo.setRole_level("20");
		hyPayInfo.setVip_level("9");
		
		hyPayInfo.setServer_id("100");
		hyPayInfo.setServer_name("服务器名字");
		hyPayInfo.setMoney(0.01);
		
		hyPayInfo.setGoods_type("商品类型");
		hyPayInfo.setGoods_id("goodsid");
		hyPayInfo.setGoods_name("元宝");
		hyPayInfo.setGoods_count(1);
		hyPayInfo.setGoods_desc("元宝");
		
		hyPayInfo.setGame_coin("60");
		hyPayInfo.setExtra("extra");//游戏cp 私有字段，可传

		HySDK.getInstance().HyShowPayView(HyMainActivity.this, hyPayInfo);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		CLData.CLOnResume();
		System.out.println("-----onResume---------");
		Log.e("HJ", "-----onResume---------");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		Log.e("HJ", "-----onPause---------");
		// System.out.println("-----onPause---------");
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		CLData.CLOnStop();
		Log.e("HJ", "-----onStop---------");
		System.out.println("-----onStop---------");
	}

	@Override
	public void onBackPressed() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				new Thread(new Runnable() {
					public void run() {
						Log.e("HJ", "hahahah");
						CLData.configGameDataByBehavior2("login_out", new HashMap<String, Object>());

					}
				}).start();
				System.exit(0);
			}
		});

//		 System.exit(0);
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("HJ", "-----onDestroy---------");
		System.out.println("-----onDestroy---------");
	}
//	private static void openTimer()
//	  {stopTimer();
//	  if (mDataTimer == null) {
//			mDataTimer = new Timer(true);
//		}
//		if (mtimerTask == null) {
//			mtimerTask = new TimerTask() {
//				public void run() {
//					Map<String, Object> map = new HashMap<String, Object>();
//					map.put("item", "1");
//					map.put("residue", "1");
//					map.put("item_totalfee", "15641");
//					map.put("reason", "156241");
////					map.put("level", "15");
//					CLData.configGameDataByBehavior("virtual", map);
//					Log.e("HJ", "ѭ  ѭ  ");
//				}
//			};
//		}
//
//		if ((mDataTimer != null) && (mtimerTask != null)) {
//			mDataTimer.schedule(mtimerTask, 500, 1000);
//		}
//		
//	  }
//	  private static void stopTimer()
//	  {
//		  if (mDataTimer != null) {
//				 mDataTimer.cancel();
//				 mDataTimer = null;
//			    }
//			    if (mtimerTask != null) {
//			    	mtimerTask.cancel();
//			    	mtimerTask = null;
//			    }
//	  }
}
