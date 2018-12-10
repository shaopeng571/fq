package com.huayang.common.platformsdk.present;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.apiinteface.HyGameCallBack;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.ApiCallback;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.nearme.game.sdk.common.model.ApiResult;
import com.nearme.game.sdk.common.model.biz.PayInfo;
import com.nearme.game.sdk.common.model.biz.ReportUserGameInfoParam;
import com.nearme.game.sdk.common.model.biz.ReqUserInfoParam;
import com.nearme.platform.opensdk.pay.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;


public class platformApi implements SdkApi {
	private Activity mActivity;
	private CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	// private static UserInfo userInfo;

	private String sdkuid;
	String token;
	
	private String oppoappid;
	private String oppoappsecret;
	@Override
	public void init(Activity context, CommonSdkInitInfo info, CommonSdkCallBack callBack, ImplCallback implCallback) {
		//oppo适配沉浸式状态栏
				Window window = context.getWindow();

				WindowManager.LayoutParams attributes = window.getAttributes();

				attributes.flags |= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;

				window.setAttributes(attributes);
				this.mActivity = context;
				this.mBack = callBack;
				this.implCallback = implCallback;
			

				oppoappsecret =ChannelConfigUtil.getMetaMsg(context, "oppoappsecret");

				FLogger.d( "oppoappsecret="+oppoappsecret);
				GameCenterSDK.init(oppoappsecret, context);

				callBack.initOnFinish("初始化成功", 0);

	}

