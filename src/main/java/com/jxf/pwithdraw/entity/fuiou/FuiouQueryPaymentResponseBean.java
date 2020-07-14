package com.jxf.pwithdraw.entity.fuiou;

/**
 * 实时付款交易结果查询response bean
 * 
 */
public class FuiouQueryPaymentResponseBean{
	//响应码
	private String ret;
	//响应描述
	private String memo;
	//原请求日期
	private String merdt;
	//原请求单号
	private String orderno;
	//银行卡号
	private String accntno;
	//持卡人姓名
	private String accntnm;
	//交易金额
	private String amt;
	//交易状态
	private String state;
	//交易结果
	private String result;
	//结果原因
	private String reason;
	//是否退款
	private String tpst;
	//银行响应码
	private String rspcd;
	//交易状态类别
	private String transStatusDesc;
	
	
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMerdt() {
		return merdt;
	}
	public void setMerdt(String merdt) {
		this.merdt = merdt;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getAccntno() {
		return accntno;
	}
	public void setAccntno(String accntno) {
		this.accntno = accntno;
	}
	public String getAccntnm() {
		return accntnm;
	}
	public void setAccntnm(String accntnm) {
		this.accntnm = accntnm;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTpst() {
		return tpst;
	}
	public void setTpst(String tpst) {
		this.tpst = tpst;
	}
	public String getRspcd() {
		return rspcd;
	}
	public void setRspcd(String rspcd) {
		this.rspcd = rspcd;
	}
	public String getTransStatusDesc() {
		return transStatusDesc;
	}
	public void setTransStatusDesc(String transStatusDesc) {
		this.transStatusDesc = transStatusDesc;
	}
	
}
