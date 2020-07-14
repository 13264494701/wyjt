package com.jxf.pwithdraw.entity.fuiou;
/**
 * @description this bean is used to load data for fuiou withdraw request,query
 * @author Administrator
 *
 */
public class FuiouRequestBean{
	/**
	 * 商户号
	 */
	private String merid;
	/**
	 * 请求类型
	 */
	private String reqtype;
	/**
	 * 请求参数
	 */
	private String xml;
	/**
	 * 校验值
	 */
	private String mac;
	
	public String getMerid() {
		return merid;
	}
	public void setMerid(String merid) {
		this.merid = merid;
	}
	public String getReqtype() {
		return reqtype;
	}
	public void setReqtype(String reqtype) {
		this.reqtype = reqtype;
	}
	public String getXml() {
		return xml;
	}
	public void setXml(String xml) {
		this.xml = xml;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	
}
