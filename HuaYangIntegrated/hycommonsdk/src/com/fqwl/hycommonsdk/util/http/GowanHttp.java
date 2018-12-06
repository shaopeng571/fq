package com.fqwl.hycommonsdk.util.http;

import android.annotation.SuppressLint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.fqwl.hycommonsdk.util.logutils.FLogger;
import com.fqwl.hycommonsdk.util.logutils.StringUtils;



/**
*@author   fan
*@version  1.0
*@time     2017年6月28日 上午10:11:31
*/
public class GowanHttp implements IHttp {
	
	public GowanHttp(String url,String para){
		this.mUrl = url;
		this.mPara = para;
		setHttpErrorType(HttpErrorType.HTTP_ERR_NONE);
	}
	
	public GowanHttp(String url){
		this(url,null);
	}
	
	public final static Charset DEFAUTL_CHARSET = Charset.forName("UTF-8");
	
	String mUrl;
	
	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void setUrl(String v) {
		mUrl = v;
	}
	
	String mPara = "";
	
	@Override
	public String getPara() {
		return mPara;
	}

	@Override
	public void setPara(String v) {
		mPara = v;
	}

	@Override
	public void access(HttpCtx ctx) {
		accessInternet(ctx);
	}

	@Override
	public void access() {
		accessInternet(HttpCtx.getDefault());
	}
	
	//连接状态
	@Override
	public boolean isNetworkError() {
		return HttpAccessStatus.Done != getHttpAccessStatus();
	}
	
	@Override
	public boolean isHttpStatusError() {
		return HttpStatus.OK != getHttpStatus();
	}
	
	//连接完成，状态码为200
	@Override
	public boolean isSucceed() {
		return !isNetworkError() && !isHttpStatusError();
	}
	
	HttpAccessStatus mHttpAccessStatus;
	
	@Override
	public HttpAccessStatus getHttpAccessStatus() {
		return mHttpAccessStatus;
	}

	@Override
	public void setHttpAccessStatus(HttpAccessStatus v) {
		this.mHttpAccessStatus = v;
	}
	
	long mElapsedTime;
	@Override
	public long getElapsedTime() {
		return mElapsedTime;
	}

	@Override
	public void setElapsedTime(long v) {
		mElapsedTime = v;
	}
	
	HttpStatus mHttpStatus;
	
	
	@Override
	public HttpStatus getHttpStatus() {
		return mHttpStatus;
	}
	
	@Override
	public void setHttpStatus(HttpStatus v) {
		mHttpStatus = v;
	}
	
	byte[] mResult;
	
	@Override
	public byte[] getResult() {
		return mResult;
	}

	@Override
	public void setResult(byte[] v) {
		mResult = v;
	}

	@SuppressLint("NewApi")
	@Override
	public String getHtml(Charset charset) {
		if(null == getResult()){
			return null;
		}
		return new String(getResult(),charset);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("url [");
		sb.append(getUrl());
		sb.append("]\r\n");

		sb.append("para [");
		sb.append(getPara());
		sb.append("]\r\n");

		sb.append("HttpAccessStatus [");
		sb.append(getHttpAccessStatus());
		sb.append("]\r\n");

		sb.append("ElapsedTime [");
		sb.append(String.valueOf(getElapsedTime()));
		sb.append("ms]\r\n");

		sb.append("HttpStatus [");
		sb.append(getHttpStatus());
		sb.append("]\r\n");

		sb.append("Html [");
		sb.append(getHtml());
		sb.append("]\r\n");

		sb.append("HttpAccessErrorMsg [");
		sb.append((null == getHttpAccessErrMsg() ? "null" : new String(
				getHttpAccessErrMsg())));
		sb.append("]\r\n");
		return sb.toString();
	}
	
	@Override
	public String getHtml() {
		return getHtml(DEFAUTL_CHARSET);
	}

