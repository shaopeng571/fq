package com.fqwl.hycommonsdk.model;

import java.util.List;


import android.text.TextUtils;

public class CommonSdkExtendData {
	private String roleId = "";
	private String roleName = "";
	private String roleLevel = "";
	private String roleLevel_begin="";
	private String serverName = "";
	private String serverId = "";
	private String userMoney = "";
	private String vipLevel = "";
	private String user_id="";
	private String party_name="";
	private String roleCTime = "";//角色创建时间(单位：秒 即10位数)，必须传服务器时间
	private String roleLevelMTime = "";//角色等级变化时间(单位：秒 即10位数)，必须传服务器时间
	private String game_version;//不是必须参数
	
	public String getGame_version() {
		return game_version;
	}

	public void setGame_version(String game_version) {
		this.game_version = game_version;
	}

	public String getRoleCTime() {
		return roleCTime;
	}

	public void setRoleCTime(String roleCTime) {
		this.roleCTime = roleCTime;
	}

	public String getRoleLevelMTime() {
		return roleLevelMTime;
	}

	public void setRoleLevelMTime(String roleLevelMTime) {
		this.roleLevelMTime = roleLevelMTime;
	}

	public String getServerName() {
		return serverName;
	}

	public String getParty_name() {
		return party_name;
	}

	public void setParty_name(String party_name) {
		this.party_name = party_name;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleLevel() {
		return roleLevel;
	}

	public void setRoleLevel(String roleLevel) {
		this.roleLevel = roleLevel;
	}

	public String getRoleLevel_begin() {
		return roleLevel_begin;
	}

	public void setRoleLevel_begin(String roleLevel_begin) {
		this.roleLevel_begin = roleLevel_begin;
	}


	public void setServerName(String servceName) {
		this.serverName = servceName;
	}

	public String getServerId() {
		return serverId;
	}

	public void setServerId(String servceId) {
		this.serverId = servceId;
	}

	public String getUserMoney() {
		return userMoney;
	}

	public void setUserMoney(String userMoney) {
		this.userMoney = userMoney;
	}

//	@Override
//	public String toString() {
//		JSONObject obj = new JSONObject();
//		try {
//			obj.put("roleId", roleId);
//			obj.put("roleName", roleName);
//			obj.put("serverName", serverName);
//			obj.put("serverId", serverId);
//			obj.put("userMoney", userMoney);
//			obj.put("vipLevel", vipLevel);
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		return obj.toString();
//	}

	public String getVipLevel() {
		return vipLevel;
	}

	public void setVipLevel(String vipLevel) {
		this.vipLevel = vipLevel;
	}
	
	

}
