package com.jxf.web.model.wyjt.app.act;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * 用户充值返回实体类
 * @author suhuimin
 *
 */
public class ActRechargeResponseResult  {
	/**
	 * 用户ID
	 */
	@JSONField(name="USERID")
	private String USERID;
	/**
	 * 证件类型  0 身份证
	 */
	@JSONField(name="IDTYPE")
	private String IDTYPE;
	/**
	 * 证件号码
	 */
	@JSONField(name="IDNO")
	private String IDNO;
	/**
	 * 商户号
	 */
	@JSONField(name="MCHNTCD")
	private String MCHNTCD;
	/**
	 * 商户订单号
	 */
	@JSONField(name="MCHNTORDERID")
	private String MCHNTORDERID;
	/**
	 * 银行名称
	 */
	@JSONField(name="BANKNAME")
	private String BANKNAME;
	/**
	 * 银行卡号
	 */
	@JSONField(name="BANKCARD")
	private String BANKCARD;
	/**
	 * 充值类型 02
	 */
	@JSONField(name="TYPE")
	private String TYPE;
	/**
	 * 签名类型 MD5
	 */
	@JSONField(name="SIGNTP")
	private String SIGNTP;
	/**
	 * 富友回调地址
	 */
	@JSONField(name="BACKURL")
	private String BACKURL;
	/**
	 * 版本号 2.0
	 */
	@JSONField(name="VERSION")
	private String VERSION;
	/**
	 * 充值金额
	 */
	@JSONField(name="AMT")
	private String AMT;
	/**
	 * 用户姓名
	 */
	@JSONField(name="NAME")
	private String NAME;
	/**
	 * 时间戳
	 */
	@JSONField(name="TIME")
	private String TIME;
	/**
	 * 签名
	 */
	@JSONField(name="SIGN")
	private String SIGN;
	
	
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getIDTYPE() {
		return IDTYPE;
	}
	public void setIDTYPE(String iDTYPE) {
		IDTYPE = iDTYPE;
	}
	public String getIDNO() {
		return IDNO;
	}
	public void setIDNO(String iDNO) {
		IDNO = iDNO;
	}
	public String getMCHNTCD() {
		return MCHNTCD;
	}
	public void setMCHNTCD(String mCHNTCD) {
		MCHNTCD = mCHNTCD;
	}
	public String getMCHNTORDERID() {
		return MCHNTORDERID;
	}
	public void setMCHNTORDERID(String mCHNTORDERID) {
		MCHNTORDERID = mCHNTORDERID;
	}
	public String getBANKNAME() {
		return BANKNAME;
	}
	public void setBANKNAME(String bANKNAME) {
		BANKNAME = bANKNAME;
	}
	public String getBANKCARD() {
		return BANKCARD;
	}
	public void setBANKCARD(String bANKCARD) {
		BANKCARD = bANKCARD;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getSIGNTP() {
		return SIGNTP;
	}
	public void setSIGNTP(String sIGNTP) {
		SIGNTP = sIGNTP;
	}
	public String getBACKURL() {
		return BACKURL;
	}
	public void setBACKURL(String bACKURL) {
		BACKURL = bACKURL;
	}
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}
	public String getAMT() {
		return AMT;
	}
	public void setAMT(String aMT) {
		AMT = aMT;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getTIME() {
		return TIME;
	}
	public void setTIME(String tIME) {
		TIME = tIME;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	
}
