package com.tomato.fqsdk.models;

public class CLPayResult  {
	private String sid="";
	private String uid = "";
	private String playerid = "";
	private String subject = "";
	private String detail = "";
	private double price = 0;
	private int quantity = 1;
	private double totalFee = 0.0;
	private String outRradeNo = "";
	private String payType = "";
	private String goodsid="";
	private String extra="";
	//     ģʽ
	private static CLPayResult rl;

	private CLPayResult() {

	}

	//   ȡʵ  
	public static CLPayResult getInstance() {
		synchronized (CLPayResult.class) {
			if (rl == null) {
				rl = new CLPayResult();
			}

		}
		return rl;
	}

	public static void clean() {
		rl = new CLPayResult();
	}
	public String getExtra() {                                                                                                                                                                                                                                              
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}
	public String getSid() {                                                                                                                                                                                                                                              
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getGoodsid() {
		return goodsid;
	}

	public void setGoodsid(String goodsid) {
		this.goodsid = goodsid;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getPlayerid() {
		return playerid;
	}

	public void setPlayerid(String playerid) {
		this.playerid = playerid;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public double getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(double totalFee) {
		this.totalFee = totalFee;
	}

	public String getOutRradeNo() {
		return outRradeNo;
	}

	public void setOutRradeNo(String outRradeNo) {
		this.outRradeNo = outRradeNo;
	}


//	/**
//	 *     ֧      
//	 */
//	public void setPay(String uid, String playerid, String subject,
//			String detail, double totalFee, String notify,
//			String extra) {
//		this.uid = uid;
//		this.playerid = playerid;
//		this.subject = subject;
//		this.detail = detail;
//		this.totalFee = totalFee;
//		this.notify = notify;
//		this.extra = extra;
//	}

	/**
	 *     ֧      
	 */
	public void setPay(String uid, String playerid, String subject,
			String detail, double totalFee, int quantity, double price,
			String goodsid,String sid,String extra) {
		this.uid = uid;
		this.playerid = playerid;
		this.subject = subject;
		this.detail = detail;
		this.totalFee = totalFee;
		this.quantity = quantity;
		this.price = price;
		this.goodsid=goodsid;
		this.sid=sid;
		this.extra=extra;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "uid:"+uid+" playerid:"+playerid+" subject:"+subject
				+" detail:"+detail+" price:"+price+" quantity:"+quantity+" totalFee:"+totalFee
				+" outRradeNo:"+outRradeNo+" payType:"+payType+" goodsid:"+goodsid+" extra:"+extra;
	}
}
