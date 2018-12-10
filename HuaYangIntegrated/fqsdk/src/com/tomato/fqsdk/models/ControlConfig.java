package com.tomato.fqsdk.models;

import android.content.Context;

public class ControlConfig {

	public static final int SCREEN_ORIENTATION_PORTRAIT = 1;
	public static final int SCREEN_ORIENTATION_LANDSCAPE = 0;
	
	private Context context;
	private int orientation=-1;
	private String appId =""; //   ϷID
	private String appVersion ="v1.0";
	
	private String gameRegion ="1";
	private String channel ="1"; //        
	
	public Context getAppContext() {return this.context;}
	public int getOrientation() {return this.orientation;}
	public String getAppId() { return appId ; }
	public String getAppVersion() { return appVersion ; }
	
	public String getGameRegion() { return gameRegion ; }
	public String getChannel() { return channel ; }
	
	public ControlConfig(Builder paramBuilder) {
		
		this.context = paramBuilder.context;
		this.appId = paramBuilder.appId;
		this.appVersion = paramBuilder.appVersion;
		this.gameRegion = paramBuilder.gameRegion;
		this.orientation = paramBuilder.orientation;
		this.channel = paramBuilder.channel;
		
	}

	public static class Builder {
		Context context;
		int orientation=-1;
		String appId =""; //   ϷID
	    String appVersion ="v1.0";
		String gameRegion ="1";
		String channel =""; //        

		public Builder setOrientation(int orientation) {
			if ((orientation == 0) || (orientation == 1))
				this.orientation = orientation;
			return this;
		}

		public Builder setAppId(String appId) {
			this.appId = appId;
			return this;
		}
		
		
		public Builder setChannel(String channel) {
			this.channel = channel;
			return this;
		}

		public Builder setAppVersion(String appVersion) {
			this.appVersion = appVersion;
			return this;
		}
		
		public Builder setGameRegion(String gameRegion) {
			this.gameRegion = gameRegion;
			return this;
		}

		public Builder(Context context) {
				 this.context =context.getApplicationContext();
		}

		public ControlConfig build() {
			return new ControlConfig(this);
		}
	}

	public static enum PopWinPosition {
		POS_LEFT, POS_BOTTOM, POS_RIGHT, POS_TOP;
	}

}
