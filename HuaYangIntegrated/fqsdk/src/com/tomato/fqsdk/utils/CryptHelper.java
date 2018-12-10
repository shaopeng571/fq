package com.tomato.fqsdk.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;


public class CryptHelper {

	public CryptHelper() {
		// TODO Auto-generated constructor stub
	}

	// ȫ      
	private final static String[] strDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	//       ʽΪ   ָ  ַ   
	private static String byteToArrayString(byte bByte) {
		int iRet = bByte;
		// System.out.println("iRet="+iRet);
		if (iRet < 0) {
			iRet += 256;
		}
		int iD1 = iRet / 16;
		int iD2 = iRet % 16;
		return strDigits[iD1] + strDigits[iD2];
	}

//	//       ʽֻΪ    
//	private static String byteToNum(byte bByte) {
//		int iRet = bByte;
//		System.out.println("iRet1=" + iRet);
//		if (iRet < 0) {
//			iRet += 256;
//		}
//		return String.valueOf(iRet);
//	}

	// ת   ֽ     Ϊ16     ִ 
	private static String byteToString(byte[] bByte) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < bByte.length; i++) {
			sBuffer.append(byteToArrayString(bByte[i]));
		}
		return sBuffer.toString();
	}

	public static String GetMD5Code(String strObj) {
		String resultString = null;
		try {
			resultString = new String(strObj);
			MessageDigest md = MessageDigest.getInstance("MD5");
			// md.digest()  ú       ֵΪ  Ź ϣֵ     byte    
			resultString = byteToString(md.digest(strObj.getBytes()));
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return resultString;
	}

	public static String GetMD5Code(String strObj, int lenth) {
		String md5code = "";

		switch (lenth) {
		case 16:
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(strObj.getBytes());
				return getEncode16(digest);
			} catch (Exception e) {

			}
			break;
		case 32:
			try {
				MessageDigest digest = MessageDigest.getInstance("MD5");
				digest.update(strObj.getBytes());
				return getEncode32(digest);
			} catch (Exception e) {

			}
			break;
		default:
		}

		return md5code;
	}

	public static String GetMD5Code2(TreeMap<String, Object> treeMap, String k) {
		String str = "";
		Set<Entry<String, Object>> set = treeMap.entrySet();
		Iterator<Entry<String, Object>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			str += entry.getValue();
		}
		return GetMD5Code(str + k);
	}

	public static String GetMD5Code(TreeMap<String, String> treeMap, String k) {
		String str = "";
		Set<Entry<String, String>> set = treeMap.entrySet();
		Iterator<Entry<String, String>> iterator = set.iterator();
		while (iterator.hasNext()) {
			Entry<?, ?> entry = (Entry<?, ?>) iterator.next();
			str += entry.getValue();
		}
		return GetMD5Code(str + k);
	}
	/**
	 * 32λ    
	 * 
	 * @param digest
	 * @return
	 */
	private static String getEncode32(MessageDigest digest) {
		StringBuilder builder = new StringBuilder();
		for (byte b : digest.digest()) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString(b & 0xf));
		}
		// return builder.toString().toLowerCase(); // Сд
		// return builder.toString().toUpperCase(); //   д
		// java.lang.String.toUpperCase(Locale locale)
		//        ڴ  ַ    е      ַ Ϊ  д Ĺ        Locale.
		// return builder.toString().toUpperCase(Locale.getDefault()); //   д
		return builder.toString();
	}

	/**
	 * 16λ    
	 * 
	 * @param digest
	 * @return
	 */
	private static String getEncode16(MessageDigest digest) {
		StringBuilder builder = new StringBuilder();
		for (byte b : digest.digest()) {
			builder.append(Integer.toHexString((b >> 4) & 0xf));
			builder.append(Integer.toHexString(b & 0xf));
		}

		// 16λ   ܣ  ӵ 9λ  25λ
		// return builder.substring(8, 24).toString().toUpperCase();
		return builder.substring(8, 24).toString();
	}

}
