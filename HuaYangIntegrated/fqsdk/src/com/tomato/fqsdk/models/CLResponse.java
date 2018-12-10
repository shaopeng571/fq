package com.tomato.fqsdk.models;

import java.io.Serializable;

public class CLResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	  private String resultCode;
	  private String description;
	  
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	  
}
