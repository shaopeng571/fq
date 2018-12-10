package com.tomato.fqsdk.models;

public class HyLoginResult {
	private static HyLoginResult rl;

	private HyLoginResult() {

	}

	public static HyLoginResult getInstance() {
		synchronized (HyLoginResult.class) {
			if (rl == null)
				rl = new HyLoginResult();
		}
		return rl;
	}

	private String uid = "";
	private String Account = "";
	private String TokenKey = "";
	private String behavior;

	public String getBehavior() {
		return behavior;
	}

	public void setBehavior(String behavior) {
		this.behavior = behavior;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getTokenKey() {
		return TokenKey;
	}

	public void setTokenKey(String tokenKey) {
		TokenKey = tokenKey;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "uid:" + uid + " Account:" + Account + " TokenKey:" + TokenKey + " behavior:" + behavior;
	}

}
