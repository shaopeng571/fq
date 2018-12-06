package com.fqwl.hycommonsdk.util.http;

import java.nio.charset.Charset;

/**
*@author   fan
*@version  1.0
*@time     2017年6月28日 上午10:05:21
*/
public interface IHttp {
	
	/*request para*/
	String getUrl();

	void setUrl(String v);

	String getPara();

	void setPara(String v);

	/*提交*/
	void access(HttpCtx ctx);

	void access();
	
	/*response para*/

	boolean isNetworkError();

	boolean isHttpStatusError();

	boolean isSucceed();
	
	/*
	 * http访问完成情况
	 */
	HttpAccessStatus getHttpAccessStatus();

	void setHttpAccessStatus(HttpAccessStatus v);

	/*
	 * http消耗的时间，毫秒数
	 */
	long getElapsedTime();

	void setElapsedTime(long v);
	
	HttpStatus getHttpStatus();

	void setHttpStatus(HttpStatus v);

	byte[] getResult();

	void setResult(byte[] v);

	String getHtml(Charset charset);

	String getHtml();

	String getErrorStr();
	
	/**
	 * 设置http请求过程中，由底层返回的错误信息
	 */
	void setHttpAccessErrMsg(byte[] msg);
	
	/**
	 * 获取http请求过程中，底层返回的错误信息
	 */
	byte[] getHttpAccessErrMsg();
	
	/**
	 * 设置头信息
	 * @param headerName 头信息名称
	 * @param headerValue 头信息值
	 */
	public void addHeader(String headerName, String headerValue);
	
	/**
	 * 获取头信息
	 * @param headerName 头信息名称
	 * @return 可能为null
	 */
	public String getHeader(String headerName);
	
	/**
	 * 设置http错误类型
	 */
	public void setHttpErrorType(HttpErrorType type);
	
	/**
	 * 获取http错误类型
	 * @return 不为null
	 */
	public HttpErrorType getHttpErrorType();
	

}
