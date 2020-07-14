package com.jxf.web.model.wyjt.app.execution;

public class ArbitrationExecutionListRequestParam   {
	
	/**
	 * 业务类型 
	 * 1可申请 2待受理 3 待缴费 4强执中 5强执结束
	 */
	private String type;
	
	/** 页码 */
	private Integer pageNo;

	/** 每页大小 */
	private Integer pageSize = 20;

	public String getType() {
		return type;
	}

	public void setType(String type) {
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