	@Override
	public String getErrorStr() {
		
		if (isSucceed()) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Url [");
		sb.append(getUrl());
		sb.append("] HttpAccessStatus [");
		if (null != getHttpAccessStatus()) {
			sb.append(getHttpAccessStatus().getCode());
			sb.append(":");
		}
		sb.append(getHttpAccessStatus());

		sb.append("] HttpStatus [");
		if (null != getHttpStatus()) {
			sb.append(getHttpStatus().getCode());
			sb.append(":");
		}
		sb.append(getHttpStatus());
		sb.append("] HttpAccessErrorMsg [");

		if (null == getHttpAccessErrMsg()) {
			sb.append("null]");
		} else {
			sb.append(new String(getHttpAccessErrMsg()));
			sb.append("]");
		}

		return sb.toString();
	}
	
	private String mErrMsg;
	
	@Override
	public void setHttpAccessErrMsg(byte[] msg) {
		mErrMsg = (null == msg) ? null : new String(msg);
	}

	@Override
	public byte[] getHttpAccessErrMsg() {
		return (null == mErrMsg) ? null : mErrMsg.getBytes();
	}
	
	
	private void accessInternet(HttpCtx ctx) {
		if(getUrl() == null){
			FLogger.e("fq","url is null,failed to accessInternet");
			return;
		}
		String charset = HttpCtx.DEFAULT_CHARSET;
		if(null != ctx){
			charset = ctx.getCharset();
		}
		
		int connectTimeout = (ctx == null)? HttpCtx.DEFAULT_TIMEOUT:ctx.getTimeout();
		int readTimeout = (ctx == null)? HttpCtx.DEFAULT_TIMEOUT:ctx.getReadTimeout();
		
		HttpMode requestMode = (ctx == null)? HttpMode.Post :ctx.getHttpMode();
		
		String urlstr = getUrl();
		
		if(getPara()!=null && requestMode == HttpMode.Get){
			
			StringBuffer replace = new StringBuffer(getPara());
			//参数如果第一个字符为& 则替换
			if(replace.charAt(0) == '&') {
				 replace = replace.replace(0, 1, "");
			}
			urlstr += "?"+replace;
		}
		long timeBeforeRequest = System.currentTimeMillis();
		
		URL url = null;
		HttpURLConnection conn = null;
		InputStream is = null;
		OutputStream os = null;
		StringBuffer sb = null;
		
		try {
			url = new URL(urlstr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setAllowUserInteraction(true);
			HttpURLConnection.setFollowRedirects(true);
			// 字符集
			if (null != charset) {
				conn.setRequestProperty("Accept-Charset", charset);
				conn.setRequestProperty("contentType", charset);
			} else {
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				conn.setRequestProperty("contentType", "UTF-8");
			}
			setHeadersToConnection(conn);
			
			// 设置超时
			conn.setConnectTimeout(connectTimeout);
			conn.setReadTimeout(readTimeout);

			// 设置请求方式
			conn.setRequestMethod(requestMode.toString());
			
			if(requestMode == HttpMode.Post){
				conn.setDoOutput(true);
				os = conn.getOutputStream();
				os.write(getPara().getBytes());
				os.flush();
			}else if(requestMode == HttpMode.Get){
				conn.setDoOutput(false);
			}
			int respCode = conn.getResponseCode();
			setHttpStatus(HttpStatus.valueOf(respCode));
			
			FLogger.d("fq",MessageFormat.format("连接 url[{0}] 的状态码:[{1}]", urlstr,respCode));
			
			if (respCode != HttpURLConnection.HTTP_OK) {
				FLogger.e("fq",MessageFormat.format("连接url【{0}】返回状态码【{1}】，请求失败！", urlstr,
						respCode));
				return;
			}
			
			int count = 0;
			byte[] buffer = new byte[1024];
			sb = new StringBuffer();
			
			is = conn.getInputStream();
			while((count = is.read(buffer)) > 0){
				sb.append(new String(buffer,0,count,charset));
			}
			
			String receivedStr = sb.toString();
			setResult(receivedStr.getBytes());
			setHttpAccessStatus(HttpAccessStatus.Done);
		} catch (MalformedURLException e) {
			String errMsg = MessageFormat.format(
					"the url [{0}] is in wrong format ", url);
			
			FLogger.Ex("fq",e);
			FLogger.e("fq",errMsg);
			setHttpAccessStatus(HttpAccessStatus.NotDone);
			setHttpStatus(HttpStatus.NotAccess);
			setHttpAccessErrMsg(errMsg.getBytes());
			setHttpErrorType(HttpErrorType.HTTP_ERR_URL);
		} catch (SocketTimeoutException e) {
			String errMsg = MessageFormat.format(
					"failed to connect to the url [{0}], the reasons are follows: {1}", 
					url, e.getMessage());
			
			FLogger.Ex("fq",e);
			FLogger.e("fq",errMsg);
			setHttpAccessStatus(HttpAccessStatus.NotDone);
			setHttpStatus(HttpStatus.NotAccess);
			setHttpAccessErrMsg(errMsg.getBytes());
			setHttpErrorType(HttpErrorType.HTTP_ERR_TIMEOUT);
		} catch (IOException e) {
			String errMsg = MessageFormat.format(
					"failed to connect to or read from the url [{0}], the reasons are follows: {1}", 
					url, e.getMessage());
			
			FLogger.Ex("fq",e);
			FLogger.e("fq",errMsg);
			setHttpAccessStatus(HttpAccessStatus.NotDone);
			setHttpStatus(HttpStatus.NotAccess);
			setHttpAccessErrMsg(errMsg.getBytes());
			setHttpErrorType(HttpErrorType.HTTP_ERR_IO);
		} catch(Exception e) {
			String errMsg = MessageFormat.format(
					"failed to connect to the url [{0}], the reasons are follows: {1}", 
					url, e.getMessage());
			FLogger.Ex("fq",e);
			FLogger.e("fq",errMsg);
			setHttpAccessStatus(HttpAccessStatus.NotDone);
			setHttpStatus(HttpStatus.NotAccess);
			setHttpAccessErrMsg(errMsg.getBytes());
			setHttpErrorType(HttpErrorType.HTTP_ERR_OTHERS);
		} finally {
			setElapsedTime(System.currentTimeMillis() - timeBeforeRequest);

			if (null != is) {
				try {
					is.close();
				} catch (IOException e) {
					FLogger.Ex("fq",e);
				}
			}

			if (null != os) {
				try {
					os.close();
				} catch (IOException e) {
					FLogger.Ex("fq",e);
				}
			}

			if (null != conn) {
				conn.disconnect();
			}
		}
	}
	
	private void setHeadersToConnection(HttpURLConnection conn) {
		if(null == getHeaderMap()) {
			return;
		}
		
		if(null == conn) {
			return;
		}
		
		for(Entry<String, String> entry : getHeaderMap().entrySet()) {
			String headerName = entry.getKey();
			String headerValue = entry.getValue();
			
			if(StringUtils.isEmpty(headerName) || StringUtils.isEmpty(headerValue)) {
				continue;
			}
			
			conn.setRequestProperty(headerName, headerValue);
		}
		
	}
	
	private Map<String, String> mHeaders;

	private Map<String, String> getHeaderMap() {
		return mHeaders;
	}
	
	
	@Override
	public void addHeader(String headerName, String headerValue) {
		if(StringUtils.isEmpty(headerName)) {
			FLogger.e("fq","header name is empty, failed to add http header.");
			return;
		}
		
		if(StringUtils.isEmpty(headerValue)) {
			FLogger.e("fq","header value is empty, failed to add http header.");
			return;
		}
		
		if(null == getHeaderMap()) {
			mHeaders = new HashMap<String, String>();
		}
		
		getHeaderMap().put(headerName, headerValue);

	}

	@Override
	public String getHeader(String headerName) {
		if(StringUtils.isEmpty(headerName)) {
			FLogger.e("fq","header name is empty, failed to get http header.");
			return null;
		}
		
		if(null == getHeaderMap()) {
			FLogger.e("fq","no http header found, null returned.");
			return null;
		}
		
		return getHeaderMap().get(headerName);
	}
	
	private HttpErrorType mHttpErrType;
	
	@Override
	public void setHttpErrorType(HttpErrorType type) {
		mHttpErrType = type;

	}

	@Override
	public HttpErrorType getHttpErrorType() {
		return mHttpErrType;
	}

}
