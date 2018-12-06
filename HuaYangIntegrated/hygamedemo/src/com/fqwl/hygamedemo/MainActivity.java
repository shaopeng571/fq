package com.fqwl.hygamedemo;

import com.tomato.fqsdk.clinterface.HyInterface.OnInitFinishedListener;
import com.tomato.fqsdk.clinterface.HyInterface.OnLoginFinishedListener;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.models.HyInitInfo;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.models.HyRoleData;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private HySDK hysdk;
	private String user_Id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		hysdk = HySDK.getInstance();
		HyInitInfo initInfo = new HyInitInfo();
		initInfo.setDebug(true);
		initInfo.setGameId("1001");
		initInfo.setGameVersion("v1.0");
		HySDK.getInstance().HyInitSDK(MainActivity.this, initInfo,new OnInitFinishedListener() {
			
			@Override
			public void onInitFinish(int code, String desc) {
				Log.d("fq","code："+code+" desc:"+desc);
			}
		});
		initView();
	}

	private void initView() {
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

		HySDK.getInstance().HyShowPayView(MainActivity.this, hyPayInfo);
	}
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case 0:
			hysdk.HyShowLoginView(MainActivity.this, new OnLoginFinishedListener() {

				@Override
				public void onLoginFinished(int ret, HyLoginResult paramUser) {
					switch (ret) {
					case 0:// 登录成功
						String uid = paramUser.getUid();
						String token = paramUser.getTokenKey();
						Log.d("fq", "uid:"+uid+" token:"+token);
						Toast.makeText(MainActivity.this, "登录成功！", 0).show();
						user_Id=uid;
						if (TextUtils.isEmpty(uid)) {
							uid = hysdk.HyGetCurrentUserID();
							if (TextUtils.isEmpty(uid)) {
								// 重新显示登录页面
								hysdk.HyShowLoginView(MainActivity.this, null);
						}
						}
						break;
					case 1:// sdk登录页面退出 
						break;
					default:
						break;
					}
				}
			});
			break;
		case 1://支付
			hyShowPay();
			break;
		case 2://角色创建
			HyRoleData data = new HyRoleData();
			data.setRoleId("123");// 角色id
			data.setRoleName("roletest");// 角色名
			data.setRoleLevel("33");// 角色等级
			data.setServerId("333");// 所在服id
			data.setServerName("server1");
			data.setUserMoney("100");
			data.setVipLevel("2");
			data.setUser_id(user_Id);
			hysdk.HySubmitDataRoleCreate(data);
			break;
		case 3://角色登录
			HyRoleData data2 = new HyRoleData();
			data2.setRoleId("123");// 角色id
			data2.setRoleName("roletest");// 角色名
			data2.setRoleLevel("33");// 角色等级
			data2.setServerId("333");// 所在服id
			data2.setServerName("server1");
			data2.setUserMoney("100");
			data2.setVipLevel("2");
			data2.setUser_id(user_Id);
			hysdk.HySubmitRoleData(data2);
			break;
		case 4://角色升级
			HyRoleData data3 = new HyRoleData();
			data3.setRoleId("123");// 角色id
			data3.setRoleName("roletest");// 角色名
			data3.setServerId("333");// 所在服id
			data3.setServerName("server1");
			data3.setUserMoney("100");
			data3.setVipLevel("2");
			data3.setRoleLevel_begin("1");//升级前等级
			data3.setRoleLevel("2");//升级后等级
			data3.setUser_id(user_Id);
			hysdk.HySubmitDataRoleLevelUpdate(data3);
			break;
		case 5://角色退出
			HyRoleData data4 = new HyRoleData();
			data4.setRoleId("123");// 角色id
			data4.setRoleName("roletest");// 角色名
			data4.setRoleLevel("33");// 角色等级
			data4.setServerId("333");// 所在服id
			data4.setServerName("server1");
			data4.setUserMoney("100");
			data4.setVipLevel("2");
			data4.setUser_id(user_Id);
			hysdk.HySubmitDataLogout(data4);
			break;
		default:
			break;
		}
	}
}
