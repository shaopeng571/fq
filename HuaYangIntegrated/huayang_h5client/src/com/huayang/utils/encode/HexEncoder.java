package com.huayang.utils.encode;



import com.fqwl.hycommonsdk.util.logutils.FLogger;

import android.text.TextUtils;

/**
 * @Description: 将byte数组进行十六进制编码转换为字符串
 * 
 */
public class HexEncoder implements IByteEncoder {

	private  final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

	private  final char[] DIGITS_UPPER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };

	private volatile static HexEncoder instance = null;

	private HexEncoder() {
	}

	public static HexEncoder getInstance() {
		
		if (instance == null) {
			synchronized (HexEncoder.class) {
				if (instance == null) {
					instance = new HexEncoder();
				}
			}
		}
		
		return instance;
	}

	@Override
	public String encode(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return new String(encodeHex(bytes, DIGITS_LOWER));
	}
	
	@Override
	public byte[] decode(String str) {
		if (TextUtils.isEmpty(str)) {
			return null;
		}
		return decodeHex(str.toCharArray());
	}

	private  byte[] decodeHex(char[] data)   {

		final int len = data.length;

		if ((len & 0x01) != 0) {
			FLogger.e("Odd number of characters.");
			return null;
		}

		final byte[] out = new byte[len >> 1];

		// two characters form the hex value.
		for (int i = 0, j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f = f | toDigit(data[j], j);
			j++;
			out[i] = (byte) (f & 0xFF);
		}

		return out;
	}
	
	
	protected  char[] encodeHex(byte[] data, char[] toDigits) {
		final int l = data.length;
		final char[] out = new char[l << 1];
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
			out[j++] = toDigits[0x0F & data[i]];
		}
		return out;
	}

	/**
	 * Converts a hexadecimal character to an integer.
	 *
	 * @param ch
	 *            A character to convert to an integer digit
	 * @param index
	 *            The index of the character in the source
	 * @return An integer
	 * @throws Exception 
	 */
	protected  int toDigit(final char ch, final int index) {
		
		final int digit = Character.digit(ch, 16);
		if (digit == -1) {
			FLogger.e("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}
	/** 
     * 将字节数组换成成16进制的字符串 
     *  
     * @param byteArray 
     * @return 
     */  
    private static String byteArrayToHex(byte[] byteArray) {  
        char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
                'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] resultCharArray = new char[byteArray.length * 2];  
        int index = 0;  
        for (byte b : byteArray) {  
            resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];  
            resultCharArray[index++] = hexDigits[b & 0xf];  
        }  
        return new String(resultCharArray);  
    }  
	
	
}
