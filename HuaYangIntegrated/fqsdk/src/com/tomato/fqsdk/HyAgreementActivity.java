package com.tomato.fqsdk;

import com.tomato.fqsdk.base.BaseActivity;
import com.tomato.fqsdk.utils.CLNaviteHelper;
import com.tomato.fqsdk.utils.FindResHelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class HyAgreementActivity extends BaseActivity {

	// TextView txtContent;
	ImageView imgClose;
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(FindResHelper.RLayout("hj_user_agreement"));

		web = (WebView) findViewById(FindResHelper.RId("txt_agreement"));
		setWebView();
		imgClose = (ImageView) findViewById(FindResHelper.RId("img_btn_close"));

		imgClose.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	@SuppressLint("SetJavaScriptEnabled") 
	private void setWebView() {
		WebSettings webSettings = web.getSettings();
//		webSettings.setLoadWithOverviewMode(true);
//		webSettings.setUseWideViewPort(true);
//		webSettings.setTextSize(TextSize.NORMAL);
		web.setWebViewClient(new WebViewClient());
//		web.requestFocus();
//		webSettings.setSupportZoom(false);
//		webSettings.setDisplayZoomControls(false);
		webSettings.setJavaScriptEnabled(true);
//		Locale locale = getResources().getConfiguration().locale; 
//		String language = locale.getLanguage();
		web.loadUrl("file:///android_asset/html/hj_useragreement.html");
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		
	}
}