package com.fqwl.hycommonsdk.model;

import java.io.Serializable;

import com.tomato.fqsdk.models.HyPayInfo;

import android.text.TextUtils;


public class CommonSdkChargeInfo implements Serializable{
	private static final long serialVersionUID = 16L;
	private String server_id = "";
	private String goods_name = "";
	private String goods_desc = "";
	private int goods_count = 1;
	private double money = 0.0;
//	private String notify = "";
	private String extra;
	private String goods_id = "";

	// ========================
	private String cp_order_id;
	private String role_id;
	private String role_name;
	private String role_level;
	private String vip_level;
	private String server_name;
	private String goods_type;
	private String game_coin;

	
	//======================
	private String order;
	private boolean state;
	private String msg;//服务器返回的订单msg
	
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCp_order_id() {
		return cp_order_id;
	}

	public void setCp_order_id(String cp_order_id) {
		this.cp_order_id = cp_order_id;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

//	public HyPayInfo(HyPayInfo paramPayRequest) {
//		this.sid = paramPayRequest.getSid();
//		this.uid = paramPayRequest.getUid();
//		this.playerid = paramPayRequest.getPlayerid();
//		this.subject = paramPayRequest.getSubject();
//		this.detail = paramPayRequest.getDetail();
//		this.totalFee = paramPayRequest.getTotalFee();
//		this.quantity = paramPayRequest.getQuantity();
//		this.price = paramPayRequest.getPrice();
////	    this.notify = paramPayRequest.getNotify();
//		this.extra = paramPayRequest.getExtra();
//		this.goodsid = paramPayRequest.getGoodsid();
//	}



//		public String getNotify() {
//			return notify;
//		}
//		public HJPayRequest setNotify(String notify) {
//			this.notify = notify;
//			return this;
//		}
	public String getExtra() {
		return extra;
	}

	public CommonSdkChargeInfo setExtra(String extra) {
		this.extra = extra;
		return this;
	}







	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_desc() {
		return goods_desc;
	}

	public void setGoods_desc(String goods_desc) {
		this.goods_desc = goods_desc;
	}

	public int getGoods_count() {
		return goods_count;
	}

	public void setGoods_count(int goods_count) {
		this.goods_count = goods_count;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(String goods_id) {
		this.goods_id = goods_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public String getRole_level() {
		return role_level;
	}

	public void setRole_level(String role_level) {
		this.role_level = role_level;
	}

	public String getVip_level() {
		return vip_level;
	}

	public void setVip_level(String vip_level) {
		this.vip_level = vip_level;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		this.server_name = server_name;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getGame_coin() {
		return game_coin;
	}

	public void setGame_coin(String game_coin) {
		this.game_coin = game_coin;
	}
	

}
