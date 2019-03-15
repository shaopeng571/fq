package com.huayang.utils;


public class ExceptionUtils {

	private static final String TAG_PACKAGENAME = "cn.gowan";
	private static final int ADD_COUNT = 2;
	
	/***
	 * 
	 * @param t 异常对象
	 * @return  异常栈信息字符串
	 */
	public static String getStackTrace(Throwable t) {
		if (null == t) {
			return "Exception Is Null";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(t.getClass().getName());
		sb.append(" : ");
		sb.append(t.getMessage());
		sb.append("\r\n");
		StackTraceElement[] messages = t.getStackTrace();

		int length = messages.length;
		for (int i = 0; i < length; i++) {
			sb.append(String.valueOf(i));
			sb.append(" ");
			sb.append(messages[i].getClassName());
			sb.append(" - line ");
			sb.append(messages[i].getLineNumber());
			sb.append("\r\n");
		}
		return sb.toString();
	}
	/**
	 *  在异常栈信息中根据包名关键字找出相关异常 最多两条
	 * @param t 
	 * @param pkg  包名关键字
	 * @return
	 */
	public static String getStackTracePackage(Throwable t, String pkg) {
		if (null == t) {
			return "Exception Is Null";
		}
		StringBuilder sb = new StringBuilder();
		sb.append(t.getMessage());
		sb.append("\r\n");
		StackTraceElement[] messages = t.getStackTrace();
														
		int length = messages.length;
		int addcount = 0;
		String tag = pkg;
		
		if (tag == null || tag.trim() == "") {
			tag = TAG_PACKAGENAME;
		}
		
		for (int i = 0; i < length; i++) {
			if ((null != messages[i].getClassName() && "" != messages[i].getClassName())
					&& messages[i].getClassName().contains(tag)) {
				sb.append(String.valueOf(i));
				sb.append(" ");
				sb.append(messages[i].getClassName());
				sb.append(" - line ");
				sb.append(messages[i].getLineNumber());
				sb.append("\r\n");
				addcount++;
				if (addcount >= ADD_COUNT) {
					break;
				}
			}

		}
		return sb.toString();
	}

	public static void logCaller() {
		int i;
		StackTraceElement stacks[] = (new Throwable()).getStackTrace();
		for (i = stacks.length - 1; i >= 1; i--) {
			StackTraceElement ste = stacks[i];
			String format = String.format("%s  %s.%s --- %s line %s",i,
					ste.getClassName(), ste.getMethodName(), ste.getFileName(),
					ste.getLineNumber());
			FLogger.d(format);
		}
	}
}