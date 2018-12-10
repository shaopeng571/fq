package com.tomato.fqsdk;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;

import com.tomato.fqsdk.adapter.PayAdapter;
import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.control.CLControlCenter;
import com.tomato.fqsdk.control.HySDK;
import com.tomato.fqsdk.data.PostUserInfo;
import com.tomato.fqsdk.fqutils.FLogger;
import com.tomato.fqsdk.data.HyApi.HttpCallback;
import com.tomato.fqsdk.models.HyPayInfo;
import com.tomato.fqsdk.models.CLPayResult;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;
import com.tomato.fqsdk.utils.HyAppUtils;
import com.tomato.fqsdk.utils.JsonParse;

@SuppressLint("NewApi")
public class HyPayMainActivity extends BaseActivity
		implements  OnClickListener, OnItemClickListener {
	private static HyPayMainActivity context;
	// private static final int SDK_PAY_FLAG = 1;
	private ImageView iv_back;
	private ImageView iv_gamehp;
	private TextView tv_gamename;
	private TextView tv_pay;
	private TextView tv_money;
	private TextView tv_payuser;
	private GridView gridView;
	private WebView webView;
	private int paytype = 0;
	// private TelephonyManager telephonyManager;
	private PopupWindow popupWindow;
	private View view;
	private CLControlCenter mControlCenter;
	private PayAdapter adapter;
	// private String code;
//	static CLPayResult mHjPayResult;
	private HyPayInfo hyPayInfo;

	private static final int mCode_wechat = 0;
	private static final int mCode_alipay = 1;
	private static final int mCode_wechatwap = 2;
	// 微信订单 ???
	String wxorderid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(FindResHelper.RLayout("hj_pay_main"));
		mControlCenter = CLControlCenter.getInstance();
		
//		mHjPayResult = CLPayResult.getInstance();
		context = this;
		// 获取支付参数
		try {
//			mHjPayResult = CLPayResult.getInstance();
			Bundle bundle = getIntent().getExtras();
			hyPayInfo = (HyPayInfo) bundle.getSerializable("payInfo");
//			mHjPayResult.setPay(mHjPayRequest.getUid(), mHjPayRequest.getPlayerid(), mHjPayRequest.getSubject(),
//					mHjPayRequest.getDetail(), mHjPayRequest.getTotalFee(), mHjPayRequest.getQuantity(),
//					mHjPayRequest.getPrice(), mHjPayRequest.getGoodsid(), mHjPayRequest.getSid(),
//					mHjPayRequest.getExtra());
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		initView();

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		BitmapDrawable bd=new BitmapDrawable(getResources(), HyAppUtils.getBitmap(context));
		
		// 游戏图标，游戏名
		iv_gamehp.setBackground(bd);
		tv_gamename.setText(HyAppUtils.getAppName(context));
		// 支付选择
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		// 支付金额
		tv_money.setText(hyPayInfo.getMoney() + "");
		// 支付用户
		// if (hjControlCenter.isTemp(context)) {
		tv_payuser.setText(mControlCenter.getCurrentName());
		// } else {
		// tv_payuser.setText(hjControlCenter
		// .getCurrentName(HJPayMainActivity.this));
		// }
	}

	// public String GetTempName(String str){
	// try {
	// str=str.substring(0, 8);
	// } catch (Exception e) {
	// return str;
	// }
	// return str;
	//
	// }
	@Override
	public void initView() {
		// telephonyManager = (TelephonyManager) this
		// .getSystemService(Context.TELEPHONY_SERVICE);
		webView = (WebView) findViewById(FindResHelper.RId("web_wechatpay"));
		iv_back = (ImageView) findViewById(FindResHelper.RId("iv_pay_back"));
		iv_gamehp = (ImageView) findViewById(FindResHelper.RId("iv_pay_gamehp"));
		tv_gamename = (TextView) findViewById(FindResHelper.RId("tv_pay_gamename"));
		tv_pay = (TextView) findViewById(FindResHelper.RId("tv_pay_ok"));
		tv_money = (TextView) findViewById(FindResHelper.RId("iv_pay_money"));
		gridView = (GridView) findViewById(FindResHelper.RId("payment_gridview"));
		tv_payuser = (TextView) findViewById(FindResHelper.RId("tv_pay_user"));

		iv_back.setOnClickListener(this);
		tv_pay.setOnClickListener(this);

		adapter = new PayAdapter(HyPayMainActivity.this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

		LayoutInflater inflater = getLayoutInflater();
		view = inflater.inflate(FindResHelper.RLayout("hj_pay_isback"), null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == FindResHelper.RId("iv_pay_back")) {
			showPopupWindow();
		}
		if (v.getId() == FindResHelper.RId("tv_pay_ok")) {
			ChoosePay();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		adapter.selecter(position);
		ChooseType(position);
		adapter.notifyDataSetChanged();
	}

	public void ChooseType(int num) {
		paytype = num;
	}

	/**
	 * 选择支付方式
	 */
	public void ChoosePay() {
		if (!TextUtils.isEmpty(mControlCenter.getCurrentName())) {
			switch (paytype) {
			case 0:
				// 支付 ???
				doOrdPay(1);
				break;
			case 1:
				// 微信支付
				doOrdPay(2);
//				// 微信
//				if (WxPay.isWXAppInstalledAndSupported(mWXApi)) {
//					progressDialog.setMessage(FindResHelper.RStringStr("hj_toast_paywxpayloading"));
//					progressDialog.show();
//					// 微信支付
//					doOrdPay(2);
//				} else {
//					// 请安装微 ???
//					HySDK.CLToast(FindResHelper.RStringStr("hj_toast_paypleaseupwxpay"));
//				}

				break;
			default:
				break;
			}
		} else {
			HySDK.HyToast("请先进行登录");
			return;
		}

	}

	/**
	 * 微信支付
	 */
//	public void Wxpay(String order) {
//		PayReq request = new PayReq();
//		progressDialog.dismiss();
//		try {
//			JSONObject jsonObject = new JSONObject(order);
//			request.appId = jsonObject.optString("appid");
//			request.partnerId = jsonObject.optString("partnerid");
//			request.prepayId = jsonObject.optString("prepayid");
//			request.packageValue = jsonObject.optString("package");
//			request.nonceStr = jsonObject.optString("noncestr");
//			request.timeStamp = jsonObject.optString("timestamp");
//			request.sign = jsonObject.optString("sign");
//
//			mWXApi.sendReq(request);
//		} catch (JSONException e) {
//			// progressDialog.dismiss();
//			HySDK.CLToast(FindResHelper.RStringStr("hj_toast_paypayfail"));
//		}
//
//		// request.appId = HJNaviteHelper.GetWXAppId();
//		// request.partnerId = HJNaviteHelper.GetWXMchId();
//		// request.prepayId = wxorderid;
//		// request.packageValue = "Sign=WXPay";
//		// request.nonceStr = WxPay.genNonceStr();
//		// request.timeStamp = String.valueOf(WxPay.genTimeStamp());
//		//
//		// List<NameValuePair> signParams = new LinkedList<NameValuePair>();
//		// signParams.add(new BasicNameValuePair("appid", request.appId));
//		// signParams.add(new BasicNameValuePair("noncestr", request.nonceStr));
//		// signParams.add(new BasicNameValuePair("package",
//		// request.packageValue));
//		// signParams.add(new BasicNameValuePair("partnerid",
//		// request.partnerId));
//		// signParams.add(new BasicNameValuePair("prepayid", request.prepayId));
//		// signParams.add(new BasicNameValuePair("timestamp",
//		// request.timeStamp));
//		//
//		// request.sign = WxPay.genAppSign(signParams, HJPayMainActivity.this);
//		// HJControlCenter..registerApp(Constants.APP_ID);
//
//	}

	// /**
	// * 微信统一下单
	// */
	// private void Wxpayhead2(String notify) {
	// try {
	// AsyncHttpClient wx = new AsyncHttpClient();
	// wx.addHeader("Accept", "application/json");
	// // wx.addHeader("Content-type", "application/json");
	// String url = String
	// .format("https://api.mch.weixin.qq.com/pay/unifiedorder");
	// String subject = new String(mHjPayRequest.getSubject().getBytes(),
	// "UTF-8");
	// String detail = new String(mHjPayRequest.getDetail().getBytes(),
	// "UTF-8");
	// String entity = WxPay.genProductArgs(HJPayMainActivity.this,
	// telephonyManager, mHjPayRequest.getSubject(),
	// mHjPayRequest.getDetail(),
	// mHjPayRequest.getTotalFee() + "",
	// mHjPayResult.getOutRradeNo(),notify);
	//
	// wx.post(HJPayMainActivity.this, url, new StringEntity(entity),
	// "application/json", new TextHttpResponseHandler() {
	//
	// @Override
	// public void onSuccess(int statusCode, Header[] headers,
	// String responseString) {
	// try {
	//
	// String content = new String(EntityUtils
	// .toByteArray(new StringEntity(
	// responseString)));
	// Map<String, String> xml = WxPay
	// .decodeXml(content);
	// wxorderid = xml.get("prepay_id");// 保留订单id
	// Log.e("CL", "wxorderid: "+wxorderid);
	// // Wxpay();
	//
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onFailure(int statusCode, Header[] headers,
	// String responseString, Throwable throwable) {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// } catch (UnsupportedEncodingException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// }

	// 设置和显示返回窗 ???
	private void showPopupWindow() {
		TextView tv_back = (TextView) view.findViewById(FindResHelper.RId("tv_back"));
		ImageView iv_close = (ImageView) view.findViewById(FindResHelper.RId("img_btn_close"));
		iv_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				popupWindow.dismiss();
			}
		});
		tv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				// ShowToast(FindResHelper.RStringStr("hj_toast_paypaysuccess"));
				HyPayMainActivity.this.finish();
			}
		});
		TextView tv_notback = (TextView) view.findViewById(FindResHelper.RId("tv_notback"));
		tv_notback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setFocusable(true);
		// popupWindow.setOutsideTouchable(true);
		// popupWindow.setBackgroundDrawable(getResources().getDrawable(
		// FtnnRes.RDrawable("hj_pay_backshape")));
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.3f;
		getWindow().setAttributes(lp);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1f;
				getWindow().setAttributes(lp);
			}
		});
		popupWindow.setAnimationStyle(android.R.style.Animation_Translucent);

		// 显示PopupWindow ???3个方 ???
		// popupWindow.showAsDropDown(view);
		// popupWindow.showAsDropDown(anchor, xoff, yoff)
		popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		//  ???要注意的是以上三个方法必须在触发事件中使用，比如在点击某个按钮的时 ??

	}

	/**
	 * 支付下单 1 支付 ??? 2微信
	 * 
	 * @param type
	 */
	private void doOrdPay(final int type) {
		// String payway = "weixin";//weixinwap
		String payway = "weixin";

		if (type == 1) {
			wxorderid = "Alipay";
			payway = "alipay";
		}
		PostUserInfo.CLCreateOrder(HyPayMainActivity.this,hyPayInfo , payway,
				new HttpCallback() {
					@Override
					public void onStart() {
						progressDialog.show();
						super.onStart();
					}

					@Override
					public void onSuccess(String responseString) {
						progressDialog.dismiss();

						try {
							if (!TextUtils.isEmpty(responseString)) {
								JSONObject jsonObject;
								jsonObject = new JSONObject(responseString);
								String ret = JsonParse.HJJsonGetRet(jsonObject);
								String msg = JsonParse.HJJsonGetMsg(jsonObject);
								String data = JsonParse.HJJsonGetData(jsonObject);
								JSONObject jsonObject2 = new JSONObject(data);
								String pay_way_str = jsonObject2.getString("pay_way_str");
//								pay_way_str = pay_way_str.replace("\\", "");
								FLogger.d(pay_way_str);

								if (ret.equals("1")) {
//									mHjPayResult.setOutRradeNo(new JSONObject(data).optString("order_number"));
									// String notify_url=new JSONObject(data)
									// .optString("notify_url");
									switch (type) {
									case 1:
										WxWebPay(pay_way_str);
//										AliPay aliPay = new AliPay();
//										aliPay.StartAliPay(HyPayMainActivity.this, pay_way_str, new AliPayCallBack() {
//
//											@Override
//											public void onFinish(int ret) {
//												if (ret == 0) {
//													mhandler.sendEmptyMessage(mCode_alipay);
//												}
//												// if (ret == 1) {
//												// ShowToast(FindResHelper
//												// .RStringStr("hj_toast_paypayfail"));
//												// HJControlCenter
//												// .onPayFinished(1,mHjPayResult);
//												// HyPayMainActivity.this
//												// .finish();
//												// }
//											}
//										});
										break;
									case 2:
										// 微信app支付
										// Wxpay(paywaystr);
										// //微信wap支付
										// Intent intent=new
										// Intent(HyPayMainActivity.this,CLWapPayActivity.class);
										// intent.putExtra("url", paywaystr);
										// startActivityForResult(intent, 1);
										WxWebPay(pay_way_str);
										break;
									default:
										break;
									}
								} else {
									HySDK.HyToast(msg);
								}

							} else {
								HySDK.HyToast(FindResHelper.RStringStr("hj_toast_paychecknetwork"));
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				});
	}

	private boolean isPay;

	/**
	 * 微信网页支付
	 * 
	 * @param paywaystr
	 */
	@SuppressLint("SetJavaScriptEnabled")
	private void WxWebPay(final String paywaystr) {
		isPay = true;

		WebViewClient webViewClient =new WebViewClient() {
//			 @Override  
//
//		        public boolean shouldOverrideUrlLoading(WebView view, String url) {  
//
//		            // 如下方案可在非微信内部WebView的H5页面中调出微信支付
//
//		            if (url.startsWith("weixin://wap/pay?")) {
//
//		                Intent intent = new Intent();
//
//		                intent.setAction(Intent.ACTION_VIEW);
//
//		                intent.setData(Uri.parse(url));
//
//		                startActivity(intent);
//
//
//
//		                return true;
//
//		            }
//
//		            return super.shouldOverrideUrlLoading(view, url);
//
//		        }
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				// Log.e("CL", url);
				FLogger.d(url);
//				if (!url.startsWith("weixin")) {
//					url=paywaystr;
//					
//				}
//				
//				FLogger.d(url);
//				if (url.contains("successpay")) {
//					setResult(1);
//					HyPayMainActivity.this.finish();
//				}
//				if (url.startsWith("http:") || url.startsWith("https:")) {
//					return false;
//				}
//				// Otherwise allow the OS to handle things like tel, mailto,
//				// etc.
//				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//				startActivity(intent);
//
//				// view.loadUrl(url);
//				return true;

				// view.loadUrl(url);
				 if (url.startsWith("weixin://wap/pay?")||url.startsWith("alipays://")) {
					 try {
						 Intent intent = new Intent();
		                    intent.setAction(Intent.ACTION_VIEW);
		                    intent.setData(Uri.parse(url));
		                    startActivity(intent);
		            
					} catch (Exception e) {
						Toast.makeText(context, "请安装支付软件后重试", Toast.LENGTH_SHORT).show();
					}
					 finish(); 
	                    return true;
	                } 
//				 if (url.contains("platformapi/startApp")) {
//	                    startAlipayActivity(url);
//	                    // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
//	                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
//	                        && (url.contains("platformapi") && url.contains("startapp"))) {
//	                    startAlipayActivity(url);
//	                }
	                else {
	                	return false;
//	                    Map<String, String> extraHeaders = new HashMap<String, String>();
//	                    extraHeaders.put("Referer", "http://api-sdk.huayang.fun/v1/");
//	                    extraHeaders.put("User-Agent", "Android");
//	                    view.loadUrl(url);
	                }

//				return true;
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// Log.e("CL", error.getPrimaryError()+" error");
				handler.proceed();
			}
		};
//		webView.setWebChromeClient(new WebChromeClient() {
//			public void onProgressChanged(WebView view, int newProgress) {
//				if (isPay) {
//					progressDialog.show();
////					CLLog.Log(""+newProgress);
//					if (newProgress == 100) {
//						progressDialog.dismiss();
//					}
//				}
//
//			};
//		});
		
		
//		WebSettings webSettings = (webView).getSettings();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
//		}
//		// web.requestFocus();
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		webSettings.setUseWideViewPort(true);// 关键 ???
//		webSettings.setDomStorageEnabled(true);
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//
//		webSettings.setDisplayZoomControls(false);
//		webSettings.setAllowFileAccess(true); // 允许访问文件
//		webSettings.setBuiltInZoomControls(true); // 设置显示缩放按钮
//		webSettings.setSupportZoom(true); // 支持缩放
//		webSettings.setLoadWithOverviewMode(true);
		 webView.setWebChromeClient(new WebChromeClient());
		WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

//		webView.getSettings().setDefaultTextEncodingName("gb2312");
        Map<String, String> map = new HashMap<String, String>();
        map.put("Referer", "http://api-sdk.huayang.fun/v1/payment/test?id=12");//http://api-sdk.huayang.fun/v1/payment/test?id=12//"api-sdk.huayang.fun");
		map.put("User-Agent", "Android");
		webView.setWebViewClient(webViewClient);
		//
		webView.loadUrl(paywaystr, map);
	}

	@Override
	public void onBackPressed() {
		if (!this.popupWindow.isShowing()) {
			showPopupWindow();
		}
	}


	/**
	 * 支付成功后调用充值记录handler
	 */
	private static Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case mCode_wechat:
				mPayFinish("WeChat");
				break;
			case mCode_alipay:
				mPayFinish("AliPay");
				break;
			case mCode_wechatwap:
				mPayFinish("WeChatWap");
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 支付成功后调用充值记 ???
	 */
	private static void mPayFinish(String payType) {
//		mHjPayResult.setPayType(payType);
//		CLControlCenter.onPayFinished(0, mHjPayResult);
//		// 记录充 ? 数 ???
//		PostUserInfo.HJpayment(context, mHjPayResult.getSid(), mHjPayResult.getUid(), mHjPayResult.getPayType(),
//				mHjPayResult.getTotalFee() + "", mHjPayResult.getTotalFee() + "", "CNY", mHjPayResult.getOutRradeNo());
//		HySDK.HyToast(FindResHelper.RStringStr("hj_toast_paypaysuccess"));
//		context.finish();
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // TODO Auto-generated method stub
	// if (requestCode == 1) {
	// if (resultCode == 1) {
	// ShowToast(FindResHelper.RStringStr("hj_toast_paypaysuccess"));
	// CLControlCenter.onPayFinished(0, mHjPayResult);
	// HyPayMainActivity.this.finish();
	// }
	// }
	//
	// super.onActivityResult(requestCode, resultCode, data);
	// }
	// 调起支付宝并跳转到指定页面
	private void startAlipayActivity(String url) {
	    Intent intent;
	    try {
	        intent = Intent.parseUri(url,
	                Intent.URI_INTENT_SCHEME);
	        intent.addCategory(Intent.CATEGORY_BROWSABLE);
	        intent.setComponent(null);
	        startActivity(intent);
	        finish();
	    } catch (Exception e) {
	    	Toast.makeText(context, "请安装支付宝", Toast.LENGTH_SHORT).show();;
	        e.printStackTrace();
	    }
	}

}
