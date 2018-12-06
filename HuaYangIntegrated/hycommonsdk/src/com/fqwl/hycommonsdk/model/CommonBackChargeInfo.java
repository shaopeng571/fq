package com.fqwl.hycommonsdk.model;

public class CommonBackChargeInfo {
	public static final int status_success = 0;
	  public static final int status_fail = -1;
	  public static final int status_error = -2;
	  public static final String success = "充值流程完成";
	  public static final String fail = "充值流程未完成";
	  public int statusCode;
	  public String desc;

	  public String toString()
	  {
	    return "{  \"statusCode\":  "+ "\"" + this.statusCode+ "\"" + ", \"desc\":"+ "\"" + this.desc + "\""+ "}";
	  }
}
