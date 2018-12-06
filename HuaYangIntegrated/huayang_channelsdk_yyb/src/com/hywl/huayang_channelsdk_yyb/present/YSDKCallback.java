//package com.hywl.huayang_channelsdk_yyb.present;
//
//import android.app.Activity;
//import android.util.Log;
//
//import com.fqwl.hycommonsdk.util.ToastUtil;
//import com.fqwl.hycommonsdk.util.logutils.FLogger;
//import com.tencent.ysdk.framework.common.eFlag;
//import com.tencent.ysdk.module.bugly.BuglyListener;
//import com.tencent.ysdk.module.pay.PayListener;
//import com.tencent.ysdk.module.pay.PayRet;
//import com.tencent.ysdk.module.user.PersonInfo;
//import com.tencent.ysdk.module.user.UserListener;
//import com.tencent.ysdk.module.user.UserLoginRet;
//import com.tencent.ysdk.module.user.UserRelationRet;
//import com.tencent.ysdk.module.user.WakeupRet;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
///** 
// * TODO GAME 游戏需要根据自己的逻辑实现自己的YSDKCallback对象。 
// * YSDK通过UserListener抽象类中的方法将授权或查询结果回调给游戏。
// * 游戏根据回调结果调整UI等。只有设置回调，游戏才能收到YSDK的响应。
// * 这里是Java层回调(设置了Java层回调会优先调用Java层回调, 如果要使用C++层回调则不能设置Java层回调)
// */
//public class YSDKCallback implements UserListener, BuglyListener,PayListener {
//    public static Activity mActivity;
//
//    
//    public YSDKCallback(Activity activity) {
//    	mActivity = (Activity) activity;
//    }
//    
//    public void OnLoginNotify(UserLoginRet ret) {
//    	FLogger.d(ret.toString());
//		String accessToken = ret.getAccessToken();
//		String payToken = ret.getPayToken();
//		int flag = ret.flag;
//		String open_id = ret.open_id;
//		ToastUtil.toastInfo(mActivity, "有货了");
//		FLogger.w( "OnLoginNotify accessToken=" + accessToken + " payToken=" + payToken + " flag=" + flag
//				+ " open_id=" + open_id+" ret.ret="+ret.ret);
////		if (qqLoginDialog != null) {
////			qqLoginDialog.dismiss();
////		}
////		qqLoginDialog = null;
//
//		Log.d("commonsdk", "YSDKCallback.OnLoginNotify ret.flag=" + ret.flag);
////        switch (ret.flag) {
////            case eFlag.Succ:
////                Activity.letUserLogin();
////                break;
////            // 游戏逻辑，对登录失败情况分别进行处理
////            case eFlag.QQ_UserCancel:
////                Activity.showToastTips("用户取消授权，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.QQ_LoginFail:
////                Activity.showToastTips("QQ登录失败，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.QQ_NetworkErr:
////                Activity.showToastTips("QQ登录异常，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.QQ_NotInstall:
////                Activity.showToastTips("手机未安装手Q，请安装后重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.QQ_NotSupportApi:
////                Activity.showToastTips("手机手Q版本太低，请升级后重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.WX_NotInstall:
////                Activity.showToastTips("手机未安装微信，请安装后重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.WX_NotSupportApi:
////                Activity.showToastTips("手机微信版本太低，请升级后重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.WX_UserCancel:
////                Activity.showToastTips("用户取消授权，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.WX_UserDeny:
////                Activity.showToastTips("用户拒绝了授权，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.WX_LoginFail:
////                Activity.showToastTips("微信登录失败，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.Login_TokenInvalid:
////                Activity.showToastTips("您尚未登录或者之前的登录已过期，请重试");
////                Activity.letUserLogout();
////                break;
////            case eFlag.Login_NotRegisterRealName:
////                // 显示登录界面
////                Activity.showToastTips("您的账号没有进行实名认证，请实名认证后重试");
////                Activity.letUserLogout();
////                break;
////            default:
////                // 显示登录界面
////                Activity.letUserLogout();
////                break;
////        }
//    }
//
//    public void OnWakeupNotify(WakeupRet ret) {
//        Log.d("fq","called");
//        Log.d("fq","flag:" + ret.flag);
//        Log.d("fq","msg:" + ret.msg);
//        Log.d("fq","platform:" + ret.platform);
////        Activity.platform = ret.platform;
////        // TODO GAME 游戏需要在这里增加处理异账号的逻辑
////        if (eFlag.Wakeup_YSDKLogining == ret.flag) {
////            // 用拉起的账号登录，登录结果在OnLoginNotify()中回调
////        } else if (ret.flag == eFlag.Wakeup_NeedUserSelectAccount) {
////            // 异账号时，游戏需要弹出提示框让用户选择需要登录的账号
////            Log.d(Activity.LOG_TAG,"diff account");
////            Activity.showDiffLogin();
////        } else if (ret.flag == eFlag.Wakeup_NeedUserLogin) {
////            // 没有有效的票据，登出游戏让用户重新登录
////            Log.d(Activity.LOG_TAG,"need login");
////            Activity.letUserLogout();
////        } else {
////            Log.d(Activity.LOG_TAG,"logout");
////            Activity.letUserLogout();
////        }
//    }
//
//    @Override
//    public void OnRelationNotify(UserRelationRet relationRet) {
//    	String result = "";
//        result = result +"flag:" + relationRet.flag + "\n";
//        result = result +"msg:" + relationRet.msg + "\n";
//        result = result +"platform:" + relationRet.platform + "\n";
//        if (relationRet.persons != null && relationRet.persons.size()>0) {
//            PersonInfo personInfo = (PersonInfo)relationRet.persons.firstElement();
//            StringBuilder builder = new StringBuilder();
//            builder.append("UserInfoResponse json: \n");
//            builder.append("nick_name: " + personInfo.nickName + "\n");
//            builder.append("open_id: " + personInfo.openId + "\n");
//            builder.append("userId: " + personInfo.userId + "\n");
//            builder.append("gender: " + personInfo.gender + "\n");
//            builder.append("picture_small: " + personInfo.pictureSmall + "\n");
//            builder.append("picture_middle: " + personInfo.pictureMiddle + "\n");
//            builder.append("picture_large: " + personInfo.pictureLarge + "\n");
//            builder.append("provice: " + personInfo.province + "\n");
//            builder.append("city: " + personInfo.city + "\n");
//            builder.append("country: " + personInfo.country + "\n");
//            result = result + builder.toString();
//        } else {
//            result = result + "relationRet.persons is bad";
//        }
//        Log.d("fq","OnRelationNotify" + result);
//
//        // 发送结果到结果展示界面
////       Activity.sendResult(result);
//    }
//
//    @Override
//    public String OnCrashExtMessageNotify() {
//        // 此处游戏补充crash时上报的额外信息
//        Log.d("fq",String.format(Locale.CHINA, "OnCrashExtMessageNotify called"));
//        Date nowTime = new Date();
//        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        return "new Upload extra crashing message for bugly on " + time.format(nowTime);
//    }
//
//	@Override
//	public byte[] OnCrashExtDataNotify() {
//		return null;
//	}
//
//    @Override
//    public void OnPayNotify(PayRet ret) {
////        Log.d(Activity.LOG_TAG,ret.toString());
////        if(PayRet.RET_SUCC == ret.ret){
////            //支付流程成功
////            switch (ret.payState){
////                //支付成功
////                case PayRet.PAYSTATE_PAYSUCC:
////                    Activity.sendResult(
////                            "用户支付成功，支付金额"+ret.realSaveNum+";" +
////                            "使用渠道："+ret.payChannel+";" +
////                            "发货状态："+ret.provideState+";" +
////                            "业务类型："+ret.extendInfo+";建议查询余额："+ret.toString());
////                    break;
////                //取消支付
////                case PayRet.PAYSTATE_PAYCANCEL:
////                    Activity.sendResult("用户取消支付："+ret.toString());
////                    break;
////                //支付结果未知
////                case PayRet.PAYSTATE_PAYUNKOWN:
////                    Activity.sendResult("用户支付结果未知，建议查询余额："+ret.toString());
////                    break;
////                //支付失败
////                case PayRet.PAYSTATE_PAYERROR:
////                    Activity.sendResult("支付异常"+ret.toString());
////                    break;
////            }
////        }else{
////            switch (ret.flag){
////                case eFlag.Login_TokenInvalid:
////                    Activity.sendResult("登录态过期，请重新登录："+ret.toString());
////                    Activity.letUserLogout();
////                    break;
////                case eFlag.Pay_User_Cancle:
////                    //用户取消支付
////                    Activity.sendResult("用户取消支付："+ret.toString());
////                    break;
////                case eFlag.Pay_Param_Error:
////                    Activity.sendResult("支付失败，参数错误"+ret.toString());
////                    break;
////                case eFlag.Error:
////                default:
////                    Activity.sendResult("支付异常"+ret.toString());
////                    break;
////            }
////        }
//    }
//}
//
