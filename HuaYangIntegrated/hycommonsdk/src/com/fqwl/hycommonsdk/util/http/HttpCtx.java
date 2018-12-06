package com.fqwl.hycommonsdk.util.http;

import java.text.MessageFormat;
public class HttpCtx {
	/**
	 * 默认超时时间，20s
	 */
	public static final int DEFAULT_TIMEOUT = 20 * 1000;
	
	/**
	 * 
	 * @param connectTimeout 连接超时
	 * @param readTimeout 读超时
	 * @param agent
	 * @param mode
	 */
	public HttpCtx(int connectTimeout, int readTimeout, String agent, HttpMode mode) {
		mTimeout = connectTimeout;
		mReadTimeout = readTimeout;
		mAgent = agent;
		mHttpMode = mode;
	}
	/**
	 * 
	 * @param timeout connectTimeout 连接超时
	 * @param agent
	 * @param mode get post
	 */
	public HttpCtx(int timeout, String agent, HttpMode mode) {
		this(timeout, DEFAULT_TIMEOUT, agent, mode);
	}

	/**
	 * 
	 * @param timeout 连接超时
	 * @param agent
	 */
	public HttpCtx(int timeout, String agent) {
		this(timeout, agent, HttpMode.Get);
	}

	public HttpCtx() {
		this(DEFAULT_TIMEOUT, null, HttpMode.Get);
	}

	/** 连接超时 */
	int mTimeout;

	/**
	 * 获得连接超时时间，以豪秒为单位，最小为100，小于100等同于无超时
	 * @return
	 */
	public int getTimeout() {
		return mTimeout;
	}

	/**
	 * 设置连接超时时间
	 * @param v
	 */
	public void setTimeout(int v) {
		mTimeout = v;
	}
	
	/** 读取超时 */
	int mReadTimeout;
	
	public int getReadTimeout() {
		return mReadTimeout;
	}
	
	public void setReadTimeout(int value) {
		mReadTimeout = value;
	}

	String mAgent;

	public String getAgent() {
		return mAgent;
	}

	public void setAgent(String v) {
		mAgent = v;
	}

	HttpMode mHttpMode;

	public HttpMode getHttpMode() {
		return mHttpMode;
	}

	public void setHttpMode(HttpMode v) {
		mHttpMode = v;
	}
	
	String mCharset = DEFAULT_CHARSET;
	
	public static final String DEFAULT_CHARSET = "UTF-8";
	
	public String getCharset() {
		return mCharset;
	}
	
	public void setCharset(String charset) {
		mCharset = charset;
	}

	private static HttpCtx _Default;

	public static HttpCtx getDefault() {
		if (null == _Default) {
			synchronized (HttpCtx.class) {
				if (null == _Default) {
					_Default = new HttpCtx();
				}
			}
		}
		return _Default;
	}
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(MessageFormat.format("connect timeout: {0}", getTimeout()));
		
		buffer.append("\n");
		buffer.append(MessageFormat.format("read timeout: {0}", getReadTimeout()));
		
		buffer.append("\n");
		buffer.append(MessageFormat.format("agent: {0}", getAgent()));
		
		buffer.append("\n");
		buffer.append(MessageFormat.format("http mode: {0}", getHttpMode()));
		
		buffer.append("\n");
		buffer.append(MessageFormat.format("charset: {0}", getCharset()));
		
		return buffer.toString();
	}
	
	
}
