package com.fqwl.hy_hygamechannelsdk.present;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonBackLoginInfo;
import com.fqwl.hycommonsdk.model.CommonSDKHttpCallback;
import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkChargeInfo;
import com.fqwl.hycommonsdk.model.CommonSdkExtendData;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.model.CommonSdkLoginInfo;
import com.fqwl.hycommonsdk.present.HySDKManager;
import com.fqwl.hycommonsdk.present.apiinteface.IRoleDataAnaly;
import com.fqwl.hycommonsdk.present.apiinteface.ImplCallback;
import com.tomato.fqsdk.clinterface.HyInterface.OnInitFinishedListener;
import com.tomato.fqsdk.clinterface.HyInterface.OnLoginFinishedListener;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.models.HyInitInfo;
import com.tomato.fqsdk.models.HyLoginResult;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.models.HyRoleData;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class SdkApi implements com.fqwl.hycommonsdk.present.apiinteface.SdkApi,IRoleDataAnaly{
	protected Activity mActivity;
	protected CommonSdkCallBack mBack;
	protected ImplCallback implCallback;
	public String sdkUid = "";

	@Override
	public void DoRelease(Activity arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void charge(Activity activity, CommonSdkChargeInfo commonSdkChargeInfo) {
		// TODO Auto-generated method stub
		HyPayInfo hyPayInfo = new HyPayInfo();
		hyPayInfo.setCp_order_id(commonSdkChargeInfo.getCp_order_id());
		hyPayInfo.setRole_id(commonSdkChargeInfo.getRole_id());
		hyPayInfo.setRole_name(commonSdkChargeInfo.getRole_name());
		hyPayInfo.setRole_level(commonSdkChargeInfo.getRole_level());
		hyPayInfo.setVip_level(commonSdkChargeInfo.getVip_level());
		
		hyPayInfo.setServer_id(commonSdkChargeInfo.getServer_id());
		hyPayInfo.setServer_name(commonSdkChargeInfo.getServer_name());
		hyPayInfo.setMoney(commonSdkChargeInfo.getMoney());
		
		hyPayInfo.setGoods_type(commonSdkChargeInfo.getGoods_type());
		hyPayInfo.setGoods_id(commonSdkChargeInfo.getGoods_id());
		hyPayInfo.setGoods_name(commonSdkChargeInfo.getGoods_name());
		hyPayInfo.setGoods_count(commonSdkChargeInfo.getGoods_count());
		hyPayInfo.setGoods_desc(commonSdkChargeInfo.getGoods_desc());
		
		hyPayInfo.setGame_coin(commonSdkChargeInfo.getGame_coin());
		hyPayInfo.setExtra(commonSdkChargeInfo.getExtra());//游戏cp 私有字段，可传

		HySDK.getInstance().HyShowPayView(activity, hyPayInfo);
	
	}

	@Override
	public void controlFlow(Activity arg0, boolean arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getAdult(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getChannelID() {
		// TODO Auto-generated method stub
		return "0";
	}

	@Override
	public String getChannelName() {
		// TODO Auto-generated method stub
		return "花样游戏";
	}

	@Override
	public void getOderId(CommonSdkChargeInfo arg0, Activity arg1, CommonSDKHttpCallback arg2) {
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
		return "1.0";
	}

	@Override
	public boolean hasExitView() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void init(Activity activity, CommonSdkInitInfo commonSdkInitInfo, final CommonSdkCallBack callBack, ImplCallback implCallback) {
		this.mActivity = activity;
		this.mBack = callBack;
		this.implCallback = implCallback;
		
		
		HyInitInfo initInfo = new HyInitInfo();
		initInfo.setDebug(commonSdkInitInfo.isDebug());
		initInfo.setGameId(commonSdkInitInfo.getGameId(activity));
		initInfo.setGameVersion(commonSdkInitInfo.getGameVersion());
		HySDK.getInstance().HyInitSDK(activity, initInfo,new OnInitFinishedListener() {
			
			@Override
			public void onInitFinish(int code, String desc) {
				callBack.initOnFinish("初始化成功", 0);
			}
		});
	}

	@Override
	public void login(Activity activity, CommonSdkLoginInfo initInfo) {
		// TODO Auto-generated method stub
		HySDK.getInstance().HyShowLoginView(activity, loginFinishedListener);
	}
	private com.tomato.fqsdk.clinterface.HyInterface.OnLoginFinishedListener loginFinishedListener=new OnLoginFinishedListener() {
		
		@Override
		public void onLoginFinished(int ret, HyLoginResult paramUser) {
			// TODO Auto-generated method stub
			switch (ret) {
			case 0:// 登录成功
				String uid = paramUser.getUid();
				String token = paramUser.getTokenKey();
//				FLogger.d("uid:"+uid+" token:"+token);
				sdkUid=uid;
				if (TextUtils.isEmpty(uid)) {
					uid = HySDK.getInstance().HyGetCurrentUserID();
					if (TextUtils.isEmpty(uid)) {
						// 重新显示登录页面
						 HySDK.getInstance().HyShowLoginView(mActivity, null);
				}
				}
				else {
					JSONObject result_data = new JSONObject();
					try {
						result_data.put("userId", paramUser.getUid());
						result_data.put("token", paramUser.getTokenKey());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					CommonBackLoginInfo.getInstance().statusCode=0;
					CommonBackLoginInfo.getInstance().userId=paramUser.getUid();
					CommonBackLoginInfo.getInstance().userName=paramUser.getAccount();
					CommonBackLoginInfo.getInstance().channelToken=paramUser.getTokenKey();
					CommonBackLoginInfo.getInstance().platformChanleId=HySDKManager.getInstance().getChanleId(mActivity);
					CommonBackLoginInfo.getInstance().timestamp=HySDKManager.getInstance().getTimeZ();
				mBack.loginOnFinish(CommonBackLoginInfo.getInstance().toString(), 0);
				//不需要校验
//					implCallback.onLoginVerify(result_data);	
				}
				
				break;
			case 1:// sdk登录页面退出 
				mBack.logoutOnFinish("取消登录", 2);
				break;
			default:
				break;
			}
		}
	};
	@Override
	public void reLogin(Activity activity, CommonSdkLoginInfo arg1) {
		// TODO Auto-generated method stub
		HySDK.getInstance().HyShowLoginView(activity, loginFinishedListener);
	}

	@Override
	public void setDebug(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean showExitView(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean showPersonView(Activity arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void submitExtendData(Activity arg0, CommonSdkExtendData arg1) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void roleCreate(Activity arg0, CommonSdkExtendData arg1) {
		
		
	}

	@Override
	public void roleLevelUpdate(Activity arg0, CommonSdkExtendData arg1) {
		// TODO Auto-generated method stub
		
	}
}
