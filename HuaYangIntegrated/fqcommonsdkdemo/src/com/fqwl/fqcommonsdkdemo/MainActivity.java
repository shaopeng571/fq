package com.fqwl.fqcommonsdkdemo;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.present.HySDKManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
 private String uid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initview();
		CommonSdkInitInfo initInfo=new CommonSdkInitInfo();
		initInfo.setGameVersion("v1.0");
		initInfo.setDebug(true);
		HySDKManager.getInstance().initCommonSdk(MainActivity.this, initInfo, sdkcallback);

	}
	
	private void initview() {
			LinearLayout layout = new LinearLayout(this);
			layout.setOrientation(LinearLayout.VERTICAL);
			this.setContentView(layout);

			Button button = new Button(this);
			button.setText("登陆");
			button.setId(0);
			button.setOnClickListener(this);
			layout.addView(button);

			Button button1 = new Button(this);
			button1.setText("定额充值");
			button1.setId(1);
			button1.setOnClickListener(this);
			layout.addView(button1);

			Button button2 = new Button(this);
			button2.setText("角色等创建");
			button2.setId(2);
			button2.setOnClickListener(this);
			layout.addView(button2);

			Button button3 = new Button(this);
			button3.setText("角色登录");
			button3.setId(3);
			button3.setOnClickListener(this);
			layout.addView(button3);

			Button button4 = new Button(this);
			button4.setText("角色等级变化");
			button4.setId(4);
			button4.setOnClickListener(this);
			layout.addView(button4);
			
			Button button5 = new Button(this);
			button5.setText("角色退出");
			button5.setId(5);
			button5.setOnClickListener(this);
			layout.addView(button5);


		
	}
private CommonSdkCallBack sdkcallback=new CommonSdkCallBack() {
		
		@Override
		public void realNameOnFinish(int arg0) {
			// TODO Auto-generated method stub
			
		}
		
		/**
		* 注销接口回调
		*/
		@Override
		public void logoutOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 注销成功

			}
		}
		
		@Override
		public void loginOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
