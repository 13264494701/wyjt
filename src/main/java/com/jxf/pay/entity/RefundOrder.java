package com.jxf.pay.entity;


/**
 * 
 *
 * @author 微信支付退款
 */
public class RefundOrder {

	
	private String appid;		// 公众账号ID
	private String mch_id;		// 商户号
	private String nonce_str;		// 随机字符串
	private String sign;		// 签名
	private String sign_type;		// 签名类型
	private String out_trade_no;		// 商户订单号
	private String out_refund_no;		// 商户退款单号

	private int total_fee;		// 订单金额
	private int refund_fee;		// 退款金额
	private String refund_fee_type; //货币种类
	private String refund_desc;		// 退款原因
	
	private String refund_account; //退款资金来源
	private String notify_url;		// 通知地址



	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getMch_id() {
		return mch_id;
	}
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public int getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}




	public String getOut_refund_no() {
		return out_refund_no;
	}
	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}
	public int getRefund_fee() {
		return refund_fee;
	}
	public void setRefund_fee(int refund_fee) {
		this.refund_fee = refund_fee;
	}
	public String getRefund_fee_type() {
		return refund_fee_type;
	}
	public void setRefund_fee_type(String refund_fee_type) {
		this.refund_fee_type = refund_fee_type;
	}
	public String getRefund_desc() {
		return refund_desc;
	}
	public void setRefund_desc(String refund_desc) {
		this.refund_desc = refund_desc;
	}
	public String getRefund_account() {
		return refund_account;
	}
	public void setRefund_account(String refund_account) {
		this.refund_account = refund_account;
	}


	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	
}
