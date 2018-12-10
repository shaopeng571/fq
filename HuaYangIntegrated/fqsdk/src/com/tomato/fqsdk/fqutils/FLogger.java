package com.tomato.fqsdk.fqutils;

import java.text.MessageFormat;

import android.text.TextUtils;
import android.util.Log;

/**
 * @author fan
 * @version 1.0
 * @time 2017年6月29日 下午2:15:06
 * Log打印类，通过init开关log打印。如果不想要关闭则直接调用系统的Log类
 */
public class FLogger {
	
	// 私有化，只调用静态方法
	private FLogger() {
	}
	
	// 栈信息第5行为调用FLogger.d等的地方
	private static final int STACK_TRACE_INDEX_5 = 5;
	private static final String SUFFIX = ".java";
	
	// 是否允许显示Log
	private static boolean SHOW_LOG_FLAG = false;
	// 是否有设置全局tag
	private static boolean mIsGlobalTagEmpty = true;
	// 全局tag
	private static String mGlobalTag;
	
	
	// Log等级
	private static final int V = 0x1;
	private static final int D = 0x2;
	private static final int I = 0x3;
	private static final int W = 0x4;
	private static final int E = 0x5;
	private static final int WTF = 0x6;
	
	/** 换行符 */
	private static final String LINE_SEPARATOR = System.getProperty("line.separator");
	
	/**异常打印顶行和底行*/
	private static final String START_STR = "╔════════════════════════════════════════════════════════════"+LINE_SEPARATOR;
	private static final String END_STR =   "╚════════════════════════════════════════════════════════════"+LINE_SEPARATOR;
	
	
	/**
	 * 是否允许显示log
	 * @param isShowLog
	 */
	public static void init(boolean isShowLog) {
		SHOW_LOG_FLAG = isShowLog;
	}

	public static void init(boolean isShowLog, String tag) {
		
		SHOW_LOG_FLAG = isShowLog;
		mGlobalTag = tag;
		mIsGlobalTagEmpty = TextUtils.isEmpty(mGlobalTag);
	}

	public static void v(String msg) {
		printLog(V, null, msg);
	}

	public static void v(String tag, String msg) {
		printLog(V, tag, msg);
	}

	public static void d(String msg) {
		printLog(D, null, msg);

	}

	public static void d(String tag, String msg) {
		printLog(D, tag, msg);
	}

	public static void i(String msg) {
		printLog(I, null, msg);

	}

	public static void i(String tag, String msg) {
		printLog(I, tag, msg);
	}

	public static void w(String msg) {
		printLog(W, null, msg);

	}

	public static void w(String tag, String msg) {
		printLog(W, tag, msg);
	}

	public static void e(String msg) {
		printLog(E, null, msg);
	}

	public static void e(String tag, String msg) {
		printLog(E, tag, msg);
	}

	public static void Ex(Throwable e) {
		if (e == null) {
			return;
		}
		printEx(null, MessageFormat.format("{0}", ExceptionUtils.getStackTrace(e)));
	}

	public static void Ex(String tag, Throwable e) {
		printEx(tag, MessageFormat.format("{0}", ExceptionUtils.getStackTrace(e)));
	}

	public static void Ex(String tag, Throwable e, String logMessage) {
		String attach = "";
		if (null != logMessage && !logMessage.isEmpty()) {
			attach = MessageFormat.format("\r\nOtherInfo:{0}", logMessage);
		}
		printEx(null, MessageFormat.format("{0}{1}", ExceptionUtils.getStackTrace(e) + "__" + attach));
	}

	private static void printEx(String tagName, String message) {
		if (!SHOW_LOG_FLAG) {
			 return;
		}
		String[] contents = wrapperContent(STACK_TRACE_INDEX_5, tagName, message);
		String tag = contents[0];
		String msg = contents[1];
		String headString = contents[2];
		Log.e(tag, START_STR);
		printDefault(E, tag, headString+msg);
		Log.e(tag, END_STR);
	}

	/**
	 * 
	 * @param type
	 *            log等级
	 * @param tagName
	 *            tag
	 * @param objects
	 *            需要打印的参数
	 */
	private static void printLog(int type, String tagName, String  message) {

		if (!SHOW_LOG_FLAG) {
			 return;
		}
		String[] contents = wrapperContent(STACK_TRACE_INDEX_5, tagName, message);
		String tag = contents[0];
		String msg = contents[1];
		String headString = contents[2];
		printDefault(type, tag, headString + msg);
	}

	

	private static String[] wrapperContent(int stackTraceIndex, String tagStr,String msg) {

		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
		StackTraceElement targetElement = stackTrace[stackTraceIndex];// com.example.androidtest2.MainActivity$1.onClick(MainActivity.java:26)
		String className = targetElement.getClassName();// com.example.androidtest2.MainActivity$1
		String[] classNameInfo = className.split("\\.");// [com, example,
														// androidtest2,
														// MainActivity$1]
		if (classNameInfo.length > 0) {
			className = classNameInfo[classNameInfo.length - 1] + SUFFIX;
		}
		if (className.contains("$")) {
			className = className.split("\\$")[0] + SUFFIX;
		}
		String methodName = targetElement.getMethodName();// onClick
		int lineNumber = targetElement.getLineNumber();// 26
		if (lineNumber < 0) {
			lineNumber = 0;
		}
		String tag;

		if (!TextUtils.isEmpty(tagStr)) {
			tag = tagStr;
		} else if (!mIsGlobalTagEmpty) {
			tag = mGlobalTag;
		} else {
			tag = Global.UTIL_TAG;//TODO 默认tag
		}
		String headString = "[ (" + className + ":" + lineNumber + ")#" + methodName + " ] ";
		//headString : [ (MainActivity.java:28)#onCreate ]
		return new String[] { tag, msg, headString };
	}

	private static final int MAX_LENGTH = 10000;

	private static void printDefault(int type, String tag, String msg) {

		int index = 0;
		int length = msg.length();
		int countOfSub = length / MAX_LENGTH;
		if (countOfSub > 0) {
			for (int i = 0; i < countOfSub; i++) {
				String sub = msg.substring(index, index + MAX_LENGTH);
				printSub(type, tag, sub);
				index += MAX_LENGTH;
			}
			printSub(type, tag, msg.substring(index, length));
		} else {
			printSub(type, tag, msg);
		}
	}

	private static void printSub(int type, String tag, String msg) {
		switch (type) {
		case V:
			Log.v(tag, msg);
			break;
		case D:
			Log.d(tag, msg);
			break;
		case I:
			Log.i(tag, msg);
			break;
		case W:
			Log.w(tag, msg);
			break;
		case E:
			Log.e(tag, msg);
			break;
		}
	}

}
