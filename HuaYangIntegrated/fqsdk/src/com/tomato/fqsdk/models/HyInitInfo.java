package com.tomato.fqsdk.models;

public class HyInitInfo {

	private String gameId;
	private boolean debug=false;
	private String gameVersion;

//appversion, String GameName, String GameIcon
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

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
