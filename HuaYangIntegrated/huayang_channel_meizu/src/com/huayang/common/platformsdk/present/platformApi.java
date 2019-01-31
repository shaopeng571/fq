package com.huayang.common.platformsdk.present;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.bean.ResultInfo;
import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.ActivityCycle;
import com.fqwl.hycommonsdk.present.apiinteface.IApplication;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.meizu.gamesdk.model.callback.MzExitListener;
import com.meizu.gamesdk.model.callback.MzLoginListener;
import com.meizu.gamesdk.model.callback.MzPayListener;
import com.meizu.gamesdk.model.model.LoginResultCode;
import com.meizu.gamesdk.model.model.MzAccountInfo;
import com.meizu.gamesdk.model.model.MzBuyInfo;
import com.meizu.gamesdk.model.model.PayResultCode;
import com.meizu.gamesdk.online.core.MzGameBarPlatform;
import com.meizu.gamesdk.online.core.MzGameCenterPlatform;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings.Global;
import android.text.TextUtils;
import android.text.format.Time;



/**
 * 魅族 网游实现类
 * 
 * @author Administrator
 * 
 */
public class platformApi implements SdkApi, IApplication,ActivityCycle {
	private Activity mActivity;
	private CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	private String sdkuid;
	private String mzappid;
	private String mzappkey;
	private String session;

	// 充值信息
	/**
	 * 根据要求对订单信息的签名后的串
	 */
	private String sign = " ";// 签名算法
	private String signType = "MD5";
	private int buyCount = 1;// 购买的产品总数
	// 游戏自定义信息
	private String cpUserInfo = "ok";
	private String total_price = " "; // 总价，需要传递金额字符。该值最多只能精确到 0.01
	// String total_price = 0.1+"";
	// 购买的产品 id
	private String productId = " ";
	// 产品名称，将会展示给用户看
	private String productSubject = " ";
	private String productBody = "xxx";
	private String productUnit = "xxx";
	// String perPrice = 0.1+"";
	private String perPrice = " ";// 产品单价，
	private long createTime = 1385452122;
	private MzBuyInfo buyInfo;
	// gamebar操作实例,不需要悬浮窗的可以不用
	MzGameBarPlatform mzGameBarPlatform;
	private boolean islogout;
	private boolean isChangeUserCallback;

	private int payType;// 购买类型，暂时支持固定金额(值为 0)、不 定金额充值(值为 1)

	@Override
	public void init(Activity context, CommonSdkInitInfo info,
			final CommonSdkCallBack callBack, ImplCallback implCallback) {
		this.mActivity = context;
		this.mBack = callBack;
		this.implCallback = implCallback;
		
		
		
		

		mzappid =ChannelConfigUtil.getMetaMsg(context, "mzappid");
		mzappkey =ChannelConfigUtil.getMetaMsg(context, "mzappkey");
		if (TextUtils.isEmpty(mzappid)
				|| TextUtils.isEmpty(mzappkey)) {
			mBack.initOnFinish("初始化失败，参数错误", -1);
			return;
		}
		FLogger.d("登录 mzappid--" + mzappid + "  mzappkey--" + mzappkey);
	
		// 　游戏登录后不能显示悬浮窗问题应检查下系统是否允许魅族游戏框架使用悬浮窗权限（在MIUI中可能会遇到）,具体可查看接入文档
		mzGameBarPlatform = new MzGameBarPlatform(context,
				MzGameBarPlatform.GRAVITY_RIGHT_BOTTOM);
		mzGameBarPlatform.onActivityCreate();
		mBack.initOnFinish("初始化成功", 0);
	}


