package com.fqwl.hycommonsdk.model;

import java.io.Serializable;

import com.fqwl.hycommonsdk.util.ChannelConfigUtil;

import android.content.Context;

public class CommonSdkInitInfo implements Serializable {
/**
	 * 
	 */
	private static final long serialVersionUID = 878L;
	//	private boolean isLandScape;
//	private String gameId;
	private boolean debug=false;
	private String gameVersion;
	
//appversion, String GameName, String GameIcon
	public String getGameId(Context context) {
		return ChannelConfigUtil.getGameId(context);
	}
//
//	public void setGameId(String gameId) {
//		this.gameId = gameId;
//	}

	public String getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