//			Log.e("fq", "登录成功！！！"+arg0);
			switch (arg1) {
			case 0:// 登录成功
				try {
					JSONObject object = new JSONObject(arg0);
					String uid = object.getString("userId");
					Log.d("fq",object.toString());
					int chanlid = object.getInt("platformChanleId");
					Toast.makeText(MainActivity.this, "登录成功！", 0).show();
					
					
					if (TextUtils.isEmpty(uid)) {
						uid = HySDKManager.getInstance().getCurrentUserId(MainActivity.this);
						if (TextUtils.isEmpty(uid)) {
							// 重新显示登录页面
							HySDKManager.getInstance().showLoginView(MainActivity.this, null);
						}
					}
					MainActivity.this.uid=uid;
					// sendExtData();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			case 2:// sdk登录页面退出 （不是所有渠道都有该状态）
				break;
			default:
				break;
			}
		}
		
		@Override
		public void initOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			Log.d("fq","initOnFinish:" + arg0 + " code:" + arg1);
			if (arg1 == 0) {
				// 初始化成功 继续游戏
			} else if (arg1 == 1) {
				// 初始化成功 同时用户自动登录成功，不要再次调用登录接口
			} else {
				// 初始化失败 建议在有网络的情况下再次调用，失败还是失败，建议重启游戏或者退出游戏
			}
		}

		/**
		* 
		* 该接口已经sdk处理，游戏方不用处理
		*/
		@Override
		public void getAdultOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}


		/**
		* 退出页面回调
		* 
		*/
		@Override
		public void exitViewOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 用户推出游戏
				// 这里结束游戏
				MainActivity.this.finish();
			} else {
				// 用户继续游戏
			}
		}

		/**
		* 充值回调 不是所有平台都有改回调，充值是否成功，以服务器收到通知为准
		* 
		*/
		@Override
		public void chargeOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 充值流程完成

			} else {
				// 充值流程未完成
			}
		}

		/**
		* 浮动图标切换账号，修改密码回调 回调登陆页面，让用户重新登陆
		*/
		@Override
		public void ReloginOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			switch (arg1) {
			case 0:
			case 3:
				// 收到回调，立即退出游戏回到登陆页面，让用户重新调用sdk登陆页面
				Log.d("fq","收到回调，立即退出游戏回到登陆页面，让用户重新调用sdk登陆页面");
				break;
			case 1:
				// 用户已经处于注销状态,sdk登录界面已经打开
				break;
			case 4:
				// 用户已经处于登录状态,sdk登录界面关闭，用户信息返回(不要再次调用登录)
				break;
			default:
				break;
			}
		}

	
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case 0:
			HySDKManager.getInstance().showLoginView(MainActivity.this,null);
			break;
		case 1:
			CommonSdkChargeInfo hyPayInfo = new CommonSdkChargeInfo();
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
			HySDKManager.getInstance().showChargeView(MainActivity.this, hyPayInfo);
			break;
		case 2:
			CommonSdkExtendData rolecreate_data = new CommonSdkExtendData();
			rolecreate_data.setRoleId("123");// 角色id
			rolecreate_data.setRoleName("roletest");// 角色名
			rolecreate_data.setRoleLevel("33");// 角色等级
			rolecreate_data.setServerId("333");// 所在服id
			rolecreate_data.setServerName("server1");
			rolecreate_data.setUserMoney("100");
			rolecreate_data.setVipLevel("2");
			rolecreate_data.setRoleCTime(HySDKManager.getInstance().getTimeZ());
			rolecreate_data.setParty_name("帮派");
			rolecreate_data.setServerName("服务器名字");
			rolecreate_data.setUser_id(uid);
			HySDKManager.getInstance().sendExtendDataRoleCreate(MainActivity.this, rolecreate_data);
			break;
		case 3:
			CommonSdkExtendData data = new CommonSdkExtendData();
			data.setRoleId("123");// 角色id
			data.setRoleName("roletest");// 角色名
			data.setRoleLevel("33");// 角色等级
			data.setServerId("333");// 所在服id
			data.setServerName("server1");
			data.setUserMoney("100");
			data.setVipLevel("2");
			data.setRoleCTime(HySDKManager.getInstance().getTimeZ());
			data.setParty_name("帮派");
			data.setServerName("服务器名字");
			data.setUser_id(uid);
			HySDKManager.getInstance().sendExtendData(MainActivity.this, data);
			break;
		case 4://角色升级
			CommonSdkExtendData data3 = new CommonSdkExtendData();
			data3.setRoleId("123");// 角色id
			data3.setRoleName("roletest");// 角色名
			data3.setServerId("333");// 所在服id
			data3.setServerName("server1");
			data3.setUserMoney("100");
			data3.setVipLevel("2");
			data3.setRoleLevel_begin("1");//升级前等级
			data3.setRoleLevel("2");//升级后等级
			data3.setRoleCTime(HySDKManager.getInstance().getTimeZ());
			data3.setParty_name("帮派");
			data3.setServerName("服务器名字");
			data3.setUser_id(uid);
			HySDKManager.getInstance().sendExtendDataRoleLevelUpdate(MainActivity.this, data3);
			break;
		case 5://角色退出
			CommonSdkExtendData data4 = new CommonSdkExtendData();
			data4.setRoleId("123");// 角色id
			data4.setRoleName("roletest");// 角色名
			data4.setRoleLevel("33");// 角色等级
			data4.setServerId("333");// 所在服id
			data4.setServerName("server1");
			data4.setUserMoney("100");
			data4.setVipLevel("2");
			data4.setRoleCTime(HySDKManager.getInstance().getTimeZ());
			data4.setParty_name("帮派");
			data4.setServerName("服务器名字");
			data4.setUser_id(uid);
			HySDKManager.getInstance().sendExtendDataRoleLogout(MainActivity.this, data4);
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {// 处理返回键
			if (HySDKManager.getInstance().hasExitView(this)) {
				HySDKManager.getInstance().showExitView(this);
			} else {
				exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		// TODO Auto-generated method stub
		this.finish();
		System.exit(0);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		HySDKManager.getInstance().onNewIntent(intent);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(hasFocus);
		// HySDKManager.getInstance().onWindowFocusChanged();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		HySDKManager.getInstance().onStart(MainActivity.this);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		HySDKManager.getInstance().onRestart(MainActivity.this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		HySDKManager.getInstance().onResume(MainActivity.this);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		HySDKManager.getInstance().onPause(MainActivity.this);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		HySDKManager.getInstance().onStop(MainActivity.this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		HySDKManager.getInstance().onDestroy(MainActivity.this);
		System.exit(0);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		HySDKManager.getInstance().onConfigurationChanged(newConfig);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		HySDKManager.getInstance().onActivityResult(requestCode, resultCode,
				data);
	}

}
