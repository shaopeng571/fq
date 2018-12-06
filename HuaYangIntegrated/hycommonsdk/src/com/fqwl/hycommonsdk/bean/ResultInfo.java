package com.fqwl.hycommonsdk.bean;

public class ResultInfo {

	public ResultInfo(){
		code = -1;
	}
	public int code;//0是成功，其他是错误。
	public String msg;
	public String data;
	public String time;
	@Override
	public String toString() {
		return "ResultInfo [code=" + code + ", msg=" + msg + ", data=" + data
				+ ", time="+ time + "]";
	}

}
