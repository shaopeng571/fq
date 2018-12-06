package com.fqwl.hycommonsdk.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataEnCoderUtil {
	private final static String KEY = "huayangwangluo";
	private final static Pattern PATTERN = Pattern.compile("\\d+");

	public static String encode(String src) {
		try {
			byte[] data = src.getBytes("utf-8");
			byte[] keys = KEY.getBytes();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < data.length; i++) {
				int n = (0xff & data[i]) + (0xff & keys[i % keys.length]);
				sb.append("%" + n);
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return src;
	}

	public static String decode(String src) {
		if (src == null || src.length() == 0) {
			return src;
		}
		Matcher m = PATTERN.matcher(src);
		List<Integer> list = new ArrayList<Integer>();
		while (m.find()) {
			try {
				String group = m.group();
				list.add(Integer.valueOf(group));
			} catch (Exception e) {
				e.printStackTrace();
				return src;
			}
		}

		if (list.size() > 0) {
			try {
				byte[] data = new byte[list.size()];
				byte[] keys = KEY.getBytes();

				for (int i = 0; i < data.length; i++) {
					data[i] = (byte) (list.get(i) - (0xff & keys[i
							% keys.length]));
				}
				return new String(data, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return src;
		} else {
			return src;
		}
	}
}
