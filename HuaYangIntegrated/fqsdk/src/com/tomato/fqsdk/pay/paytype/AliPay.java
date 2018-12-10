//package com.tomato.fqsdk.pay.paytype;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//import java.util.Random;
//
//import android.app.Activity;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.alipay.sdk.app.PayTask;
//import com.tomato.fqsdk.pay.alipay.PayResult;
//public class AliPay {
//	private static final int SDK_PAY_FLAG = 1;
//	public void StartAliPay(final Activity activity,String order,final AliPayCallBack aliPayCallBack){
//		
//		// ֧     ص 
//				final Handler mHandler = new Handler() {
//					public void handleMessage(Message msg) {
//						switch (msg.what) {
//						case SDK_PAY_FLAG: {
//							PayResult payResult = new PayResult((String) msg.obj);
//
//							// ֧       ش˴ ֧         ǩ       ֧    ǩ    Ϣ  ǩԼʱ֧     ṩ Ĺ Կ    ǩ
//							String resultInfo = payResult.getResult();
//
//							String resultStatus = payResult.getResultStatus();
//							//  ж resultStatus Ϊ  9000       ֧   ɹ       ״̬       ɲο  ӿ  ĵ 
//							if (TextUtils.equals(resultStatus, "9000")) {
//								aliPayCallBack.onFinish(0);
//							} else {
//								//  ж resultStatus Ϊ ǡ 9000          ֧  ʧ  
//								//   8000      ֧       Ϊ֧      ԭ     ϵͳԭ   ڵȴ ֧     ȷ ϣ    ս    Ƿ ɹ  Է     첽֪ͨΪ׼  С    ״̬  
//								if (TextUtils.equals(resultStatus, "8000")) {
//									aliPayCallBack.onFinish(2);
//									Log.e("CL", "resultStatus:"+resultStatus+" resultInfo:"+resultInfo);
//								} else {
//									//     ֵ Ϳ    ж Ϊ֧  ʧ ܣ      û     ȡ  ֧        ϵͳ   صĴ   
//									aliPayCallBack.onFinish(1);
//									Log.e("CL", "resultStatus:"+resultStatus+" resultInfo:"+resultInfo);
//								}
//							}
//							break;
//						}
//						default:
//							break;
//						}
//					};
//				};
//
//				/**
//				 * ֧     ĵ   
//				 */
//				class PayRunnable implements Runnable {
//					private String payInfo = "";
//
//					public PayRunnable(String arg0) {
//						payInfo = arg0;
//					}
//
//					@Override
//					public void run() {
//						PayTask task = new PayTask(activity);
//						String result = task.pay(payInfo, true);
//						Message msg = new Message();
//						msg.what = SDK_PAY_FLAG;
//						msg.obj = result;
//						mHandler.sendMessage(msg);
//					}
//				}
//				PayRunnable payRunnable = new PayRunnable(order);
////				AliPay.getPayInfo(activity.getApplicationContext(),
////						subject, detail,
////						totalFee, orderId,notify);
////				PayRunnable payRunnable = new PayRunnable();
//				Thread payThread = new Thread(payRunnable);
//				payThread.start();
//	}
//	public interface AliPayCallBack{
//		public void onFinish(int ret);
//	}
//	/**
//	 * call alipay sdk pay.     SDK֧  
//	 * 
//	 */
//	public void pay() {
//		
//	}
//
//	
//	/**
//	 *   ȡ     ķ   ֧         淶 Ķ     Ϣ
//	 * 
//	 * @param subject
//	 *              Ʒ  
//	 *            <p>
//	 *             ò    Ϊ128     ֡ 
//	 * @param body
//	 *              Ʒ   
//	 * @param coast
//	 *             ܽ  
//	 * @param orderId
//	 *             ̻   վΨһ      
//	 * @return
//	 */
////	public static String getPayInfo(Context context,String subject, String body, String price,
////			String outTradeNo,String notify) {
////		//     
////		String orderInfo = getOrderInfo(context,subject, body, price, outTradeNo,notify);
////
////		//  Զ     RSA ǩ  
////		String sign = sign(context,orderInfo);
////		try {
////			//      sign   URL    
////			sign = URLEncoder.encode(sign, "UTF-8");
////		} catch (UnsupportedEncodingException e) {
////			e.printStackTrace();
////		}
////
////		//      ķ   ֧         淶 Ķ     Ϣ
////		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
////				+ getSignType();
////		Log.e("CL", payInfo);
////		return payInfo;
////	}
//
//	/**
//	 * create the order info.           Ϣ
//	 * 
//	 */
////	public static String getOrderInfo(Context context,String subject, String body,
////			String price, String outTradeNo,String notify) {
////		String orderInfo = "";
////
////		// ǩԼ         ID
////		orderInfo = "partner=" + "\"" + HJNaviteHelper.getAliPartner(context) + "\"";
////
////		// ǩԼ    ֧     ˺ 
////		orderInfo += "&seller_id=" + "\"" + HJNaviteHelper.getAliSeller(context)
////				+ "\"";
////
////		//  ̻   վΨһ      
////		orderInfo += "&out_trade_no=" + "\"" + outTradeNo + "\"";
////
////		//   Ʒ    
////		orderInfo += "&subject=" + "\"" + subject + "\"";
////
////		//   Ʒ    
////		orderInfo += "&body=" + "\"" + body + "\"";
////
////		//   Ʒ   
////		orderInfo += "&total_fee=" + "\"" + price + "\"";
////		//        첽֪ͨҳ  ·  
////		orderInfo += "&notify_url=" + "\""
////				+ notify + "\"";
////
////		//     ӿ    ƣ   ̶ ֵ
////		orderInfo += "&service=\"mobile.securitypay.pay\"";
////
////		// ֧     ͣ   ̶ ֵ
////		orderInfo += "&payment_type=\"1\"";
////
////		//        룬  ̶ ֵ
////		orderInfo += "&_input_charset=\"utf-8\"";
////
////		//     δ    ׵ĳ ʱʱ  
////		// Ĭ  30   ӣ һ    ʱ   ñʽ  ׾ͻ  Զ    رա 
////		// ȡֵ  Χ  1m  15d  
////		// m-   ӣ h-Сʱ  d- 죬1c-   죨   ۽  ׺ ʱ          0  رգ   
////		//  ò     ֵ      С   㣬  1.5h    ת  Ϊ90m  
////		orderInfo += "&it_b_pay=\"30m\"";
////
////		// extern_tokenΪ         Ȩ  ȡ    alipay_open_id,   ϴ˲    û   ʹ    Ȩ   ˻     ֧  
////		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";
////
////		// ֧              󣬵 ǰҳ    ת   ̻ ָ  ҳ   ·     ɿ 
////		orderInfo += "&return_url=\"m.alipay.com\"";
////
////		//        п ֧         ô˲         ǩ      ̶ ֵ     ҪǩԼ         п    ֧        ʹ ã 
////		// orderInfo += "&paymethod=\"expressGateway\"";
////
////		return orderInfo;
////	}
//
//	/**
//	 * get the out_trade_no for an order.      ̻      ţ   ֵ   ̻   Ӧ    Ψһ     Զ    ʽ 淶  
//	 * 
//	 */
//	public static String getOutTradeNo() {
//		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
//				Locale.getDefault());
//		Date date = new Date();
//		String key = format.format(date);
//
//		Random r = new Random();
//		key = key + r.nextInt();
//		key = key.substring(0, 15);
//		return key;
//	}
//
//	/**
//	 * sign the order info.  Զ     Ϣ    ǩ  
//	 * 
//	 * @param content
//	 *              ǩ        Ϣ
//	 */
////	private static String sign(Context context,String content) {
////		return SignUtils.sign(content, HJNaviteHelper.getAliRsaPrivate(context));
////
////	}
//
////	/**
////	 * sign the order info.  Զ     Ϣ    ǩ  
////	 * 
////	 * @param content
////	 *              ǩ        Ϣ
////	 */
////	private static String sign(String content, String key) {
////		return SignUtils.sign(content, key);
////	}
////
////	/**
////	 * get the sign type we use.   ȡǩ    ʽ
////	 * 
////	 */
////	private static String getSignType() {
////		return "sign_type=\"RSA\"";
////	}
//	// /**
//	// * get the sdk version.   ȡSDK 汾  
//	// *
//	// */
//	// public void getSDKVersion() {
//	// PayTask payTask = new PayTask(this);
//	// String version = payTask.getVersion();
//	// Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
//	// }
//
//}
