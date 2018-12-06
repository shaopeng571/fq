package com.fqwl.hycommonsdk.util.http;
public enum HttpMode {
	Get(1), Post(2);

	private int Code;

	private HttpMode(int code) {
		this.Code = code;
	}

	public int getCode() {
		return this.Code;
	}

	@Override
	public String toString() {
		if(this.getCode() == Get.getCode()) {
			return "GET";
		} else {
			return "POST";
		}
	}
	
	
}