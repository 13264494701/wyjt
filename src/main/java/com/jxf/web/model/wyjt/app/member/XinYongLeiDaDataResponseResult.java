package com.jxf.web.model.wyjt.app.member;

public class XinYongLeiDaDataResponseResult {
	/**
	 * 报告人姓名
	 */
	private  String name;
	/**
	 * 报告人时间
	 */
	private  String createTime;
	/**
	 * 编号
	 */
	private String  bianNum;
	/**
	 * 身份证号
	 */
	private  String idcard;
	
	
	/**
	 * 申请雷达 多个字段中间用 | 分割
	 */
	private String applyLeiDa;
	
	/**
	 * 行为雷达  多个字段中间用 | 分割
	 */
	private String behaviorDa;
	
	
	/**
	 * 信用雷达  多个字段中间用 | 分割
	 */
	private String creditLeiDa;
	
	/**
	 * 解释内容
	 */
	private String note;


	public String getNote() {
		return note;
	}


	public void setNote(String note) {
		this.note = note;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public String getBianNum() {
		return bianNum;
	}


	public void setBianNum(String bianNum) {
		this.bianNum = bianNum;
	}


	public String getIdcard() {
		return idcard;
	}


	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}


	public String getApplyLeiDa() {
		return applyLeiDa;
	}


	public void setApplyLeiDa(String applyLeiDa) {
		this.applyLeiDa = applyLeiDa;
	}


	public String getBehaviorDa() {
		return behaviorDa;
	}


	public void setBehaviorDa(String behaviorDa) {
		this.behaviorDa = behaviorDa;
	}


	public String getCreditLeiDa() {
		return creditLeiDa;
	}


	public void setCreditLeiDa(String creditLeiDa) {
		this.creditLeiDa = creditLeiDa;
	}
	

}
