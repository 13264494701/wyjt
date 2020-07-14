package com.jxf.web.model.wyjt.app.auction;

/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 可转让的借条列表查询
 */
public class CrTransferListRequestParam {

	/** 页码(必传) */
	private Integer pageNo;
	
	/** 每页记录数 */
	private Integer pageSize = 20;
	
	private Integer overdueTab;//逾期天数 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer amountTab;//借款金额 排序专用 默认0 不排序  1 正序 2 倒叙
	
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getOverdueTab() {
		return overdueTab;
	}
	public void setOverdueTab(Integer overdueTab) {
		this.overdueTab = overdueTab;
	}
	public Integer getAmountTab() {
		return amountTab;
	}
	public void setAmountTab(Integer amountTab) {
		this.amountTab = amountTab;
	}
	
	
}
