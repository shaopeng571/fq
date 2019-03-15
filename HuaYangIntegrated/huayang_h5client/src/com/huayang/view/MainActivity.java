package com.huayang.view;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fqwl.hycommonsdk.model.CommonSdkCallBack;
import com.fqwl.hycommonsdk.model.CommonSdkInitInfo;
import com.fqwl.hycommonsdk.present.HySDKManager;
import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.huayang.utils.GsonUtil;
import com.huayang.utils.InitDataUtils;
import com.huayang.utils.PhoneInfo;
import com.huayang.utils.ResUtils;
import com.huayang.utils.UIUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
	private String uid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (getActionBar() != null) {
			getActionBar().hide();
		}
		activity = this;
		FLogger.init(true, "huayang");
		ResUtils.init(this);
		initView();
		initWebView();
//		String result=InitDataUtils.getInitData(activity);
//		if (TextUtils.isEmpty(result)) {
//			NetWork.HJactivate(activity);
//			NetWork.HJinstall(activity);
//		}
//		if (!SpUtils.getBooleanValue(activity, "install")) {
//			NetWork.HJinstall(activity);
//		}

		// 将背景图与状态栏融合
		if (Build.VERSION.SDK_INT >= 21) {

			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			getWindow().setStatusBarColor(Color.parseColor("#18b1ea"));
			fitsSystemWindows(this);
		}
		CommonSdkInitInfo initInfo = new CommonSdkInitInfo();
		initInfo.setGameVersion("v1.0");
		initInfo.setDebug(true);
		HySDKManager.getInstance().initCommonSdk(MainActivity.this, initInfo, sdkcallback);

	}

	/**
	 * 设置页面最外层布局 FitsSystemWindows 属性
	 *
	 * @param activity
	 */
	private void fitsSystemWindows(Activity activity) {
		ViewGroup contentFrameLayout = (ViewGroup) activity.findViewById(android.R.id.content);
		View parentView = contentFrameLayout.getChildAt(0);
		if (parentView != null && Build.VERSION.SDK_INT >= 14) {
			// 布局预留状态栏高度的 padding
			parentView.setFitsSystemWindows(true);
			if (parentView instanceof DrawerLayout) {
				DrawerLayout drawer = (DrawerLayout) parentView;
				// 将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
				drawer.setClipToPadding(false);
			}
		}
	}

	private void initView() {
		rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
		LinearLayout.LayoutParams progressParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				5);
		Drawable drawable = ResUtils.getInstance().getDrawable("go_web_progress");
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
				FLogger.d(TAG, "准备加载:" + url);
				return false;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				FLogger.d(TAG, "开始加载" + url);
				progressBar.setVisibility(View.VISIBLE);
				progressBar.setProgress(0);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				FLogger.d(TAG, "结束加载:" + url);
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
				FLogger.d(TAG, "进度.." + newProgress);
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});

		FLogger.v(TAG, "-------------");
		webView.clearCache(true); // 清除资源缓存。请注意，缓存是每个应用程序的，所以这将清除所有使用的WebViews的缓存。false的话，只会清除RAM上的缓存。
		webView.clearHistory(); // 清除历史记录
		webView.clearFormData(); // //从当前关注的表单字段中移除自动填充弹出窗口
		webView.clearSslPreferences(); // 清除存储的SSL首选项表
		webView.loadUrl(url);

	}

	long exitTime = 0;

	@Override
	public void onBackPressed() {
		FLogger.d(TAG, "按了返回建");
//		if (webView.canGoBack()) {
//			webView.goBack();
//		} else {
		long currentTimeMillis = System.currentTimeMillis();
		FLogger.d(TAG, "currentTimeMillis=" + currentTimeMillis + " exitTime=" + exitTime);
		if ((currentTimeMillis - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			exitTime = currentTimeMillis;
		} else {
			finish();
		}
//		}
	}

	private class MyJs2An {
		@JavascriptInterface
		public void init(String orderInfo) {
			// TODO Auto-generated method stub
			FLogger.d(TAG, "js调用了init:"+orderInfo);
			webView.post(new Runnable() {
				@Override
				public void run() {
				webView.loadUrl("javascript:onInitCallback()");}
				});

			
		}
		

		@JavascriptInterface
		public void login(String orderInfo) {
			// TODO Auto-generated method stub
			FLogger.d(TAG, "js调用了login:" + orderInfo);
			HySDKManager.getInstance().showLoginView(MainActivity.this, null);
		}

		@JavascriptInterface
		public void doPay(String orderInfo) {
			// TODO Auto-generated method stub
			FLogger.d(TAG, "js调用了doPay:" + orderInfo);
		}

		@JavascriptInterface
		public void doEnterGameInfo(String gameInfo) {
			// TODO Auto-generated method stub
			FLogger.d(TAG, "js调用了doEnterGameInfo:" + gameInfo);
		}

		@JavascriptInterface
		public void doCreateRoleInfo(String gameInfo) {
			// TODO Auto-generated method stub
			FLogger.d(TAG, "js调用了doCreateRoleInfo:" + gameInfo);
		}

		@JavascriptInterface
		public void doLevelUpInfo(String gameInfo) {
			// TODO Auto-generated method stub
			FLogger.d(TAG, "js调用了doLevelUpInfo:" + gameInfo);
		}



		@JavascriptInterface
		public void toast(String msg) {
			UIUtil.toastShortOnMain(activity, msg);
		}

		@JavascriptInterface
		public void finish() {
			FLogger.d(TAG, "js调用了关闭");
			activity.finish();
		}

		@JavascriptInterface
		public void androidlog(String msg) {
			FLogger.i(TAG, "js info :" + msg);
		}
	}

	@Override
	protected void onDestroy() {
		FLogger.d(TAG, "onDestroy");
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

	private CommonSdkCallBack sdkcallback = new CommonSdkCallBack() {

		@Override
		public void realNameOnFinish(int arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * 注销接口回调
		 */
		@Override
		public void logoutOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 注销成功

			}
		}

		@Override
		public void loginOnFinish(final String arg0, int arg1) {
			// TODO Auto-generated method stub
//			Log.e("fq", "登录成功！！！"+arg0);
			switch (arg1) {
			case 0:// 登录成功
				
					webView.post(new Runnable() {
						@Override
						public void run() {
							try {
								final JSONObject object = new JSONObject(arg0);
								object.put("code", 1);
								String uid = object.getString("userId");
								Log.d("fq", object.toString());
								int chanlid = object.getInt("platformChanleId");
								Toast.makeText(MainActivity.this, "登录成功！", 0).show();
							webView.loadUrl("javascript:onLoginCallback('" + object.toString() + "')");
							if (TextUtils.isEmpty(uid)) {
								uid = HySDKManager.getInstance().getCurrentUserId();
								if (TextUtils.isEmpty(uid)) {
									// 重新显示登录页面
									HySDKManager.getInstance().showLoginView(MainActivity.this, null);
								}
							}
							MainActivity.this.uid = uid;
							// sendExtData();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						}
					});
					
				break;
			case 2:// sdk登录页面退出 （不是所有渠道都有该状态）
				break;
			default:
				break;
			}
		}

		@Override
		public void initOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			System.out.println(arg0);
			Log.d("fq", "initOnFinish:" + arg0 + " code:" + arg1);
			if (arg1 == 0) {
				// 初始化成功 继续游戏
			} else if (arg1 == 1) {
				// 初始化成功 同时用户自动登录成功，不要再次调用登录接口
			} else {
				// 初始化失败 建议在有网络的情况下再次调用，失败还是失败，建议重启游戏或者退出游戏
			}
		}

		/**
		 *   该接口已经sdk处理，游戏方不用处理
		 */
		@Override
		public void getAdultOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub

		}

		/**
		 * 退出页面回调  
		 */
		@Override
		public void exitViewOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 用户推出游戏
				// 这里结束游戏
				MainActivity.this.finish();
			} else {
				// 用户继续游戏
			}
		}

		/**
		 * 充值回调 不是所有平台都有改回调，充值是否成功，以服务器收到通知为准  
		 */
		@Override
		public void chargeOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			if (arg1 == 0) {// 充值流程完成

			} else {
				// 充值流程未完成
			}
		}

		/**
		 * 浮动图标切换账号，修改密码回调 回调登陆页面，让用户重新登陆
		 */
		@Override
		public void ReloginOnFinish(String arg0, int arg1) {
			// TODO Auto-generated method stub
			switch (arg1) {
			case 0:
			case 3:
				// 收到回调，立即退出游戏回到登陆页面，让用户重新调用sdk登陆页面
				Log.d("fq", "收到回调，立即退出游戏回到登陆页面，让用户重新调用sdk登陆页面");
				break;
			case 1:
				// 用户已经处于注销状态,sdk登录界面已经打开
				break;
			case 4:
				// 用户已经处于登录状态,sdk登录界面关闭，用户信息返回(不要再次调用登录)
				break;
			default:
				break;
			}
		}

	};
}
