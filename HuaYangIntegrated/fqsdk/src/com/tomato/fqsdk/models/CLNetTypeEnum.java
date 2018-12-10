package com.tomato.fqsdk.models;

public enum CLNetTypeEnum {

	_3G(2), WIFI(5), _2G(1),Other(0),_4G(3);

	private final int value;

	private CLNetTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
}
