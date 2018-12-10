//package com.tomato.fqsdk;
////package com.mgsdk.huanjia;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.net.http.SslError;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.webkit.JsResult;
//import android.webkit.SslErrorHandler;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebSettings.LayoutAlgorithm;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.Button;
//import android.widget.ImageView;
//import com.tomato.fqsdk.R;
//
//@SuppressLint("NewApi") public class CLWapPayActivity extends Activity {
//	WebView webview;
//	String url;
//	ImageView button_close;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.cl_weixinwap);
////		url = getIntent().getStringExtra("url");
//		url = "http://h5.gowanme.com/?yisdk_param=mZxhetruwtw%3D&gowan_param=aZ9haqKt";
//		webview = ((WebView) findViewById(R.id.webView1));
//		button_close = (ImageView) findViewById(R.id.img_btn_close);
//		button_close.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				setResult(1);
//				CLWapPayActivity.this.finish();
//			}
//		});
//		webview.setWebViewClient(new WebViewClient() {
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				// TODO Auto-generated method stub
//				// Log.e("CL", url);
//				if (url.contains("successpay")) {
//					setResult(1);
//					CLWapPayActivity.this.finish();
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
//			}
//
//			@Override
//			public void onReceivedSslError(WebView view,
//					SslErrorHandler handler, SslError error) {
//				// Log.e("CL", error.getPrimaryError()+"         error");
//				handler.proceed();
//			}
//		});
//		webview.setWebChromeClient(new WebChromeClient() {
//			@Override
//			public boolean onJsAlert(WebView view, String url, String message,
//					JsResult result) {
//				// TODO Auto-generated method stub
//				return super.onJsAlert(view, url, message, result);
//			}
//
//		});
//		WebSettings webSettings = (webview).getSettings();
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//			webview.getSettings().setMixedContentMode(
//					WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
//		}
//		Map<String, String> map = new HashMap<String, String>();
//		map.put("User-Agent", "Android");
//		// web.requestFocus();
//		webSettings.setJavaScriptEnabled(true);
//		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		webSettings.setUseWideViewPort(true);//  ؼ   
//		webSettings.setDomStorageEnabled(true);
//		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//
//		webSettings.setDisplayZoomControls(false);
//		webSettings.setAllowFileAccess(true); //         ļ 
//		webSettings.setBuiltInZoomControls(true); //       ʾ   Ű ť
//		webSettings.setSupportZoom(true); // ֧      
//		webSettings.setLoadWithOverviewMode(true);
//		webview.getSettings().setDefaultTextEncodingName("gb2312");
//		webview.loadUrl(url, map);
//	}
//
//}
