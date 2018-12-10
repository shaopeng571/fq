
package com.tomato.fqsdk.fqutils;

import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

public class StringUtils {

	public static String getNewLine() {
		return "\r\n";
	}

	public static boolean isEmpty(String s, boolean needTrim) {
		if (null == s) {
			return true;
		}
		if (needTrim) {
			s = s.trim();
		}
		if ("".equals(s)) {
			return true;
		}

		return false;
	}

	public static boolean isEmpty(String s) {
		return isEmpty(s, true);
	}

	public static boolean isSame(String s1, String s2, boolean needTrim) {
		if (null == s1) {
			return null == s2;
		}
		if (null == s2) {
			return false;
		}
		if (needTrim) {
			return s1.trim().equals(s2.trim());
		} else {
			return s1.equals(s2);
		}
	}

	public static boolean isSame(String s1, String s2) {
		return isSame(s1, s2, false);
	}

	/**
	 * 字符串的模式匹配，正则表达式
	 * 
	 * @param str
	 *            被检查字符串
	 * @param pattern
	 *            正则表达 ?
	 * @return 是否匹配。true-匹配；false-不匹配或参数不合 ?
	 * @throws PatternSyntaxException
	 *             - If the expression's syntax is invalid
	 */
	public static boolean patternMatch(String str, String pattern) {
		if (null == str || null == pattern) {
			return false;
		}

		if (pattern.trim().isEmpty()) {
			return false;
		}

		Pattern p = Pattern.compile(pattern);
		Matcher matcher = p.matcher(str);

		return matcher.matches();
	}

	final static String CHARSET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789 ";

	public static String getRandomString(String charset, int minLength, int maxLength) {
		if (isEmpty(charset, false)) {
			throw new IllegalArgumentException("char set null");
		}

		if (minLength < 0) {
			throw new IllegalArgumentException("minLength < 0");
		}

		if (minLength > maxLength) {
			throw new IllegalArgumentException("minLength > maxLength");
		}

		int charsetLen = charset.length();

		Random random = new Random(System.currentTimeMillis());

		int resLen = minLength;
		if (maxLength > minLength) {
			resLen += random.nextInt(maxLength - minLength);
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < resLen; i++) {
			int pos = random.nextInt(charsetLen);
			sb.append(charset.charAt(pos));
		}
		return sb.toString();
	}

	public static String getRandomString(String charset, int length) {
		return getRandomString(charset, length, length);
	}

	public static String getRandomString(int minLength, int maxLength) {
		return getRandomString(CHARSET, minLength, minLength);
	}

	public static String getRandomString(int length) {
		return getRandomString(length, length);
	}

	public enum PaddingMode {
		Left(0), Both(1), Right(2);

		private int mCode;

		private PaddingMode(int code) {
			mCode = code;
		}

		public int getCode() {
			return mCode;
		}

		public static PaddingMode valueOf(int value) {
			switch (value) {
			case 0:
				return Left;
			case 1:
				return Both;
			case 2:
				return Right;
			default:
				return null;
			}
		}
	}

	public static String getFixLengthStr(Object src, int length, char paddingChar, PaddingMode paddingMode) {
		if (length < 0) {
			throw new IllegalArgumentException("length must >=0");
		}

		if (length == 0) {
			return "";
		}

		String s = null;
		int srcLen = 0;
		if (null != src) {
			s = String.valueOf(src);
			srcLen = s.length();
		}

		if (srcLen > length) {
			return s.substring(0, length);
		}

		if (srcLen == length) {
			return s;
		}

		int padTotal = length - srcLen;
		int padLeft = 0, padRight = 0;

		switch (paddingMode) {
		case Left:
			padLeft = padTotal;
			break;
		case Both:
			padLeft = padTotal / 2;
			padRight = padTotal - padLeft;
			break;
		case Right:
			padRight = padTotal;
			break;
		default:
			throw new IllegalArgumentException("unknow pad mode");
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < padLeft; i++) {
			sb.append(paddingChar);
		}
		sb.append(s);
		for (int i = 0; i < padRight; i++) {
			sb.append(paddingChar);
		}
		return sb.toString();
	}

