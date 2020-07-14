package com.jxf.web.model.wyjt.app.member;

public class CredictBaoGaoRecodeRequestParam   {
	
	/**
	 * 业务类型 
	 * @param type  0：全部 1：免费   2一元
	 */
	private int type;
	
	/** 页码 */
	private Integer pageNo;

	/** 每页大小 */
	private Integer pageSize = 20;



	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
	
}
