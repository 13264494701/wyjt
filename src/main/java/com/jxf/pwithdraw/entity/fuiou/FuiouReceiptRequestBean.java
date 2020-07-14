package com.jxf.pwithdraw.entity.fuiou;
/**
 * @description 富友收款凭证请求参数
 * @author wo
 *
 */
public class FuiouReceiptRequestBean extends FuiouRequestBean{
	/**
	 * 版本号
	 */
	private String VERSION;
	
	/**
	 * 商户代码
	 */
	private String MCHNTCD;

	/**
	 * 业务类型
	 */
	private String BUSICD;
	
	/**
	 * 商户流水号
	 */
	private String MCHNTORDERID;

	/**
	 * 签名方式
	 */
	private String SIGNTP;
	/**
	 * 摘要数据
	 */
	private String SIGN;
	
	
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}
	
	public String getMCHNTCD() {
		return MCHNTCD;
	}
	public void setMCHNTCD(String mCHNTCD) {
		MCHNTCD = mCHNTCD;
	}
	
	public String getBUSICD() {
		return BUSICD;
	}
	public void setBUSICD(String bUSICD) {
		BUSICD = bUSICD;
	}
	
	public String getMCHNTORDERID() {
		return MCHNTORDERID;
	}
	public void setMCHNTORDERID(String mCHNTORDERID) {
		MCHNTORDERID = mCHNTORDERID;
	}
	
	public String getSIGNTP() {
		return SIGNTP;
	}
	public void setSIGNTP(String sIGNTP) {
		SIGNTP = sIGNTP;
	}
	
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	
}