	public static String getFixLengthStr(Object src, int length, char paddingChar) {
		return getFixLengthStr(src, length, paddingChar, PaddingMode.Right);
	}

	public static String getFixLengthStr(Object src, int length, PaddingMode paddingMode) {
		return getFixLengthStr(src, length, ' ', paddingMode);
	}

	public static String getFixLengthStr(Object src, int length) {
		return getFixLengthStr(src, length, ' ', PaddingMode.Right);
	}

	public enum CompareMode {
		Equal(0), StartWith(1), EndWith(2), Include(3);

		private int mCode;

		private CompareMode(int code) {
			mCode = code;
		}

		public int getCode() {
			return mCode;
		}

		public static CompareMode valueOf(int value) {
			switch (value) {
			case 0:
				return Equal;
			case 1:
				return StartWith;
			case 2:
				return EndWith;
			default:
				return Include;
			}
		}
	}

	public static boolean in(String s, Iterable<String> strs, CompareMode mode) {
		if (null == strs) {
			return false;
		}

		if (null == s) {
			return true;
		}
		if (s.equals("")) {
			return true;
		}

		for (String s1 : strs) {
			if (null == s1) {
				continue;
			}
			switch (mode) {
			case Equal:
				if (s.equals(s1)) {
					return true;
				}
				break;
			case StartWith:
				if (s.startsWith(s1)) {
					return true;
				}
				break;
			case EndWith:
				if (s.endsWith(s1)) {
					return true;
				}
				break;
			case Include:
				if (s.indexOf(s1) > 0) {
					return true;
				}
				break;
			default:
				break;
			}
		}
		return false;
	}

	/** utf-8编码 */
	public static final String M_ENCODE_TYPE_UTF_8 = "UTF-8";
	/** gb2312编码 */
	public static final String M_ENCODE_TYPE_GB2312 = "GB2312";
	/** gbk编码 */
	public static final String M_ENCODE_TYPE_GBK = "GBK";
	/** iso8859-1编码 */
	public static final String M_ENCODE_TYPE_ISO8859_1 = "ISO8859-1";

	private static final String[] mEncodeTypes = new String[] { M_ENCODE_TYPE_UTF_8, M_ENCODE_TYPE_GB2312,
			M_ENCODE_TYPE_GBK, M_ENCODE_TYPE_ISO8859_1 };

	/**
	 * 获取字符串的编码方式，目前支持utf-8, gb2312, gbk, iso8859-1
	 * 
	 * @param str
	 * @return 编码方式，可能为null
	 * @warning 此方法不能正常工作，后续再修 ?
	 */
	private static String getStrEncodeType(String str) {
		if (null == str) {
			FLogger.e("argument str is null, no encode type returned.");
			return null;
		}

		String res = null;
		for (String decType : mEncodeTypes) {
			boolean found = false;
			for (String encType : mEncodeTypes) {
				try {
					String tmp = new String(str.getBytes(decType), decType);
					if (tmp.equals(str)) {
						res = decType;
						found = true;
						break;
					}
				} catch (UnsupportedEncodingException e) {
					// do nothing
				}
			}

			if (found) {
				break;
			}
		}
		return res;
	}

	private static String M_PROP_NAME_LINE_SEPARATOR = "line.separator";
	private static String M_PROP_NAME_FILE_SEPARATOR = "file.separator";
	private static String M_PROP_NAME_PATH_SEPARATOR = "path.separator";

	public static String getPropValue(String propName) {
		String res = null;

		try {
			res = System.getProperty(propName);
		} catch (Exception e) {
			FLogger.Ex(e);
		}

		return res;
	}

