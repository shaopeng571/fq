package com.huayang.utils.encode;

import android.text.TextUtils;
import android.util.Base64;

public class Base64Encoder implements IByteEncoder {

	private  static volatile Base64Encoder instance = null;

	private Base64Encoder() {
	}

	public static Base64Encoder getInstance() {
		if (instance == null) {
			synchronized (Base64Encoder.class) {
				if (instance == null) {
					instance = new Base64Encoder();
				}
			}
		}
		return instance;
	}

  @Override
  public String encode(byte[] bytes) {
		if(bytes == null || bytes.length == 0){
			return null;
		}
		return Base64.encodeToString(bytes, Base64.DEFAULT);
  }

	@Override
  public byte[] decode(String str) {
		if(TextUtils.isEmpty(str)){
			return null;
		}
		return Base64.decode(str, Base64.DEFAULT);
	}

}
