package com.hywl.huayang_channelsdk_huawei.present;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.fqwl.hycommonsdk.present.apiinteface.IRoleDataAnaly;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.fqwl.hycommonsdk.present.apiinteface.SdkApi;
import com.fqwl.hycommonsdk.util.ChannelConfigUtil;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.game.handler.LoginHandler;
import com.huawei.android.hms.agent.game.handler.SaveInfoHandler;
import com.huawei.android.hms.agent.pay.PaySignUtil;
import com.huawei.android.hms.agent.pay.handler.PayHandler;
import com.huawei.hms.support.api.entity.game.GamePlayerInfo;
import com.huawei.hms.support.api.entity.game.GameUserData;
import com.huawei.hms.support.api.entity.pay.HwPayConstant;
import com.huawei.hms.support.api.entity.pay.PayReq;
import com.huawei.hms.support.api.pay.PayResultInfo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

public class ChannelSDK_HuaWei implements SdkApi, ActivityCycle, IApplication ,IRoleDataAnaly{
	private Activity mActivity;
	protected CommonSdkInitInfo initInfo;
	protected CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	private String huawei_appid;
	private String huawei_cpid;
	private static final int INIT = 0x0;
	private static final int LOGIN = 0x1;
	private int try_init = 0;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INIT:
				init(mActivity, initInfo, mBack, implCallback);
				break;
			case LOGIN:
				login(mActivity, null);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void init(final Activity activity, CommonSdkInitInfo info, CommonSdkCallBack callBack,
			ImplCallback implCallback) {
		// TODO Auto-generated method stub
		HMSAgent.init(activity);
		this.mActivity = activity;
		this.mBack = callBack;
		this.implCallback = implCallback;

		try {
			huawei_appid = ChannelConfigUtil.getMetaMsg(activity, "HUAWEI_UID");
			huawei_cpid = ChannelConfigUtil.getMetaMsg(activity, "HUAWEI_CPID") + "";
			FLogger.d("huawei_appid:" + huawei_appid + " huawei_cpid:" + huawei_cpid);

			HMSAgent.connect(activity, new ConnectHandler() {

				@Override
				public void onConnect(int rst) {
					FLogger.d("rst:" + rst);
					handler.removeMessages(0);
					if (rst == 0) {
						mBack.initOnFinish("初始化成功", 0);
						HMSAgent.checkUpdate(activity, new CheckUpdateHandler() {

							@Override
							public void onResult(int rst) {
								FLogger.d("rst:" + rst);
							}
						});
					} else {

						if (try_init < 50) {
							FLogger.d("重试初始化");
							try_init++;
							handler.sendEmptyMessageDelayed(INIT, 1000);// 初始化失败 1秒后重试
						} else {
							mBack.initOnFinish("初始化失败", 2);
						}
					}
				}
			});
		} catch (Exception e) {
			mBack.initOnFinish("初始化失败", 2);
		}

	}
	private int try_login = 0;
	@Override
	public void login(Activity activity, CommonSdkLoginInfo commonSdkLoginInfo) {
		// TODO Auto-generated method stub
		HMSAgent.Game.login(new LoginHandler() {

			@Override
			public void onResult(int rst, GameUserData userData) {

				FLogger.d("rst:" + rst);
				if (rst == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS && userData != null) {

					FLogger.d("game login: onResult: rst=" + rst + "  user=" + userData.getDisplayName() + "|"
							+ userData.getPlayerId() + "|" + userData.getIsAuth() + "|" + userData.getPlayerLevel());
					if (userData.getIsAuth() == 1) {
						handler.removeMessages(1);
						JSONObject hashMap = new JSONObject();
						try {
							hashMap.put("appId", huawei_appid);
							hashMap.put("ts", userData.getTs());
							hashMap.put("playerId", userData.getPlayerId());
							hashMap.put("gameAuthSign", userData.getGameAuthSign());
							hashMap.put("platform_api_version", "3");// TODO
							hashMap.put("playerLevel", userData.getPlayerLevel() + "");
							hashMap.put("cpId", huawei_cpid);
							hashMap.put("method", "external.hms.gs.checkPlayerSign");
							implCallback.onLoginVerify(hashMap);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

						HMSAgent.Game.showFloatWindow(mActivity);
					}

				} else {
					if (try_login < 50) {
						FLogger.d("重试登录");
						try_login++;
						handler.sendEmptyMessageDelayed(LOGIN, 1000);// 登录失败 1秒后再试
					} else {
						implCallback.onLoginFail(2);
//						listener.onLoginFailed("rst is not suc or userData == null");
					}
				}
			}

			@Override
			public void onChange() {
				FLogger.d("onChange");
				mBack.logoutOnFinish("onChange", 0);
//				listener.onLogout(true);
			}
		}, 1);// 网游填1
	}

	@Override
	public void charge(Activity activity, CommonSdkChargeInfo ChargeInfo) {
		// TODO Auto-generated method stub
		createPayReq(ChargeInfo);
		
	}

	/**
	 * 创建普通支付请求对象 | Create an ordinary Payment request object
	 * 
	 * @param totalAmount 要支付的金额 | Amount to pay
	 * @return 普通支付请求对象 | Ordinary Payment Request Object
	 */
	private void createPayReq(CommonSdkChargeInfo params) {
		final PayReq payReq = new PayReq();

		DecimalFormat format = new DecimalFormat("0.00");
		String amount = format.format(params.getMoney());

		// 商品名称 | Product Name
		payReq.productName = params.getGoods_name();
		// 商品描述 | Product Description
		payReq.productDesc = params.getGoods_desc();
		// 商户ID，来源于开发者联盟，也叫“支付id” | Merchant ID, from the Developer Alliance, also known
		// as "Payment ID"
		payReq.merchantId = huawei_cpid;
		// 应用ID，来源于开发者联盟 | Application ID, from the Developer Alliance
		payReq.applicationID = huawei_appid;
		// 支付金额 | Amount paid
		payReq.amount = amount;
		// 支付订单号 | Payment order Number
		payReq.requestId = params.getOrder();
		// 国家码 | Country code
		payReq.country = "CN";
		// 币种 | Currency
		payReq.currency = "CNY";
		// 渠道号 | Channel number
		payReq.sdkChannel = 1;
		// 回调接口版本号 | Callback Interface Version number
		payReq.urlVer = "2";
		// 商户名称，必填，不参与签名。会显示在支付结果页面 | Merchant name, must be filled out, do not
		// participate in the signature. will appear on the Pay results page
		payReq.merchantName = "北京中视博源文化传媒有限公司";
		// 分类，必填，不参与签名。该字段会影响风控策略 | Categories, required, do not participate in the
		// signature. This field affects wind control policy
		// X4：主题,X5：应用商店, X6：游戏,X7：天际通,X8：云空间,X9：电子书,X10：华为学习,X11：音乐,X12 视频, | X4:
		// Theme, X5: App Store, X6: Games, X7: Sky Pass, X8: Cloud Space, X9: ebook,
		// X10: Huawei Learning, X11: Music, X12 video,
		// X31 话费充值,X32 机票/酒店,X33 电影票,X34 团购,X35 手机预购,X36 公共缴费,X39 流量充值 | X31, X32 air
		// tickets/hotels, X33 movie tickets, X34 Group purchase, X35 mobile phone
		// advance, X36 public fees, X39 flow Recharge
		payReq.serviceCatalog = "X6";
		// 商户保留信息，选填不参与签名，支付成功后会华为支付平台会原样 回调CP服务端 | The merchant retains the
		// information, chooses not to participate in the signature, the payment will be
		// successful, the Huawei payment platform will be back to the CP service
		payReq.extReserved = params.getOrder();
		// 对单机应用可以直接调用此方法对请求信息签名，非单机应用一定要在服务器端储存签名私钥，并在服务器端进行签名操作。| For stand-alone
		// applications, this method can be called directly to the request information
		// signature, not stand-alone application must store the signature private key
		// on the server side, and sign operation on the server side.
		// 在服务端进行签名的cp可以将getStringForSign返回的待签名字符串传给服务端进行签名 | The CP, signed on the
		// server side, can pass the pending signature string returned by
		// Getstringforsign to the service side for signature
//		payReq.url="url";
//		payReq.expireTime="expiretime";
//		payReq.sign=PaySignUtil.rsaSign(PaySignUtil.getStringForSign(payReq), prikey);
//		FLogger.d("官方签名"+payReq.sign);
		
		implCallback.getPaySign(getStringForSign(payReq),new CommonSDKHttpCallback() {
			
			@Override
			public void onResult(ResultInfo resultInfo, String msg) {
				// TODO Auto-generated method stub
				try {
					JSONObject signjs=new JSONObject(resultInfo.data);
					payReq.sign=signjs.optString("sign");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (payReq != null) {
					HMSAgent.Pay.pay(payReq, new PayHandler() {
						@Override
						public void onResult(int rst, PayResultInfo result) {
							FLogger.d("rst:" + rst + " result:" + result);

						}
					});
				}
			}
		});
		
//		HuaweiOrder go_order_obj = (HuaweiOrder) params.getGo_order_obj();
//		if (go_order_obj.getExt() == null) {
//			UIUtil.toastShortOnMain(mActivity, "构建订单失败,ext为空");
//			return null;
//		}	
//		payReq.sign = go_order_obj.getExt().getSign();
//		return payReq;
	}

	@Override
	public boolean showExitView(Activity activity) {
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub

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
	public void submitExtendData(Activity activity, CommonSdkExtendData params) {
		// TODO Auto-generated method stub
		sumbitHuaweiData(params);
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
		return "2.6.1.301";
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "huawei";
	}

	@Override
	public void onStart(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRestart(Activity activity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume(Activity activity) {
		// TODO Auto-generated method stub
		HMSAgent.Game.showFloatWindow(mActivity);
	}

	@Override
	public void onPause(Activity activity) {
		// TODO Auto-generated method stub
		HMSAgent.Game.hideFloatWindow(mActivity);
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
	public void initGamesApi(Application context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initPluginInAppcation(Application application, Context context) {
		// TODO Auto-generated method stub

	}

	@Override
	public void roleCreate(Activity activity, CommonSdkExtendData params) {
		// TODO Auto-generated method stub
		sumbitHuaweiData(params);
	}

	@Override
	public void roleLevelUpdate(Activity activity, CommonSdkExtendData data) {
		// TODO Auto-generated method stub
		
	}
	public void sumbitHuaweiData(CommonSdkExtendData params) {
		GamePlayerInfo gpi = new GamePlayerInfo();
		gpi.area = params.getServerName();
		gpi.rank = params.getRoleLevel() + "";
		gpi.role = params.getRoleName();
		gpi.sociaty = params.getParty_name();
		HMSAgent.Game.savePlayerInfo(gpi, new SaveInfoHandler() {
			@Override
			public void onResult(int retCode) {
				FLogger.d("retCode:" + retCode);
			}
		});
	}
	 /**
     * 计算支付请求的待签名字符串
     * 在服务端进行签名的cp，可以将此方法返回的字符串传给自己发服务端进行签名
     * @param request 支付请求
     */
    public static JSONObject getStringForSign(PayReq request) {

        Map<String, Object> params=new HashMap<String, Object>();
        // 必选参数
       
			params.put(HwPayConstant.KEY_MERCHANTID, request.getMerchantId());
			params.put(HwPayConstant.KEY_APPLICATIONID, request.getApplicationID());
	        params.put(HwPayConstant.KEY_PRODUCTNAME, request.getProductName());
	        params.put(HwPayConstant.KEY_PRODUCTDESC, request.getProductDesc());
	        params.put(HwPayConstant.KEY_REQUESTID, request.getRequestId());
	        params.put(HwPayConstant.KEY_AMOUNT, request.getAmount());

	        // 可选参数
	        params.put(HwPayConstant.KEY_PARTNER_IDS, request.getPartnerIDs());
	        params.put(HwPayConstant.KEY_CURRENCY, request.getCurrency());
	        params.put(HwPayConstant.KEY_COUNTRY, request.getCountry());
	        params.put(HwPayConstant.KEY_URL, request.getUrl());
	        params.put(HwPayConstant.KEY_URLVER, request.getUrlVer());
	        params.put(HwPayConstant.KEY_EXPIRETIME, request.getExpireTime());
	        params.put(HwPayConstant.KEY_SDKCHANNEL, request.getSdkChannel());

        
        
        return getNoSign(params, false);
    }

    /**
     * 根据参数map获取待签名字符串
     * @param params 待签名参数map
     * @param includeEmptyParam 是否包含值为空的参数：
     *                          与 HMS-SDK 支付能力交互的签名或验签，需要为false（不包含空参数）
     *                          由华为支付服务器回调给开发者的服务器的支付结果验签，需要为true（包含空参数）
     * @return 待签名字符串
     */
    private static JSONObject getNoSign(Map<String, Object> params, boolean includeEmptyParam) {
        //对参数按照key做升序排序，对map的所有value进行处理，转化成string类型
        //拼接成key=value&key=value&....格式的字符串
        StringBuilder content = new StringBuilder();

        // 按照key做排序
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String value = null;
        Object object = null;
        boolean isFirstParm = true;
        JSONObject jsonObject=new JSONObject();
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            object = params.get(key);

            if (object == null) {
                value = "";
            }else if (object instanceof String) {
                value = (String) object;
            } else {
                value = String.valueOf(object);
            }
            try {
				jsonObject.put(key, value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//            if (includeEmptyParam || !TextUtils.isEmpty(value)) {
//                content.append((isFirstParm ? "" : "&") + key + "=" + value);
//                isFirstParm = false;
//            } else {
//                continue;
//            }
        }

        //待签名的字符串
        return jsonObject;//content.toString();
    }
}
