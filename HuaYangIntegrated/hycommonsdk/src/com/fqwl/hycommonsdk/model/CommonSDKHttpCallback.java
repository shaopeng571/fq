package com.fqwl.hycommonsdk.model;

import com.fqwl.hycommonsdk.bean.ResultInfo;

public  interface CommonSDKHttpCallback {
//		// ʼ
//		public void onStart() ;
//
//		// ɹ ص
//		public void onSuccess(String data);
//
//		// ʧ ܻص
//		public void onError(String msg) ;

		public void onResult(ResultInfo resultInfo, String msg) ;
	}