package com.jxf.web.model.wyjt.app.member;


/**
 * @作者: xiaorongdian
 * @创建时间 :2018年11月1日 上午9:37:24
 * @功能说明:身份认证数据
 */
public class IdentityInfoResponseResult {

	/** 真实姓名 */
	private String name;
	/** 身份证号 */
	private String idNo;
	/**实名认证*/
	private Integer realIdentityStatus;//0->未认证,1->认证
	

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Integer getRealIdentityStatus() {
		return realIdentityStatus;
	}
	public void setRealIdentityStatus(Integer realIdentityStatus) {
		this.realIdentityStatus = realIdentityStatus;
	}
}
