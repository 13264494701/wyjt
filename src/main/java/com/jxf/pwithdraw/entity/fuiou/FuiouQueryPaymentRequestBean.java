package com.jxf.pwithdraw.entity.fuiou;

/**
 * @description 富友代付交易结果查询请求
 * @author SuHuimin
 */
public class FuiouQueryPaymentRequestBean extends FuiouRequestBean{
	/**
	 * 版本号 查询请求版本号为1.1
	 */
	private String ver;
	/**
	 * 业务代码  代付：AP01
	 */
	private String busicd;
	/**
	 * 原请求流水
	 */
	private String orderno;
	/**
	 * 开始日期 
	 */
	private String startdt;
	/**
	 * 结束日期
	 */
	private String enddt;
	/**
	 * 交易状态
	 */
	private String transst;
	/**
	 * 交易来源 接口交易值为：HMP
	 */
	private String srcModuleCd;
	
	
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getBusicd() {
		return busicd;
	}
	public void setBusicd(String busicd) {
		this.busicd = busicd;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getStartdt() {
		return startdt;
	}
	public void setStartdt(String startdt) {
		this.startdt = startdt;
	}
	public String getEnddt() {
		return enddt;
	}
	public void setEnddt(String enddt) {
		this.enddt = enddt;
	}
	public String getTransst() {
		return transst;
	}
	public void setTransst(String transst) {
		this.transst = transst;
	}
	public String getSrcModuleCd() {
		return srcModuleCd;
	}
	public void setSrcModuleCd(String srcModuleCd) {
		this.srcModuleCd = srcModuleCd;
	}
}
