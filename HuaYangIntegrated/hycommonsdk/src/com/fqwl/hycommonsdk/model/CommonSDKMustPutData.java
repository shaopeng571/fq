package com.fqwl.hycommonsdk.model;

public class CommonSDKMustPutData {
	private CommonSDKMustPutData() {}
	private static CommonSDKMustPutData sdkdata;
	public static CommonSDKMustPutData getInstance() {
		if (sdkdata==null) {
			sdkdata=new CommonSDKMustPutData();
		}
		return sdkdata;
	}
	
	private String gameVersion;
	public String getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}
	
}
