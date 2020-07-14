package com.jxf.web.model.wyjt.app.auction;

/**
 * @作者: gaobo
 * @创建时间 :2019年3月6日 
 * @功能说明: 我买入的列表查询
 */
public class MinePurchaseListRequestParam {

	/** 每页记录数 */
	private Integer pageSize = 20;
	/** 页码(必传) */
	private Integer pageNo;
	
	private Integer unPayTab;//未还款 排序专用 默认0 不排序  1 正序 2 倒叙
	
	private Integer payedTab;//已还款 排序专用 默认0 不排序  1 正序 2 倒叙
	
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
	public Integer getUnPayTab() {
		return unPayTab;
	}
	public void setUnPayTab(Integer unPayTab) {
		this.unPayTab = unPayTab;
	}
	public Integer getPayedTab() {
		return payedTab;
	}
	public void setPayedTab(Integer payedTab) {
		this.payedTab = payedTab;
	}
	
	
}