	@Override
	public void login(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		this.mActivity = activity;

		GameCenterSDK.getInstance().doLogin(mActivity, new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {

				GameCenterSDK.getInstance().doGetTokenAndSsoid(new ApiCallback() {

					@Override
					public void onSuccess(String resultMsg) {
						try {
							JSONObject json = new JSONObject(resultMsg);
							token = json.getString("token");
							sdkuid = json.getString("ssoid");
							// FLogger.d("doGetUserInfo.onSuccess." +
							// "token = "+token+" ssoid = "+uid);

							GameCenterSDK.getInstance().doGetUserInfo(new ReqUserInfoParam(token, sdkuid),
									new ApiCallback() {

										@Override
										public void onSuccess(String resultMsg) {
											// {“userName”:”xxx”,”mobile”:”12345678912”,”email”:””,”ssoid”:”123456”}
											FLogger.d("doGetUserInfo.onSuccess.resultMsg = " + resultMsg);

											JSONObject object = new JSONObject();
											try {

												JSONObject data = new JSONObject(resultMsg);
												String userName = data.getString("userName");

												object.put("token", token);
												object.put("ssoid", sdkuid);
												//ResultNotify.ShowLoginSucess(mActivity, uid, userName, chanle, mBack);
												FLogger.d( "开始验证:"+token+" "+sdkuid);
												implCallback.onLoginVerify(object);
//												implCallback.onLoginSuccess(sdkuid, userName, object, null, null);
											} catch (Exception e) {
												// TODO: handle
												// exception
											}

										}

										@Override
										public void onFailure(String resultMsg, int resultCode) {
											FLogger.d("doGetUserInfo.onFailure");
										}
									});
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onFailure(String content, int resultCode) {
						FLogger.d("doGetTokenAndSsoid.onFailure");
					}
				});
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				// Toast.makeText(DemoActivity.this, resultMsg,
				// Toast.LENGTH_LONG).show();
				FLogger.d("doLogin.onFailure");
			}
		});
	}

	@Override
	public void charge(Activity activity, CommonSdkChargeInfo ChargeInfo) {
		this.mActivity = activity;

		PayInfo payInfo = new PayInfo(ChargeInfo.getOrder(),
				ChargeInfo.getOrder(),(int) ChargeInfo.getMoney()*100);
		payInfo.setProductDesc(ChargeInfo.getGoods_desc());
		payInfo.setProductName(ChargeInfo.getGoods_name());
		payInfo.setCallbackUrl("http://api-sdk.huayang.fun/v1/channel/pay_notify/game_code/cytl/channel_code/2001108");
		Context oppoContext=activity;
		FLogger.i( "开始充值..");
		GameCenterSDK.getInstance().doPay(oppoContext, payInfo, new ApiCallback() {

			@Override
			public void onSuccess(String resultMsg) {
				FLogger.d( "充值成功 :"+resultMsg);
				implCallback.onPayFinish(0);
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				FLogger.d( "充值失败 :"+resultMsg+" resultCode:"+resultCode);
				implCallback.onPayFinish(-2);
			}
		});

	}

	@Override
	public boolean showExitView(Activity context) {
		this.mActivity = context;
		GameCenterSDK.getInstance().onExit(context, new GameExitCallback() {
			@Override
			public void exitGame() {
				// 在此加入退出游戏的代码
				mBack.exitViewOnFinish("游戏退出", 0);
			}
		});
		return true;
	}

	@Override
	public boolean getAdult(Activity activity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setDebug(boolean b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reLogin(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		this.mActivity = activity;
		login(activity, commonSdkLoginInfo);
	}
	/**
	 * 重启游戏
	 * 
	 * @param content
	 */
	protected void restartApp(Activity content) {
		Intent i = content
				.getBaseContext()
				.getPackageManager()
				.getLaunchIntentForPackage(
						content.getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		content.startActivity(i);
	}
	@Override
	public boolean showPersonView(Activity activity) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void controlFlow(Activity context, boolean isShow) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void DoRelease(Activity activity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submitExtendData(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		submitExtendOppo(activity, data);
	}

	@Override
	public void getOderId(CommonSdkChargeInfo info, Activity context, CommonSDKHttpCallback httpCallback) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getUserId() {
		// TODO Auto-generated method stub
		return CommonBackLoginInfo.getInstance().userId;
	}

	@Override
	public String getVersionName() {
		// TODO Auto-generated method stub
		return "2.0.0";
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "oppo";
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		
	}
	public void submitExtendOppo(final Activity activity,
			final CommonSdkExtendData data) {
		GameCenterSDK.getInstance().doReportUserGameInfoData(
			    new ReportUserGameInfoParam(data.getRoleId(), data.getRoleName(),Integer.parseInt(data.getRoleLevel()), data.getServerId(), data.getServerName(), "chapter", null), 
			       new ApiCallback() {

						@Override
						public void onSuccess(String resultMsg) {
							FLogger.d("resultMsg "+resultMsg);
						}

						@Override
						public void onFailure(String resultMsg, int resultCode) {
							FLogger.e("resultCode="+resultCode+" resultMsg "+resultMsg);
						}
					});
		
		/*GameCenterSDK.getInstance().doReportUserGameInfoData(
				new ReportUserGameInfoParam(PhoneInfoUtil.getAppId(activity)
						+ "", data.getServceName(), data.getRoleName(),
						data.getRoleLevel()), new ApiCallback() {

					@Override
					public void onSuccess(String resultMsg) {
						FLogger.d("doReportUserGameInfoData.onSuccess.resultMsg");
					}

					@Override
					public void onFailure(String resultMsg, int resultCode) {
						FLogger.d("doReportUserGameInfoData.onFailure.resultMsg");
					}
				});*/
		
		
	}



	void showLoginFail() {
		implCallback.onLoginFail(CommonBackLoginInfo.login_platform_fail);
	}

	public static void getVipGrade(final HyGameCallBack callback){
		GameCenterSDK.getInstance().doGetVIPGrade(new ApiCallback() {
			@Override
			public void onSuccess(String vipGrade) {
				FLogger.d("oppo vipGrade=" + vipGrade);
			    if (callback != null) {
			    	callback.onSuccess();
				}
			} 
			@Override         
			public void onFailure(String resultMsg, int resultCode) {
				FLogger.d("oppo vip resultMsg=" + resultMsg);
				if (callback != null) {
			    	callback.onFailed();//(resultMsg, resultCode);
				}
			} 
		}); 
	}

	public int getUserAge() {
		GameCenterSDK.getInstance().doGetVerifiedInfo(new ApiCallback() {
			@Override
			public void onSuccess(String resultMsg) {
				try {
					// 解析年龄age
					int age = Integer.parseInt(resultMsg);
					if (age < 18) {
//						Toast.makeText(DemoActivity.this, "已实名但未成年，CP开始处理防沉迷",
//								Toast.LENGTH_SHORT).show();
					} else {
//						Toast.makeText(DemoActivity.this, "已实名且已成年，尽情玩游戏吧~",
//								Toast.LENGTH_SHORT).show();
					}
					mBack.realNameOnFinish(age);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(String resultMsg, int resultCode) {
				if (resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_RESUME_GAME) {
//					Toast.makeText(DemoActivity.this, resultMsg + "，还可以继续玩游戏",
//							Toast.LENGTH_SHORT).show();
				} else if (resultCode == ApiResult.RESULT_CODE_VERIFIED_FAILED_AND_STOP_GAME) {
//					Toast.makeText(DemoActivity.this,
//							resultMsg + ",CP自己处理退出游戏", Toast.LENGTH_SHORT)
//							.show();
				}
				mBack.realNameOnFinish(0);
			}
		});
		return 0;
	}
}