	public static String getLineSeparator() {
		return getPropValue(M_PROP_NAME_LINE_SEPARATOR);
	}

	public static String getFileSeparator() {
		return getPropValue(M_PROP_NAME_FILE_SEPARATOR);
	}

	public static String getPathSeparator() {
		return getPropValue(M_PROP_NAME_PATH_SEPARATOR);
	}

	/**
	 * 分转 ?  ?多保 ?2位小 ?,如果 ?后小数为0则删 ? 1004 -> 10.04
	 * 
	 * @param price
	 * @return
	 */
	public static String formatMoney(int price) {
		double temp = price / 100.00;
		String format = String.format("%.2f", temp);
		StringBuilder stringBuilder = new StringBuilder(format);
		char lastChar1 = format.charAt(format.length() - 1);
		char lastChar2 = format.charAt(format.length() - 2);
		if (lastChar1 == '0') {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);// 删除 ?后第 ? ?0
			if (lastChar2 == '0') {
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);// 删除 ?后第二个0
				stringBuilder.deleteCharAt(stringBuilder.length() - 1);// 删除小数 ?
			}
		}

		return stringBuilder.toString();
	}

	static String ID_REG = "^(?![0-9]+$)[0-9A-Za-z]{6,20}$";
	static String PASS_REG = "[\u4e00-\u9fa5]";
	// 不能是汉 ? [^\u4e00-\u9fa5] //^[0-9A-Za-z^!#%&',;=+*/?$_-]{6,20}$

	static String MAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	static String PHONE = "^1[34578]\\d{9}$";

	public static boolean checkID(String msg) {
		Pattern pattern = Pattern.compile(ID_REG);
		Matcher matcher = pattern.matcher(msg);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	// 是否不包含中 ? false 包含 true 不包 ?
	public static boolean checkPass(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return false;
		}
		Pattern pattern = Pattern.compile(PASS_REG);
		Matcher matcher = pattern.matcher(msg);
		if (matcher.find()) {
			return false;
		}
		return true;
	}

	public static boolean checkMail(String msg) {
		if (TextUtils.isEmpty(msg)) {
			return false;
		}
		Pattern pattern = Pattern.compile(MAIL);
		Matcher matcher = pattern.matcher(msg);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	public static boolean checkPhone(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return false;
		}
		Pattern pattern = Pattern.compile(PHONE);
		Matcher matcher = pattern.matcher(phone);
		if (matcher.find()) {
			return true;
		}
		return false;
	}

	// 13100000000 处理 ?131***0
	public static String dealPhone(String phone) {
		if (TextUtils.isEmpty(phone)) {
			return "";
		}
		StringBuffer buffer = null;
		try {
			buffer = new StringBuffer();
			buffer.append(phone.charAt(0)).append(phone.charAt(1)).append(phone.charAt(2)).append("***")
					.append(phone.charAt(phone.length() - 4)).append(phone.charAt(phone.length() - 3))
					.append(phone.charAt(phone.length() - 2)).append(phone.charAt(phone.length() - 1));
		} catch (Exception e) {
			FLogger.Ex("gowan_utils", e);
			return "";
		}
		return buffer.toString();
	}

	//  ? 123545@qq.com 处理 ?12****5@qq.com
	public static String dealemail(String email) {
		if (TextUtils.isEmpty(email)) {
			return "";
		}
		StringBuffer buffer = null;
		try {
			buffer = new StringBuffer();
			String[] split = email.split("@");

			if (split[0].length() > 4) {
				buffer.append(split[0].charAt(0)).append(split[0].charAt(1));
				buffer.append("**");
				buffer.append(split[0].charAt(split[0].length() - 1));
			} else {
				buffer.append(split[0]);
			}
			buffer.append("@").append(split[1]);
		} catch (Exception e) {
			FLogger.Ex("gowan_utils", e);
			return "";
		}
		return buffer.toString();
	}
}
