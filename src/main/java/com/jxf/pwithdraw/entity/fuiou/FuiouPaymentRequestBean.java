package com.jxf.pwithdraw.entity.fuiou;
/**
 * @description 富友代付请求参数
 * @author Administrator
 *
 */
public class FuiouPaymentRequestBean extends FuiouRequestBean{
	/**
	 * 版本号
	 */
	private String ver;
	/**
	 * 请求日期
	 */
	private String merdt;
	/**
	 * 请求流水
	 */
	private String orderno;
	/**
	 * 账号
	 */
	private String accntno;
	/**
	 * 账户名称 
	 */
	private String accntnm;
	/**
	 * 金额 单位：分，单笔最少3元
	 */
	private String amt;
	/**
	 * 总行代码
	 */
	private String bankno;
	/**
	 * 城市代码
	 */
	private String cityno;
	/**
	 * 支行名称
	 */
	private String branchnm;
	/**
	 * 企业流水号
	 */
	private String entseq;
	/**
	 * 备注
	 */
	private String memo;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 是否返回交易状态类别 值为1：需要返回，其他不需要
	 */
	private String addDesc;
	
	
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
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
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getCityno() {
		return cityno;
	}
	public void setCityno(String cityno) {
		this.cityno = cityno;
	}
	public String getBranchnm() {
		return branchnm;
	}
	public void setBranchnm(String branchnm) {
		this.branchnm = branchnm;
	}
	public String getEntseq() {
		return entseq;
	}
	public void setEntseq(String entseq) {
		this.entseq = entseq;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddDesc() {
		return addDesc;
	}
	public void setAddDesc(String addDesc) {
		this.addDesc = addDesc;
	}
	
}