	@Override
	public void login(final Activity context,
			CommonSdkLoginInfo commonSdkLoginInfo) {
		this.mActivity = context;
		FLogger.d("login");
		MzGameCenterPlatform.login(context, new MzLoginListener() {

			@Override
			public void onLoginResult(int code, MzAccountInfo accountInfo,
					String errorMsg) {
				// TODO Auto-generated method stub
				FLogger.d("onLoginResult code=" + code);
				switch (code) {
				case LoginResultCode.LOGIN_SUCCESS:
					// TODO 登录成功，拿到uid 和 session到自己的服务器去校验session合法性
					
					sdkuid = accountInfo.getUid();
					session = accountInfo.getSession();
					
					FLogger.d( "登录成功！\r\n 用户名：" + accountInfo.getName()
					+ "\r\n Uid：" + sdkuid
					+ "\r\n session：" + session);
					JSONObject ob=new JSONObject();
					try {
						//登陆校验
						ob.put("uid",sdkuid );
						ob.put("session_id",session );
						//ResultNotify.ShowLoginSucess(mActivity, uid, "",chanle, mBack);
						implCallback.onLoginVerify(ob);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case LoginResultCode.LOGIN_ERROR_CANCEL:
					// TODO 用户取消登陆操作
					showLoginFail();
					break;
				case LoginResultCode.LOGIN_LOGOUT:
					FLogger.d("islogout=" + islogout);
					if (islogout) {
						islogout = false;
						return;
					}
					mBack.ReloginOnFinish("切换成功", 0);
					sdkuid =null;
					if (mzGameBarPlatform != null) {
						mzGameBarPlatform.hideGameBar();
					}
					FLogger.d("切换回调成功");
					isChangeUserCallback = true;
					break;
				default:
					// TODO 登陆失败，包含错误码和错误消息。
					// TODO 注意，错误消息需要由游戏展示给用户，错误码可以打印，供调试使用
					showLoginFail();
					FLogger.d("登录失败 : " + errorMsg + " , code = " + code);
					FLogger.e( "登录失败 : " + errorMsg + " , code = " + code);
					break;

				}
			}

		});

	}
	
	@Override
	public void reLogin(final Activity activity, final CommonSdkLoginInfo info) {
		this.mActivity = activity;
		FLogger.d("relogin");
		//如果是浮标切换账号后
		if (isChangeUserCallback) {
			isChangeUserCallback = false;
		}else{
			islogout = true;
			//浮标切换账号后，调退出是没有回调的
//			MzGameCenterPlatform.logout(activity);
			MzGameCenterPlatform.logout(activity,new MzLoginListener() {
				
				@Override
				public void onLoginResult(int code, MzAccountInfo accountInfo, String msg) {
					//TODO 登录
					// MzGameCenterPlatform.login(GameMainActivity.this, GameMainActivity.this);
					login(activity, info);
				}
			});
		}
		//login(activity, info);
	}


	@Override
	public void charge(final Activity context,
			final CommonSdkChargeInfo ChargeInfo) {
		this.mActivity = context;
		showChargeView(context, ChargeInfo);
	}

	protected void showChargeView(final Activity context,final
			CommonSdkChargeInfo ChargeInfo) {
		

		
		
		
		JSONObject map = new JSONObject();
		//订单生产时间
		final String TimeStr=System.currentTimeMillis()+"";
		try {
			map.put("app_id",mzappid);
			map.put("uid", sdkuid);
			map.put("product_id",  ChargeInfo.getGoods_id());
			map.put("product_subject",  ChargeInfo.getGoods_name());
			map.put("product_body",  ChargeInfo.getGoods_desc());
			map.put("product_unit", "");
			map.put("buy_amount", "1");
			map.put("product_per_price", "1");
			map.put("total_price", (int)ChargeInfo.getMoney());
			map.put("pay_type", "0");
			map.put("user_info", "");
			map.put("create_time",TimeStr );
			map.put("cp_order_id", ChargeInfo.getOrder());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		implCallback.getPaySign(map, new CommonSDKHttpCallback() {
			
			@Override
			public void onResult(ResultInfo resultInfo, String arg1) {
				// TODO Auto-generated method stub
				JSONObject signjs;
				String sign="";
				try {
					signjs = new JSONObject(resultInfo.data);
					 sign =  signjs.optString("sign"); 
					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String orderId = ChargeInfo.getOrder();
				
				int buyCount =1;
				String cpUserInfo = ""; 
				int amount =(int) (ChargeInfo.getMoney());
				String productId = ChargeInfo.getGoods_id(); 
				String productSubject = ChargeInfo.getGoods_name(); 
				String productBody = ChargeInfo.getGoods_desc(); 
				String productUnit = ""; 
				String appid = mzappid;
				String uid =sdkuid;
				String perPrice ="1";
				String signType = "md5"; 
				long createTime = Long.parseLong(TimeStr);
				int payType = 0;
				Bundle buyBundle = new MzBuyInfo()
						.setBuyCount(buyCount)
						.setCpUserInfo(cpUserInfo) 
						.setOrderAmount(amount+"")
						.setOrderId(orderId)
						.setPerPrice(perPrice)
						.setProductBody(productBody)
						.setProductId(productId)
						.setProductSubject(productSubject) 
						.setProductUnit(productUnit)
						.setSign(sign)
						.setSignType(signType)
						.setCreateTime(createTime)
						.setAppid(appid)
						.setUserUid(uid)
						.setPayType(payType)
						.toBundle();
				MzGameCenterPlatform.payOnline(context, buyBundle,
						new MzPayListener() {
							@Override
							public void onPayResult(int code,
									Bundle info, String errorMsg) {
								// TODO 支付结果回调，该回调跑在应用主线程
								switch (code) {
								case PayResultCode.PAY_SUCCESS:
									// TODO 如果成功，接下去需要到自己的服务器查询订单结果
									MzBuyInfo payInfo = MzBuyInfo.fromBundle(info);
									FLogger.d("支付成功 : " + payInfo.toString());
									implCallback.onPayFinish(0);
									break;
								case PayResultCode.PAY_ERROR_CANCEL:
									// TODO 用户取消支付操作
									implCallback.onPayFinish(-2);
									break;
								default:
									// TODO 支付失败，包含错误码和错误消息。
									// TODO 注意，错误消息需要由游戏展示给用户，错误码可以打印，供调试使用
									implCallback.onPayFinish(-2);
									FLogger.d("支付失败 : " + errorMsg
											+ " , code = " + code);
									break;
								}
							}

						});
			}
		});
		
		
		
		
		
//		final String orderId = ChargeInfo.getOrderId();
//
//		total_price = ChargeInfo.getAmount() / 100 + "";
//		productId = ChargeInfo.getProductId();
//		perPrice = ChargeInfo.getAmount() / 100 + "";
//
//		final JSONObject json = new JSONObject();
//
//		if (ChargeInfo.getAmount() >= 1) {
//			payType = 0;
//			if(!TextUtils.isEmpty(ChargeInfo.getDes())){
//				productSubject = "购买" +ChargeInfo.getDes();
//			}else {
//				productSubject = "购买" + ChargeInfo.getAmount() / 100
//						* ChargeInfo.getRate() + ChargeInfo.getProductName();	
//			}
//			
//			// productSubject="购买"+ 0.1+ChargeInfo.getProductName();
//			try {
//				json.put("total_price", total_price);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} else {
//			payType = 1;
//			productSubject = "充值" + PhoneInfoUtil.getGameName(mActivity);
//		}
//
//		try {
//			json.put("app_id", appid);
//			json.put("buy_amount", buyCount + "");// 购买数量
//			json.put("cp_order_id", orderId);
//			json.put("create_time", createTime + "");// 360
//			json.put("pay_type", payType + "");
//			json.put("product_body", productBody);
//			json.put("product_id", productId);
//			json.put("product_subject", productSubject);
//			json.put("product_per_price", perPrice);
//			json.put("product_unit", productUnit);// 产品单价
//			json.put("uid", sdkuid);// 1 开 0 关
//			json.put("user_info", cpUserInfo);
//
//		} catch (JSONException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//
//		new Thread(new Runnable() {
//			public void run() {
//				Looper.prepare();
//				try {
//					JSONObject jsonObject = null;
//					jsonObject = new JSONObject(ApiClient
//							.getInstance(mActivity).chargeMZ(json.toString()));
//					if (jsonObject.getInt("code") == 200) {
//						JSONObject jsonvale = new JSONObject(jsonObject.getString("value"));
//						sign = jsonvale.getString("sign");
//						signType = jsonvale.getString("sign_type");
//						Logger.d("sign--" + sign);
//					}
//
//					buyInfo = new MzBuyInfo().setBuyCount(buyCount)
//							.setCpUserInfo(cpUserInfo)
//							.setOrderAmount(total_price).setOrderId(orderId)
//							.setPerPrice(perPrice).setProductBody(productBody)
//							.setProductId(productId)
//							.setProductSubject(productSubject)
//							.setProductUnit(productUnit).setSign(sign)
//							.setSignType(signType).setCreateTime(createTime)
//							.setAppid(appid).setUserUid(sdkuid)
//							.setPayType(payType);
//					Logger.d("----buyInfo" + buyInfo.toString());
//
//					MzGameCenterPlatform.payOnline(mActivity, buyInfo.toBundle(),
//							new MzPayListener() {
//								@Override
//								public void onPayResult(int code,
//										Bundle info, String errorMsg) {
//									// TODO 支付结果回调，该回调跑在应用主线程
//									switch (code) {
//									case PayResultCode.PAY_SUCCESS:
//										// TODO 如果成功，接下去需要到自己的服务器查询订单结果
//										MzBuyInfo payInfo = MzBuyInfo.fromBundle(info);
//										Logger.d("支付成功 : " + payInfo.toString());
//										implCallback.onPayFinish(0);
//										break;
//									case PayResultCode.PAY_ERROR_CANCEL:
//										// TODO 用户取消支付操作
//										implCallback.onPayFinish(-2);
//										break;
//									default:
//										// TODO 支付失败，包含错误码和错误消息。
//										// TODO 注意，错误消息需要由游戏展示给用户，错误码可以打印，供调试使用
//										implCallback.onPayFinish(-2);
//										Logger.d("支付失败 : " + errorMsg
//												+ " , code = " + code);
//										break;
//									}
//								}
//
//							});
//
//				} catch (JSONException e) {
//
//					e.printStackTrace();
//				}
//				Looper.loop();
//			}
//		}).start();

	}



	@Override
	public boolean showExitView(Activity context) {
		this.mActivity = context;
		//退出游戏接口
 	    MzGameCenterPlatform.exitSDK(context, new MzExitListener() {
            public void callback(int code, String msg) {
                if (code == MzExitListener.CODE_SDK_LOGOUT) {
             	   //TODO 在这里处理退出逻辑
                    //finish();
                    //System.exit(0);               	
                	mBack.exitViewOnFinish("游戏退出", 0);
                } else if (code == MzExitListener.CODE_SDK_CONTINUE) {
             	   //TODO 继续游戏
                    //Toast.makeText(GameMainActivity.this, "继续游戏", Toast.LENGTH_SHORT).show();
                	mBack.exitViewOnFinish("继续游戏", -1);
                }

            }
        });
		return true;
	}

	@Override
	public boolean getAdult(Activity context) {
		this.mActivity = context;
		return false;
	}

	@Override
	public void setDebug(boolean b) {
		
	}

	

	@Override
	public boolean showPersonView(Activity activity) {
		this.mActivity = activity;
		return false;
	}

	@Override
	public void controlFlow(Activity context, boolean isShow) {
		 if (isShow) {
			 //调一下onActivityResume
		     if(null != mzGameBarPlatform){
		    	 mzGameBarPlatform.onActivityResume();
		    	 if(sdkuid != null)
		    		 mzGameBarPlatform.showGameBar();
		     }
		 } else {
			//调一下onActivityPause
			 if(null != mzGameBarPlatform){
				 mzGameBarPlatform.onActivityPause();
				 if(sdkuid != null)
				 mzGameBarPlatform.hideGameBar();
		     }
		 }
	}

	@Override
	public void DoRelease(Activity activity) {
		islogout = true;
		MzGameCenterPlatform.logout(activity, new MzLoginListener() {
			
			@Override
			public void onLoginResult(int arg0, MzAccountInfo arg1, String arg2) {
				// TODO Auto-generated method stub
				
			}
		});
		 //调一下onActivityDestroy
		//如果游戏用Sysetm.exit(0)或者killProcess方式退出，需在退出前主动调用mzGameBarPlatform.onActivityPause()和mzGameBarPlatform.onActivityDestroy();
		//否则可能会出现游戏退出后悬浮窗还在桌面
		if(null != mzGameBarPlatform){
			mzGameBarPlatform.onActivityDestroy();
		}

	}

	public void submitExtendData(final Activity activity, final CommonSdkExtendData data) {

	}

	void showLoginFail() {
		implCallback.onLoginFail(CommonBackLoginInfo.login_platform_fail);
	}



	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return CommonBackLoginInfo.getInstance().userId;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return "4.8.6";
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return "mz";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void initGamesApi(Application context) {
		// TODO Auto-generated method stub
		String mzappid =ChannelConfigUtil.getMetaMsg(context, "mzappid");
		String mzappkey =ChannelConfigUtil.getMetaMsg(context, "mzappkey");
		
		MzGameCenterPlatform.init(context, mzappid, mzappkey);
	}

	@Override
	public void initPluginInAppcation(Application application, Context context) {
		
	}

	@Override
	public void onStart(Activity activity) {
		
	}

	@Override
	public void onRestart(Activity activity) {
		
	}

	@Override
	public void onResume(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStop(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNewIntent(Activity activity, Intent intent) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getOderId(CommonSdkChargeInfo arg0, Activity arg1, CommonSDKHttpCallback arg2) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}


}