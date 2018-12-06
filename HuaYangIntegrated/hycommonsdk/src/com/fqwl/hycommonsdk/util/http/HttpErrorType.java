package com.fqwl.hycommonsdk.util.http;

public enum HttpErrorType {

	/**
	 * 无错误
	 */
	HTTP_ERR_NONE(-1),
	
	/**
	 * 其他错误
	 */
	HTTP_ERR_OTHERS(0),
	/**
	 * 访问超时
	 */
	HTTP_ERR_TIMEOUT(1),
	/**
	 * io错误
	 */
	HTTP_ERR_IO(2),
	/**
	 * url格式错误
	 */
	HTTP_ERR_URL(3),
	
	HTTP_ERR_SOCKET(4);
	
	HttpErrorType(int c) {
		mCode = c;
	}
	
	private int mCode;
	
	public int getCode() {
		return mCode;
	}
}
