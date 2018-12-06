package com.fqwl.hycommonsdk.util.http;

/**
 * 连接状态
 * @author fan
 */
public enum HttpAccessStatus {
	
	Timeout(-2),Error( -1), NotDone(0), Done(1);

	private int Code;

	private HttpAccessStatus(int code) {
		this.Code = code;
	}

	public int getCode() {
		return this.Code;
	}

	public static HttpAccessStatus valueOf(int value) {
		switch (value) {
		case -1:
			return Error;
		case 0:
			return NotDone;
		case 1:
			return Done;
		default:
			return null;
		}
	}

}
