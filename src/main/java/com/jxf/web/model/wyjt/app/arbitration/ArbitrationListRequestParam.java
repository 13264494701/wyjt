package com.jxf.web.model.wyjt.app.arbitration;

public class ArbitrationListRequestParam   {
	
	/**
	 * 业务类型 
	 * 1待仲裁 2仲裁中 3 仲裁出裁决 4仲裁已结束
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
