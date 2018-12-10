//package com.tomato.fqsdk.pay.paytype;
//
//import java.io.StringReader;
//import java.net.Inet4Address;
//import java.net.InetAddress;
//import java.net.NetworkInterface;
//import java.net.SocketException;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Random;
//
//import org.xmlpull.v1.XmlPullParser;
//
//import android.content.Context;
//import android.net.wifi.WifiInfo;
//import android.net.wifi.WifiManager;
//import android.util.Log;
//import android.util.Xml;
//
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.tomato.fqsdk.pay.wxpay.MD5;
//
//@SuppressWarnings("deprecation")
//public class WxPay {
//	public static IWXAPI wxAPI = null;
//	Map<String, String> resultunifiedorder;
//
//	/**
//	 *   ʼ  ΢  ֧  
//	 * 
//	 * @param context
//	 * @param wechatAppID
//	 * @return
//	 */
//	public static String initWechatPay(Context context, String wechatAppID) {
//		String errMsg = null;
//
//		if (context == null) {
//			errMsg = "Error: initWechatPay context        Ϊnull.";
//			Log.e("BCPay", errMsg);
//			return errMsg;
//		}
//
//		if ((wechatAppID == null) || (wechatAppID.length() == 0)) {
//			errMsg = "Error: initWechatPay wx_appid    Ϊ Ϸ   ΢  AppID.";
//			Log.e("BCPay", errMsg);
//			return errMsg;
//		}
//
//		wxAPI = WXAPIFactory.createWXAPI(context, null);
//
//		// BCCache.getInstance().wxAppId = wechatAppID;
//		try {
//			if (isWXPaySupported()) {
//				wxAPI.registerApp(wechatAppID);
//			} else {
//				errMsg = "Error:   װ  ΢ Ű汾  ֧  ֧  .";
//				Log.d("HJPay", errMsg);
//			}
//		} catch (Exception ignored) {
//			errMsg = "Error:  ޷ ע  ΢   " + wechatAppID + ". Exception: "
//					+ ignored.getMessage();
//			Log.e("HJPay", errMsg);
//		}
//
//		return errMsg;
//	}
//
//	/**
//	 * ΢   Ƿ ֧  
//	 */
//	public static boolean isWXPaySupported() {
//		boolean isPaySupported = false;
//		if (wxAPI != null) {
//			isPaySupported = wxAPI.getWXAppSupportAPI() >= 570425345;
//		}
//		return isPaySupported;
//	}
//
//	/**
//	 * ΢   Ƿ    
//	 */
//	public static boolean isWXAppInstalledAndSupported(IWXAPI wxAPI) {
//		boolean isWXAppInstalledAndSupported = false;
//		if (wxAPI != null) {
//			isWXAppInstalledAndSupported = (wxAPI.isWXAppInstalled())
//					&& (wxAPI.isWXAppSupportAPI());
//		}
//		return isWXAppInstalledAndSupported;
//	}
//
////	/**
////	 *   ȡ      Ϣ
////	 * 
////	 * @param activity
////	 * @param telephonyManager
////	 * @param subject
////	 * @param price
////	 * @return
////	 */
////	public static String genProductArgs(Activity activity,
////			TelephonyManager telephonyManager, String subject, String detail,
////			String price, String outRradeNo,String notify) {
////		StringBuffer xml = new StringBuffer();
////
////		try {
////			xml.append("</xml>");
////			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
////			
////			packageParams.add(new BasicNameValuePair("appid", HJNaviteHelper
////					.GetWXAppId()));
////			packageParams.add(new BasicNameValuePair("attach", HJControlCenter.getInstance().getControlConfig().getAppId()));
////			packageParams.add(new BasicNameValuePair("body", subject));
////			packageParams.add(new BasicNameValuePair("detail", detail));
////			packageParams.add(new BasicNameValuePair("device_info",
////					telephonyManager.getSimSerialNumber()));
////
////			packageParams.add(new BasicNameValuePair("mch_id", HJNaviteHelper
////					.GetWXMchId()));
////
////			packageParams.add(new BasicNameValuePair("nonce_str", WxPay
////					.genNonceStr()));
////
////			packageParams.add(new BasicNameValuePair("notify_url",
////					notify));//http://open.huanjia.cc/PayNotify/TenpayNotify  "http://sdkapi.joyhj.com/notify/weixin"
////			packageParams
////					.add(new BasicNameValuePair("out_trade_no", outRradeNo));
////			packageParams.add(new BasicNameValuePair("spbill_create_ip","127.0.0.1"));//
////			try {
////				if (price.contains(".")) {
////					if (price.length() - price.lastIndexOf(".") > 2) {
////						double mprice = Double.parseDouble(price);
////						packageParams.add(new BasicNameValuePair("total_fee",
////								String.valueOf((int) (mprice * 100))));
////					} else {
////						float mprice = Float.parseFloat(price);
////						packageParams.add(new BasicNameValuePair("total_fee",
////								String.valueOf((int) (mprice * 100))));
////					}
////				} else {
////					int mprice = Integer.parseInt(price);
////					packageParams.add(new BasicNameValuePair("total_fee",
////							String.valueOf((int) (mprice * 100))));
////
////				}
////			} catch (Exception e) {
////				Toast.makeText(activity, "    Ľ      ", 0).show();
////				activity.finish();
////			}
////			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
////			String sign = WxPay.genPackageSign(packageParams,activity.getBaseContext());
////			packageParams.add(new BasicNameValuePair("sign", sign));
////			String xmlstring = WxPay.toXml(packageParams);
////			xmlstring = new String(xmlstring.getBytes("UTF-8"), "ISO-8859-1");
////			return xmlstring;
////
////		} catch (Exception e) {
////			return null;
////		}
////
////	}
//
////	public static String genAppSign(List<NameValuePair> params,Context context) {
////		StringBuilder sb = new StringBuilder();
////
////		for (int i = 0; i < params.size(); i++) {
////			sb.append(params.get(i).getName());
////			sb.append('=');
////			sb.append(params.get(i).getValue());
////			sb.append('&');
////		}
////		sb.append("key=");
////		sb.append(HJNaviteHelper.getWXApiKey(context));
////
////		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
////				.toUpperCase();
////		return appSign;
////	}
//
//	public static String genNonceStr() {
//		Random random = new Random();
//		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
//				.getBytes());
//	}
//
//	public static long genTimeStamp() {
//		return System.currentTimeMillis() / 1000;
//	}
//
//	public static Map<String, String> decodeXml(String content) {
//
//		try {
//			Map<String, String> xml = new HashMap<String, String>();
//			XmlPullParser parser = Xml.newPullParser();
//			parser.setInput(new StringReader(content));
//			int event = parser.getEventType();
//			while (event != XmlPullParser.END_DOCUMENT) {
//
//				String nodeName = parser.getName();
//				switch (event) {
//				case XmlPullParser.START_DOCUMENT:
//
//					break;
//				case XmlPullParser.START_TAG:
//
//					if ("xml".equals(nodeName) == false) {
//						// ʵ    student    
//						xml.put(nodeName, parser.nextText());
//					}
//					break;
//				case XmlPullParser.END_TAG:
//					break;
//				}
//				event = parser.next();
//			}
//
//			return xml;
//		} catch (Exception e) {
//			Log.e("orionexception", e.toString());
//		}
//		return null;
//
//	}
//
//	//
//
//	/**
//	 *   ȡ ն ip
//	 * 
//	 * @return
//	 */
//	public static String GetHostIp() {
//		try {
//			for (Enumeration<NetworkInterface> en = NetworkInterface
//					.getNetworkInterfaces(); en.hasMoreElements();) {
//				NetworkInterface intf = en.nextElement();
//				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
//						.hasMoreElements();) {
//					InetAddress inetAddress = ipAddr.nextElement();
//					if (!inetAddress.isLoopbackAddress()
//							&& (inetAddress instanceof Inet4Address)) {
//						return inetAddress.getHostAddress();
//					}
//				}
//			}
//
//		} catch (SocketException ex) {
//		} catch (Exception e) {
//		}
//		return null;
//	}
//
//	/**
//	 *   ip        ʽת    ip  ʽ
//	 * 
//	 * @param ipInt
//	 * @return
//	 */
//	public static String int2ip(int ipInt) {
//		StringBuilder sb = new StringBuilder();
//		sb.append(ipInt & 0xFF).append(".");
//		sb.append((ipInt >> 8) & 0xFF).append(".");
//		sb.append((ipInt >> 16) & 0xFF).append(".");
//		sb.append((ipInt >> 24) & 0xFF);
//		return sb.toString();
//	}
//
//	public static String getLocalIpAddress(Context context) {
//		try {
//			WifiManager wifiManager = (WifiManager) context
//					.getSystemService(Context.WIFI_SERVICE);
//			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
//			int i = wifiInfo.getIpAddress();
//			return int2ip(i);
//		} catch (Exception ex) {
//			return "   ȡIP      !!!! 뱣֤  WIFI,         ´     !\n" + ex.getMessage();
//		}
//		// return null;
//	}
//
////	/**
////	 *     ǩ  
////	 */
////
////	public static String genPackageSign(List<NameValuePair> params,Context context) {
////		StringBuilder sb = new StringBuilder();
////
////		for (int i = 0; i < params.size(); i++) {
////			sb.append(params.get(i).getName());
////			sb.append('=');
////			sb.append(params.get(i).getValue());
////			sb.append('&');
////		}
////		sb.append("key=");
////		sb.append(HJNaviteHelper.getWXApiKey(context));
////
////		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
////				.toUpperCase();
////		return packageSign;
////	}
//
////	public static String toXml(List<NameValuePair> params) {
////		StringBuilder sb = new StringBuilder();
////		sb.append("<xml>");
////		for (int i = 0; i < params.size(); i++) {
////			sb.append("<" + params.get(i).getName() + ">");
////
////			sb.append(params.get(i).getValue());
////			sb.append("</" + params.get(i).getName() + ">");
////		}
////		sb.append("</xml>");
////
////		return sb.toString();
////	}
//
//	public static String genOutTradNo() {
//		Random random = new Random();
//		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
//				.getBytes());
//	}
//}
