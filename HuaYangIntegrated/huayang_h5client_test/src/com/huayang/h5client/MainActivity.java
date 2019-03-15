package com.huayang.h5client;

import java.util.HashMap;
import java.util.Map;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	private WebView webView;
	private LinearLayout rootLayout;
	private ProgressBar progressBar;
	String url = "https://api-ah.zsgames.cn/login/htest.html";
	private String TAG = "fq";
	private MyJs2An myJs2An;
	private Activity activity;

	private String cookieStr;

	// ------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (getActionBar() != null) {
			getActionBar().hide();
		}
		activity = this;
		initView();
		initWebView();

	}


	private void initView() {
		rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				5);
		Drawable drawable = getResources().getDrawable(com.example.huayang_h5client_test.R.drawable.go_web_progress);//("go_web_progress", "id", getPackageName());
		progressBar.setProgressDrawable(drawable);
		progressBar.setMax(100);
		rootLayout.addView(progressBar, progressParams);

		webView = new WebView(getApplicationContext());
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		rootLayout.addView(webView, layoutParams);
		setContentView(rootLayout);
	}

	Map<String, String> wxExtraHeaders = new HashMap<String, String>();

	@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
	private void initWebView() {
		WebSettings settings = webView.getSettings();

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
		}
		settings.setUserAgentString("huayang");
//		// 加速
		settings.setAppCacheEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setDatabaseEnabled(true);
//		// 缩放操作
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setDisplayZoomControls(false);
//		// 支持Javascript
		settings.setJavaScriptEnabled(true);
//		// 将图片调整到适合webview的大小
		settings.setUseWideViewPort(true);
//		// 缩放至屏幕的大小
		settings.setLoadWithOverviewMode(true);
//		// 关闭webview中缓存
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// 支持通过JS打开新窗口
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		// 设置编码格式
		settings.setDefaultTextEncodingName("utf-8");
		// 设置可以访问文件
		settings.setAllowFileAccess(true);
		myJs2An = new MyJs2An();
		webView.addJavascriptInterface(myJs2An, "MyJs2An");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// chromium, enable hardware acceleration
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		} else {
			// older android version, disable hardware acceleration
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
				Log.d(TAG, "准备加载:" + url);
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				Log.d(TAG, "开始加载" + url);
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(0);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				Log.d(TAG, "结束加载:" + url);
				progressBar.setProgress(100);
				progressBar.setProgress(0);
				progressBar.setBackgroundColor(Color.parseColor("#18b1ea"));
				progressBar.setVisibility(View.GONE);
				CookieManager cookieManager = CookieManager.getInstance();
				cookieStr = cookieManager.getCookie(url); // 获取到cookie字符串值
			}

		});
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				super.onProgressChanged(view, newProgress);
				if (newProgress < 100) {
					progressBar.setProgress(newProgress);
				} else {
				}
				Log.d(TAG, "进度.." + newProgress);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		Log.v(TAG, "-------------");
		webView.clearCache(true); // 清除资源缓存。请注意，缓存是每个应用程序的，所以这将清除所有使用的WebViews的缓存。false的话，只会清除RAM上的缓存。
		webView.clearHistory(); // 清除历史记录
		webView.clearFormData(); // //从当前关注的表单字段中移除自动填充弹出窗口
		webView.clearSslPreferences(); // 清除存储的SSL首选项表
		webView.loadUrl(url);

	}

	long exitTime = 0;

	@Override
	public void onBackPressed() {
		Log.d(TAG, "按了返回建");
//		if (webView.canGoBack()) {
//			webView.goBack();
//		} else {
		long currentTimeMillis = System.currentTimeMillis();
		Log.d(TAG, "currentTimeMillis=" + currentTimeMillis + " exitTime=" + exitTime);
		if ((currentTimeMillis - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = currentTimeMillis;
		} else {
			finish();
		}
//		}
	}
/**
 * 主要修改添加方法
 *
 */
	private class MyJs2An {
		@JavascriptInterface
		public void init(String gameInfo) {
			// TODO Auto-generated method stub
			Log.d(TAG, "js调用了init:"+gameInfo);
			webView.post(new Runnable() {
				@Override
				public void run() {
				webView.loadUrl("javascript:onInitCallback()");}
				});

			
		}
		

		@JavascriptInterface
		public void login(String gameInfo) {
			// TODO Auto-generated method stub
			Log.d(TAG, "js调用了login:"+gameInfo); 
			webView.post(new Runnable() {
				@Override
				public void run() {
					webView.loadUrl("javascript:onLoginCallback(' {\"code\":1} ')");}
				});
		}

		@JavascriptInterface
		public void doPay(String orderInfo) {
			// TODO Auto-generated method stub
			Log.d(TAG, "js调用了doPay:" + orderInfo);
		}

		@JavascriptInterface
		public void doEnterGameInfo(String gameInfo) {
			// TODO Auto-generated method stub
			Log.d(TAG, "js调用了doEnterGameInfo:" + gameInfo);
		}

		@JavascriptInterface
		public void doCreateRoleInfo(String gameInfo) {
			// TODO Auto-generated method stub
			Log.d(TAG, "js调用了doCreateRoleInfo:" + gameInfo);
		}

		@JavascriptInterface
		public void doLevelUpInfo(String gameInfo) {
			// TODO Auto-generated method stub
			Log.d(TAG, "js调用了doLevelUpInfo:" + gameInfo);
		}
		@JavascriptInterface
		public void toast(String msg) {
			Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
		}

		@JavascriptInterface
		public void finish() {
			Log.d(TAG, "js调用了关闭");
			activity.finish();
		}

		@JavascriptInterface
		public void androidlog(String msg) {
			Log.i(TAG, "js info :" + msg);
		}
	}

	@Override
	protected void onDestroy() {
		Log.d(TAG, "onDestroy");
		destoryWebView();
		webView.clearCache(true); // 清除资源缓存。请注意，缓存是每个应用程序的，所以这将清除所有使用的WebViews的缓存。false的话，只会清除RAM上的缓存。
		webView.clearHistory(); // 清除历史记录
		webView.clearFormData(); // //从当前关注的表单字段中移除自动填充弹出窗口
		webView.clearSslPreferences(); // 清除存储的SSL首选项表
		super.onDestroy();
	}

	
	private void destoryWebView() {
		if (webView != null) {
			webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
			rootLayout.removeView(webView);
			webView.clearCache(true);
			webView.clearHistory();
			webView.clearFormData();
			webView.destroy();
			webView = null;
		}

	}

	

}
